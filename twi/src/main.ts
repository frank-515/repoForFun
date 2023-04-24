import { Request, Response } from "express";
import { request } from "http";
import { connect } from "http2";
import { Connection, MysqlError, Pool, PoolConnection, Query, queryCallback } from "mysql";

const express = require('express')
const { Request, Response } = require('express');
type Req = typeof Request
type Rsp = typeof Response
const bodyParser = require('body-parser')
const rawBody = require('raw-body')
const multer = require('multer');
const cookieParser = require('cookie-parser');
const mysql = require('mysql');
const crypto = require('crypto');
const fs = require('fs');
const path = require('path');

const upload = multer();
const app = express()

const imagePath = path.join(__dirname, '../uploads')

function sha256(str: string): string {
  const buffer = Buffer.from(str, 'utf8');
  return crypto.createHash('sha256').update(buffer).digest('hex');
}

const pool: Pool = mysql.createPool({
  connectionLimit: 10, // 连接池大小
  host: 'localhost',
  user: 'frank515',
  password: 'frank515',
  database: 'twi',
});

function generateUUID() {
  const buf = crypto.randomBytes(16);
  buf[6] = (buf[6] & 0x0f) | 0x40;  // 设置 UUID 版本为 4
  buf[8] = (buf[8] & 0x3f) | 0x80;  // 设置 UUID 变体为 RFC 4122 定义的变体
  return buf.toString('hex').match(/(.{8})(.{4})(.{4})(.{4})(.{12})/).slice(1).join('-');
}

// 使用 body-parser 中间件来解析 POST 请求体
app.use(bodyParser.urlencoded({ extended: true }))
app.use(bodyParser.json())
app.use(cookieParser());
app.use(express.json());
app.use(express.static(path.join(__dirname, '../public')));

// POST `password` `username` `user_id`
// RESPONSE 'success'
app.post('/api/register', upload.none(), (req: Request, res: Response) => {
  pool.getConnection(async function(err: any, connection:any ){
    await connection.query(
      'INSERT INTO users (user_id, password, username) VALUES (?, ?, ?)',
      [req.body.user_id, sha256(req.body.password), req.body.username ? req.body.username : "Select your username"],
      function(err: any, result: any) {
        if (err) {
          // console.log(err.message);
          res.send('failed: Duplicate');
        } else {
          connection.release();
          res.send('success')
        }
      }
    );
    
   });
})
// POST user_id password,
// REQUEST 'success' or 'failure'
app.post('/api/login', upload.none(), (req: Req, res: Rsp) => {
  let password: string = 'null';
  pool.getConnection((err: any, connection: any) => {
    connection.query(
      'SELECT * FROM users WHERE user_id = ?', 
      [req.body.user_id],
      (error: any, results: any, fields: any) => {
        connection.release();
        if (error) console.log(error.message);
        if (results[0]) password = results[0].password;
        if (password && password === sha256(req.body.password)) {
          set_login_credentials(req, res)          
        } else {
          res.send('failure')
        }
      }
    )
  })
})


function set_login_credentials(req: Req, res: Rsp) {
  
  const login_credentials: string = generateUUID()
  const today = new Date();
  const tomorrow = new Date(today.getTime() + (24 * 60 * 60 * 1000));

  pool.getConnection(async (err:any, connection: any) => {
    await connection.query(
      'INSERT INTO login_credentials (token, issue_time, expiry_time, user_id, login_ip) VALUES (?, ?, ?, ?, ?)',
      [login_credentials, today, tomorrow, req.body.user_id, req.ip],
      (err: any, results: any, fields: any) => {
        if (err) {
          console.log('unable to set_login_credentials' + err.message)
        } else {
          res.cookie('login_credentials', login_credentials)          
          res.cookie('user_id', req.body.user_id).send('success')
        }
        connection.release();
      })
  })

}

const authenticateUser = (req: Request, res: Response, next: any) => {
  if (req.body.isAuthenticated) {
    req.body.isAuthenticated = false
  }
  pool.getConnection((err: MysqlError, connection: PoolConnection) => {
    let expiry_time :Date = new Date();
    if (err) {
      console.log(err.message);
    }
    connection.query(
      'SELECT MAX(expiry_time) expiry_time FROM login_credentials WHERE user_id = ?',
      [req.cookies.user_id],
      (err: any, results: any, fields: any) => {
        expiry_time = new Date(results[0].expiry_time)
        connection.release();
        if (new Date().getTime() - expiry_time.getTime() < 0) {
          req.body.isAuthenticated = true;
          req.body.user_id = req.cookies.user_id
          next();
        } else {
          res.send("Authentication failed")
        }
        
      }
    )
  })
  
};

app.use('/a', authenticateUser) // static hosting 
app.use('/api/a', authenticateUser) // APIs

// 记录所有访问
app.use('/', (req: Request, res: Response, next: any) => {
  const { ip, method, originalUrl, headers } = req;
  const userAgent = headers['user-agent'];
  const timestamp = new Date().toISOString();
  const queryString = `INSERT INTO server_access_log (ip_address, request_url, http_method, user_agent) VALUES (?, ?, ?, ?);`;

  pool.query(queryString, [ip, originalUrl, method, userAgent], (err) => {
    if (err) {
      console.error(err);
    } else {
      console.log(`[${timestamp}] IP: ${ip}, URL: ${method} ${originalUrl}`);
    }
  });
  next();
});


app.post('/api/a/post', upload.none(), (req: Request, res: Response) => {
  let prev_tweet_id = req.body.prev_tweet_id ? req.body.prev_tweet_id : 0;
  let sql: string = 'INSERT INTO tweets(tweet_text, sender_id, prev_tweet_id) VALUES (?, ?, ?)'
  console.log(req.body.post_text);
  
  let paras = [req.body.post_text, req.body.user_id, prev_tweet_id]
  const rows: Query = pool.query(sql, paras)
  rows.on('error', (err: MysqlError) => console.error(err.message))
  rows.on('result', (result: any) => {
    res.send('done')
  })
  //推文判断逻辑，分解@
})
// bio
app.post('/api/a/set-bio', upload.none(), (req: Request, res: Response) => {
  const sql = 'UPDATE users SET bio = ? WHERE user_id = ?'
  const params = [req.body.bio, req.body.user_id] 
  const rows = pool.query(sql, params)
  rows.on('error', (err: MysqlError) => console.error(err.message))
  rows.on('result', (result: any) => {
    res.send('done')
  })
})

// like_twitter_id
app.post('/api/a/like', upload.none(), async (req: Request, res: Response) => {
  const queryLikeSQL = 'SELECT * FROM likes WHERE user_id = ? AND tweet_id = ?'
  const insertLikeSQL = 'INSERT INTO likes(user_id, tweet_id) VALUES (?, ?)'
  const deleteLikeSQL = 'DELETE FROM likes WHERE user_id = ? AND tweet_id = ?'
  const params = [req.body.user_id, req.body.like_twitter_id]
  let existsLike : boolean = false

  const insertQuery = () => {
    const insertCallback = pool.query(insertLikeSQL, params)
    insertCallback.on('error', (err: MysqlError) => {
      console.error(err.message)
      res.sendStatus(500)
    })
    insertCallback.on('result', (result: any) => {
    res.sendStatus(200)
    })
  }

  const deleteQuery = () => {
    const insertCallback = pool.query(deleteLikeSQL, params)
    insertCallback.on('error', (err: MysqlError) => {
      console.error(err.message)
      res.sendStatus(500)
    })
    insertCallback.on('result', (result: any) => {
    res.sendStatus(200)
    })
  }

  const queryCallback = pool.query(queryLikeSQL, params)
  queryCallback.on('error', (err: MysqlError) => {
    console.error(err.message)
    res.send(500)
  })
  queryCallback.on('result', (result: any) => {
    existsLike = true
    if (result.tweet_id) deleteQuery()
  })

  setTimeout(() => {
    if (!existsLike) {
      insertQuery()
    }
  }, 100);
})
// followed_id
app.post('/api/a/follow', upload.none(), async (req: Request, res: Response) => {
  const followerId = req.body.user_id;
  const followedId = req.body.followed_id;
  const checkFollowSql = 'SELECT * FROM follows WHERE follower_id = ? AND followed_id = ?';
  const insertFollowSql = 'INSERT INTO follows(follower_id, followed_id) VALUES (?, ?)';
  const deleteFollowSql = 'DELETE FROM follows WHERE follower_id = ? AND followed_id = ?';
  let existsFollow = false;

  const insertFollow = () => {
    const insertCb = pool.query(insertFollowSql, [followerId, followedId]);
    insertCb.on('error', (err: MysqlError) => {
      console.error(err.message);
      res.sendStatus(500);
    });
    insertCb.on('result', () => {
      res.sendStatus(200)
    });
  };

  const deleteFollow = () => {
    const deleteCb = pool.query(deleteFollowSql, [followerId, followedId]);
    deleteCb.on('error', (err: MysqlError) => {
      console.error(err.message);
      res.sendStatus(500);
    });
    deleteCb.on('result', () => res.sendStatus(200));
  };

  const checkFollowCb = pool.query(checkFollowSql, [followerId, followedId]);
  checkFollowCb.on('error', (err: MysqlError) => {
    console.error(err.message);
    res.sendStatus(500);
  });
  checkFollowCb.on('result', (result: any) => {
    existsFollow = true;
    if (result.follower_id) {
      deleteFollow();
    }
  });

  setTimeout(() => {
    if (!existsFollow) {
      insertFollow();
    }
  }, 100);
});

const storage = multer.diskStorage({
  destination: function (req: Request, file: any, cb: any) {
    cb(null, imagePath); // 指定上传文件的存放目录
  },
  filename: function (req: Request, file: any, cb: any) {
    const ext = path.extname(file.originalname); // 获取文件扩展名
    const uuid = generateUUID(); // 生成一个UUID作为文件名
    cb(null, uuid + ext);
  }
});

const uploadImage = multer({storage: storage});

app.post('/api/a/upload-image', uploadImage.single('image'), async (req: any, res: Response) => {
  const imageFile = req.body.image;
  const uuid = req.file.filename; // 获取文件名中的UUID  
  const uploader = req.body.user_id;
  const description = req.body.description || null;
  
  // 将上传的图片信息存入数据库
  
  const result = await pool.query('INSERT INTO images (uuid, uploader, description) VALUES (?, ?, ?)',
    [uuid, uploader, description]);
  result.on('error', (err: MysqlError) => {
    console.log(err);
  })
  result.on('result', (result: any) => {
    res.json({uuid: uuid});  
  })
});

app.get('/api/images/:uuid', async (req: Request, res: Response) => {
  const uuid = req.params.uuid
  let founded = false;
  /* 使用连接池从数据库查找项目 */
  const result = await pool.query('SELECT uuid FROM images WHERE uuid LIKE ?',
   [uuid + '.%'])
  result.on('error', (err: any) => {    
    console.error(err);
    res.status(500).send('Error querying database');
  })

  result.on('result', (result: any) => {
    
    const file = result.uuid;
    const filePathname = path.join(imagePath, file);

      /* 读取文件并发送给客户端 */

    fs.readFile(filePathname, (err: any, data: any) => {
      if (err) {
         console.error(err);
        res.status(500).send('Error reading file');
      } else {
        res.set('Content-Type', 'image/jpeg');
        res.send(data);
        founded = true
      }
  })
  })
  setTimeout(() => {
    if (!founded) res.status(404).send('File not found');
  }, 200);
})

app.get('/api/u/:user_id', function(req: Request, res: Response) {
  const userId = req.params.user_id;

  pool.getConnection(function(err, connection) {
    if (err) {
      res.status(500).send('Server Error');
      return;
    }

    const query = 'SELECT * FROM users WHERE user_id = ?';

    connection.query(query, [userId], function(error, results, fields) {
      connection.release();

      if (error) {
        res.status(500).send('Server Error');
        return;
      }

      if (results && results.length > 0) {
        const user = results[0];

        const userResponse = {
          user_id: user.user_id,
          username: user.username,
          bio: user.bio,
          avatar_url: user.avatar_url,
          following_count: user.following_count,
          followers_count: user.followers_count,
          join_date: user.join_date
        };

        res.json(userResponse);
      } else {
        res.status(404).send('User not found');
      }
    });
  });
});
// image_name
app.post('/api/a/set-avatar', upload.none(), async (req: Request, res: Response) => {
  const image_name = req.body.image_name;;
  const user_id = req.body.user_id;

  try {
    const result = await pool.query('UPDATE users SET avatar_url = ? WHERE user_id = ?', [image_name, user_id]);
    res.status(200).json({ success: true });
  } catch (error: any) {
    console.error(error.message);
    res.status(500).json({ success: false, message: 'Internal server error' });
  }
});
// retweet_id
app.post('/api/a/retweet',upload.none(), async (req: Request, res: Response) => {
  const retweetId = req.body.retweet_id;
  const senderId = req.body.user_id;
  let tweetExist = false;
  
    const conn = await pool.getConnection( async (err: any, conn: any) => {
      if (err) {
        console.log('转发推文失败', err);
        res.status(500).json({ message: '服务器错误' });
      }
      // 查询原推文
      const oldTweetRows = await conn.query('SELECT sender_id FROM tweets WHERE tweet_id = ?', [retweetId]);
      setTimeout(() => {
        if (!tweetExist) {
          res.status(404).json({ message: '推文不存在' });
          return;    
        }
      }, 100);
      oldTweetRows.on('result', async (row: any, index: Number) => {
        
        tweetExist = true;
        const oldSenderId = row.sender_id;
        // 插入新推文
        await conn.query('INSERT INTO tweets (tweet_text, sender_id, prev_tweet_id) VALUES (?, ?, ?)', ['', senderId, retweetId]);
        // 更新原推文
        await conn.query('UPDATE tweets SET retweet_count = retweet_count + 1 WHERE tweet_id = ?', [retweetId]);
        // 更新发送者信息
        await conn.query('UPDATE users SET retweet_count = retweet_count + 1 WHERE user_id = ?', [senderId]);
        if (oldSenderId !== senderId) {
          await conn.query('UPDATE users SET retweet_from_count = retweet_from_count + 1 WHERE user_id = ?', [oldSenderId]);
        }

        res.status(200).json({ message: '成功转发' });
      })
      conn.release()
    });

})

app.get('/api/u/:user_id/tweets/:page', upload.none(), async (req: Request, res: Response) => {
  const { user_id, page } = req.params;
  const pageNumber = parseInt(page, 10);
  const limit = 10;
  const offset = (pageNumber - 1) * limit;
  let tweets;
  const result = await pool.query(`
    SELECT t.tweet_id, t.tweet_text, t.tweet_time, t.sender_id, u.username
    FROM tweets t
    JOIN users u ON t.sender_id = u.user_id
    WHERE t.sender_id = ?
    ORDER BY t.tweet_time DESC
    LIMIT ?, ?
  `, [user_id, offset, limit], (err: any, result: any) => {
    if (err) {
      console.error(err.message);
      res.status(500).json({ success: false, message: 'Internal server error' });
      return
    }
    tweets = result
    console.log(tweets);
  
    const data = JSON.parse(JSON.stringify(tweets));
  
    res.status(200).json({ success: true, data });
  });
});

app.get('/api/a/get-time-line/:page', async(req: Request, res: Response) => {
  const page = parseInt(req.params.page) || 1;
  const tweetPerPage = 20;
  const offset = (page - 1) * tweetPerPage;
  const limit = tweetPerPage + 1;

  const re = await pool.query(`
  SELECT t.tweet_id, t.tweet_text, t.tweet_time, t.sender_id, u.username,
  u.avatar_url, COUNT(DISTINCT l.like_id) AS likes, t.retweet_count
  FROM tweets t
  JOIN users u ON t.sender_id = u.user_id
  LEFT JOIN likes l ON t.tweet_id = l.tweet_id
  WHERE t.sender_id IN (
    SELECT followed_id
    FROM follows
    WHERE follower_id = ?
  )
  GROUP BY t.tweet_id
  ORDER BY tweet_time DESC
  LIMIT ?, ?
  `, [req.body.user_id, offset, limit],
  function (err: any, results: any) {
    if (err) {
      console.error('Failed to get timeline', err);
      res.status(500).json({ message: 'Failed to get timeline' });
    }
    const tweets = results.slice(0, tweetPerPage).map((row: any) => ({
      tweetId: row.tweet_id,
      tweetText: row.tweet_text,
      tweetTime: row.tweet_time,
      senderId: row.sender_id,
      senderUsername: row.username,
      senderAvatarUrl: row.avatar_url,
      likeCount: row.likes,
      retweetCount: row.retweet_count
    }));
    const hasNextPage = results.length > tweetPerPage;
    res.json({ tweets, hasNextPage });
  });
});


// 启动服务器并开始监听端口
app.listen(3000, () => {
  console.log('服务器已启动')
})
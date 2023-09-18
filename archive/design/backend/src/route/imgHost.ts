import express, { Request, Response, response } from 'express';
import { QueryError, RowDataPacket } from 'mysql2';
import pool from '../database'
import path from 'path'
import crypto from 'crypto'
import multer from 'multer';
import { parseJsonFile } from '../misc'
import fs from 'fs'

const config: any = parseJsonFile('../config.json')
const router = express.Router();

function generateUUID() {
  const buf = crypto.randomBytes(16);
  buf[6] = (buf[6] & 0x0f) | 0x40;  // 设置 UUID 版本为 4
  buf[8] = (buf[8] & 0x3f) | 0x80;  // 设置 UUID 变体为 RFC 4122 定义的变体
  return buf.toString('hex').match(/(.{8})(.{4})(.{4})(.{4})(.{12})/)!.slice(1).join('-');
}

function findFileByName(name: string, folderPath: string): string | null {
  const files = fs.readdirSync(folderPath);

  
  // 遍历文件夹中的文件
  for (const file of files) {
    
    const fileName = path.parse(file).name;
    // console.log(name + ": " + path.parse(file).name);
    
    
    // 忽略后缀进行匹配
    if (fileName == name) {
      return path.join(folderPath, file);
    }
  }

  return null; // 文件未找到
}


const storage = multer.diskStorage({
  destination: function (req: Request, file: any, cb: any) {
    cb(null, path.resolve(config.imageHostPath)); // 指定上传文件的存放目录
  },
  filename: function (req: Request, file: any, cb: any) {
    const ext = path.extname(file.originalname); // 获取文件扩展名
    const uuid = generateUUID(); // 生成一个UUID作为文件名
    cb(null, uuid + ext);
  }
});

const uploadImage = multer({ storage: storage });

router.post('/api/upload-image', uploadImage.single('image'), async (req: any, res: Response) => {

  const imageFile = req.body.image;
  const uuid = req.file.filename; // 获取文件名中的UUID
  
  res.send({
    image_uuid: path.basename(uuid, path.extname(uuid)),
    status: 'success'
  })
});

// @param {uuid}
router.post('/api/image', (req: Request, res: Response) => {
  const { uuid } = req.body
  
  const filename = findFileByName(uuid, path.resolve(config.imageHostPath))
  if (filename) {
    res.sendFile(filename)
  } else {
    res.sendStatus(404)
  }
})

// @param {uuid}
router.get('/api/image/:uuid', (req: Request, res: Response) => {
  const { uuid } = req.params
  
  const filename = findFileByName(uuid, path.resolve(config.imageHostPath))
  if (filename) {
    res.sendFile(filename)
  } else {
    res.sendStatus(404)
  }
})

export default router;
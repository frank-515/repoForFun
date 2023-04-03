CREATE TABLE users (
  user_id VARCHAR(16) PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  bio VARCHAR(255) DEFAULT '',
  avatar_url VARCHAR(255),
  following_count INT DEFAULT 0,
  followers_count INT DEFAULT 0,
  retweet_from_count INT DEFAULT 0,
  join_date DATETIME DEFAULT CURRENT_TIMESTAMP, 
  password VARCHAR(255),
  INDEX (user_id)
);


CREATE TABLE tweets (
  tweet_id INT PRIMARY KEY AUTO_INCREMENT,
  tweet_text VARCHAR(280),
  tweet_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  sender_id VARCHAR(16) NOT NULL,
  prev_tweet_id INT,
  like_count INT DEFAULT 0,
  retweet_count INT DEFAULT 0,
  INDEX (tweet_id),
  FOREIGN KEY (sender_id) REFERENCES users(user_id)
)


CREATE TABLE likes (
  like_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id VARCHAR(16) NOT NULL,
  tweet_id INT NOT NULL,
  like_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (tweet_id) REFERENCES tweets(tweet_id),
  UNIQUE (user_id, tweet_id)
);

CREATE TABLE follows (
  follower_id VARCHAR(16) NOT NULL,
  followed_id VARCHAR(16) NOT NULL,
  follow_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX (follower_id, followed_id),
  FOREIGN KEY (follower_id) REFERENCES users(user_id),
  FOREIGN KEY (followed_id) REFERENCES users(user_id),
  UNIQUE (follower_id, followed_id)
);

CREATE TABLE login_credentials (
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(256) NOT NULL,
    issue_time DATETIME NOT NULL,
    expiry_time DATETIME NOT NULL,
    user_id VARCHAR(16) NOT NULL,
    login_ip VARCHAR(45) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE images (
  images_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  uuid CHAR(64) UNIQUE NOT NULL,
  uploader VARCHAR(16) NOT NULL,
  upload_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  description TEXT,
  FOREIGN KEY (uploader) REFERENCES users(user_id)
);


CREATE TRIGGER update_counts_trigger AFTER INSERT ON follows
FOR EACH ROW
BEGIN
    UPDATE users SET following_count = following_count + 1 WHERE user_id = NEW.follower_id;
    UPDATE users SET followers_count = followers_count + 1 WHERE user_id = NEW.followed_id;
END;


CREATE TRIGGER delete_counts_trigger AFTER DELETE ON follows
FOR EACH ROW
BEGIN
    UPDATE users SET following_count = following_count - 1 WHERE user_id = OLD.follower_id;
    UPDATE users SET followers_count = followers_count - 1 WHERE user_id = OLD.followed_id;
END;

CREATE TRIGGER update_tweet_likes_trigger AFTER INSERT ON likes
FOR EACH ROW
BEGIN
    UPDATE tweets SET like_count = like_count + 1 WHERE tweet_id = NEW.tweet_id;
END;

CREATE TRIGGER delete_tweet_likes_trigger AFTER DELETE ON likes
FOR EACH ROW
BEGIN
    UPDATE tweets SET like_count = like_count - 1 WHERE tweet_id = OLD.tweet_id;
END;
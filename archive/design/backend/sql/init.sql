-- Active: 1694449625386@@127.0.0.1@3306
CREATE DATABASE IF NOT EXISTS store;
USE store;

CREATE TABLE IF NOT EXISTS user_info (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) DEFAULT '' NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL DEFAULT 'Other',
    email VARCHAR(100) NOT NULL DEFAULT '',
    contact_number VARCHAR(20) NOT NULL DEFAULT '',
    password VARCHAR(256) NOT NULL DEFAULT '',
    registration_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_points INT DEFAULT 0 NOT NULL,
    user_level INT DEFAULT 1 NOT NULL CHECK (user_level >= 1 AND user_level <= 5),
    user_avatar VARCHAR(256) DEFAULT '' NOT NULL,
    default_address VARCHAR(256) DEFAULT '' NOT NULL,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE,
    cookie_token VARCHAR(256) DEFAULT '' NOT NULL,
    cookie_expiration DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_username UNIQUE (username),
    CONSTRAINT unique_email UNIQUE (email)
);



DELIMITER ;

CREATE TABLE IF NOT EXISTS product_info (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL DEFAULT '',
    product_quantity INT NOT NULL DEFAULT 0,
    product_price DECIMAL(10, 2) NOT NULL,
    product_type VARCHAR(50) NOT NULL DEFAULT 'Others',
    product_description TEXT,
    product_image VARCHAR(256) DEFAULT '' NOT NULL,
    is_recommended BOOLEAN NOT NULL DEFAULT false,
    on_shelf_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    point_ratio DECIMAL(10, 2) NOT NULL DEFAULT 10,
    product_value INT NOT NULL DEFAULT 4
);
/* ALTER TABLE order_info ADD CONSTRAINT fk_order_info_product FOREIGN KEY (product_id) REFERENCES product_info(product_id) ON DELETE CASCADE; */

CREATE TABLE IF NOT EXISTS order_info (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    address TEXT,
    payment_method VARCHAR(50) NOT NULL DEFAULT '',
    contact_number VARCHAR(20) NOT NULL DEFAULT '',
    shipping_method VARCHAR(50) NOT NULL DEFAULT '',
    remarks TEXT,
    FOREIGN KEY (product_id) REFERENCES product_info(product_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user_info(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS review (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    content TEXT,
    star INT NOT NULL DEFAULT 5 CHECK (star >= 1 AND star <= 5),
    like_count INT NOT NULL DEFAULT 0,
    review_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product_info(product_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user_info(user_id) ON DELETE CASCADE
);

/* DELIMITER $$
CREATE TRIGGER update_user_level
AFTER UPDATE ON user_info
FOR EACH ROW
BEGIN
    DECLARE points INT;
    DECLARE level INT;

    SET points = NEW.total_points;
    IF points >= 0 AND points <= 10 THEN
        SET level = 1;
    ELSEIF points >= 11 AND points <= 100 THEN
        SET level = 2;
    ELSEIF points >= 101 AND points <= 500 THEN
        SET level = 3;
    ELSEIF points >= 501 AND points <= 2000 THEN
        SET level = 4;
    ELSEIF points >= 2001 AND points <= 10000 THEN
        SET level = 5;
    ELSE 
        SET level = 1;
    END IF;

    UPDATE user_info SET user_level = level WHERE user_id = NEW.user_id;
END$$
DELIMITER ; */
CREATE TRIGGER delete_related_data
AFTER DELETE ON product_info
FOR EACH ROW
BEGIN
    DELETE FROM order_info WHERE product_id = OLD.product_id;
    DELETE FROM review WHERE product_id = OLD.product_id;
END;
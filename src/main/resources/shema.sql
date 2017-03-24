CREATE DATABASE IF NOT EXISTS `online-shop` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `online-shop`;

CREATE TABLE IF NOT EXISTS `category`
(
  id            BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  description   VARCHAR(255),
  name          VARCHAR(255),
  parent_id     BIGINT(20),
  created_date  DATETIME               NOT NULL,
  modified_date DATETIME,
  CONSTRAINT FK2y94svpmqttx80mshyny85wqr FOREIGN KEY (parent_id) REFERENCES category (id)
);
CREATE INDEX FK2y94svpmqttx80mshyny85wqr
  ON category (parent_id);
CREATE TABLE IF NOT EXISTS `category_children`
(
  category_id BIGINT(20) NOT NULL,
  children_id BIGINT(20) NOT NULL,
  CONSTRAINT FK6n2k92j6sc537ex4s3voewjn4 FOREIGN KEY (category_id) REFERENCES category (id),
  CONSTRAINT FK659qdt2r98579x3b414qvc211 FOREIGN KEY (children_id) REFERENCES category (id)
);
CREATE INDEX FK6n2k92j6sc537ex4s3voewjn4
  ON category_children (category_id);
CREATE UNIQUE INDEX UK_98qo8cf5kyu4c5d0bcrd04cqg
  ON category_children (children_id);



/*-------------------------------------*/

CREATE TABLE IF NOT EXISTS `product`
(
  id            BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name          VARCHAR(255),
  price         BIGINT(20),
  category_id   BIGINT(20),
  created_date  DATETIME               NOT NULL,
  modified_date DATETIME,
  search_string VARCHAR(1000),
  ava_name      VARCHAR(255),
  CONSTRAINT FK1mtsbur82frn64de7balymq9s FOREIGN KEY (category_id) REFERENCES category (id)
);
CREATE INDEX FK1mtsbur82frn64de7balymq9s
  ON product (category_id);



/*------------------------------------*/
# CREATE TABLE IF NOT EXISTS`description`
# (
#   id            BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
#   type          VARCHAR(255)           NOT NULL,
#   product_id    BIGINT(20),
#   created_date  DATETIME               NOT NULL,
#   modified_date DATETIME,
#   value         VARCHAR(255)           NOT NULL,
#   CONSTRAINT FKpu1hnodrf9m8awy8u83u4rv7a FOREIGN KEY (product_id) REFERENCES product (id)
# );
# CREATE INDEX FKpu1hnodrf9m8awy8u83u4rv7a
#   ON description (product_id);

CREATE TABLE IF NOT EXISTS `description` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime NOT NULL,
  `modified_date` datetime DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpu1hnodrf9m8awy8u83u4rv7a` (`product_id`),
  CONSTRAINT `FKpu1hnodrf9m8awy8u83u4rv7a` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;


/*-------------------------------------*/
CREATE TABLE IF NOT EXISTS `user`
(
  id            BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  email         VARCHAR(255)           NOT NULL,
  first_name    VARCHAR(255)           NOT NULL,
  last_name     VARCHAR(255)           NOT NULL,
  password      VARCHAR(255)           NOT NULL,
  phone_number  VARCHAR(255),
  role          VARCHAR(255),
  sex           VARCHAR(255),
  user_name     VARCHAR(255)           NOT NULL,
  enabled       BIT(1),
  created_date  DATETIME               NOT NULL,
  modified_date DATETIME,
  ava_name      VARCHAR(255)
);
CREATE UNIQUE INDEX UK_lqjrcobrh9jc8wpcar64q1bfh
  ON user (user_name);
CREATE UNIQUE INDEX UK_ob8kqyqqgmefl0aco34akdtpe
  ON user (email);





/*-------------------------------*/
CREATE TABLE IF NOT EXISTS `cart`
(
  id            BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  created_date  DATETIME               NOT NULL,
  modified_date DATETIME,
  quantity      INT(11),
  product_id    BIGINT(20),
  user_id       BIGINT(20),
  CONSTRAINT FK3d704slv66tw6x5hmbm6p2x3u FOREIGN KEY (product_id) REFERENCES product (id),
  CONSTRAINT FKl70asp4l4w0jmbm1tqyofho4o FOREIGN KEY (user_id) REFERENCES user (id)
);
CREATE INDEX FK3d704slv66tw6x5hmbm6p2x3u
  ON cart (product_id);
CREATE INDEX FKl70asp4l4w0jmbm1tqyofho4o
  ON cart (user_id);

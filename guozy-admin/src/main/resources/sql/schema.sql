CREATE DATABASE `guozy`;
USE `guozy`;

-- 管理账户表
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`account` VARCHAR(50) NOT NULL COMMENT '账号',
	`password` CHAR(60) NOT NULL COMMENT '密码(加密存储)',
	`name` VARCHAR(30) NOT NULL COMMENT '姓名',
	`email` VARCHAR(40) NULL COMMENT '邮箱',
	`phone` CHAR(11) NULL COMMENT '手机号',
	`active_status` BIT NOT NULL DEFAULT b'1',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	PRIMARY KEY (`id`),
	KEY `idx_user_account`(`account`)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='管理账户表';

-- 管理角色表
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`name` VARCHAR(50) NOT NULL,
	`description` VARCHAR(255) NULL,
	PRIMARY KEY (`id`),
	KEY `idx_role_name`(`name`)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `admin_user_role_xref`;
CREATE TABLE `admin_user_role_xref` (
	`user_id` INT UNSIGNED NOT NULL,
	`role_id` INT UNSIGNED NOT NULL,
	PRIMARY KEY (`user_id`, `role_id`),
	FOREIGN KEY (`user_id`) REFERENCES `admin_user` (`id`)
			ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY (`role_id`) REFERENCES `admin_role` (`id`)
    	ON DELETE CASCADE
      ON UPDATE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`name` VARCHAR(50) NULL,
	`description` VARCHAR(255) NULL,
  `type` VARCHAR(20) NOT NULL,
  `is_composite` BIT NOT NULL DEFAULT b'0',
	PRIMARY KEY (`id`)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `perm_xref`;
CREATE TABLE `perm_xref` (
	`p_id` INT UNSIGNED NOT NULL,
	`c_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`p_id`, `c_id`),
	FOREIGN KEY (`p_id`) REFERENCES `permission` (`id`)
			ON DELETE CASCADE
      ON UPDATE CASCADE,
	FOREIGN KEY (`c_id`) REFERENCES `permission` (`id`)
			ON DELETE CASCADE
		  ON UPDATE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `admin_user_perm_xref`;
CREATE TABLE `admin_user_perm_xref` (
	`admin_user_id` INT UNSIGNED NOT NULL,
	`perm_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`admin_user_id`, `perm_id`),
	FOREIGN KEY (`admin_user_id`) REFERENCES `admin_user` (`id`)
			ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY (`perm_id`) REFERENCES `permission` (`id`)
    	ON DELETE CASCADE
      ON UPDATE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `admin_role_perm_xref`;
CREATE TABLE `admin_role_perm_xref` (
	`admin_role_id` INT UNSIGNED NOT NULL,
	`perm_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`admin_role_id`, `perm_id`),
	FOREIGN KEY (`admin_role_id`) REFERENCES `admin_role` (`id`)
			ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY (`perm_id`) REFERENCES `permission` (`id`)
    	ON DELETE CASCADE
      ON UPDATE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `admin_module`;
CREATE TABLE `admin_module` (
	`md_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `md_key` VARCHAR(30) NOT NULL,
	`md_name` VARCHAR(30) NOT NULL,
	`md_icon` VARCHAR(30) NULL,
	`md_order` INT NOT NULL DEFAULT 0,
	PRIMARY KEY (`md_id`)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `admin_func`;
CREATE TABLE `admin_func` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `key` VARCHAR(30) NOT NULL,
	`name` VARCHAR(30) NOT NULL,
	`url` VARCHAR(255) NOT NULL,
	`icon` VARCHAR(30) NULL,
  `mid` INT UNSIGNED NULL,
	`order` INT NOT NULL DEFAULT 0,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`mid`) REFERENCES `admin_module` (`md_id`)
			ON DELETE SET NULL
      ON UPDATE CASCADE
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `admin_func_perm_xref`;
CREATE TABLE `admin_func_perm_xref` (
	`admin_func_id` INT UNSIGNED NOT NULL,
	`perm_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`admin_func_id`, `perm_id`),
	FOREIGN KEY (`admin_func_id`) REFERENCES `admin_func` (`id`)
			ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY (`perm_id`) REFERENCES `permission` (`id`)
    	ON DELETE CASCADE
      ON UPDATE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=utf8;

-- 账户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`password` VARCHAR(70) NOT NULL COMMENT '密码(加密存储)',
	`phone` CHAR(11) NOT NULL COMMENT '手机号',
	`nickname` VARCHAR(30) NULL,
	`deactivated` BIT NOT NULL DEFAULT b'0',
  `is_saler` BIT NOT NULL DEFAULT b'0',
	`line_to_saler` BIT NOT NULL DEFAULT b'1',
	`avatar` VARCHAR(255) NULL,
	`sid` INT UNSIGNED NOT NULL,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	PRIMARY KEY (`id`),
	KEY `idx_user_phone`(`phone`),
	FOREIGN KEY (`sid`) REFERENCES `shop` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`name` VARCHAR(100) NOT NULL,
	`address` VARCHAR(255) NOT NULL,
  `admin` INT UNSIGNED NULL,
  `ship_distance` TINYINT NOT NULL DEFAULT 1,
  `ship_price` DECIMAL NOT NULL DEFAULT 5,
  `open_time` TIME NOT NULL DEFAULT  '9:00',
  `close_time` TIME NOT NULL DEFAULT '22:00',
	PRIMARY KEY (`id`),
  FOREIGN KEY (`admin`) REFERENCES `admin_user` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`name` VARCHAR(100) NULL,
  `sid` INT UNSIGNED NULL,
	PRIMARY KEY (`id`),
  FOREIGN KEY (`sid`) REFERENCES `shop` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`name` VARCHAR(255) NOT NULL,
  `pic` BLOB NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `need_saler` BIT NOT NULL DEFAULT b'0',
	PRIMARY KEY (`id`)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `goods_cat_xref`;
CREATE TABLE `goods_cat_xref` (
	`gid` INT UNSIGNED NOT NULL,
  `cid` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`gid`, `cid`),
  FOREIGN KEY (`gid`) REFERENCES `goods` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (`cid`) REFERENCES `category` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`name` VARCHAR(100) NOT NULL,
  `value` VARCHAR(100) NULL,
  `ptype` VARCHAR(20) NOT NULL DEFAULT 'STRING',
  `f_name` VARCHAR(100) NULL,
	`f_group` VARCHAR(100) NULL,
	`f_tab` VARCHAR(100) NULL,
	PRIMARY KEY (`id`),
	KEY `idx_sysconfig_name`(`name`)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `asset`;
CREATE TABLE `asset` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`name` VARCHAR(100) NOT NULL,
  `url` VARCHAR(255) NOT NULL,
  `title` VARCHAR(100) NULL,
  `alt_txt` VARCHAR(100) NULL,
	`mime` VARCHAR(30) NULL,
	`size` INT UNSIGNED NULL,
	`ext` VARCHAR(10) NULL,
	`date_created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`date_updated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `idx_asset_url`(`url`)
)ENGINE=INNODB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`name` VARCHAR(50) NOT NULL COMMENT '收件人',
  `addr_line1` VARCHAR(255) NOT NULL,
  `addr_line2` VARCHAR(255) NOT NULL,
  `phone` CHAR(11) NOT NULL COMMENT '手机号',
	`is_def` BIT NOT NULL DEFAULT b'0',
	`uid` INT UNSIGNED NOT NULL COMMENT '和user的关联外键',
	PRIMARY KEY (`id`),
	FOREIGN KEY (`uid`) REFERENCES `user` (`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
)ENGINE=INNODB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `offer`;
CREATE TABLE `offer` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(255) NULL,
  `discount_type` VARCHAR(30) NOT NULL,
  `value` DECIMAL(10,3) NOT NULL,
	`start_date` DATETIME NULL,
	`end_date` DATETIME NULL,
	`max_uses_per_user` SMALLINT NOT NULL DEFAULT -1,
	`max_uses_per_order` SMALLINT NOT NULL DEFAULT -1,
	PRIMARY KEY (`id`)
)ENGINE=INNODB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_offer`;
CREATE TABLE `user_offer` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`user_id` INT UNSIGNED NOT NULL,
	`offer_id` INT UNSIGNED NOT NULL,
	`is_used` BIT NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (`offer_id`) REFERENCES `offer` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)ENGINE=INNODB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `fruit_cs`;
CREATE TABLE `fruit_cs` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`title` VARCHAR(100) NOT NULL,
  `picurl` VARCHAR(255) NULL,
  `cnturl` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id`)
)ENGINE=INNODB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
	`odr_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`sid` INT UNSIGNED NULL,
	`uid` INT UNSIGNED NULL,
	`ship_addr` INT UNSIGNED NULL,
	`odr_num` VARCHAR(20) NULL,
  `status` VARCHAR(20) NOT NULL,
	`total` DECIMAL(10,2),
	`subtotal` DECIMAL(10,2),
  `ship_price` DECIMAL(10,2),
  `sale_price` DECIMAL(10,2),
	`sale_price_override` BIT NOT NULL DEFAULT b'0',
	`is_saler_odr` BIT NOT NULL DEFAULT b'0',
	`date_created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`date_updated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updated_by` INT UNSIGNED NOT NULL,
	`created_by` INT UNSIGNED NOT NULL,
	`date_submited` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`odr_id`),
	FOREIGN KEY (`sid`) REFERENCES `shop` (`id`)
		ON DELETE SET NULL
		ON UPDATE CASCADE,
	FOREIGN KEY (`uid`) REFERENCES `user` (`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY (`ship_addr`) REFERENCES `address` (`id`)
		ON DELETE SET NULL
		ON UPDATE CASCADE
)ENGINE=INNODB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`odr_id` INT UNSIGNED NOT NULL,
	`price` DECIMAL(10,2) NULL,
	`quantity` INT NOT NULL,
  `goods_id` INT UNSIGNED NOT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`odr_id`) REFERENCES `order` (`odr_id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`)
		ON DELETE SET NULL
		ON UPDATE CASCADE
)ENGINE=INNODB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `order_adjustment`;
CREATE TABLE `order_adjustment` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`odr_id` INT UNSIGNED NOT NULL,
	`usroffer_id` INT UNSIGNED NOT NULL,
	`reason` VARCHAR(255) NULL,
	`value` DECIMAL(10,2) NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`odr_id`) REFERENCES `order` (`odr_id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY (`usroffer_id`) REFERENCES `user_offer` (`id`)
		ON DELETE SET NULL
		ON UPDATE CASCADE
)ENGINE=INNODB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `pay_map`;
CREATE TABLE `pay_map`(
  id INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  orderId INT UNSIGNED NOT NULL COMMENT '所属订单ID',
  orderNum VARCHAR(20) NOT NULL COMMENT '所属订单code',
  tempPayCode VARCHAR(200) NOT NULL COMMENT '临时支付号ID',
  platform VARCHAR(200) DEFAULT NULL COMMENT '所属平台',
  payParams VARCHAR(3500) DEFAULT NULL COMMENT '支付所生成的请求信息',
  retMsg VARCHAR(800) DEFAULT NULL COMMENT '支付后回调时的详细信息',
  retMsg2 VARCHAR(800) DEFAULT NULL COMMENT '备用消息',
  isPaid CHAR(1) DEFAULT NULL COMMENT '是否已支付0否；1是',
  remark VARCHAR(200) DEFAULT NULL,
  swiftNumber VARCHAR(60) DEFAULT NULL COMMENT '交易流水号',
  payPurpose VARCHAR(30) DEFAULT NULL,
  idBelongsTo VARCHAR(60) DEFAULT NULL,
  cashAmt DECIMAL(18, 2) DEFAULT NULL,
  remark2 VARCHAR(200) DEFAULT NULL,
  notify_time BIGINT(20) DEFAULT NULL COMMENT '通知回调时间',
  requestBiz VARCHAR(200) DEFAULT NULL COMMENT '支付请求业务来源',
  PRIMARY KEY (id),
  INDEX orderNum (orderNum),
  INDEX orderId (orderId)
) ENGINE=INNODB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

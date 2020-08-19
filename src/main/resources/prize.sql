/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : prize

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2020-08-19 10:01:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_no` varchar(255) DEFAULT NULL,
  `score` decimal(10,2) DEFAULT '0.00',
  `gold` decimal(10,2) DEFAULT '0.00',
  `load_factor` decimal(10,2) DEFAULT '0.00',
  `wx_mini_open_id` varchar(255) DEFAULT NULL,
  `zfb_open_id` varchar(255) DEFAULT NULL,
  `wx_open_id` varchar(255) DEFAULT NULL COMMENT 'WX_OPENID/WX_MINI/ZFB',
  `nick_name` varchar(255) DEFAULT NULL,
  `head_url` varchar(255) DEFAULT NULL,
  `customer_status` varchar(255) DEFAULT NULL COMMENT 'EFFECT/NOT_EFFECT',
  `customer_type` varchar(255) DEFAULT NULL COMMENT 'COMMON/MEMBER_01/MEMBER_02...',
  `member_expire` datetime DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_no` (`customer_no`),
  KEY `uuid_id` (`wx_mini_open_id`,`wx_open_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for flow_record
-- ----------------------------
DROP TABLE IF EXISTS `flow_record`;
CREATE TABLE `flow_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_no` varchar(255) DEFAULT NULL,
  `record_type` varchar(255) DEFAULT NULL COMMENT 'SCORE/GOLD',
  `record_reason` varchar(255) DEFAULT NULL COMMENT '流水原因',
  `value` decimal(10,2) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `customer_no` (`customer_no`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for pay_record
-- ----------------------------
DROP TABLE IF EXISTS `pay_record`;
CREATE TABLE `pay_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pay_no` varchar(255) DEFAULT NULL,
  `pay_channel` varchar(255) DEFAULT NULL COMMENT 'WX/ALIPAY',
  `pay_type` varchar(255) DEFAULT NULL COMMENT 'PAY/REFUND/CASH',
  `pay_amount` decimal(10,2) DEFAULT NULL,
  `channel_no` varchar(255) DEFAULT NULL,
  `pay_status` varchar(255) DEFAULT NULL COMMENT 'INIT/PENDING/SUCCESS/FAIL',
  `error_msg` varchar(255) DEFAULT NULL,
  `customer_no` varchar(255) DEFAULT NULL,
  `order_msg` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wx_access_token
-- ----------------------------
DROP TABLE IF EXISTS `wx_access_token`;
CREATE TABLE `wx_access_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `access_token` varchar(512) DEFAULT NULL,
  `expires_in` int(11) DEFAULT NULL COMMENT '剩余有效秒数',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `jsapi_ticket` varchar(512) DEFAULT NULL,
  `ticket_expires_in` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for wx_access_token_user
-- ----------------------------
DROP TABLE IF EXISTS `wx_access_token_user`;
CREATE TABLE `wx_access_token_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `access_token` varchar(512) DEFAULT NULL,
  `user_no` varchar(255) DEFAULT NULL COMMENT '用户号',
  `expires_in` int(255) DEFAULT NULL COMMENT '超时时间（秒）',
  `refresh_token` varchar(512) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_no` (`user_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

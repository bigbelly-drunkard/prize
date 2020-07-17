/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : daina

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2020-02-26 19:42:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for accept_order
-- ----------------------------
DROP TABLE IF EXISTS `accept_order`;
CREATE TABLE `accept_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) DEFAULT NULL,
  `user_no` varchar(255) DEFAULT NULL,
  `pub_order_no` varchar(255) DEFAULT NULL,
  `time_delay` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `cancel_reason` varchar(255) DEFAULT NULL COMMENT '取消原因 ',
  `amout` decimal(10,2) DEFAULT NULL COMMENT '结算后单子金额',
  `pub_price` decimal(10,2) DEFAULT NULL COMMENT '结算前的金额',
  `status` varchar(255) DEFAULT NULL COMMENT '已接单  接到货物 已完成 已取消',
  `created_date` datetime DEFAULT NULL,
  `finished_date` datetime DEFAULT NULL,
  `mofied_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_flow
-- ----------------------------
DROP TABLE IF EXISTS `account_flow`;
CREATE TABLE `account_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `record_no` varchar(255) DEFAULT NULL,
  `flow_user_type` tinyint(4) DEFAULT NULL,
  `record_type` tinyint(4) DEFAULT NULL,
  `order_no` varchar(255) DEFAULT NULL,
  `total_money` decimal(10,2) DEFAULT NULL,
  `coupon_id` varchar(255) DEFAULT NULL,
  `coupon_amount` decimal(10,2) DEFAULT NULL,
  `pay_amount` decimal(10,2) DEFAULT NULL,
  `user_no` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `acc_type` tinyint(4) DEFAULT NULL,
  `record_status` tinyint(4) DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for approve
-- ----------------------------
DROP TABLE IF EXISTS `approve`;
CREATE TABLE `approve` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_no` varchar(255) DEFAULT NULL,
  `stu_no` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL COMMENT '男 女 未知',
  `tel_no` varchar(255) DEFAULT NULL COMMENT '联系方式 电话号码',
  `real_name` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `real_id_no` varchar(255) DEFAULT NULL COMMENT '实名证件号',
  `stu_sch_name` varchar(255) DEFAULT NULL COMMENT '学校名字',
  `stu_address_l` varchar(255) DEFAULT NULL COMMENT '地址 几号楼',
  `stu_address_q` varchar(255) DEFAULT NULL COMMENT '寝室号',
  `status` varchar(255) DEFAULT NULL COMMENT '申请状态（实名状态）',
  `remark` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_no`),
  KEY `stu_no` (`stu_no`),
  KEY `real_name` (`real_name`),
  KEY `tel_no` (`tel_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for boss_user
-- ----------------------------
DROP TABLE IF EXISTS `boss_user`;
CREATE TABLE `boss_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `boss_user_no` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `login_pwd` varchar(255) DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for feed_back
-- ----------------------------
DROP TABLE IF EXISTS `feed_back`;
CREATE TABLE `feed_back` (
  `feed_back_no` varchar(255) DEFAULT NULL,
  `user_no` varchar(255) DEFAULT NULL,
  `feed_back_content` varchar(512) DEFAULT NULL,
  `phone_type` varchar(255) DEFAULT NULL,
  `os` varchar(255) DEFAULT NULL,
  `browser` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for fee_rule
-- ----------------------------
DROP TABLE IF EXISTS `fee_rule`;
CREATE TABLE `fee_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fee_type` tinyint(4) NOT NULL,
  `fee_threshold_value` decimal(10,2) DEFAULT NULL,
  `fee_value` decimal(10,2) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `modified_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `version` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for grade_record
-- ----------------------------
DROP TABLE IF EXISTS `grade_record`;
CREATE TABLE `grade_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `score` int(11) DEFAULT NULL COMMENT '星级 5星',
  `target_user_no` varchar(255) DEFAULT NULL COMMENT '目标用户',
  `user_no` varchar(255) DEFAULT NULL COMMENT '打分人',
  `accept_record_no` varchar(255) DEFAULT NULL COMMENT '接单号',
  `content` varchar(512) DEFAULT NULL COMMENT '内容',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modified_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for pub_order
-- ----------------------------
DROP TABLE IF EXISTS `pub_order`;
CREATE TABLE `pub_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) DEFAULT NULL,
  `user_no` varchar(255) DEFAULT NULL,
  `weigth` varchar(255) DEFAULT NULL COMMENT '重量等级5斤以内 5~10斤 10~20斤 20斤以上',
  `price` decimal(10,2) DEFAULT NULL COMMENT '奖励价格',
  `kd_type` varchar(255) DEFAULT NULL COMMENT '快递类型 0：申通 1.。。',
  `sch_name` varchar(255) DEFAULT NULL,
  `address_get` varchar(255) DEFAULT NULL COMMENT '取件地址',
  `get_longitude` varchar(255) DEFAULT NULL COMMENT '取件 经度',
  `get_latitude` varchar(255) DEFAULT NULL,
  `sco_no` varchar(255) DEFAULT NULL COMMENT '取件凭证',
  `address_services` varchar(255) DEFAULT NULL COMMENT '送达地址',
  `ser_longitude` varchar(255) DEFAULT NULL,
  `ser_latitude` varchar(255) DEFAULT NULL,
  `time_delay` datetime DEFAULT NULL COMMENT '超时时间',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL COMMENT '未支付 支付并发布 已取消 被接单 接到货物 已完成',
  `confirm_code` varchar(255) DEFAULT NULL COMMENT '确认收货码',
  `cancel_reason` varchar(255) DEFAULT NULL COMMENT '取消原因 系统 发单人取消  接单人取消',
  `wuliu_no` varchar(255) DEFAULT NULL,
  `wuliu_json` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sms_record
-- ----------------------------
DROP TABLE IF EXISTS `sms_record`;
CREATE TABLE `sms_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sms_type` varchar(255) DEFAULT NULL COMMENT '短信类型',
  `sms_content` varchar(512) DEFAULT NULL,
  `sms_template_id` varchar(255) DEFAULT NULL,
  `sms_phone` varchar(255) DEFAULT NULL,
  `sms_status` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1  DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user_history_address
-- ----------------------------
DROP TABLE IF EXISTS `user_history_address`;
CREATE TABLE `user_history_address` (
  `id` bigint(20) DEFAULT NULL AUTO_INCREMENT,
  `user_no` varchar(255) DEFAULT NULL,
  `adress_type` varchar(255) DEFAULT NULL COMMENT '取件地址/收货地址 ',
  `adress` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user_stu
-- ----------------------------
DROP TABLE IF EXISTS `user_stu`;
CREATE TABLE `user_stu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_no` varchar(255) NOT NULL,
  `real_name` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL COMMENT '男 女 未知',
  `nick_name` varchar(255) DEFAULT NULL,
  `user_head_pic` varchar(255) DEFAULT NULL,
  `tel_no` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `wx_open_id` varchar(255) DEFAULT NULL,
  `wx_mini_open_id` varchar(255) DEFAULT NULL,
  `sch_name` varchar(255) DEFAULT NULL COMMENT '学校',
  `address_l` varchar(255) DEFAULT NULL COMMENT '楼号',
  `address_q` varchar(255) DEFAULT NULL COMMENT '寝室号',
  `remark` varchar(255) DEFAULT NULL,
  `user_status` varchar(255) DEFAULT NULL COMMENT '正常 冻结',
  `real_status` varchar(255) DEFAULT NULL COMMENT '实名状态 0：未实名 1：已实名',
  `real_id_no` varchar(255) DEFAULT NULL COMMENT '实名证件号',
  `credit_score` int(11) DEFAULT NULL,
  `balance` decimal(10,2) DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wx_access_token
-- ----------------------------
DROP TABLE IF EXISTS `wx_access_token`;
CREATE TABLE `wx_access_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `access_token` varchar(255) DEFAULT NULL,
  `expires_in` int(11) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modified_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `jsapi_ticket` varchar(255) DEFAULT NULL,
  `ticket_expires_in` int(11) DEFAULT NULL,
  `channel_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wx_access_token_user
-- ----------------------------
DROP TABLE IF EXISTS `wx_access_token_user`;
CREATE TABLE `wx_access_token_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `access_token` varchar(255) DEFAULT NULL,
  `user_no` varchar(255) DEFAULT NULL,
  `session_key` varchar(255) DEFAULT NULL,
  `expires_in` int(11) DEFAULT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

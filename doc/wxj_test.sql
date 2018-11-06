/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50547
Source Host           : localhost:3305
Source Database       : wxj_test

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2018-11-06 10:49:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for data_original
-- ----------------------------
DROP TABLE IF EXISTS `data_original`;
CREATE TABLE `data_original` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `edit_date` datetime DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `del_flag` char(2) DEFAULT NULL,
  `file_id` int(11) DEFAULT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `month` char(10) DEFAULT NULL,
  `value1` int(4) DEFAULT NULL,
  `value2` int(4) DEFAULT NULL,
  `value3` int(4) DEFAULT NULL,
  `value4` int(4) DEFAULT NULL,
  `value5` int(4) DEFAULT NULL,
  `value6` int(4) DEFAULT NULL,
  `total` int(5) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3427 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for data_result
-- ----------------------------
DROP TABLE IF EXISTS `data_result`;
CREATE TABLE `data_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `edit_date` datetime DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `del_flag` char(2) DEFAULT NULL,
  `file_id` int(11) DEFAULT NULL,
  `month` char(10) DEFAULT NULL,
  `value1` int(4) DEFAULT NULL,
  `value2` int(4) DEFAULT NULL,
  `value3` int(4) DEFAULT NULL,
  `value4` int(4) DEFAULT NULL,
  `value5` int(4) DEFAULT NULL,
  `value6` int(4) DEFAULT NULL,
  `relation_id` varchar(11) DEFAULT NULL,
  `relation_type` varchar(20) DEFAULT NULL,
  `total` int(5) DEFAULT NULL,
  `person_nub` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for org_department
-- ----------------------------
DROP TABLE IF EXISTS `org_department`;
CREATE TABLE `org_department` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `parentid` int(11) DEFAULT NULL,
  `order_nub` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for org_group
-- ----------------------------
DROP TABLE IF EXISTS `org_group`;
CREATE TABLE `org_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tagid` int(11) DEFAULT NULL,
  `tagname` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for org_relation
-- ----------------------------
DROP TABLE IF EXISTS `org_relation`;
CREATE TABLE `org_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(50) DEFAULT NULL,
  `relation_id` int(11) DEFAULT NULL,
  `relation_type` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2024 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `edit_date` datetime DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `del_flag` char(2) DEFAULT NULL,
  `path` varchar(50) DEFAULT NULL,
  `ext` char(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `month` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_data
-- ----------------------------
DROP TABLE IF EXISTS `user_data`;
CREATE TABLE `user_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(50) DEFAULT NULL,
  `pass_word` varchar(50) DEFAULT NULL,
  `account` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `position` varchar(50) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `avatar` varchar(100) DEFAULT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=741 DEFAULT CHARSET=utf8;


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
  `open_id` varchar(50) DEFAULT NULL,
  `month` char(10) DEFAULT NULL,
  `value1` int(4) DEFAULT NULL,
  `value2` int(4) DEFAULT NULL,
  `value3` int(4) DEFAULT NULL,
  `value4` int(4) DEFAULT NULL,
  `value5` int(4) DEFAULT NULL,
  `value6` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `data_result`;
CREATE TABLE `data_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `edit_date` datetime DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `del_flag` char(2) DEFAULT NULL,
  `file_id` int(11) DEFAULT NULL,
  `open_id` varchar(50) DEFAULT NULL,
  `month` char(10) DEFAULT NULL,
  `value1` int(4) DEFAULT NULL,
  `value2` int(4) DEFAULT NULL,
  `value3` int(4) DEFAULT NULL,
  `value4` int(4) DEFAULT NULL,
  `value5` int(4) DEFAULT NULL,
  `value6` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `org_department`;
CREATE TABLE `org_department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `edit_date` datetime DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `del_flag` char(2) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `d_level` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `org_group`;
CREATE TABLE `org_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `edit_date` datetime DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `del_flag` char(2) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `org_relation`;
CREATE TABLE `org_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `edit_date` datetime DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `del_flag` char(2) DEFAULT NULL,
  `open_id` varchar(50) DEFAULT NULL,
	`group_id` int(11) DEFAULT NULL,
	`dept_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `edit_date` datetime DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `del_flag` char(2) DEFAULT NULL,
  `open_id` varchar(50) DEFAULT NULL,
	`path` varchar(50) DEFAULT NULL,
	`ext` char(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `month` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `user_data`;
CREATE TABLE `user_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `edit_date` datetime DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `del_flag` char(2) DEFAULT NULL,
  `open_id` varchar(50) DEFAULT NULL,
	`pass_word` varchar(50) DEFAULT NULL,
	`account` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

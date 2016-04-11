/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50096
Source Host           : 127.0.0.1:3306
Source Database       : zzdb

Target Server Type    : MYSQL
Target Server Version : 50096
File Encoding         : 65001

Date: 2016-04-08 18:02:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `record_category_tb`
-- ----------------------------
DROP TABLE IF EXISTS `record_category_tb`;
CREATE TABLE `record_category_tb` (
  `record_category` tinyint(4) NOT NULL default '0',
  `record_category_desc` varchar(30) default NULL,
  PRIMARY KEY  (`record_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of record_category_tb
-- ----------------------------
INSERT INTO `record_category_tb` VALUES ('0', '道路维护');
INSERT INTO `record_category_tb` VALUES ('1', '环境污染');
INSERT INTO `record_category_tb` VALUES ('2', '市政设施');
INSERT INTO `record_category_tb` VALUES ('3', '违章停车');
INSERT INTO `record_category_tb` VALUES ('4', '交通拥堵');
INSERT INTO `record_category_tb` VALUES ('5', '违法犯罪');
INSERT INTO `record_category_tb` VALUES ('6', '突发事件');
INSERT INTO `record_category_tb` VALUES ('7', '其他投诉');

-- ----------------------------
-- Table structure for `record_task_tb`
-- ----------------------------
DROP TABLE IF EXISTS `record_task_tb`;
CREATE TABLE `record_task_tb` (
  `task_id` varchar(30) NOT NULL,
  `report_phone` varchar(11) default NULL,
  `create_time` datetime default NULL,
  `dispatch_time` datetime default NULL,
  `end_time` datetime default NULL,
  `status` tinyint(4) default NULL,
  `comment` varchar(200) default NULL,
  PRIMARY KEY  (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of record_task_tb
-- ----------------------------
INSERT INTO `record_task_tb` VALUES ('t_1', '13800000003', '2016-04-08 16:16:01', '2016-04-08 16:17:01', '2016-04-08 16:26:01', '0', 'note');

-- ----------------------------
-- Table structure for `record_type_tb`
-- ----------------------------
DROP TABLE IF EXISTS `record_type_tb`;
CREATE TABLE `record_type_tb` (
  `record_type` tinyint(4) NOT NULL,
  `record_type_desc` varchar(20) default NULL,
  PRIMARY KEY  (`record_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of record_type_tb
-- ----------------------------
INSERT INTO `record_type_tb` VALUES ('0', 'picture');
INSERT INTO `record_type_tb` VALUES ('1', 'audio');
INSERT INTO `record_type_tb` VALUES ('2', 'video');

-- ----------------------------
-- Table structure for `report_record_orig`
-- ----------------------------
DROP TABLE IF EXISTS `report_record_orig`;
CREATE TABLE `report_record_orig` (
  `record_id` varchar(30) NOT NULL,
  `task_id` varchar(30) default NULL,
  `report_phone` varchar(11) default NULL,
  `record_type` tinyint(1) default NULL,
  `record_category` tinyint(1) default NULL,
  `record_time` datetime default NULL,
  `record_desc` varchar(150) default NULL,
  `record_path` varchar(500) default NULL,
  `longitude` int(11) default NULL,
  `latitude` int(11) default NULL,
  `location` varchar(100) default NULL,
  PRIMARY KEY  (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of report_record_orig
-- ----------------------------
INSERT INTO `report_record_orig` VALUES ('m_1', 't_1', '13800000003', '0', '0', '2016-04-08 16:16:01', 'aaaaaaaaa', 'aaaaaaaaaaaaaa', '411738656', '81982081', '五道口');

-- ----------------------------
-- Table structure for `task_status_tb`
-- ----------------------------
DROP TABLE IF EXISTS `task_status_tb`;
CREATE TABLE `task_status_tb` (
  `status` tinyint(30) NOT NULL,
  `status_desc` varchar(20) default NULL,
  PRIMARY KEY  (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_status_tb
-- ----------------------------
INSERT INTO `task_status_tb` VALUES ('0', '待处理');
INSERT INTO `task_status_tb` VALUES ('1', '处理中');
INSERT INTO `task_status_tb` VALUES ('2', '已完成');

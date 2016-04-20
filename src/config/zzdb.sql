/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50096
Source Host           : localhost:3306
Source Database       : zzdb

Target Server Type    : MYSQL
Target Server Version : 50096
File Encoding         : 65001

Date: 2016-04-20 14:52:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `provider_list_tb`
-- ----------------------------
DROP TABLE IF EXISTS `provider_list_tb`;
CREATE TABLE `provider_list_tb` (
  `company_id` varchar(30) NOT NULL,
  `company_name` varchar(100) default NULL,
  `server_type` varchar(30) default NULL,
  `order_count` int(11) default NULL,
  `server_grade` varchar(5) default NULL,
  `company_type` int(11) default NULL,
  `company_logo` varchar(50) default NULL,
  `longitude` int(11) default NULL,
  `latitude` int(11) default NULL,
  `location` varchar(100) default NULL,
  `company_desc` varchar(300) default NULL,
  PRIMARY KEY  (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of provider_list_tb
-- ----------------------------
INSERT INTO `provider_list_tb` VALUES ('c_1101685805001', '���������ۺ�����������', '0#1#2#3#4#5#6#7', '33608', 'A', '1', '/opt/zzfile/logo/00.jpg/opt/zzfile/logo/10.jpg', '418685151', '144130377', '�����к���������춫·', 'ƽ̨�����̱����ܲ�');
INSERT INTO `provider_list_tb` VALUES ('c_1160685805067', '����·��', '0', '1000', 'A', '0', '/opt/zzfile/logo/01.jpg', '418681371', '144104234', '�����к������������ּ�6��', '����·�������񱱾���·');
INSERT INTO `provider_list_tb` VALUES ('c_1160685805068', '����������', '1', '2380', 'A', '0', '/opt/zzfile/logo/02.jpg', '418629628', '144165189', '�����к��������԰��·', '����������ά���׶�����');
INSERT INTO `provider_list_tb` VALUES ('c_1160685805069', '��������', '2', '8900', 'A', '0', '/opt/zzfile/logo/03.jpg', '418706625', '144205243', '�����к������ϵ�ʮ��10��', '����������������������');
INSERT INTO `provider_list_tb` VALUES ('c_1160685805070', '�������ܾ�', '3#4', '11002', 'A', '0', '/opt/zzfile/logo/00.jpg', '418685670', '144148874', '�����к����������ݱ�·', '�������ܾ֣�ά��������ͨ��ȫ');
INSERT INTO `provider_list_tb` VALUES ('c_1160685805071', '�����й�����', '5#6#7', '26504', 'A', '0', '/opt/zzfile/logo/00.jpg', '418635738', '144139626', '�����к�������԰�н�', '�����й����֣�ά���׶�����ȫ����');
INSERT INTO `provider_list_tb` VALUES ('c_1460685805065', '�п�ʵҵ���ţ��عɣ���˾', '6#7', '789', 'A', '1', '/opt/zzfile/logo/2.jpg', '418722004', '143958646', '�����к��������ݽ�3�Ŵ��Ƽ�����15��	', '�п�ʵҵ����(�ع�)���޹�˾(���¼���пƼ���)���� ����ѧԺ�����Ĵ��͸߿Ƽ���ҵͶ�ʼ�����˾');
INSERT INTO `provider_list_tb` VALUES ('c_1460685805066', '���м���', '0#1', '100', 'A', '1', '/opt/zzfile/logo/1.jpg', '418608160', '144153888', '�����', 'ʼ����2012');
INSERT INTO `provider_list_tb` VALUES ('c_1460685805067', '�н���ҵ����˾', '0', '2235', 'B', '1', '/opt/zzfile/logo/00.jpg/opt/zzfile/logo/11.jpg', '418683859', '144148374', '�����к����������ݱ�·', '�н���ҵ����˾������1993�꣬���������й�����ҵ���Ľ�����˾�й����������ܹ�˾��ȫ���ӹ�˾');

-- ----------------------------
-- Table structure for `provider_type_tb`
-- ----------------------------
DROP TABLE IF EXISTS `provider_type_tb`;
CREATE TABLE `provider_type_tb` (
  `company_type` int(11) NOT NULL,
  `company_desc` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of provider_type_tb
-- ----------------------------
INSERT INTO `provider_type_tb` VALUES ('0', 'ְ�ܲ���');
INSERT INTO `provider_type_tb` VALUES ('1', '��ҵ����');
INSERT INTO `provider_type_tb` VALUES ('2', '����');

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
INSERT INTO `record_category_tb` VALUES ('0', '��·ά��');
INSERT INTO `record_category_tb` VALUES ('1', '������Ⱦ');
INSERT INTO `record_category_tb` VALUES ('2', '������ʩ');
INSERT INTO `record_category_tb` VALUES ('3', 'Υ��ͣ��');
INSERT INTO `record_category_tb` VALUES ('4', '��ͨӵ��');
INSERT INTO `record_category_tb` VALUES ('5', 'Υ������');
INSERT INTO `record_category_tb` VALUES ('6', 'ͻ���¼�');
INSERT INTO `record_category_tb` VALUES ('7', '����Ͷ��');

-- ----------------------------
-- Table structure for `record_task_tb`
-- ----------------------------
DROP TABLE IF EXISTS `record_task_tb`;
CREATE TABLE `record_task_tb` (
  `task_id` varchar(30) NOT NULL,
  `task_type` tinyint(1) default NULL,
  `company_id` varchar(30) default NULL,
  `create_time` datetime default NULL,
  `dispatch_time` datetime default NULL,
  `end_time` datetime default NULL,
  `status` tinyint(4) default NULL,
  `record_count` int(11) default NULL,
  `comment` varchar(200) default NULL,
  PRIMARY KEY  (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of record_task_tb
-- ----------------------------
INSERT INTO `record_task_tb` VALUES ('t_1460969143648', '1', null, '2016-04-18 16:45:43', null, null, '0', '3', 'comments here');
INSERT INTO `record_task_tb` VALUES ('t_1460969319694', '0', null, '2016-04-18 16:48:39', null, null, '0', '2', 'comments here');
INSERT INTO `record_task_tb` VALUES ('t_1460969433710', '2', null, '2016-04-18 16:50:33', null, null, '0', '2', 'comments here');
INSERT INTO `record_task_tb` VALUES ('t_1460969483620', '5', null, '2016-04-18 16:51:23', null, null, '0', '1', 'comments here');
INSERT INTO `record_task_tb` VALUES ('t_1460973019636', '7', null, '2016-04-18 17:50:19', null, null, '0', '2', 'comments here');
INSERT INTO `record_task_tb` VALUES ('t_1460973085905', '1', null, '2016-04-18 17:51:25', null, null, '1', '6', 'comments here');

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
INSERT INTO `report_record_orig` VALUES ('r_1460969143648', 't_1460969143648', '15210433578', '0', '1', '2016-04-18 16:45:41', '��111', '\\zzfile\\2016\\4\\18\\16\\1460969143425.jpg', '418607680', '144153488', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460969221453', 't_1460969143648', '15210433578', '0', '1', '2016-04-18 16:47:00', '��222', '\\zzfile\\2016\\4\\18\\16\\1460969221254.jpg', '418607680', '144153488', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460969269618', 't_1460969143648', '15210433578', '0', '1', '2016-04-18 16:47:48', '��333', '\\zzfile\\2016\\4\\18\\16\\1460969269497.jpg', '418607680', '144153488', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460969319694', 't_1460969319694', '15210433578', '0', '0', '2016-04-18 16:48:38', '������444', '\\zzfile\\2016\\4\\18\\16\\1460969319485.jpg', '418608032', '144153664', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460969354174', 't_1460969319694', '15210433578', '0', '0', '2016-04-18 16:49:12', '������555', '\\zzfile\\2016\\4\\18\\16\\1460969353629-1.jpg#\\zzfile\\2016\\4\\18\\16\\1460969353629-2.jpg', '418608032', '144153664', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460969433710', 't_1460969433710', '15210433578', '0', '2', '2016-04-18 16:50:33', '������666', '\\zzfile\\2016\\4\\18\\16\\1460969433501.jpg', '418608032', '144153664', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460969452632', 't_1460969433710', '15210433578', '0', '2', '2016-04-18 16:50:51', '����777', '\\zzfile\\2016\\4\\18\\16\\1460969452432.jpg', '418608032', '144153664', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460969483620', 't_1460969483620', '15210433578', '0', '5', '2016-04-18 16:51:22', '����888', '\\zzfile\\2016\\4\\18\\16\\1460969483451.jpg', '418608032', '144153664', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460973019636', 't_1460973019636', '15210433578', '0', '7', '2016-04-18 17:50:18', '11111', '\\zzfile\\2016\\4\\18\\17\\1460973019503.jpg', '418608032', '144153664', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460973049992', 't_1460973019636', '15210433578', '0', '7', '2016-04-18 17:50:49', '2222', '\\zzfile\\2016\\4\\18\\17\\1460973049789.jpg', '418608032', '144153664', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460973085905', 't_1460973085905', '15210433578', '0', '1', '2016-04-18 17:51:24', '333', '\\zzfile\\2016\\4\\18\\17\\1460973085659.jpg', '418608032', '144153664', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460973128417', 't_1460973085905', '15210433578', '0', '1', '2016-04-18 17:52:07', '444', '\\zzfile\\2016\\4\\18\\17\\1460973128223.jpg', '418608032', '144153664', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460973144311', 't_1460973085905', '15210433578', '0', '1', '2016-04-18 17:52:23', '444', '\\zzfile\\2016\\4\\18\\17\\1460973144129.jpg', '418608032', '144153664', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460973155255', 't_1460973085905', '15210433578', '0', '1', '2016-04-18 17:52:34', '444', '\\zzfile\\2016\\4\\18\\17\\1460973155236.jpg', '418608032', '144153664', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460973212379', 't_1460973085905', '15210433578', '0', '1', '2016-04-18 17:53:31', '������', '\\zzfile\\2016\\4\\18\\17\\1460973212265.jpg', '418608032', '144153664', '��������������·');
INSERT INTO `report_record_orig` VALUES ('r_1460973247645', 't_1460973085905', '15210433578', '0', '1', '2016-04-18 17:54:06', '���������ظ���', '\\zzfile\\2016\\4\\18\\17\\1460973247463.jpg', '418608032', '144153664', '��������������·');

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
INSERT INTO `task_status_tb` VALUES ('0', '������');
INSERT INTO `task_status_tb` VALUES ('1', '������');
INSERT INTO `task_status_tb` VALUES ('2', '�����');

-- ----------------------------
-- Table structure for `user_info_tb`
-- ----------------------------
DROP TABLE IF EXISTS `user_info_tb`;
CREATE TABLE `user_info_tb` (
  `report_phone` varchar(11) NOT NULL,
  `phone_type` tinyint(1) default NULL,
  `token` varchar(64) default NULL,
  PRIMARY KEY  (`report_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_info_tb
-- ----------------------------
INSERT INTO `user_info_tb` VALUES ('13800000003', '0', 'ef8fdd817022c0f0408f6b1d36be305628fcbb9c9e5fa828b9525d4c4a346e9d');

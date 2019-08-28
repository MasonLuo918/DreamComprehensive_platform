/*
 Navicat Premium Data Transfer

 Source Server         : Database_Mysql
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost
 Source Database       : Comprehensive_Survey_Platform

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : utf-8

 Date: 08/28/2019 22:34:40 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `activity`
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动id',
  `name` varchar(255) NOT NULL COMMENT '活动名称',
  `time` date NOT NULL COMMENT '活动时间',
  `material` varchar(255) DEFAULT NULL COMMENT '志愿时证明材料文件uuid',
  `volunteer_time` varchar(255) DEFAULT NULL COMMENT '志愿时证明文档uuid',
  `activity_prove` varchar(255) DEFAULT NULL COMMENT '活动证明文档uuid',
  `department_id` int(11) NOT NULL COMMENT '活动所属组织的id',
  `section_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `activity`
-- ----------------------------
BEGIN;
INSERT INTO `activity` VALUES ('1', 'da', '2019-08-06', '1', '2', '', '30', '2'), ('2', 'dfalkj', '2019-08-06', '2', '', '', '30', null), ('3', 'IT', '2019-08-08', 'abc', 'abc', null, '50', null);
COMMIT;

-- ----------------------------
--  Table structure for `activity_prove`
-- ----------------------------
DROP TABLE IF EXISTS `activity_prove`;
CREATE TABLE `activity_prove` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '志愿时id',
  `act_id` int(11) NOT NULL COMMENT '所属活动id',
  `volun_time_num` decimal(5,1) DEFAULT NULL COMMENT '志愿时个数',
  `stu_num` varchar(255) NOT NULL COMMENT '学号',
  `stu_name` varchar(20) NOT NULL COMMENT '学生姓名',
  `stu_class` varchar(255) DEFAULT NULL COMMENT '学生班级',
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '1 代表志愿时公示， 0 代表活动分，如果是1则volun_time_num不能为空，0的话必须为空',
  PRIMARY KEY (`id`),
  KEY `act_id_index` (`act_id`),
  KEY `stu_score_index` (`stu_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `department`
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '组织id',
  `password` varchar(255) NOT NULL COMMENT '用户密码，进行加密之后的密码',
  `dept_name` varchar(255) NOT NULL COMMENT '组织名称',
  `college` varchar(255) NOT NULL COMMENT '所属院系',
  `email` varchar(50) NOT NULL COMMENT '用来接收验证信息的邮箱',
  `status` int(11) NOT NULL DEFAULT '0',
  `create_time` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `department`
-- ----------------------------
BEGIN;
INSERT INTO `department` VALUES ('30', '69f3574f4f008accc1b31695e7551bb8', '第一', '海洋学院', 'masonluo918@gmail.com', '1', '2019-08-17');
COMMIT;

-- ----------------------------
--  Table structure for `oauth`
-- ----------------------------
DROP TABLE IF EXISTS `oauth`;
CREATE TABLE `oauth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL COMMENT '用户在网站里的唯一标示id',
  `open_id` varchar(255) NOT NULL COMMENT 'qq 用户唯一标识id',
  `access_token` varchar(255) DEFAULT NULL,
  `open_type` int(11) DEFAULT NULL,
  `expired_time` bigint(20) DEFAULT NULL COMMENT '令牌过期时间，单位为秒',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `section`
-- ----------------------------
DROP TABLE IF EXISTS `section`;
CREATE TABLE `section` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `department_id` int(11) NOT NULL COMMENT '所属组织',
  `account` varchar(32) NOT NULL COMMENT '部门账号',
  `name` varchar(255) NOT NULL COMMENT '部门名称',
  `password` varchar(20) NOT NULL COMMENT '部门登录密码',
  `status` int(1) NOT NULL COMMENT '激活状态',
  `create_time` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `account_index` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='各大组织下属部门的数据表';

-- ----------------------------
--  Table structure for `sign_in`
-- ----------------------------
DROP TABLE IF EXISTS `sign_in`;
CREATE TABLE `sign_in` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `stu_id` varchar(12) NOT NULL COMMENT '学生学号',
  `stu_name` varchar(255) NOT NULL COMMENT '学生姓名',
  `stu_profession_class` varchar(255) NOT NULL COMMENT '专业和班级',
  `activity_id` int(11) NOT NULL COMMENT '活动id',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='学生签到数据表';

-- ----------------------------
--  Records of `sign_in`
-- ----------------------------
BEGIN;
INSERT INTO `sign_in` VALUES ('4', '201725010116', '俊权', 'jfalkjfldaj', '1', null, '2019-08-22'), ('5', '21345', '罗俊权dmala', 'dfjadfalndlfa', '2', null, '2019-08-22'), ('6', '21345', '罗俊权dmal', 'dfjadfalndlfa', '2', null, '2019-08-22'), ('7', '21345', '罗俊权', 'dfjalkjl', '2', null, '2019-08-22');
COMMIT;

-- ----------------------------
--  Table structure for `upload_file`
-- ----------------------------
DROP TABLE IF EXISTS `upload_file`;
CREATE TABLE `upload_file` (
  `uuid` varchar(255) NOT NULL,
  `path` text NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `upload_file`
-- ----------------------------
BEGIN;
INSERT INTO `upload_file` VALUES ('1', '1', '物品.zip'), ('2', '2', '操作.zip');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

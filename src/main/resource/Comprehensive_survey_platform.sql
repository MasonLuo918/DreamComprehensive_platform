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

 Date: 08/04/2019 00:47:23 AM
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
  `material_url` varchar(255) DEFAULT NULL COMMENT '志愿时证明材料url',
  `volun_time_doc_url` varchar(255) DEFAULT NULL COMMENT 'Voluntary time document url，志愿时文档url',
  `activity_score_doc_url` varchar(255) DEFAULT NULL COMMENT '活动分文档url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `activity_prove`
-- ----------------------------
DROP TABLE IF EXISTS `activity_prove`;
CREATE TABLE `activity_prove` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '志愿时id',
  `act_id` int(11) NOT NULL COMMENT '所属活动id',
  `volun_time_num` decimal(5,1) DEFAULT '0.0' COMMENT '志愿时个数',
  `stu_num` varchar(255) NOT NULL COMMENT '学号',
  `stu_name` varchar(20) NOT NULL COMMENT '学生姓名',
  `stu_class` varchar(255) DEFAULT NULL COMMENT '学生班级',
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '1 代表志愿时公示， 0 代表活动分，如果是1则volun_time_num不能为空，0的话必须为空',
  PRIMARY KEY (`id`),
  KEY `act_id_index` (`act_id`),
  KEY `stu_score_index` (`stu_num`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='各大组织下属部门的数据表';

SET FOREIGN_KEY_CHECKS = 1;

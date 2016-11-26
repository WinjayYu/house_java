/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50520
Source Host           : localhost:3306
Source Database       : zaja

Target Server Type    : MYSQL
Target Server Version : 50520
File Encoding         : 65001

Date: 2016-11-26 14:59:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for area
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `add_by` int(11) DEFAULT NULL,
  `last_modified_time` datetime DEFAULT NULL,
  `last_modified_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小区';

-- ----------------------------
-- Table structure for buy_house_demand
-- ----------------------------
DROP TABLE IF EXISTS `buy_house_demand`;
CREATE TABLE `buy_house_demand` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `min_price` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '要求价格最小值',
  `max_price` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '要求价格最大值',
  `house_type` varchar(50) NOT NULL,
  `fitment_level_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='买方需求表';

-- ----------------------------
-- Table structure for fitment_level
-- ----------------------------
DROP TABLE IF EXISTS `fitment_level`;
CREATE TABLE `fitment_level` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '装修程度名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='装修程度';

-- ----------------------------
-- Table structure for house
-- ----------------------------
DROP TABLE IF EXISTS `house`;
CREATE TABLE `house` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `house_type` varchar(50) NOT NULL DEFAULT '' COMMENT '房屋类型',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '位置',
  `area` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '房屋面积',
  `sale_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '售价',
  `source_house_status` varchar(10) NOT NULL DEFAULT '' COMMENT '源房屋状态：10=已发布',
  `middleman_house_status` varchar(255) NOT NULL DEFAULT '' COMMENT '10=待审核、20=上架、30=下架',
  `longitude` varchar(50) NOT NULL DEFAULT '' COMMENT '经度',
  `latitude` varchar(50) NOT NULL DEFAULT '' COMMENT '纬度',
  `type` varchar(255) NOT NULL DEFAULT '' COMMENT '10=卖房发布、20=经济人二次发布、30=经济人一手发布',
  `source_house_id` int(11) NOT NULL DEFAULT '0' COMMENT '源房屋id',
  `publish_date` datetime NOT NULL,
  `tags` varchar(255) NOT NULL DEFAULT '',
  `year` varchar(10) NOT NULL DEFAULT '' COMMENT '年代',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='房屋';

-- ----------------------------
-- Table structure for house_feature
-- ----------------------------
DROP TABLE IF EXISTS `house_feature`;
CREATE TABLE `house_feature` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `house_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `content` varchar(500) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='房屋特色';

-- ----------------------------
-- Table structure for house_image
-- ----------------------------
DROP TABLE IF EXISTS `house_image`;
CREATE TABLE `house_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `house_id` int(11) NOT NULL,
  `path` varchar(500) NOT NULL COMMENT '七牛路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='房屋图片';

-- ----------------------------
-- Table structure for middleman_house
-- ----------------------------
DROP TABLE IF EXISTS `middleman_house`;
CREATE TABLE `middleman_house` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `house_id` int(11) NOT NULL,
  `status` varchar(10) NOT NULL DEFAULT '10' COMMENT '房屋状态：10=保存、20=待审核、30=上架、40=下架',
  `add_time` datetime DEFAULT NULL,
  `add_by` int(11) DEFAULT NULL,
  `last_modified_time` datetime DEFAULT NULL,
  `last_modified_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='经济人发布房屋信息';

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL COMMENT '订单号',
  `middleman_id` int(11) NOT NULL COMMENT '经济人id',
  `seller_id` int(11) NOT NULL COMMENT '卖方id',
  `house_id` int(11) NOT NULL COMMENT '房屋id',
  `status` varchar(10) NOT NULL COMMENT '订单状态：10=待付款、20=房屋交接中、30=用户确认、40=待评价、50=已完成',
  `add_time` datetime NOT NULL,
  `buyer_mobile` varchar(50) NOT NULL,
  `buyer_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单';

-- ----------------------------
-- Table structure for sell_house_demand
-- ----------------------------
DROP TABLE IF EXISTS `sell_house_demand`;
CREATE TABLE `sell_house_demand` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `house_id` int(11) NOT NULL,
  `middleman_id` int(11) DEFAULT NULL COMMENT '经济人id',
  `status` int(10) DEFAULT NULL COMMENT '状态：10=保存、20=发布、30=经济人已接单',
  `commission` int(11) DEFAULT NULL COMMENT '佣金',
  `add_time` datetime DEFAULT NULL,
  `add_by` int(11) DEFAULT NULL,
  `last_modified_time` datetime DEFAULT NULL,
  `last_modified_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='卖方需求表';

-- ----------------------------
-- Table structure for send_order
-- ----------------------------
DROP TABLE IF EXISTS `send_order`;
CREATE TABLE `send_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `middleman_id` int(11) NOT NULL DEFAULT '0',
  `house_id` int(11) NOT NULL DEFAULT '0',
  `status` varchar(10) NOT NULL DEFAULT '10' COMMENT '10=已派送、20=已接单',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派单';

-- ----------------------------
-- Table structure for third_user
-- ----------------------------
DROP TABLE IF EXISTS `third_user`;
CREATE TABLE `third_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `openid` varchar(50) NOT NULL,
  `type` varchar(10) NOT NULL DEFAULT '' COMMENT '10=QQ、20=微信、30=微博',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方登录';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
  `mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号',
  `type` varchar(10) NOT NULL COMMENT '用户类型：10=后台管理员、20=用户、30=经济人',
  `middleman_register_status` varchar(10) NOT NULL DEFAULT '' COMMENT '注册状态：10=待审核、20=审核通过，注册成功',
  `nickname` varchar(255) NOT NULL DEFAULT '',
  `head` varchar(500) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

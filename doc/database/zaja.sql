/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : zaja

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2016-11-30 11:23:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for agent_material
-- ----------------------------
DROP TABLE IF EXISTS `agent_material`;
CREATE TABLE `agent_material` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for background_img
-- ----------------------------
DROP TABLE IF EXISTS `background_img`;
CREATE TABLE `background_img` (
  `id` int(11) NOT NULL,
  `img` varchar(500) DEFAULT NULL COMMENT '图片url',
  `describe` varchar(255) DEFAULT NULL COMMENT '图片描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for buy_house
-- ----------------------------
DROP TABLE IF EXISTS `buy_house`;
CREATE TABLE `buy_house` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `min_price` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '要求价格最小值',
  `max_price` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '要求价格最大值',
  `house_type` varchar(50) NOT NULL DEFAULT '' COMMENT '户型',
  `fitment_level` varchar(11) NOT NULL DEFAULT '0' COMMENT '装修程度',
  `community_id` int(11) NOT NULL DEFAULT '0' COMMENT '小区id',
  `add_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `last_modified_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_oqpk2cdqw9wig0b8mb7xx6m1i` (`community_id`),
  KEY `FK_9r1xfs0j77g05sj5rgx8pv6mo` (`fitment_level`),
  KEY `FK_je50gpe4jgbx772m449hmvul0` (`user_id`),
  CONSTRAINT `FK_je50gpe4jgbx772m449hmvul0` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='买方需求表';

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `house_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收藏表';

-- ----------------------------
-- Table structure for community
-- ----------------------------
DROP TABLE IF EXISTS `community`;
CREATE TABLE `community` (
  `uid` varchar(11) NOT NULL,
  `name` varchar(255) DEFAULT '' COMMENT '小区名',
  `adcode` varchar(255) DEFAULT NULL COMMENT '邮编',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `id` int(11) NOT NULL,
  `add_time` datetime DEFAULT NULL,
  `last_modified_by` int(11) DEFAULT NULL,
  `last_modified_time` datetime DEFAULT NULL,
  `add_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_j9djsni04pdhqy3w1nu5hetww` (`add_by`),
  CONSTRAINT `FK_j9djsni04pdhqy3w1nu5hetww` FOREIGN KEY (`add_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小区';

-- ----------------------------
-- Table structure for house
-- ----------------------------
DROP TABLE IF EXISTS `house`;
CREATE TABLE `house` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '经纪人id',
  `house_type` varchar(50) DEFAULT '' COMMENT '房屋类型',
  `address` varchar(255) DEFAULT '' COMMENT '位置',
  `area` decimal(10,2) DEFAULT '0.00' COMMENT '房屋面积',
  `sell_price` decimal(10,2) DEFAULT '0.00' COMMENT '售价',
  `status` varchar(255) DEFAULT '' COMMENT '10=待审核、20=上架、30=下架',
  `longitude` varchar(50) DEFAULT '' COMMENT '经度',
  `latitude` varchar(50) DEFAULT '' COMMENT '纬度',
  `type` varchar(255) DEFAULT '' COMMENT '10=经济人二次发布、20=经济人一手发布',
  `sell_house_id` int(11) DEFAULT '0' COMMENT '源房屋id',
  `publish_date` datetime DEFAULT NULL COMMENT '发布时间',
  `tags` varchar(255) DEFAULT '' COMMENT '标签，如(近地铁|交通方便)，中间以|隔开',
  `year` varchar(10) DEFAULT '' COMMENT '年代',
  `feature` varchar(255) DEFAULT NULL COMMENT '房屋特色',
  `imgs` varchar(2000) DEFAULT NULL COMMENT '图片',
  `fitment_level` varchar(255) DEFAULT NULL COMMENT '装修程度',
  `commission` decimal(10,3) DEFAULT NULL COMMENT '佣金',
  `direction` varchar(10) DEFAULT NULL COMMENT '朝向',
  PRIMARY KEY (`id`),
  KEY `FK_3cuicb608pp7ye1uwdase8kdc` (`user_id`),
  CONSTRAINT `FK_3cuicb608pp7ye1uwdase8kdc` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='房屋';

-- ----------------------------
-- Table structure for house_order
-- ----------------------------
DROP TABLE IF EXISTS `house_order`;
CREATE TABLE `house_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL COMMENT '订单号',
  `user_id` int(11) DEFAULT '0' COMMENT '经济人id',
  `seller_id` int(11) DEFAULT '0' COMMENT '卖方id',
  `house_id` int(11) DEFAULT '0' COMMENT '房屋id',
  `status` varchar(10) DEFAULT '10' COMMENT '订单状态：10=待付款、20=房屋交接中、30=用户确认、40=待评价、50=已完成',
  `add_time` datetime DEFAULT NULL,
  `buyer_mobile` varchar(50) DEFAULT '',
  `buyer_id` int(11) DEFAULT '0' COMMENT '买家id',
  `middleman_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_rg7f2tiw4ir7fckr98k5tsx1x` (`buyer_id`),
  KEY `FK_1i8xt85c0l5xqgxsnbymg9o3n` (`house_id`),
  KEY `FK_2m8tspeqjribqv0gy4vqp890q` (`user_id`),
  KEY `FK_egcr1b4d5yp0errwa0rhris8j` (`seller_id`),
  CONSTRAINT `FK_1i8xt85c0l5xqgxsnbymg9o3n` FOREIGN KEY (`house_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_2m8tspeqjribqv0gy4vqp890q` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_egcr1b4d5yp0errwa0rhris8j` FOREIGN KEY (`seller_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_rg7f2tiw4ir7fckr98k5tsx1x` FOREIGN KEY (`buyer_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='订单';

-- ----------------------------
-- Table structure for sell_house
-- ----------------------------
DROP TABLE IF EXISTS `sell_house`;
CREATE TABLE `sell_house` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `status` varchar(5) DEFAULT NULL COMMENT '状态：10=保存、20=发布、30=经济人已接单',
  `add_time` datetime DEFAULT NULL,
  `last_modified_time` datetime DEFAULT NULL,
  `community_uid` varchar(255) DEFAULT NULL COMMENT '小区id',
  `house_type` varchar(255) DEFAULT NULL COMMENT '户型',
  `sell_price` decimal(10,2) DEFAULT NULL COMMENT '售价',
  `model` varchar(255) DEFAULT NULL COMMENT '房屋类型',
  `fitment_level` varchar(255) DEFAULT NULL COMMENT '装修程度',
  `area` double(255,0) DEFAULT NULL COMMENT '面积',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='卖方需求表';

-- ----------------------------
-- Table structure for send_order
-- ----------------------------
DROP TABLE IF EXISTS `send_order`;
CREATE TABLE `send_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0',
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
  `head` varchar(500) NOT NULL DEFAULT '' COMMENT '头像',
  `nickname` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方登录';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT '' COMMENT '用户名',
  `password` varchar(50) DEFAULT '' COMMENT '密码',
  `sex` varchar(255) DEFAULT NULL COMMENT '性别',
  `mobile` varchar(20) DEFAULT '' COMMENT '手机号',
  `type` varchar(10) DEFAULT '10' COMMENT '用户类型：10=后台管理员，20=用户，30=经纪人',
  `nickname` varchar(255) DEFAULT '' COMMENT '昵称',
  `head` varchar(500) DEFAULT '' COMMENT '头像',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=utf8 COMMENT='用户';
SET FOREIGN_KEY_CHECKS=1;

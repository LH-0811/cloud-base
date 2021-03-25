/*
 Navicat Premium Data Transfer

 Source Server         : 本地连接
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : example-storage

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 25/03/2021 20:33:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_storage
-- ----------------------------
DROP TABLE IF EXISTS `t_storage`;
CREATE TABLE `t_storage` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint DEFAULT NULL COMMENT '产品id',
  `total` int DEFAULT NULL COMMENT '总库存',
  `used` int DEFAULT NULL COMMENT '已用库存',
  `residue` int DEFAULT NULL COMMENT '剩余库存',
  `price` decimal(10,2) DEFAULT NULL COMMENT '单价',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_storage
-- ----------------------------
BEGIN;
INSERT INTO `t_storage` VALUES (1, 1, 100, 0, 100, 10.00);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

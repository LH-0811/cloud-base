/*
 Navicat Premium Data Transfer

 Source Server         : 本地连接
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : test1

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 25/03/2021 15:16:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_people
-- ----------------------------
DROP TABLE IF EXISTS `t_people`;
CREATE TABLE `t_people` (
  `id` bigint NOT NULL COMMENT '主键id',
  `name` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
  `age` int DEFAULT NULL COMMENT '年龄',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_people
-- ----------------------------
BEGIN;
INSERT INTO `t_people` VALUES (1336672865801150464, 'test1', 12);
INSERT INTO `t_people` VALUES (1336673186606686208, 'test2', 13);
INSERT INTO `t_people` VALUES (1336673484494536704, 'test2', 13);
INSERT INTO `t_people` VALUES (1336674488845799424, 'test2', 13);
INSERT INTO `t_people` VALUES (1336675635325247488, 'test5', 13);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

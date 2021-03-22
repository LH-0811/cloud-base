/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50715
 Source Host           : localhost:3306
 Source Schema         : security

 Target Server Type    : MySQL
 Target Server Version : 50715
 File Encoding         : 65001

 Date: 05/12/2019 13:59:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_res
-- ----------------------------
DROP TABLE IF EXISTS `sys_res`;
CREATE TABLE `sys_res` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '父节点id',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '资源名称',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '资源类型0-未定义 1-菜单 2-按钮 3-接口',
  `perms` varchar(64) DEFAULT NULL COMMENT '资源权限码',
  `path` varchar(255) DEFAULT '' COMMENT '资源父级路径',
  `url` varchar(255) DEFAULT '' COMMENT '资源指向url',
  `icon` varchar(64) DEFAULT '' COMMENT '资源icon',
  `compent` varchar(64) DEFAULT '' COMMENT '资源对应前端组件',
  `active_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用',
  `order_num` int(11) DEFAULT '0' COMMENT '排序序号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_res
-- ----------------------------
BEGIN;
INSERT INTO `sys_res` VALUES (1, 0, '系统管理', 1, 'system_mgr', '0', '', '', '', 1, 0, '2019-11-24 20:56:52', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (2, 1, '用户管理', 1, 'system_user_mgr', '0,1', '', '', '', 1, 0, '2019-11-24 20:57:42', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (3, 2, '增加用户按钮', 2, 'system_user_add_btn', '0,1,2', '', '', '', 1, 0, '2019-11-24 20:58:59', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (4, 2, '删除用户按钮', 2, 'system_user_delete_btn', '0,1,2', '', '', '', 1, 0, '2019-11-24 20:58:59', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (5, 2, '查询用户按钮', 2, 'system_user_query_btn', '0,1,2', '', '', '', 1, 0, '2019-11-24 20:58:59', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (6, 2, '修改用户按钮', 2, 'system_user_update_btn', '0,1,2', '', '', '', 1, 0, '2019-11-24 20:58:59', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (7, 2, '增加用户接口', 3, 'system_user_add_interface', '0,1,2', '/user/add', '', '', 1, 0, '2019-11-24 20:58:59', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (8, 2, '删除用户接口', 3, 'system_user_delete_interface', '0,1,2', '/user/delete/*', '', '', 1, 0, '2019-11-24 20:58:59', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (9, 2, '修改用户接口', 3, 'system_user_update_interface', '0,1,2', '/user/update', '', '', 1, 0, '2019-11-24 20:58:59', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (10, 2, '查询用户接口', 3, 'system_user_query_interface', '0,1,2', '/user/query', '', '', 0, 0, '2019-11-24 20:58:59', NULL, 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '角色名称',
  `key` varchar(64) NOT NULL COMMENT '权限key',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '管理员', 'admin', '2019-11-24 20:55:47', NULL, 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_res
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_res`;
CREATE TABLE `sys_role_res` (
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `res_id` bigint(20) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`role_id`,`res_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_role_res
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_res` VALUES (1, 1);
INSERT INTO `sys_role_res` VALUES (1, 2);
INSERT INTO `sys_role_res` VALUES (1, 3);
INSERT INTO `sys_role_res` VALUES (1, 4);
INSERT INTO `sys_role_res` VALUES (1, 5);
INSERT INTO `sys_role_res` VALUES (1, 6);
INSERT INTO `sys_role_res` VALUES (1, 7);
INSERT INTO `sys_role_res` VALUES (1, 8);
INSERT INTO `sys_role_res` VALUES (1, 10);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(64) NOT NULL DEFAULT '' COMMENT '用户名',
  `nick_name` varchar(64) NOT NULL DEFAULT '' COMMENT '用户昵称',
  `password` varchar(255) NOT NULL DEFAULT '' COMMENT '密码',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `last_login` datetime DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 'admin', '管理员', '123456', '2019-11-24 20:54:14', NULL, 0, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (1, 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

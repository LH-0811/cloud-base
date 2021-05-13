/*
 Navicat Premium Data Transfer

 Source Server         : 49.232.166.94_3306
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 49.232.166.94:3306
 Source Schema         : member-user

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 13/05/2021 14:44:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL COMMENT '字典id',
  `type` varchar(32) DEFAULT NULL COMMENT '字典类型',
  `dict_key` varchar(32) NOT NULL COMMENT '字典键',
  `dict_name` varchar(64) DEFAULT NULL COMMENT '字典名',
  `dict_value` varchar(64) NOT NULL COMMENT '字典值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统表-字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` VALUES (1, 'user_type', 'admin', '管理员用户', '1');
INSERT INTO `sys_dict` VALUES (2, 'user_type', 'merchant', '商户用户', '2');
INSERT INTO `sys_dict` VALUES (3, 'user_type', 'client_user', 'C端客户', '3');
COMMIT;

-- ----------------------------
-- Table structure for sys_res
-- ----------------------------
DROP TABLE IF EXISTS `sys_res`;
CREATE TABLE `sys_res` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源id',
  `user_type` int(11) NOT NULL COMMENT '资源所属用户类型（字典表 user_type）',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父节点id',
  `name` varchar(80) NOT NULL COMMENT '名称',
  `type` int(11) NOT NULL COMMENT '类型：1-"菜单" 2-"接口" 3-"权限码",4-"静态资源"',
  `code` varchar(255) DEFAULT NULL COMMENT '权限标识符',
  `url` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `order_no` int(11) DEFAULT NULL COMMENT '排序',
  `router` varchar(255) DEFAULT NULL COMMENT '路由',
  `notes` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1002 DEFAULT CHARSET=utf8mb4 COMMENT='系统-资源表';

-- ----------------------------
-- Records of sys_res
-- ----------------------------
BEGIN;
INSERT INTO `sys_res` VALUES (1, 0, 0, '系统管理', 1, NULL, NULL, NULL, NULL, '0', NULL, '2021-01-20 09:57:08', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (2, 0, 1, '用户管理', 1, '', '/users', 'user', 1, '0,1', NULL, '2021-03-01 21:55:41', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (3, 0, 2, '创建用户', 2, 'sys_admin:sys_user:create', '/sys_admin/sys_user/create', NULL, 1, '0,1,2', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (4, 0, 2, '修改用户', 2, 'sys_admin:sys_user:update', '/sys_admin/sys_user/update', NULL, 2, '0,1,2', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (5, 0, 2, '查询用户', 2, 'sys_admin:sys_user:query', '/sys_admin/sys_user/query', NULL, 3, '0,1,2', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (6, 0, 2, '设置用户角色', 2, 'sys_admin:sys_user:roles:set', '/sys_admin/sys_user/roles/set', NULL, 4, '0,1,2', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (8, 0, 1, '角色管理', 1, '', '/roles', 'role', 2, '0,1', NULL, '2021-03-01 22:00:37', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (9, 0, 8, '创建角色', 2, 'sys_admin:sys_role:create', '/sys_admin/sys_role/create', NULL, 1, '0,1,8', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (10, 0, 8, '修改角色', 2, 'sys_admin:sys_role:update', '/sys_admin/sys_role/update', NULL, 2, '0,1,8', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (11, 0, 8, '查询角色', 2, 'sys_admin:sys_role:query', '/sys_admin/sys_role/query', NULL, 3, '0,1,8', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (12, 0, 8, '删除角色', 2, 'sys_admin:sys_role:delete', '/sys_admin/sys_role/delete/{roleId}', NULL, 4, '0,1,8', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (13, 0, 8, '查询所有角色列表', 2, 'sys_admin:sys_role:query:all_list', '/sys_admin/sys_role/query/all_list', NULL, 5, '0,1,8', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (14, 0, 8, '查询角色资源列表', 2, 'sys_admin:sys_role:res:by_id:{roleId}', '/sys_admin/sys_role/res/by_id/{roleId}', NULL, 6, '0,1,8', NULL, '2021-03-01 22:07:09', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (15, 0, 8, '保存角色资源（权限）', 2, 'sys_admin:sys_role:save_res', '/sys_admin/sys_role/save_res', NULL, 7, '0,1,8', NULL, '2021-03-01 22:07:09', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (16, 0, 1, '资源管理', 1, '', '/resources', 'res', 3, '0,1', NULL, '2021-03-01 22:11:28', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (17, 0, 16, '创建权限', 2, 'sys_admin:sys_res:create', '/sys_admin/sys_res/create', NULL, 1, '0,1,16', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (18, 0, 16, '获取全部资源树', 2, 'sys_admin:sys_res:tree', '/sys_admin/sys_res/tree', NULL, 2, '0,1,16', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (19, 0, 16, '删除权限', 2, 'sys_admin:sys_res:delete', '/sys_admin/sys_res/delete/{resId}', NULL, 4, '0,1,16', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (20, 0, 1, '行政区域管理', 1, '', '/region', 'region', 4, '0,1', NULL, '2021-03-01 22:11:28', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (21, 0, 20, '创建区域信息', 2, 'sys_admin:region:create', '/sys_admin/region/create', NULL, 1, '0,1,20', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (22, 0, 20, '查询子区域列表', 2, 'sys_admin:region:list:by_pcode:{pcode}', '/sys_admin/region/list/by_pcode/{pcode}', NULL, 1, '0,1,20', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (23, 0, 20, '修改区域信息', 2, 'sys_admin:region:update', '/sys_admin/region/update', NULL, 1, '0,1,20', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (24, 0, 20, '删除区域信息', 2, 'sys_admin:region:delete', '/sys_admin/region/delete/{code}', NULL, 1, '0,1,20', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (1000, 0, 0, '系统管理员全PermsCode权限', 3, 'ALL', '/**', NULL, 0, '0', NULL, '2021-05-13 14:28:04', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (1001, 0, 0, '系统管理员全URL权限', 2, 'ALL', '/**', NULL, 0, '0', NULL, '2021-05-13 14:28:04', NULL, 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL COMMENT '角色id',
  `name` varchar(80) NOT NULL COMMENT '角色名称',
  `no` varchar(80) DEFAULT NULL COMMENT '角色编码',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用',
  `notes` varchar(255) DEFAULT NULL COMMENT '角色备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统-角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '系统管理员', 'ROLE001', 1, '最高权限', '2021-02-26 17:12:16', '2021-03-12 16:11:51', 0, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_res
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_res`;
CREATE TABLE `sys_role_res` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色权限关系id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `res_id` bigint(20) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `role_id` (`role_id`) USING BTREE COMMENT '角色id',
  KEY `permission_id` (`res_id`) USING BTREE COMMENT '权限id'
) ENGINE=InnoDB AUTO_INCREMENT=1370307961687486467 DEFAULT CHARSET=utf8mb4 COMMENT='系统-角色-权限关系表';

-- ----------------------------
-- Records of sys_role_res
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_res` VALUES (1, 1, 1000);
INSERT INTO `sys_role_res` VALUES (2, 1, 1001);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL COMMENT '系统管理员用户',
  `user_type` varchar(255) DEFAULT NULL COMMENT '1-系统管理员 2-商户 3-C端客户',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `salt` varchar(32) NOT NULL COMMENT '密码盐',
  `nick_name` varchar(64) DEFAULT NULL COMMENT '昵称',
  `phone` varchar(32) DEFAULT NULL COMMENT '手机号',
  `e_mail` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `token` varchar(512) DEFAULT NULL COMMENT '登录口令',
  `active_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `last_login` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户中心-管理员用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, '1', 'admin', '8d421e892a47dff539f46142eb09e56b', '1234', '管理员', '17666665555', '123@qq.com', NULL, 1, 0, NULL, '2021-04-24 15:39:20', 1, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL COMMENT '用户-角色关系表',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE COMMENT '用户id索引',
  KEY `role_id` (`role_id`) USING BTREE COMMENT '角色id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统-用户-角色关系表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

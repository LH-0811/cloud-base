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

 Date: 01/09/2021 22:49:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` bigint(20) NOT NULL COMMENT '部门id',
  `parent_id` bigint(20) NOT NULL COMMENT '父级部门id',
  `no` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '部门编号',
  `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '部门名称',
  `is_leaf` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否是叶子结点',
  `router` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '路径',
  `notes` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `idx_router` (`router`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户中心-部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES (1, 0, '00000001', '系统管理部门', 0, '0,1', '系统管理员', '2021-08-07 18:54:43', 0, '2021-08-07 18:54:45', 0);
INSERT INTO `sys_dept` VALUES (2, 1, '00000002', '测试部门1', 0, '0,1,2', '测试部门1', '2021-08-18 16:40:18', 0, '2021-08-18 16:40:20', NULL);
INSERT INTO `sys_dept` VALUES (3, 0, '00000003', '业务部门', 0, '0,3', '业务部门', '2021-08-18 16:40:44', 0, '2021-08-18 16:40:48', NULL);
INSERT INTO `sys_dept` VALUES (4, 0, '00000004', '风控部门', 1, '0,4', NULL, '2021-08-18 16:41:04', 0, '2021-08-18 16:41:07', NULL);
INSERT INTO `sys_dept` VALUES (5, 3, '00000005', '客户管理部', 1, '0,3,5', NULL, '2021-08-18 16:41:36', 0, '2021-08-18 16:41:40', NULL);
INSERT INTO `sys_dept` VALUES (6, 2, '00000006', '测试部门2', 1, '0,1,2,6', '测试部门2', '2021-08-18 16:41:59', 0, '2021-08-18 16:42:03', NULL);
COMMIT;

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
-- Table structure for sys_position
-- ----------------------------
DROP TABLE IF EXISTS `sys_position`;
CREATE TABLE `sys_position` (
  `id` bigint(20) NOT NULL COMMENT '岗位id',
  `no` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '岗位编号',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '岗位名称',
  `notes` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户中心-岗位信息';

-- ----------------------------
-- Records of sys_position
-- ----------------------------
BEGIN;
INSERT INTO `sys_position` VALUES (1, 'POST0001', '测试岗位', '测试岗位', '2021-08-27 21:48:51', 0, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_res
-- ----------------------------
DROP TABLE IF EXISTS `sys_res`;
CREATE TABLE `sys_res` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父节点id',
  `name` varchar(80) NOT NULL COMMENT '名称',
  `type` int(11) NOT NULL COMMENT '类型：1-"菜单" 2-"接口" 3-"权限码",4-"静态资源"',
  `code` varchar(255) DEFAULT NULL COMMENT '权限标识符',
  `url` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `order_no` int(11) DEFAULT NULL COMMENT '排序',
  `is_leaf` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否是叶子节点',
  `router` varchar(255) DEFAULT NULL COMMENT '路由',
  `notes` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_router` (`router`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1432348272776441857 DEFAULT CHARSET=utf8mb4 COMMENT='系统-资源表';

-- ----------------------------
-- Records of sys_res
-- ----------------------------
BEGIN;
INSERT INTO `sys_res` VALUES (1, 0, '系统管理', 1, NULL, NULL, NULL, NULL, 0, '0,1', NULL, '2021-01-20 09:57:08', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (1000, 0, '系统管理员全PermsCode权限', 3, 'ALL', '/**', NULL, 0, 1, '0,1000', NULL, '2021-05-13 14:28:04', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (1001, 0, '系统管理员全URL权限', 2, 'ALL', '/**', NULL, 0, 1, '0,1001', NULL, '2021-05-13 14:28:04', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (10000, 1, '用户管理', 1, '', '/system/users', 'user', 1, 0, '0,1,10000', NULL, '2021-03-01 21:55:41', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (10001, 10000, '创建用户', 2, 'sys_user:create', '/sys_user/create', NULL, 1, 1, '0,1,10000,10001', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (10002, 10000, '修改用户', 2, 'sys_user:update', '/sys_user/update', NULL, 2, 1, '0,1,10000,10002', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (10003, 10000, '查询用户', 2, 'sys_user:query', '/sys_user/query', NULL, 3, 1, '0,1,10000,10003', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (10004, 10000, '重置用户密码', 2, 'sys_user:reset:pwd', '/sys_user/reset/pwd', NULL, 4, 1, '0,1,10000,10004', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (10005, 10000, '查询部门级联列表', 2, 'sys_dept:cascader:query', '/sys_dept/cascader/query', NULL, 5, 1, '0,1,10000,10005', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (10006, 10000, '查询全部岗位列表', 2, 'sys_positions:query:all', '/sys_positions/query/all', NULL, 6, 1, '0,1,10000,10006', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (10007, 10000, '查询全部角色列表', 2, 'sys_role:query:all_list', '/sys_role/query/all_list', NULL, 7, 1, '0,1,10000,10007', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (10008, 10000, '查询部门树', 2, 'sys_dept:tree:query', '/sys_dept/tree/query', NULL, 8, 1, '0,1,10000,10008', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (10009, 10000, '查询部门用户信息', 2, 'sys_user:dept:query', '/sys_user/dept/query', NULL, 9, 1, '0,1,10000,10009', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (20000, 1, '角色管理', 1, '', '/system/roles', 'role', 2, 0, '0,1,20000', NULL, '2021-03-01 22:00:37', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (20001, 20000, '创建角色', 2, 'sys_role:create', '/sys_role/create', NULL, 1, 1, '0,1,20000,20001', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (20002, 20000, '修改角色', 2, 'sys_role:update', '/sys_role/update', NULL, 2, 1, '0,1,20000,20002', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (20003, 20000, '查询角色', 2, 'sys_role:query', '/sys_role/query', NULL, 3, 1, '0,1,20000,20003', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (20004, 20000, '删除角色', 2, 'sys_role:delete/{roleId}', '/sys_role/delete/{roleId}', NULL, 4, 1, '0,1,20000,20004', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (20005, 20000, '查询资源列表', 2, 'sys_res:tree', '/sys_res/tree', NULL, 6, 1, '0,1,20000,20005', NULL, '2021-03-01 22:07:09', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (30000, 1, '资源管理', 1, '', '/system/resources', 'res', 3, 0, '0,1,30000', NULL, '2021-03-01 22:11:28', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (30001, 30000, '创建权限', 2, 'sys_res:create', '/sys_res/create', NULL, 1, 1, '0,1,30000,30001', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (30002, 30000, '获取全部资源树', 2, 'sys_res:tree', '/sys_res/tree', NULL, 2, 1, '0,1,30000,30002', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (30003, 30000, '删除权限', 2, 'sys_res:delete:{resId}', '/sys_res/delete/{resId}', NULL, 4, 1, '0,1,30000,30003', NULL, '2021-03-01 21:57:24', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (40000, 1, '部门管理', 1, NULL, '/system/depts', 'dept', 4, 0, '0,1,40000', NULL, '2021-08-25 21:06:52', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (40001, 40000, '创建部门', 2, 'sys_dept:create', '/sys_dept/create', NULL, 1, 1, '0,1,40000,40001', NULL, '2021-08-25 21:06:52', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (40002, 40000, '获取部门树', 2, 'sys_dept:tree:query', '/sys_dept/tree/query', NULL, 2, 1, '0,1,40000,40002', NULL, '2021-08-25 21:06:52', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (40003, 40000, '删除部门', 2, 'sys_dept:delete:{deptId}', '/sys_dept/delete/{deptId}', NULL, 3, 1, '0,1,40000,40003', NULL, '2021-08-25 21:06:52', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (50000, 1, '岗位管理', 1, NULL, '/system/positions', 'position', 5, 0, '0,1,50000', NULL, '2021-08-25 21:06:52', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (50001, 50000, '创建岗位', 2, 'sys_position:create', '/sys_position/create', NULL, 1, 1, '0,1,50000,50001', NULL, '2021-08-25 21:06:52', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (50002, 50000, '删除岗位', 2, 'sys_position:delete:{positionId}', '/sys_position/delete/{positionId}', NULL, 2, 1, '0,1,50000,50002', NULL, '2021-08-25 21:06:52', NULL, 0, NULL);
INSERT INTO `sys_res` VALUES (50003, 50000, '查询岗位', 2, 'sys_position:query', '/sys_position/query', NULL, 3, 1, '0,1,50000,50003', NULL, '2021-08-25 21:06:52', NULL, 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL COMMENT '角色id',
  `name` varchar(80) NOT NULL COMMENT '角色名称',
  `no` varchar(80) DEFAULT NULL COMMENT '角色编码',
  `active_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用',
  `sort_num` int(11) DEFAULT '0' COMMENT '排序（升序）',
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
INSERT INTO `sys_role` VALUES (1, '系统管理员', 'ROLE001', 1, 1, '最高权限', '2021-02-26 17:12:16', '2021-03-12 16:11:51', 0, 1);
INSERT INTO `sys_role` VALUES (2, '商户管理员', 'ROLE002', 1, 2, '', '2021-05-31 16:57:43', '2021-09-01 22:42:07', 0, 1);
INSERT INTO `sys_role` VALUES (1417005479799832576, 'C端用户', 'ROLE003', 1, 3, '', '2021-07-19 14:16:48', '2021-09-01 22:45:57', 1, 1);
INSERT INTO `sys_role` VALUES (1432155140697194496, '用户管理', 'TEST001', 1, 4, NULL, '2021-08-30 09:36:08', '2021-09-01 22:42:24', 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_res_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_res_rel`;
CREATE TABLE `sys_role_res_rel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色权限关系id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `res_id` bigint(20) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `role_id` (`role_id`) USING BTREE COMMENT '角色id',
  KEY `permission_id` (`res_id`) USING BTREE COMMENT '权限id'
) ENGINE=InnoDB AUTO_INCREMENT=1433077784648511499 DEFAULT CHARSET=utf8mb4 COMMENT='系统-角色-权限关系表';

-- ----------------------------
-- Records of sys_role_res_rel
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_res_rel` VALUES (1417006204470706176, 1, 1000);
INSERT INTO `sys_role_res_rel` VALUES (1417006204470706177, 1, 1001);
INSERT INTO `sys_role_res_rel` VALUES (1433077784648511488, 1432155140697194496, 10000);
INSERT INTO `sys_role_res_rel` VALUES (1433077784648511489, 1432155140697194496, 10001);
INSERT INTO `sys_role_res_rel` VALUES (1433077784648511490, 1432155140697194496, 10002);
INSERT INTO `sys_role_res_rel` VALUES (1433077784648511491, 1432155140697194496, 10003);
INSERT INTO `sys_role_res_rel` VALUES (1433077784648511492, 1432155140697194496, 10004);
INSERT INTO `sys_role_res_rel` VALUES (1433077784648511493, 1432155140697194496, 10005);
INSERT INTO `sys_role_res_rel` VALUES (1433077784648511494, 1432155140697194496, 10006);
INSERT INTO `sys_role_res_rel` VALUES (1433077784648511495, 1432155140697194496, 10007);
INSERT INTO `sys_role_res_rel` VALUES (1433077784648511496, 1432155140697194496, 10008);
INSERT INTO `sys_role_res_rel` VALUES (1433077784648511497, 1432155140697194496, 10009);
INSERT INTO `sys_role_res_rel` VALUES (1433077784648511498, 1432155140697194496, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL COMMENT '系统管理员用户',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `gender` int(2) NOT NULL DEFAULT '0' COMMENT '0-保密 1-男 2-女',
  `salt` varchar(32) NOT NULL COMMENT '密码盐',
  `nick_name` varchar(64) DEFAULT NULL COMMENT '昵称',
  `phone` varchar(32) DEFAULT NULL COMMENT '手机号',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
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
INSERT INTO `sys_user` VALUES (1, 'admin', '8d421e892a47dff539f46142eb09e56b', 0, '1234', '管理员', '17666665555', '123@qq.com', NULL, 1, 0, '2021-09-01 22:38:39', '2021-04-24 15:39:20', 1, '2021-08-29 16:40:43', 1);
INSERT INTO `sys_user` VALUES (1431556382892097536, 'test01', '4f0920c2f6a171f4f07cc0efd06e7b9', 1, '㍗硄𥮀', 'test01', '13300000001', '123@qq.com', NULL, 1, 0, '2021-09-01 22:48:38', '2021-08-28 17:56:53', 1, '2021-09-01 21:35:18', 1431556382892097536);
INSERT INTO `sys_user` VALUES (1431899731574575104, 'test02', '4c65209d7f61d0e09ca3bbddd9203f66', 0, '𡳞삪헎', 'test02', '13300000003', '123@qq.com', NULL, 1, 1, NULL, '2021-08-29 16:41:14', 1, '2021-08-29 16:48:54', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_dept_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept_rel`;
CREATE TABLE `sys_user_dept_rel` (
  `id` bigint(20) NOT NULL COMMENT '用户部门关系id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `dept_id` bigint(20) NOT NULL COMMENT '部门id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户中心-用户部门信息关系表';

-- ----------------------------
-- Records of sys_user_dept_rel
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_dept_rel` VALUES (1, 1, 1);
INSERT INTO `sys_user_dept_rel` VALUES (1431556383433162752, 1431556382892097536, 4);
INSERT INTO `sys_user_dept_rel` VALUES (1431899731859787776, 1431899731574575104, 6);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_position_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_position_rel`;
CREATE TABLE `sys_user_position_rel` (
  `id` bigint(20) NOT NULL COMMENT '用户岗位关系id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `position_id` bigint(20) NOT NULL COMMENT '岗位id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户中心-用户岗位信息关系表';

-- ----------------------------
-- Records of sys_user_position_rel
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_position_rel` VALUES (1431899602431954944, 1, 1);
INSERT INTO `sys_user_position_rel` VALUES (1431899732111446016, 1431899731574575104, 1);
INSERT INTO `sys_user_position_rel` VALUES (1433060899504717824, 1431556382892097536, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role_rel`;
CREATE TABLE `sys_user_role_rel` (
  `id` bigint(20) NOT NULL COMMENT '用户-角色关系表',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE COMMENT '用户id索引',
  KEY `role_id` (`role_id`) USING BTREE COMMENT '角色id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统-用户-角色关系表';

-- ----------------------------
-- Records of sys_user_role_rel
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role_rel` VALUES (1431899602914299904, 1, 1);
INSERT INTO `sys_user_role_rel` VALUES (1431899732350521344, 1431899731574575104, 1);
INSERT INTO `sys_user_role_rel` VALUES (1431899732350521345, 1431899731574575104, 1417005479799832576);
INSERT INTO `sys_user_role_rel` VALUES (1433060899823484928, 1431556382892097536, 1432155140697194496);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

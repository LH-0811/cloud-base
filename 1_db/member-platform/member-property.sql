/*
 Navicat Premium Data Transfer

 Source Server         : 49.232.166.94_3306
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 49.232.166.94:3306
 Source Schema         : member-property

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 08/06/2021 21:12:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for prop_coupon_info
-- ----------------------------
DROP TABLE IF EXISTS `prop_coupon_info`;
CREATE TABLE `prop_coupon_info` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `template_id` bigint(20) NOT NULL COMMENT '模板id',
  `mcht_base_info_id` bigint(20) NOT NULL COMMENT '商户基本信息id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `expiry_time` datetime NOT NULL COMMENT '过期时间',
  `status` int(4) NOT NULL COMMENT '状态(0-初始化 1-已消费 2-已过期 3-已失效)',
  `invalid_time` datetime DEFAULT NULL COMMENT '失效时间',
  `invalid_by` bigint(20) DEFAULT NULL COMMENT '失效操作人',
  `invalid_reason` varchar(255) DEFAULT NULL COMMENT '失效原因',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产-优惠券信息表';

-- ----------------------------
-- Records of prop_coupon_info
-- ----------------------------
BEGIN;
INSERT INTO `prop_coupon_info` VALUES (1, 1, 1, 1, '2021-06-12 16:36:37', 0, NULL, NULL, NULL, 0, '2021-06-05 16:36:50', 1, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for prop_coupon_status_history
-- ----------------------------
DROP TABLE IF EXISTS `prop_coupon_status_history`;
CREATE TABLE `prop_coupon_status_history` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `coupon_info_id` bigint(20) NOT NULL COMMENT '优惠券id',
  `status` int(4) NOT NULL COMMENT '状态：0-初始化创建 1-消费 2-过期 3-失效',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产-优惠券生命周期历史';

-- ----------------------------
-- Table structure for prop_coupon_template
-- ----------------------------
DROP TABLE IF EXISTS `prop_coupon_template`;
CREATE TABLE `prop_coupon_template` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `mcht_base_info_id` bigint(20) NOT NULL COMMENT '商户基本信息id',
  `mcht_name` varchar(255) DEFAULT NULL COMMENT '商户名称',
  `mcht_address` varchar(255) DEFAULT NULL COMMENT '商户地址',
  `coupon_type` int(4) NOT NULL COMMENT '优惠券类型（1-线下 2-满减积分抵扣 3-折扣）',
  `describe` varchar(128) NOT NULL COMMENT '优惠券描述',
  `effective_minute` int(11) NOT NULL COMMENT '有效时长(分钟)',
  `full_score` int(11) DEFAULT NULL COMMENT '满减-满多少',
  `reduce_score` int(11) DEFAULT NULL COMMENT '满减-减多少',
  `discount_product_id` bigint(20) DEFAULT NULL COMMENT '折扣商品id',
  `full_number` int(11) DEFAULT NULL COMMENT '满折-满几件',
  `reduce_discount` int(11) DEFAULT NULL COMMENT '满折-打几折',
  `offline_function` varchar(1024) DEFAULT NULL COMMENT '线下能力说明',
  `enable_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否可用',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产-优惠券模板表';

-- ----------------------------
-- Records of prop_coupon_template
-- ----------------------------
BEGIN;
INSERT INTO `prop_coupon_template` VALUES (1, 1, '测试商户', '测试商户地址', 1, '线下券，赠一个辣子鸡', 10080, NULL, NULL, NULL, NULL, NULL, '辣子鸡一个', 1, 0, '2021-06-05 16:36:09', 0, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for prop_score_account
-- ----------------------------
DROP TABLE IF EXISTS `prop_score_account`;
CREATE TABLE `prop_score_account` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `mcht_id` bigint(20) NOT NULL COMMENT '商户id',
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '可用积分',
  `frozen_score` int(11) NOT NULL DEFAULT '0' COMMENT '冻结积分',
  `enable_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用 ',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产-用户积分账号';

-- ----------------------------
-- Table structure for prop_score_history
-- ----------------------------
DROP TABLE IF EXISTS `prop_score_history`;
CREATE TABLE `prop_score_history` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `account_id` bigint(20) NOT NULL COMMENT '账户id',
  `score_type` int(2) DEFAULT NULL COMMENT '动账积分类型 1-积分 2-冻结解放',
  `type` int(4) NOT NULL COMMENT '动账类型： 0-账户新建 1-充值 2-消费 3-赠与 4-冻结 5-解冻 9-其他',
  `notes` varchar(255) DEFAULT NULL COMMENT '备注',
  `score_change` int(11) NOT NULL DEFAULT '0' COMMENT '动账积分',
  `score_after` int(11) NOT NULL COMMENT '动账后积分',
  `socre_before` int(11) NOT NULL COMMENT '动账前积分',
  `frozen_score_change` int(11) NOT NULL DEFAULT '0' COMMENT '动账冻结积分',
  `frozen_score_after` int(11) NOT NULL COMMENT '动账后冻结积分',
  `frozen_score_before` int(11) NOT NULL COMMENT '动账前冻结积分',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产-积分动账记录';

SET FOREIGN_KEY_CHECKS = 1;

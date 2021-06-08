/*
 Navicat Premium Data Transfer

 Source Server         : 49.232.166.94_3306
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 49.232.166.94:3306
 Source Schema         : member-merchant

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 08/06/2021 21:12:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mcht_address
-- ----------------------------
DROP TABLE IF EXISTS `mcht_address`;
CREATE TABLE `mcht_address` (
  `id` bigint(20) NOT NULL COMMENT '商户地址id',
  `mcht_id` bigint(20) NOT NULL COMMENT '商户id',
  `province_code` varchar(64) DEFAULT NULL COMMENT '省编号',
  `province_name` varchar(64) DEFAULT NULL COMMENT '省名称',
  `city_code` varchar(64) DEFAULT NULL COMMENT '市编号',
  `city_name` varchar(64) DEFAULT NULL COMMENT '市名称',
  `area_code` varchar(64) DEFAULT NULL COMMENT '区编号',
  `area_name` varchar(64) DEFAULT NULL COMMENT '区名称',
  `address` varchar(128) DEFAULT NULL COMMENT '详细地址',
  `longitude` decimal(11,8) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(11,8) DEFAULT NULL COMMENT '纬度',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户中心-商户地址信息';

-- ----------------------------
-- Table structure for mcht_gift_settings
-- ----------------------------
DROP TABLE IF EXISTS `mcht_gift_settings`;
CREATE TABLE `mcht_gift_settings` (
  `id` bigint(20) NOT NULL COMMENT '商户礼物配置注解id',
  `mcht_id` bigint(20) NOT NULL COMMENT '商户基本信息id',
  `consume_score_gift` varchar(64) DEFAULT '1-1' COMMENT '消费积分奖励 (消费金额-积分)',
  `birthday_gift_flag` tinyint(1) DEFAULT '0' COMMENT '是否有生日福利',
  `birthday_gift_type` int(2) DEFAULT NULL COMMENT '生日：1-积分 2-优惠券',
  `birthday_gift_score` int(11) DEFAULT NULL COMMENT '生日：奖励积分数',
  `birthday_gift_coupons_template_id` bigint(20) DEFAULT NULL COMMENT '生日：奖励优惠券模板',
  `month_gift_flag` tinyint(1) DEFAULT '0' COMMENT '是否有月度福利',
  `month_gift_type` int(2) DEFAULT NULL COMMENT '月度：1-积分 2-优惠券',
  `month_gift_score` int(11) DEFAULT NULL COMMENT '月度：奖励积分数',
  `month_gift_coupons_template_id` bigint(20) DEFAULT NULL COMMENT '月度：奖励优惠券模板',
  `month_gift_day` int(4) DEFAULT '1' COMMENT '月度：奖励时间(日 例如 1 表示每月一号)',
  `year_gift_flag` tinyint(1) DEFAULT '0' COMMENT '是否有年度福利',
  `year_gift_type` int(2) DEFAULT NULL COMMENT '年度：1-积分 2-优惠券',
  `year_gift_score` int(11) DEFAULT NULL COMMENT '年度：奖励积分数',
  `year_gift_coupons_template_id` bigint(20) DEFAULT NULL COMMENT '年度：奖励优惠券模板',
  `year_gift_month_day` varchar(32) DEFAULT '01-01' COMMENT '年度：奖励发放日期 （MM-dd）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户中心-商户福利信息设置';

-- ----------------------------
-- Table structure for mcht_info
-- ----------------------------
DROP TABLE IF EXISTS `mcht_info`;
CREATE TABLE `mcht_info` (
  `id` bigint(20) NOT NULL COMMENT '商户信息主键',
  `mcht_user_id` bigint(20) NOT NULL COMMENT '商户用户id',
  `mcht_name` varchar(64) NOT NULL COMMENT '商户名称',
  `mcht_desc` varchar(255) NOT NULL COMMENT '商户描述',
  `enable_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是可用',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户中心-商户信息表';

SET FOREIGN_KEY_CHECKS = 1;

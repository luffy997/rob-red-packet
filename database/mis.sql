/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1-mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:3306
 Source Schema         : mis

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 15/08/2021 09:22:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for red_detail
-- ----------------------------
DROP TABLE IF EXISTS `red_detail`;
CREATE TABLE `red_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `record_id` bigint(11) NOT NULL COMMENT '红包记录id',
  `amount` decimal(8, 2) DEFAULT NULL COMMENT '金额（单位为分）',
  `is_active` tinyint(4) DEFAULT 1,
  `create_time` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '红包明细金额' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of red_detail
-- ----------------------------
INSERT INTO `red_detail` VALUES (41, 624020375935647744, 4.00, 1, '2021-08-14 20:35:17');
INSERT INTO `red_detail` VALUES (42, 624020375935647744, 4.00, 1, '2021-08-14 20:35:17');
INSERT INTO `red_detail` VALUES (43, 624020375935647744, 7.00, 1, '2021-08-14 20:35:17');
INSERT INTO `red_detail` VALUES (44, 624020375935647744, 12.00, 1, '2021-08-14 20:35:17');
INSERT INTO `red_detail` VALUES (45, 624020375935647744, 16.00, 1, '2021-08-14 20:35:17');
INSERT INTO `red_detail` VALUES (46, 624020375935647744, 18.00, 1, '2021-08-14 20:35:17');
INSERT INTO `red_detail` VALUES (47, 624020375935647744, 8.00, 1, '2021-08-14 20:35:17');
INSERT INTO `red_detail` VALUES (48, 624020375935647744, 17.00, 1, '2021-08-14 20:35:17');
INSERT INTO `red_detail` VALUES (49, 624020375935647744, 1.00, 1, '2021-08-14 20:35:17');
INSERT INTO `red_detail` VALUES (50, 624020375935647744, 13.00, 1, '2021-08-14 20:35:17');

-- ----------------------------
-- Table structure for red_record
-- ----------------------------
DROP TABLE IF EXISTS `red_record`;
CREATE TABLE `red_record`  (
  `id` bigint(11) NOT NULL,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `red_packet` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '红包全局唯一标识串',
  `total` int(11) NOT NULL COMMENT '人数',
  `amount` decimal(10, 2) DEFAULT NULL COMMENT '总金额（单位为分）',
  `is_active` tinyint(4) DEFAULT 1,
  `create_time` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '发红包记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of red_record
-- ----------------------------
INSERT INTO `red_record` VALUES (624020375935647744, 767, 'REDIS:RED:PACKET:767:624020375935647744', 10, 100.00, 1, '2021-08-14 20:35:17');

-- ----------------------------
-- Table structure for red_rob_record
-- ----------------------------
DROP TABLE IF EXISTS `red_rob_record`;
CREATE TABLE `red_rob_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户账号',
  `red_packet` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '红包标识串',
  `amount` decimal(8, 2) DEFAULT NULL COMMENT '红包金额（单位为分）',
  `rob_time` datetime(0) DEFAULT NULL COMMENT '时间',
  `is_active` tinyint(4) DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 128 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '抢红包记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of red_rob_record
-- ----------------------------
INSERT INTO `red_rob_record` VALUES (118, 1010, 'REDIS:RED:PACKET:767:624020375935647744', 7.00, '2021-08-14 20:35:41', 1);
INSERT INTO `red_rob_record` VALUES (119, 1006, 'REDIS:RED:PACKET:767:624020375935647744', 4.00, '2021-08-14 20:35:41', 1);
INSERT INTO `red_rob_record` VALUES (120, 1007, 'REDIS:RED:PACKET:767:624020375935647744', 12.00, '2021-08-14 20:35:41', 1);
INSERT INTO `red_rob_record` VALUES (121, 1002, 'REDIS:RED:PACKET:767:624020375935647744', 16.00, '2021-08-14 20:35:41', 1);
INSERT INTO `red_rob_record` VALUES (122, 1001, 'REDIS:RED:PACKET:767:624020375935647744', 18.00, '2021-08-14 20:35:41', 1);
INSERT INTO `red_rob_record` VALUES (123, 1004, 'REDIS:RED:PACKET:767:624020375935647744', 4.00, '2021-08-14 20:35:41', 1);
INSERT INTO `red_rob_record` VALUES (124, 1003, 'REDIS:RED:PACKET:767:624020375935647744', 8.00, '2021-08-14 20:35:41', 1);
INSERT INTO `red_rob_record` VALUES (125, 1005, 'REDIS:RED:PACKET:767:624020375935647744', 13.00, '2021-08-14 20:35:41', 1);
INSERT INTO `red_rob_record` VALUES (126, 1008, 'REDIS:RED:PACKET:767:624020375935647744', 1.00, '2021-08-14 20:35:41', 1);
INSERT INTO `red_rob_record` VALUES (127, 1009, 'REDIS:RED:PACKET:767:624020375935647744', 17.00, '2021-08-14 20:35:41', 1);

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : help_other

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 30/03/2023 23:03:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for help_user
-- ----------------------------
DROP TABLE IF EXISTS `help_user`;
CREATE TABLE `help_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'uuid',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户的权限',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户的昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户的头像',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户的邮箱号码',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户的手机号码',
  `age` int NULL DEFAULT NULL COMMENT '用户的年龄',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间于',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间于',
  `deleted_at` datetime NULL DEFAULT NULL COMMENT '删除时间于',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of help_user
-- ----------------------------
INSERT INTO `help_user` VALUES (7, '45df5a38-43fc-4da5-8ed7-2aa521614baf', 'yuanan', '$2a$10$hBGZMeDSeuYYAN0aWrxIMO8yUuF13vedvaUvewLhsJ1x2vhBF24Qy', 'admin', '袁安', NULL, NULL, '18224687099', 18, '2023-03-26 16:03:56', NULL, NULL);
INSERT INTO `help_user` VALUES (8, '8a0aa8b7-9c39-479a-83ff-f018a4a5e7d6', 'admin', '$2a$10$hNVCDt4iMhaFbXC7RgrY1.vZnAoWeEzXCSBPAUR4vsCDFxQZy.Mn.', 'admin', 'admin', NULL, NULL, '18224687099', 18, '2023-03-26 16:18:01', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Dump SQL

 Source Server         : mysql-8.0.31
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3306
 Source Schema         : sdms

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 04/07/2025 15:43:05
*/
-- 自动创建数据库并切换
CREATE DATABASE IF NOT EXISTS sdms
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_zh_0900_as_cs;
USE sdms;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for college
-- ----------------------------
DROP TABLE IF EXISTS `college`;
CREATE TABLE `college`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '学院名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of college
-- ----------------------------
INSERT INTO `college` VALUES (1, '人工智能学院');
INSERT INTO `college` VALUES (2, '计算机学院');
INSERT INTO `college` VALUES (3, '数学科学学院');

-- ----------------------------
-- Table structure for discipline_record
-- ----------------------------
DROP TABLE IF EXISTS `discipline_record`;
CREATE TABLE `discipline_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `student_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '学生姓名',
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '学号',
  `college` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '所属学院',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '违纪类型',
  `date` date NOT NULL COMMENT '违纪日期',
  `punishment` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '处分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of discipline_record
-- ----------------------------
INSERT INTO `discipline_record` VALUES (1, '张三', '0001', '计算机学院', '警告', '2025-07-03', '夜不归寝');
INSERT INTO `discipline_record` VALUES (2, '李四', '0002', '人工智能学院', '警告', '2025-07-03', '无视校规');
INSERT INTO `discipline_record` VALUES (3, '王五', '0003', '数学科学学院', '记过', '2025-07-04', '骚扰同学');
INSERT INTO `discipline_record` VALUES (4, '刘六', '0004', '数学科学学院', '记过', '2025-07-04', '考试作弊');

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '操作用户ID',
  `action` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '操作内容',
  `time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_operation_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_operation_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of operation_log
-- ----------------------------
INSERT INTO `operation_log` VALUES (7, 1, '保存违纪记录', '2025-07-04 15:34:57');
INSERT INTO `operation_log` VALUES (8, 1, '保存违纪记录', '2025-07-04 15:36:20');
INSERT INTO `operation_log` VALUES (9, 1, '保存违纪记录', '2025-07-04 15:36:51');
INSERT INTO `operation_log` VALUES (10, 1, '保存违纪记录', '2025-07-04 15:37:11');

-- ----------------------------
-- Table structure for punishment_type
-- ----------------------------
DROP TABLE IF EXISTS `punishment_type`;
CREATE TABLE `punishment_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '处分名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of punishment_type
-- ----------------------------
INSERT INTO `punishment_type` VALUES (1, '警告', '轻微违纪');
INSERT INTO `punishment_type` VALUES (2, '严重警告', '较重违纪');
INSERT INTO `punishment_type` VALUES (3, '记过', '严重违纪');
INSERT INTO `punishment_type` VALUES (4, '留校察看', '特别违纪');
INSERT INTO `punishment_type` VALUES (5, '开除学籍', '无法容忍');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '系统管理员', '全局管理');
INSERT INTO `role` VALUES (2, '学院角色', '本院管理');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '姓名',
  `job_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '工号/学号',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '密码',
  `roles` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '多角色，逗号分隔',
  `colleges` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '多学院，逗号分隔',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT 'active' COMMENT '状态(active/frozen)',
  `add_time` datetime NULL DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '管理员', 'A001', 'admin', 'admin123', 'admin', '', 'active', '2025-07-03 15:25:58');
INSERT INTO `user` VALUES (2, '张老师', 'T001', 'college1', 'college123', 'college', '人工智能学院', 'active', '2025-07-03 15:25:58');
INSERT INTO `user` VALUES (6, '李老师', 'T002', 'college2', 'college123', 'college', '计算机学院', 'active', '2025-07-04 15:18:08');

SET FOREIGN_KEY_CHECKS = 1;

alter table u_user
    add column source_user_id varchar(30)
    comment '数字化平台userid';
alter table u_user
    add column sync_date timestamp
    comment '同步时间';
alter table u_user
    MODIFY column flag tinyint(4)
    comment '标记。1：数据来自政务微信；null则是系统自添加；2数字化平台';

alter table u_org
    add column source_org_id varchar(30)
    comment '数字化平台userid';
alter table u_org
    add column sync_time timestamp
    comment '同步时间';
alter table u_org
    MODIFY column flag tinyint(4)
    comment '标记。1：数据来自政务微信；null则是系统自添加；2数字化平台';


alter table u_org_staff_rel
    add column sync_time timestamp
    comment '同步时间';
alter table u_org_staff_rel
    MODIFY column flag tinyint(4)
    comment '标记。1：数据来自政务微信；null则是系统自添加；2数字化平台';


alter table attendance_record
    add column repair_type tinyint(4)
comment '见attendance_type';
alter table attendance_record
    add column remark varchar(255)
    comment '说明，如果有假期调整';

/*
 Navicat Premium Data Transfer

 Source Server         : 150.100.13.173
 Source Server Type    : MySQL
 Source Server Version : 50743
 Source Host           : 150.100.13.173:3306
 Source Schema         : tt

 Target Server Type    : MySQL
 Target Server Version : 50743
 File Encoding         : 65001

 Date: 28/04/2026 22:40:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attendance_non_work_day
-- ----------------------------
DROP TABLE IF EXISTS `attendance_non_work_day`;
CREATE TABLE `attendance_non_work_day`  (
                                            `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                            `year_month` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年月',
                                            `start_date` date NULL DEFAULT NULL COMMENT '开始日期',
                                            `end_date` date NULL DEFAULT NULL COMMENT '结束日期',
                                            `create_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '录入日期',
                                            `holiday_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'N普通休息日，Y是法定节假日',
                                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 860 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attendance_non_work_day
-- ----------------------------
INSERT INTO `attendance_non_work_day` VALUES (538, '202101', '2021-01-01', '2021-01-03', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (539, '202101', '2021-01-09', '2021-01-10', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (540, '202101', '2021-01-16', '2021-01-17', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (541, '202101', '2021-01-23', '2021-01-24', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (542, '202101', '2021-01-30', '2021-01-31', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (543, '202102', '2021-02-06', '2021-02-06', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (544, '202102', '2021-02-11', '2021-02-15', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (545, '202102', '2021-02-16', '2021-02-17', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (546, '202102', '2021-02-21', '2021-02-21', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (547, '202102', '2021-02-27', '2021-02-28', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (548, '202103', '2021-03-06', '2021-03-07', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (549, '202103', '2021-03-13', '2021-03-14', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (550, '202103', '2021-03-20', '2021-03-21', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (551, '202103', '2021-03-27', '2021-03-28', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (552, '202104', '2021-04-03', '2021-04-05', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (553, '202104', '2021-04-10', '2021-04-11', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (554, '202104', '2021-04-17', '2021-04-18', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (555, '202104', '2021-04-24', '2021-04-24', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (556, '202105', '2021-05-01', '2021-05-05', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (557, '202105', '2021-05-09', '2021-05-09', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (558, '202105', '2021-05-15', '2021-05-16', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (559, '202105', '2021-05-22', '2021-05-23', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (560, '202105', '2021-05-29', '2021-05-30', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (561, '202106', '2021-06-05', '2021-06-06', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (562, '202106', '2021-06-12', '2021-06-14', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (563, '202106', '2021-06-19', '2021-06-20', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (564, '202106', '2021-06-26', '2021-06-27', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (565, '202107', '2021-07-03', '2021-07-04', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (566, '202107', '2021-07-10', '2021-07-11', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (567, '202107', '2021-07-17', '2021-07-18', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (568, '202107', '2021-07-24', '2021-07-25', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (569, '202107', '2021-07-31', '2021-08-01', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (570, '202108', '2021-08-07', '2021-08-08', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (571, '202108', '2021-08-14', '2021-08-15', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (572, '202108', '2021-08-21', '2021-08-22', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (573, '202108', '2021-08-28', '2021-08-29', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (574, '202109', '2021-09-04', '2021-09-05', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (575, '202109', '2021-09-11', '2021-09-12', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (576, '202109', '2021-09-19', '2021-09-21', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (577, '202109', '2021-09-25', '2021-09-25', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (578, '202110', '2021-10-01', '2021-10-07', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (579, '202110', '2021-10-10', '2021-10-10', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (580, '202110', '2021-10-16', '2021-10-17', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (581, '202110', '2021-10-23', '2021-10-24', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (582, '202110', '2021-10-30', '2021-10-31', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (583, '202111', '2021-11-06', '2021-11-07', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (584, '202111', '2021-11-13', '2021-11-14', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (585, '202111', '2021-11-20', '2021-11-21', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (586, '202111', '2021-11-27', '2021-11-28', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (587, '202112', '2021-12-04', '2021-12-05', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (588, '202112', '2021-12-11', '2021-12-12', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (589, '202112', '2021-12-18', '2021-12-19', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (590, '202112', '2021-12-25', '2021-12-26', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (591, '202201', '2022-01-01', '2022-01-03', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (592, '202201', '2022-01-08', '2022-01-09', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (593, '202201', '2022-01-15', '2022-01-16', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (594, '202201', '2022-01-22', '2022-01-23', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (595, '202201', '2022-01-31', '2022-01-31', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (596, '202202', '2022-02-01', '2022-02-06', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (597, '202202', '2022-02-12', '2022-02-13', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (598, '202202', '2022-02-19', '2022-02-20', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (599, '202202', '2022-02-26', '2022-02-27', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (600, '202203', '2022-03-05', '2022-03-06', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (601, '202203', '2022-03-12', '2022-03-13', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (602, '202203', '2022-03-19', '2022-03-20', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (603, '202203', '2022-03-26', '2022-03-27', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (604, '202204', '2022-04-03', '2022-04-05', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (605, '202204', '2022-04-09', '2022-04-10', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (606, '202204', '2022-04-16', '2022-04-17', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (607, '202204', '2022-04-23', '2022-04-23', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (608, '202204', '2022-04-30', '2022-05-01', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (609, '202205', '2022-05-01', '2022-05-04', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (610, '202205', '2022-05-08', '2022-05-08', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (611, '202205', '2022-05-14', '2022-05-15', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (612, '202205', '2022-05-21', '2022-05-22', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (613, '202205', '2022-05-28', '2022-05-29', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (614, '202206', '2022-06-03', '2022-06-05', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (615, '202206', '2022-06-11', '2022-06-12', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (616, '202206', '2022-06-18', '2022-06-19', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (617, '202206', '2022-06-25', '2022-06-26', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (618, '202207', '2022-07-02', '2022-07-03', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (619, '202207', '2022-07-09', '2022-07-10', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (620, '202207', '2022-07-16', '2022-07-17', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (621, '202207', '2022-07-23', '2022-07-24', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (622, '202207', '2022-07-30', '2022-07-31', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (623, '202208', '2022-08-06', '2022-08-07', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (624, '202208', '2022-08-13', '2022-08-14', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (625, '202208', '2022-08-20', '2022-08-21', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (626, '202208', '2022-08-27', '2022-08-28', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (627, '202209', '2022-09-03', '2022-09-04', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (628, '202209', '2022-09-10', '2022-09-12', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (629, '202209', '2022-09-17', '2022-09-18', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (630, '202209', '2022-09-24', '2022-09-25', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (631, '202210', '2022-10-01', '2022-10-07', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (632, '202210', '2022-10-15', '2022-10-16', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (633, '202210', '2022-10-22', '2022-10-23', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (634, '202210', '2022-10-29', '2022-10-30', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (635, '202211', '2022-11-05', '2022-11-06', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (636, '202211', '2022-11-12', '2022-11-13', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (637, '202211', '2022-11-19', '2022-11-20', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (638, '202211', '2022-11-26', '2022-11-27', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (639, '202212', '2022-12-03', '2022-12-04', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (640, '202212', '2022-12-10', '2022-12-11', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (641, '202212', '2022-12-17', '2022-12-18', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (642, '202212', '2022-12-24', '2022-12-25', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (643, '202212', '2022-12-31', '2023-01-01', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (644, '202301', '2023-01-01', '2023-01-02', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (645, '202301', '2023-01-07', '2023-01-08', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (646, '202301', '2023-01-14', '2023-01-15', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (647, '202301', '2023-01-21', '2023-01-27', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (648, '202302', '2023-02-04', '2023-02-05', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (649, '202302', '2023-02-11', '2023-02-12', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (650, '202302', '2023-02-18', '2023-02-19', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (651, '202302', '2023-02-25', '2023-02-26', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (652, '202303', '2023-03-04', '2023-03-05', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (653, '202303', '2023-03-11', '2023-03-12', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (654, '202303', '2023-03-18', '2023-03-19', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (655, '202303', '2023-03-25', '2023-03-26', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (656, '202304', '2023-04-01', '2023-04-02', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (657, '202304', '2023-04-05', '2023-04-05', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (658, '202304', '2023-04-08', '2023-04-09', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (659, '202304', '2023-04-15', '2023-04-16', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (660, '202304', '2023-04-22', '2023-04-23', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (661, '202304', '2023-04-29', '2023-04-30', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (662, '202305', '2023-05-01', '2023-05-03', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (663, '202305', '2023-05-07', '2023-05-07', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (664, '202305', '2023-05-13', '2023-05-14', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (665, '202305', '2023-05-20', '2023-05-21', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (666, '202305', '2023-05-27', '2023-05-28', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (667, '202306', '2023-06-03', '2023-06-04', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (668, '202306', '2023-06-10', '2023-06-11', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (669, '202306', '2023-06-17', '2023-06-18', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (670, '202306', '2023-06-22', '2023-06-24', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (671, '202307', '2023-07-01', '2023-07-02', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (672, '202307', '2023-07-08', '2023-07-09', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (673, '202307', '2023-07-15', '2023-07-16', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (674, '202307', '2023-07-22', '2023-07-23', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (675, '202307', '2023-07-29', '2023-07-30', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (676, '202308', '2023-08-05', '2023-08-06', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (677, '202308', '2023-08-12', '2023-08-13', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (678, '202308', '2023-08-19', '2023-08-20', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (679, '202308', '2023-08-26', '2023-08-27', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (680, '202309', '2023-09-02', '2023-09-03', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (681, '202309', '2023-09-09', '2023-09-10', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (682, '202309', '2023-09-16', '2023-09-17', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (683, '202309', '2023-09-23', '2023-09-24', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (684, '202309', '2023-09-29', '2023-09-30', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (685, '202310', '2023-10-01', '2023-10-06', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (686, '202310', '2023-10-14', '2023-10-15', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (687, '202310', '2023-10-21', '2023-10-22', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (688, '202310', '2023-10-28', '2023-10-29', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (689, '202311', '2023-11-04', '2023-11-05', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (690, '202311', '2023-11-11', '2023-11-12', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (691, '202311', '2023-11-18', '2023-11-19', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (692, '202311', '2023-11-25', '2023-11-26', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (693, '202312', '2023-12-02', '2023-12-03', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (694, '202312', '2023-12-09', '2023-12-10', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (695, '202312', '2023-12-16', '2023-12-17', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (696, '202312', '2023-12-23', '2023-12-24', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (697, '202312', '2023-12-30', '2023-12-31', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (698, '202401', '2024-01-01', '2024-01-01', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (699, '202401', '2024-01-06', '2024-01-07', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (700, '202401', '2024-01-13', '2024-01-14', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (701, '202401', '2024-01-20', '2024-01-21', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (702, '202401', '2024-01-27', '2024-01-28', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (703, '202402', '2024-02-03', '2024-02-03', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (704, '202402', '2024-02-10', '2024-02-15', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (705, '202402', '2024-02-16', '2024-02-17', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (706, '202402', '2024-02-24', '2024-02-25', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (707, '202403', '2024-03-02', '2024-03-03', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (708, '202403', '2024-03-09', '2024-03-10', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (709, '202403', '2024-03-16', '2024-03-17', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (710, '202403', '2024-03-23', '2024-03-24', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (711, '202403', '2024-03-30', '2024-03-31', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (712, '202404', '2024-04-04', '2024-04-06', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (713, '202404', '2024-04-13', '2024-04-14', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (714, '202404', '2024-04-20', '2024-04-21', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (715, '202404', '2024-04-27', '2024-04-27', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (716, '202405', '2024-05-01', '2024-05-05', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (717, '202405', '2024-05-12', '2024-05-12', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (718, '202405', '2024-05-18', '2024-05-19', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (719, '202405', '2024-05-25', '2024-05-26', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (720, '202406', '2024-06-01', '2024-06-02', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (721, '202406', '2024-06-08', '2024-06-10', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (722, '202406', '2024-06-15', '2024-06-16', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (723, '202406', '2024-06-22', '2024-06-23', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (724, '202406', '2024-06-29', '2024-06-30', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (725, '202407', '2024-07-06', '2024-07-07', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (726, '202407', '2024-07-13', '2024-07-14', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (727, '202407', '2024-07-20', '2024-07-21', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (728, '202407', '2024-07-27', '2024-07-28', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (729, '202408', '2024-08-03', '2024-08-04', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (730, '202408', '2024-08-10', '2024-08-11', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (731, '202408', '2024-08-17', '2024-08-18', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (732, '202408', '2024-08-24', '2024-08-25', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (733, '202408', '2024-08-31', '2024-09-01', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (734, '202409', '2024-09-07', '2024-09-08', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (735, '202409', '2024-09-15', '2024-09-17', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (736, '202409', '2024-09-21', '2024-09-22', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (737, '202409', '2024-09-28', '2024-09-28', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (738, '202410', '2024-10-01', '2024-10-07', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (739, '202410', '2024-10-13', '2024-10-13', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (740, '202410', '2024-10-19', '2024-10-20', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (741, '202410', '2024-10-26', '2024-10-27', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (742, '202411', '2024-11-02', '2024-11-03', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (743, '202411', '2024-11-09', '2024-11-10', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (744, '202411', '2024-11-16', '2024-11-17', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (745, '202411', '2024-11-23', '2024-11-24', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (746, '202411', '2024-11-30', '2024-11-30', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (747, '202412', '2024-12-01', '2024-12-01', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (748, '202412', '2024-12-07', '2024-12-08', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (749, '202412', '2024-12-14', '2024-12-15', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (750, '202412', '2024-12-21', '2024-12-22', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (751, '202412', '2024-12-28', '2024-12-29', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (752, '202501', '2025-01-01', '2025-01-01', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (753, '202501', '2025-01-04', '2025-01-05', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (754, '202501', '2025-01-11', '2025-01-12', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (755, '202501', '2025-01-18', '2025-01-19', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (756, '202501', '2025-01-25', '2025-01-25', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (757, '202501', '2025-01-28', '2025-01-31', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (758, '202502', '2025-02-01', '2025-02-04', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (759, '202502', '2025-02-09', '2025-02-09', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (760, '202502', '2025-02-15', '2025-02-16', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (761, '202502', '2025-02-22', '2025-02-23', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (762, '202503', '2025-03-01', '2025-03-02', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (763, '202503', '2025-03-08', '2025-03-09', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (764, '202503', '2025-03-15', '2025-03-16', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (765, '202503', '2025-03-22', '2025-03-23', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (766, '202503', '2025-03-29', '2025-03-30', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (767, '202504', '2025-04-04', '2025-04-06', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (768, '202504', '2025-04-12', '2025-04-13', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (769, '202504', '2025-04-19', '2025-04-20', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (770, '202504', '2025-04-26', '2025-04-26', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (771, '202505', '2025-05-01', '2025-05-05', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (772, '202505', '2025-05-10', '2025-05-11', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (773, '202505', '2025-05-17', '2025-05-18', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (774, '202505', '2025-05-24', '2025-05-25', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (775, '202505', '2025-05-31', '2025-05-31', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (776, '202506', '2025-06-01', '2025-06-02', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (777, '202506', '2025-06-07', '2025-06-08', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (778, '202506', '2025-06-14', '2025-06-15', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (779, '202506', '2025-06-21', '2025-06-22', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (780, '202506', '2025-06-28', '2025-06-29', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (781, '202507', '2025-07-05', '2025-07-06', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (782, '202507', '2025-07-12', '2025-07-13', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (783, '202507', '2025-07-19', '2025-07-20', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (784, '202507', '2025-07-26', '2025-07-27', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (785, '202508', '2025-08-02', '2025-08-03', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (786, '202508', '2025-08-09', '2025-08-10', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (787, '202508', '2025-08-16', '2025-08-17', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (788, '202508', '2025-08-23', '2025-08-24', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (789, '202508', '2025-08-30', '2025-08-31', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (790, '202509', '2025-09-06', '2025-09-07', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (791, '202509', '2025-09-13', '2025-09-14', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (792, '202509', '2025-09-20', '2025-09-21', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (793, '202509', '2025-09-27', '2025-09-27', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (794, '202510', '2025-10-01', '2025-10-08', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (795, '202510', '2025-10-12', '2025-10-12', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (796, '202510', '2025-10-18', '2025-10-19', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (797, '202510', '2025-10-25', '2025-10-26', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (798, '202511', '2025-11-01', '2025-11-02', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (799, '202511', '2025-11-08', '2025-11-09', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (800, '202511', '2025-11-15', '2025-11-16', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (801, '202511', '2025-11-22', '2025-11-23', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (802, '202511', '2025-11-29', '2025-11-30', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (803, '202512', '2025-12-06', '2025-12-07', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (804, '202512', '2025-12-13', '2025-12-14', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (805, '202512', '2025-12-20', '2025-12-21', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (806, '202512', '2025-12-27', '2025-12-28', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (807, '202601', '2026-01-01', '2026-01-03', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (808, '202601', '2026-01-10', '2026-01-11', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (809, '202601', '2026-01-17', '2026-01-18', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (810, '202601', '2026-01-24', '2026-01-25', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (811, '202601', '2026-01-31', '2026-01-31', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (812, '202602', '2026-02-01', '2026-02-01', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (813, '202602', '2026-02-07', '2026-02-08', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (814, '202602', '2026-02-15', '2026-02-23', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (815, '202603', '2026-03-01', '2026-03-01', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (816, '202603', '2026-03-07', '2026-03-08', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (817, '202603', '2026-03-14', '2026-03-15', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (818, '202603', '2026-03-21', '2026-03-22', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (819, '202603', '2026-03-28', '2026-03-29', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (820, '202604', '2026-04-04', '2026-04-06', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (821, '202604', '2026-04-11', '2026-04-12', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (822, '202604', '2026-04-18', '2026-04-19', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (823, '202604', '2026-04-25', '2026-04-26', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (824, '202605', '2026-05-01', '2026-05-05', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (825, '202605', '2026-05-10', '2026-05-10', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (826, '202605', '2026-05-16', '2026-05-17', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (827, '202605', '2026-05-23', '2026-05-24', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (828, '202605', '2026-05-30', '2026-05-31', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (829, '202606', '2026-06-06', '2026-06-07', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (830, '202606', '2026-06-13', '2026-06-14', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (831, '202606', '2026-06-19', '2026-06-21', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (832, '202606', '2026-06-27', '2026-06-28', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (833, '202607', '2026-07-04', '2026-07-05', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (834, '202607', '2026-07-11', '2026-07-12', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (835, '202607', '2026-07-18', '2026-07-19', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (836, '202607', '2026-07-25', '2026-07-26', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (837, '202608', '2026-08-01', '2026-08-02', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (838, '202608', '2026-08-08', '2026-08-09', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (839, '202608', '2026-08-15', '2026-08-16', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (840, '202608', '2026-08-22', '2026-08-23', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (841, '202608', '2026-08-29', '2026-08-30', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (842, '202609', '2026-09-05', '2026-09-06', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (843, '202609', '2026-09-12', '2026-09-13', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (844, '202609', '2026-09-19', '2026-09-19', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (845, '202609', '2026-09-25', '2026-09-27', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (846, '202610', '2026-10-01', '2026-10-07', NULL, 'Y');
INSERT INTO `attendance_non_work_day` VALUES (847, '202610', '2026-10-11', '2026-10-11', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (848, '202610', '2026-10-17', '2026-10-18', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (849, '202610', '2026-10-24', '2026-10-25', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (850, '202610', '2026-10-31', '2026-10-31', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (851, '202611', '2026-11-01', '2026-11-01', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (852, '202611', '2026-11-07', '2026-11-08', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (853, '202611', '2026-11-14', '2026-11-15', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (854, '202611', '2026-11-21', '2026-11-22', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (855, '202611', '2026-11-28', '2026-11-29', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (856, '202612', '2026-12-05', '2026-12-06', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (857, '202612', '2026-12-12', '2026-12-13', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (858, '202612', '2026-12-19', '2026-12-20', NULL, 'N');
INSERT INTO `attendance_non_work_day` VALUES (859, '202612', '2026-12-26', '2026-12-27', NULL, 'N');

SET FOREIGN_KEY_CHECKS = 1;



/*
 Navicat Premium Data Transfer

 Source Server         : 150.100.13.173
 Source Server Type    : MySQL
 Source Server Version : 50743
 Source Host           : 150.100.13.173:3306
 Source Schema         : tt

 Target Server Type    : MySQL
 Target Server Version : 50743
 File Encoding         : 65001

 Date: 28/04/2026 22:54:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attendance_exception_data_back
-- ----------------------------
DROP TABLE IF EXISTS `attendance_exception_data_back`;
CREATE TABLE `attendance_exception_data_back`  (
                                                   `id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                                   `attendance_date` datetime(0) NULL DEFAULT NULL COMMENT '考勤日期',
                                                   `actual_start_work_time` time(0) NULL DEFAULT NULL COMMENT '实际上班时间',
                                                   `actual_end_work_time` time(0) NULL DEFAULT NULL COMMENT '实际下班时间',
                                                   `type` tinyint(4) NULL DEFAULT NULL COMMENT '类型。1：正常，2：迟到，3：早退，4：旷工',
                                                   `exception_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'attendance_exception表对应的id',
                                                   `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;


alter table sys_user
    add column source_user_id varchar(50)
    comment '数字化平台userid';
alter table sys_user
    add column sync_time datetime
    comment '同步时间';

SELECT gen_date
FROM (
         SELECT
             DATE_ADD(@start_date, INTERVAL (a.n + b.n*10 + c.n*100 + d.n*1000) DAY) AS gen_date,
             @start_date,
             @end_date
         FROM
             (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) a,
             (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) b,
             (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) c,
             (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) d,
             (SELECT @start_date := '2020-01-01', @end_date := '2028-12-31') e
     ) t
WHERE gen_date <= @end_date
ORDER BY gen_date;
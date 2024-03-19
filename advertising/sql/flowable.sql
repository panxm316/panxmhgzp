

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
                            `id` bigint NOT NULL COMMENT 'id',
                            `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                            `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                            `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
                            `readed` tinyint(1) NOT NULL COMMENT '是否已读',
                            `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                            `unique_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '唯一id',
                            `param` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息参数',
                            `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息内容',
                            `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息头',
                            `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                            `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 238 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知消息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process
-- ----------------------------
DROP TABLE IF EXISTS `process`;
CREATE TABLE `process`  (
                            `id` bigint NOT NULL COMMENT 'id',
                            `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                            `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                            `flow_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '表单ID',
                            `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单名称',
                            `logo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图标配置',
                            `settings` json NULL COMMENT '设置项',
                            `group_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组ID',
                            `form_items` json NOT NULL COMMENT '表单设置内容',
                            `process` json NOT NULL COMMENT '流程设置内容',
                            `remark` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                            `sort` int NOT NULL,
                            `is_hidden` tinyint(1) NOT NULL COMMENT '0 正常 1=隐藏',
                            `is_stop` tinyint(1) NOT NULL COMMENT '0 正常 1=停用 ',
                            `admin_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程管理员',
                            `unique_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '唯一性id',
                            `admin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员',
                            `range_show` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE INDEX `idx_form_id`(`flow_id` ASC) USING BTREE,
                            INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 230 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_copy
-- ----------------------------
DROP TABLE IF EXISTS `process_instance_copy`;
CREATE TABLE `process_instance_copy`  (
                                 `id` bigint NOT NULL COMMENT 'id',
                                 `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                 `create_time` datetime NOT NULL COMMENT '创建时间',
                                 `update_time` datetime NOT NULL COMMENT '更新时间',
                                 `start_time` datetime NOT NULL COMMENT ' 流程发起时间',
                                 `node_time` datetime NOT NULL COMMENT '当前节点时间',
                                 `start_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发起人',
                                 `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                 `process_instance_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '实例id',
                                 `node_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点id',
                                 `group_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组id',
                                 `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组名称',
                                 `process_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名称',
                                 `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点 名称',
                                 `form_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单数据',
                                 `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '抄送人id',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程抄送数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_group
-- ----------------------------
DROP TABLE IF EXISTS `process_group`;
CREATE TABLE `process_group`  (
                                  `id` bigint NOT NULL COMMENT 'id',
                                  `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组名',
                                  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for process_node_data
-- ----------------------------
DROP TABLE IF EXISTS `process_node_data`;
CREATE TABLE `process_node_data`  (
                                      `id` bigint NOT NULL COMMENT 'id',
                                      `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                      `create_time` datetime NOT NULL COMMENT '创建时间',
                                      `update_time` datetime NOT NULL COMMENT '更新时间',
                                      `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                      `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单数据',
                                      `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1755 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_node_record
-- ----------------------------
DROP TABLE IF EXISTS `process_instance_node_record`;
CREATE TABLE `process_instance_node_record`  (
                                        `id` bigint NOT NULL COMMENT 'id',
                                        `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                        `create_time` datetime NOT NULL COMMENT '创建时间',
                                        `update_time` datetime NOT NULL COMMENT '更新时间',
                                        `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                        `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
                                        `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表单数据',
                                        `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                        `node_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点类型',
                                        `node_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点名字',
                                        `status` int NOT NULL COMMENT '节点状态',
                                        `start_time` datetime NOT NULL COMMENT '开始时间',
                                        `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
                                        `execution_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行id',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2018 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_node_record_assign_user
-- ----------------------------
DROP TABLE IF EXISTS `process_instance_assign_user_record`;
CREATE TABLE `process_instance_assign_user_record`  (
                                                    `id` bigint NOT NULL COMMENT 'id',
                                                    `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                                    `create_time` datetime NOT NULL COMMENT '创建时间',
                                                    `update_time` datetime NOT NULL COMMENT '更新时间',
                                                    `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                                    `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
                                                    `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表单数据',
                                                    `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                                    `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ' 用户id',
                                                    `status` int NOT NULL COMMENT '节点状态',
                                                    `start_time` datetime NOT NULL COMMENT '开始时间',
                                                    `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
                                                    `execution_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行id',
                                                    `task_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ' 任务id',
                                                    `approve_desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批意见',
                                                    `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ' 节点名称',
                                                    `task_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务类型',
                                                    `local_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表单本地数据',
                                                    PRIMARY KEY (`id`) USING BTREE,
                                                    INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 845 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点记录-执行人' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_starter
-- ----------------------------
DROP TABLE IF EXISTS `process_starter`;
CREATE TABLE `process_starter`  (
                                    `id` bigint NOT NULL COMMENT 'id',
                                    `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                    `create_time` datetime NOT NULL COMMENT '创建时间',
                                    `update_time` datetime NOT NULL COMMENT '更新时间',
                                    `type_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id或者部门id',
                                    `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ' 类型 user dept',
                                    `process_id` bigint NOT NULL COMMENT '流程id',
                                    `data` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程发起人' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
DROP TABLE IF EXISTS `process_instance_record`;


CREATE TABLE `process_instance_record` (
                                           `id` bigint NOT NULL COMMENT 'id',
                                           `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名字',
                                           `logo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像',
                                           `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                                           `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                           `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                           `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流程id',
                                           `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流程实例id',
                                           `form_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '表单数据',
                                           `group_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组id',
                                           `group_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组名称',
                                           `status` int DEFAULT '1' COMMENT '状态',
                                           `end_time` datetime DEFAULT NULL COMMENT '结束时间',
                                           `parent_process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上级流程实例id',
                                           `process` json DEFAULT NULL,
                                           `result` tinyint(1) DEFAULT NULL COMMENT '流程结果',
                                           PRIMARY KEY (`id`) USING BTREE,
                                           KEY `idx_id` (`id`) USING BTREE,
                                           KEY `idx_dep_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='流程记录';
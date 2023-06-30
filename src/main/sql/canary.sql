
-- canary database
CREATE DATABASE IF NOT EXISTS `canary` DEFAULT CHARACTER SET = `utf8mb4` DEFAULT COLLATE = `utf8mb4_0900_ai_ci`;

USE `canary`;

-- drop table t_task_base
DROP TABLE IF EXISTS `t_task_base`;
-- creae table t_task_base
CREATE TABLE `t_task_base` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
    `name_en` varchar(200) DEFAULT NULL COMMENT '名称（英文）',
    `description` varchar(200) DEFAULT NULL COMMENT '描述',
    `cron_expression` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表达式',
    `class_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类名',
    `method_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '方法名',
    `method_params` varchar(200) DEFAULT NULL COMMENT '方法参数',
    `is_disabled` bigint DEFAULT '0' COMMENT '是否禁用, 0:否 1:是',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除 0:否 非0:是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `udx_multi_1` (`name`,`is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务';

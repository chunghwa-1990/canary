
-- canary database
CREATE DATABASE IF NOT EXISTS `canary` DEFAULT CHARACTER SET = `utf8mb4` DEFAULT COLLATE = `utf8mb4_0900_ai_ci`;

USE `canary`;

-- drop table sys_user
DROP TABLE IF EXISTS `sys_user`;
-- create table sys_user
CREATE TABLE `sys_user` (
    `id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID',
    `account` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称（英文）',
    `nick_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
    `real_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
    `sex` tinyint DEFAULT '0' COMMENT '性别 0:男 1:女',
    `mobile_no` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
    `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
    `salt` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '盐值',
    `is_admin` tinyint DEFAULT '0' COMMENT '是否超级管理员 0:否 1:是',
    `is_disabled` tinyint DEFAULT '0' COMMENT '是否禁用 0:否 1:是',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '是否删除 0:否 非0:是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `udx_account_1` (`account`,`is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户';

INSERT INTO `sys_user` (`id`, `account`, `nick_name`, `real_name`, `sex`, `mobile_no`, `password`, `salt`, `is_admin`, `is_disabled`, `create_time`, `update_time`, `is_deleted`) VALUES
('28270e6bdb569cca27955bff36fcea48', 'admin', 'admin', 'admin', 0, NULL, 'aad73b27954f58c9acf3994ab1250574', 'EtbGTE', 1, 0, '2023-07-07 20:54:40', '2023-07-07 20:56:12', '0');

-- drop table sys_role
DROP TABLE IF EXISTS `sys_role`;
-- create table sys_role
CREATE TABLE `sys_role` (
    `id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID',
    `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
    `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
    `is_disabled` tinyint DEFAULT '0' COMMENT '是否禁用 0:否 1:是',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted` char(32) DEFAULT '0' COMMENT '是否删除 0:否 非0:是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `udx_name_1` (`name`,`is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色';

INSERT INTO `sys_role` (`id`, `name`, `description`, `is_disabled`, `create_time`, `update_time`, `is_deleted`) VALUES
('08fdc5462fdbe394733d3261b2c34416', '超级管理员', '超级管理员', 1, '2023-09-14 01:22:02', '2023-09-14 01:27:33', '0');

-- drop table sys_menu
DROP TABLE IF EXISTS `sys_menu`;
-- create table sys_menu
CREATE TABLE `sys_menu` (
    `id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID',
    `parent_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '父ID',
    `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
    `icon` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图标',
    `level` tinyint DEFAULT '1' COMMENT '菜单级别 [0,1]',
    `route` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '路由',
    `is_disabled` tinyint DEFAULT '0' COMMENT '是否禁用 0:否 1:是',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted` char(32) DEFAULT '0' COMMENT '是否删除 0:否 非0:是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `udx_route_1` (`route`,`is_deleted`) USING BTREE,
    UNIQUE KEY `udx_name_route_2` (`name`,`route`,`is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单';

-- drop table sys_menu
DROP TABLE IF EXISTS `sys_permission`;
-- create table sys_permission
CREATE TABLE `sys_permission` (
    `id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID',
    `menu_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
    `name` varchar(200) NOT NULL COMMENT '名称',
    `icon` varchar(20) DEFAULT NULL COMMENT '图标',
    `route` varchar(100) NOT NULL COMMENT '路由',
    `is_disabled` tinyint DEFAULT '0' COMMENT '是否禁用 0:否 1:是',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted` char(32) DEFAULT '0' COMMENT '是否删除 0:否 非0:是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `udx_route_1` (`route`,`is_deleted`) USING BTREE,
    UNIQUE KEY `udx_name_route_2` (`name`,`route`,`is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限';

-- drop table sys_menu_permission
DROP TABLE IF EXISTS `sys_menu_permission`;
-- create table sys_menu_permission
CREATE TABLE `sys_menu_permission` (
    `id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID',
    `menu_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单ID',
    `permission_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted` char(32) DEFAULT '0' COMMENT '是否删除 0:否 非0:是',
    PRIMARY KEY (`id`),
UNIQUE KEY `udx_menu_permission_1` (`menu_id`,`permission_id`,`is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限关联关系';

-- drop table sys_role_permission
DROP TABLE IF EXISTS `sys_role_permission`;
-- create table sys_role_permission
CREATE TABLE `sys_role_permission` (
    `id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID',
    `role_id` char(32) NOT NULL COMMENT '角色ID',
    `permission_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted` char(32) DEFAULT '0' COMMENT '是否删除 0:否 非0:是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `udx_role_permission_1` (`role_id`,`permission_id`,`is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色权限关联关系';

-- drop table sys_user_role
DROP TABLE IF EXISTS `sys_user_role`;
-- create table sys_user_role
CREATE TABLE `sys_user_role` (
    `id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID',
    `user_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
    `role_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted` char(32) DEFAULT '0' COMMENT '是否删除 0:否 非0:是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `udx_user_role_1` (`user_id`,`role_id`,`is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联关系';

-- drop table t_task
DROP TABLE IF EXISTS `t_task`;
-- create table t_task
CREATE TABLE `t_task` (
    `id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID',
    `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `cron_expression` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表达式',
    `class_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类名',
    `method_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '方法名',
    `method_params` varchar(200) DEFAULT NULL COMMENT '方法参数',
    `is_disabled` tinyint DEFAULT '0' COMMENT '是否禁用 0:否 1:是',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '是否删除 0:否 非0:是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `udx_name_1` (`name`,`is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务';

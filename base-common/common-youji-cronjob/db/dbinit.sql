CREATE TABLE `youji_task_exec_log` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                       `task_no` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务编号',
                                       `task_name` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务名称',
                                       `worker_id` bigint(20) DEFAULT NULL COMMENT '工作节点id',
                                       `worker_ip` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工作节点ip',
                                       `worker_port` int(11) DEFAULT NULL COMMENT '工作节点端口号',
                                       `task_param` text COLLATE utf8mb4_unicode_ci COMMENT '参数:只支持字符串类型参数',
                                       `contacts_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人',
                                       `contacts_phone` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人电话',
                                       `contacts_email` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人邮箱',
                                       `finish_flag` tinyint(1) DEFAULT NULL COMMENT '是否执行完成',
                                       `result_msg` text COLLATE utf8mb4_unicode_ci COMMENT '任务执行结果信息',
                                       `create_time` datetime NOT NULL COMMENT '创建时间',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定时任务表日志表';


CREATE TABLE `youji_task_info` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                   `bind_mgr_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '绑定的管理节点的自定义id',
                                   `task_type` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'bean' COMMENT '（url-通过URL触发 bean-遍历容器中的bean触发）',
                                   `exec_type` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'single' COMMENT '执行方式 (single-单节点执行 all-全节点执行)',
                                   `task_no` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务编号',
                                   `task_name` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务名称',
                                   `corn` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务执行表达式',
                                   `task_url` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '任务执行触发的url地址',
                                   `task_bean_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应用上下文执行中对应的全限定类名',
                                   `task_method` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应用上下文中对应的方法名，或者url的请求类型',
                                   `task_param` text COLLATE utf8mb4_unicode_ci COMMENT '参数:只支持字符串类型参数',
                                   `contacts_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人',
                                   `contacts_phone` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人电话',
                                   `contacts_email` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人邮箱',
                                   `enable_flag` tinyint(1) DEFAULT NULL COMMENT '是否可用',
                                   `exec_num` int(11) DEFAULT NULL COMMENT '执行次数统计',
                                   `create_time` datetime NOT NULL COMMENT '创建时间',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `last_exec_time` datetime DEFAULT NULL COMMENT '最后一次执行时间',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1459853225740673026 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定时任务表';


CREATE TABLE `youji_task_mgr` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '管理节点id主键',
                                  `mgr_id` varchar(32) NOT NULL COMMENT '管理节点自定义id',
                                  `mgr_server_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '管理节点应用名称，配置文件中spring.application.name的配置',
                                  `mgr_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '管理节点ip',
                                  `mgr_port` int(11) NOT NULL COMMENT '管理节点端口号',
                                  `enable_flag` tinyint(1) NOT NULL COMMENT '管理节点是否可用',
                                  `online_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '节点是否在线',
                                  `beat_fail_num` int(11) NOT NULL DEFAULT '0' COMMENT '心跳失败累计数(成功-1，失败+1 到0为止)',
                                  `last_heart_beat_time` datetime DEFAULT NULL COMMENT '最后一次心跳时间',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='酉鸡定时任务-管理节点表';


CREATE TABLE `youji_task_worker` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作节点id主键',
                                     `task_id` bigint(20) NOT NULL COMMENT '任务id',
                                     `task_no` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务编号',
                                     `worker_server_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工作节点应用名称，配置文件中spring.application.name的配置',
                                     `worker_ip` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工作节点ip',
                                     `worker_port` int(11) NOT NULL COMMENT '工作节点端口号',
                                     `enable_flag` tinyint(1) NOT NULL COMMENT '工作节点是否可用',
                                     `online_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '节点是否在线',
                                     `beat_fail_num` int(11) NOT NULL DEFAULT '0' COMMENT '心跳失败累计数(成功-1，失败+1 到0为止)',
                                     `last_heart_beat_time` datetime DEFAULT NULL COMMENT '最后一次心跳时间',
                                     `exec_task_num` int(11) NOT NULL DEFAULT '0' COMMENT '执行任务的次数',
                                     `last_exec_time` datetime DEFAULT NULL COMMENT '最后一次执行任务的时间',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1513880448520605697 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定时任务的工作节点信息';



CREATE TABLE `youji_config` (
                                `id` bigint(20) NOT NULL COMMENT '配置主键',
                                `cfg_key` varchar(64) DEFAULT NULL COMMENT '配置的key',
                                `cfg_value` varchar(512) DEFAULT NULL COMMENT '配置值',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

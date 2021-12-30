create table sys_dict
(
    id         bigint      not null comment '字典id'
        primary key,
    type       varchar(32) null comment '字典类型',
    dict_key   varchar(32) not null comment '字典键',
    dict_name  varchar(64) null comment '字典名',
    dict_value varchar(64) not null comment '字典值'
)
    comment '系统表-字典表' charset = utf8mb4;

INSERT INTO sys_dict (id, type, dict_key, dict_name, dict_value) VALUES (1, 'user_type', 'admin', '管理员用户', '1');
INSERT INTO sys_dict (id, type, dict_key, dict_name, dict_value) VALUES (2, 'user_type', 'merchant', '商户用户', '2');
INSERT INTO sys_dict (id, type, dict_key, dict_name, dict_value) VALUES (3, 'user_type', 'client_user', 'C端客户', '3');

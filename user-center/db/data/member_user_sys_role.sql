create table sys_role
(
    id          bigint               not null comment '角色id'
        primary key,
    name        varchar(80)          not null comment '角色名称',
    no          varchar(80)          null comment '角色编码',
    active_flag tinyint(1) default 1 not null comment '是否可用',
    sort_num    int        default 0 null comment '排序（升序）',
    notes       varchar(255)         null comment '角色备注',
    create_time datetime             not null comment '创建时间',
    update_time datetime             null comment '更新时间',
    create_by   bigint               not null comment '创建人',
    update_by   bigint               null comment '更新人'
)
    comment '系统-角色表' charset = utf8mb4;

INSERT INTO sys_role (id, name, no, active_flag, sort_num, notes, create_time, update_time, create_by, update_by) VALUES (1, '系统管理员', 'ROLE001', 1, 1, '最高权限', '2021-02-26 17:12:16', '2021-03-12 16:11:51', 0, 1);
INSERT INTO sys_role (id, name, no, active_flag, sort_num, notes, create_time, update_time, create_by, update_by) VALUES (2, '商户管理员', 'ROLE002', 1, 2, '', '2021-05-31 16:57:43', '2021-09-01 22:42:07', 0, 1);
INSERT INTO sys_role (id, name, no, active_flag, sort_num, notes, create_time, update_time, create_by, update_by) VALUES (1417005479799832576, 'C端用户', 'ROLE003', 1, 3, '', '2021-07-19 14:16:48', '2021-09-01 22:45:57', 1, 1);
INSERT INTO sys_role (id, name, no, active_flag, sort_num, notes, create_time, update_time, create_by, update_by) VALUES (1432155140697194496, '用户管理', 'TEST001', 1, 4, null, '2021-08-30 09:36:08', '2021-09-01 22:42:24', 1, 1);

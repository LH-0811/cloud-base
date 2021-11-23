create table sys_user
(
    id          bigint               not null comment '系统管理员用户'
        primary key,
    username    varchar(64)          null comment '用户名',
    password    varchar(255)         not null comment '密码',
    gender      int(2)     default 0 not null comment '0-保密 1-男 2-女',
    salt        varchar(32)          not null comment '密码盐',
    nick_name   varchar(64)          null comment '昵称',
    phone       varchar(32)          null comment '手机号',
    email       varchar(64)          null comment '邮箱',
    token       varchar(512)         null comment '登录口令',
    active_flag tinyint(1) default 1 not null comment '是否可用',
    del_flag    tinyint(1) default 0 not null comment '是否删除',
    last_login  datetime             null comment '最后登录时间',
    create_time datetime             not null comment '创建时间',
    create_by   bigint               not null comment '创建人',
    update_time datetime             null comment '更新时间',
    update_by   bigint               null comment '更新人'
)
    comment '用户中心-管理员用户表' charset = utf8mb4;

INSERT INTO sys_user (id, username, password, gender, salt, nick_name, phone, email, token, active_flag, del_flag, last_login, create_time, create_by, update_time, update_by) VALUES (1, 'admin', '8d421e892a47dff539f46142eb09e56b', 0, '1234', '管理员', '17666665555', '123@qq.com', null, 1, 0, '2021-11-23 21:09:29', '2021-04-24 15:39:20', 1, '2021-08-29 16:40:43', 1);
INSERT INTO sys_user (id, username, password, gender, salt, nick_name, phone, email, token, active_flag, del_flag, last_login, create_time, create_by, update_time, update_by) VALUES (1431556382892097536, 'test01', '4f0920c2f6a171f4f07cc0efd06e7b9', 1, '㍗硄?', 'test01', '13300000001', '123@qq.com', null, 1, 0, '2021-09-01 22:48:38', '2021-08-28 17:56:53', 1, '2021-09-01 21:35:18', 1431556382892097536);
INSERT INTO sys_user (id, username, password, gender, salt, nick_name, phone, email, token, active_flag, del_flag, last_login, create_time, create_by, update_time, update_by) VALUES (1431899731574575104, 'test02', '4c65209d7f61d0e09ca3bbddd9203f66', 0, '?삪헎', 'test02', '13300000003', '123@qq.com', null, 1, 1, null, '2021-08-29 16:41:14', 1, '2021-08-29 16:48:54', 1);

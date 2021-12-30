create table sys_position
(
    id          bigint       not null comment '岗位id'
        primary key,
    no          varchar(32)  null comment '岗位编号',
    name        varchar(32)  not null comment '岗位名称',
    notes       varchar(255) null comment '部门备注',
    create_time datetime     not null comment '创建时间',
    create_by   bigint       not null comment '创建人',
    update_time datetime     null comment '更新时间',
    update_by   bigint       null comment '更新人'
)
    comment '用户中心-岗位信息';

INSERT INTO sys_position (id, no, name, notes, create_time, create_by, update_time, update_by) VALUES (1, 'POST0001', '测试岗位', '测试岗位', '2021-08-27 21:48:51', 0, null, null);

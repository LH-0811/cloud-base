create table sys_dept
(
    id          bigint                  not null comment '部门id'
        primary key,
    parent_id   bigint                  not null comment '父级部门id',
    no          varchar(32)  default '' not null comment '部门编号',
    name        varchar(64)             not null comment '部门名称',
    is_leaf     tinyint(1)   default 1  not null comment '是否是叶子结点',
    router      varchar(256) default '' not null comment '路径',
    notes       varchar(255)            null comment '部门备注',
    create_time datetime                not null comment '创建时间',
    create_by   bigint                  not null comment '创建人',
    update_time datetime                null comment '更新时间',
    update_by   bigint                  null comment '更新人'
)
    comment '用户中心-部门表';

create index idx_router
    on sys_dept (router);

INSERT INTO sys_dept (id, parent_id, no, name, is_leaf, router, notes, create_time, create_by, update_time, update_by) VALUES (1, 0, '00000001', '系统管理部门', 0, '0,1', '系统管理员', '2021-08-07 18:54:43', 0, '2021-08-07 18:54:45', 0);
INSERT INTO sys_dept (id, parent_id, no, name, is_leaf, router, notes, create_time, create_by, update_time, update_by) VALUES (2, 1, '00000002', '测试部门1', 0, '0,1,2', '测试部门1', '2021-08-18 16:40:18', 0, '2021-08-18 16:40:20', null);
INSERT INTO sys_dept (id, parent_id, no, name, is_leaf, router, notes, create_time, create_by, update_time, update_by) VALUES (3, 0, '00000003', '业务部门', 0, '0,3', '业务部门', '2021-08-18 16:40:44', 0, '2021-08-18 16:40:48', null);
INSERT INTO sys_dept (id, parent_id, no, name, is_leaf, router, notes, create_time, create_by, update_time, update_by) VALUES (4, 0, '00000004', '风控部门', 1, '0,4', null, '2021-08-18 16:41:04', 0, '2021-08-18 16:41:07', null);
INSERT INTO sys_dept (id, parent_id, no, name, is_leaf, router, notes, create_time, create_by, update_time, update_by) VALUES (5, 3, '00000005', '客户管理部', 1, '0,3,5', null, '2021-08-18 16:41:36', 0, '2021-08-18 16:41:40', null);
INSERT INTO sys_dept (id, parent_id, no, name, is_leaf, router, notes, create_time, create_by, update_time, update_by) VALUES (6, 2, '00000006', '测试部门2', 1, '0,1,2,6', '测试部门2', '2021-08-18 16:41:59', 0, '2021-08-18 16:42:03', null);
INSERT INTO sys_dept (id, parent_id, no, name, is_leaf, router, notes, create_time, create_by, update_time, update_by) VALUES (1438079146788950016, 0, 'string', 'string', 1, '0,1438079146788950016', 'string', '2021-09-15 17:56:01', 1, null, null);
INSERT INTO sys_dept (id, parent_id, no, name, is_leaf, router, notes, create_time, create_by, update_time, update_by) VALUES (1438079390259908608, 0, '001', '技术部', 1, '0,1438079390259908608', '开发', '2021-09-15 17:57:00', 1, null, null);

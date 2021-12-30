create table sys_res
(
    id          bigint auto_increment comment '资源id'
        primary key,
    parent_id   bigint               null comment '父节点id',
    name        varchar(80)          not null comment '名称',
    type        int                  not null comment '类型：0-菜单组 1-"菜单" 2-"接口" 3-"权限码",4-"静态资源"',
    code        varchar(255)         null comment '权限标识符',
    url         varchar(255)         null comment '请求地址',
    icon        varchar(255)         null comment '图标',
    order_no    int                  null comment '排序',
    is_leaf     tinyint(1) default 1 not null comment '是否是叶子节点',
    router      varchar(255)         null comment '路由',
    notes       varchar(255)         null comment '备注',
    create_time datetime             not null comment '创建时间',
    update_time datetime             null comment '更新时间',
    create_by   bigint               not null comment '创建人',
    update_by   bigint               null comment '更新人'
)
    comment '系统-资源表' charset = utf8mb4;

create index idx_parent_id
    on sys_res (parent_id);

create index idx_router
    on sys_res (router);

INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (1, 0, '系统管理', 0, null, null, null, null, 0, '0,1', null, '2021-01-20 09:57:08', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (2, 0, '系统监控', 0, null, null, null, 2, 0, '0,2', null, '2021-09-02 10:32:34', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (3, 0, '开发工具', 0, null, null, null, null, 0, '0,3', null, '2021-11-08 22:55:51', null, 1, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (1000, 0, '系统管理员全PermsCode权限', 3, 'ALL', '/**', null, 0, 1, '0,1000', null, '2021-05-13 14:28:04', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (1001, 0, '系统管理员全URL权限', 2, 'ALL', '/**', null, 0, 1, '0,1001', null, '2021-05-13 14:28:04', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (10000, 1, '用户管理', 1, '', '/system/users', 'user', 1, 0, '0,1,10000', null, '2021-03-01 21:55:41', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (10001, 10000, '创建用户', 2, 'sys_user:create', '/sys_user/create', null, 1, 1, '0,1,10000,10001', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (10002, 10000, '修改用户', 2, 'sys_user:update', '/sys_user/update', null, 2, 1, '0,1,10000,10002', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (10003, 10000, '查询用户', 2, 'sys_user:query', '/sys_user/query', null, 3, 1, '0,1,10000,10003', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (10004, 10000, '重置用户密码', 2, 'sys_user:reset:pwd', '/sys_user/reset/pwd', null, 4, 1, '0,1,10000,10004', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (10005, 10000, '查询部门级联列表', 2, 'sys_dept:cascader:query', '/sys_dept/cascader/query', null, 5, 1, '0,1,10000,10005', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (10006, 10000, '查询全部岗位列表', 2, 'sys_positions:query:all', '/sys_positions/query/all', null, 6, 1, '0,1,10000,10006', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (10007, 10000, '查询全部角色列表', 2, 'sys_role:query:all_list', '/sys_role/query/all_list', null, 7, 1, '0,1,10000,10007', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (10008, 10000, '查询部门树', 2, 'sys_dept:tree:query', '/sys_dept/tree/query', null, 8, 1, '0,1,10000,10008', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (10009, 10000, '查询部门用户信息', 2, 'sys_user:dept:query', '/sys_user/dept/query', null, 9, 1, '0,1,10000,10009', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (20000, 1, '角色管理', 1, '', '/system/roles', 'role', 2, 0, '0,1,20000', null, '2021-03-01 22:00:37', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (20001, 20000, '创建角色', 2, 'sys_role:create', '/sys_role/create', null, 1, 1, '0,1,20000,20001', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (20002, 20000, '修改角色', 2, 'sys_role:update', '/sys_role/update', null, 2, 1, '0,1,20000,20002', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (20003, 20000, '查询角色', 2, 'sys_role:query', '/sys_role/query', null, 3, 1, '0,1,20000,20003', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (20004, 20000, '删除角色', 2, 'sys_role:delete/{roleId}', '/sys_role/delete/{roleId}', null, 4, 1, '0,1,20000,20004', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (20005, 20000, '查询资源列表', 2, 'sys_res:tree', '/sys_res/tree', null, 6, 1, '0,1,20000,20005', null, '2021-03-01 22:07:09', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (30000, 1, '资源管理', 1, '', '/system/resources', 'res', 3, 0, '0,1,30000', null, '2021-03-01 22:11:28', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (30001, 30000, '创建权限', 2, 'sys_res:create', '/sys_res/create', null, 1, 1, '0,1,30000,30001', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (30002, 30000, '获取全部资源树', 2, 'sys_res:tree', '/sys_res/tree', null, 2, 1, '0,1,30000,30002', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (30003, 30000, '删除权限', 2, 'sys_res:delete:{resId}', '/sys_res/delete/{resId}', null, 4, 1, '0,1,30000,30003', null, '2021-03-01 21:57:24', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (40000, 1, '部门管理', 1, null, '/system/depts', 'dept', 4, 0, '0,1,40000', null, '2021-08-25 21:06:52', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (40001, 40000, '创建部门', 2, 'sys_dept:create', '/sys_dept/create', null, 1, 1, '0,1,40000,40001', null, '2021-08-25 21:06:52', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (40002, 40000, '获取部门树', 2, 'sys_dept:tree:query', '/sys_dept/tree/query', null, 2, 1, '0,1,40000,40002', null, '2021-08-25 21:06:52', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (40003, 40000, '删除部门', 2, 'sys_dept:delete:{deptId}', '/sys_dept/delete/{deptId}', null, 3, 1, '0,1,40000,40003', null, '2021-08-25 21:06:52', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (50000, 1, '岗位管理', 1, null, '/system/positions', 'position', 5, 0, '0,1,50000', null, '2021-08-25 21:06:52', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (50001, 50000, '创建岗位', 2, 'sys_position:create', '/sys_position/create', null, 1, 1, '0,1,50000,50001', null, '2021-08-25 21:06:52', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (50002, 50000, '删除岗位', 2, 'sys_position:delete:{positionId}', '/sys_position/delete/{positionId}', null, 2, 1, '0,1,50000,50002', null, '2021-08-25 21:06:52', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (50003, 50000, '查询岗位', 2, 'sys_position:query', '/sys_position/query', null, 3, 1, '0,1,50000,50003', null, '2021-08-25 21:06:52', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (60000, 3, '代码生成', 1, null, '/dev-tools/code-generator', 'position', 0, 1, '0,3,60000', null, '2021-11-08 22:56:34', null, 1, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (60001, 3, '定时任务管理', 1, null, '/dev-tools/youji-task', 'position', 1, 1, '0,3,60001', null, '2021-11-08 22:56:34', null, 1, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (200000, 2, '数据库连接池', 1, null, '/monitor/database', 'database', 1, 1, '0,2,200000', null, '2021-09-02 10:34:21', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (200001, 2, '流控管理', 1, null, '/monitor/sentinel', 'sentinel', 2, 1, '0,2,200001', null, '2021-09-02 10:34:21', null, 0, null);
INSERT INTO sys_res (id, parent_id, name, type, code, url, icon, order_no, is_leaf, router, notes, create_time, update_time, create_by, update_by) VALUES (200002, 2, '链路跟踪', 1, null, '/monitor/zipkin', 'zipkin', 3, 1, '0,2,200003', null, '2021-09-02 10:34:21', null, 0, null);

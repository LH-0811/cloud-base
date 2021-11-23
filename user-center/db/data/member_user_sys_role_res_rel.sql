create table sys_role_res_rel
(
    id      bigint auto_increment comment '角色权限关系id'
        primary key,
    role_id bigint not null comment '角色id',
    res_id  bigint not null comment '权限id'
)
    comment '系统-角色-权限关系表' charset = utf8mb4;

create index permission_id
    on sys_role_res_rel (res_id)
    comment '权限id';

create index role_id
    on sys_role_res_rel (role_id)
    comment '角色id';

INSERT INTO sys_role_res_rel (id, role_id, res_id) VALUES (1417006204470706176, 1, 1000);
INSERT INTO sys_role_res_rel (id, role_id, res_id) VALUES (1417006204470706177, 1, 1001);
INSERT INTO sys_role_res_rel (id, role_id, res_id) VALUES (1433077784648511488, 1432155140697194496, 10000);
INSERT INTO sys_role_res_rel (id, role_id, res_id) VALUES (1433077784648511489, 1432155140697194496, 10001);
INSERT INTO sys_role_res_rel (id, role_id, res_id) VALUES (1433077784648511490, 1432155140697194496, 10002);
INSERT INTO sys_role_res_rel (id, role_id, res_id) VALUES (1433077784648511491, 1432155140697194496, 10003);
INSERT INTO sys_role_res_rel (id, role_id, res_id) VALUES (1433077784648511492, 1432155140697194496, 10004);
INSERT INTO sys_role_res_rel (id, role_id, res_id) VALUES (1433077784648511493, 1432155140697194496, 10005);
INSERT INTO sys_role_res_rel (id, role_id, res_id) VALUES (1433077784648511494, 1432155140697194496, 10006);
INSERT INTO sys_role_res_rel (id, role_id, res_id) VALUES (1433077784648511495, 1432155140697194496, 10007);
INSERT INTO sys_role_res_rel (id, role_id, res_id) VALUES (1433077784648511496, 1432155140697194496, 10008);
INSERT INTO sys_role_res_rel (id, role_id, res_id) VALUES (1433077784648511497, 1432155140697194496, 10009);
INSERT INTO sys_role_res_rel (id, role_id, res_id) VALUES (1433077784648511498, 1432155140697194496, 1);

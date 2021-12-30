create table sys_user_role_rel
(
    id      bigint not null comment '用户-角色关系表'
        primary key,
    user_id bigint not null comment '用户id',
    role_id bigint not null comment '角色id'
)
    comment '系统-用户-角色关系表' charset = utf8mb4;

create index role_id
    on sys_user_role_rel (role_id)
    comment '角色id索引';

create index user_id
    on sys_user_role_rel (user_id)
    comment '用户id索引';

INSERT INTO sys_user_role_rel (id, user_id, role_id) VALUES (1431899602914299904, 1, 1);
INSERT INTO sys_user_role_rel (id, user_id, role_id) VALUES (1431899732350521344, 1431899731574575104, 1);
INSERT INTO sys_user_role_rel (id, user_id, role_id) VALUES (1431899732350521345, 1431899731574575104, 1417005479799832576);
INSERT INTO sys_user_role_rel (id, user_id, role_id) VALUES (1433060899823484928, 1431556382892097536, 1432155140697194496);

create table sys_user_dept_rel
(
    id      bigint not null comment '用户部门关系id'
        primary key,
    user_id bigint not null comment '用户id',
    dept_id bigint not null comment '部门id'
)
    comment '用户中心-用户部门信息关系表';

INSERT INTO sys_user_dept_rel (id, user_id, dept_id) VALUES (1, 1, 1);
INSERT INTO sys_user_dept_rel (id, user_id, dept_id) VALUES (1431556383433162752, 1431556382892097536, 4);
INSERT INTO sys_user_dept_rel (id, user_id, dept_id) VALUES (1431899731859787776, 1431899731574575104, 6);

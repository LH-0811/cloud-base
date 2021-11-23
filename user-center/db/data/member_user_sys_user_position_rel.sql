create table sys_user_position_rel
(
    id          bigint not null comment '用户岗位关系id'
        primary key,
    user_id     bigint not null comment '用户id',
    position_id bigint not null comment '岗位id'
)
    comment '用户中心-用户岗位信息关系表';

INSERT INTO sys_user_position_rel (id, user_id, position_id) VALUES (1431899602431954944, 1, 1);
INSERT INTO sys_user_position_rel (id, user_id, position_id) VALUES (1431899732111446016, 1431899731574575104, 1);
INSERT INTO sys_user_position_rel (id, user_id, position_id) VALUES (1433060899504717824, 1431556382892097536, 1);

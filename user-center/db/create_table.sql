create table if not exists sys_dept
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

create table if not exists sys_dict
(
    id         bigint      not null comment '字典id'
    primary key,
    type       varchar(32) null comment '字典类型',
    dict_key   varchar(32) not null comment '字典键',
    dict_name  varchar(64) null comment '字典名',
    dict_value varchar(64) not null comment '字典值'
    )
    comment '系统表-字典表' charset = utf8mb4;

create table if not exists sys_position
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

create table if not exists sys_res
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

create table if not exists sys_role
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

create table if not exists sys_role_res_rel
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

create table if not exists sys_user
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

create table if not exists sys_user_dept_rel
(
    id      bigint not null comment '用户部门关系id'
    primary key,
    user_id bigint not null comment '用户id',
    dept_id bigint not null comment '部门id'
)
    comment '用户中心-用户部门信息关系表';

create table if not exists sys_user_position_rel
(
    id          bigint not null comment '用户岗位关系id'
    primary key,
    user_id     bigint not null comment '用户id',
    position_id bigint not null comment '岗位id'
)
    comment '用户中心-用户岗位信息关系表';

create table if not exists sys_user_role_rel
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


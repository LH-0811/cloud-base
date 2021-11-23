create table youji_task_exec_log
(
    id             bigint auto_increment comment '主键id'
        primary key,
    task_no        varchar(32)  not null comment '任务编号',
    task_name      varchar(256) not null comment '任务名称',
    worker_id      bigint       null comment '工作节点id',
    worker_ip      varchar(30)  null comment '工作节点ip',
    worker_port    int          null comment '工作节点端口号',
    task_param     text         null comment '参数:只支持字符串类型参数',
    contacts_name  varchar(64)  null comment '联系人',
    contacts_phone varchar(64)  null comment '联系人电话',
    contacts_email varchar(64)  null comment '联系人邮箱',
    finish_flag    tinyint(1)   null comment '是否执行完成',
    result_msg     text         null comment '任务执行结果信息',
    create_time    datetime     not null comment '创建时间',
    update_time    datetime     null comment '更新时间'
)
    comment '定时任务表日志表';

create table youji_task_info
(
    id             bigint auto_increment comment '主键'
        primary key,
    task_type      varchar(10) default '2' not null comment '（1-通过URL触发 2-遍历容器中的bean触发）',
    exec_type      varchar(10) default '1' not null comment '执行方式 (1-单节点执行 2-全节点执行)',
    task_no        varchar(32)             not null comment '任务编号',
    task_name      varchar(256)            not null comment '任务名称',
    corn           varchar(32)             not null comment '任务执行表达式',
    task_url       varchar(256)            null comment '任务执行触发的url地址',
    task_bean_name varchar(256)            null comment '应用上下文执行中对应的全限定类名',
    task_method    varchar(256)            null comment '应用上下文中对应的方法名，或者url的请求类型',
    task_param     text                    null comment '参数:只支持字符串类型参数',
    contacts_name  varchar(64)             null comment '联系人',
    contacts_phone varchar(64)             null comment '联系人电话',
    contacts_email varchar(64)             null comment '联系人邮箱',
    enable_flag    tinyint(1)              null comment '是否可用',
    exec_num       int                     null comment '执行次数统计',
    create_time    datetime                not null comment '创建时间',
    update_time    datetime                null comment '更新时间',
    last_exec_time datetime                null comment '最后一次执行时间'
)
    comment '定时任务表';

create table youji_task_worker
(
    id                   bigint auto_increment comment '工作节点id主键'
        primary key,
    task_id              bigint               not null comment '任务id',
    task_no              varchar(32)          not null comment '任务编号',
    worker_app_name      varchar(64)          not null comment '工作节点应用名称，配置文件中spring.application.name的配置',
    worker_ip            varchar(32)          not null comment '工作节点ip',
    worker_port          int                  not null comment '工作节点端口号',
    enable_flag          tinyint(1)           not null comment '工作节点是否可用',
    online_flag          tinyint(1) default 1 not null comment '节点是否在线',
    beat_fail_num        int        default 0 not null comment '心跳失败累计数(成功-1，失败+1 到0为止)',
    last_heart_beat_time datetime             null comment '最后一次心跳时间',
    exec_task_num        int        default 0 not null comment '执行任务的次数',
    last_exec_time       datetime             null comment '最后一次执行任务的时间'
)
    comment '定时任务的工作节点信息';


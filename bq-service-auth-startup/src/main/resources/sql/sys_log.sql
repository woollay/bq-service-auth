DROP TABLE IF EXISTS sys_log cascade;

create table sys_log
(
    id          varchar(64) primary key,
    op_type     varchar(64),
    op_module   varchar(64),
    op_user_id  varchar(64) references sys_user (id),
    op_address  varchar(64),
    op_resource varchar(64),
    op_detail   varchar(1024),
    level       int,
    create_time int8
);

comment on column sys_log.id is '随机ID主键';
comment on column sys_log.op_type is '操作类型';
comment on column sys_log.op_module is '操作模块';
comment on column sys_log.op_user_id is '用户ID';
comment on column sys_log.op_address is '操作地址';
comment on column sys_log.op_resource is '操作资源';
comment on column sys_log.op_detail is '操作详情';
comment on column sys_log.level is '级别';
comment on column sys_log.create_time is '创建时间';

insert into sys_log
values ('log001', 'Add', 'UserManager', NULL, '127.0.0.1', 'User', 'change zhangthree password', 2, 1566382443412);
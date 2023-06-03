DROP TABLE IF EXISTS sys_permission cascade;

create table sys_permission
(
    id          varchar(64) primary key,
    name        varchar(64) unique,
    parent_id   varchar(64),
    create_time int8 not null,
    remark      varchar(1024),
    status      int  not null
);

comment on column sys_permission.id is '随机ID主键';
comment on column sys_permission.name is '权限名称';
comment on column sys_permission.parent_id is '父级权限id';
comment on column sys_permission.create_time is '创建时间';
comment on column sys_permission.remark is '权限描述';
comment on column sys_permission.status is '状态';

insert into sys_permission
values ('permission000', 'menu', NULL, 1566382443412, NULL, 1),
       ('permission001', 'user', NULL, 1566382443412, NULL, 1),
       ('permission002', 'user.add', 'permission001', 1566382443412, NULL, 1),
       ('permission003', 'user.delete', 'permission001', 1566382443412, NULL, 1),
       ('permission004', 'user.get', 'permission001', 1566382443412, NULL, 1);
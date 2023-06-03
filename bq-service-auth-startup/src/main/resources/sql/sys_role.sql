DROP TABLE IF EXISTS sys_role cascade;

create table sys_role
(
    id          varchar(64) primary key,
    name        varchar(64) unique,
    create_time int8 not null,
    remark      varchar(1024),
    status      int  not null
);

comment on column sys_role.id is '随机ID主键';
comment on column sys_role.name is '角色名称';
comment on column sys_role.create_time is '创建时间';
comment on column sys_role.remark is '角色描述';
comment on column sys_role.status is '状态';

insert into sys_role
values ('role001', 'admin', 1566382443412, NULL, 1),
       ('role002', 'operator', 1566382443412, NULL, 1);
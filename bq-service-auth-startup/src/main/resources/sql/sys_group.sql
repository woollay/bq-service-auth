DROP TABLE IF EXISTS sys_group cascade;

create table sys_group
(
    id          varchar(64) primary key,
    name        varchar(64) unique,
    parent_id   varchar(64),
    create_time int8 not null,
    remark      varchar(1024)
);

comment on column sys_group.id is '随机ID主键';
comment on column sys_group.name is '组名称';
comment on column sys_group.parent_id is '父级组id';
comment on column sys_group.create_time is '创建时间';
comment on column sys_group.remark is '描述';

insert into sys_group
values ('group001', 'operationgroup', NULL, 1566382443412, NULL);
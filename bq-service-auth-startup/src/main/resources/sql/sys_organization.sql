DROP TABLE IF EXISTS sys_organization cascade;

create table sys_organization
(
    id            varchar(64) primary key,
    name          varchar(64) unique,
    parent_id     varchar(64),
    create_time   int8 not null,
    owner_user_id varchar(64),
    remark        varchar(1024)
);

comment on column sys_organization.id is '随机ID主键';
comment on column sys_organization.name is '组织名称';
comment on column sys_organization.parent_id is '父级组织id';
comment on column sys_organization.create_time is '创建时间';
comment on column sys_organization.owner_user_id is '负责人';
comment on column sys_organization.remark is '组织描述';

insert into sys_organization
values ('org001', 'development', NULL, 1566382443412, 'xyz001', NULL)
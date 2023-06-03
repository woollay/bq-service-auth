DROP TABLE IF EXISTS sys_group_permission cascade;

create table sys_group_permission
(
    id              varchar(64) primary key,
    group_id        varchar(64) references sys_group (id),
    permission_id   varchar(64) references sys_permission (id),
    permission_type int not null
);



comment on column sys_group_permission.id is '随机ID主键';
comment on column sys_group_permission.group_id is '组ID';
comment on column sys_group_permission.permission_id is '权限ID';
comment on column sys_group_permission.permission_type is '0:可访问,1:可授权';

insert into sys_group_permission
values ('grouppermission001', 'group001', 'permission001', 1);
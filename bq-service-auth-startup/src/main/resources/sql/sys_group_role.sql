DROP TABLE IF EXISTS sys_group_role cascade;

create table sys_group_role
(
    id       varchar(64) primary key,
    group_id varchar(64) references sys_group (id),
    role_id  varchar(64) references sys_role (id)
);



comment on column sys_group_role.id is '随机ID主键';
comment on column sys_group_role.group_id is '组ID';
comment on column sys_group_role.role_id is '角色ID';

insert into sys_group_role
values ('grouprole001', 'group001', 'role001');
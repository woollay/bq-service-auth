DROP TABLE IF EXISTS sys_role_permission cascade;

create table sys_role_permission
(
    id              varchar(64) primary key,
    role_id         varchar(64) references sys_role (id),
    permission_id   varchar(64) references sys_permission (id),
    permission_type int not null
);



comment on column sys_role_permission.id is '随机ID主键';
comment on column sys_role_permission.role_id is '角色ID';
comment on column sys_role_permission.permission_id is '权限ID';
comment on column sys_role_permission.permission_type is '0:可访问,1:可授权';

insert into sys_role_permission
values ('rolepermission000', 'role001', 'permission000', 1),
       ('rolepermission001', 'role001', 'permission001', 1),
       ('rolepermission002', 'role001', 'permission002', 1),
       ('rolepermission003', 'role001', 'permission003', 1),
       ('rolepermission004', 'role001', 'permission004', 1),
       ('rolepermission007', 'role002', 'permission000', 1),
       ('rolepermission005', 'role002', 'permission001', 1),
       ('rolepermission006', 'role002', 'permission004', 1);
DROP TABLE IF EXISTS sys_user_permission cascade;

create table sys_user_permission
(
    id              varchar(64) primary key,
    user_id         varchar(64) references sys_user (id),
    permission_id   varchar(64) references sys_permission (id),
    permission_type int not null
);



comment on column sys_user_permission.id is '随机ID主键';
comment on column sys_user_permission.user_id is '用户ID';
comment on column sys_user_permission.permission_id is '权限ID';
comment on column sys_user_permission.permission_type is '0:可访问,1:可授权';

insert into sys_user_permission
values ('userpermission001', 'user001', 'permission001', 1);
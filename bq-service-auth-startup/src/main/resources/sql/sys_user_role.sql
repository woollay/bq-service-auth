DROP TABLE IF EXISTS sys_user_role cascade;

create table sys_user_role
(
    id      varchar(64) primary key,
    user_id varchar(64) references sys_user (id),
    role_id varchar(64) references sys_role (id)
);



comment on column sys_user_role.id is '随机ID主键';
comment on column sys_user_role.user_id is '用户ID';
comment on column sys_user_role.role_id is '角色ID';

insert into sys_user_role
values ('userrole001', 'user001', 'role001'),
       ('userrole002', 'user002', 'role002');
DROP TABLE IF EXISTS sys_user_group cascade;

create table sys_user_group
(
    id       varchar(64) primary key,
    user_id  varchar(64) references sys_user (id),
    group_id varchar(64) references sys_group (id)
);



comment on column sys_user_group.id is '随机ID主键';
comment on column sys_user_group.user_id is '用户ID';
comment on column sys_user_group.group_id is '组ID';

insert into sys_user_group
values ('userrole001', 'user001', 'group001');
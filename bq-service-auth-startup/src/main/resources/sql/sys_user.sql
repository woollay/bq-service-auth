DROP TABLE IF EXISTS sys_user cascade;

create table sys_user
(
    id               varchar(64) primary key,
    name             varchar(64) unique,
    real_name        varchar(64),
    status           int           not null,
    password         varchar(2048) not null,
    org_id           varchar(64) references sys_organization (id),
    email            varchar(64),
    mobile           varchar(20),
    address          varchar(256),
    create_time      int8          not null,
    first_login_time int8,
    last_login_time  int8,
    login_fail_times int,
    ext1             varchar(64),
    ext2             varchar(64),
    ext3             varchar(64),
    ext4             varchar(64)
);

comment
    on column sys_user.id is '随机ID主键';
comment
    on column sys_user.name is '登录用户名';
comment
    on column sys_user.real_name is '真实姓名';
comment
    on column sys_user.status is '用户状态';
comment
    on column sys_user.password is '密码';
comment
    on column sys_user.org_id is '用户组织ID';
comment
    on column sys_user.email is '邮箱';
comment
    on column sys_user.mobile is '电话';
comment
    on column sys_user.address is '地址';
comment
    on column sys_user.create_time is '账号创建时间';
comment
    on column sys_user.first_login_time is '第一次登录时间';
comment
    on column sys_user.last_login_time is '最近登录时间';
comment
    on column sys_user.login_fail_times is '登录失败次数';
comment
    on column sys_user.ext1 is '预留字段1';
comment
    on column sys_user.ext2 is '预留字段2';
comment
    on column sys_user.ext3 is '预留字段3';
comment
    on column sys_user.ext4 is '预留字段4';

insert into sys_user
values ('user001', 'zhangsan', 'zhangsan', 1,
        '2c2ab8c48395f9aea0d2c88b79887c663ca5347aae229dd9a31a92e0485e558a11a4130f698422be71e9f6e576219533c68c781488f8a07aaa53e04ffd91237952461f7b152cc9910f8d09838a5064755bf1b379bc6c94d7e41e9ed79983b1001f988dd9245aba7d9624684a5cc65f8a657047fe28c2c4afbfd45bcdefabc963'
           , NULL, NULL, '13000000000', NULL, 1566382443412, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
       ('user002', 'lisi', 'lisi', 1,
        '2c2ab8c48395f9aea0d2c88b79887c663ca5347aae229dd9a31a92e0485e558a11a4130f698422be71e9f6e576219533c68c781488f8a07aaa53e04ffd91237952461f7b152cc9910f8d09838a5064755bf1b379bc6c94d7e41e9ed79983b1001f988dd9245aba7d9624684a5cc65f8a657047fe28c2c4afbfd45bcdefabc963'
           , NULL, NULL, '13100000000', NULL, 1566382443412, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
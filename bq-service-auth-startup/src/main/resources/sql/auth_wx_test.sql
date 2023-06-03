DROP TABLE IF EXISTS auth_wx_test cascade;

create table auth_wx_test
(
    open_id varchar(64) primary key,
    name    varchar(2048) not null,
    path    varchar(2048) not null,
    pwd     varchar(2048) not null,
    sec_key varchar(2048)
);

comment
    on column auth_wx_test.open_id is '随机ID主键';
comment
    on column auth_wx_test.name is '用户名';
comment
    on column auth_wx_test.path is '路径';
comment
    on column auth_wx_test.pwd is '秘钥';

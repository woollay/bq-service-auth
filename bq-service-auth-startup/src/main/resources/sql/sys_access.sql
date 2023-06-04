DROP TABLE IF EXISTS sys_access cascade;

--应用接入表
create table sys_access
(
    id          varchar(64) primary key,
    app_name    varchar(256)       not null,
    app_id      varchar(64) unique not null,
    app_key     varchar(256)       not null,
    status      int,
    expire_time int8,
    create_time int8,
    sec_key     varchar(2048)
);

comment
    on column sys_access.id is '随机ID主键';
comment
    on column sys_access.app_name is '应用名称';
comment
    on column sys_access.app_id is '应用唯一ID';
comment
    on column sys_access.app_key is '应用秘钥';
comment
    on column sys_access.status is '应用状态';
comment
    on column sys_access.expire_time is '应用秘钥过期时间';
comment
    on column sys_access.create_time is '应用创建时间';

-- insert into sys_access
-- values ('d001', 'bq-app', 'app001', 'pwd001', '1', 1566382443412, 1566382443412, null);

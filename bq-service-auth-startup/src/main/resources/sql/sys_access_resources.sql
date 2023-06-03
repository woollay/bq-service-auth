DROP TABLE IF EXISTS sys_access_resources cascade;

--应用接入表
create table sys_access_resources
(
    id     varchar(64) primary key,
    app_id varchar(64)  not null,
    url_id varchar(256) not null,
    status int
);

comment on column sys_access_resources.id is '随机ID主键';
comment on column sys_access_resources.app_id is '应用唯一ID';
comment on column sys_access_resources.url_id is '应用访问的资源id';
comment on column sys_access.status is '资源状态';

insert into sys_access_resources
values ('d001', 'app001', 'CLIENT_WX_API', '1');

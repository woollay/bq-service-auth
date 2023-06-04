DROP TABLE IF EXISTS bq_global_dict cascade;

--全局字典表
create table bq_global_dict
(
    id    varchar(64) primary key,
    key   varchar(64) not null,
    value varchar(64) not null,
    type  varchar(64)
);

comment
    on column bq_global_dict.id is '随机ID主键';
comment
    on column bq_global_dict.key is '字典key';
comment
    on column bq_global_dict.value is '字典值';
comment
    on column bq_global_dict.type is '字典类型';

insert into bq_global_dict
values ('d001', 'CLIENT_TOKEN_API', '/oauth/token', 'ClientUrl'),
       ('d002', 'CHANNEL_WX_API', 'https://api.weixin.qq.com/sns/oauth2/access_token', 'ChannelUrl'),

       ('d110', 'CLIENT_TOKEN_API', 'client.to.channel', 'ClientConfig'),
       ('d120', 'channel.status', 'true', 'CHANNEL_WX_API'),
       ('d121', 'channel.snake', 'true', 'CHANNEL_WX_API'),
       ('d011', 'CLIENT_QR_API', '/auth/qr', 'ClientUrl'),
       ('d012', 'CLIENT_WX_API', '/auth/wx', 'ClientUrl'),
       ('d013', 'CLIENT_WX_API', 'client.to.channel', 'ChannelConfig');

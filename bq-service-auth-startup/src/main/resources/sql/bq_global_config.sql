DROP TABLE IF EXISTS bq_global_config cascade;

--id, client_id, url_id, svc_id, svc_value
create table bq_global_config
(
    id          varchar(64) primary key,
    svc_id      varchar(64)  not null,
    client_id   varchar(64),
    url_id      varchar(64),
    svc_value   varchar(256) not null,
    create_time int8         not null
);

comment
    on column bq_global_config.id is '随机ID主键';
comment
    on column bq_global_config.svc_id is '业务参数id';
comment
    on column bq_global_config.client_id is '客户id';
comment
    on column bq_global_config.url_id is '接口id';
comment
    on column bq_global_config.svc_value is '业务参数的配置值';
comment
    on column bq_global_config.create_time is '创建时间';

insert into bq_global_config
values
    --客户app001调用CLIENT_TOKEN_API接口的qps限流值为100
    ('svc001', 'client.limit.qps', 'app001', 'CLIENT_TOKEN_API', 100, 1566382443412),
    --当客户app001调用CLIENT_TOKEN_API接口，没有配置svc001限流规则时，则默认qps限流值为1000
    ('svc002', 'client.limit.qps', 'app001', NULL, 1000, 1566382443412),
    --当客户app001调用CLIENT_TOKEN_API接口，没有配置svc001/svc002限流规则时，则默认qps限流值为500
    ('svc003', 'client.limit.qps', NULL, 'CLIENT_TOKEN_API', 500, 1566382443412),
    --当客户app001调用CLIENT_TOKEN_API接口，没有配置svc001/svc002/svc003限流规则时，则默认qps限流值为10000
    ('svc004', 'client.limit.qps', NULL, NULL, 10000, 1566382443412),
    --客户app001调用CLIENT_TOKEN_API接口的qps限流单位为1000ms(即qps,表示每秒多少次)
    ('svc005', 'client.limit.qps.unit', 'app001', 'CLIENT_TOKEN_API', 1000, 1566382443412),
    ('svc006', 'client.limit.qps.unit', NULL, NULL, 1000, 1566382443412),

    ('svc011', 'client.limit.max', 'app001', 'CLIENT_TOKEN_API', 10000, 1566382443412),
    --客户app001调用CLIENT_TOKEN_API接口的max限流单位为86400000ms(即每天,表示每天多少次)
    ('svc012', 'client.limit.max.unit', 'app001', 'CLIENT_TOKEN_API', 86400000, 1566382443412),
    ('svc013', 'client.limit.max.unit', NULL, NULL, 86400000, 1566382443412),

    --客户CLIENT_TOKEN_API调用渠道CHANNEL_WX_API接口的qps限流值为100
    ('svc101', 'channel.limit.qps', 'CLIENT_TOKEN_API', 'CHANNEL_WX_API', 100, 1566382443412),
    --当客户CLIENT_TOKEN_API调用渠道CHANNEL_WX_API接口，没有配置svc101限流规则时，则默认qps限流值为1000
    ('svc102', 'channel.limit.qps', 'CLIENT_TOKEN_API', NULL, 1000, 1566382443412),
    --当客户CLIENT_TOKEN_API调用渠道CHANNEL_WX_API接口，没有配置svc101/svc102限流规则时，则默认qps限流值为500
    ('svc103', 'channel.limit.qps', NULL, 'CHANNEL_WX_API', 500, 1566382443412),
    --当客户CLIENT_TOKEN_API调用渠道CHANNEL_WX_API接口，没有配置svc101/svc102/svc103限流规则时，则默认qps限流值为10000
    ('svc104', 'channel.limit.qps', NULL, NULL, 10000, 1566382443412),
    --客户CLIENT_TOKEN_API调用渠道CHANNEL_WX_API接口的qps限流单位为1000ms(即qps,表示每秒多少次)
    ('svc105', 'channel.limit.qps.unit', 'CLIENT_TOKEN_API', 'CHANNEL_WX_API', 1000, 1566382443412),
    ('svc106', 'channel.limit.qps.unit', NULL, NULL, 1000, 1566382443412),

    ('svc111', 'channel.limit.max', 'CLIENT_TOKEN_API', 'CHANNEL_WX_API', 100000, 1566382443412),
    --客户CLIENT_TOKEN_API调用渠道CHANNEL_WX_API接口的max限流单位为86400000ms(即每天,表示每天多少次)
    ('svc112', 'channel.limit.max.unit', 'CLIENT_TOKEN_API', 'CHANNEL_WX_API', 86400000, 1566382443412),
    ('svc113', 'channel.limit.max.unit', NULL, NULL, 86400000, 1566382443412),

    ('svc201', 'client.to.channel', 'app001', 'CLIENT_TOKEN_API', 'CHANNEL_WX_API', 1566382443412),
    ('svc202', 'channel.status', NULL, 'CHANNEL_WX_API', '1', 1566382443412),
    ('svc221', 'client.to.channel', 'app001', 'CLIENT_WX_API', 'CHANNEL_WX_API', 1566382443412),

    ('svc231', 'client.limit.qps', 'app001', 'CLIENT_WX_API', 2, 1566382443412),
    ('svc232', 'client.limit.max', 'app001', 'CLIENT_WX_API', 2, 1566382443412),
    ('svc234', 'channel.limit.qps', 'CLIENT_WX_API', 'CHANNEL_WX_API', 2, 1566382443412),
    ('svc235', 'channel.limit.max', 'CLIENT_WX_API', 'CHANNEL_WX_API', 2, 1566382443412);

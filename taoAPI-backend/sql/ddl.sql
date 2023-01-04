

-- 接口信息
create table if not exists taoAPI.`interface_info`
(
    `id` bigint not null auto_increment  primary key,
    `name` varchar(256) not null comment '接口名',
    `url` varchar(512) not null comment '接口地址',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `description` varchar(256) null comment '描述',
    `status` int default 0 not null comment '接口状态',
    `isDelete` tinyint not null,
    `createTime` datetime default CURRENT_TIMESTAMP not null,
    `updateTime` datetime default CURRENT_TIMESTAMP not null,
    `method` varchar(256) not null comment '请求类型',
    `userId` bigint not null comment '创建人'
) comment '接口信息';

insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('胡天翊', 'www.steven-schuppe.com', '本溪', '东南体育大学', 'qhNjD', 0, 0, '2022-01-14 10:41:27', '2022-01-23 10:19:14', 'Get', 31);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('陆嘉熙', 'www.ciara-klocko.org', '阳泉', '北技术大学', 'h0U9', 0, 0, '2022-10-03 20:03:26', '2022-10-10 01:01:44', 'Get', 338562236);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('夏振家', 'www.gertrude-huel.org', '海门', '西北科技大学', 'KWi', 0, 0, '2022-09-21 14:38:07', '2022-03-03 07:36:14', 'Get', 525659);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('魏鹭洋', 'www.tyree-lang.co', '江阴', '东经贸大学', 'X8mr', 0, 0, '2022-09-27 16:29:46', '2022-01-28 17:15:59', 'Get', 2369732);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('范金鑫', 'www.dorthy-graham.co', '金华', '西南农业大学', 'Pz4tA', 0, 0, '2022-07-03 11:30:23', '2022-07-31 18:31:59', 'Get', 4);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('何修洁', 'www.larissa-hahn.info', '大庆', '东科技大学', 'nOyP1', 0, 0, '2022-10-08 05:24:46', '2022-04-10 15:56:34', 'Get', 61582);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('魏烨霖', 'www.jesusita-boyle.net', '泰安', '北理工大学', 'Qs', 0, 0, '2022-10-02 15:29:50', '2022-08-25 11:27:09', 'Get', 3698);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('蔡子轩', 'www.genaro-torp.biz', '乳山', '西南科技大学', 'VGBJz', 0, 0, '2022-09-27 05:20:38', '2022-07-16 19:34:22', 'Get', 53938239);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('陈健雄', 'www.houston-jacobi.name', '连云港', '东南艺术大学', 'TrZIS', 0, 0, '2022-07-27 00:01:41', '2022-07-21 16:52:38', 'Get', 50);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('何潇然', 'www.trevor-simonis.biz', '攀枝花', '东南科技大学', 'rL', 0, 0, '2022-11-21 18:17:09', '2022-04-25 20:45:31', 'Get', 822);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('王嘉熙', 'www.britt-oreilly.org', '茂名', '北科技大学', 'pne', 0, 0, '2022-06-04 19:34:21', '2022-06-01 12:16:19', 'Get', 31820);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('林旭尧', 'www.brent-terry.name', '临沂', '西理工大学', '78', 0, 0, '2022-06-11 22:25:51', '2022-09-28 23:10:18', 'Get', 363662770);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('万驰', 'www.rey-considine.net', '菏泽', '东北技术大学', 'TA9', 0, 0, '2022-01-25 13:00:43', '2022-06-18 01:29:33', 'Get', 325767720);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('郑伟诚', 'www.ilana-wilkinson.info', '济宁', '东北艺术大学', '4Dk', 0, 0, '2022-06-24 15:12:43', '2022-01-18 08:04:16', 'Get', 24519);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('李潇然', 'www.heath-ledner.net', '廊坊', '东经贸大学', 'OkPBR', 0, 0, '2022-10-07 02:52:31', '2022-02-04 04:55:05', 'Get', 8195);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('刘弘文', 'www.leroy-senger.info', '常熟', '中国经贸大学', 'qwA', 0, 0, '2022-01-31 19:49:11', '2022-03-24 15:57:26', 'Get', 5016571);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('陆弘文', 'www.johnnie-wintheiser.info', '云浮', '北农业大学', '2yUk', 0, 0, '2022-10-29 03:46:51', '2022-03-29 13:53:19', 'Get', 97425950);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('史明哲', 'www.johnny-borer.biz', '三亚', '西北技术大学', 'dhBT', 0, 0, '2022-09-28 06:13:53', '2022-07-03 16:04:32', 'Get', 73);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('莫瑞霖', 'www.haley-schaefer.info', '长春', '西南农业大学', 'OUkn', 0, 0, '2022-04-13 20:56:21', '2022-02-13 11:49:21', 'Get', 56401);
insert into taoAPI.`interface_info` (`name`, `url`, `requestHeader`, `responseHeader`, `description`, `status`, `isDelete`, `createTime`, `updateTime`, `method`, `userId`) values ('郝明', 'www.myron-von.info', '吉林', '中国经贸大学', 'bVL', 0, 0, '2022-11-26 09:41:45', '2022-07-09 04:50:42', 'Get', 5);
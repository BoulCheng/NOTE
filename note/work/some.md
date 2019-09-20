- 根据broker_id 分库， user_id分表； 按照单表500w条记录计算，现在1000w左右；预估20亿条委托；20e8/500e4=
- 400个表；取2的幂，所以建512个表；分4个库（ option0-3），每个库128个表（option_order_0-127)；



```


create DATABASE option0; 
use option0;
delimiter $$
drop PROCEDURE IF EXISTS loopExec;
CREATE PROCEDURE loopExec(IN sql1 VARCHAR(300), IN num INT, IN sql2 VARCHAR(16383))
BEGIN
DECLARE i int(11) DEFAULT 0; WHILE i < num DO
SET @sqlstr = CONCAT(sql1, i, sql2);
prepare stmt from @sqlstr; execute stmt;
SET i = i + 1;
END WHILE; END;


```

```
call loopExec("CREATE TABLE option_order_", 0, 128,"(
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`order_num` varchar(32) NOT NULL COMMENT ' 号',
`user_id` bigint(20) NOT NULL COMMENT '用 ID',
`asset` varchar(16) NOT NULL COMMENT ' 的物',
`type` int(11) NOT NULL COMMENT ' 型:1 按
`spot_index` decimal(10,4) NOT NULL COMMENT '下
`price` decimal(20,10) NOT NULL COMMENT ' 金 ',
`principal` decimal(20,10) NOT NULL COMMENT '本金',
`fee` decimal(22,12) NOT NULL COMMENT '手 ',
`amount` bigint(20) DEFAULT NULL COMMENT '按份数
`direction` int(11) NOT NULL COMMENT '看 看跌:1 看 `rate_of_return` decimal(10,4) DEFAULT NULL COMMENT '按 金金 `unit_price` decimal(20,10) DEFAULT NULL COMMENT '按份数 `profit` decimal(20,10) DEFAULT NULL COMMENT '收益', `trading_time` bigint(20) NOT NULL COMMENT '成交 ', `settlement` int(11) NOT NULL DEFAULT '0' COMMENT ' 算状 :0 未
算收益中，2 已 算, 收益已到 ,3 算失 退款中,4 算失 退款已到 ', `settlement_time` bigint(20) DEFAULT NULL COMMENT ' 算 ', `valid` int(1) NOT NULL DEFAULT '0',
`gmt_create` datetime NOT NULL,
`gmt_modified` datetime NOT NULL,
`currency` int(11) NOT NULL DEFAULT '2' COMMENT ' 种:1 USDT, 2 BTC, 3 ETH, 4 FOTA, 5 EOS, 6 BCH, 7 ETC, 8 LTC',
`broker_id` bigint(20) unsigned DEFAULT NULL COMMENT '券商id', `extra_info` varchar(2000) DEFAULT NULL,
`low_strike` decimal(32,16) DEFAULT NULL COMMENT 'gamma 行 价下限', `high_strike` decimal(32,16) DEFAULT NULL COMMENT 'gamma 行 价上限', `strike_price` decimal(20,10) DEFAULT NULL COMMENT '行 价', `settlement_price` decimal(20,10) DEFAULT NULL COMMENT ' 算价格', PRIMARY KEY (`id`),
UNIQUE KEY `option_order_order_num_uindex` (`order_num`),
KEY `option_order_trading_time_index` (`trading_time`),
KEY `idx_user_time` (`user_id`,`trading_time`,`currency`,`settlement`), KEY `idx_user_full_time`
(`user_id`,`currency`,`settlement`,`trading_time`), KEY `idx_broker_id` (`broker_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;"); delimiter $$
```

```
use option0;
DELIMITER $$
CREATE PROCEDURE `truncateData`() 
begin
declare i int; declare j int; set i=0;
while i<128 do ##循 每个表
select " 入i",i;##
## 除之前的表
set @delSqlStr=concat("truncate option_order_",i);
PREPARE trunStmt from @delSqlStr; EXECUTE trunStmt ;
set i=i+1;
end while; end
$$
DELIMITER ;
call truncateData();
DROP PROCEDURE IF EXISTS truncateData;



```
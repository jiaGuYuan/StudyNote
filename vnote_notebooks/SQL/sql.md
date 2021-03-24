# sql

## 初始化环境
``` # sql
-- 建表
CREATE TABLE order_product (
	  id INTEGER PRIMARY KEY AUTOINCREMENT,
	  order_key CHAR(12) NOT NULL ,
	  product_id INT NOT NULL ,
		product_unit_price DECIMAL(8,2) NOT NULL,
	  product_quantity INT NOT NULL DEFAULT 1 -- 默认值为1
	);
	
-- 创建索引
CREATE INDEX index_order_key ON order_product(order_key);

-- 插入数据
INSERT INTO order_product(order_key, product_id, product_unit_price, product_quantity)
VALUES("KEY2020-12-05$000001", 10032725, 3.25, 1),
      ("KEY2020-12-05$000001", 10032600, 0.5, 8),
			("KEY2020-12-05$000001", 10032601, 2.0, 1),
			("KEY2020-12-05$000001", 10032602, 1.2, 2),
			("KEY2020-12-05$000002", 10000001, 0.5, 1),
			("KEY2020-12-05$000002", 10000002, 1.5, 2),
			("KEY2020-12-05$000002", 10000003, 100.0, 1),
			("KEY2020-12-05$000002", 10000003, 20.5, 2),
			("KEY2020-12-05$000003", 10000000, 6.0, 2);
```
# 获取分组内某列最大(或最小)的N条记录
以'取order_key组内product_id前N大的记录'为例
**Note**: 思路转换: 
   求分组内最大的N条记录 
    <==> 组内降序(排名)取前N条
    <==> 计算组内比'我'大的值有多少个,再按该计数升序,取前N条［该方法需要进行自关联］．
注: 当组内指定列存在相同值时,自关联等到的结果与RANK相同．
eg:指定列取值为1,2,2,3; 自关联后通过计数可统计其为对应的第X大的值：
通过'SELECT COUNT(tmp.sort_field)＋1 FROM tablename base_t 
    LEFT JOIN tablename tmp 
        ON tmp.group_field=base_t.group_field AND **tmp.sort_field > base_t.sort_field**
    GROUP BY base_t.group_field, tmp.sort_field'进行自关联来计算在group_field组内base_t.sort_field是第几大的数．
1 -- 第4大
2 -- 第2大
2 -- 第2大
3 -- 第1大
**Note**: 关联时使用'tmp.sort_field > base_t.sort_field' & 'count(tmp.sort_field)+1',
而不使用'tmp.sort_field>=base_t.sort_field' & 'count(*)',是为了正确处理并列排名的情况．
使用'tmp.sort_field > base_t.sort_field'时,对应的计数不能使用count(*),因为最大值关联后对应值是NULL,count()应该为0.

当要实现类似DENSE_RANK的功能时,可通过右表去重　＋ order by ＋ limit来实现


## 方法一: 先在子查询中进行排序,再对子查询结果分组
**Note**: 该方法只适用于取分组内最大(或最小)的一条记录,无法取多条.
分组内最大记录: 先对product_id降序,然后分组取第一条即可
``` #
-- 取order_key组内product_id最的记录
-- Note: 'SELECT * FROM table GROUP BY xxx'在sqlite中取的是分组的最后一条,而在mysql中取的是分组的第一条
SELECT * FROM (
    SELECT * FROM order_product ORDER BY product_id -- mysql: desc
) t
GROUP BY order_key;
```


## 方法二(不推荐):使用自关联,计算组内比'我'大的有多少个
思路: 使用自关联,计算组内比'我'大的有多少个,再按该计数升序,取前N条
``` #
-- 分组内最大的N条记录:取order_key组内product_id前N大的记录
-- note:这种方法,子查询执行次数很多,速度慢
SELECT t.* FROM (
    SELECT base_t.*,
    -- 计算在order_key组内base_t.product_id是第几大的
    (SELECT COUNT(tmp.product_id)+1 FROM order_product tmp 
        WHERE tmp.order_key=base_t.order_key 
            AND tmp.product_id > base_t.product_id) AS proid_rank 
    FROM order_product base_t
) t
WHERE proid_rank <= 2
ORDER BY t.order_key, proid_rank;
```

上面写法的变体
``` #
-- 取order_key组内product_id前N大的记录
-- 同上(效率低)
SELECT 
    base_t.*
FROM order_product base_t
WHERE (SELECT COUNT(tmp.product_id)+1 FROM order_product tmp 
        WHERE tmp.order_key=base_t.order_key
              AND tmp.product_id > base_t.product_id) <= 2 -- 计算在order_key组内base_t.product_id是第几大的
ORDER BY base_t.order_key;
```

## 方法三(推荐)：使用自关联,计算组内比'我'大的有多少个
``` #使用自关联,计算组内比'我'大的有多少个
-- 取order_key组内product_id前N大的记录
```sql
select -- 保持表中重复的order_key, salary
    op_base.order_key,
    op_base.product_id
from order_product op_base
inner join (
    -- 通过自联结,计算组内排名(op1.order_key, op1.product_id已去重)
    select
        op1.order_key,
        op1.product_id
    from 
        order_product op1
    left join order_product op2
        ON op2.order_key=op1.order_key
        AND op2.product_id > op1.product_id 
    group by op1.order_key, op1.product_id
    having count(DISTINCT op2.product_id) < N
) op_rank ON op_base.order_key=op_rank.order_key AND op_base.product_id=op_rank.product_id
order by order_key, product_id desc
```
[参考](https://leetcode-cn.com/problems/department-top-three-salaries/submissions/)

## 方法四(推荐)：使用开窗函数
```sql
-- 使用开窗函数
select * from (
    SELECT 
        order_key,
        product_id,
        product_unit_price, 
        product_quantity,
        ROW_NUMBER() OVER(PARTITION BY order_key ORDER BY product_id desc) AS proid_rank
    FROM order_product
) t
WHERE proid_rank <= 2
```

# 关于开窗函数
MySql在8.0的版本增加了对开窗函数的支持，终于可以在MySql使用开窗函数了。开窗函数又称OLAP函数(Online Analytical Processing).

开窗函数的语法结构：
```
# Key word :Partiton by & order by
<开窗函数> OVER ([PARTITION by <列清单>]
                     Order by <排序用列清单>）
```
开窗函数大体分为两种：
1. 能够作为开窗函数的聚合函数：（sum,avg,count,max,min）
2. 专用开窗函数：(Rank,Dense_Rank,Row_Number)

专用窗口函数
语句中:
    'PARTITION By'指定排序的对象范围(将行[记录]进行分组); 
    'ORDER BY'指定了按照哪些列,何种顺序进行排列（对记录定义排序规则）。
    Row_Number: 将查询到的数据进行排序,并产生一个行号; ［组内不重复递增:1,2,3,4,5....］
    DENSE_RANK: 将查询到的数据进行排序,并产生一个可并列的连续排名号;［组内可重复连续递增:1,2,2,3,4....］
    RANK:将查询到的数据进行排序,并产生一个可并列的跳跃排名号;［组内可重复跳跃递增:1,2,2,4,5....］


# MySql日期操作函数

## 获取时间
```sql
-- 获得当前日期+时间(date + time): now()
-- 获得当前时间戳函数: current_timestamp()
select now(), current_timestamp();
```

## DateTime <==> str转换函数
```sql
-- str ==> DateTime: str_to_datetime(datetime_str, format_str)/str_to_date
-- datetime ==> str: date_format(datetime, format_str)/time_format(time, format_str) 
-- %Y: 四位数年; %m: 二位数月(01..12); %d:每月的某天(01..31); 
-- %H:小时(00..23)/%h:小时(01..12); %i:分钟(00..59); %s/%S:秒(00..59)
-- %j: 一年中的第几天(001..366)
-- %f: 微秒

-- DateTime ==> str
select date_format(now(), '%Y-%m-%d %H:%i:%s.%f') AS cur_datetime_str,
	time_format(now(), '%H:%i:%s.%f') AS cur_time_str
	;

-- str ==> DateTime
select str_to_datetime('2020-12-13 17:24:22.354', '%Y-%m-%d %H:%i:%s.%f') AS datetime_str;
```

## 日期 & 天数 转换函数: 
```sql
-- to_days(date): 返回从0000年(公元1年)至当前日期的总天数
-- from_days(days): 返回从0000年(公元1年)相距days天的日期
select to_days('0000-01-01'), to_days('0000-01-01 17:25:43'), 
	from_days(1), from_days(2);
```

## 秒数 & 时间 转换函数: 
```sql
-- 时间==>秒数: time_to_sec(time)
-- 秒数==>时间: sec_to_time(seconds)
select time_to_sec('01:00:05'),
		   sec_to_time(3605);
```

## 拼凑日期,时间
```sql
-- 函数:makdedate(year,dayofyear), maketime(hour,minute,second)
select makedate(2001, 32), 
       maketime(12,15,30);
```


## Unix 时间戳 & 日期时间 转换函数
```sql
-- 日期时间 ==> 时间戳: unix_timestamp(datetime) #　datetime默认为当前日期时间
-- 时间戳 ==> 日期时间: from_unixtime(unix_timestamp, format)
select unix_timestamp(), unix_timestamp(now()),
	unix_timestamp('2008-08-08 12:30:00'),
　from_unixtime(1218169800, '%Y-%m-%d %H:%i:%s');
```

## 日期时间计算函数
```sql
-- 为日期增加一个时间间隔: date_add(datetime, interval xxx day/hour/minute/secod/year/month)
select now(),
       date_add(now(), interval -1 day),
			 date_add(now(), interval 1 day),
			 date_add(now(), interval 1 hour),
	  	 date_add(now(), interval 1 minute),
	  	 date_add(now(), interval 1 second),
	  	 date_add(now(), interval 1 month),
	  	 date_add(now(), interval 1 year)
;

-- 为日期减去一个时间间隔: date_sub
-- 用法与date_add类似

-- 计算两个日期 或时间之差: 
-- datediff(date1, date2): date1-date2　只计算日期级别的差值(无法计算时间级别, 计算跨天)
-- timediff(time1, time2): time1-time2  只计算时间级别的差值(计算天以内)
select datediff('2008-08-08', '2008-08-01'),
		   datediff('2008-08-01', '2008-08-08'),
			 datediff('2008-08-01 08:00:00', '2008-08-02 09:00:01'),
			 timediff('08:08:08', '23:59:59'),
			 timediff('2008-08-08 08:08:08', '2008-08-09 23:59:59')
;
```

## 时间戳(timestamp)转换,增减函数
```sql
-- datetime ==> 时间戳: timestamp(date)
-- 时间戳增减: timestamp(dt, time) # dt + time # 只能加一个时间(不能加日期)
-- 			       timestampadd(unit, interval, datetime_expr)
--             timestampdiff(unit, datetime_expr1, datetime_expr2) # datetime_expr2 - datetime_expr1
-- unit: day/hour/minute/secod/year/month
select timestamp('2008-08-08 08:00:00'),
		   timestamp('2008-08-08 08:00:00', '01:01:01'),
			 timestampadd(day, 1, '2008-08-08 08:00:00'),
			 timestampadd(day, -1, '2008-08-08 08:00:00'),
			 timestampadd(minute, 1, '2008-08-08 08:00:00'),
			 timestampdiff(day,'2008-08-08 12:00:00','2008-08-07 00:00:00')
;
```




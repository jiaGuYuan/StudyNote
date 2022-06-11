-- sqlit查询建表语句
SELECT sql FROM sqlite_master WHERE type="table" AND name = "order_product";

-- 删表
-- drop table order_product

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
VALUES("KEY2020-12-05$000001", 10032601, 3.25, 1),
      ("KEY2020-12-05$000001", 10032600, 0.5, 8),
			("KEY2020-12-05$000001", 10032603, 2.0, 1),
			("KEY2020-12-05$000001", 10032602, 1.2, 2),
			("KEY2020-12-05$000002", 10000002, 0.5, 1),
			("KEY2020-12-05$000002", 10000000, 5.5, 2),
			("KEY2020-12-05$000002", 10000001, 1.5, 2),
			("KEY2020-12-05$000002", 10000004, 100.0, 1),
			("KEY2020-12-05$000002", 10000003, 20.5, 2),
			("KEY2020-12-05$000003", 10000000, 6.0, 2);
		

-- 取order_key组内product_id最大(或最小)的记录
-- 方法:先对product_id升序(降序),然后分组取第一条即可 == 需要使用子查询
-- Note: 'select * from table GROUP BY xxx'在sqlite中取的是分组的最后一条,而在mysql中取的是分组的第一条
-- Eg: 取order_key组内product_id最大
select * from (
	select * from order_product ORDER BY product_id -- mysql: desc
) t
GROUP BY order_key;


-- 取order_key组内product_id前N大的记录
-- 这种方法,子查询执行次数很多,速度慢
select t.* from (
	select base_t.*,
	(select count(tmp.product_id)+1 from order_product tmp where tmp.order_key=base_t.order_key AND tmp.product_id > base_t.product_id) AS proid_rank -- 计算在order_key组内base_t.product_id是第几大的
	from order_product base_t
) t
where proid_rank <= 2
order by t.order_key, proid_rank;

-- 取order_key组内product_id前N大的记录
-- 同上(效率低)
select 
	base_t.*
from order_product base_t
where (select count(tmp.product_id)+1 from order_product tmp where tmp.order_key=base_t.order_key AND tmp.product_id > base_t.product_id) <= 2 -- 计算在order_key组内base_t.product_id是第几大的
order by base_t.order_key;

-- 取order_key组内product_id前N大的记录
select * from (
	-- 通过自关联计算组内排名: 通过自关联计算在order_key组内t1.product_id是第几大的
	select 
		base_t.*,
		count(tmp.product_id)+1 AS proid_rank
	from order_product base_t
	left join order_product tmp
		on base_t.order_key=tmp.order_key
			AND base_t.product_id < tmp.product_id
	group by base_t.order_key, base_t.product_id
)
where proid_rank <= 2
ORDER BY order_key, proid_rank;

-- 使用开窗函数
select 
	order_key,
	product_id,
	product_unit_price, 
	product_quantity,
	row_number() over(partition by order_key order by product_id desc) AS proid_rank
from order_product
where proid_rank <= 2


DISTINCT


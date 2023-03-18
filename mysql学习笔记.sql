'mysql 8'配置文件: /etc/my.cnf
MySQL第一次启动后会创建超级管理员账号root@localhost,初始密码存储在日志文件中:sudo grep 'temporary password' /var/log/mysqld.log

如何启动/停止/重启MySQL:
一、启动方式
    1、使用 service 启动：service mysqld start
    2、使用 mysqld 脚本启动：/etc/inint.d/mysqld start
    3、使用 safe_mysqld 启动：safe_mysqld&
    
    centos启动mysql: systemctl status mysqld.service

二、停止
    1、使用 service 启动：service mysqld stop
    2、使用 mysqld 脚本启动：/etc/inint.d/mysqld stop
    3、mysqladmin shutdown

三、重启
    1、使用 service 启动：service mysqld restart
    2、使用 mysqld 脚本启动：/etc/inint.d/mysqld restart

登录mysql:
    -h hostAddr -P Port -u userName -D DbName
    1. 本地登录: mysql -u username -p (回车,会提示输入密码)
    2. 远程登录: mysql -h hostAddr -u userName -p(回车,会提示输入密码)  
    --eg:  mysql -h 192.168.0.200 -P 3306 -u gjy -p (Port默认3306)
    或: mysql -h hostAddr -u userName -pMyPassWord (密码与-p之前没有空格)
    ubuntu mysql用户名密码: gjy/gujiayuan root/Gujiayuan
    
新版本的mysql修改root密码(我的ECS,mysql是"Ver 8.0.19"):
    安装好mysql后首次直接使用' mysql -u username -p'登陆会报错'ERROR 1045 (28000): Access denied...'
    需要先使用skip-grant-table方式登录,修改密码后再以正常方式登录
    1. 修改配置使mysql可以用skip-grant-table方式登录
        vim /etc/my.cnf 在[mysqld]节点下添加一行: skip-grant-tables
    2. service mysqld stop
    3. service mysqld start
    4. mysql -u root [敲回车进入]
    5. 修改root的登录密码
      (语法: ALTER USER 'user'@'host' IDENTIFIED BY 'NewPassword'; --usert和host由'select user,host from user;'查询可得)
        ALTER USER 'root'@'localhost' IDENTIFIED BY 'Gujiayuan@0412';
       或 ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Gujiayuan@0412';
        --mysql8.0以上密码策略限制必须要大小写加数字特殊符号            
    6. 允许root的远程登录[可选]
       mysql.user表中的host字段为'localhost'时表示对应的用户只能使用本地登录,
       mysql.user表中的host字段为'%'时表示对应的用户可以进行远程登录.
       当允许某用户使用远程登录时,需要将 mysql.user表中对应用户的host字段设置为'%':
        update user set host='%' where user='root';
        flush privileges;  --flush privileges,刷新数据库.否则会保留在缓存中
    7. 删除步骤1中添加的skip-grant-table配置.
    8. 重启MySQL: service mysqld restart
        
        
--查询有哪些数据库:
    SHOW databases; 
--使用指定的数据库:
    USE databaseName; //eg: USE mysql;
-- 创建数据库并指定字符集和校对规则:
    CREATE DATABASE `db_name` DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
-- 删除数据库:
    drop DATABASE `db_name`;
    
--查看数据库中有哪些表:
    SHOW tables;
--查看表结构:
    DESC tablename; //或SHOW COLUMNS FROM tablename;
    
--创建用户(username):
    CREATE USER username IDNETIFIED BY "gujiayuan"; 
--删除用户(username):
    DROP USER username;
--查询用户(用户信息存储在mysql数据库中的user表中):
    SELECT user FROM mysql.user;
--用户权限查询:
    SHOW GRANTS FOR username;
--设置用户权限(在指定的数据库databaseName上拥有除GRANT OPTION外的所有权限):
    GRANT ALL ON databaseName.* TO username;
--撤销用户权限(撤销用户在指定的数据库databaseName上的SELECT权限)
    REVOKE SELECT ON databaseName.* FROM username;

-- 查看字符集编码设置
　　mysql> show variables like "%character%";

-- 设置字符集编码
　　mysql> set names "utf8";
    --相当于同时：
    　　set character_set_client = utf8;
    　　set character_set_results = utf8;
    　　set character_set_connection = utf8;
--修改数据库字符集:
　　mysql> alter database database_name character set xxx; 
-- 只修改库的字符集，影响后续创建的表的默认定义；对于已创建的表的字符集不受影响。（一般在数据库实现字符集即可，表和列都默认采用数据库的字符集）

--查看数据库支持的所有校对规则:
    SHOW collation;  

--查看当前字符集和校对规则设置:
    SHOW variables LIKE "collation_%";  

--查看当前登录用户:
    SELECT user();
--当前正在使用的数据库:
    SELECT database();

问题:
    1.要使用远程访问: 需要注释掉配置文件中的地址绑定(/etc/mysql/mysql.conf.d/mysqlc.cnf):
        #bind-address           = 127.0.0.1
        并设置用户的远程访问权限; 
        mysql8.0以前的版本可以使用grant在授权的时候隐式的创建用户; mysql8.0以后所以必须先创建用户,然后再授权
        # mysql8.0之前版本
        GRANT ALL PRIVILEGES ON *.* TO 'user_name'@'%' IDENTIFIED BY 'password';
        # mysql8.0及之后版本
        CREATE USER 'user_name'@'%' IDENTIFIED BY 'password';
        GRANT ALL PRIVILEGES ON *.* TO 'user_name'@'%';        
    
    
SELECT prod_name
FROM products
WHERE prod_name LIKE "%1000"
ORDER BY prod_name;

SELECT prod_name
FROM products
WHERE prod_name REGEXP "[123] ton"
ORDER BY prod_name;

SELECT prod_name
FROM products
WHERE prod_name REGEXP "[0-9]{4}"  -- [[:digit:]]{4}
ORDER BY prod_name;


SELECT prod_name
FROM products
WHERE prod_name REGEXP "[[:<:]]s"
ORDER BY prod_name;


SELECT 2*3;
SELECT UPPER("abc");
SELECT NOW();

SELECT vend_name, LENGTH(vend_name) AS vend_name_upcase
FROM vendors
ORDER BY vend_name;


UPDATE orders
SET order_date = "2005-09-01 08:08:08"
WHERE order_num = 20005;	

SELECT * FROM orders;

SELECT cust_id, order_num
FROM orders
WHERE DATE(order_date) = "2005-09-01";

SELECT cust_id, order_num
FROM orders
WHERE DATE(order_date) BETWEEN "2005-09-01" AND "2005-09-30";

SELECT cust_id, order_num
FROM orders
WHERE YEAR(order_date)="2005" AND MONTH(order_date)="09";

SELECT CONCAT(RTRIM(vend_name), "(", RTRIM(vend_country), ")") AS vend_title
FROM vendors
ORDER BY vend_name;


-- AVG()必须以类型为数值的列为参数
SELECT AVG(prod_price) AS avg_price
FROM products;


-- COUNT(*)对表的行的数目进行计数，不管表列中包含的是否为空值(NULL)
SELECT COUNT(*) AS num_cust
FROM customers;

-- COUNT(column)对特定列中具有值的行进行计数,忽略NULL值
SELECT COUNT(cust_email) AS num_cust
FROM customers;

-- DISTINCT表示只考虑不同的列(不重复算相同的列)
SELECT AVG(DISTINCT prod_price) AS avg_price
FROM products;



-- 分组：把数据分为多个逻辑组, 以便能对每个组进行聚集计算
SELECT vend_id, COUNT(*) AS num_prods
FROM products
GROUP BY vend_id ; 

-- 检索出至少有2个订单的顾客
SELECT cust_id, COUNT(*) AS orders
FROM orders
GROUP BY cust_id -- 将orders表按cust_id分组
	HAVING COUNT(*)>=2; -- 过滤分组：选出行数不小于2的分组


-- 检索出具有2个以上、价格不低于10的产品的供应商
SELECT vend_id, COUNT(*) AS num_prods
FROM products
WHERE prod_price>=10
GROUP BY vend_id
HAVING COUNT(*)>=2;

-- 检索总计价格大于等于50的订单的订单号与总计订单价格, --输出结果按总价格排序
SELECT order_num, SUM(quantity*item_price) AS ordertotal
FROM orderitems
GROUP BY order_num
HAVING SUM(quantity*item_price)>=50
ORDER BY ordertotal;



-- 检索出订购物品"TNT2"的所有客户-- 法一:用联结的方式
SELECT customers.cust_id, cust_contact
FROM customers, orders, orderitems
WHERE customers.cust_id=orders.cust_id
	AND orders.order_num=orderitems.order_num
	AND orderitems.prod_id="TNT2";

-- 法二:用子查询的方式(子查询的效率低于联结)
SELECT cust_id, cust_contact
FROM customers
WHERE cust_id IN(SELECT cust_id 
		 FROM orders
		 WHERE order_num IN(SELECT order_num
				    FROM orderitems
				    WHERE prod_id="TNT2"));

-- 计算每个客户的订单数
-- 法一
SELECT  cust_name, 
	cust_state,
	(SELECT COUNT(*) FROM orders WHERE orders.cust_id=customers.cust_id) AS orders
FROM customers

-- 法二
SELECT customers.cust_id, COUNT(order_num) 
FROM customers, orders
WHERE customers.cust_id=orders.cust_id
GROUP BY customers.cust_id;


-- *********联结*****************
SELECT vend_name, prod_name, prod_price
FROM vendors, products
WHERE vendors.vend_id=products.vend_id	-- 使用完全限定名。因为两个表有同名的字段,为了防止二义性
ORDER BY vend_name, prod_name;

-- 别名
SELECT CONCAT(RTRIM(vend_name), "(", RTRIM(vend_country), ")") -- 将查询到的结果按指定格式连接起来
AS vend_title	-- 指定select返回结果的列别名
FROM vendors
ORDER BY vend_name;


SELECT cust_name, cust_contact
FROM customers AS c, orders AS o, orderitems AS oi	-- 指定表别名
WHERE c.cust_id=o.cust_id
	AND oi.order_num=o.order_num
	AND oi.prod_id="TNT2";

-- 查询生产DTNTR的供应商所生产的所有物品
-- 法一:使用一个子查询实现
SELECT prod_id, prod_name
FROM products
WHERE vend_id=(SELECT vend_id
		FROM products
		WHERE prod_id = "DTNTR");

-- 法二: 自结联。通过对同一个表指定两个别名，来进行结联查询。(可以当作在两个表上进行查询)		
SELECT p1.prod_id, p1.prod_name
FROM products AS p1, products AS p2
WHERE p1.vend_id=p2.vend_id
	AND p2.prod_id = "DTNTR";


-- 自然联结: 显示的指定返回不重复的列
SELECT c.*, o.order_num, o.order_date,
	oi.prod_id, oi.quantity, oi.item_price
FROM customers AS c, orders AS o, orderitems AS oi
WHERE c.cust_id=o.cust_id
	AND oi.order_num=o.order_num
	AND oi.prod_id="FB";

-- 内部结联:查询客户及其定单,只能检索出有订单的客户
SELECT customers.cust_id, orders.order_num
FROM customers LEFT OUTER JOIN orders   -- LEFT表示:结果迄今OUTER JOIN左边的表的所有行,即使不满足ON条件。同理也有RIGHT
	       ON customers.cust_id=orders.cust_id;
	
-- 外部结联:查询所有客户,包括那些没有订单的客户
SELECT customers.cust_id, orders.order_num
FROM customers LEFT JOIN orders
	       ON customers.cust_id=orders.cust_id;

-- 检索所有客户及每个客户所下的订单数(不检索没有订单的客户)
SELECT  customers.cust_name, customers.cust_id, 
	COUNT(orders.order_num) AS num_ord	-- 按分组计算订单数
FROM customers INNER JOIN orders ON customers.cust_id=orders.cust_id	-- 过滤客户
GROUP BY customers.cust_id; -- 因为要计算每个客户的订单数,所以需要按客户分组

-- 检索所有客户及每个客户所下的订单数(即使没有订单的客户也检索出来)
SELECT  customers.cust_name, customers.cust_id, 
	COUNT(orders.order_num) AS num_ord	-- 按分组计算订单数
FROM customers LEFT OUTER JOIN orders ON customers.cust_id=orders.cust_id	-- 过滤客户
GROUP BY customers.cust_id; -- 因为要计算每个客户的订单数,所以需要按客户分组	
	
	

--  组合查询
SELECT vend_id, prod_id, prod_price
FROM products 
WHERE prod_price<=5
UNION   -- 将两个select查询的结果组合起来.UNION ALL不合并重复行
SELECT vend_id, prod_id, prod_price
FROM products
WHERE vend_id IN(1001, 1002)
ORDER BY vend_id, prod_price; -- 在组合查询中只能使用一条ORDER BY子句,且必须出现在最后一个select之后


-- 使用where条件实现与上面组合查询一样的功能
SELECT vend_id, prod_id, prod_price
FROM products 
WHERE prod_price<=5
      OR vend_id IN(1001, 1002);




-- 向表customers中插入多条记录
INSERT INTO customers( cust_name, cust_address, cust_city, cust_state, cust_zip, cust_country)
VALUES("Coyote Inc.", "200 Maple Lane", "Detroit", "MI", "44444", "USA"),
      ("Mouse House", "333 Fromage Lane", "Columbus", "OH", "43333", "USA");

-- 向customers表中插入从custnew表检索出的数据
INSERT INTO customers( cust_name, cust_address, cust_city, cust_state, cust_zip, cust_country)
SELECT custnew_name, custnew_address, custnew_city, custnew_state, custnew_zip, custnew_country
FROM custnew;



-- 更新(修改)数据
-- 将customers表中cust_id为1005的记录的和cust_email列修改为指定值
UPDATE customers
SET cust_name = "The Fudds",
    cust_email = "elmer@fudd.com"
WHERE cust_id = 1005;	-- WHERE很重要,当没有指定WHERE条件时会更新整个表相关列

-- 删除数据(记录)
-- 删除customers表中cust_id为1005的记录
DELETE FROM customers
WHERE cust_id = 1005; -- WHERE很重要,当没有指定WHERE条件时会删除表的所有记录

-- 快速的删除表的所有行(记录)
TRUNCATE TABLE tablename;



-- ************************************* --
-- 创建表
CREATE TABLE customers(
	cust_id      INT       NOT NULL AUTO_INCREMENT,  -- 自动增量
	cust_name    CHAR(50)  NOT NULL ,
	cust_address CHAR(50)  NULL ,
	cust_city    CHAR(50)  NULL ,
	cust_state   CHAR(5)   NULL ,
	cust_zip     CHAR(10)  NULL ,
	cust_country CHAR(50)  NULL ,
	cust_contact CHAR(50)  NULL ,
	cust_email   CHAR(255) NULL ,
	PRIMARY KEY (cust_id) -- 指定主键
	) ENGINE=INNODB; -- 

CREATE TABLE orders(
	  order_num  INT      NOT NULL AUTO_INCREMENT,
	  order_date DATETIME NOT NULL ,
	  cust_id    INT      NOT NULL ,
	  PRIMARY KEY (order_num)
	) ENGINE=INNODB;

CREATE TABLE orderitems(
	  order_num  INT          NOT NULL ,
	  order_item INT          NOT NULL ,
	  prod_id    CHAR(10)     NOT NULL ,
	  quantity   INT          NOT NULL DEFAULT 1, -- 默认值为1
	  item_price DECIMAL(8,2) NOT NULL ,
	  PRIMARY KEY (order_num, order_item) -- 指定主键,主键由多个列组成
	) ENGINE=INNODB;

-- 定义外键
ALTER TABLE orderitems ADD CONSTRAINT fk_orderitems_orders FOREIGN KEY (order_num) REFERENCES orders (order_num);
ALTER TABLE orders ADD CONSTRAINT fk_orders_customers FOREIGN KEY (cust_id) REFERENCES customers (cust_id);
ALTER TABLE products ADD CONSTRAINT fk_products_vendors FOREIGN KEY (vend_id) REFERENCES vendors (vend_id);

-- 如何获取自动增量的值
SELECT LAST_INSERT_ID();  -- 返回最后一个AUTO_INCREMENT的值

-- 更改表: 注意,在表的设计过程中要考虑成熟,不要指望后期更改
ALTER TABLE customers ADD customers_phone CHAR(20); -- 给表customers添加一个列
ALTER TABLE customers DROP customers_phone; -- 删除刚添加的列

DROP TABLE tablename; -- 删除表

-- 重命名表（或多个表）
RENAME TABLE old_tablename TO new_tablename,
	     old_tablename1 TO new_tablename1;


-- ************************************* --
-- 一个多表查询的例子: 查询购买了某种物品的客户
SELECT cust_name, cust_contact 
FROM customers, orders, orderitems
WHERE customers.cust_id=orders.cust_id 
	AND orders.order_num=orderitems.order_num
	AND orderitems.prod_id="TNT2";

CREATE VIEW ...; -- 创建视图
SHOW CREATE VIEW viewname; -- 查看创建视图的语句 
DROP VIEW viewname;  -- 删除视图
CREATE OR REPLACE VIEW ...;  -- 更新视图。 与选drop 再 create效果一样 

--  建立一个视图，它返回订购了任意产品的所有客户的列表。(利用视图简化联结)
CREATE VIEW productcustomers AS
SELECT cust_name, cust_contact, prod_id
FROM customers, orders, orderitems
WHERE customers.cust_id=orders.cust_id
	AND orderitems.order_num=orders.order_num;

SELECT * FROM productcustomers;
SELECT cust_name, cust_contact FROM productcustomers WHERE prod_id="TNT2"; -- 从视图中检索特定的数据

-- 用视图重新格式化检索出的数据
CREATE VIEW vendorlocations AS
SELECT CONCAT(RTRIM(vend_name),"(", RTRIM(vend_country), ")")
	AS vend_title
FROM vendors
ORDER BY vend_name;

SELECT * FROM vendorlocations;



-- ************************************* --
-- 因为mysql语句分隔符和mysql命令行工具的分隔符都是";"。 为了不出错需要临时更改命令行实用工具的分隔符
DELIMITER // 
-- 定义存储过程
-- 参数: onumber: 订单号
--       taxable: 0不带税, 1带税
--       ototal: 订单总额
CREATE PROCEDURE ordertotal(IN onumber INT, 
			    IN taxable BOOLEAN, 
			    OUT ototal DECIMAL(8,2)) COMMENT "Obtain order total, optionally adding tax"
BEGIN
	DECLARE total DECIMAL(8,2);
	DECLARE taxrate INT DEFAULT 6;
	
	SELECT SUM(item_price*quantity) FROM orderitems WHERE order_num=onumber INTO total;
	
	IF taxable THEN
		SELECT total+(total/100*taxrate) INTO total;
	END IF;
	
	SELECT total INTO ototal;
END//
DELIMITER ;  -- 将命令行实用工具的分隔符还原为";"


CALL ordertotal(20005, 0, @total); -- 调用ordertotal存储过程
SELECT @total; -- 显示调用结果
CALL ordertotal(20005, 1, @total); -- 调用ordertotal存储过程
SELECT @total; -- 显示调用结果
SHOW PROCEDURE STATUS; -- 显示存储过程的详细信息
SHOW CREATE PROCEDURE ordertotal; -- 显示存储过程是如何创建的
-- 删除存储过程
DROP PROCEDURE processorders;



-- ***************************************** --
DELIMITER //
-- 在存储过程中使用游标
CREATE PROCEDURE processorders()
BEGIN
	DECLARE done BOOLEAN DEFAULT 0;
	DECLARE o INT;
	DECLARE t DECIMAL(8,2);
	
	-- 创建游标
	DECLARE ordernumbers CURSOR FOR	SELECT order_num FROM orders;
	
	-- 当SQLSTATE "02000"出现时将SET done=1。SQLSTATE "02000"是一个未找到条件，当REPEAT由于没有更多的行供循环时就产生这个条件
	DECLARE CONTINUE HANDLER FOR SQLSTATE "02000" SET done=1;
	
	--  当ordertotals表不存在时创建它
	CREATE TABLE IF NOT EXISTS ordertotals(order_num INT, total DECIMAL(8,2));

	OPEN ordernumbers; -- 打开游标
	
	-- 重复执行，直到done变为真.
	REPEAT
		FETCH ordernumbers INTO o;  
		CALL ordertotal(o, 0, t);
		INSERT INTO ordertotals(order_num, total) VALUES(o, t);
	UNTIL done END REPEAT;

	CLOSE ordernumbers; --  关闭游标
END//
DELIMITER ;

CALL processorders();



-- 创建表
CREATE TABLE customers(
	cust_id      INT       NOT NULL AUTO_INCREMENT,  -- 自动增量
	cust_name    CHAR(50)  NOT NULL ,
	cust_address CHAR(50)  NULL ,
	cust_city    CHAR(50)  NULL ,
	cust_state   CHAR(5)   NULL ,
	cust_zip     CHAR(10)  NULL ,
	cust_country CHAR(50)  NULL ,
	cust_contact CHAR(50)  NULL ,
	cust_email   CHAR(255) NULL ,
	PRIMARY KEY (cust_id) -- 指定主键
	) ENGINE=INNODB; -- 

-- sql数据类型
http://www.w3school.com.cn/sql/sql_datatypes.asp


-- 向表customers中插入多条记录
INSERT INTO customers( cust_name, cust_address, cust_city, cust_state, cust_zip, cust_country)
VALUES("Coyote Inc.", "200 Maple Lane", "Detroit", "MI", "44444", "USA"),
      ("Mouse House", "333 Fromage Lane", "Columbus", "OH", "43333", "USA");
# oracle跨库查询
[Oracle 跨库查询表数据](https://www.cnblogs.com/jijm123/p/12457914.html)
[Oracle dblink](https://www.cnblogs.com/sumsen/archive/2013/03/04/2943471.html)

## 场景
需要从A库去访问B库中的数据；

## 解决方案
在oracle中建立DBLINK后, 便可以通过DBLINK在当前数据库取访问另一个数据库了.
    

1. DBLINK创建方法
```
create database link <DBLINK-NAME>
  connect to <USER-NAME> identified by <PASSWORD>
  using '(DESCRIPTION =                       
    (ADDRESS_LIST =
      (ADDRESS = (PROTOCOL = TCP)(HOST = <域名>)(PORT = <端口号>))
    )
    (CONNECT_DATA =
      (SERVICE_NAME = <数据库实例名称>)
    )
  )';
```
2. 通过DBLINK访问另一个数据库的表
`select * from <TableName>@<DBLINK-NAME>`



eg:
```
-- Create database link
create database link DBLINK_B --自定义要连接的数据库名称
  connect to username identified by password  --用户名和密码
  using '(DESCRIPTION =                      --数据库连接：域名，端口号，数据库实例   
    (ADDRESS_LIST =
      (ADDRESS = (PROTOCOL = TCP)(HOST = 127.0.0.1)(PORT = 1521))
    )
    (CONNECT_DATA =
      (SERVICE_NAME = orcl)
    )
  )';　
  
select * from B_TABLE@DBLINK_B;
```
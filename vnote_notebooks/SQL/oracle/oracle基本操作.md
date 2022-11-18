# oracle基本操作

## Oracle创建表空间、创建用户的完整过程
1. 创建表空间; 指定物理文件路径, 初始大小, 每次递增大小, 最大大小
`CREATE TABLESPACE "MyTableSpace" DATAFILE 'D:/app/GJY/oradata/orcl/mydata01.dbf' SIZE 2G AUTOEXTEND ON NEXT 50M MAXSIZE 20G;`
Note: DATAFILE一定要使用单引号，否则会出现"ORA-00972: identifier is too long"的错误。

 2. 创建用户
`CREATE USER MyUserName IDENTIFIED BY skd_test0123 DEFAULT TABLESPACE "MyTableSpace";`
Note: 用户名不要加引号，否则引号也将成为名称的一部分.

3. 用户授权
```
GRANT CONNECT TO MyUserName;  -- with admin option
GRANT RESOURCE TO MyUserName;
GRANT DS_Role TO MyUserName;
GRANT CREATE VIEW TO MyUserName;
GRANT CREATE TYPE TO MyUserName;
```

## 其它查询语句

```
-- 查询所有表空间物理位置
SELECT name FROM v$datafile;

-- 查询当前用户的表空间
SELECT username, default_tablespace FROM user_users;

-- 修改用户的默认表空间
ALTER USER {user_name} DEFAULT TABLESPACE {new_table_space_name}; 

-- 查询所有的表空间
select * from user_tablespaces; 

-- 查询表空间下的所有表
select table_name from all_tables where TABLESPACE_NAME='{new_table_space_name}';
  
-- 删除表空间
ALTER TABLESPACE {table_space_name} OFFLINE;
DROP TABLESPACE {table_space_name} INCLUDING CONTENTS AND DATAFILES;
  
-- 为表空间ZCGL增加数据文件
ALTER TABLESPACE ZCGL ADD DATAFILE 'D:/app/GJY/oradata/orcl/mydatat02.dbf' SIZE 2G AUTOEXTEND ON NEXT 5M MAXSIZE UNLIMITED;

-- 查询Oracle的服务名称和实例（SID）名称
select value serviceName from v$parameter where name = 'service_names';
select value instanceName from v$parameter where name = 'instance_name';

```

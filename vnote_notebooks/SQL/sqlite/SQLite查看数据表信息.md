# SQLite查看数据表信息
1. 查看当前数据库中所有表 `.tables`
2. 查看当前数据库所有表(或指定表)的建表语句 `.schema [表名]`
3. 查看所有表结构及索引信息 `select * from sqlite sqlite_master`
4. 查看所有表(或指定表)的结构信息
```
select * from sqlite_master where type='table' [and name='表名']；
pragma table_info 表名  -- 查看指定表所有字段信息，类型于mysql: desc table_name
```
5. 查看所有表(或指定表)的索引信息 `select * from sqlit_master where type='index' [and name='表名']`
6. 查看指定表字段[column]类型 `select typeof('列名') from 表名`
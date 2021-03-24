# OSS(阿里云存储服务OpenStorageService)
[OSS API: https://help.aliyun.com/document_detail/32027.html](https://help.aliyun.com/document_detail/32027.html)

# 基本概念
## Object
在OSS中，用户操作的基本数据单元是Object。
Object包含key、meta和data。
    key是Object的名字；
    meta是用户对该object的描述,由一系列name-value对组成；
    data是Object的数据。

## Bucket
Bucket是OSS上的命名空间,是计费、权限控制、日志记录等高级功能的管理实体；
Bucket名称在整个OSS服务中具有全局唯一性，且不能修改；
存储在OSS上的每个Object必须都包含在某个Bucket中。
一个应用，可以对应一个或多个Bucket。
一个用户最多可创建10个Bucket，但每个Bucket中存放的Object的数量和大小总和没有限制，用户不需要考虑数据的可扩展性。


# OSS功能简介



存储空间（Bucket）是存储对象（Object）的容器。对象都隶属于存储空间.




# rabbitmq命令

## 启动命令
[参考](https://blog.csdn.net/qq_20143059/article/details/105433201)

1. 以应用方式启动
后台启动： rabbitmq-server -detached 
直接启动： rabbitmq-server 
关闭： rabbitmqctl stop


2. 以服务方式启动（安装完之后在任务管理器中服务一栏能看到RabbtiMq）
安装服务: rabbitmq-service install 
开始服务: rabbitmq-service start 
停止服务: rabbitmq-service stop  
使服务有效: rabbitmq-service enable 
使服务无效: rabbitmq-service disable 
帮助: rabbitmq-service help 

3. Rabbitmq节点管理方式
rabbitmqctl
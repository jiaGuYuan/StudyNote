#########################################################################
# File Name: startOrStopRedisServer.sh
# Author: GJY
# function: 后台运行redis-server
# Created Time: 2020年04月02日 星期四 14时46分02秒
#########################################################################
#!/bin/bash

logpath=/root/workspace/logs/redis-server.log
if [ -z $1 ]; then
    echo "**** 用法: startOrStopRedisServer.sh (start|stop) ****"  
    exit 0
fi

ps -ef | grep redis-server | grep -v grep >/dev/null 
if [ $? -eq 0 ];then
    echo "**** 当前已有redis-server在运行 ****"
    if [ "$1" -nq "stop" ]; then
        exit 0
    fi
fi

case $1 in
    "start"){
        echo "****** nohup 启动 redis-server ******"
        nohup redis-server >${logpath} 2>&1 &
        echo "后台运行redis-server; 日志路径:${logpath}"
        echo ""
    };;
    "stop"){
        echo "****** 停止 redis-server ******"
        ps -ef | grep redis-server | grep -v grep | awk '{print $2}' | xargs kill >/dev/null 2>&1 &
        echo "******* redis-server已停止 ***********"
        echo ""
    };;
    *){
        echo "**** 用法: startOrStopRedisServer.sh (start|stop) ****"  
    };;
esac

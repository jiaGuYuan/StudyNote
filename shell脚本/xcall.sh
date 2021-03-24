#!/bin/bash

# 集群整体操作脚本. 使用方法: xcall.sh shellcmd [; shellcmd2 ;...]
# eg: xcall.sh jps

# 获取输入参数个数,如果没有参数,直接退出
pcount=$#
if((pcount==0)); then
    echo no args;
    exit;
fi


# 在各集群主机上执行命令
user=$(whoami)
hosts=('192.168.10.101' '192.168.10.102' '192.168.10.103')
for((i=0; i<${#hosts[@]}; i++)); do
    echo --------- ${i} ----------
    ssh ${i} "source /etc/profile ; $*"
done

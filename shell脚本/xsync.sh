#!/bin/bash

# 集群分发脚本. 使用方法: xsync.sh fileName 

#1 获取输入参数个数,如果没有参数,直接退出
pcount=$#
if((pcount==0)); then
    echo no args;
    exit;
fi

#2 获取文件名称
p1=$1
fname=$(basename $p1)
echo fname=$fname

#3 获取上级目录的绝对路径
pdir=$(cd -P $(dirname $p1); pwd)
echo pdir=$pdir

#4 获取当前用户名称, 这里假设进行分发的主机名与分发到的主机名相同
user=$(whoami)
hosts=('192.168.10.101' '192.168.10.102' '192.168.10.103') # 要分发到的主机IP
for((i=0; i<${#hosts[@]}; i++)); do
    echo "$pdir/$fname $user@${hosts[${i}]}:$pdir"
    rsync -rvl $pdir/$fname $user@${hosts[${i}]}:$pdir
done

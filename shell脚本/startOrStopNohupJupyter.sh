#########################################################################
# File Name: nohupJupyter.sh
# Author: GJY
# function: 后台运行jupyter or 关闭jupyter
# Created Time: 2020年04月02日 星期四 14时46分02秒
# 用法: startOrStopNohupJupyter.sh [start|stop] [JupyterDir_WK_DIR]
#########################################################################
#!/bin/bash

logpath=/home/gjy/wks/jupyterWKS/jupyter.log

if [ -z $1 ]; then
    echo "**** 用法: startOrStopNohupJupyter.sh (start JupyterDir|stop) ****"  
    ps -ef | grep jupyter-notebook | grep -v grep >/dev/null 
    if [ $? -eq 0 ];then
        echo "**** 当前已有jupyter在运行 ****"
        head -n 25 ${logpath} | grep -E "NotebookApp.*http"
        echo ""
    fi
    exit 0
fi

if [ -z $2 ]; then
    JupyterDir='/root/toutiao_project/'
else
    JupyterDir=$2
fi

# 检查jupyter是否在运行
ps -ef | grep jupyter-notebook | grep -v grep >/dev/null 
if [ $? -ne 0 ];then #jupyter未在运行
    case $1 in
    "start"){
        echo "****** nohup 启动 jupyter ******"
        nohup jupyter notebook --notebook-dir=${JupyterDir} --allow-root --ip=120.79.101.81 >${logpath} 2>&1 &
        sleep 7
        echo "***** jupyter已启动: ${JupyterDir} *****"
        head -n 25 ${logpath} | grep -E "NotebookApp.*http"
    };;
    *){
        echo "**** 当前没有jupyter在运行  ****"
        echo ""
    };;
    esac
else
    case $1 in
    "stop"){
        echo "****** 停止 nohup jupyter ******"
        ps -ef | grep jupyter-notebook | grep -v grep | awk '{print $2}' | xargs kill >/dev/null 2>&1 &
        echo "******* jupyter已停止 ***********"
    };;
    *){
        echo "**** 当前已有jupyter在运行 ****"
        head -n 25 ${logpath} | grep -E "NotebookApp.*http"
        echo ""
    }
    esac
fi


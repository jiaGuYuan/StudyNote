#!/bin/bash

# 配置定时任务
# cat /etc/crontab
# 每小时执行一次
# 0 */1 * * * root sh /beat_tasks/clean_dir.sh > /tmp/corntab.log 2>&1 &

funcRmExpiredFile(){
    work_path=$1  # 以绝对路径提供操作目录
    if [ ${work_path:0:1} != "/" ]; then
       echo [$(date +"%Y-%m-%d %H:%M:%S")]: "参数错误'${work_path}'; 请以绝对路径指定工作目录"
       return 1
    elif [ ${#work_path} -lt 2 ]; then
        echo [$(date +"%Y-%m-%d %H:%M:%S")]: "危险操作, 请不要删除根目录'${work_path}'"
        return 1
    fi
    
    cd ${work_path} || return 1  # 确保进入到正确的目录
    echo [$(date +"%Y-%m-%d %H:%M:%S")]: "work dir ${work_path}, delete expired file"
    
    # 获取指定目录下, 最后修改时间在N分钟之前(-mmin +N)的所有文件 (目录: -type d) 
    sub_dir_list=$(find ${work_path}/* -maxdepth 0 -mmin +120)
    for sub_dir in ${sub_dir_list}
    do
        echo [$(date +"%Y-%m-%d %H:%M:%S")]: "delete dir ${sub_dir}"
        rm -rf ${sub_dir}  # 删除目录
    done
    return 0
}

# 遍历匹配的子目录
for tmp_path in $(ls /data/aaa-*-bbb* -d)
do
    funcRmSubDir "${tmp_path}/task_data_interface"
    funcRmSubDir "${tmp_path}/logs"
done


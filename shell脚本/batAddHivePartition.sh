#########################################################################
# File Name: batAddHivePartition.sh
# Author: GJY
# Function: 在Hive已经关联外部表的情况下,批量添加分区(关联分区)
# Created Time: 2020年03月31日 星期二 21时30分41秒
#########################################################################
#!/bin/bash

# 需要根据情况适配这几个变量
tableDir='/user/hive/warehouse/profile.db/user_action/'
dbname=profile
tableName=user_action

if [ "$1" == "run" ]; then
    for path in `hdfs dfs -ls ${tableDir} | awk '{print $8}'`
    do
        partitionValue=${path:0-10:10} # 需要根据情况适配"2020-03-31"

        echo "***********添加分区: ${partitionValue}***********"
        hive --database ${dbname} -e "alter table ${tableName} add PARTITION(dt='${partitionValue}') location '$path'"
    done
else
    echo "危险脚本,请带run参数执行: batAddHivePartition.sh run"
fi


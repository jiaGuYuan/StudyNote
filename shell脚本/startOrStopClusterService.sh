#! /bin/bash

case $1 in
"start"){
    echo "******** 启动 集群 ********"
	# 同步时间
	ssh hadoop-slave2 ntpdate 0.cn.pool.ntp.org
	ssh hadoop-slave1 ntpdate 0.cn.pool.ntp.org
    ntpdate 0.cn.pool.ntp.org


	# 启动在master上的 docker mysql
	echo "************* 启动 mysql(mysql部署在docker中) *************"
	systemctl restart docker
    docker start mysql
    echo "************* 已启动docker mysql *************"""

    #启动 HDFS 集群
    echo "******** 启动 HDFS 集群 ********"
    start-dfs.sh 
    
    #启动 Yarn 集群
    echo "******** 启动 YARN 集群 *******"
    start-yarn.sh

    #启动 HiveMetastore
    echo "******** 启动 HiveMetastore ********"
    nohup hive --service metastore 1>/dev/null 2>&1 &

    #启动 HBase 集群
    echo "******** 启动 HBase 集群 ********"
    start-hbase.sh
	#使用happybase开发hbase,需要开启thriftserver
    hbase-daemon.sh start thrift


    #启动 Spark 集群
    echo "******** 启动 Spark 集群 ********"
    start-spark.sh

	echo "************* 集群启动完毕 **************"
};;

"stop"){
    echo "******** 停止 集群 *********"

    #停止 Spark 集群
    echo "******** 停止 Spark 集群 ********"
    stop-spark.sh

    #停止 HBase 集群
    hbase-daemon.sh stop thrift #先停止thriftserver
    echo "******** 停止 HBase 集群 ********"
    stop-hbase.sh

    #停止 HiveMetastore
    echo "******** 停止 HiveMetastore ********"
    ps -ef | grep hive | grep -v grep| awk '{print $2}' | xargs kill >/dev/null 2>&1 &    

    #停止 YARN 集群
    echo "******** 停止 YARN 集群*******"
    stop-yarn.sh
    
    #停止 HDFS 集群
    echo "******** 停止 HDFS 集群 *******"
    stop-dfs.sh 
    
    
    # 停止master中的docker mysql
    echo "************* 停止 mysql(mysql部署在master的docker中) *************"
    docker stop mysql
    systemctl stop docker
	echo "************* 已停止docker mysql ***************"""

	echo "************* 集群停止完毕 **************"
};;

*){
    echo "******** 启动集群 && 停止集群 *********"
    echo "使用方法: startOrStopClusterService.sh(start or stop)"
	echo ""
};;
esac

##### 安装  zookeeper-3.4.5-cdh5.16.2 #####

mkdir -p /export/programdata/zookeeper


server.1=node02:2888:3888
server.2=node03:2888:3888
server.3=node04:2888:3888


cd /export/modules

for i in {2..3}
do
scp -r zookeeper node0$i:$PWD
ssh node0$i "echo $[$i-1] > /export/programdata/zookeeper/myid;"
done



for i in node0{2..4}
do
ssh $i "echo 'export ZOOKEEPER_HOME=/export/modules/zookeeper' >> /etc/profile; echo 'export PATH=:\${ZOOKEEPER_HOME}/bin:\$PATH' >> /etc/profile"
done



##### hadoop 完全分布  #####


            node01  node02  node03  node04
zookeeper     0       1       1       1
journalnode   0       1       1       1
namenode      1       1       0       0
datanode      1       1       1       1
rm            0       1       1       0
nm            1       1       1       1
historyserver 0       0       0       1
hregionserver 1       0       1       1
hmaster       1       0       0       0
sparkhistser  0       0       1       0
hbase thrift  0       0       0       1
kafka         1       1       1       1


JAVA_HOME
/export/modules/jdk

hadoop-env.sh 25
mapred-env.sh
yarn-env.sh



for i in node0{1..4}
do
ssh $i "echo 'export HADOOP_HOME=/export/modules/hadoop' >> /etc/profile; echo 'export PATH=:\${HADOOP_HOME}/bin:\${HADOOP_HOME}/sbin:\$PATH' >> /etc/profile"
done

yum -y install openssl-devel

hadoop checknative

################################# 启动 ##########################################

# 由于hbase元数据存储在这里，启动时候请注意
cd /export/programdata/zookeeper





export JAVA_HOME=/export/modules/jdk
export HADOOP_CONF_DIR=/export/modules/hadoop/etc/hadoop
export SPARK_DAEMON_JAVA_OPTS="-Dspark.deploy.recoveryMode=ZOOKEEPER -Dspark.deploy.zookeeper.url=node02:2181,node03:2181,node04:2181 -Dspark.deploy.zookeeper.dir=/spark_ha"
export SPARK_HISTORY_OPTS="-Dspark.history.fs.logDirectory=hdfs://ns/logs/spark -Dspark.history.fs.cleaner.enabled=true -Dspark.history.retainedApplications=10"
#export spark.hadoop.dfs.replication=1
export SPARK_DAEMON_MEMORY=512m
export SPARK_WORKER_OPTS="-Dspark.worker.cleanup.enabled=true -Dspark.worker.cleanup.appDataTtl=259200"















zkServer.sh start

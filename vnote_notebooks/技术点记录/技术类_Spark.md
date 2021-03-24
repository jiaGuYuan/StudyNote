
# 技术类_Spark

# django
## execute_from_command_line
 from django.core.management import execute_from_command_line
 


# pyspark
## jupyter spark测试环境
```
import os
os.environ['JAVA_HOME'] = 'D:/Software/JDK_8U92'
PYTHON_PATH = 'D:/Software/anaconda3/envs/wk_py37/python'
os.environ['PYSPARK_DRIVER_PYTHON'] = PYTHON_PATH
os.environ['PYSPARK_PYTHON'] = PYTHON_PATH


from pyspark.sql import SparkSession, functions as F, Window
from pyspark.sql.types import StructType, StructField, LongType, StringType, FloatType


spark = SparkSession.builder.appName("Python Spark SQL basic example").master('local[*]')\
    .getOrCreate()


##### 用于测试的DataFrame结构 ####
rdd1 = spark.sparkContext.parallelize([
        (1, "2020-02-02"),
        (2, "2020-02-02"),
        (3, "2020-02-03"), 
        (4, "2020-02-04")])
 
# 设置dataFrame将要使用的数据模型，定义列名，类型和是否为能为空
schema = StructType([StructField("order_id", LongType(), True),
                     StructField("order_time", StringType(), True)])
# 创建DataFrame
orders_df = spark.createDataFrame(rdd1, schema)

rdd2 = spark.sparkContext.parallelize([
        (1, 1, 1,  15.5, 5),
        (2, 1, 2,  22.0, 10),
        (3, 2, 2,  23.0, 15), 
        (4, 2, 3,  5.0, 20),
        (5, 2, 5,  6.9, 25)])
schema = StructType([StructField("id", LongType(), True),
                     StructField("order_id", StringType(), True),
                     StructField("product_id", LongType(), True),
                     StructField("price", FloatType(), True),
                     StructField("quantity", LongType(), True)])
order_product_df = spark.createDataFrame(rdd2, schema)

orders_df.collect()
order_product_df.collect()
orders_df.createOrReplaceTempView("orders")
order_product_df.createOrReplaceTempView("order_products")
```

## pyspark报错Exception
1. Java gateway process exited before sending its port number解决方法
解决办法:
    这里指定一下Java的环境就可以了，添加代码：
 ```
import os
os.environ['JAVA_HOME'] = 'D:\JavaJDK'  # 这里的路径为java的bin目录所在路径
```

## 以下代码的返回类型是什么??
```
# 通过Jdbc获取表数据. 
def get_df_from_jdbc(self, table):
	data = self.spark.read \
		.format("jdbc") \
		.option("url", self.url) \
		.option("dbtable", table) \
		.option("user", self.prop["user"]) \
		.option("password", self.prop["password"]) \
		.option("fetchsize", 200000) \
		.load()
	return data
```
==spark.read  ==> return: :class:`DataFrameReader`



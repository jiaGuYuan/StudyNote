
[spark_RDD操作.ipynb](./spark_jupyter/spark_RDD操作.ipynb)

# rdd操作

```python
%config ZMQInteractiveShell.ast_node_interactivity='all'
```

```python
%config ZMQInteractiveShell.ast_node_interactivity='all'
```

# Spark环境


```python
import os
os.environ['JAVA_HOME'] = 'D:/Software/JDK_8U92'
PYTHON_PATH = 'D:/Software/anaconda3/envs/wk_py37/python'
os.environ['PYSPARK_DRIVER_PYTHON'] = PYTHON_PATH
os.environ['PYSPARK_PYTHON'] = PYTHON_PATH

from pyspark.sql import SparkSession, functions as F, Window
from pyspark.sql.types import StructType, StructField, LongType, StringType, FloatType


spark = SparkSession.builder.appName("Python Spark SQL basic example").master('local[*]')\
    .getOrCreate()

sc = spark.sparkContext
```


```python

words = sc.parallelize(
    ["scala",
     "java",
     "hadoop",
     "spark",
     "akka",
     "spark vs hadoop",
     "pyspark",
     "pyspark and spark"
     ])
counts = words.count()
print(f"Number of elements in RDD -> {counts}")

```


```python
import os, sys
datafile_dir = 'D:/StudyNote/原来的一些学习笔记_GJY/StudyNote/vnote_notebooks/大数据/spark_学习/test_data/'
datafile1 = os.path.join(datafile_dir, 'data1.txt')

dataflie1 = sc.textFile(datafile1)

```


```python
dataflie1
dataflie1.collect()
print(dataflie1)

```


```python
data_rdd = sc.textFile(logfile_path) 
data_rdd.collect()
data_rdd.count()
```


```python
datafile_dir_pattern = os.path.join(datafile_dir, '*.txt') # 读取目录下匹配指定模式的文件
path_data_rdd = sc.wholeTextFiles(datafile_dir_pattern)
path_data_rdd.collect()
len(path_data_rdd.collect()[0])
```

## foreach


```python
words = sc.parallelize(
    ["scala",
     "java",
     "hadoop",
     "spark",
     "akka",
     "spark vs hadoop",
     "pyspark",
     "pyspark and spark"
     ])

def f_foreach(x):
    print(x)

words.foreach(f_foreach)
```

## rdd.filter(f)
只取出满足条件的元素, 返回一个新RDD


```python
words = sc.parallelize(
    ["scala",
     "java",
     "hadoop",
     "spark",
     "akka",
     "spark vs hadoop",
     "pyspark",
     "pyspark and spark"
     ])


rdd2 = words.filter(lambda x: 'spark' in x)
rdd2.collect()
```

## rdd.map(f)
一对一转换


```python
words = sc.parallelize(
    ["scala",
     "java",
     "hadoop",
     "spark",
     "akka",
     "spark vs hadoop",
     "pyspark",
     "pyspark and spark"
     ])


rdd2 = words.map(lambda x: (x, 1))
rdd2.collect()
```

## rdd.reduce(f)
使用指定的交换和结合二元运算符 减少这个RDD的元素。减小了本地分区.
与python的reduce一样


```python
from operator import add

nums = sc.parallelize([1, 2, 3, 4, 5])
adding = nums.reduce(add)
print(f"Adding all the elements -> {adding}")
```

## rdd.join(other, numPartitions=None)
返回一个RDD，包含在'self'和'other'中具有匹配键的所有对元素。<br/>
rdd,other的元素都被视作是二元的 k-v pair. <br/>
当rdd,other元素的长度超过2个时,元素第一个值被视为k,第二个值被视作v,其他值将被忽略.


```python
rdd1 = sc.parallelize([("spark", 1), ("hadoop", 4)])
rdd2 = sc.parallelize([("spark", 2), ("hadoop", 5)])
joined = rdd1.join(rdd2)
joined.collect()
```

## rdd.distinct()
去重


```python
intRDD = sc.parallelize([3,1,2,5,5])
intRDD.distinct().collect()

```

## rdd.randomSplit([0.2, 0.3, ...])
将整个集合以随机的方式按照比例分为多个RDD


```python
intRDD = sc.parallelize([3,1,2,5,5, 0, 1,2,3,4,6])
sRDD = intRDD.randomSplit([0.4,0.6])
len(sRDD)
sRDD[0].collect()
sRDD[1].collect()
```

## rdd.groupBy(f)
按照传入函数的规则，将数据分为多个Array


```python
intRDD = sc.parallelize([3,1,2,5,5, 0, 1,2,3,4,6])
result = intRDD.groupBy(lambda x : x % 2).collect() # 分成2个组
result


result2 = intRDD.groupBy(lambda x : x % 3).collect() # 分成3个组
result2
```

## rdd.union(other)
并集运算,但不会去重(效果像sql 的 union all)


```python
intRDD1 = sc.parallelize([3,1,2,5,5])
intRDD2 = sc.parallelize([5,6])
intRDD3 = sc.parallelize([2,7])
intRDD1.union(intRDD2).union(intRDD3).collect()
```

## rdd.intersection(other)
交集运算,结果中无重复元素


```python
rdd1 = sc.parallelize([1, 10, 2, 3, 4, 5])
rdd2 = sc.parallelize([1, 6, 2, 3, 7, 8])
rdd1.intersection(rdd2).collect()
```

## rdd.subtract(other) 
差集运算(返回在"rdd"中,但不包含在"other"中的每个元素)


```python
x = sc.parallelize([("a", 1), ("b", 4), ("b", 5), ("a", 3)])
y = sc.parallelize([("a", 3), ("c", None)])
x.subtract(y).collect()
```

## rdd.cartesian(other)
进行笛卡尔乘积运算


```python
x = sc.parallelize([("a", 1), ("b", 4), ("b", 5), ("a", 3)])
y = sc.parallelize([("a", 3), ("c", None)])
x.cartesian(y).collect()
```

## RDD基本动作运算


```python
intRDD = sc.parallelize([3,1,2,5,5, 0, 1,2,3,4,6])
#取第一条数据
intRDD.first()

#取前两条数据
intRDD.take(2)

# takeOrdered方法应该只在结果数组很小的情况下使用，因为所有的数据都被加载到驱动程序的内存中.
# rdd.takeOrdered(num, key=None) -- 按照key操作对元素的评分从小到大排序

#升序排列，并取前3条数据
intRDD.takeOrdered(3)

#降序排列，并取前3条数据
intRDD.takeOrdered(3, lambda x:-x)

```

## 统计功能
可以将RDD内的元素进行统计运算


```python
intRDD = sc.parallelize([3,1,2,5,5, 0, 1,2,3,4,6])

#统计  会返回一些常见的统计指标的值
intRDD.stats()

#最小值
intRDD.min()
#最大值
intRDD.max()
#标准差
intRDD.stdev()
#计数
intRDD.count()
#求和
intRDD.sum()
#平均
intRDD.mean()

```

# RDD Key-Value基本"转换"运算

sc.parallelize([(k, v), (k, v),...]) <br/>
我们用元素类型为tuple的数组初始化我们的RDD; 这里,每个tuple的第一个值将作为键,而第二个元素将作为值。<br/>
可以使用keys和values函数分别得到RDD的键数组和值数组


```python
kvRDD1 = sc.parallelize([(3,4), (3,6), (5,6), (1,2)])

kvRDD1.keys().collect()
kvRDD1.values().collect()

```


```python
# 使用filter函数筛选元素,可以按照键进行元素筛选，也可以通过值进行元素筛选.
kvRDD1 = sc.parallelize([(3,4), (3,6), (5,6), (1,2)])

# 按键进行筛选 得到一个新的RDD
kvRDD1.filter(lambda x:x[0] < 5).collect()

# 按值进行筛选 得到一个新的RDD
kvRDD1.filter(lambda x:x[1] < 5).collect()


# 对值进行处理得到一个新的RDD
kvRDD1.mapValues(lambda x:x**2).collect()
```

## kvRdd.sortByKey()对rdd按照key进行排序(rdd由(key, value)对组成)
```
kvRdd.sortByKey(
    ascending=True,
    numPartitions=None,
    keyfunc=?,
)
```
rdd由(key, value)对组成.
对这个RDD的key进行按照keyfunc函数进行评分,并对评分按照ascending指定的顺序进行排序.
未设置时keyfunc时,评分即为key.



```python
kvRDD1 = sc.parallelize([(3,4), (3,6), (5,6), (1,2)])
kvRDD1.sortByKey().collect() # 对key升序
kvRDD1.sortByKey(False).collect() # 对key降序

kvRDD1.sortByKey(ascending=True, keyfunc=lambda k: -k).collect() # 对-k升序

```

## kvRdd.reduceByKey()对具有相同key值的数据进行合并
```
kvRdd.reduceByKey(
    func,
    numPartitions=None,
    partitionFunc=?,
)
```


```python
from operator import add
rdd = sc.parallelize([("a", 1), ("b", 1), ("a", 1)])
rdd.reduceByKey(add).collect()
```

## join内连接运算
类似数据库的内连接

`kvRdd1.join(kvRdd2, numPartitions=None)` <br/>
返回一个新的RDD,其每个元素格式为(k,(v1, v2)).<br/>
其中(k, v1)在kvRdd1中, (k, v2)在kvRdd2中。


```python
kvRDD1 = sc.parallelize([(3,4),(3,6),(5,6),(1,2)])
kvRDD2 = sc.parallelize([(3,8), (3,1)])
kvRDD1.join(kvRDD2).collect()
```

## leftOuterJoin左外连接
类似数据库的左外连接
`kvRdd1.leftOuterJoin(kvRdd2, numPartitions=None)`<br/>

rightOuterJoin右外连接


```python
x = sc.parallelize([("a", 1), ("b", 4)])
y = sc.parallelize([("a", 2)])
x.leftOuterJoin(y).collect()
```

## kvRdd1.subtractByKey(kvRdd2)返回key在vkRdd1但不在kvRdd2中的数据


```python
kvRDD1 = sc.parallelize([(3,4),(3,6),(5,6),(1,2)])
kvRDD2 = sc.parallelize([(3,8), (1,2)])
kvRDD1.subtractByKey(kvRDD2).collect()
```

## kvRdd.countByKey()函数可以统计各个key值对应的数据的条数


```python
kvRDD1 = sc.parallelize([(3,4),(3,6),(5,6),(1,2)])
kvRDD1.countByKey()
```

## kvRdd.lookup(key)函数可以根据输入的key值来查找对应的Value值


```python
kvRDD1 = sc.parallelize([(3,4),(3,6),(5,6),(1,2)])
kvRDD1.lookup(3)
```

# 持久化操作
spark RDD的持久化机制，可以将需要重复运算的RDD存储在内存中，以便大幅提升运算效率.<br/>
有两个主要的函数：
   * persist() :对RDD进行持久化
   * unpersist() : 对RDD进行去持久化



```python

```

## End


[spark_DataFrame操作.ipynb](./spark_jupyter/spark_DataFrame操作.ipynb)

# spark_DataFrame操作

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

from pyspark.sql import SparkSession
from pyspark.sql import SQLContext
from pyspark.sql import functions as F, Window
from pyspark.sql.types import StructType, StructField, LongType, StringType, FloatType


spark = SparkSession.builder.appName("Python Spark SQL basic example").master('local[*]')\
    .getOrCreate()

sc = spark.sparkContext

sqlContext = SQLContext(sc)

```

RDD与DataFrame之间的差别在于：<br/>
* dataframe比rdd的速度快，对于结构化的数据，使用dataframe编写的代码更简洁。
* 对于非结构化数据，建议先使用rdd处理成结构化数据，然后转换成dataframe。

在dataFrame中的操作主要可以分为增,删,查,改,合并,统计,去重,格式转换,SQL操作,读写csv文件几个大的方向。<br/>
我们从数据库中查询到的数据返回的数据结构就是DataFrame格式的



```python
import os, sys
datafile_dir = 'D:/StudyNote/原来的一些学习笔记_GJY/StudyNote/vnote_notebooks/大数据/spark_学习/test_data/'
datafile1 = os.path.join(datafile_dir, 'WA_Fn-UseC_-Telco-Customer-Churn.csv')

```


```python

```

## 加载数据


```python
df = sqlContext.read.format('com.databricks.spark.csv')\
        .options(header='true', inferschema='true')\
        .load(datafile1)

```


```python
df.printSchema() # 以树的形式打印概要
# df.dtypes # 查看数据类型
# df.show(5) # 显示前N行

# df["Partner", "gender"].describe().show() # 对df中的数据进行统计，返回常用的一些统计指标的值
```


```python
df.head(3) # df.take(5) # 获取前N行数据,以python列表形式返回

df.count() # 查询总行数
```

## Column.alias(*alias) 用列别名返回
用一个或多个新名称返回此列(对于返回多个列的表达式,如explode)。


```python
df.select(df.customerID.alias("customerID_alias")).show(2) # 为查询的列取别名
```


```python
df.select('gender', 'Partner').count() # 计数
df.select('gender', 'Partner').distinct().count() # 去重后计数
```

## df.sample()随机取样
```
df.sample(withReplacement=None, fraction=None, seed=None)
withReplacement: 是否进行有放回的抽样, 默认False
fraction: 抽样比例fraction
```


```python
sample = df.sample(False, 0.5, 0)
sample.count() # 随机取样，抽取50%的数据出来
```

## expr(str)将字符串作为一个函数来执行
from pyspark.sql.functions import expr

expr(str)将表达式字符串解析为它所表示的列,或对列的函数操作


```python
from pyspark.sql.functions import expr

df.select(expr('Churn')).show(5) # 返回'Churn'列取值的长度

df.select(expr('length(Churn)')).show(5) # 返回'Churn'列取值的长度
```

## select() 选取某一列或某几列数据
select返回的是dataframe格式


```python

df.select("customerID") 
df.select(df.customerID, df.gender)
df.select(df["customerID"], df["gender"])

```

## df.where(condition) 选择满足条件的数据
与df.filter()功能一样


```python
df.where(df.gender=="Female").where(df.Churn>='Yes').count()
df.where((df.gender=="Female") & (df.Churn>='Yes')).count()

```

## 其它
```
withColumnRenamed("old_name", "new_name") # 重命名列
asc()  # 基于指定列返回一个升序表达式
desc()  # 基于指定列返回一个降序表达式
astype(dataType)   # 转换列的数据类型,跟cast()是同一个函数
cast(dataType)   # 转换数据类型
startswith(string)  # 判断列中每个值是否以指定字符开头，返回布尔值
endswith(string)   # 判断列中每个值是否以指定字符结尾，返回布尔值
isNotNull()   # 判断列中的值是否不为空
isNull()   # 判断列中的值是否为空
like(expression)   # 判断列中的值是否满足相似条件,判断条件跟sql语法相同,支持通配符
```

## 窗口函数


```python
from pyspark.sql import functions as F
from pyspark.sql.window import Window

df.select("customerID", 
          F.rank().over(Window.orderBy("customerID")).alias("customerID_rank"))\
      .show(5)


df.select("customerID", 
          F.rank().over(Window.orderBy(df.customerID.desc())).alias("customerID_rank"))\
      .show(5)

```

## 按条件筛选when / between
`when(condition1, value1).when(condition2, value2)..otherwise(other_value)`<br/>
当满足条件condition1时赋值为values1,当满足条件condition2时赋值为values2,....条件都不满足时赋值other_value.<br/>
otherwise表示,不满足条件的情况下，应该赋值何值.<br/>

与SQL中的case...when...语句类似.


```python
from pyspark.sql import functions as F

df.select(df.customerID, 
          F.when(df.gender=="Male", "1")\
           .when(df.gender=="Female", "0")\
           .otherwise("2")\
           .alias("sex"))\
    .show(10)

```

## between(lowerBound, upperBound) # 筛选出某个范围内的值
返回的是TRUE or FALSE


```python
df.select(df.customerID, df.tenure.between(10, 20).alias("tenure")).show(5)
```


```python
# 如果需要进行筛选df中的特定区间内的数据时，可以使用下面这种通过行索引进行筛选的方式

from pyspark.sql.functions import monotonically_increasing_id

dfWithIndex = df.withColumn("index", monotonically_increasing_id())  # 为df添加行索引
dfWithIndex.select(dfWithIndex.index, dfWithIndex.tenure, 
                   dfWithIndex.tenure.between("30","50").alias("tenure"))\
    .show(5) #筛选特定行

```

## filter()过滤数据


```python
df2 = df.filter(df['tenure']>=21) # 等价于 df = df.where(df['tenure']>=21)
df2.count()

df2 = df.filter("tenure>=21") # 可以用sql字符串的方式指定条件
df2.count()

# 同时指定多个过滤条件
df2 = df .filter("tenure>=21 and tenure<=30") # 
df2.count()

# 过滤null值或nan值时：
from pyspark.sql.functions import isnan, isnull
df3 = df.filter(isnull("tenure")) # 把列中数据为null的筛选出来(代表python的None类型)
df3.count()

df3 = df.filter(isnan("tenure"))  # 把列中数据为nan的筛选出来(Not a Number,非数字数据)
df3.count()
```

## pandas的DataFrame <====> pyspark的DataFrame


```python
import pandas as pd

pandas_df = pd.DataFrame({"name":["ss","aa","qq","ee"],
                          "age":[12,18,20,25]})

spark_df = spark.createDataFrame(pandas_df) # pandas中的df ==> pyspark中的df
type(spark_df)

new_pandas = spark_df.toPandas() # pyspark中的df ==> pandas中的df
type(new_pandas)
```

## 将rdd转为spark中的DataFrame格式


```python
from pyspark.sql import Row

row = Row("spe_id", "InOther")
x = ['x1','x2']
y = ['y1','y2']
new_df = sc.parallelize([row(x[i], y[i]) for i in range(2)]) # RDD
spark_df = new_df.toDF() # RDD ==> DataFrame
spark_df
```

## withColumn()
通过添加列或替换具有相同名称的现有列,来得到一个新的DataFrame.<br/>
熟用pyspark.sql.functions.<br/>
```
from pyspark.sql import functions as F
F.lit('xxx') # 创建一个字面值常量的列.
```


```python
df1 = df.select("customerID","gender","Partner","tenure")

from pyspark.sql import functions as F
from pyspark.sql.types import StringType

# 返回一个新的DF, 通过添加newcol_gender列(由gender列转换成大写)
withcolumns_df = df1.withColumn("newcol_gender", F.upper('gender')) 
withcolumns_df.show(3)

# 返回一个新的DF, 通过替换tenure列(取值+1)
withcolumns_df = df1.withColumn("tenure", df.tenure+1)
withcolumns_df.show(3)

# 修改指定列的类型
df1.printSchema()
df2 = df1.withColumn('tenure', F.col('tenure').cast("bigint"))
df2.printSchema()

# 转换列可通过多个字段进行
df2 = df1.withColumn('new_col', 
                F.concat_ws('_', *[F.lit("gender"), F.lit("Partner")]))
df2.show(3)

# 自定义UDF函数
def udf_concat_ws(sep, *cols):
    return sep.join(cols)
# 创建一个用户自定义的UDF函数(使用时只需要传入列名即可,函数内部自动得到列的值)
f_udf_concat_ws = F.udf(udf_concat_ws, StringType()) 

df2 = df1.withColumn('new_col', f_udf_concat_ws(F.lit('_'), "gender", "Partner")) # 注意F.lit()的使用
df2.show(3) 
```

## union()合并的操作(与SQL union all类似)
行合并(行会增加),
要求合并的两端具有相同的列名及列数


```python
df2 = df.select("customerID", "gender", "tenure").filter("tenure=1")
df3 = df.select("customerID", "gender", "tenure").filter("tenure=2")
df4 = df2.union(df3) # 等价于unionAll()
df2.count()
df3.count()
df4.count()

```

## Join()根据条件进行关联 (与SQL join类似)
```
df1.join(df2, on=None, how=None)
参数说明:
on: 联结条件, 可以是
    1. 要联结的列名字符串(单字段联结),eg: 'colName' 
    2. 要联结的列名字符串列表(多字段联结), eg: ['colName1', 'colName2']
    3. 关于Column的联结表达式(单字段联结), eg: df1.colnameX==df2.colnameY 
    4. 关于Column的联结表达式列表(多字段联结), eg: [df1.colnameX1==df2.colnameX2, df1.colnameY1==df2.colnameY2]
    其中1/2 要求联结列在df1,df2中同名; 3/4 不要求联结列在df1,df2中同名.
``` 
    
    
列合并(列会增加). <br/>
注意:使用df=df1.join(df2, on=)进行关联后,在结果df中可能会存在同名的列.<br/>
可在连接后使用select()选择出需要的列,或重新命名. <br/>
eg: df=df1.join(df2, on=).select(df1.colname1, df1.colname2, df2.colnamex.alias('colname_alias'))


```python
df2 = df1.select("customerID", "gender", "tenure")
df3 = df1.select("gender", "tenure", "Partner")
df4 = df2.join(df3, df1.gender==df2.gender, how="left_outer")
# df4 = df2.join(df3, 'gender', how="left_outer")
df4 = df4.select(df2.customerID, df2.gender, df2.tenure, df3.tenure.alias('tenure_2'))
df4.show(5)
df4.printSchema()
```

## 差集,交集,并集
* df1.subtract(df2)求差集
返回一个新的df, 其由在df1中但不在df2中的行组成

* df1.intersect(df2)求交集
返回一个新的df, 其由在df1中又在df2中的行组成

* df1.union(df2) 求并集(但并不去重). union+distinct求并集并进行去重


```python
df1 = spark.createDataFrame((
      (1, "asf"),
      (2, "2143"),
      (3, "rfds")
    )).toDF("label", "sentence")
df1.show()

df2 = spark.createDataFrame((
      (1, "asf"),
      (2, "2143"),
      (4, "f8934y")
    )).toDF("label", "sentence")

# 差集
new_df = df1.select("sentence").subtract(df2.select("sentence"))
new_df.show()

# 交集
new_df2 = df1.select("sentence").intersect(df2.select("sentence"))
new_df2.show()

# 并集
new_df3 = df1.select("sentence").union(df2.select("sentence"))
new_df3.show()

# 求并集并去重
new_df4 = df1.select("sentence").union(df2.select("sentence")).distinct()
new_df4.show()
```

## 行 <==> 列 
行列转换

## 将一行数据拆成多行(列转行)
explode()+ split() # 对某列的数据以固定的分隔符进行分隔,并新增一列


```python
from pyspark.sql import functions as F

df1 = spark.createDataFrame((
      (1, "XXX-YYY-ZZZ"),
      (2, "AAA BB"),
      (3, "CC-DD")
    )).toDF("label", "sentence")
df1.show()

# 将sentence列值按'-'分隔并扩展成多行
df1.withColumn("sentence_d", F.explode(F.split(df1.sentence, "-"))).show()

```

## pivot()聚合透视(行转列)

执行指定的聚合并对对指定列进行透视。



```python
dfx = spark.sparkContext.parallelize(
    [[15,11,2],
     [15,14,5],
     [15,16,4],
     [15,13,4], 
     [18,21,3],
     [18,11,3],
     [18,13,1]]
    ).toDF(["userID","movieID","rating"])
dfx.show()

# pivot 透视
res_df = dfx.groupBy("userID").pivot("movieID").sum("rating")
res_df.show()


# 手动指定要透视键的取值列表 -- 效率要高于不指定时
res_df = dfx.groupBy("userID")\
            .pivot("movieID", values=[11,13,14,16,21])\
            .sum("rating")
res_df.show()
```

## 统计的操作


```python
# 频数统计与筛选，根据tenure与gender字段，统计该字段值出现频率在40%以上的内容
df.stat.freqItems(["tenure", "gender"], 0.4).show() 
```


```python
# 交叉分析
df.crosstab('tenure', 'Partner').show(5)
```

## 分组函数


```python
df.groupby('gender').agg({'tenure': 'mean'}).show(5)

df.groupby('gender').count().show()

from pyspark.sql import functions as F
df.groupBy("gender")\
   .agg(F.avg("tenure"), F.min("tenure"), F.max("tenure"))\
   .show(5)

```

## 删除的操作
df = df.drop(*cols) -- 删除指定的一列或或多列<br/>
df = df.na.drop(how='any', subset=[col1, col2, ...]) # 如果一行中指定列包含null值则删除<br/>
df = df.dropna() 等价于 df.na.drop()


```python

df1 = spark.createDataFrame((
      (1, "asf", 11),
      (2, "2143", 21),
      (3, "rfds", 31)
    )).toDF("label", "sentence", 'xx')
df1.show()

df1.drop(*["label", "xx"]).columns
df1.drop(df1.label).columns
```


```python
df1 = spark.createDataFrame((
      (1, None, 11),
      (2, "2143", None),
      (3, "rfds", 31),
      (4, None, None)
    )).toDF("label", "sentence", 'xx')
df1.show()

df1.na.drop(how='any', subset=['sentence', 'xx']).show() # 如果一行中指定列包含null值则删除
df1.na.drop(how='all', subset=['sentence', 'xx']).show() # 如果一行中指定列全是null值则删除
# how:默认为any, subset:默认为None,即所有列


# df.dropna() 等价于 df.na.drop()
df1.dropna(how='any', subset=['sentence', 'xx']).show()
```

## 空值填充fillna() 
df.na.fill()与fillna()相同


```python
df1 = spark.createDataFrame((
      (1, None, 11),
      (2, "2143", None),
      (3, "rfds", 31),
      (4, None, None)
    )).toDF("label", "sentence", 'xx')
df1.show()

df1.fillna(-1).show() # -1填充

means = df1.select(F.avg(df1.xx))
df1.na.fill({"xx": means.toPandas().values[0][0]}).show() # 均值填充

```

## 去重的操作
df1.dropDuplicates(subset=['sentence']).show() # 根据指定列去重. 类似于select distinct a, b操作


```python
df1 = spark.createDataFrame((
      (1, "asf", 11),
      (2, "hello", 21),
      (3, "rfds", 31),
      (4, "hello", 31)
    )).toDF("label", "sentence", 'xx')
df1.show()


df1.dropDuplicates(subset=['sentence']).show() # 根据指定列去重

```


```python
## 从hive读取数据,要在开始的时候创建一个sparksession的对象
```


```python
from pyspark.sql import SparkSession
myspark = SparkSession.builder \
    .appName('compute_customer_age') \
    .config('spark.executor.memory','2g') \
    .enableHiveSupport() \
    .getOrCreate()


```

## pyspark.sql.functions


```python
# 使用 pyspark.sql.functions.udf 进行自定义函数的使用
from pyspark.sql.functions import udf
from pyspark.sql.types import StringType

df1 = spark.createDataFrame((
      (1, "asf", 11),
      (2, "hello", 21),
      (3, "rfds", 31),
      (4, "hello", 31)
    )).toDF("label", "sentence", 'xx')
df1.show()


def udf_test(x):
    if x >= 30:
        return "yes"
    else:
        return "no"
f_udf_test = udf(udf_test, returnType=StringType())   # 注册函数

df2 = df1.withColumn("xx_new", f_udf_test(df1.xx))   # 调用函数
df2.show()

# 使用udf对性能会有负面的影响，如果不是太过于复杂的逻辑
# 可以使用f.when.when.otherwise()的方式得出想要的结果
```

### 字符串方法


```python
# 字符串方法

from pyspark.sql.functions import concat, concat_ws
df1 = spark.createDataFrame([('abcd', '123')], ['s', 'd'])
df1.show()

# 直接拼接
df1.select(concat(df1.s, df1.d).alias('s_d')).show()

# 指定拼接符
df1.select(concat_ws('-', df1.s, df1.d).alias('s-d')).show()

```


```python
# 格式化字符串
from pyspark.sql.functions import format_string
df = spark.createDataFrame([(5, "hello")], ['col_a', 'col_b'])
df.select(format_string('%d %s', df.col_a, df.col_b).alias('col_v'))\
  .withColumnRenamed("col_av", "col_vv").show()

```


```python
# 查找字符串的位置
from pyspark.sql.functions import instr
df = spark.createDataFrame([('abcd',)], ['col_s'])
df.show()
df.select(instr(df.col_s, 'b').alias('col_s_idx')).show()

```


```python
# 字符串截取
from pyspark.sql.functions import substring
df = spark.createDataFrame([('abcd',)], ['col_s'])
df.select(substring(df.col_s, 1, 2).alias('col_sub')).show()  #1与2表示开始与截取长度

```


```python
# 正则表达式替换
from pyspark.sql.functions import regexp_replace
df = spark.createDataFrame([('100sss200',)], ['col_str'])
df.select(regexp_replace('col_str', '(\d)', '-').alias('col_regex')).collect()  # 替换类型，正则语句，替换内容

```

### 与时间有关的方法


```python
# 与时间有关的方法
from pyspark.sql import functions as F

df = spark.createDataFrame([('2015-04-08',)], ['col_dt'])
df.select(F.date_format('col_dt', 'yyyyMMdd').alias('date')).collect()

```


```python
from pyspark.sql.functions import current_date
spark.range(3).withColumn('date', current_date()).show() # 获取当前日期

from pyspark.sql.functions import current_timestamp
spark.range(3).withColumn('date', current_timestamp()).show() # 获取当前时间戳


# 字符串日期 ==> 时间日期格式

from pyspark.sql.functions import to_date, to_timestamp
df = spark.createDataFrame([('1997-02-28 10:30:00',)], ['col_dt'])
df.select(to_date(df.col_dt).alias('date')).show()   # 转日期
df.select(to_timestamp(df.col_dt).alias('dt')).show()   # 带时间的日期
df.select(to_timestamp(df.col_dt, 'yyyy-MM-dd HH:mm:ss').alias('dt')).show()   # 可以指定日期格式

```


```python
# 获取日期中的年月日
from pyspark.sql.functions import year, month, dayofmonth
df = spark.createDataFrame([('2015-04-08',)], ['col_dt'])
df.select(year('col_dt').alias('year'), 
          month('col_dt').alias('month'),
          dayofmonth('col_dt').alias('day')
  ).show()

# 只接受以-连接的日期，如果是以/等其他连接符连接的，需要进行格式转换或者字符替换。

```


```python

from pyspark.sql.functions import datediff, months_between 

# 日期差
df = spark.createDataFrame([('2015-04-08','2015-05-10')], ['d1', 'd2'])
df.select(datediff(df.d2, df.d1).alias('diff')).show() 

# 月份差
df = spark.createDataFrame([('1997-02-28 10:30:00', '1996-10-30')], ['t', 'd'])
df.select(months_between(df.t, df.d).alias('months')).show()

```

### functions其它函数


```python
pyspark.sql.functions.abs(col)   # 计算绝对值
pyspark.sql.functions.avg(col)   # 聚合函数：返回组中的值的平均值。
pyspark.sql.functions.variance(col)   # 返回组中值的总体方差
pyspark.sql.functions.ceil(col)   # 计算给定值的上限
pyspark.sql.functions.floor(col)   # 计算给定值的下限。
pyspark.sql.functions.collect_list(col)   # 返回重复对象的列表。
pyspark.sql.functions.collect_set(col)    # 返回一组消除重复元素的对象。
pyspark.sql.functions.count(col)    # 返回组中的项数量。
pyspark.sql.functions.countDistinct(col, *cols)   # 返回一列或多列的去重计数的新列。
pyspark.sql.functions.initcap(col)    # 在句子中将每个单词的第一个字母翻译成大写。
pyspark.sql.functions.isnan(col)    # 如果列是NaN，则返回true的表达式
pyspark.sql.functions.lit(col)     # 创建一个文字值的列
pyspark.sql.functions.lower(col)   # 将字符串列转换为小写
pyspark.sql.functions.reverse(col)   # 反转字符串列并将其作为新的字符串列返回
pyspark.sql.functions.sort_array(col, asc=True)   # 按升序对给定列的输入数组进行排序
pyspark.sql.functions.split(str, pattern)   # 按指定字符进行分隔数据
pyspark.sql.functions. array_min (col)   # 计算指定列的最小值
pyspark.sql.functions. array_max (col)   # 计算指定列的最大值
pyspark.sql.functions.stddev(col)    # 返回组中表达式的无偏样本标准差
pyspark.sql.functions.sumDistinct(col)   # 返回表达式中不同值的总和
pyspark.sql.functions.trim(col)   # 去除空格
pyspark.sql.functions. greatest (col1,col2)   # 求行的最大值，可以计算一行中多列的最大值
pyspark.sql.functions. least (col1,col2)   # 求行的最小值，可以计算一行中多列的最小值，也可以用lit()指定常数进行与列的值进行比较

```

## End

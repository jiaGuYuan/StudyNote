# Tensorflow
![Tensorflow2](images_attachments/20200514114617678_11115.png =900x)

![Tensorflow_Estimator流程](images_attachments/20200514114630426_16001.png =900x)

![Tensorflow特征处理API](images_attachments/20200514114658018_8435.png =900x)

## tf.data
Dataset API 包含下列类:
![](images_attachments/20200514165915240_6051.png =900x)

* Dataset: 包含创建和转换数据集的方法的基类。您还可以通过该类从内存中的数据或 Python 生成器初始化数据集。
* TextLineDataset: 从文本文件中读取行。
* TFRecordDataset: 从 TFRecord 文件中读取记录。
* FixedLengthRecordDataset: 从二进制文件中读取具有固定大小的记录。
* Iterator: 提供一次访问一个数据集元素的方法


使用 tf.data API 可以轻松处理大量数据、不同的数据格式以及复杂的转换。
tf.data API 在 TensorFlow 中引入了两个新的抽象类：
+ tf.data.Dataset: 表示一系列元素，其中每个元素包含一个或多个 Tensor 对象
  - 创建来源（通过内存中的某些张量构建 Dataset: Dataset.from_tensor_slices()等）
  - 应用转换（将一个Dataset转换为另一个Dataset: Dataset.map(), Dataset.batch()等）
  - 以TFRecord文件创建Dataset: tf.data.TFRecordDataset
  - dataset如果用于tf.estimator， 必须是(字典形式的feature, label)
+ tf.data.Iterator 提供了从数据集中提取元素的主要方法。
  － Iterator.get_next(): 返回的OP会在执行时生成 Dataset 的下一个元素，此操作通常充当输入管道代码和模型之间的接口。
  － Dataset.make_one_shot_iterator(): 仅支持对数据集进行一次迭代.不能用于迭代有状态对象的数据集.

一个数据集包含多个元素(样本)，每个元素(样本)的结构都相同。
一个元素(样本)包含一个或多个 tf.Tensor 对象，这些对象称为组件。
可以通过 Dataset.output_types 和 Dataset.output_shapes 属性检查数据集元素各个组件的推理类型和形状。
```
# 数据集dataset2,包含4个元素．每个元素对应一个元组(包括一个1dim的tensor和一个100dim的tensor)
# 以元组的方式提供构成dataset的多个tensor．第一个维度必须相同
dataset1 = tf.data.Dataset.from_tensor_slices(
   (tf.random_uniform([4]),
    tf.random_uniform([4, 100], maxval=100, dtype=tf.int32))) 
print(dataset1.output_types)
print(dataset1.output_shapes)

# 使用迭代器访问dataset元素. 
dataset2 = tf.data.Dataset.range(5)
iterator2 = dataset2.make_one_shot_iterator() # 不能对dataset1用单次迭代器,因为dataset1是有状态对象的数据集
next_element2 = iterator2.get_next()
with tf.Session() as sess:
    for i in range(5):
      value = sess.run(next_element2)
      print(value)
      
```

**训练的数据集迭代轮次：Dataset.repeat()**
Dataset.repeat() 迭代同一数据数据集多个周期(epoch)
    例如，要创建一个将其输入重复 10 个周期的数据集：
    dataset = dataset.repeat(10) # Dataset.repeat()中没有参数 转换将无限次地重复输入
**训练的数据集重排：Dataset.shuffle()**
Dataset.shuffle() 会使用算法随机重排输入数据集：它会维持一个固定大小的缓冲区，并从该缓冲区统一地随机选择下一个元素。
    dataset = dataset.shuffle(buffer_size=10000) #buffer_size一般要大于等于dataset中的样本数

## 特征处理tf.feature_colum
![](images_attachments/20200514194046914_17892.png =800x)

## TFRecords
TFRecords是TensorFlow官方推荐使用的数据格式化存储工具，它不仅规范了数据的读写方式，还大大地提高了IO效率.
文件格式'*.tfrecords'.

### spark生成TFRecords
![](images_attachments/20200513221101943_17308.png =600x)

### 选择正确的IO训练方式
TensorFlow读取数据的方式主要有2种，一般选择错误会造成性能问题，两种方式为：

* Feed_dict
  通过feed_dict将数据喂给session.run函数.
  优点:清晰，易于理解
  缺点:性能差 --原因是feed给session的数据需要在session.run之前准备好，如果之前这个数据没有进入内存，那么就需要等待数据进入内存，而在实际场景中，这不仅仅是等待数据从磁盘或者网络进入内存的事情，还可能包括很多前期预处理的工作也在这里做，所以相当于一个串行过程。此时，GPU显存处于等待状态，同时，由于tf的Graph中的input为空，所以CPU也处于等待状态，无法运算。

* RecordReader：
  在tf中还有batch与threads的概念，可以异步的读取数据，保证在GPU或者CPU进行计算的时候，读取数据这个操作也可以多线程异步执行。
 
**好的IO训练方式(数据提供方式)** 是将程序中的预处理部分从代码中剥离出来，使用Map-Reduce批处理去做;MR输出为TensorFlow Record格式，避免使用Feed_dict。

### 使用TFRecords文件的优势
Tensorflow有和TFRecords配套的一些函数，可以加快数据的处理。实际读取TFRecords数据时，先以相应的TFRecords文件为参数，创建一个输入队列，这个队列有一定的容量（视具体硬件限制，用户可以设置不同的值），在一部分数据出队列时，TFRecords中的其他数据就可以通过预取进入队列，并且这个过程和网络的计算是独立进行的。
网络每一个iteration的训练不必等待数据队列准备好再开始，队列中的数据始终是充足的，而往队列中填充数据时，也可以使用多线程加速。
![](images_attachments/20200513222409358_25711.png =600x)




### Example结构解析
TFRecords文件包含了tf.train.Example 协议内存块(protocol buffer);
    议内存块包含了字段 Features。
    可以将你的数据填入到Example协议内存块(protocol buffer)，将协议内存块序列化为一个字符串， 并且通过tf.python_io.TFRecordWriter 写入到TFRecords文件。

```
# tf.train.Example协议内存块, 包含了字段 Features，Features包含了一个Feature字段，Features中包含要写入的数据、并指明数据类型。
# 这是一个样本的结构，批数据需要循环存入这样的结构
example = tf.train.Example(
  features=tf.train.Features(feature={
    "features": tf.train.Feature(bytes_list=tf.train.BytesList(value=[features])),
    "label": tf.train.Feature(int64_list=tf.train.Int64List(value=[label])),
  })
)
```
+ tf.train.Feature(options)
  - 对应一批样本中某个特征的取值(相当于训练数据的一列)（这批样本的多少由TFRecordWriter()转换的原文件决定）
  - options：例如
   bytes_list=tf.train.BytesList(value=[Bytes]) # 参数类型为Bytes列表
   int64_list=tf.train.Int64List(value=[Value])
  - 支持存入的类型如下
    * tf.train.Int64List(value=[Value])
    * tf.train.BytesList(value=[Bytes])
    * tf.train.FloatList(value=[value])
  - 返回Feature

+ tf.train.Features(feature=None)
  - 对应一批样本; 参数为{特征名:(Feature格式的)这批样本中该特征的值,...}
  - 构建每个样本的信息(字段)键值对
  - feature:字典数据
    * key为要保存的名字
    * value为tf.train.Feature实例
  - return:Features类型封装的一批样本

+ tf.train.Example(features=None)
  - 写入tfrecords文件
  - features: tf.train.Features类型的特征实例
  - return：example格式协议块

将其他数据存储为TFRecords文件的步骤： 
1. 建立TFRecord存储器
2. 构造Example模块
3. 将example数据系列化为字符串
4. 使用TFRecord存储器将系列化为字符串的example数据写入协议缓冲区
```
data = {
    'price':[0, 0, 1, 1],
    'length': [0, 1.1, 2.2, 3.3],
    'width': [1.4, 1.5, 4.2,3.0]
}
df = pd.DataFrame(data, colnums=['price', 'length', 'width'])

# 建立TFRecord存储器
writer = tf.python_io.TFRecordWriter(path, options=None)

# 构造tf.train.Feature --> 构造tf.train.Features
feature_internal = {
    "label":tf.train.Feature(int64_list=tf.train.Int64List(value=df['price'])),
    "length":tf.train.Feature(float_list=tf.train.FloatList(value=df['length'])),
    "width":tf.train.Feature(float_list=tf.train.FloatList(value=df['width']))
}
features_extern = tf.train.Features(feature_internal)

# 构造tf.train.Example -- 相当于有4个样本的数据集df
example = tf.train.Example(features_extern)

# 将example数据系列化为字符串
example_str = example.SerializeToString()

# 将系列化为字符串的example数据写入协议缓冲区
writer.write(example_str)
writer.close()
```

数值型特征:
```
columnNameXXX1 = tf.feature_column.numeric_column('columnName')
```

固定数目的类别型特征
```
columnNameXXX2 = tf.feature_column.categorical_column_with_vocabulary_list(
    'columnName', ['类别取值1', '类别取值2', '...'])
```


不确定类别数量时，哈希列进行分桶
```
columnNameXXX3 = tf.feature_column.categorical_column_with_hash_bucket(
    'columnName', hash_bucket_size=N)
```


分桶：
```
age_buckets = tf.feature_column.bucketized_column(age, 
    boundaries=[18, 25, 30, 35, 40, 45, 50, 55, 60, 65])
```

交叉：
```
crossed_colNameX_colNameY = tf.feature_column.crossed_column(
    ['colNameX', 'colNameY'], hash_bucket_size=1000)
    
crossed_feature = tf.feature_column.crossed_column(
    [crossed_colNameX_colNameY, 'colNameXXX','colNameYYY'], hash_bucket_size=1000)
```




```
features =  [columnNameXXX1, columnNameXXX2, crossed_feature, ...]
classifiry = tf.estimator.LinearClassifier(feature_columns=features)
# tensorflow处理输入数据的函数格式(输入/输出)是固定的,因此需要使用partial处理
train_func = functools.partial(input_func, train_file, epoches=3, batch_size=32)
classifiry.train(train_func)
result = classifiry.evaluate(test_func)
```




## Tensorboard


## TODO
```
def parse_tfrecords_function(example_proto):
    features = {
        "label": tf.FixedLenFeature([], tf.int64),
        "feature": tf.FixedLenFeature([], tf.string)
    }
    parsed_features = tf.parse_single_example(example_proto, features) # ??

    feature = tf.decode_raw(parsed_features['feature'], tf.float64) # ??
    feature = tf.reshape(tf.cast(feature, tf.float32), [1, 121])
    label = tf.reshape(tf.cast(parsed_features['label'], tf.float32), [1, 1])
    return feature, label

dataset = tf.data.TFRecordDataset(["./train_ctr_201904.tfrecords"]) # ??
dataset = dataset.map(parse_tfrecords_function)
dataset = dataset.shuffle(buffer_size=10000)
dataset = dataset.repeat(10000)
```
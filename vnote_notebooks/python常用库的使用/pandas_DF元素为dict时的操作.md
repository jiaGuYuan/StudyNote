```python
%config ZMQInteractiveShell.ast_node_interactivity='all'

import numpy as np
import pandas as pd

```

# DataFrame列元素类型是dict时的操作


```python
product_attri_data = {
    'product_id':[1, 1, 1, 2, 2],
    'attribute1':[{'colour':'red', 'size':(3,4,5)}, 
                {'colour2':'blue', 'size':(1, 5, 1)}, 
                {'colour3':'black', 'size':(0.25, 1, 3)}, 
                {'colour':'blue', 'size':(1, 1, 1)}, 
                {'colour1':'white', 'size':(2, 0.5, 1)}],
    'attribute2':[{'xx':'XXX'}, 
                {'yy':'YYY', 'yy2':'YYY2'}, 
                {'zz':'ZZZZ'}, 
                {}, 
                {}]
}

df = pd.DataFrame(product_attri_data)
df

# 合并两列的字典
def merge_attri(record):
#     print(type(record), '\n', 
#           type(record['attribute1']), record['attribute1'], '\n', 
#           type(record['attribute2']), record['attribute2'])
    record['attribute1'].update(record['attribute2'])  # 注意:这个方法返回None
    return record['attribute1']

df['attribute_merge'] = df.apply(merge_attri, 
                              axis=1, result_type='reduce')
df

# 同一列聚合后的字典合并
def merge_attri2(ser):
    from functools import reduce
    def dict_update(dic1, dic2):
        dic1.update(dic2)
        return dic1
    res = reduce(dict_update, ser.values)
    return res
df.groupby('product_id').agg({'attribute_merge': merge_attri2})
```




<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>product_id</th>
      <th>attribute1</th>
      <th>attribute2</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>1</td>
      <td>{'colour': 'red', 'size': (3, 4, 5)}</td>
      <td>{'xx': 'XXX'}</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>{'colour2': 'blue', 'size': (1, 5, 1)}</td>
      <td>{'yy': 'YYY', 'yy2': 'YYY2'}</td>
    </tr>
    <tr>
      <th>2</th>
      <td>1</td>
      <td>{'colour3': 'black', 'size': (0.25, 1, 3)}</td>
      <td>{'zz': 'ZZZZ'}</td>
    </tr>
    <tr>
      <th>3</th>
      <td>2</td>
      <td>{'colour': 'blue', 'size': (1, 1, 1)}</td>
      <td>{}</td>
    </tr>
    <tr>
      <th>4</th>
      <td>2</td>
      <td>{'colour1': 'white', 'size': (2, 0.5, 1)}</td>
      <td>{}</td>
    </tr>
  </tbody>
</table>
</div>



    <class 'pandas.core.series.Series'> 
     <class 'dict'> {'colour': 'red', 'size': (3, 4, 5)} 
     <class 'dict'> {'xx': 'XXX'}
    <class 'pandas.core.series.Series'> 
     <class 'dict'> {'colour2': 'blue', 'size': (1, 5, 1)} 
     <class 'dict'> {'yy': 'YYY', 'yy2': 'YYY2'}
    <class 'pandas.core.series.Series'> 
     <class 'dict'> {'colour3': 'black', 'size': (0.25, 1, 3)} 
     <class 'dict'> {'zz': 'ZZZZ'}
    <class 'pandas.core.series.Series'> 
     <class 'dict'> {'colour': 'blue', 'size': (1, 1, 1)} 
     <class 'dict'> {}
    <class 'pandas.core.series.Series'> 
     <class 'dict'> {'colour1': 'white', 'size': (2, 0.5, 1)} 
     <class 'dict'> {}
    




<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>product_id</th>
      <th>attribute1</th>
      <th>attribute2</th>
      <th>attribute_merge</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>1</td>
      <td>{'colour': 'red', 'size': (3, 4, 5), 'xx': 'XXX'}</td>
      <td>{'xx': 'XXX'}</td>
      <td>{'colour': 'red', 'size': (3, 4, 5), 'xx': 'XXX'}</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>{'colour2': 'blue', 'size': (1, 5, 1), 'yy': '...</td>
      <td>{'yy': 'YYY', 'yy2': 'YYY2'}</td>
      <td>{'colour2': 'blue', 'size': (1, 5, 1), 'yy': '...</td>
    </tr>
    <tr>
      <th>2</th>
      <td>1</td>
      <td>{'colour3': 'black', 'size': (0.25, 1, 3), 'zz...</td>
      <td>{'zz': 'ZZZZ'}</td>
      <td>{'colour3': 'black', 'size': (0.25, 1, 3), 'zz...</td>
    </tr>
    <tr>
      <th>3</th>
      <td>2</td>
      <td>{'colour': 'blue', 'size': (1, 1, 1)}</td>
      <td>{}</td>
      <td>{'colour': 'blue', 'size': (1, 1, 1)}</td>
    </tr>
    <tr>
      <th>4</th>
      <td>2</td>
      <td>{'colour1': 'white', 'size': (2, 0.5, 1)}</td>
      <td>{}</td>
      <td>{'colour1': 'white', 'size': (2, 0.5, 1)}</td>
    </tr>
  </tbody>
</table>
</div>






<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>attribute_merge</th>
    </tr>
    <tr>
      <th>product_id</th>
      <th></th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>1</th>
      <td>{'colour': 'red', 'size': (0.25, 1, 3), 'xx': ...</td>
    </tr>
    <tr>
      <th>2</th>
      <td>{'colour': 'blue', 'size': (2, 0.5, 1), 'colou...</td>
    </tr>
  </tbody>
</table>
</div>




```python

```


```python
data = {
    'product_id':[1, 1, 1, 2, 2],
    'attri_name':['colour', 'size', 'weight', 'colour', 'weight'],
    'attri_value':['red', '12x14', 15.6, 'blue', 10.2] 
}

df2 = pd.DataFrame(data)
df2

# 同一列聚合后的字典合并. (先将两列组合成元组,再由元组构成字典)
df2['attri_k_v'] = df2.apply(lambda row:(row['attri_name'], row['attri_value']), axis=1)
df2
df2['attri_merge'] = df2.groupby('product_id')\
    .agg({'attri_k_v': lambda attr_kv_ser:dict(attr_kv_ser.values)})

df2
```




<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>product_id</th>
      <th>attri_name</th>
      <th>attri_value</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>1</td>
      <td>colour</td>
      <td>red</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>size</td>
      <td>12x14</td>
    </tr>
    <tr>
      <th>2</th>
      <td>1</td>
      <td>weight</td>
      <td>15.6</td>
    </tr>
    <tr>
      <th>3</th>
      <td>2</td>
      <td>colour</td>
      <td>blue</td>
    </tr>
    <tr>
      <th>4</th>
      <td>2</td>
      <td>weight</td>
      <td>10.2</td>
    </tr>
  </tbody>
</table>
</div>






<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>product_id</th>
      <th>attri_name</th>
      <th>attri_value</th>
      <th>attri_k_v</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>1</td>
      <td>colour</td>
      <td>red</td>
      <td>(colour, red)</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>size</td>
      <td>12x14</td>
      <td>(size, 12x14)</td>
    </tr>
    <tr>
      <th>2</th>
      <td>1</td>
      <td>weight</td>
      <td>15.6</td>
      <td>(weight, 15.6)</td>
    </tr>
    <tr>
      <th>3</th>
      <td>2</td>
      <td>colour</td>
      <td>blue</td>
      <td>(colour, blue)</td>
    </tr>
    <tr>
      <th>4</th>
      <td>2</td>
      <td>weight</td>
      <td>10.2</td>
      <td>(weight, 10.2)</td>
    </tr>
  </tbody>
</table>
</div>






<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>product_id</th>
      <th>attri_name</th>
      <th>attri_value</th>
      <th>attri_k_v</th>
      <th>attri_merge</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>1</td>
      <td>colour</td>
      <td>red</td>
      <td>(colour, red)</td>
      <td>NaN</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>size</td>
      <td>12x14</td>
      <td>(size, 12x14)</td>
      <td>{'colour': 'red', 'size': '12x14', 'weight': 1...</td>
    </tr>
    <tr>
      <th>2</th>
      <td>1</td>
      <td>weight</td>
      <td>15.6</td>
      <td>(weight, 15.6)</td>
      <td>{'colour': 'blue', 'weight': 10.2}</td>
    </tr>
    <tr>
      <th>3</th>
      <td>2</td>
      <td>colour</td>
      <td>blue</td>
      <td>(colour, blue)</td>
      <td>NaN</td>
    </tr>
    <tr>
      <th>4</th>
      <td>2</td>
      <td>weight</td>
      <td>10.2</td>
      <td>(weight, 10.2)</td>
      <td>NaN</td>
    </tr>
  </tbody>
</table>
</div>




```python
# 错误示例: 以下代码会产生 SettingWithCopyWarning -- 操作的是切片产生的副本.
# A value is trying to be set on a copy of a slice from a DataFrame.
# Try using .loc[row_indexer,col_indexer] = value instead
df3 = df2[['product_id', 'attri_name', 'attri_value']] # 这里产生的是“链接索引”
df3['new_col'] = df3[['attri_name', 'attri_value']].apply(
    lambda row:(row['attri_name'], row['attri_value']), 
    axis=1, result_type='reduce')
df3


df4 = df2.loc[:,['product_id', 'attri_name', 'attri_value']] # 这里产生的是副本,所以不会产生SettingWithCopyWarning
df4['new_col'] = df4[['attri_name', 'attri_value']]\
    .apply(lambda row:(row['attri_name'], row['attri_value']), 
           axis=1, result_type='reduce')
df4

# 直接操作切片,也不会产生SettingWithCopyWarning
df5 = df2[['attri_name', 'attri_value']]\
    .apply(lambda row:(row['attri_name'], row['attri_value']), 
           axis=1, result_type='reduce')
df5
```

    E:\anaconda3\envs\py37_tf2x\lib\site-packages\ipykernel_launcher.py:7: SettingWithCopyWarning: 
    A value is trying to be set on a copy of a slice from a DataFrame.
    Try using .loc[row_indexer,col_indexer] = value instead
    
    See the caveats in the documentation: https://pandas.pydata.org/pandas-docs/stable/user_guide/indexing.html#returning-a-view-versus-a-copy
      import sys
    




<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>product_id</th>
      <th>attri_name</th>
      <th>attri_value</th>
      <th>new_col</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>1</td>
      <td>colour</td>
      <td>red</td>
      <td>(colour, red)</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>size</td>
      <td>12x14</td>
      <td>(size, 12x14)</td>
    </tr>
    <tr>
      <th>2</th>
      <td>1</td>
      <td>weight</td>
      <td>15.6</td>
      <td>(weight, 15.6)</td>
    </tr>
    <tr>
      <th>3</th>
      <td>2</td>
      <td>colour</td>
      <td>blue</td>
      <td>(colour, blue)</td>
    </tr>
    <tr>
      <th>4</th>
      <td>2</td>
      <td>weight</td>
      <td>10.2</td>
      <td>(weight, 10.2)</td>
    </tr>
  </tbody>
</table>
</div>






<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>product_id</th>
      <th>attri_name</th>
      <th>attri_value</th>
      <th>new_col</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>1</td>
      <td>colour</td>
      <td>red</td>
      <td>(colour, red)</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>size</td>
      <td>12x14</td>
      <td>(size, 12x14)</td>
    </tr>
    <tr>
      <th>2</th>
      <td>1</td>
      <td>weight</td>
      <td>15.6</td>
      <td>(weight, 15.6)</td>
    </tr>
    <tr>
      <th>3</th>
      <td>2</td>
      <td>colour</td>
      <td>blue</td>
      <td>(colour, blue)</td>
    </tr>
    <tr>
      <th>4</th>
      <td>2</td>
      <td>weight</td>
      <td>10.2</td>
      <td>(weight, 10.2)</td>
    </tr>
  </tbody>
</table>
</div>






    0     (colour, red)
    1     (size, 12x14)
    2    (weight, 15.6)
    3    (colour, blue)
    4    (weight, 10.2)
    dtype: object




```python

```


```python
product_attri_data2 = {
    'product_id':[1, 1, 1, 2, 2],
    'attribute1':[{'colour':'red', 'size':(3,4,5)}, 
                {'colour2':'blue', 'size':(1, 5, 1)}, 
                {'colour3':'black', 'size':(0.25, 1, 3)}, 
                {'colour':'blue', 'size':(1, 1, 1)}, 
                {'colour1':'white', 'size':(2, 0.5, 1)}],
    
    # 有nan时,attribute1和attribute2两个字典无法正常合并
    'attribute2':[np.nan, 
                {'yy':'YYY', 'yy2':'YYY2'}, 
                {'zz':'ZZZZ'}, 
                np.nan, 
                {}]
    
#     没有nan时,attribute1和attribute2两个字典可正常合并
#     'attribute2':[{}, 
#                 {'yy':'YYY', 'yy2':'YYY2'}, 
#                 {'zz':'ZZZZ'}, 
#                 {}, 
#                 {}]
}

df5 = pd.DataFrame(product_attri_data2)
df5

def merge_attri_dict(row):
#     print(type(row), '\n',
#           type(row['attribute1']), row['attribute1'], '\n',
#           type(row['attribute2']), row['attribute2'], '\n')
    row['attribute1'].update(row['attribute2'])
    return row['attribute1']

# TypeError: 'float' object is not iterable
# 有nan时,直接合并两列字典会报错
# df5['attribute_merge'] = df5.apply(merge_attri_dict,
#     axis=1, result_type='reduce')


# OK
# 方法一
# 将'attribute2'列的nan替换成空字典{}, 现进行两列字典的合并
df5['attribute2'] = df5['attribute2'].apply(lambda item: item if item is not np.nan else {}) # 逐一修改Series元素类型
df5['attribute_merge'] = df5.apply(merge_attri_dict,
    axis=1, result_type='reduce')
df5

# 疑问: 怎么直接将一列中的nan替换成空字典
# df5['attribute2'].fillna({}) //不行


# 方法二
# 一开始不要存成dict格式,而是存成字符串结构,这样就可以使用使用b'{}'填充nan,最后再反序列化进行合并.
# 当列中的格式为json格式时,可以使用这种方法
# import json
# df5['attribute2'] = df5['attribute2'].apply(lambda item: json.dumps(item) if item is not np.nan else np.nan) # 将dict转换成json字符串(特意构造的)
# df5['attribute2'].fillna(b'{}', inplace=True)
# df5
# df5['attribute2'] = df5['attribute2'].apply(lambda item: json.loads(item))
# df5['attribute_merge'] = df5.apply(merge_attri_dict,
#     axis=1, result_type='reduce')
# df5

```




<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>product_id</th>
      <th>attribute1</th>
      <th>attribute2</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>1</td>
      <td>{'colour': 'red', 'size': (3, 4, 5)}</td>
      <td>NaN</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>{'colour2': 'blue', 'size': (1, 5, 1)}</td>
      <td>{'yy': 'YYY', 'yy2': 'YYY2'}</td>
    </tr>
    <tr>
      <th>2</th>
      <td>1</td>
      <td>{'colour3': 'black', 'size': (0.25, 1, 3)}</td>
      <td>{'zz': 'ZZZZ'}</td>
    </tr>
    <tr>
      <th>3</th>
      <td>2</td>
      <td>{'colour': 'blue', 'size': (1, 1, 1)}</td>
      <td>NaN</td>
    </tr>
    <tr>
      <th>4</th>
      <td>2</td>
      <td>{'colour1': 'white', 'size': (2, 0.5, 1)}</td>
      <td>{}</td>
    </tr>
  </tbody>
</table>
</div>






<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>product_id</th>
      <th>attribute1</th>
      <th>attribute2</th>
      <th>attribute_merge</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>1</td>
      <td>{'colour': 'red', 'size': (3, 4, 5)}</td>
      <td>{}</td>
      <td>{'colour': 'red', 'size': (3, 4, 5)}</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>{'colour2': 'blue', 'size': (1, 5, 1), 'yy': '...</td>
      <td>{'yy': 'YYY', 'yy2': 'YYY2'}</td>
      <td>{'colour2': 'blue', 'size': (1, 5, 1), 'yy': '...</td>
    </tr>
    <tr>
      <th>2</th>
      <td>1</td>
      <td>{'colour3': 'black', 'size': (0.25, 1, 3), 'zz...</td>
      <td>{'zz': 'ZZZZ'}</td>
      <td>{'colour3': 'black', 'size': (0.25, 1, 3), 'zz...</td>
    </tr>
    <tr>
      <th>3</th>
      <td>2</td>
      <td>{'colour': 'blue', 'size': (1, 1, 1)}</td>
      <td>{}</td>
      <td>{'colour': 'blue', 'size': (1, 1, 1)}</td>
    </tr>
    <tr>
      <th>4</th>
      <td>2</td>
      <td>{'colour1': 'white', 'size': (2, 0.5, 1)}</td>
      <td>{}</td>
      <td>{'colour1': 'white', 'size': (2, 0.5, 1)}</td>
    </tr>
  </tbody>
</table>
</div>




```python
s = df5['attribute2']
s.fillna?
print(s.dtype)


```

    object
    


```python
json.dumps?
```


```python


```


```python

```


```python

```


```python

```

# pandas实现between功能
使用pandas实现range join (between)的几种方法.
```
select
    *
from
    df_left as t1, df_right as t2
where
    t1.KEY = t2.KEY
    and t2.V between t1.MIN_V and t1.MAX_V
``` 
1. 使用sqlite的内存模式sqlite3.connect(':memory:'),并将dataframe写入表,再以sql between的方式进行关联查询.
优点: range join查询消耗的内存较少
缺点: 需要依赖内存数据库
参考: used_sql
2. 使用pandas dataframe的inner merge, 然后再通过df=df[(df['MIN_V'] <= df['V']) & (df['V'] <= df['MAX_V'])]的方式过虑.
缺点: inner merge可能会急剧膨胀,消耗大量内存
优点: 逻辑简单
参考: inner_and_filter
3. 将between判断逻辑转换成等于比较逻辑, 增加一个用于inner merge的字段.
    3.1 将df_left中的一行转成多行(每行取MIN_V~MAX_V中的一个值--列为为V), 构成new_df_left
    3.2 new_df_left.merge(df_right, how='inner', on=['KEY', 'V'])
优点: 使用内存少, 不依赖内存数据库
缺点: 只适用于value是离散值的情况(如整数适用, 浮点数不适用).
参考 add_join_column

```
import random
import sqlite3
import time
from functools import reduce

import pandas as pd
import numpy as np

# 显示所有列
from test_case.utils import Utils

pd.set_option('display.max_columns', None)
# 不限制宽度
pd.set_option('display.width', None)
# 显示所有行
pd.set_option('display.max_rows', None)


def build_data():
    # IS_FLAG True/False各N条
    N = 10000
    M = 2000
    df1 = pd.DataFrame(np.random.randn(N, 4), columns=list('ABCD'))
    df1['IS_FLAG'] = True

    df2 = pd.DataFrame(np.random.randn(N, 4), columns=list('ABCD'))
    df2['IS_FLAG'] = False

    df = pd.concat([df1, df2]).reset_index(drop=True)
    df['KEY'] = pd.Series([random.choice(['I', 'J', 'K']) for _ in range(df.shape[0])])
    df['IDX'] = list(range(df.shape[0]))
    choose = list(range(1, df.shape[0], 30))
    # df['MIN_SEQ'] = [random.choice(choose) for _ in range(df.shape[0])]
    df['MIN_SEQ'] = [random.randint(1, df.shape[0]) for _ in range(df.shape[0])]
    df['MAX_SEQ'] = df['MIN_SEQ'] + 4
    # print(df)

    df_right = pd.DataFrame(np.random.randn(3, 3), columns=list('OPQ'))
    df_right['KEY'] = pd.Series(['I', 'J', 'K'])
    df_right['SEQ'] = [list(range(1, M)) for _ in range(df_right.shape[0])]
    df_right = df_right.explode('SEQ')
    # print(df_right)
    return df, df_right


@Utils.elapsed_time
def used_sql(df_left, df_right) -> pd.DataFrame:
    conn = sqlite3.connect(':memory:')
    df_left.to_sql('df_left', conn, index=False)
    df_right.to_sql('df_right', conn, index=False)
    query_df = f'''
        select
            t1.IDX, t1.A, t1.B, t1.C, t1.D, t1.IS_FLAG, t1.KEY, t2.O, t2.P, t2.Q, t2.SEQ
        from
            df_left as t1, df_right as t2
        where
            t1.IS_FLAG = False
            and t1.KEY = t2.KEY
            and t2.SEQ between t1.MIN_SEQ and t1.MAX_SEQ
    '''
    s = time.time()
    res_df = pd.read_sql(query_df, conn)
    print('sql用时: ', time.time()-s)
    conn.close()
    # print(res_df)
    return res_df


@Utils.elapsed_time
def inner_and_filter(df_left: pd.DataFrame, df_right) -> pd.DataFrame:
    """先inner join再过滤"""
    df_left = df_left[~df_left['IS_FLAG']]
    df = df_left.merge(df_right, how='inner', on='KEY')
    df = df[(df['MIN_SEQ'] <= df['SEQ']) & (df['SEQ'] <= df['MAX_SEQ'])]
    # print(res_df)
    return df[['IDX', 'A', 'B', 'C', 'D', 'IS_FLAG', 'KEY', 'O', 'P', 'Q', 'SEQ']]



@Utils.elapsed_time
def add_join_column(df_left: pd.DataFrame, df_right) -> pd.DataFrame:
    df_left: pd.DataFrame = df_left[~df_left['IS_FLAG']].copy()
    df_left['SEQ'] = df_left.apply(lambda row: range(row['MIN_SEQ'], row['MAX_SEQ']+1),
            result_type='reduce', axis=1)
    df_left = df_left.explode(column='SEQ')
    s = time.time()
    df = df_left.merge(df_right, how='inner', on=['KEY', 'SEQ'])
    print('df.merge用时: ', time.time()-s)
    return df[['IDX', 'A', 'B', 'C', 'D', 'IS_FLAG', 'KEY', 'O', 'P', 'Q', 'SEQ']]


if __name__ == '__main__':
    df_left, df_right = build_data()
    df01: pd.DataFrame = used_sql(df_left, df_right)
    df02: pd.DataFrame = inner_and_filter(df_left, df_right)
    df03: pd.DataFrame = add_join_column(df_left, df_right)
    assert df01.shape == df02.shape
    assert df01.shape == df03.shape

    df01 = df01.sort_values(by=['KEY', 'SEQ']).reset_index(drop=True)
    df02 = df02.sort_values(by=['KEY', 'SEQ']).reset_index(drop=True)
    df03 = df03.sort_values(by=['KEY', 'SEQ']).reset_index(drop=True)
    df01['IS_FLAG'] = df01['IS_FLAG'].astype(bool)
    df02['SEQ'] = df02['SEQ'].astype(np.int64)
    df03['SEQ'] = df03['SEQ'].astype(np.int64)
    from pandas.testing import assert_frame_equal
    assert_frame_equal(df01, df02)
    assert_frame_equal(df01, df03)
```




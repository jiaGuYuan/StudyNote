# python-redis
# 连接redis
```
import time

from redis import StrictRedis, ConnectionPool

# 连接Redis
# redis = StrictRedis(host='localhost', port=6379, db=0, password=None)
# redis.set('name', 'GJY')
# print(redis.get('name'))

"""
创建Redis TCP连接: redis://[:password]@host:port/db
Redis TCP+SSL连接: rediss://[:password]@host:port/db
Redis UNIX socket连接: unix://[:password]@/path/to/socket.sock?db=db
"""
url = 'redis://@localhost:6379/0'
pool = ConnectionPool.from_url(url)
redis = StrictRedis(connection_pool=pool)
redis.set('name', 'GJY')
print(redis.get('name'))
redis.flushdb()  # 删除当前选择数据库中的所有键
# redis.flushall()  # 删除所有数据库中的所有键
```

# 键操作
```
redis.set('name', 'GJY')
assert bool(redis.exists('name')) is True  # 是否存在返回的是1/0
assert bool(redis.delete('name')) is True  # 是否成功删除返回的是1/0
assert bool(redis.exists('name')) is False
assert bool(redis.delete('name')) is False

redis.set('name', 'GJY')
redis.set('name_used_before', 'GJY2')
assert redis.type('name') == b'string'  # 注意结果的编码格式
assert redis.type('name') != 'string'
assert redis.keys('n*') == [b'name', b'name_used_before']

redis.rename('name_used_before', 'name_2')  # key不存在时会抛出异常

redis.dbsize()  # 获取当前数据库中key的数目

redis.expire('name_2', 2)  # 设置过期时间(单位秒)
time.sleep(2.1)
assert bool(redis.exists('name_2')) is False

redis.ttl('name')  # 获取key的(剩余)过期时间[-1:永不过期; -2:已经过期]


redis.set('name2', 'GJY2')
# redis.move('name2', 1)  # 将key移动到其它数据库
redis.flushdb()
assert redis.keys('*') == []
```

# 字符串操作 
Redis支持最基本的键值对形式存储
```
redis.flushdb()
redis.set('name', 'Bob')
assert redis.get('name') == b'Bob'  # key不存在时返回None

redis.getset('name2', 'gjy')  # 返回key上次的value,并为其设置新的值.

assert redis.mget(['name', 'name2']) == [b'Bob', b'gjy']  # 获取多个key对应的值

redis.setnx('newname', 'James')  # 如果key不存在则设置(返回True),如果key存在则保持(返回False).

redis.setex('name', 1, 'James')  # 设置key的值并指定有效期


# 设置指定key的value值的子字符串
redis.set('name', 'Hello')
redis.setrange('name', 6, 'World')  # 替换指定偏移位置的子串,返回新字符串的长度

redis.mset({'name1': 'Durant', 'name2': 'James'})  # 批量赋值
assert redis.mget(['name1', 'name2']) == [b'Durant', b'James']

redis.msetnx({'name3': 'Smith', 'name4': 'Curry'})  # 键都不存在时才批量赋值. 赋值时返回True/否则False


redis.incr('age', amount=1)  # key存在时递增key对应的值(返回递增后的值),key不存在时设置为amount并返回

redis.decr('age', amount=1)  # key存在时递减key对应的值(返回递减后的值),key不存在时设置为-amount并返回


redis.append('nickname', 'OK')  # 在key的值上附加value, key可以不存在

redis.substr('name', start=1, end=4)  # 返回key对应value的子串
redis.getrange('name', start=1, end=4)  # 返回key对应value的子串
```

# 列表操作 
Redis提供了列表存储，列表内的元素可以重复，而且可以从两端存储
```
redis.flushdb()
redis.rpush('list', 1, 2, 3)  # 在列表的尾部添加元素
redis.lpush('list', 0)  # 在列表的头部添加元素
redis.llen('list')  # 返回列表的长度
assert redis.lrange('list', start=0, end=-1) == [b'0', b'1', b'2', b'3']  # 返回索引范围[start, end]内的列表元素

redis.ltrim('list', 1, 3)  # 保留key为'list'的列表中索引为start到end的内容
assert redis.lrange('list', start=0, end=-1) == [b'1', b'2', b'3']

assert redis.lindex('list', index=1) == b'2'  # 返回key为'list'的列表中index位置的元素


redis.lset('list', index=1, value=5)  # 将列表中index位置的元素赋值,越界则报错
assert redis.lrange('list', start=0, end=-1) == [b'1', b'5', b'3']

redis.lrem('list', count=2, value=3)  # 最多删除列表中count个值为value的元素,返回删除的个数

assert redis.lpop('list') == b'1' # 返回并删除列表中的首元素
assert redis.rpop('list') == b'5' # 返回并删除列表中的尾元素

redis.flushdb()
redis.rpush('list', 1, 2, 3)
redis.rpush('list2', 11, 12, 13)

redis.blpop(keys='list', timeout=0)
redis.blpop(keys=['list', 'list2'], timeout=0)  # 返回并删除keys中首个非空list的首元素,如果所有列表为空则会阻塞等待(0表示一直阻塞)
redis.brpop(keys=['list', 'list2'], timeout=0)  # 返回并删除keys中首个非空list的尾元素, 如果所有列表为空则会阻塞等待

redis.rpoplpush(src='list', dst='list2')  # 返回并删除src列表的尾元素,并将该元素添加到dst列表头部

```

# 集合操作
 Redis提供了集合存储,集合中的元素都是不重复的
```
redis.flushdb()
redis.sadd('tags', 'Book', 'Tea', 'Coffee')  # 向key为'tags'的集合中添加元素
assert redis.scard('tags') == 3  # 返回key为'tags'的集合的元素个数
redis.smembers('tags')  # 返回键为name的集合的所有元素
assert redis.sismember('tags', value='Book') is True  # 测试value是否为指定集合的元素
redis.srem('tags', 'Book')  # 从集合中删除元素
assert redis.scard('tags') == 2
redis.spop('tags')  # 随机返回并删除集合中的一个元素
redis.srandmember('tags')  # 随机返回集合中的一个元素,但不删除元素

# 将集合src中的元素value移动到集合dst中. 如果正常进行了移动(value在src中)则返回True/否则返回False
redis.smove(src='tags', dst='tags2', value='Coffee')

redis.sadd('tags', 'Book', 'Tea', 'Coffee')
redis.sadd('tags2', 'Book', 'Tea2', 'Coffee')
assert redis.sinter(keys=['tags', 'tags2']) == {b'Coffee', b'Book'}  # 返回所有给定key的集合的交集
redis.sinterstore(dest='intertag', keys=['tags', 'tags2'])  # 求交集并将交集保存到指定key的集合

assert redis.sunion(keys=['tags', 'tags2']) == {b'Tea', b'Coffee', b'Tea2', b'Book'}  # 返回所有给定键的集合的并集
redis.sunionstore(dest='uniontag', keys=['tags', 'tags2'])  # 求并集并将并集保存到指定key的集合

assert redis.sdiff(['tags', 'tags2']) == {b'Tea'}  # 返回所有给定键的集合的差集
redis.sdiffstore('difftag', ['tags', 'tags2'])  # 求差集并将差集保存到指定key的集合

```

# 有序集合操作
有序集合比集合多了一个分数字段，利用它可以对集合中的数据进行排序
```
redis.flushdb()
redis.zadd('grade', {'Bob': 100, 'Mike': 98, 'ZhangSan': 95})  # 向zset中添加元素member,score用于排序.如果该元素存在,则更新其顺序
redis.zcard('grade')  # 返回指定zset的元素个数
redis.zcount('grade', min=80, max=99)  # 返回指定zset中score在给定区间[min, max]的数量

# 返回指定zset中score在给定区间[min, max]的元素, withscores:是否带score
redis.zrangebyscore(name='grade', min=80, max=99, withscores=False)

# 返回指定zset中score在给定区间[min, max]的元素并只取其索引在[start, start+num]的子元素
redis.zrangebyscore(name='grade', min=80, max=99, start=1, num=1, withscores=False)

# 删除指定zset中排名在给定区间[min, max]的元素
redis.zremrangebyrank('grade', min=0, max=0)

redis.zremrangebyscore('grade', min=80, max=99)  # 删除指定zset中score在给定区间[min, max]的元素

```

# 散列操作
Redis提供了散列表的数据结构,可以用name指定一个散列表的名称,表内存储了各个键值对
```
redis.flushdb()
redis.hset('price', 'cake', 5)  # 向指定的散列表中添加映射
redis.hset('price', mapping={'banana': 2, 'pear': 6})  # 批量向散列表中添加映射
redis.hvals('price')  # 从散列表中获取所有映射键值
assert redis.hgetall('price') == {b'cake': b'5', b'banana': b'2', b'pear': b'6'}  # 从散列表中获取所有映射键值对
redis.hsetnx('price', 'book', 6)  # 如果映射键名不存在,则向键为name的散列表中添加映射,如果映射已存在什么也不做
assert redis.hget('price', 'cake') == b'5'  # 返回指定散列表中key对应的值

assert redis.hmget('price', ['book', 'orange']) == [b'6', None]  # 获取散列表中的多个值
redis.hincrby('price', 'apple', amount=3)  # 将散列表中映射的值增加amount, 若映射键名不存在则首次调用将值设置为amount

assert redis.hexists('price', 'banana') is True  # 检查散列表中是否存在键名为键的映射
redis.hdel('price', 'banana')  # 从散列表中删除键名为键的映射
assert redis.hexists('price', 'banana') is False

redis.hlen('price')  # 获取散列表中获取映射个数
redis.hkeys('price')  #  获取散列表中获取所有映射键名
```

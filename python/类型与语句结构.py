#!/usr/bin/env python3
# -*- coding: utf-8 -*-     #只是说明性描述，必须要确保文本编辑器正在使用UTF-8 without BOM编码


# [x, y, z, ...]--list是可变对象， (x, y, z,.....)--tuple是不可变对象，
#set()--set，{x:vx, y:vy, z:vz, ...}--dict，

#dict
"""
Python内置了字典：dict的支持，dict全称dictionary，在其他语言中也称为map，
使用键-值（key-value）存储，具有极快的查找速度。
dict可以用在需要高速查找的很多地方，在Python代码中几乎无处不在，正确使用dict非常重要，
需要牢记的第一条就是dict的key必须是不可变对象。
d = {'Michael': 95, 'Bob': 75, 'Tracy': 85}
d['Michael'] #95

#要避免key不存在的错误，有两种办法，
一是通过in判断key是否存在
'Tracy' in d  ----True
二是通过dict提供的get方法，如果key不存在，可以返回None，
或者自己指定的value：
d.get('Thomas') 或 d.get('Thomas', -1)

#删除一个key，用pop(key)方法，对应的value也会从dict中删除：
d.pop('Bob')
"""


#set
"""
set和dict类似，也是一组key的集合，但不存储value。
由于key不能重复，所以，在set中，没有重复的key。
可以通过[]list来创建set---- s = set([1, 2, 3, 2]) 重复元素在set中自动被过滤：
通过add(key)方法可以添加元素到set中，可以重复添加，但不会有效果
通过remove(key)方法可以删除元素：
et可以看成数学意义上的无序和无重复元素的集合，因此，两个set可以做数学意义上的交集、并集等操作：
s1 = set([1, 2, 3])
s2 = set([2, 3, 4])
s1 & s2   #{2, 3}
s1 | s2     #{1, 2, 3, 4}
"""

#不变对象
"""
在Python中，字符串、整数等都是不可变的，因此，可以放心地作为key。而list是可变的，就不能作为key：
对于不变对象来说，调用对象自身的任意方法，也不会改变该对象自身的内容。
相反，这些方法会创建新的对象并返回，这样，就保证了不可变对象本身永远是不可变的。
"""




#for
# for x in ...循环就是把每个元素代入变量x，然后执行缩进块的语句
#Python提供一个range()函数，可以生成一个整数序列，再通过list()函数可以转换为list。
list(range(5)) #[0, 1, 2, 3, 4]

#while
sum = 0
n = 0
while n < 10:
    sum = sum + n
    n = n - 1
print(sum)

#break 与 continue

#input
"""
input()返回的数据类型是str，str不能直接和整数比较，必须先把str转换成整数。
Python提供了int()函数来完成这件事情：
birth = input('birth: ')
if birth < 2000:
    print('00前')
else:
    print('00后')
"""

#if 语句格式
"""
if <条件判断1>:
    <执行1>
elif <条件判断2>:
    <执行2>
elif <条件判断3>:
    <执行3>
else:
    <执行4>
"""

#list
classmates = ['Michael', 'Bob', 'Tracy']
len(classmates)
classmates[0]
classmates.append('Adam')
classmates.insert(1, 'Jack')
classmates.pop()
classmates[1] = 'Sarah'
p = ['asp', 'php']
classmates.append(p)
 

#tuple  元组tuple一旦初始化就不能修改
classmates = ('Michael', 'Bob', 'Tracy')

exit()


#由于Python的字符串类型是str，在内存中以Unicode表示，
#一个字符对应若干个字节。如果要在网络上传输，或者保存到磁盘上，就需要把str变为以字节为单位的bytes。
#Python对bytes类型的数据用带b前缀的单引号或双引号表示：
x = b'ABC'
#以Unicode表示的str通过encode()方法可以编码为指定的bytes
'ABC'.encode('ascii')
'中文'.encode('utf-8')

#要把bytes变为str，就需要用decode()方法：
b'ABC'.decode('ascii')

#要计算str包含多少个字符，可以用len()函数：(注意是计算字符数)
len('ABC')  #3
len('中文') #2



#除法 10/3, 10//3
exit()

a = 'abc'
#Python解释器干了两件事情：
#1.在内存中创建了一个'ABC'的字符串；
#2.在内存中创建了一个名为a的变量，并把它指向'abc'。

a = 'ABC'
b = a
a = 'XYZ'
print(b) #输出的是'ABC'还是'XYZ'


#变量本身类型不固定
a = 123    # a是整数
print(a) 
a = 'asg'   # a变为字符串
print(a)
exit()


name = input('请输入名字:')
print('输入的名字是:\'', name)
#  字符串的表示'xx' "xxx" "x'xx" "x\'x\"xx" 'x\'x\"xx' 
#避免转义 r'xxx\\x'
#'''...'''的格式表示多行

print(r'''如果字符串里面有很多字符都需要转义，
就需要加很多\，
为了简化，
Python还允许用r'',
表示''内部的字符串默认不转义，
可以自己试试：''')

#以缩进方式来组织
if name>'a':
    print('大于a')
else :
    print('小于a')

#exit()



#回顾
"""
python 变量是动态类型，变量类型因值而异，变量名区分大小写。

单行字符串 '...'，"..."两种方式表示
#多行字符串'''...'''表示
 转义字符与C基本相同
 r'...'不使用转义表示

 布尔值：True,False----and,or,not运算
 空值：None

 python变量赋值的理解：
 a = 'ABC' #@1
 b = a
 对于a = 'ABC' ：python解释器做了两件事
 1.在内存中创建了一个'ABC'的字符串；
 2. 在内存中创建了一个名为a的变量，并把它指向'ABC'。
 执行b = a，解释器创建了变量b，并把b指向a指向的字符串'ABC'

 字符串与编码
 在计算机内存中，统一使用Unicode编码，当需要保存到硬盘或者需要传输的时候，就转换为UTF-8编码
 很多网页上的源码中有<meta charset="utf-8" />的信息，表示该网页正是用的UTF-8编码
 ord(str)   chr(xxx)
 b'str'          str.encode()         b'str'.decode('ascii')  len(str)
 格式化:基本同C.  如'%d' % 3---第二个%用于分隔格式化串与实际值


 列表: list---list是一种有序的集合，可以随时添加和删除其中的元素
 classmates = ['Michael', 'Bob', 'Tracy']
 len(classmates)
 classmates[i]
 classmates.append('Adam')
 classmates.insert(i, 'Jack') #在原list的第i个位置插入,原来的元素后移
 classmates.pop(i) #无参时删除末尾元素
 classmates[i] = 'Sarah' #修改
 list里面的元素的数据类型也可以不同
  p = ['asp', 'php']
  classmates[i] = p
  list类型与C++的List，可以通过下标访问，当其元素为

  元组：tuple -- 和list非常类似，但是tuple一旦初始化就不能修改
  classmates = ('Michael', 'Bob', 'Tracy')
  classmates[0] = 'abc' #错误

   t = ('a', 'b', ['A', 'B'])
   t[2][0] = 'X' #正确，说明：tuple不可变类型，这里t的第三个元素是list类型，我们改变的是
                     #t[2][0]所指向的元素， t[2]并没有改变（即tuple的list元素并没有改变）
    对tuple元素不能修改的理解是--指向不变。

    对python元素的理解是----C/C++中的高级指针，变量赋值是改变指针的指向


    字典：dict ---使用键-值（key-value）存储，具有极快的查找速度(类比C++的map)
    牢记:dict的key必须是不可变对象。
    
    #一个key只能对应一个value
    d = {key:value, 'Michael': 95, 'Bob': 75, 'Tracy': 85}
    
    d['Michael']
    d['Adam'] = 67 #会在dict中增加一个元素

    #如果key不存在，dict就会报错：
    避免key不存在的错误，有两种办法，一是通过in判断key是否存在：
    二是通过dict提供的get方法，如果key不存在，可以返回None，或者自己指定的value
     'Thomas' in d
     d.get(key) 或 d.get(key, value)
     
     pop(key) #删除

     集合:set--一组key的集合(类比C++的set)
     在set中，没有重复的key
     s = set([key, 1, 2, 3])
     add(key)
     remove(key)
     set可以看成数学意义上的无序和无重复元素的集合，因此，两个set可以做数学意义上的交集、并集等操作

对于不变对象来说，调用对象自身的任意方法，也不会改变该对象自身的内容。
这些方法会创建新的对象并返回，这样就保证了不可变对象本身永远是不可变的。     
"""

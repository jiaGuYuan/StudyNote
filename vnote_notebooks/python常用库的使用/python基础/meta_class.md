# python

## 元类 MetaClass
动态语言和静态语言最大的不同，就是函数和类的定义，不是编译时定义的，而是运行时动态创建的。
动态语言中创建实例的过程：
先定义metaclass ==> 创建类 ==> 创建实例
你可以把类看成是metaclass创建出来的"实例" (即metaclass为类的类).

一个类MyClass在被加载时,会依次调用其metaclass的__new__和__init__, 完成该类的创建,并记录下来.

默认情况下在实例化MyClass类生成对象时,会依次调用MyClass的__new__和__init__; 但需要注意的是这个过程是由MyClass的metaclass的__call__方法所定义的--即实例化类对象时,实际上调用的是metaclass.__call__, 在meta.__call__中调用了类的__new__和__init__.
所以在实现单例模式时,可以通过覆盖metaclass的__call__方法, 来控制对__new__和__init__的多次调用.

![](vx_images/143200716236627.png =1000x600)

```python
class MyMetaClass(type):
    _instances = {}

    def __new__(cls, *args, **kwargs):
        print(cls, '__new__', *args, **kwargs)
        return super().__new__(cls, *args, **kwargs)

    def __init__(self, *args, **kwargs):
        print(self.__class__, '__init__')

    # def __call__(self, *args, **kwargs):
    #     # self: MyMetaClass实例--类
    #     print(self, self.__class__, '__call__')
    #     return super().__call__(*args, **kwargs)

    def __call__(self, *args, **kwargs):
        # self: MyMetaClass实例--对应一个类
        print(self, self.__class__, '__call__')
        if self not in self._instances:  # self._instances即MyMetaClass._instances
            self._instances[self] = super().__call__(*args, **kwargs)
        return self._instances[self]


class MyClass(metaclass=MyMetaClass):

    def __new__(cls, *args, **kwargs):
        print(cls,  '__new__')
        return super().__new__(cls, *args, **kwargs)

    def __init__(self):
        print(self.__class__, '__init__')

    def __call__(self, *args, **kwargs):
        print(self, '... call ...')


class MyClass2(metaclass=MyMetaClass):
    pass


inst = MyClass()
inst2 = MyClass()
print(id(inst), id(inst2))

```
也可以不使用懒单例; 而是将惟一实例的创建放在MetaClass的__new__中; 只是这样无论如何都将创建单例.
# python对接cpp示例
示例: 
    在python代码"example.py"中调用cpp代码"Rectangle.cpp"提供的功能。
    Note: "rect.pyx"充当python与cpp的桥梁.将cpp的接口暴露给python,供python使用,需要转换成.pyd文件.
    涉及文件:
        源文件:
            cpp: Rectangle.cpp, Rectangle.h # 提供cpp实现的功能
            pythoh: example.py # 演示如何以python方式调用cpp提供的功能
            rect.pyx # **.pyx文件充当python与cpp的桥梁.将cpp的接口暴露给python,供python使用. 需要转换成.pyd文件**
        .pyx转.pyd:
            rect.cpp # 中间文件
            rect_module.cp37-win_amd64.pyd # 生成的.pyd文件(rect_module由setup.py中Extension的name参数而来)

## 将.pyx转换成.pyd文件
命令行执行:` python setup.py build_ext --inplace`
其中"setup.py"文件的写法如下:
```python setup.py

from distutils.core import setup
from setuptools import Extension
from Cython.Build import cythonize
# import numpy as np

# "python setup.py build_ext --inplace" 将.pyx文件转换成.pyd文件.

ext_modules = [
    Extension(
        name='rect_module', # 对应生成的pyd文件名(python中使用Extension导包需要使用该名 from rect_module as rect)
        sources=[
            'rect.pyx',
            'Rectangle.cpp'
        ],
        include_dirs=[
            # np.get_include(), 
            './'
        ],
        library_dirs=[],
        libraries=[],
        language='c++'
    )
]

setup(
    name= 'demo',
    packsges=['demo'],
    ext_modules = cythonize(ext_modules)
)
```

## python调用CPP代码
示例: example.py
```python
# 示例: 通过python调用cpp中的代码
import rect_module as rect # rect_module对应setup.py中Extension的name参数

x0, y0, x1, y1 = 1, 2, 3, 4
rect_obj = rect.PyRectangle(x0, y0, x1, y1) # PyRectangle是.pyx文件中对CPP类的包装
print(dir(rect_obj))
print(rect_obj.getArea())
```

## C++代码如下
源文件: Rectangle.cpp
```cpp
#include "Rectangle.h"
using namespace shapes;
Rectangle::Rectangle(int X0, int Y0, int X1, int Y1){
    x0 = X0;
    y0 = Y0;
    x1 = X1;
    y1 = Y1;
}
Rectangle::~Rectangle() {}
int Rectangle::getLength() {
    return (x1 - x0);
}
int Rectangle::getHeight() {
    return (y1 - y0);
}
int Rectangle::getArea() {
    return (x1 - x0) * (y1 - y0);
}
void Rectangle::move(int dx, int dy) {
    x0 += dx;
    y0 += dy;
    x1 += dx;
    y1 += dy;
}
```
头文件: Rectangle.h
```cpp
namespace shapes {
    class Rectangle {
        public:
            int x0, y0, x1, y1;
            Rectangle(int x0, int y0, int x1, int y1);
            ~Rectangle();
            int getLength();
            int getHeight();
            int getArea();
            void move(int dx, int dy);
    };
 }
```

## .pyx文件写法
```pyx
# distutils: language = c++
# distutils: sources = Rectangle.cpp
# cython: language_level = 3 # cython版本
# 上面的设置不可缺少.

# .pyx文件充当python与cpp的桥梁.将cpp的接口暴露给python,供python使用.
# .pyx文件需要转换成.pyd文件才能真正的提供给python调用cpp的能力.

cdef extern from "Rectangle.h" namespace "shapes":
    cdef cppclass Rectangle:
        Rectangle(int, int, int, int)
        int x0, y0, x1, y1
        int getLength()
        int getHeight()
        int getArea()
        void move(int, int)

cdef class PyRectangle:
    cdef Rectangle *thisptr      # hold a C++ instance which we're wrapping
    def __cinit__(self, int x0, int y0, int x1, int y1):
        self.thisptr = new Rectangle(x0, y0, x1, y1)
    def __dealloc__(self):
        del self.thisptr
    def getLength(self):
        return self.thisptr.getLength()
    def getHeight(self):
        return self.thisptr.getHeight()
    def getArea(self):
        return self.thisptr.getArea()
    def move(self, dx, dy):
        self.thisptr.move(dx, dy)
```

生成.pyd文件后，可以没有.pyx文件。

## 完成示例代码
[python_call_cpp_002.rar](images_attachments/20210219161411738_7730/python_call_cpp_002.rar)

## 参考
[知乎](https://zhuanlan.zhihu.com/p/25077636)

[stack overflow](https://stackoverflow.com/questions/18154361/cython-c-example-fails-to-recognize-c-why)

[官网](http://docs.cython.org/en/latest/src/userguide/wrapping_CPlusPlus.html)

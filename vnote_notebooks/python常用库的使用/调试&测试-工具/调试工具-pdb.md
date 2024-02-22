# pdb
在代码中设置断点: `breakpoint()`, 此时直接以python执行`python example.py`也会进入Pdb并停在断点处(不需要以pdb的方式启动)

## Pdb基本命令
* 以pdb的方式运行代码: `python -m pdb example.py`
* 查看变量: `p varName`
* 查看调用栈: `w`
* 查看当前位置附近的源代码: `l`(会显示前后5行), `ll`(会显示当前函数)
* 改变当前帧: `u | up`(上移当前帧), `d | down`(下移当前帧)
* 单步: `n`,  单步进入函数: `s | step`
* 执行到指定的位置: 
    + `until`(运行到下一行,在循环尾部时将跳出循环), 
    + `until rowNumber`(运行到指定的行) 
    + `r | return`(运行到函数返回位置)
    + `c`(继续执行,直接遇到下一个断点)
* 可在pdb中执行任意python代码(如改变变量的值)
* 断点:
    + 设置断点: `b rowNumber`(在指定行设置断点), `b pkg.funcName`(在指定函数入口设置断点)
    + 查看所有断点: `b | break`
    + 删除断点: `clear`(删除所有断点), `clear breakpointNumber`(删除指定断点)
* 退出: `q | quit`
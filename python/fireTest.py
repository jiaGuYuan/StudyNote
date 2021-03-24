import fire

# Fire可将任何Python组件转变为命令行界面

class FireTestTask(object):

    def __init__(self, *args, **kwargs):
        print('start __init__')
        print('args:', args)
        print('kwargs:', kwargs)
        print('end __init__\n\n')
    
    @staticmethod
    def static_method(*args, **kwargs):
        print('start static_method')
        print('args:', args)
        print('kwargs:', kwargs)
        print('end static_method\n\n')

    
    def obj_method(self, *args, **kwargs):
        print('start obj_method_test')
        print('args:', args)
        print('kwargs:', kwargs)
        print('end obj_method_test\n\n')


def global_func(*args, **kwargs):
    print('start global_func')
    print('args:', args)
    print('kwargs:', kwargs)
    print('end global_func\n\n')
    
def args_type_func(pos_arg, *args, **kwargs):
    print('start args_type_func')
    print('pos_arg:', pos_arg, ', type:', type(pos_arg))
    for i,arg in enumerate(args):
        print('arg{}:'.format(i), arg, ', type:', type(arg))
    for key,value in kwargs.items():
        print(key, ':', value, ', value_type:', type(value))
    print('end args_type_func\n\n')


if __name__ == "__main__":
    # #********************Note: fire.Fire()*********************** #
    # # fire.Fire()会将程序的全部内容暴露给命令行. 
    # # 格式: fireTest.py COMMAND VALUE [ - COMMAND VALUE [- COMMAND VALUE [...]] ]
    # #       COMMAND对应模块可见的类或函数．
    # #       VALUE对应类的构造参数或函数的函数--非关键字参数直接指定,关键字参数使用'--key=value'的方式指定．
    # #       Note!: 当要调用类的方法时可以使用嵌套的'- COMMAND VALUE'方式
    # # Eg调用global_func: fireTest.py global_func gf_pos_arg1 --gf_key1=value1
    # # Eg调用FireTestTask.obj_method: fireTest.py FireTestTask init_pos_arg1 --init_key1=value1 - obj_method om_pos_arg1 --om_key1=value1
    # # Eg调用FireTestTask.static_method: fireTest.py FireTestTask init_pos_arg1 --init_key1=value1 - static_method sm_pos_arg1 --sm_key1=value1
    fire.Fire()

    # #*********************fire.Fire(<fn>)********************** #
    # # fire.Fire(<fn>) 只将指定函数暴露给命令行
    # # Eg: fireTest.py gf_pos_arg1 --gf_key1=value1
    # fire.Fire(global_func)
    
    # #*********************fire.Fire(<dict>)********************** #
    # # fire.Fire(<dict>) 通过使用字典, 我们可以有选择性地将一些函数暴露给命令行.
    # # Eg: fireTest.py global_f gf_pos_arg1 --gf_key1=value1
    # fire.Fire({
      # 'global_f': global_func,
      # 'args_type_f': args_type_func
    # })
    
    # #***********************fire.Fire(<object>)******************** #
    # # fire.Fire(<object>) 将对象的全部内容暴露给命令行,这是暴露多个命令的一个好的做法.
    # # Eg调用object.obj_method: fireTest.py obj_method om_pos_arg1 --om_key1=value1
    # ftt = FireTestTask()
    # fire.Fire(ftt)
    
    # #*************************Note: fire.Fire(<class>)****************** #
    # # fire.Fire(<class>) 将类的全部内容暴露给命令行(可以传递构造参数),这是暴露多个命令的一个更好的做法.
    # # Eg调用FireTestTask.obj_method: fireTest.py init_pos_arg1 --init_key1=value1 - obj_method om_pos_arg1 --om_key1=value1
    # fire.Fire(FireTestTask)

    # #*************************Note: 参数解析****************** #
    # # 参数解析: 参数的类型取决于它们的值,而不是使用它们的函数签名;可以从命令行传递任何Python文本: 数字,字符串,元组,列表,字典(仅在某些版本的Python中支持集合).
    # #           Note: 参数内部有空格时使用双引号包裹; 单引号包裹会被做为字符串处理．
    # # Eg 传递数字: python fireTest.py 0 "0" --k1=0 --k2=1.0
    # # Eg 传递字符串: python fireTest.py abc "'0'" --k1=abc
    # #                python fireTest.py '(1,2)' "'(1, 2)'" --k1="'(1, 2)'"
    # # Eg 传递元组: python fireTest.py (1,2) "(1, 2)" --k1="(1, 2)"
    # # Eg 传递列表: python fireTest.py [1,2] "[1, 2]" --k1="[1, 2]"
    # # Eg 传递字典: python fireTest.py "{'k1':0, 'k2':1}"  --k1="{1:1, 2:2}"
    # fire.Fire(args_type_func)



    
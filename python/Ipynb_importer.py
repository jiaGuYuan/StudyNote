# 该文件提供的功能:使得我们可以调用Jupyter Notebooks中的函数.
# Notebook文件是Json格式的文件而不是正常的py文件,无法直接import导入使用.
# 该方法的本质就是使用挂钩来进行加载; 先使用jupyter解析器先对'.ipynb'文件进行解析,把文件内的各个模块加载到内存里供其他python文件调用
# 示例: 如果我们调用'xxx/A.ipynb'的foo函数;我们需要做如下三步
# 1. 拷贝该文件('Ipynb_importer.py')到工作目录
# 2. import Ipynb_importer # 导入Ipynb_importer后就可以import其它Jupyter文件中的函数了
# 3. 从'A.ipynb'中导入模块.-- from xxx.A import foo (建议使用from xxx import xxx只导入需要的名称)

import io, os,sys,types
from IPython import get_ipython
from nbformat import read
from IPython.core.interactiveshell import InteractiveShell

# 查找指定模块对应的'.ipynb'文件是否存在
def find_notebook(fullname, path=None):
    """找一个JupyterNotebook文件,给定它的完全限定名和可选路径.
    示例: find_notebook('A.B_C', path='./wsk')会先查找'./wsk/A/B_C.ipynb',
    如果'./wsk/A/B_C.ipynb'不存在将查找'./wsk/A/B C.ipynb'
    """
    name = fullname.rsplit('.', 1)[-1]
    if not path:
        path = ['']
    for d in path:
        nb_path = os.path.join(d, name + ".ipynb")
        if os.path.isfile(nb_path):
            return nb_path
        nb_path = nb_path.replace("_", " ")
        if os.path.isfile(nb_path):
            return nb_path

class NotebookLoader(object):
    """本地Jupyter Notebooks模块加载器"""
    def __init__(self, path=None):
        self.shell = InteractiveShell.instance()
        self.path = path

    def load_module(self, fullname):
        """导入一个Notebook作为模块"""
        path = find_notebook(fullname, self.path)
        print ("importing Jupyter notebook from '%s'" % path)

        # 加载笔记本对象
        with io.open(path, 'r', encoding='utf-8') as f:
            nb = read(f, 4)
            
        # 创建模块并将其添加到sys.modules中
        # if fullname in sys.modules:
            # return sys.modules[fullname]
        mod = types.ModuleType(fullname)
        mod.__file__ = path
        mod.__loader__ = self
        mod.__dict__['get_ipython'] = get_ipython
        sys.modules[fullname] = mod

        # extra work to ensure that magics that would affect the user_ns
        # actually affect the notebook module's ns
        save_user_ns = self.shell.user_ns
        self.shell.user_ns = mod.__dict__

        try:
          for cell in nb.cells:
            if cell.cell_type == 'code':
                # 将输入转换为可执行的Python
                code = self.shell.input_transformer_manager.transform_cell(cell.source)
                # 在模块中运行的代码
                exec(code, mod.__dict__)
        finally:
            self.shell.user_ns = save_user_ns
        return mod
        

class NotebookFinder(object):
    """本地Jupyter Notebooks模块查找器"""
    def __init__(self):
        self.loaders = {}

    def find_module(self, fullname, path=None):
        nb_path = find_notebook(fullname, path)
        if not nb_path:
            return
        print("{} 模块对应的ipynb文件为 '{}'".format(fullname, nb_path))
        
        key = path
        if path:
            # lists aren't hashable
            key = os.path.sep.join(path)

        if key not in self.loaders:
            self.loaders[key] = NotebookLoader(path)
        return self.loaders[key]


# 注册钩子, 在sys.meta_path中注册我们的NotebookFinder
g_finder = NotebookFinder()
sys.meta_path.append(g_finder)
print('钩子注册成功,可以正常从JupyterNotebook中导入函数了')


# 通过以上方法,就可以通过'import otherNotebookName'导入otherNotebookName中的函数了
# 但如果otherNotebookName中的函数发生了变更,需要重启当前Notebook文件.
# 这个函数使的不用重启Notebook文件,就能使用变更
# 使用方法: moduleFileName = Ipynb_importer.reload_module('moduleFileName')
#           moduleFileName.test_oof()
# TODO: 还只是勉强能用,建议只用于调试.
def reload_module(fullname, path=None):
    global g_finder
    nb_path = find_notebook(fullname, path)
    if not nb_path:
        print('{} 对应的Jupyter文件不存在'.format(fullname))
        return
            
    key = path
    if path:
        key = os.path.sep.join(path)
    if key in g_finder.loaders:
        if fullname in sys.modules:
            del sys.modules[fullname]
            print('reload_module fullname:', fullname)
            import importlib
            mod = importlib.import_module(fullname)
            return mod
    
    import importlib
    mod = importlib.import_module(fullname)
    return mod
            





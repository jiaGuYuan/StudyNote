import numpy as np
import matplotlib
# matplotlib.use('TkAgg') # 修改matplotlib的后端
import matplotlib.pyplot
from matplotlib.widgets import RectangleSelector
from matplotlib.backend_bases import MouseButton
import math
import random

def pyplot_pause(plot, interval, is_use_plt_pause=False):
    """
    在交互式matplotlib中,每次更新时窗口都会弹出到前面; 导致其他软件无法正常使用(如VSCode无法进行编辑)。
    使用该函数代替plt.pause()可以保持matplotlib交互时更新图像的Z顺序.
    该函数要求：首先处于交互模式plt.ion()，然后至少调用一次plt.show()。
    参考: https://codingdict.com/questions/173381
    """
    if is_use_plt_pause:
        plot.pause(interval)
    else:
        backend = plot.rcParams['backend']
        if backend in matplotlib.rcsetup.interactive_bk:
            figManager = matplotlib._pylab_helpers.Gcf.get_active()
            if figManager is not None:
                canvas = figManager.canvas
                if canvas.figure.stale:
                    canvas.draw()
                canvas.start_event_loop(interval)
                
            
class Point(object):
    def __init__(self, x=-1, y=-1):
        self.x = math.ceil(x)
        self.y = math.ceil(y)

    def __bool__(self):
        return self.x >= 0 and self.y >= 0

    def __repr__(self):
        return f"({self.x}, {self.y})"

    def __str__(self):
        return self.__repr__()

    def __add__(self, other):
        return Point(self.x+other.x, self.y+other.y)

    def __eq__(self, other):
        if not isinstance(other, Point):
            return False
        return self.x == other.x and self.y == other.y

    def __hash__(self):
        return hash(f"({self.x}, {self.y})")

    def get_coords(self):
        return (self.x, self.y)


class Grid(object):
    def __init__(self, width, height, fullvalue=0):
        self.fullvalue = fullvalue
        self.width, self.height = math.ceil(width), math.ceil(height)
        if fullvalue == 0:
            self.grid = np.zeros([self.height, self.width], dtype=int)
        else:
            self.grid = np.full([self.height, self.width], fullvalue)

    def set_grid(self, np_array2d, fullvalue=0):
        self.fullvalue = fullvalue
        self.grid = np_array2d
        self.width = np_array2d.shape[1]
        self.height = np_array2d.shape[0]

    def __getitem__(self, point:Point):
        x,y = point.x, point.y
        return self.grid[y][x]

    def __setitem__(self, key:Point, value):
        x,y = key.x, key.y
        self.grid[y][x] = value

    # 判断point是否在Grid内部
    def is_grid_inner(self, point:Point):
        if point.x >=0 and point.x <= self.width-1 \
           and point.y >= 0 and point.y <= self.height-1:
           return True
        else:
            return False

    # point点的值是否为默认填充值
    def point_is_fullvalue(self, point:Point):
        if not self.is_grid_inner(point):
            return False
        return self[point] == self.fullvalue

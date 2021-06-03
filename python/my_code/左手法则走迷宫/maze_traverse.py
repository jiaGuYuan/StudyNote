from Utils import Point, Grid, pyplot_pause
from ordered_set import OrderedSet
import numpy as np


## 左手法则走迷宫
# 当左边没墙时，就向左转，然后向前走一步；
# 当左边有墙，前面没墙就向前走一步；
# 当左边有墙，前面也有墙就向右转。
class Walker(object):
    RIGHT, DOWN, LEFT, UP = 0, 1, 2, 3
    # 使用绘图坐标(即原点在左上角,X轴向右,Y轴向下)
    def __init__(self, start_point:Point, end_point:Point, direction, grid_map:Grid):
        self.point = start_point
        self.end_point = end_point
        self.direction = direction
        self.grid_map = grid_map
        self.route_path = []
 
    # 向左转
    def turn_left(self):
        self.direction = (self.direction + 4 - 1) % 4

    # 向右转
    def turn_right(self):    
        self.direction = (self.direction + 1) % 4

    # 获取左侧点
    def get_left_point(self):
        point = Point(self.point.x, self.point.y)
        if self.direction == Walker.RIGHT:
            point.y -= 1
        elif self.direction == Walker.DOWN:
            point.x += 1
        elif self.direction == Walker.LEFT:
            point.y += 1
        elif self.direction == Walker.UP:
            point.x -= 1
        return point

    # 获取行走方向前面的点
    def get_forward_point(self):
        point = Point(self.point.x, self.point.y)
        if self.direction == Walker.RIGHT:
            point.x += 1
        elif self.direction == Walker.DOWN:
            point.y += 1
        elif self.direction == Walker.LEFT:
            point.x -= 1
        elif self.direction == Walker.UP:
            point.y -= 1
        return point

    # 向前走一步 
    def step_forward(self):
        self.point = self.get_forward_point()
        return self.point
 
    # 左侧位置是否为墙
    def is_wall_on_left(self):
        left_point = self.get_left_point()
        if not self.grid_map.is_grid_inner(left_point): # 左侧点在边界外
            return True
        if self.grid_map[left_point] == 0: # 0 表示墙
            return True
        return False

    # 前面位置是否为墙
    def is_wall_on_front(self):
        forward_point = self.get_forward_point()
        if not self.grid_map.is_grid_inner(forward_point):
            return True
        if self.grid_map[forward_point] == 0: 
            return True
        return False

    # 使用左手法则走迷宫
    def maze_traverse(self):
        loop_count = self.grid_map.width * self.grid_map.height # 最大次数
        while loop_count>0:
            loop_count -= 1
            if self.point == self.end_point:
                return self.route_path
            if not self.is_wall_on_left() : # 左边没墙
                # 向左转，然后向前一步
                self.turn_left() 
                self.route_path.append(self.point)
                self.step_forward()
            else : # 左边有墙
                if not self.is_wall_on_front() : # 前面没墙
                    self.route_path.append(self.point)
                    self.step_forward()  # 直接向前一步
                else : 
                    self.turn_right() # 右转
        return self.route_path
    
                    
if __name__ == "__main__":
    np_array2d = np.loadtxt("./grid_map.csv", dtype=int, delimiter=',')
    print(np_array2d)

    width, height = np_array2d.shape[1], np_array2d.shape[0]
    grid = Grid(width, height, fullvalue=0)
    grid.set_grid(np_array2d)
    wer = Walker(Point(0, 0), Point(width-1, height-1), Walker.DOWN, grid)
    route_path = wer.maze_traverse()
    print(route_path)
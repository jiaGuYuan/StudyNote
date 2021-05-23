from enum import Enum
from typing import List
from PySide2.QtCore import QPointF

from queue import Queue
from typing import Dict
import math

class AnchorType(Enum):
    CORNER = 0 # 角
    EDGE = 1   # 边
    DRIFT = 2  # 浮动


class Anchor:
    def __init__(self, point=QPointF(-1, -1), _type:AnchorType=None, area_key=None):
        self.point = point # 锚点坐标
        self.type = _type # 锚点类型
        self.reside_area_key = area_key # 记录锚点所属的区域key (None不属于任何区域)
        self.is_related = False # 是否已与其他锚点关联

    def move(self, offser_point:QPointF):
        self.point.setX(self.point.x() + offser_point.x())
        self.point.setY(self.point.y() + offser_point.y())

    # 是否是有效的锚点
    def is_valid(self):
        return self.type is not None

    # 完成关联
    def finish_related(self):
        self.is_related = True

    # 记录锚点所属区域
    def set_reside_area_key(self, area_key):
        self.reside_area_key = area_key

class AnchorRelation:
    def __init__(self, to_anchor:Anchor, from_anchor:Anchor):
        self.to_anchor = to_anchor
        self.from_anchor = from_anchor
        self.to_anchor.finish_related()
        self.from_anchor.finish_related()

    def __str__(self):
        from_pos = self.from_anchor.point
        to_pos = self.to_anchor.point
        return f"({from_pos.x()}, {from_pos.y()}) --> ({to_pos.x(), to_pos.y()})" 

class Area:
    def __init__(self, vertex:List[Anchor], anchors:List[Anchor], area_key=None):
        self.vertex = vertex # 顶点集
        self.anchors = anchors # 锚点集
        for anchor in anchors:
            anchor.set_reside_area_key(area_key)


class Utils:
    # 计算距离
    @staticmethod
    def calc_euclidean_distance(point1: QPointF, point2: QPointF):
        return math.sqrt((point1.x() - point2.x()) ** 2 + (point1.y() - point2.y()) ** 2)

    # 从anchors找出与anchor距离最近的锚点
    @staticmethod
    def get_min_distance_idx_by_anchors(anchor:Anchor, anchors:List[Anchor]):
        min_dist = float("inf")
        min_dist_anchor = Anchor() # 距离最近的锚点(初始化为无效的锚点)
        for tmp_anchor in anchors: 
            # 只比较同类型且未构建关联的锚点
            if (anchor.type == tmp_anchor.type) and  (not tmp_anchor.is_related):
                dist = Utils.calc_euclidean_distance(anchor.point, tmp_anchor.point)
                if dist < min_dist:
                    min_dist, min_dist_anchor = dist, tmp_anchor
        return min_dist_anchor

    # 从areas找出与anchor距离最近的锚点
    @staticmethod
    def get_min_distance_idx_by_areas(anchor:Anchor, areas:Dict[int, Area]):
        min_dist = float("inf")
        min_dist_anchor = Anchor() # 距离最近的锚点(初始化为无效的锚点)
        for area_key, area in areas.items(): 
            # 只在'非anchor归属'的区域中，比较同类型且未构建关联的锚点
            if area_key == anchor.reside_area_key:
                continue

            for tmp_anchor in area.anchors:
                if (anchor.type == tmp_anchor.type) and  (not tmp_anchor.is_related):
                    dist = Utils.calc_euclidean_distance(anchor.point, tmp_anchor.point)
                    if dist < min_dist:
                        min_dist = dist
                        min_dist_anchor = tmp_anchor
        return min_dist_anchor
                

class AreaCollections:
    
            
    def __init__(self, width, height):
        self.width = width
        self.height = height
        # 固定的锚点
        self.fixed_anchors = [
            Anchor(QPointF(0, 0), AnchorType.CORNER), 
            Anchor(QPointF(self.width-1, 0), AnchorType.CORNER), 
            Anchor(QPointF(self.width-1, self.height-1), AnchorType.CORNER), 
            Anchor(QPointF(0, self.height-1), AnchorType.CORNER)
        ]

        self.area_map = {
            1: Area([QPointF(0, 0), QPointF(15, 0), QPointF(15, 6), QPointF(7, 6), 
                  QPointF(7, 11), QPointF(2, 11), QPointF(2, 17), QPointF(0, 17)],
                 [Anchor(QPointF(0, 0), AnchorType.CORNER), 
                  Anchor(QPointF(15, 0), AnchorType.DRIFT),
                  Anchor(QPointF(7, 6), AnchorType.DRIFT)],
                 1),
            2: Area([QPointF(20, 0), QPointF(32, 0), QPointF(32, 6), QPointF(20, 6)],
                 [Anchor(QPointF(20, 0), AnchorType.DRIFT)],
                 2),
            3: Area([QPointF(15, 13), QPointF(20, 13), QPointF(20, 17), QPointF(15, 17)],
                 [Anchor(QPointF(15, 13), AnchorType.DRIFT)],
                 3),
            4: Area([QPointF(9, 8), QPointF(18, 8), QPointF(18, 11), QPointF(13, 11), 
                  QPointF(13, 14), QPointF(9, 14)],
                 [Anchor(QPointF(9, 8), AnchorType.DRIFT), 
                  Anchor(QPointF(13, 11), AnchorType.DRIFT)],
                 4),
            5: Area([QPointF(38, 12), QPointF(41, 12), QPointF(41, 17), QPointF(34, 17), 
                  QPointF(34, 16), QPointF(38, 16)],
                 [Anchor(QPointF(41, 17), AnchorType.DRIFT), 
                  Anchor(QPointF(38, 16), AnchorType.DRIFT)], 
                 5),
            6: Area([QPointF(19, 24), QPointF(31, 24), QPointF(31, 31), QPointF(19, 31)],
                 [Anchor(QPointF(31, 31), AnchorType.DRIFT)], 
                 6),
            7: Area([QPointF(30, 19), QPointF(43, 19), QPointF(43, 15), QPointF(49, 15), 
                  QPointF(49, 39), QPointF(25, 39), QPointF(25, 33), QPointF(33, 33),
                  QPointF(33, 22), QPointF(30, 22)],
                 [Anchor(QPointF(49, 39), AnchorType.CORNER), 
                  Anchor(QPointF(33, 33), AnchorType.DRIFT),
                  Anchor(QPointF(43, 19), AnchorType.DRIFT)], 
                 7),
            8: Area([QPointF(31, 11), QPointF(36, 11), QPointF(36, 14), QPointF(31, 14)],
                 [Anchor(QPointF(36, 14), AnchorType.DRIFT)], 
                 8)
        }

        # 按广度搜索记录的"关联的锚点对"
        self.anchor_relation:List[AnchorRelation] = [] 

    # anchor是否为区域的随动锚点
    def is_follower_anchor(self, anchor:Anchor):
        for anchor_pair in self.anchor_relation:
            if anchor == anchor_pair.from_anchor:
                return True
            if anchor == anchor_pair.to_anchor:
                return False
        return True

    # 获取anchor的跟随锚点
    def get_follower_anchor(self, anchor:Anchor):
        for anchor_pair in self.anchor_relation:
            if anchor == anchor_pair.to_anchor:
                return anchor_pair.from_anchor
        return Anchor()
        

    def build_anchor_relationship(self):
        queue = Queue(maxsize=0) # 不限长度的队列
        # queue.put()  # queue.get()
        # 找到所有类型为"AnchorType.CORNER"的锚点，作为遍历起点
        for area in self.area_map.values():
            for anchor in area.anchors:
                if anchor.type == AnchorType.CORNER:
                    queue.put(anchor)
                    min_dist_anchor = Utils.get_min_distance_idx_by_anchors(anchor, self.fixed_anchors)
                    if not min_dist_anchor.is_valid():
                        print("eroor !!!; 'AnchorType.CORNER'类型的锚点配置错误")
                        exit(0)
                    # 添加一组关联的锚点对
                    self.anchor_relation.append(AnchorRelation(min_dist_anchor, anchor))
        
        while not queue.empty():
            anchor = queue.get()
            # 如果anchor是随动锚点; 则将anchor所属区域中，未构建关联的其他锚点 加入队列
            if self.is_follower_anchor(anchor): 
                for tmp_anchor in self.area_map[anchor.reside_area_key].anchors:
                    if tmp_anchor != anchor and (not tmp_anchor.is_related):
                        queue.put(tmp_anchor)
                        min_dist_anchor = Utils.get_min_distance_idx_by_areas(tmp_anchor, self.area_map)
                        if not min_dist_anchor.is_valid():
                            print("eroor !!!; 类型的锚点配置错误")
                            exit(0)
                        self.anchor_relation.append(AnchorRelation(tmp_anchor, min_dist_anchor))
            else:
                # anchor是被动锚点, 将与其关联的随动锚点加入队列
                follower_ahchor = self.get_follower_anchor(anchor)
                queue.put(follower_ahchor)

        return self.anchor_relation

    # 移动指定区域
    def move(self, area_key, offset_point:QPointF):
        area = self.area_map[area_key]
        for point in area.vertex:
            point.setX(point.x() + offset_point.x())
            point.setY(point.y() + offset_point.y())
        for anchor in area.anchors:
            anchor.move(offset_point)

if __name__ == "__main__":
    areas = AreaCollections(50, 40)
    # 输出锚点关联关系
    anchor_relations = areas.build_anchor_relationship()
    for anchor_relation in anchor_relations:
        print(anchor_relation)

    print("===========")
    # 移动区域
    areas.move(1, QPointF(10, 10))
    for anchor_relation in anchor_relations:
        print(anchor_relation)        

                            
                        

                    
# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://docs.scrapy.org/en/latest/topics/item-pipeline.html

import re
import datetime
from scrapy.exceptions import DropItem

# 清理数据的pipeline
class KuakuaItemDataCollationPipeline(object):
    def process_item(self, item, spider):
        # 对抓取到的原始内容进行处理
        # 去除多余的空格
        item['topicDiscussion'] = list(map(lambda x: x.strip(), item['topicDiscussion']))
        item['author'] = list(map(lambda x: x.strip(), item['author']))
        item['responsesNum'] = list(map(lambda x: x.strip(), item['responsesNum']))
        item['lastResponseTime'] = list(map(lambda x: x.strip(), item['lastResponseTime']))
        # 类型转换
        item['topicDiscussion'] = item['topicDiscussion'][0]
        item['author'] = item['author'][0]
        item['responsesNum'] = 0 if len(item['responsesNum']) == 0 else int(item['responsesNum'][0])
        item['lastResponseTime'] = item['lastResponseTime'][0]
        if re.match(r'\d{4}-\d{2}-\d{2}', item['lastResponseTime']):
            item['lastResponseTime'] = datetime.datetime.strptime(item['lastResponseTime'], "%Y-%m-%d")
        elif re.match(r'\d{2}-\d{2} \d{2}:\d{2}', item['lastResponseTime']):
            year = datetime.datetime.now().strftime("%Y")
            item['lastResponseTime'] = '%s-' % year + item['lastResponseTime']
            item['lastResponseTime'] = datetime.datetime.strptime(item['lastResponseTime'], "%Y-%m-%d %H:%M")
        else:
            raise DropItem('LastResponseTime error')
        return item


# 去重的pipeline
class KuakuaItemDuplicatesPipeline(object):
    def __init__(self):
        self.itemSet = set()

    def process_item(self, item, spider):
        if item['topicDiscussion'] in self.itemSet:
            raise DropItem("Duplicate item found: %s" % item)
        else:
            self.itemSet.add(item['topicDiscussion'])
            return item

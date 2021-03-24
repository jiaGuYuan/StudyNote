# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://docs.scrapy.org/en/latest/topics/items.html

import scrapy

class CommentItem(scrapy.Item):
    commentator = scrapy.Field()
    comment = scrapy.Field()
    commentTime = scrapy.Field()


class TopicDetailsItem(scrapy.Item):
    topicDetails = scrapy.Field()
    topicStartDataTime = scrapy.Field()
    commentItemList = scrapy.Field()


class KuakuaItem(scrapy.Item):
    # define the fields for your item here like:
    topicDiscussion = scrapy.Field()
    author = scrapy.Field()
    responsesNum = scrapy.Field()
    lastResponseTime = scrapy.Field()
    topicDetailsItem = scrapy.Field()


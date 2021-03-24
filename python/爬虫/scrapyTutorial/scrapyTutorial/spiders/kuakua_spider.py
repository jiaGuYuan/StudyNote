import scrapy
from scrapyTutorial.items import KuakuaItem
from scrapyTutorial.items import CommentItem
from scrapyTutorial.items import TopicDetailsItem

class KuakuaSpider(scrapy.Spider):
    name = 'Kuakua'
    allowed_domains = ['douban.com'] # 与OffsiteMiddleware设置共同生效
    start_urls = [
        "https://www.douban.com/group/656969/discussion"
    ]

    # 浏览器用户代理
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36'
    }
    # 从浏览器中复制的cookie
    user_cookie = 'bid=w1HZMM_fVJg; __gads=ID=13b8eea5f40220c9:T=1584441273:S=ALNI_MYc6ATxmWxu4dNvO-mgg5oGocgNLw; __utmc=30149280; __utmz=30149280.1584457795.2.2.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; douban-fav-remind=1; ct=y; push_noty_num=0; push_doumail_num=0; __utmv=30149280.18984; __utmc=223695111; __utmz=223695111.1584543643.1.1.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/; __yadk_uid=Ni1FjD3EhOOEtv0wkq1IaRniqZ7mgmHc; _vwo_uuid_v2=D39E3B8CC2E108F9B3060F8AB5EE22789|ecb30983a3e8336370003d202cbc8fcb; dbcl2="189842070:vmEMKK1SlmA"; ck=9AqE; _pk_ref.100001.4cf6=%5B%22%22%2C%22%22%2C1584597027%2C%22https%3A%2F%2Fwww.douban.com%2F%22%5D; _pk_ses.100001.4cf6=*; ap_v=0,6.0; __utma=30149280.741641532.1584441270.1584576329.1584598370.13; __utmb=30149280.0.10.1584598370; __utma=223695111.195136546.1584543643.1584576329.1584598370.5; __utmb=223695111.0.10.1584598370; _pk_id.100001.4cf6=8c3a5842d9cbb517.1584543643.5.1584598799.1584576328.'

    # scrapy cookie必须为一个dict
    def make_cookie_dict(self, cookieStrGoogle):
        # scrapy cookie必须为一个dict
        cookie_dict = {x.split("=", 1)[0].strip(' "'): x.split("=", 1)[1].strip(' "') for x in
                       self.user_cookie.split(";")}
        return cookie_dict

    def parse(self, response):
        # 将爬取到的网页保存到outSpiderHTML_folder目录中
        # 首页的url中没有start参数表示讨论话题的起始序号, 手动设置为0
        serialNumber = 0 if len(response.url.split('?start='))==1 else int(response.url.split('?start=')[1])
        print('serialNumber=', serialNumber)
        filename = 'outSpiderHTML_folder/KuakuaPageNum_%d.html' % serialNumber
        with open(filename, 'wb') as f:
            f.write(response.body)

        cookie_dict = self.make_cookie_dict(self.user_cookie)
        for sel in response.xpath('//table/tr/td[@class="title"]/..'):
            item = KuakuaItem()
            # 抓取到的原始内容, 填充item相关字段
            item['topicDiscussion'] = sel.xpath('./td[1]/a/text()').extract()
            item['author'] = sel.xpath('./td[2]/a/text()').extract()
            item['responsesNum'] = sel.xpath('./td[3]/text()').extract()
            item['lastResponseTime'] = sel.xpath('./td[4]/text()').extract()

            # 进入话题详情页获取评论信息
            item_href = sel.xpath('./td[1]/a/@href').extract()[0]
            # 使用Request的meta功能来传递已经部分获取的item
            yield scrapy.Request(url=item_href, headers=self.headers, cookies=cookie_dict, callback=self.parse_item, meta={'item': item})

        # 自动翻页
        nextLink = response.xpath('//span[@class="next"]/a/@href').extract()
        if nextLink:
            yield scrapy.Request(nextLink[0], callback=self.parse)
        else :
            return None


    def parse_item(self, response):
        item = response.meta['item']

        # 填充item的其它字段
        topicDetailsItem = TopicDetailsItem()
        topicDetailsItem['topicStartDataTime'] = response.xpath('//div[@class="topic-doc"]/h3/span[2]/text()').extract()[0].strip()
        topicDetails = response.xpath('//div[@class="topic-content"]/div[@class="topic-richtext"]/p/text()').extract()
        topicDetails = '\n'.join(list(map(lambda x: x.strip(), topicDetails)))
        if topicDetails:
            topicDetailsItem['topicDetails'] = topicDetails

        commentItemList = []
        for reply in response.xpath('//ul[@class="topic-reply"]/li'):
            commentItem = CommentItem()
            commentItem['commentator'] = reply.xpath('.//h4/a/text()').extract()[0].strip()
            comment = reply.xpath('.//p[contains(@class, "reply-content")]/text()').extract()
            if len(comment)==0:
                print('\n\ncomment none', item['topicDiscussion'], '\n\n')
            else:
                commentItem['comment'] = comment[0].strip()
            commentItem['commentTime'] = reply.xpath('.//h4/span/text()').extract()[0].strip()
            commentItemList.append(commentItem)
        # 自动翻页采集评论
        nextLink = response.xpath('//span[@class="next"]/a/@href').extract()
        if nextLink:
            yield scrapy.Request(nextLink[0], callback=self.parse_next_comment, meta={'commentItemList': commentItemList})

        topicDetailsItem['commentItemList'] = commentItemList
        item['topicDetailsItem'] = topicDetailsItem
        yield item

    def parse_next_comment(self, response):
        commentItemList = response.meta['commentItemList']
        for reply in response.xpath('//ul[@class="topic-reply"]/li'):
            commentItem = CommentItem()
            commentItem['commentator'] = reply.xpath('.//h4/a/text()').extract()[0].strip()
            comment = reply.xpath('.//p[contains(@class, "reply-content")]/text()').extract()
            if len(comment) == 0:
                print('\n\ncomment none', '\n\n')
            else:
                commentItem['comment'] = comment[0].strip()

            commentItem['commentTime'] = reply.xpath('.//h4/span/text()').extract()[0].strip()
            commentItemList.append(commentItem)
        # 自动翻页采集评论
        nextLink = response.xpath('//span[@class="next"]/a/@href').extract()
        if nextLink:
            yield scrapy.Request(nextLink[0], callback=self.parse_next_comment, meta={'commentItemList': commentItemList})
        yield commentItemList

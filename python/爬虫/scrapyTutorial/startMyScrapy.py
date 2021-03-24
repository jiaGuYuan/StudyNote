# coding=utf-8
from scrapy import cmdline
import os

if __name__ == '__main__':
    outFile = './outItems.json'
    if os.path.exists(outFile):
        print("%s existed, delete!" % outFile)
        os.remove(outFile)
    #cmdline.execute('scrapy crawl Kuakua'.split())
    cmdline.execute(('scrapy crawl Kuakua -o %s  -t json' % outFile).split())
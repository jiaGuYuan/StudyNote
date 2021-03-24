#!/usr/bin/python
# -*- coding: utf-8 -*-
#Filename:changeEncode.py

import sys
import os
import chardet

 
def print_usage():
    print('''功能:将指定文件或目录下的[指定编码方式]的文件转换为另一种编码
　　格式:changeEncode [file|directory] [charset] [match_charset]
　　[file|directory]：文件/目录
    [charset]:指定的字符集,默认为utf-8
　　[match_charset]：期望转换的文件的字符集，若指定该项则只有与之匹配的文件才会进行转换
    注意：原字符集的文件将被删除。 
    ''')

#获取字符s所用的编码方式
def get_charset(s):
    return chardet.detect(s)['encoding']

#删除文件 file_name:要删除的文件名
def remove(file_name):
    os.remove(file_name)

#将文件file_name的字符集设修改为charset，目标文件名为output_file_name
#match_charset非空时表示只有当文件file_name原字符集与之匹配时才修改。
def change_file_charset(file_name, output_file_name, charset, match_charset=''):
    try:
        with open(file_name, 'rb') as f:
            s = f.read()
        
        old_charset = get_charset(s) #获取原编码方式
        if match_charset!='' and old_charset != match_charset:
            return 0
        if old_charset == charset:
            return 0
            
        with open(file_name, 'r', encoding=old_charset) as f:
            s = f.read()
        #u = s.decode(old_charset) #将原字编码文件的字符转码为unicode（中间码）

        if file_name == output_file_name or output_file_name == "":
            remove(file_name)

        if output_file_name == "":
            output_file_name = file_name

        with open(output_file_name, 'wb') as f:
            u = s.encode(charset) #编码
            f.write(u)
        return 0
    except:
        return -1

 
def ChangeEncode(file,toEncode, match_charset=''):
    if (file[-2:]=='.h') or(file[-2:]=='.c') or (file[-4:]=='.hpp') or(file[-4:]=='.cpp'):
        return change_file_charset(file, file, toEncode, match_charset)
    else:
        return 0
                             

 
def Do(dirname,toEncode, match_charset=''):
    if(os.path.isdir(dirname)):
        for root,dirs,files in os.walk(dirname):
            for _file in files:
               _file=os.path.join(root,_file)
               if(ChangeEncode(_file,toEncode, match_charset)!=0):
                   print ("[转换失败:] "+_file)
               else:
                   print ("[成功：] "+_file)
            for subdir in dirs:
                Do(subdir,toEncode, match_charset)
    else:
        if(ChangeEncode(dirname,toEncode, match_charset)!=0):
            print ("[转换失败:] "+dirname)
        else:
            print ("[成功：] "+dirname)
    
 
def CheckParam(dirname,toEncode, match_charset):
    encode=["UTF-8","GBK","gbk","utf-8", 'cp936', 'ANSI' ]
    if(not match_charset in encode or not toEncode in encode):
        return 2
    if(match_charset==toEncode):
        return 3
    if(not os.path.isdir(dirname)):
        return 1
    return 0
 
if __name__=="__main__":
    error={1:"第一个参数不是一个有效的文件夹",3:"源编码和目标编码相同",2:"您要转化的编码不再范围之内：UTF-8，GBK"}
    if __name__ == '__main__':
        length = len(sys.argv)

    if length == 1:
        print_usage()
    elif length == 2:
        Do(sys.argv[1], "utf-8", "")
    elif length == 3:
        Do(sys.argv[1], sys.argv[2], "")
    elif length == 4:
        Do(sys.argv[1], sys.argv[2], sys.argv[3])
    else:
        print_usage()

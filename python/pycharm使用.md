## 配置pycharm使用的python环境

​    File -> settings -> 在project Interpreter中选择要使用的环境(本地或远程)

## 建议为每一个项目创建一个虚拟环境:

​    这样可以单独控制该虚拟环境中的python解析器以及各个库的版本.
​    pycharm创建虚拟环境的方法如下:
​    File -> settings -> 在project Interpreter中Add -> 然后可以根据需要选择创建Virtualenv或Conda的虚拟环境
​    -> New environment -> 在Location中选择创建虚拟环境的目录, 在Base interpreter中选择基础的python解释器
​    -> ok
​    这样在project interpreter的下拉列表中就可以找到刚才创建的虚拟环境.
​    以后便可以针对该虚拟环境单独配置所需要的库了.

# 问题:

## 问题1:

​    描述:
​        使用pycharm连接远程环境后,pyspark相关代码可以正常执行,但pycharm上没有代码提示,无法跳转到定义.
​    解决方法:
​        能正常执行说明环境正常--因为远程环境python能正常找到包"ln -s /root/bigdata/spark/python/pyspark /miniconda2/envs/reco_sys/lib/python3.6/site-packages/pyspark"
​        a. 正常情况下只需要将对应的包拷贝到python环境下即可(如拷贝到"/miniconda2/envs/reco_sys/lib/python3.6/site-packages/"下)
​        b. 本次使用a操作无法解决.所以直接将远程环境中的pyspark包拷贝到本机的"C:\Users\用户名\.PyCharm2018.1\system\remote_sources\XXX\YYY" ,pyCharm就可对其进行代码补全了.
​        

    注: 这个路径与pyCharm连接的远程环境有关. 
       XXX与远程环境的".pycharm_helpers/python_stubs/XXX"对应,
       YYY与pyCharm的"External Librarys/Remote Libraries/YYY"配置对应(可以有多个)
   可通过在pyCharm的"External Librarys/Remote Libraries/YYY"右键"show in Exploer"找到对应的目录.

​    pyCharm import sklearn错误问题:
​    问题描述:在pyCharm中import sklearn提示from . import _arpack  ImportError: DLL load failed.
​             但在jupyter中可以正常import sklearn.
​    解决方法: 将如下三个路径设置到环境变量的最前面 --问题原因:可能是系统中有多个python环境
​        D:\Software\Anaconda3
​        D:\Software\Anaconda3\Scripts
​        D:\Software\Anaconda3\Library\bin
​    参考: https://blog.csdn.net/qq_32167817/article/details/88639536
​    ​
     **另外的方法**:
     在File-Settings中的project structure中点击右边的“add  content root”，
     添加py4j-some-version.zip和pyspark.zip的路径（这两个文件都在Spark中的python/lib文件夹下）
     ![](images_attachments/20200813194817964_11260.png)

## 其它
### 中文显示阴影:
     强制指定UTF-8编码
     # _*_ coding:UTF-

###  自定义快捷键:
    * 全部折叠: Alt 0               　 全部展开: Alt Shift 0
    * 当前位置折叠: Alt +              当前位置展开: Alt -
    * 当前位置递归折叠: Alt Shift +　　 当前位置展开: Alt Shift -
    * 当前位置展开N层: Alt N (N为1,2,3,4)
    * 全部位置展开N层: Alt Shift N
![](images_attachments/20200618164019367_25280.png =500x)



### 远程环境配置

File-->Setting

添加远程解释器

![添加远程解释器](pycharm使用.assets/image-20200512002723204.png) 

![远程解释器配置](pycharm使用.assets/image-20200512002909033.png)

![img](pycharm使用.assets/1U%[SMQ9ML44ZH_@ZKCZJ.png)

![image-20200512002952145](pycharm使用.assets/image-20200512002952145.png)



## windows创建pyspark项目
![](images_attachments/20200814145900171_13722.png =800x)



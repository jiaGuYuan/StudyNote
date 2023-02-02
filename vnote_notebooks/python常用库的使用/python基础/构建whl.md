# 构建whl
[构建whl参考](https://cloud.tencent.com/developer/article/1702289)
命令: `pip wheel ...`
查看帮助: `pip help wheel`

参数: 
```
-w, --wheel-dir <dir>: 指定要构建whl的目录
```

示例: 在windows上使用py38构建cx_oracle-6.4.1的whl
`pip wheel -w D:\whl_build\cx_Oracle-6.4.1`

![](images_attachments/205804216241679.png)

**用什么平台&什么版本的python构建就会生成对应平台&版本的whl文件(会在文件名中体现).**

想用whl打包功能，可能需要vc++运行库，如果遇到
 Microsoft Visual C++ 14.0 is required 报错，
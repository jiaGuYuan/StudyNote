# 覆盖率测试工具-coveragepy
安装: `pip install coverage`
进行覆盖率测试:
1. 执行覆盖率测试以获取覆盖率数据: 
    * `coverage run -m unittest`: 获取覆盖率数据(仅包含用户代码,不包含第三方库)
    * `coverage run --source libName -m unittest`: 获取'--source libName'指定库的覆盖率数据
    * 参数说明:
        + --omit: 排除指定的项(例如`--omit=*/xxx/*`)
2. 展示覆盖率数据: 
    * 命令行展示:  `coverage report -m`, -m: 显示没有被覆盖的行
    * html: `coverage html`, 会生成一个htmlcov目录,在该目录下启动一个http.server可通过web查看覆盖率数据`python -m http.server --directory htmlcov/`
3. coverage配置文件'.coveragerc'
示例:
```.coveragerc
[run]
omit = 
    # executing is imported
    */executing/*
```
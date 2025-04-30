# 在服务器上构建python环境
## 安装miniconda 

```
./Miniconda3-py38_4.10.3-Linux-x86_64.sh -b
```

## python环境
因为内部为无网环境，直接使用构建好的conda虚拟环境(适用于centos7 x86_64).
将centos_skd_env.tar.gz解压到 miniconda的envs目录下
```
cd MINICONDA3_INSTALL_PATH/envs  # MINICONDA3_INSTALL_PATH 为miniconda的安装目录
tar -C skd_env -zxvf centos_skd_env.tar.gz
```

## conda 命令
```
激活(进入)py36环境:
    source activate skd_env 
    # 指定环境的路径: source activate D:/myEnvPath/envs/py36Env
退出环境: conda deactivate 
```


# Pycharm连接远程服务器并实现远程调试
## 连接远程服务器

```
Tools(工具) -> Deployment(部署) ->  Configuration(配置) -> 新增一个SFTP协议的链接
    在connection页面 配置 host/port/user/password => 测试连接
    在mappings页面 配置 '本地项目路径' 与 '远程服务器项目路径' 的映射

Tools(工具) -> 'Start SSH sessin..'
    如果pycharm的 Terminal终端出现中文乱码的情况，那么需要修改pycharm的Tools配置
    点击左上角 Files -> Settings -> Tools -> SSH Terminal，将格式改为 utf-8
    
查看远程服务器目录结构
    Tools(工具) -> Deployment(部署)  -> 'Browse Remote Host(浏览远程主机)'
    

```

## 同步代码
```
Tools(工具) -> Deployment(部署)
    'Upload to XXX': 将本地的代码同步到远程服务器(XXX是远程主机的名称)
    'Download from XXX': 从远程服务器拉代码(XXX是远程主机的名称)
    'Automatic Upload (always)': pycharm会自动将代码同步到远程服务器上
```

## 配置远程解释器进行调试
```
左上角 Files -> Settings -> 'Project Interpreter' -> Add
选择'SSH Interpreter'，选择'Existing server configuration'，选择前面配置的远程服务器
配置Python解释器所在路径(选择前面解压的centos_skd_env.tar.gz环境)
```



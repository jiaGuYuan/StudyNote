# 构建可远程ssh的开发镜像

## 安装 WSL
[安装 WSL](https://learn.microsoft.com/zh-cn/windows/wsl/install)

## WSL 2 上的 Docker
[WSL 2 上的 Docker 远程容器入门](https://learn.microsoft.com/zh-cn/windows/wsl/tutorials/wsl-containers)

## 构建pycharm 可远程的开发镜像
参考: [构建pycharm 可远程的开发镜像](https://zhuanlan.zhihu.com/p/605389180)

```
# -d: 在后台运行容器
# -v: 主机:/home/docker_share <==> docker:/my_data
# -p: 主机8822端口 <==> docker ssh的22端口
# --privileged：使容器内的用户拥有真正的 root 权限
```

### 构建过程
```
    # 通过基础镜像构建 可ssh的开发环境
    docker run --name gjy-dev -it --privileged -p 8899:8888 -p 8822:22 -v /home/docker_share:/my_data --entrypoint /usr/sbin/init dev-base-image:2023-12-11 
    0. 进入容器
        docker exec -it gjy-dev /bin/bash
    1. 安装SSH: 
        `yum install -y openssh-server`
    2. 设置root密码(密码为root): 
        `passwd root`
    3. 修改 /etc/ssh/sshd_config 的配置：
        vim /etc/ssh/sshd_config
        PermitRootLogin: yes  # 允许root用户使用ssh登录
    3. 启动 SSH 服务
        systemctl start sshd.service
    4. SSH 开机启动
        systemctl enable sshd.service
    5. 查看 SSH 状态
        systemctl status sshd.service
    6. 退出并提交镜像
        docker commit -a "G" -m "dev with ssh server" 容器ID  gjy-dev-ssh
    7. docker save -o gjy-dev-ssh.tar gjy-dev-ssh:latest
```

### 使用过程:
1. 启动gjy-dev-ssh容器
    a. 加载镜像
        docker load -i gjy-dev-ssh.tar
    b. 启动容器
    ```
   # 指定mac地址(--mac-address)，配合optvom lic文件
   docker run --name gjy-dev-ssh-env-0819 -it --privileged -p 8899:8888 -p 8822:22 -v /home/docker_share:/my_data --entrypoint /usr/sbin/init skd-env-optvom-ssh-0705:latest  --mac-address=02:42:ac:11:00:02
   
    # 配置环境变量OPTVOM_LICENSE_PATH (可以在pycharm的启动参数中配置)
    OPTVOM_LICENSE_PATH=./Optvom_02_42_ac_11_00_02.lic  # 按实际路径配置
    ```
    c. SSH测试连接（密码root）
        ssh root@127.0.0.1 -p 8822
  
2. 根据"PyCharm配置远程Docker环境 - twilight0402 - 博客园"配置pycharm 
    注意SSH端口号
    [SSH 与 Deployment](https://blog.csdn.net/m0_57845572/article/details/116759819)
  
### 其它
1. 重启容器
    docker restart 容器名


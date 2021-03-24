# dockerfile-说明

# Dockerfile的基本结构
## FROM: 指定基础镜像，必须为第一个命令
格式:
    FROM <image>
    FROM <image>:<tag>
    FROM <image>@<digest>

## MAINTAINER: 维护者信息
格式:
    MAINTAINER <name>

## RUN: 构建镜像时在镜像容器中执行的命令
两种命令执行方式
1. shell执行
格式：
    RUN <command>
2. exec执行
格式：
    RUN ["executable", "param1", "param2"]

注：
    RUN指令创建的中间镜像会被缓存, 并会在下次构建中使用。
    如果不想使用这些缓存镜像, 可以在构建时指定--no-cache参数(如: docker build --no-cache)


## ADD：将上下文路径中的文件添加到容器中
tar类型文件会自动解压(网络压缩资源不会被解压)，可以访问网络资源，类似wget
格式：
    ADD <src>... <dest>
    ADD ["<src>", ... "<dest>"] 用于支持包含空格的路径

PS: 上下文路径是指使用'docker build'命令构建镜像的目录

## COPY：将上下文路径中的文件拷贝文件到容器中
不会自动解压文件，也不能访问网络资源.
格式：
    COPY <src>... <dest>
    COPY ["<src>", ... "<dest>"] 用于支持包含空格的路径


## CMD：构建容器后调用，也就是在容器启动时才进行调用
格式：
    CMD ["executable", "param1", "param2"] (执行可执行文件, 优先)
    CMD ["param1", "param2"] (设置了ENTRYPOINT, 参数作为于ENTRYPOINT指定的函数或命令)
    CMD command param1 param2 (执行shell内部命令)

注：
    CMD不同于RUN; CMD用于指定在容器启动时所要执行的命令, 而RUN用于指定镜像构建时所要执行的命令。

## ENTRYPOINT：配置容器，使其可执行化。
配合CMD可省去"application"，只使用参数.
格式：
    ENTRYPOINT ["executable", "param1", "param2"] (可执行文件, 优先)
    ENTRYPOINT command param1 param2 (shell内部命令)

注：
    ENTRYPOINT与CMD非常类似, 不同的是通过docker cmd执行的命令不会覆盖ENTRYPOINT, 
    而docker cmd命令中指定的任何参数, 都会被当做参数再次传递给ENTRYPOINT。
    **Dockerfile中只允许有一个ENTRYPOINT命令**, 多指定时会覆盖前面的设置, 而只执行最后的ENTRYPOINT指令。

## LABEL：用于为镜像添加元数据
格式：
    LABEL <key>=<value> <key>=<value> <key>=<value> ...

注：
　　推荐将所有的元数据通过一条LABEL指令指定，以免生成过多的中间镜像。

## ENV：设置环境变量
格式：
    ENV <key> <value>  # 只能指定单个环境变量
    ENV <key>=<value> <key2>=<value2> ...  # 可同时设置多个变量, 反斜线可以用于续行, 创建空格时使用引号


## EXPOSE：指定于外界交互的端口
格式：
    EXPOSE <port> [<port> ...]


## VOLUME：用于指定持久化目录(目录挂载)
格式：
    VOLUME ["/path/to/dir"]


## WORKDIR：工作目录，类似于cd命令
格式：
    WORKDIR /path/to/workdir

注：
　　通过WORKDIR设置工作目录后, Dockerfile中其后的命令RUN、CMD、ENTRYPOINT、ADD、COPY等命令都会在该目录下执行。
　　在使用docker run运行容器时，可以通过-w参数覆盖构建时所设置的工作目录。

## USER:指定运行容器时的用户名或 UID
格式:
　　USER user/uid
　　USER user/uid:group
　　USER user/uid:gid

## ARG：用于指定传递给构建运行时的变量
格式：
    ARG <name>[=<default value>]

## ONBUILD：用于设置镜像触发器
格式：
　　ONBUILD [INSTRUCTION]

注：
　　当所构建的镜像被用做其它镜像的基础镜像，该镜像中的触发器将会被钥触发

# 示例
```
# nginx Dockerfile
# 基础镜像
FROM centos

# 维护者信息
MAINTAINER gjy 

# 设置环境变量
ENV PATH /usr/local/nginx/sbin:$PATH

# 文件放在当前目录下, 拷过去会自动解压
ADD nginx-1.8.0.tar.gz /usr/local/  
ADD epel-release-latest-7.noarch.rpm /usr/local/  

# 执行以下命令 
RUN rpm -ivh /usr/local/epel-release-latest-7.noarch.rpm
RUN yum install -y wget lftp gcc gcc-c++ make openssl-devel pcre-devel pcre && yum clean all
RUN useradd -s /sbin/nologin -M www

# 工作目录
WORKDIR /usr/local/nginx-1.8.0 
RUN ./configure --prefix=/usr/local/nginx --user=www --group=www --with-http_ssl_module --with-pcre && make && make install
RUN echo "daemon off;" >> /etc/nginx.conf

# 映射端口
EXPOSE 80

# 运行以下命令
CMD ["nginx"]
```
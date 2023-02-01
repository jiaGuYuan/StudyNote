# rmp包相关
## rpm 离线下载与安装

离线下载rpm包:
`yum --downloadonly --downloaddir=/home/packages install mariadb-devel`

安装离线rpm包: `rpm -ivh *.rpm`

## 容器&主机间文件复制
```
# 主机 => 容器
docker cp local_file_path CONTAINER-ID:/container-path

# 容器 => 主机
docker cp CONTAINER-ID:/container-path local_file_path
```
# docker导入导出
### 在容器中删除内容后(大文件3G), 提交的镜像没有变小, 使用`docker save ...` 保存的tar包也没有变小
原因: docker的镜像是按照层存储(方便分层引入from)。
解决方法:  使用`docker export`来导入一个容器快照到本地镜像库(快照，没有历史记录); 使用`docker import`来导入镜像

[参考docker-import&export](https://yeasy.gitbook.io/docker_practice/container/import_export)
[参考docker-save&load](https://yeasy.gitbook.io/docker_practice/image/other)

容器导出为文件 & 导入为镜像：
```
# 导出容器快照到本地文件
docker export 容器ID > image.tar

# 从容器快照文件中再导入为镜像(导入时可指定tag)
cat image.tar | docker import - my-image:v1.0
```

Docker 镜像的导入和导出 docker save 和 docker load
```
# 将镜像保存为归档文件
docker save 镜像ID -o filename.tar

# 将 保存的文件 加载为镜像：
docker load -i filename.tar
```

注：用户既可以使用 `docker load` 来导入镜像存储文件到本地镜像库，也可以使用 `docker import`来导入一个容器快照到本地镜像库。
这两者的区别在于容器快照文件将丢弃所有的历史记录和元数据信息（即仅保存容器当时的快照状态），而镜像存储文件将保存完整记录，体积也要大。
此外，从容器快照文件导入时可以重新指定标签等元数据信息。

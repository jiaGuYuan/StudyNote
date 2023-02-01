# NFS相关
## 由于NFS缓存的存在,一个节点创建的文件不能立即在另一个节点上通过os.path.exists()检测
场景: 在pod1上向nfs创建了一个文件'filex', 在pod2上调用os.path.exists('filex')返回False,但该文件确实是存在的。
原因: NFS有缓存，在pod1创建文件后要过一会儿pod2的缓存的信息才会更新. 在这段时间内os.path.exists()检查是无效的；为了得到正确的结果需要手动刷新缓存.
参考: [NFSCoding 缓存相关描述](http://web.archive.org/web/20100912144722/http://www.unixcoding.org/NFSCoding)

[python os.path.exists无法检测NFS文件](https://stackoverflow.com/questions/21292365/python-os-path-exists-failing-for-nfs-mounted-directory-file-that-exists)


[Pod NFS 挂载选项mountOptions](https://github.com/NetApp/trident/issues/159)

[NFS-Client Provisioner 设置挂载选项](https://github.com/kubernetes-retired/external-storage/issues/1008)

[kubernetes-nfs-mount-options](https://stackoverflow.com/questions/52617912/kubernetes-nfs-mount-options)



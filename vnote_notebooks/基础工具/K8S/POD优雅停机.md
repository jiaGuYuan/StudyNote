# POD优雅停机
![](images_attachments/50525811257170.png =600x)
当 Kubernetes 杀死一个 pod 时，会发生以下 5 个步骤：
1、 Pod 切换到终止状态并停止接收任何新流量，容器仍在 pod 内运行。
2、 preStop 钩子是一个特殊的命令 或 一个对 Pod 中容器的 http 调用。
3、 SIGTERM 信号被发送到 pod，容器意识到它将很快关闭。
4、 Kubernetes 等待宽限期 (terminationGracePeriodSeconds)。此等待与 preStop hook 和 SIGTERM 信号执行并行（默认 30 秒）。因此，Kubernetes 不会等待这些完成。如果这段时间结束，则直接进入下一步。正确设置宽限期的值非常重要。
5、向 pod 发送 SIGKILL 信号，然后移除 pod。如果容器在宽限期后仍在运行，则 Pod 被 SIGKILL 强行移除，终止完成。

总结下大致分为两步：
第一步定义 preStop，一般情况下可以休眠 30s，用于处理残余流量；
第二步发送 SIGTERM 信号，服务收到信号后进行服务的收尾工作处理。比如：关闭连接、通知第三方注册中心服务关闭.....
疑问：既然 pod 已经终止了，同时 K8s 的网络 endpoint 也摘除了，为什么还会进来流量呢？
因为这个网络接口的摘除是异步的，这也是为什么会首先执行 preStop，然后发送 SIGTERM 信号的原因所在。


Docker 和 Kubernetes 都只能向容器内具有 PID 1 的进程发送信号。
容器以shell脚本为ENTRYPOINT时，该shell进程的PID为1 .
在shell中启动任务进程必须使用内置的 exec 命令，这样启动的任务进程的PID才为1（系统调用exec是以新的进程去代替原来的进程，但进程的PID保持不变）。



参考： 
https://cloud.google.com/architecture/best-practices-for-building-containers?hl=zh-cn#solution_1_run_as_pid_1_and_register_signal_handlers
https://github.com/celery/celery/issues/2700#issuecomment-229210623
https://docs.celeryq.dev/en/latest/userguide/workers.html#process-signals



点击链接加入boardmix中的文件「pod优雅停机」，https://boardmix.cn/app/share?token=tKlu6YGdU-2yaKh3XRLh0aRLNRKdGftLlOoffS8yAkCSGIT2U7nuWxlE6wHUdAKeEfGPunHD79SFpmROUbdVSrjpKLpW4qGf9veMdTcNrZA=&inviteCode=WbvXPC

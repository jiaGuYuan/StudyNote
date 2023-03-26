# WSL部署K8S
## 在wsl上搭建minikube k8s测试环境
1. 安装WSL
2. 安装Docker Desktop, 并配置要从哪个WSL2发行版访问Docker: "设置->Resources->WSL-Integratin ->开启配置 "
3. 安装kubectl: [参考](https://kubernetes.io/zh-cn/docs/tasks/tools/install-kubectl-linux/#verify-kubectl-configration)
4. 安装minikube: [参考](https://minikube.sigs.k8s.io/docs/start/)

# 关于minikube
默认情况下, minikube 只暴露端口 30000-32767. 可使用以下方法调整范围：
`minikube start --extra-config=apiserver.service-node-port-range=1-65535`

## minikube管理命令
```shell
# 修改虚拟机内存配置
minikube config set memory 4G
# 启动集群
minikube start --memory='4g'
# 挂起虚拟机
minikube pause
# 停止集群  -- 停止VM或容器(以此来停止一个K8S集群),但是会完整的保留数据--可被start命令重新启动
minikube stop

## 删除一个本地 Kubernetes 集群(--all:删除所有集群) -- 该命令会删除对应的VM,并且会删除所有相关的文件
#minikube delete --all

# 启动仪表板; 启动后将提示浏览器URL入口: 如: http://127.0.0.1:PORT/api/v1/namespaces/kubernetes-dashboard/services/http:kubernetes-dashboard:/proxy/
minikube dashboard

```


## 部署服务
将应用程序部署到 minikube. (建议使用`kubectl apply -f FILENAME` )
```
# 查看命令对应的yaml
kubectl create deployment hello-minikube --image=kicbase/echo-server:1.0 --dry-run='client' -o yaml
# kubectl apply -f FILENAME

# 创建具有指定名称的deployment.
kubectl create deployment hello-minikube --image=kicbase/echo-server:1.0

# 查看结果
kubectl get deployments
```
对应的yaml文件: [点击跳转](#hello-minikube-deployment.yaml)

## 访问应用程序
### NodePort 访问
NodePort 服务是将外部流量直接获取到您的服务的最基本方式。
NodePort 会打开一个特定的端口，任何发送到该端口的流量都会被转发到该服务。

#### minikube service + tunnel
在Windows、WSL上使用Docker驱动，网络受限，无法直接访问Node IP, 需要通过tunnel.
使用 Docker 驱动程序在 Linux 上运行 minikube 将不会创建隧道。

通过`minikube service <service-name> --url`命令将NodePort类型的Services暴露.
该命令作为进程运行，创建到集群的隧道; 该命令将服务直接暴露给主机操作系统上运行的任何程序.
该命令必须在单独的终端窗口中运行以保持隧道打开, 在终端中 Ctrl-C 可终止进程，届时网络路由将被清理.

0. 创建 Kubernetes 部署(如果已部署则跳过)
`kubectl create deployment hello-minikube --image=kicbase/echo-server:1.0`

1. 创建类型为 NodePort 的 Service
```
kubectl expose deployment hello-minikube --type=NodePort --port=8080
```
对应的yaml文件: [点击跳转](#hello-minikube-svc-nodeport.yaml)

2. 查看'nodePort'
```
kubectl get svc
# 输出如下:
NAME              TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
hello-minikube    NodePort    10.100.238.34   <none>        8080:31389/TCP   3s
```

3. Run service tunnel 创建隧道--通过隧道端口暴露Service(CLUSTER_IP:TARGET_PORT)

```
minikube service hello-minikube --url
# 输出如下
http://127.0.0.1:57123
 ❗  Because you are using a Docker driver on darwin, the terminal needs to be open to run it.
 
Check ssh tunnel in another terminal
```

4. 检查上一步创建的 ssh 隧道
```
ps -ef | grep docker@127.0.0.1
# 输出如下:
ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -N docker@127.0.0.1 -p 55972 -i /Users/FOO/.minikube/machines/minikube/id_rsa -L TUNNEL_PORT:CLUSTER_IP:TARGET_PORT
Try in your browser
```

5. 在浏览器中通过'本机IP:隧道端口'进行访问: `http://127.0.0.1:TUNNEL_PORT`

#### minikube ip + NodePort
minikube VM 通过一个'host-only IP'暴露给主机系统，使用命令`minikube ip`获取该地址,
NodePort类型的Service可以通过'该IP 地址+NodePort'对服务进行访问。
1. 获取minikube-ip: `minikube ip`
2. 获取nodePort
`kubectl get service <service-name> --output='jsonpath="{.spec.ports[0].nodePort}"'`
3.  从浏览器使用"minikube-ip: nodePort" 访问服务
测试:  在win10 WSL上无法ping通'minikube-ip'. 但在pod上可ping通

### LoadBalancer

0. 创建 Kubernetes 部署(如果已部署则跳过)
`kubectl create deployment hello-minikube --image=kicbase/echo-server:1.0`

1. 在单独的终端中运行隧道
`minikube tunnel`
'minikube tunnel'作为一个进程运行，使'CLUSTER-IP'地址作为网关在主机上创建到集群Service CIDR的网络路由。tunnel 命令将'EXTERNAL-IP'直接暴露给主机操作系统上运行的任何程序。

2. 创建类型为 LoadBalancer 的 Service
`kubectl expose deployment hello-minikube --type=LoadBalancer --port=8080`
对应的yaml文件: [点击跳转](#hello-minikube-svc-loadbalancer.yaml)

3. 检查'EXTERNAL-IP'
```shell
kubectl get svc 
输出如下:
 NAME            TYPE         CLUSTER-IP    EXTERNAL-IP   PORT(S)        AGE 
 hello-minikube  LoadBalancer 10.96.184.178 127.0.0.1     8080:30791/TCP 40s
```
注意: 如果没有' minikube tunnel'，Kubernetes 会将'EXTERNAL-IP'显示为'pending'。

4. 在浏览器中尝试 确保没有设置代理）
`http://REPLACE_WITH_EXTERNAL_IP:8080`



# kubectl命令

```shell
# 验证 kubectl 配置
kubectl cluster-info

# 使用kubect命令
kubectl get po -A

# 以指定image创建Pod，并进入终端(方便调试)
kubectl run busybox --image=busybox -it
```


# 附件
1. <span id="hello-minikube-deployment.yaml">hello-minikube-deployment</span>
`kubectl create deployment hello-minikube --image=kicbase/echo-server:1.0 --dry-run='client' -o yaml > hello-minikube-deployment.yaml`
[hello-minikube-deployment.yaml](images_attachments/281185615248793/hello-minikube-deployment.yaml)

2. <span id="hello-minikube-svc-nodeport.yaml">hello-minikube-svc-nodeport</span>
`kubectl expose deployment hello-minikube --type=NodePort --port=8080 --dry-run='client' -o yaml > hello-minikube-svc-nodeport.yam`
[hello-minikube-svc-nodeport.yaml](images_attachments/281185615248793/hello-minikube-svc-nodeport.yaml)

3. <span id="hello-minikube-svc-loadbalancer.yaml">hello-minikube-svc-loadbalancer</span>
`kubectl expose deployment hello-minikube --type=LoadBalancer --port=8080 --dry-run='client' -o yaml > hello-minikube-svc-loadbalancer.yaml`
[hello-minikube-svc-loadbalancer.yaml](images_attachments/281185615248793/hello-minikube-svc-loadbalancer.yaml)


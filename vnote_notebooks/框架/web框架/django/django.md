
# django(基于WSGI的)
django启动时，启动了一个WSGIserver以及为每个请求的用户生成一个handler。
理解WSGI协议; WSGIHandler这个类控制整个请求到响应的流程，以及整个流程的基本过程.
参考: https://juejin.cn/post/6844903556416274446
        https://www.jianshu.com/p/679dee0a4193
![](images_attachments/207520619250381.png)


## 请求到响应的过程
**大致几个步骤**：
![](images_attachments/70970719245487.png)
1. 用户通过浏览器请求一个页面  
2. 请求到达Request Middlewares，中间件对request做一些预处理或者直接response请求  
3. URLConf通过urls.py文件和请求的URL找到相应的View  
4. View Middlewares被访问，它同样可以对request做一些处理或者直接返回response  
5. 调用View中的函数  
6. View中的方法可以选择性的通过Models访问底层的数据  
7. 所有的Model-to-DB的交互都是通过manager完成的  
8. 如果需要，Views可以使用一个特殊的Context  
9. Context被传给Template用来生成页面  
    a.Template使用Filters和Tags去渲染输出  
    b.输出被返回到View  
    c.HTTPResponse被发送到Response Middlewares  
    d.任何Response Middlewares都可以丰富response或者返回一个完全不同的response  
    e.Response返回到浏览器，呈现给用户  

## 中间件
作用于Web服务器端和Web应用之间.
在django中，中间件能够帮我们准备好request这个对象，也帮我们在response添加头部，状态码等.
每个中间件类至少含有以下四个方法中的一个：
process_request、 process_view、process_exception、process_response
**中间件处理顺序**
![](images_attachments/143280819226728.png)




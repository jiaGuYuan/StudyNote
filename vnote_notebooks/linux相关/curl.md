# curl
curl默认发送get请求: `curl www.baidu.com`
[参考](https://gist.github.com/subfuzion/08c5d85437d5d4f00e58)

## 常用参数
* -X: 指定请求的方式
    POST, GET, PUT, DELETE
* -d, --data: 在POST请求中发送指定数据
    url编码格式: `-d "param1=value1&param2=value2" or -d @data.txt`
    json格式: `-d '{"key1":"value1", "key2":"value2"}' or -d @data.json`
* -A: 设置用户代理 User-Agent
    `curl https://item.jd.com/100012043978.html  -A "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36" `
* -b, --cookie: 指定cookie，多个cookie使用分号分隔：
    `curl http://man.linuxde.net --cookie "user=root;pass=123456"`
* -c, --cookie-ja: 将响应cookie保存到文件
* -H, --header : 请求头.
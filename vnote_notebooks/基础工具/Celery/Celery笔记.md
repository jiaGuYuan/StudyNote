# Celery笔记
任务队列一般用于线程或计算机之间分配工作的一种机制。

任务队列的输入是一个称为任务的工作单元，有专门的Worker进行不断的监视任务队列，进行执行新的任务工作。
Celery 通过消息机制进行通信，通常使用中间人（Broker）作为客户端和Worker调节。
启动一个任务，客户端向消息队列发送一条消息，然后Broker将消息传递给一个Worker，最后由Worker进行执行Broker分配的任务。

Celery支持
* 中间人
    RabbitMQ
    Redis
    Amazon SQS
* 结果存储
    AMQP、 Redis
    Memcached
    SQLAlchemy、Django ORM
    Apache Cassandra、Elasticsearch
* 并发
    prefork (multiprocessing)
    Eventlet、gevent
    solo (single threaded)
* 序列化
    pickle、json、yaml、msgpack
    zlib、bzip2 compression
    Cryptographic message signing


功能
监控
    可以针对整个流程进行监控，内置的工具或可以实时说明当前集群的概况。
调度
    可以通过调度功能在一段时间内指定任务的执行时间 datetime，也可以根据简单每隔一段时间进行执行重复的任务，支持分钟、小时、星期几，也支持某一天或某一年的Crontab表达式。
工作流
    可以通过“canvas“进行组成工作流，其中包含分组、链接、分块等等。
    简单和复杂的工作流程可以使用一组“canvas“组成，其中包含分组、链接、分块等。
资源（内存）泄漏保护
    --max-tasks-per-child 参数适用于可能会出现资源泄漏（例如：内存泄漏）的任务。
时间和速率的限制
    您可以控制每秒/分钟/小时执行任务的次数，或者任务执行的最长时间，也将这些设置为默认值，针对特定的任务或程序进行定制化配置。
自定义组件
    开发者可以定制化每一个职程（Worker）以及额外的组件。职程（Worker）是用 “bootsteps” 构建的-一个依赖关系图，可以对职程（Worker）的内部进行细粒度控制。





 
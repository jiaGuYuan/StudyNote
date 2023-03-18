# 异步IO之asyncio
[asyncio.html](https://docs.python.org/zh-cn/3/library/asyncio.html)
[asyncio-task.html#coroutine](https://docs.python.org/zh-cn/3/library/asyncio-task.html#coroutine)
[学习视频](https://www.bilibili.com/video/BV1ST4y1m7No)
通过 async/await 语法来声明 协程(coroutine) 是编写 asyncio 应用的推荐方式
asyncio中`协程`可用来表示两个紧密关联的概念:
* 协程函数: 定义形式为 async def 的函数;
* 协程对象: 调用 协程函数 所返回的对象。

## 一个示例
```
import asyncio
import time

async def say_after(delay, what):
    await asyncio.sleep(delay)
    print(what)

async def main_serial():
    """串行的执行两个任务
    以下代码段会在等待 1 秒后打印 "hello", 然后 再次 等待 2 秒后打印 "world"
    """
    print(f"started at {time.strftime('%X')}")

    await say_after(1, 'hello')
    await say_after(2, 'world')

    print(f"finished at {time.strftime('%X')}")
    

async def main_concurrent():
    """并发执行两个任务"""
    task1 = asyncio.create_task(say_after(1, 'hello'))
    task2 = asyncio.create_task(say_after(2, 'world'))
    print(f"started at {time.strftime('%X')}")

    # 等待两个任务完成 (预计用时2s)
    await task1
    await task2
    print(f"finished at {time.strftime('%X')}")

if __name__ == '__main__':
    # asyncio.run(main_serial())

    asyncio.run(main_concurrent())

```


## 可等待对象
如果一个对象可以在 await 语句中使用，那么它就是`可等待`对象。许多 asyncio API 都被设计为接受可等待对象。

`可等待`对象有三种主要类型: 协程(coroutine), 任务(task) 和 Future.
协程: Python 协程属于`可等待`对象，因此可以在其他协程中被等待
任务: 用来`并行的`调度协程, 当一个协程通过 asyncio.create_task() 等函数被封装为一个 任务，该协程会被自动调度执行:
Future: 是一种特殊的 低层级 可等待对象，表示一个异步操作的 最终结果; 是Task的基类。
```
import asyncio

async def nested():
    return 42

async def main():
    # 如果我们只调用nested()，什么都不会发生。协程对象已创建但未等待，因此它根本不会运行
    nested()
    print(await nested())  # 等待`协程`
    
    # ===========
    # 安排nested() 与 main() 同时运行。
    task = asyncio.create_task(nested())
    await task  # 等待它完成

asyncio.run(main())

```

### await可等待的对象 & event loot可调度的最小单元
await可等待 coroutine/task/future, 但asyncio event_loop调度的最小单元是future/task(task也是一个future). 所以在await coroutine时会将coroutine将换成future再进行调度.
![](images_attachments/133592701221145.png)

## 任务
### 创建任务
asyncio.create_task(coro, *, name=None)
将 coro 协程 封装为一个 Task 并调度其执行; 返回 Task 对象。
name 不为 None，它将使用 Task.set_name() 来设为任务的名称。
该任务会在 get_running_loop() 返回的循环中执行，如果当前线程没有在运行的循环则会引发 RuntimeError。


Note: 保存对此函数返回值的引用，以避免任务在执行过程中消失。事件循环只保留对任务的`弱引用`, 其他地方未引用的任务可能会在任何时候被垃圾收集(甚至在任务完成之前)。对于可靠的`fire-and-forget`后台任务，将其收集到一个集合中(可通过回调在任务结束后移出集合).

```
background_tasks = set()

for i in range(10):
    task = asyncio.create_task(some_coro(param=i))

    # 向集合中添加任务。这将创建一个强引用, 防止其被垃圾回收。
    background_tasks.add(task)

    # 为了防止永远保留对已完成任务的引用, 使每个任务在完成后从集合中删除自己的引用
    task.add_done_callback(background_tasks.discard)
```


# celery_QA

## celery worker正常, redis也可正常连接, 但task.delay无响应
原因： 在celery app所在的模块的 `__init__.py`中 忘了添加以下内容，造成task.delay卡主.
![](images_attachments/311232316227356.png)




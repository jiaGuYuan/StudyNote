# flask_注意点

## 关于flask db.create_all()无法创建表
在shell中使用db.create_all() 时发现成功创建了db文件, 但是里面并没有我要的表。
-- 需要在shell中 先from app.models import user 才能成功创建表(需要将表对应的类所在的文件导入)

## sqlalchemy使用in操作时要求synchronize_session为False的错误
执行一个更新语句
Order.query.filter(Order.OrderId.in_(ids)).update({Order.IsDeleted:1})
发生报错"Specify ‘fetch’ or False for the synchronize_session parameter."
搜到的解释大致是说删除记录时，默认会尝试删除session 中符合条件的对象，而 in 操作还不支持，于是就出错了。
解决办法就是删除时不进行同步，然后再让 session 里的所有实体都过期。更新操作的问题也是类似的。
最后按提示给synchronize_session参数设置Flase值传进去，一切就正常了
Order.query.filter(Order.OrderId.in_(ids)).update({Order.IsDeleted:1}, synchronize_session=False)


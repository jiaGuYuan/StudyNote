# Django_study
1. 创建项目
    django-admin startproject mysite
2. 创建应用
     python manage.py startapp polls
3.  运行开发服务器
    python manage.py runserver    

-- django AIP测试
    ``` python manage.py shell ```
   会设置 DJANGO_SETTINGS_MODULE 环境变量，这个变量会让 Django 根据 mysite/settings.py 文件来设置 Python 包的导入路径。

# 问题记录
1. Django: Cannot update a query once a slice has been taken
    Django一旦切片被取消，就无法更新查询
    [参考](https://www.zhihu.com/question/57861389?sort=created)
    a.objects.filter(a=1).[:30].update(b=2) # error
    可以修改为如下
    ```
    a_obj=a.objects.filter(a=1).[:30]
    a.object.filter(pk__in=list(a_obj)).update(b=2)
    ```

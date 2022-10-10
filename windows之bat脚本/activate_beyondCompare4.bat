@echo off

@rem # 用于激活Beyond Compare 4
@rem # 秘钥过期时,只需要删除"C:/Users/xxx/AppData/Roaming/Scooter Software/Beyond Compare 4/"目录下的文件即可

del /S /Q "C:/Users/GJY/AppData/Roaming/Scooter Software/Beyond Compare 4/"


@rem # 或者 修改注册表, 删除项目 '计算机\HKEY_CURRENT_USER\Software\Scooter Software\Beyond Compare 4\CacheId'
reg delete "HKEY_CURRENT_USER\Software\Scooter Software\Beyond Compare 4" /v CacheID /f

@echo on
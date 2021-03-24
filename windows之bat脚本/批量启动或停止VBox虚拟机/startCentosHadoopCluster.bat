@echo off
@rem # 脚本功能: 批量以无界面的方式启动VBox虚拟机
@rem 
@rem # 命令说明:
@rem # 以无界面的方式启动VBox虚拟机: -type headless 
@rem # VBoxManage.exe startvm vmName -type headless 
@rem # or
@rem # VBoxHeadless -startvm vmName #启动后会阻塞控制台


@rem 要运行的虚拟机:CentOS_HadoopSlave2, CentOS_HadoopSlave1, CentOS_HadoopMaster

@rem 在启动虚拟机之前先检查虚拟机是否在运行
set tmpFile="./tmpfile.tmp"
if exist %tmpFile% (del %tmpFile%)

@rem 获取正在运行的虚拟机信息
"C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" list runningvms > %tmpFile%

set exist=0
for /F %%i in ('findstr "CentOS_HadoopSlave2" %tmpFile%') do (set exist=1)
if %exist% == 0 (
    echo "以无界面的方式启动虚拟机: CentOS_HadoopSlave2"
    "C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" startvm CentOS_HadoopSlave2 -type headless 
)^
else (
    echo "CentOS_HadoopSlave2 虚拟机已经在运行"
)

set exist=0
for /F %%i in ('findstr "CentOS_HadoopSlave1" %tmpFile%') do (set exist=1)
if %exist% == 0 (
    echo "以无界面的方式启动虚拟机: CentOS_HadoopSlave1"
    "C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" startvm CentOS_HadoopSlave1 -type headless 
)^
else (
    echo "CentOS_HadoopSlave1 虚拟机已经在运行"
)

set exist=0
for /F %%i in ('findstr "CentOS_HadoopMaster" %tmpFile%') do (set exist=1)
if %exist% == 0 (
    echo "以无界面的方式启动虚拟机: CentOS_HadoopMaster"
    "C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" startvm CentOS_HadoopMaster -type headless 
)^
else (
    echo "CentOS_HadoopMaster 虚拟机已经在运行"
)

del %tmpFile%
@rem 在用户登录时自动执行该脚本时可带'auto_enter'参数
if "%1" neq "auto_enter" (
    pause
)

@echo on

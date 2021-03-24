@echo off
@rem # 脚本功能: 批量保存VBox虚拟机状态,并停止虚拟机
@rem 
@rem # 命令说明:
@rem # 将虚拟机的当前状态保存到磁盘并停止虚拟机
@rem # VBoxManage controlvm <vm_name> savestate 
@rem # or
@rem # 关闭虚拟机
@rem # VBoxManage controlvm <vm_name> acpipowerbutton


@rem 要保存并退出的虚拟机:CentOS_HadoopSlave2, CentOS_HadoopSlave1, CentOS_HadoopMaster

@rem 停止虚拟机之前先检查虚拟机是否在运行
set tmpFile="./tmpfile.tmp"
if exist %tmpFile% (del %tmpFile%)

@rem 获取正在运行的虚拟机信息
"C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" list runningvms > %tmpFile%


set running=0
for /F %%i in ('findstr "CentOS_HadoopSlave2" %tmpFile%') do (set running=1)
if %running% == 1 (
    echo "保存虚拟机状态并休眠虚拟机: CentOS_HadoopSlave2"
    "C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" controlvm CentOS_HadoopSlave2 savestate
)^
else (
    echo "CentOS_HadoopSlave2 虚拟机不在运行状态"
)

set running=0
for /F %%i in ('findstr "CentOS_HadoopSlave1" %tmpFile%') do (set running=1)
if %running% == 1 (
    echo "保存虚拟机状态并休眠虚拟机: CentOS_HadoopSlave1"
    "C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" controlvm CentOS_HadoopSlave1 savestate
)^
else (
    echo "CentOS_HadoopSlave1 虚拟机不在运行状态"
)

set running=0
for /F %%i in ('findstr "CentOS_HadoopMaster" %tmpFile%') do (set running=1)
if %running% == 1 (
    echo "保存虚拟机状态并休眠虚拟机: CentOS_HadoopMaster"
    "C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" controlvm CentOS_HadoopMaster savestate
)^
else (
    echo "CentOS_HadoopMaster 虚拟机不在运行状态"
)

del %tmpFile%

@rem 在用户注销时自动执行该脚本时可带'auto_out'参数
if "%1" neq "auto_out" (
    pause
)
@echo on


@echo off
@rem # �ű�����: ��������VBox�����״̬,��ֹͣ�����
@rem 
@rem # ����˵��:
@rem # ��������ĵ�ǰ״̬���浽���̲�ֹͣ�����
@rem # VBoxManage controlvm <vm_name> savestate 
@rem # or
@rem # �ر������
@rem # VBoxManage controlvm <vm_name> acpipowerbutton


@rem Ҫ���沢�˳��������:CentOS_HadoopSlave2, CentOS_HadoopSlave1, CentOS_HadoopMaster

@rem ֹͣ�����֮ǰ�ȼ��������Ƿ�������
set tmpFile="./tmpfile.tmp"
if exist %tmpFile% (del %tmpFile%)

@rem ��ȡ�������е��������Ϣ
"C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" list runningvms > %tmpFile%


set running=0
for /F %%i in ('findstr "CentOS_HadoopSlave2" %tmpFile%') do (set running=1)
if %running% == 1 (
    echo "���������״̬�����������: CentOS_HadoopSlave2"
    "C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" controlvm CentOS_HadoopSlave2 savestate
)^
else (
    echo "CentOS_HadoopSlave2 �������������״̬"
)

set running=0
for /F %%i in ('findstr "CentOS_HadoopSlave1" %tmpFile%') do (set running=1)
if %running% == 1 (
    echo "���������״̬�����������: CentOS_HadoopSlave1"
    "C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" controlvm CentOS_HadoopSlave1 savestate
)^
else (
    echo "CentOS_HadoopSlave1 �������������״̬"
)

set running=0
for /F %%i in ('findstr "CentOS_HadoopMaster" %tmpFile%') do (set running=1)
if %running% == 1 (
    echo "���������״̬�����������: CentOS_HadoopMaster"
    "C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" controlvm CentOS_HadoopMaster savestate
)^
else (
    echo "CentOS_HadoopMaster �������������״̬"
)

del %tmpFile%

@rem ���û�ע��ʱ�Զ�ִ�иýű�ʱ�ɴ�'auto_out'����
if "%1" neq "auto_out" (
    pause
)
@echo on


@echo off
@rem # �ű�����: �������޽���ķ�ʽ����VBox�����
@rem 
@rem # ����˵��:
@rem # ���޽���ķ�ʽ����VBox�����: -type headless 
@rem # VBoxManage.exe startvm vmName -type headless 
@rem # or
@rem # VBoxHeadless -startvm vmName #���������������̨


@rem Ҫ���е������:CentOS_HadoopSlave2, CentOS_HadoopSlave1, CentOS_HadoopMaster

@rem �����������֮ǰ�ȼ��������Ƿ�������
set tmpFile="./tmpfile.tmp"
if exist %tmpFile% (del %tmpFile%)

@rem ��ȡ�������е��������Ϣ
"C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" list runningvms > %tmpFile%

set exist=0
for /F %%i in ('findstr "CentOS_HadoopSlave2" %tmpFile%') do (set exist=1)
if %exist% == 0 (
    echo "���޽���ķ�ʽ���������: CentOS_HadoopSlave2"
    "C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" startvm CentOS_HadoopSlave2 -type headless 
)^
else (
    echo "CentOS_HadoopSlave2 ������Ѿ�������"
)

set exist=0
for /F %%i in ('findstr "CentOS_HadoopSlave1" %tmpFile%') do (set exist=1)
if %exist% == 0 (
    echo "���޽���ķ�ʽ���������: CentOS_HadoopSlave1"
    "C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" startvm CentOS_HadoopSlave1 -type headless 
)^
else (
    echo "CentOS_HadoopSlave1 ������Ѿ�������"
)

set exist=0
for /F %%i in ('findstr "CentOS_HadoopMaster" %tmpFile%') do (set exist=1)
if %exist% == 0 (
    echo "���޽���ķ�ʽ���������: CentOS_HadoopMaster"
    "C:/Program Files/Oracle/VirtualBox/VBoxManage.exe" startvm CentOS_HadoopMaster -type headless 
)^
else (
    echo "CentOS_HadoopMaster ������Ѿ�������"
)

del %tmpFile%
@rem ���û���¼ʱ�Զ�ִ�иýű�ʱ�ɴ�'auto_enter'����
if "%1" neq "auto_enter" (
    pause
)

@echo on

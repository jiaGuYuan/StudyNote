@echo off
@rem # ��1������ָ��jupyter�Ĺ���Ŀ¼


set JupyterDir=%1

if "%JupyterDir%"=="" (
    @rem #���뵽�ű�����Ŀ¼, ��ʹ�Թ���Ա���ִ�нű�Ҳ����ȷ��λ����ǰĿ¼
    cd /D %~dp0
    set JupyterDir="./"
)

jupyter notebook --notebook-dir=%JupyterDir%
@echo on
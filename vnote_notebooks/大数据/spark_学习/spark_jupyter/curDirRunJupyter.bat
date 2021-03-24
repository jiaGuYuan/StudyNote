@echo off
@rem # 第1个参数指定jupyter的工作目录


set JupyterDir=%1

if "%JupyterDir%"=="" (
    @rem #进入到脚本所在目录, 即使以管理员身份执行脚本也能正确定位到当前目录
    cd /D %~dp0
    set JupyterDir="./"
)

jupyter notebook --notebook-dir=%JupyterDir%
@echo on
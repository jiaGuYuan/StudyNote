@echo off
@rem # 脚本功能: 启动Oracle服务 --需以管理员方式运行
@rem 
@rem # 命令说明:

net stop "OracleOraDb11g_home1TNSListener"
net stop "OracleServiceORACLE"

@rem # net stop "OracleOraDb11g_home1ClrAgent"
@rem # net stop "OracleMTSRecoveryService"
@rem # net stop "OracleJobSchedulerORACLE"
@rem # net stop "OracleDBConsoleoracle"
@rem # net stop "OracleVssWriterORACLE"

pause
@echo on

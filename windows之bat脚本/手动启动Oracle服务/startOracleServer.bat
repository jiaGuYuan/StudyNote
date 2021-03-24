@echo off
@rem # 脚本功能: 启动Oracle服务 --需以管理员方式运行
@rem 
@rem # 命令说明:

net start "OracleOraDb11g_home1TNSListener"
net start "OracleServiceORACLE"

@rem # net start "OracleOraDb11g_home1ClrAgent"
@rem # net start "OracleMTSRecoveryService"
@rem # net start "OracleJobSchedulerORACLE"
@rem # net start "OracleDBConsoleoracle"
@rem # net start "OracleVssWriterORACLE"

pause
@echo on

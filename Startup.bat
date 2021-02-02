@echo off
title ������

rem # GC��־�ļ���λ��
set GC_LOG=..\log4j2\gc\gamegc.log
rem # ���ô�����־·��
set ERROR_LOG=..\log4j2\error\error.log
rem # ���ô�����־·��
set OOM_LOG=..\log4j2\error\heap_dump.hprof

set CPATH=./build/Database-mybatis1.0.0.jar;.;./resources/;./libs/*
set RUN_PARAM=0
set MAIN=com.perfect.MybatisStartup


set JVM_PARAM=-server -Xms256m -Xmx512m -XX:MaxDirectMemorySize=64m -XX:+UseG1GC -XX:MaxGCPauseMillis=128 -XX:+DisableExplicitGC -XX:+HeapDumpOnOutOfMemoryError

set LOG_PARAMS=%LOG_PARAMS% -Xlog:gc*:%GC_LOG%:time,level,tags
set LOG_PARAMS=%LOG_PARAMS% -XX:ErrorFile=%ERROR_LOG%
set LOG_PARAMS=%LOG_PARAMS% -XX:HeapDumpPath=.%OOM_LOG%

set AGENT=-javaagent:./libs/classReloader.jar

echo ===============================================================================
echo.
echo   Server Environment
echo.
echo   JAVA_RUN:%CPATH% %JVM_PARAM% %LOG_PARAMS% %AGENT% %MAIN% %RUN_PARAM%
echo.
echo ===============================================================================
echo.

rem # java -cp %CPATH% %JVM_PARAM% %LOG_PARAMS% %AGENT% %MAIN% %RUN_PARAM%

pause
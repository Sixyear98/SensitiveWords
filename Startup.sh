#!/bin/bash
source ~/.bash_profile

# 设置或显示环境变量
export LD_LIBRARY_PATH=./libs:$LD_LIBRARY_PATH

# GC日志文件的位置
GC_LOG=..\log4j2\gc\gamegc
# 设置错误日志路径
ERROR_LOG=..\log4j2\error\error
# 设置错误日志路径
OOM_LOG=..\log4j2\error\heap_dump


# 设置加载类的环境变量 运行主类名
CPATH="./build/Database-mybatis1.0.0.jar;.;./resources/;./libs/*"
RUN_PARAM="0"
MAIN="org.jow.gamesrv.main.GameStartup"


# 设置运行参数
JVM_PARAM=-server -Xms256m -Xmx512m -XX:MaxDirectMemorySize=64m -XX:+UseG1GC -XX:MaxGCPauseMillis=256 -XX:+DisableExplicitGC -XX:+HeapDumpOnOutOfMemoryError

LOG_PARAMS="$LOG_PARAMS -Xlog:gc*:$GC_LOG_%p_%t.log:time,level,tags"
LOG_PARAMS="$LOG_PARAMS -XX:ErrorFile=$ERROR_LOG_%p.log"
LOG_PARAMS="$LOG_PARAMS -XX:HeapDumpPath=$OOM_LOG_%p.hprof"


# 热更新.jar包
AGENT=-javaagent:./libs/classReloader.jar

echo "========================================================================="
echo ""
echo "Server Environment"
echo ""
echo "JAVA_RUN:$CPATH $JVM_PARAM $LOG_PARAMS $AGENT $MAIN $RUN_PARAM"
echo ""
echo "========================================================================="
echo ""

>./console.log

nohup java -cp %CPATH% %JVM_PARAM% %LOG_PARAMS% %AGENT% %MAIN% %RUN_PARAM% >>./console.log & echo $! > pid.log &

tail -f console.log

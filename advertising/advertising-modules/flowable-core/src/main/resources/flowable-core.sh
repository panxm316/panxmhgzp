#!/bin/bash
# author:petterobam
# url:https://github.com/petterobam/flowable-core
# Usage: sh flowable-core.sh start "-Xms128m -Xmx128m"
# Usage: sh flowable-core.sh stop
# Usage: sh flowable-core.sh status
# Usage: sh flowable-core.sh reload 10
# Usage: sh flowable-core.sh log
# env_args="-Xms128m -Xmx128m"
env_args=""
sleeptime=0
arglen=$#
# get flowable-core pid
get_pid(){
pname="`find . -name 'flowable-core.jar'`"
pname=${pname:3}
pid=`ps -ef | grep $pname | grep -v grep | awk '{print $2}'`
echo "$pid"
}
startup(){
pid=$(get_pid)
if [ "$pid" != "" ]
then
echo "flowable-core already startup!"
else
jar_path=`find . -name 'flowable-core.jar'`
echo "jarfile=$jar_path"
cmd="java $1 -jar $jar_path > ./flowable-core.out < /dev/null &"
echo "cmd: $cmd"
nohup java $1 -jar $jar_path > ./flowable-core.out < /dev/null &
echo "---------------------------------"
echo "启动完成，按CTRL+C退出日志界面即可>>>>>"
echo "---------------------------------"
show_log
fi
}
shut_down(){
pid=$(get_pid)
if [ "$pid" != "" ]
then
kill -9 $pid
echo "flowable-core is stop!"
else
echo "flowable-core already stop!"
fi
}
show_log(){
tail -f flowable-core.out
}
show_help(){
echo -e "\r\n\t欢迎使用 flowable-core"
echo -e "\r\nUsage: sh flowable-core.sh start|stop|reload|status|log"
exit
}
show_status(){
pid=$(get_pid)
if [ "$pid" != "" ]
then
echo "flowable-core is running with pid: $pid"
else
echo "flowable-core is stop!"
fi
}
if [ $arglen -eq 0 ]
then
show_help
else
if [ "$2" != "" ]
then
env_args="$2"
fi
case "$1" in
"start")
startup "$env_args"
;;
"stop")
shut_down
;;
"reload")
shut_down
startup "$env_args"
;;
"status")
show_status
;;
"log")
show_log
;;
esac
fi

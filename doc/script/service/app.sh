#!/bin/bash
#
# Bedrock 业务模块主机启动脚本（与 jar 同目录部署）
#
# 用法：sh app.sh [start|stop|restart|status]
#
# PID 约定：启动后在本脚本同目录写入 ${APP_NAME%.jar}.pid（如 bedrock-gateway.pid）
# 供 systemd Type=forking + PIDFile 使用；不能把本脚本当 Type=simple 前台进程，
# 因为 start 使用 nohup 后台启动后脚本会立刻退出。完整单元模板见同目录 app.service。
#

# 设置 jar 文件名、端口、激活环境（各模块按需修改）
APP_NAME=bedrock-gateway.jar
APP_PORT=29000
APP_ACTIVE=prod

# 脚本所在目录与 PID 文件（与 jar 同目录）
APP_HOME="$(cd "$(dirname "$0")" && pwd)"
PID_FILE="${APP_HOME}/${APP_NAME%.jar}.pid"

# 使用说明
usage() {
  echo "Usage: sh app.sh [start|stop|restart|status]"
  exit 1
}

# 判断进程是否在跑：优先读 PID 文件，失效则清理
is_exist() {
  pid=""
  if [ -f "${PID_FILE}" ]; then
    pid="$(cat "${PID_FILE}")"
    if [ -n "${pid}" ] && kill -0 "${pid}" 2>/dev/null; then
      return 0
    fi
    rm -f "${PID_FILE}"
  fi
  return 1
}

# 后台启动并写入 PID 文件
start() {
  is_exist
  if [ $? -eq 0 ]; then
    echo "${APP_NAME} is already running. pid=${pid}"
  else
    nohup java -Xms1024m -Xmx1024m -jar "${APP_HOME}/${APP_NAME}" \
      --spring.profiles.active="${APP_ACTIVE}" \
      --server.port="${APP_PORT}" > "${APP_HOME}/log.out" 2>&1 &
    echo $! > "${PID_FILE}"
    echo "${APP_NAME} started. pid=$(cat "${PID_FILE}")"
  fi
}

# 先 SIGTERM，超时后再强制结束，并删除 PID 文件
stop() {
  is_exist
  if [ $? -eq 0 ]; then
    kill "${pid}"
    for i in 1 2 3 4 5 6 7 8 9 10; do
      sleep 1
      is_exist
      if [ $? -ne 0 ]; then
        rm -f "${PID_FILE}"
        echo "${APP_NAME} stopped"
        return
      fi
    done
    kill -9 "${pid}" 2>/dev/null
    rm -f "${PID_FILE}"
    echo "${APP_NAME} killed"
  else
    echo "${APP_NAME} is not running"
  fi
}

# 输出运行状态
status() {
  is_exist
  if [ $? -eq 0 ]; then
    echo "${APP_NAME} is running. Pid is ${pid}"
  else
    echo "${APP_NAME} is NOT running."
  fi
}

# 重启
restart() {
  stop
  start
}

case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  *)
    usage
    ;;
esac

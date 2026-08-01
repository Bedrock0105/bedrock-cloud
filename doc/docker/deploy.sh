#!/bin/sh
# ============================================================
# Bedrock Cloud Docker 部署脚本（对齐 RuoYi-Cloud deploy.sh 风格）
#
# 用法：
#   sh deploy.sh [port|base|copy|build|modules|stop|rm|restart]
#
# 推荐流程：
#   1. 在 bedrock-cloud 根目录：mvn clean package（需先安装 bedrock-commons）
#   2. sh deploy.sh base
#      - MySQL 首次启动会自动执行 mysql/db/01-bedrock.sql、02-bedrock-ai.sql
#      - 打开 http://localhost:18080/nacos （nacos/nacos）
#      - 创建命名空间 ID：790c0609-a49f-4fef-b56b-88788f7292bb
#      - 导入 nacos/config/application-dev.yml
#   3. sh deploy.sh copy
#   4. sh deploy.sh build
#   5. sh deploy.sh modules
#
# 说明：Windows 请使用 Git Bash / WSL；生产环境建议在 Linux 上执行
# ============================================================

# 进入脚本所在目录，保证相对路径正确
cd "$(dirname "$0")" || exit 1

# 优先使用 docker compose 插件，其次 docker-compose 命令
if docker compose version >/dev/null 2>&1; then
  COMPOSE="docker compose"
elif command -v docker-compose >/dev/null 2>&1; then
  COMPOSE="docker-compose"
else
  echo "未找到 docker compose / docker-compose，请先安装 Docker"
  exit 1
fi

# -------------------- 使用说明 --------------------
usage() {
  echo "Usage: sh deploy.sh [port|base|copy|build|modules|stop|rm|restart]"
  echo "  port     开放防火墙常用端口（需 root，仅 Linux firewalld）"
  echo "  base     启动基础中间件：mysql / redis / nacos"
  echo "  copy     复制 jar 到各模块 jar/ 目录"
  echo "  build    构建全部镜像"
  echo "  modules  启动业务模块 + nginx"
  echo "  stop     停止全部容器"
  echo "  rm       删除全部容器"
  echo "  restart  重启全部服务"
  exit 1
}

# -------------------- 开放端口（可选，firewalld） --------------------
port() {
  echo ">>> 开放防火墙端口（firewalld）..."
  firewall-cmd --add-port=80/tcp --permanent
  firewall-cmd --add-port=8080/tcp --permanent
  firewall-cmd --add-port=8081/tcp --permanent
  firewall-cmd --add-port=8082/tcp --permanent
  firewall-cmd --add-port=8083/tcp --permanent
  firewall-cmd --add-port=8084/tcp --permanent
  firewall-cmd --add-port=8085/tcp --permanent
  firewall-cmd --add-port=8089/tcp --permanent
  firewall-cmd --add-port=8848/tcp --permanent
  firewall-cmd --add-port=9848/tcp --permanent
  firewall-cmd --add-port=18080/tcp --permanent
  firewall-cmd --add-port=6379/tcp --permanent
  firewall-cmd --add-port=3306/tcp --permanent
  firewall-cmd --reload
  echo ">>> 端口已开放"
}

# -------------------- 启动基础环境 --------------------
base() {
  echo ">>> 启动基础中间件：mysql / redis / nacos ..."
  echo ">>> 提示：MySQL 仅在数据目录为空时自动执行 mysql/db 初始化脚本"
  $COMPOSE up -d mysql redis nacos
  echo ">>> 基础环境已启动"
  echo ">>> 下一步："
  echo "    1) 等待 MySQL / Nacos 就绪"
  echo "    2) 访问 http://localhost:18080/nacos 账号 nacos/nacos"
  echo "    3) 创建命名空间 790c0609-a49f-4fef-b56b-88788f7292bb"
  echo "    4) 导入 ./nacos/config/application-dev.yml"
  echo "    5) sh deploy.sh copy && sh deploy.sh build && sh deploy.sh modules"
}

# -------------------- 复制 jar --------------------
copy() {
  echo ">>> 复制 jar ..."
  sh ./copy.sh
}

# -------------------- 构建镜像 --------------------
build() {
  echo ">>> 构建镜像 ..."
  $COMPOSE build
  echo ">>> 构建完成"
}

# -------------------- 启动业务模块 --------------------
modules() {
  echo ">>> 启动业务模块 + nginx ..."
  $COMPOSE up -d bedrock-gateway bedrock-auth bedrock-system bedrock-resource bedrock-log bedrock-ai bedrock-websocket nginx
  echo ">>> 业务模块已启动"
  echo ">>> 访问入口：http://localhost/"
  echo ">>> 网关直连：http://localhost:8080/doc.html"
  echo ">>> Nacos 控制台：http://localhost:18080/nacos"
}

# -------------------- 停止 --------------------
stop() {
  echo ">>> 停止全部容器 ..."
  $COMPOSE stop
}

# -------------------- 删除容器 --------------------
rm() {
  echo ">>> 删除全部容器 ..."
  $COMPOSE rm -f
}

# -------------------- 重启 --------------------
restart() {
  echo ">>> 重启全部服务 ..."
  $COMPOSE restart
}

# -------------------- 入口 --------------------
case "$1" in
  "port")
    port
    ;;
  "base")
    base
    ;;
  "copy")
    copy
    ;;
  "build")
    build
    ;;
  "modules")
    modules
    ;;
  "stop")
    stop
    ;;
  "rm")
    rm
    ;;
  "restart")
    restart
    ;;
  *)
    usage
    ;;
esac

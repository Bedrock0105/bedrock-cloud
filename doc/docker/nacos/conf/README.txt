# ============================================================
# Nacos 可选自定义配置目录
# 当前使用官方镜像默认配置 + compose 环境变量（MODE=standalone / 鉴权）
# 如需覆盖 application.properties，可将文件放于此目录并在 docker-compose.yml 中挂载：
#   - ./nacos/conf/application.properties:/home/nacos/conf/application.properties
# Docker 版业务共享配置见：../config/application-dev.yml
# ============================================================

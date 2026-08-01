# ============================================================
# Nacos 镜像：配置中心 + 服务发现
# 版本：nacos/nacos-server:v3.1.1（standalone）
# 控制台：宿主机 18080 -> 容器 8080（避免与 gateway 8080 冲突）
# API：8848 / 9848
# ============================================================

# 基础镜像（固定 3.1.1）
FROM nacos/nacos-server:v3.1.1

# 维护者 / 模块说明
LABEL maintainer="bedrock" \
      description="Bedrock Cloud Nacos v3.1.1 standalone"

# 单机模式（内嵌存储，无需额外 MySQL 存 Nacos 自身数据）
ENV MODE=standalone \
    PREFER_HOST_MODE=hostname \
    TZ=Asia/Shanghai

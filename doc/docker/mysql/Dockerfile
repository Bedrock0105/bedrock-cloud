# ============================================================
# MySQL 镜像：业务库（bedrock）
# 版本要求：MySQL 8.0 及以上，此处使用 8.4
# 初始化：compose 将 ./db 挂载到 /docker-entrypoint-initdb.d
# ============================================================

# 基础镜像
FROM mysql:8.4

# 维护者 / 模块说明
LABEL maintainer="bedrock" \
      description="Bedrock Cloud MySQL 8.4"

# 时区（可选，便于日志对齐业务时区）
ENV TZ=Asia/Shanghai

# 复制自定义配置（字符集、连接等）
COPY ./conf/my.cnf /etc/mysql/conf.d/my.cnf

# ============================================================
# Redis 镜像：缓存 / 会话 / Stream 等
# 版本要求：Redis 8.0 及以上
# ============================================================

# 基础镜像
FROM redis:8

# 维护者 / 模块说明
LABEL maintainer="bedrock" \
      description="Bedrock Cloud Redis 8"

# 时区
ENV TZ=Asia/Shanghai

# 复制自定义配置（密码、持久化等）
COPY ./conf/redis.conf /usr/local/etc/redis/redis.conf

# 使用自定义配置启动
CMD ["redis-server", "/usr/local/etc/redis/redis.conf"]

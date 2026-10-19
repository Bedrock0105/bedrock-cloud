# ============================================================
# 业务模块：bedrock-auth（认证授权）
# 端口：8081
# 构建前请先执行 copy.sh，将 jar 放入本目录 jar/
# ============================================================

# 基础镜像：Java 17 JRE
FROM eclipse-temurin:17-jre

# 维护者 / 模块说明
LABEL maintainer="bedrock" \
      description="Bedrock Cloud Auth"

# 工作目录
WORKDIR /home/bedrock

# 复制预先打包好的 jar（由 copy.sh 放入 jar/）
COPY ./jar/bedrock-auth.jar ./app.jar

# JVM / profile / Nacos / Sentinel：可由 compose 覆盖；启动时由 NacosStartConfiguration、SentinelStartConfiguration 读入
# 不再通过命令行传 --spring.profiles.active / nacos server-addr
ENV JAVA_OPTS="-Xms512m -Xmx512m" \
    SPRING_PROFILES_ACTIVE=dev \
    NACOS_ADDRESS=nacos:8848 \
    NACOS_USERNAME=nacos \
    NACOS_PASSWORD=nacos \
    NACOS_NAMESPACE=790c0609-a49f-4fef-b56b-88788f7292bb \
    SENTINEL_ADDRESS=127.0.0.1:7080 \
    SENTINEL_LOG_DIR=/home/bedrock/logs/csp/ \
    TZ=Asia/Shanghai

# 暴露服务端口
EXPOSE 8081

# 仅启动 jar；profile / Nacos / Sentinel 依赖上方环境变量
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

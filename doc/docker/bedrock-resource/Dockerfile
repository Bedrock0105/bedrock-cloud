# ============================================================
# 业务模块：bedrock-resource（资源 / 文件）
# 端口：8083
# 构建前请先执行 copy.sh，将 jar 放入本目录 jar/
# ============================================================

# 基础镜像：Java 17 JRE
FROM eclipse-temurin:17-jre

# 维护者 / 模块说明
LABEL maintainer="bedrock" \
      description="Bedrock Cloud Resource"

# 工作目录
WORKDIR /home/bedrock

# 复制预先打包好的 jar（由 copy.sh 放入 jar/）
COPY ./jar/bedrock-resource.jar ./app.jar

# JVM 参数，可在 compose 中通过环境变量覆盖
ENV JAVA_OPTS="-Xms512m -Xmx512m" \
    SPRING_PROFILES_ACTIVE=dev \
    NACOS_ADDR=nacos:8848 \
    TZ=Asia/Shanghai

# 暴露服务端口
EXPOSE 8083

# 启动服务；用命令行覆盖 Nacos 地址（覆盖代码里的 127.0.0.1 默认值）
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE} --spring.cloud.nacos.config.server-addr=${NACOS_ADDR} --spring.cloud.nacos.discovery.server-addr=${NACOS_ADDR}"]

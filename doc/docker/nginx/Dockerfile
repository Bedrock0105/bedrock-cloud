# ============================================================
# Nginx 镜像：前端静态资源 + 反向代理网关 / WebSocket
# 基础镜像：nginx:latest
# ============================================================

# 基础镜像（始终使用官方最新稳定线 latest）
FROM nginx:latest

# 维护者 / 模块说明
LABEL maintainer="bedrock" \
      description="Bedrock Cloud Nginx reverse proxy"

# 时区
ENV TZ=Asia/Shanghai

# 删除默认站点配置，使用自定义 conf
RUN rm -f /etc/nginx/conf.d/default.conf

# 复制自定义 Nginx 配置
COPY ./conf/nginx.conf /etc/nginx/nginx.conf

# 前端构建产物目录（将 bedrock-ui 的 dist 内容拷入 html/）
COPY ./html /usr/share/nginx/html

# 暴露 HTTP 端口
EXPOSE 80

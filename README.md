# Bedrock Cloud

## 📖 项目介绍

Bedrock Cloud 是一个基于 **Spring Cloud** 微服务架构的企业级云服务平台。该项目采用前后端分离的分布式微服务设计，集成了认证授权、系统管理、资源管理、日志记录、WebSocket 实时通信、**AI 对话与知识库**等核心功能模块，为企业应用提供完整的基础设施支撑。
本项目基于 [bedrock-commons](https://github.com/Bedrock0105/bedrock-commons.git) 基础框架组件库构建，充分利用了其提供的通用功能模块和最佳实践（含 `bedrock-common-ai` / Spring AI）。

### 🔗 相关仓库

| 项目 | 地址 |
|------|------|
| 前端 bedrock-ui | https://github.com/Bedrock0105/bedrock-ui.git |
| 公共组件 bedrock-commons | https://github.com/Bedrock0105/bedrock-commons.git |

### ✨ 核心特性

- 🏗️ **微服务架构**：基于 Spring Cloud Gateway + Nacos + OpenFeign 的微服务架构
- 🔐 **统一认证**：OAuth2 + JWT 实现统一的认证授权中心
- 🚪 **API 网关**：Spring Cloud Gateway 实现路由转发、鉴权、限流等功能
- 📦 **模块化设计**：清晰的模块划分，支持独立部署和扩展
- 🔄 **服务治理**：集成 Nacos 服务注册发现与配置中心
- 📊 **系统管理**：完善的用户、角色、权限、组织、字典等管理功能
- 💾 **资源管理**：支持 OSS 对象存储配置与管理
- 📝 **日志追踪**：操作日志记录与查询
- 🔌 **WebSocket**：实时双向通信支持
- 🤖 **AI 能力**：多厂商模型、流式对话、知识库 RAG、向量库、MCP/工具、Token 统计
- 📚 **API 文档**：集成 Knife4j 自动生成接口文档
- 🏢 **多租户**：支持多租户数据隔离
- 🎯 **代码生成**：基于 bedrock-commons 的代码生成能力

---

## 🛠️ 技术栈

### 基础环境

- **Java 版本**：JDK 17+
- **构建工具**：Maven 3.6+
- **编码格式**：UTF-8

### 核心框架

| 框架 | 说明 |
|------|------|
| Spring Boot | 应用框架 |
| Spring Cloud | 微服务框架 |
| Spring Cloud Alibaba | 阿里巴巴微服务解决方案 |
| Nacos | 服务注册发现与配置中心 |
| Spring Cloud Gateway | API 网关 |
| OpenFeign | 声明式服务调用 |
| OAuth2 + JWT | 认证授权 |

### 数据存储

| 组件 | 说明 |
|------|------|
| MySQL | 关系型数据库 |
| Redis | 缓存与会话存储 |
| MyBatis-Plus | ORM 框架 |
| Druid | 数据库连接池 |

### 其他组件

| 组件 | 说明 |
|------|------|
| Knife4j | API 文档增强 |
| Lombok | 简化 Java 代码 |
| FastExcel | Excel 处理 |
| Protostuff | 序列化框架 |
| Spring AI | 大模型、向量库、Advisor、MCP（经 `bedrock-common-ai`） |
| Redis Stack（可选） | RediSearch + RedisJSON，用作向量库后端 |

---

## 📦 项目结构

```
bedrock-cloud/
├── bedrock-api/              # API 接口定义模块（Feign 客户端）
│   ├── bedrock-system-api/   # 系统管理 API
│   ├── bedrock-resource-api/ # 资源管理 API
│   ├── bedrock-log-api/      # 日志管理 API
│   ├── bedrock-websocket-api/# WebSocket API
│   └── bedrock-ai-api/       # AI 服务 API（实体 / 参数 / VO）
├── bedrock-auth/             # 认证授权服务
├── bedrock-common/           # 公共模块
├── bedrock-gateway/          # API 网关服务
├── bedrock-modules/          # 业务模块
│   ├── bedrock-system/       # 系统管理服务
│   ├── bedrock-resource/     # 资源管理服务
│   ├── bedrock-log/          # 日志管理服务
│   ├── bedrock-websocket/    # WebSocket 服务
│   └── bedrock-ai/           # AI 服务（对话 / 知识库 / MCP）
├── doc/                      # 文档与配置
│   ├── nacos/                # Nacos 配置文件
│   └── sql/                  # 数据库脚本（含 bedrock-ai.sql）
└── pom.xml                   # 父 POM
```

---

## 📋 模块说明

### 1. bedrock-api - API 接口定义模块

**描述**：定义了各个微服务之间的 Feign 客户端接口、参数对象和返回值对象，用于服务间调用。

**子模块**：

#### bedrock-system-api
- **功能**：系统管理相关的 Feign 客户端接口
- **包含内容**：
  - 管理员管理（IAdminClient）
  - 在线用户管理（IAdminOnlineClient）
  - 字典管理（IDictClient）
  - 系统管理（ISystemClient）
  - 实体类：Admin、Role、Menu、Dept、Dict、Tenant 等
  - 参数对象和 VO 对象

#### bedrock-resource-api
- **功能**：资源管理相关的 Feign 客户端接口
- **包含内容**：
  - OSS 配置管理
  - 资源上传下载接口
  - 实体类：OssConfig
  - 参数对象和 VO 对象

#### bedrock-log-api
- **功能**：日志管理相关的 Feign 客户端接口
- **包含内容**：
  - 操作日志记录
  - 日志查询接口
  - 参数对象和 VO 对象

#### bedrock-websocket-api
- **功能**：WebSocket 相关的 Feign 客户端接口
- **包含内容**：
  - WebSocket 消息推送
  - 在线用户管理
  - DTO 和枚举定义

#### bedrock-ai-api
- **功能**：AI 服务相关的实体、参数、VO、枚举与常量（供 `bedrock-ai` 及前端契约复用）
- **包含内容**：
  - 实体：`AiApiKey`、`AiModel`、`AiRole`、`AiChatRecord`、`AiChatMessage`、`AiKnowledge*`、`AiVectorDb`、`AiMcp`、`AiTokenUsage` 等
  - 对话 / 知识库 / Token 等 Param、VO
  - 附件 DTO：`UserMessageAttachment`
  - 字典与错误码：`AiPlatform`、模型类型、切片模式等

---

### 2. bedrock-auth - 认证授权服务

**描述**：基于 OAuth2 + JWT 的统一认证授权中心，负责用户登录认证、Token 发放与验证。

**核心功能**：
- OAuth2 密码模式认证
- JWT Token 生成与刷新
- 客户端管理
- 用户认证信息封装
- 支持多种授权方式扩展

**技术栈**：
- bedrock-common-cloud（微服务基础）
- bedrock-common-authentication（OAuth2 认证）
- bedrock-system-api（用户信息服务）

**主要类**：
- `AuthApplication`：启动类
- `AuthController`：认证控制器
- `AuthService`：认证服务
- `JwtUtil`：JWT 工具类

**端口**：默认从 Nacos 配置中获取

---

### 3. bedrock-gateway - API 网关服务

**描述**：基于 Spring Cloud Gateway 的 API 网关，提供统一的路由转发、鉴权、限流、日志等功能。

**核心功能**：
- 动态路由配置
- 统一身份认证与鉴权
- 请求过滤与拦截
- 负载均衡
- API 文档聚合（Knife4j Gateway）
- 跨域处理
- 异常统一处理

**技术栈**：
- Spring Cloud Gateway（响应式网关）
- Spring WebFlux
- LoadBalancer（负载均衡）
- Redis（会话与限流）
- Nacos（服务发现）
- Knife4j Gateway（文档聚合）

**主要组件**：
- `GatewayApplication`：启动类
- 全局过滤器：认证过滤、日志过滤等
- 路由配置
- 异常处理器

**特点**：
- 排除 MySQL、Web MVC 等不需要的依赖
- 使用响应式编程模型
- 支持动态路由刷新

---

### 4. bedrock-common - 公共模块

**描述**：项目公共基础模块，提供通用的配置、工具类和依赖。

**核心功能**：
- 数据库驱动（MySQL）
- 序列化工具（Protostuff）
- 公共代码模块引用
- SPI 扩展支持

**依赖**：
- bedrock-common-code（来自 bedrock-commons）
- bedrock-common-spi（来自 bedrock-commons）
- MySQL Connector
- Protostuff 序列化

---

### 5. bedrock-modules - 业务模块集合

**描述**：包含所有业务微服务模块的父模块。

#### 5.1 bedrock-system - 系统管理服务

**功能**：提供完整的系统管理功能，包括用户、角色、权限、菜单、部门、字典、租户等管理。

**核心功能**：
- **管理员管理**：用户 CRUD、密码重置、状态管理
- **角色管理**：角色定义、权限分配
- **菜单管理**：菜单配置、权限控制
- **部门管理**：组织架构管理
- **字典管理**：数据字典维护
- **租户管理**：多租户配置与管理
- **在线用户**：在线用户查询与管理
- **参数配置**：系统参数配置

**技术栈**：
- bedrock-common-cloud（微服务基础）
- bedrock-system-api（API 定义）
- bedrock-common-excel（Excel 导入导出）

**主要实体**：
- `Admin`：管理员
- `Role`：角色
- `Menu`：菜单
- `Dept`：部门
- `Dict`：字典
- `Tenant`：租户
- `AdminRole`：用户角色关联
- `RoleMenu`：角色菜单关联

---

#### 5.2 bedrock-resource - 资源管理服务

**功能**：提供文件资源管理和 OSS 对象存储配置功能。

**核心功能**：
- OSS 配置管理（阿里云 OSS、腾讯云 COS 等）
- 文件上传下载
- 资源访问控制
- 存储配置切换

**技术栈**：
- bedrock-common-cloud（微服务基础）
- bedrock-resource-api（API 定义）

**主要实体**：
- `OssConfig`：OSS 配置信息

---

#### 5.3 bedrock-log - 日志管理服务

**功能**：提供操作日志记录与查询功能。

**核心功能**：
- 操作日志异步记录
- 日志查询与统计
- 日志导出
- 链路追踪

**技术栈**：
- bedrock-common-cloud（微服务基础）
- bedrock-log-api（API 定义）
- bedrock-common-log（日志组件）

---

#### 5.4 bedrock-websocket - WebSocket 服务

**功能**：提供 WebSocket 实时通信功能。

**核心功能**：
- WebSocket 连接管理
- 实时消息推送
- 在线用户状态维护
- 心跳检测
- 点对点与广播消息

**技术栈**：
- bedrock-common-cloud（微服务基础）
- bedrock-websocket-api（API 定义）
- bedrock-common-websocket（WebSocket 组件）

**应用场景**：
- 实时通知推送
- 在线聊天
- 实时数据更新
- 系统消息提醒

---

#### 5.5 bedrock-ai - AI 服务

**功能**：基于 Spring AI + `bedrock-common-ai` 的 AI 业务服务，提供多厂商模型接入、流式对话、知识库 RAG、向量库、MCP/工具与 Token 用量统计。

**核心功能**：
- **API Key / 模型管理**：多厂商密钥与 Chat / Embedding / Image / Speech 等模型配置
- **AI 角色**：系统提示词与角色预设
- **流式对话**：SSE（`/ai-chat-record/send-char-stream`），支持附件、历史记忆、知识库检索、工具与 MCP
- **创作能力**：图片生成、思维导图、文章等流式 / 同步生成接口
- **知识库 RAG**：知识库 / 文档 / 分块管理，文档解析与切片入库，对话时向量召回
- **向量库**：可配置 Redis / Milvus / ES / Simple（默认依赖含 Redis Store）
- **MCP / 本地工具**：动态 MCP 客户端 + `@ToolProvider` 本地工具
- **Token 统计**：按会话 / 模型统计用量与趋势

**技术栈**：
- `bedrock-common-cloud`（微服务基础）
- `bedrock-common-ai`（模型工厂、向量库工厂、Advisor、Tool / MCP）
- `bedrock-ai-api`（API 定义）
- `bedrock-resource-api`（附件 OSS）
- Spring AI（Tika 文档读取、MCP Client、Redis Vector Store 等）

**主要接口前缀**：

| 前缀 | 说明 |
|------|------|
| `/ai-api-key` | API Key |
| `/ai-model` | 模型 |
| `/ai-role` | AI 角色 |
| `/ai-chat-record` | 会话 / 流式发送 / 创作 |
| `/ai-chat-message` | 消息明细 |
| `/ai-knowledge` / `-doc` / `-chunk` | 知识库 |
| `/ai-vector-db` | 向量库配置 |
| `/ai-mcp` / `/ai-tool` | MCP 与工具 |
| `/ai-token-usage` | Token 用量 |

**启动类**：`org.bedrock.ai.AIApplication`（服务名 `bedrock-ai`）

**数据库脚本**：
- 全量：`doc/sql/mysql/bedrock-ai.sql`（表结构 + 菜单字典）
- 增量：`bedrock-ai-chat-message-rag-attachment.sql`、`*-menu.sql`、`*-dict.sql` 等

**与公共模块分工**：
- `bedrock-common-ai`：工厂、Advisor 骨架、SPI 契约
- `bedrock-ai`：配置落库、知识库策略、消息与 Token 持久化、具体增强与检索实现

公共能力说明见 [bedrock-commons / AI 公共模块](../bedrock-commons/doc/23-AI公共模块.md)。

---

## 🚀 快速开始

### 前置要求

- JDK 17+
- Maven 3.6+
- MySQL 5.7+ / 8.0+
- Redis 6.0+（若使用 Redis 向量库，需 **Redis Stack**：RediSearch + RedisJSON）
- Nacos 2.x+

### 1. 克隆并安装依赖

首先确保已安装 [bedrock-commons](https://gitee.com/your-repo/bedrock-commons) 到本地 Maven 仓库：

```bash
git clone https://gitee.com/your-repo/bedrock-commons.git
cd bedrock-commons
mvn clean install
```

### 2. 克隆项目

```bash
git clone https://gitee.com/your-repo/bedrock-cloud.git
cd bedrock-cloud
```

### 3. 初始化数据库

执行 `doc/sql/mysql/bedrock.sql` 脚本创建数据库和表：

```bash
mysql -u root -p < doc/sql/mysql/bedrock.sql
```

启用 AI 模块时，再执行 AI 脚本（表结构、菜单、字典）：

```bash
mysql -u root -p bedrock < doc/sql/mysql/bedrock-ai.sql
```

已有库升级可按需执行 `doc/sql/mysql/bedrock-ai-*.sql` 增量脚本。

### 4. 配置 Nacos

1. 启动 Nacos Server
2. 在 Nacos 配置中心导入 `doc/nacos/application-dev.yml` 配置
3. 根据实际环境修改数据库、Redis 等配置

### 5. 修改配置

根据实际情况修改 Nacos 中的配置：

```yaml
# 数据源配置
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/bedrock?...
    username: your_username
    password: your_password
  
  # Redis 配置
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password: your_redis_password
```

### 6. 编译打包

```bash
mvn clean package
```

### 7. 启动服务

建议按以下顺序启动服务：

```bash
# 1. 启动网关
java -jar bedrock-gateway/target/bedrock-gateway.jar

# 2. 启动认证服务
java -jar bedrock-auth/target/bedrock-auth.jar

# 3. 启动系统服务
java -jar bedrock-modules/bedrock-system/target/bedrock-system.jar

# 4. 启动资源服务
java -jar bedrock-modules/bedrock-resource/target/bedrock-resource.jar

# 5. 启动日志服务
java -jar bedrock-modules/bedrock-log/target/bedrock-log.jar

# 6. 启动 WebSocket 服务
java -jar bedrock-modules/bedrock-websocket/target/bedrock-websocket.jar

# 7. 启动 AI 服务（可选）
java -jar bedrock-modules/bedrock-ai/target/bedrock-ai.jar
```

或使用 IDE 直接运行各模块的 Application 启动类（AI：`AIApplication`）。

### 8. 访问服务

- **API 文档**：http://localhost:gateway-port/doc.html
- **Nacos 控制台**：http://localhost:8848/nacos
- **Spring Boot Admin**：如果配置了监控，可访问相应地址

---

## ⚙️ 配置说明

### Nacos 配置

项目使用 Nacos 作为配置中心和服务注册中心，主要配置项包括：

#### 应用配置（application-dev.yml）

```yaml
bedrock:
  security:
    # 不进行认证的接口
    ignore-urls:
      - /v3/api-docs
      - /oauth/token

# 数据源配置
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/bedrock?...
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  # Redis 配置
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password: password
      database: 0
      ssl:
        enabled: false
```

### 服务端口

各服务端口在 Nacos 配置中指定，建议规划如下：

| 服务 | 端口 | 说明 |
|------|------|------|
| bedrock-gateway | 8080 | API 网关 |
| bedrock-auth | 8081 | 认证服务 |
| bedrock-system | 8082 | 系统服务 |
| bedrock-resource | 8083 | 资源服务 |
| bedrock-log | 8084 | 日志服务 |
| bedrock-websocket | 8085 | WebSocket 服务 |
| bedrock-ai | 8086 | AI 服务（对话 / 知识库；本地 `application.yml` 默认 8085，建议 Nacos 改为不冲突端口） |

---

## 🔐 认证授权

### OAuth2 认证流程

1. 客户端请求 Token：`POST /oauth/token`
2. 携带用户名、密码、客户端信息
3. 获取 access_token 和 refresh_token
4. 后续请求在 Header 中携带 Token：`Authorization: Bearer {token}`

### 默认账号

数据库中预置了超级管理员账号：

- **用户名**：superadmin
- **密码**：需要查看数据库中的加密密码或联系管理员重置

### 客户端配置

默认客户端配置：

- **client_id**：artdesigpro
- **client_secret**：artdesigpro_secret
- **授权类型**：password, refresh_token

---

## 📊 数据库设计

### 核心表说明

| 表名 | 说明 |
|------|------|
| bedrock_admin | 管理员表 |
| bedrock_role | 角色表 |
| bedrock_menu | 菜单权限表 |
| bedrock_dept | 部门组织表 |
| bedrock_dict | 数据字典表 |
| bedrock_tenant | 租户表 |
| bedrock_client | OAuth2 客户端表 |
| bedrock_admin_role | 用户角色关联表 |
| bedrock_admin_dept | 用户部门关联表 |
| bedrock_role_menu | 角色菜单关联表 |
| bedrock_admin_online | 在线用户表 |
| bedrock_oss_config | OSS 配置表 |

#### AI 相关表（`doc/sql/mysql/bedrock-ai.sql`）

| 表名 | 说明 |
|------|------|
| bedrock_ai_api_key | AI API Key 配置 |
| bedrock_ai_model | AI 模型配置 |
| bedrock_ai_role | AI 角色 / 系统提示词 |
| bedrock_ai_chat_record | 聊天会话 |
| bedrock_ai_chat_message | 聊天消息（含 attachments / chunk_ids） |
| bedrock_ai_token_usage | Token 用量 |
| bedrock_ai_knowledge | 知识库 |
| bedrock_ai_knowledge_doc | 知识库文档 |
| bedrock_ai_knowledge_chunk | 知识库分块 |
| bedrock_ai_vector_db | 向量库连接配置 |
| bedrock_ai_mcp | MCP 服务配置 |

详细表结构请参考 `doc/sql/mysql/bedrock.sql` 与 `doc/sql/mysql/bedrock-ai.sql`

---

## 🔧 开发指南

### 添加新模块

1. 在 `bedrock-api` 中创建新的 API 模块
2. 定义 Feign 客户端接口、参数和返回值
3. 在 `bedrock-modules` 中创建实现模块
4. 实现业务逻辑
5. 在网关中配置路由（如需要）

### 服务间调用

使用 OpenFeign 进行服务间调用：

```java
@FeignClient("bedrock-system")
public interface ISystemClient {
    @GetMapping("/admin/info")
    Result<AdminVO> getAdminInfo(@RequestParam Long id);
}
```

### 权限控制

在 Controller 方法上添加权限注解：

```java
@PreAuthorize("@ss.hasPerm('system:admin:list')")
@GetMapping("/list")
public Result<List<AdminVO>> list() {
    // ...
}
```

### Excel 导入导出

使用 bedrock-common-excel 组件：

```java
@PostMapping("/export")
public void export(HttpServletResponse response) {
    ExcelUtils.export(response, "用户列表", dataList, AdminExportVO.class);
}
```

---

## 📝 API 文档

项目集成 Knife4j 自动生成 API 文档，启动服务后访问：

- **网关聚合文档**：http://localhost:gateway-port/doc.html
- **各服务独立文档**：http://localhost:service-port/doc.html

---

## 🐛 常见问题

### 1. 服务启动失败

- 检查 Nacos 是否正常运行
- 检查数据库、Redis 连接配置是否正确
- 检查端口是否被占用

### 2. 服务间调用失败

- 确认服务已在 Nacos 注册
- 检查 Feign 客户端定义是否正确
- 查看服务日志排查问题

### 3. 认证失败

- 检查 OAuth2 客户端配置
- 确认 Token 是否过期
- 检查 Redis 是否正常

### 4. 网关路由问题

- 检查 Nacos 中的路由配置
- 确认目标服务是否正常运行
- 查看网关日志

### 5. AI / 向量库相关

- **未执行 AI SQL**：先执行 `doc/sql/mysql/bedrock-ai.sql`
- **Redis 过滤字段报错**（`Not allowed filter identifier name` / `Unknown field ... knowledgeId`）：创建向量索引时需声明可过滤 metadata；旧索引需删除或换 indexName 后重建，详见 [AI 公共模块文档](../bedrock-commons/doc/23-AI公共模块.md)
- **流式对话无输出**：确认网关未缓冲 SSE，客户端按 `text/event-stream` 消费 `/ai-chat-record/send-char-stream`

---

## 🤝 参与贡献

我们欢迎任何形式的贡献！

1. **Fork** 本仓库
2. 新建 **Feat_xxx** 分支（Feature 功能 / Bugfix 修复）
3. 提交代码，确保代码符合规范
4. 新建 **Pull Request**
5. 等待代码审查和合并

### 贡献指南

- 遵循现有的代码风格和规范
- 添加必要的单元测试
- 更新相关文档
- 提交信息清晰明了

---

## 📄 License

本项目采用 MIT 开源协议，详见 [LICENSE](LICENSE) 文件。

---

**⭐ 如果这个项目对你有帮助，请给一个 Star！**

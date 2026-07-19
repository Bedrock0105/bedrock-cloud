/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50744
 Source Host           : 127.0.0.1:3306
 Source Schema         : bedrock

 Target Server Type    : MySQL
 Target Server Version : 50744
 File Encoding         : 65001

 Date: 19/07/2026 16:35:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bedrock_admin
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_admin`;
CREATE TABLE `bedrock_admin`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `avatar` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '性别',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `status` tinyint(1) DEFAULT 1 COMMENT '账号状态（1正常 0停用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '账号描述',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bedrock_admin
-- ----------------------------
INSERT INTO `bedrock_admin` VALUES (1932740993065500674, 'superadmin', 'e33d479b887c3e719c721d33065d239d', 'http://localhost:8083/files/bak/upload/20260708/4fc7771b165e41e99c819d501db9d662.jpg', '管理员', 'M', 'bedrock@wewfewc.com', '18864120852', 1, '超级管理员', '000000', '2025-10-03 06:31:44', '2025-10-27 23:52:49', 1932740993065500674, 1932740993065500674, 0);

-- ----------------------------
-- Table structure for bedrock_admin_dept
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_admin_dept`;
CREATE TABLE `bedrock_admin_dept`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `admin_id` bigint(20) DEFAULT NULL COMMENT '管理员id',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '组织机构id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `admin_id_dept_id_unique`(`admin_id`, `dept_id`) USING BTREE COMMENT '管理员和组织唯一'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员组织关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bedrock_admin_dept
-- ----------------------------
INSERT INTO `bedrock_admin_dept` VALUES (1983125754303209475, 1932740993065500674, 1932740993065500674);

-- ----------------------------
-- Table structure for bedrock_admin_online
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_admin_online`;
CREATE TABLE `bedrock_admin_online`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `admin_id` bigint(20) NOT NULL COMMENT '用户ID（关联业务系统用户表主键）',
  `token_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'token唯一标识',
  `token` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户访问Token',
  `token_expired` datetime(0) DEFAULT NULL COMMENT 'token过期时间',
  `last_heartbeat_time` bigint(20) NOT NULL COMMENT '上次心跳时间（WebSocket心跳包触发更新，用于判断是否离线）',
  `online_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '在线状态：0-离线，1-在线（快速查询在线用户）',
  `ws_only_id` bigint(20) DEFAULT NULL COMMENT 'WebSocket连接唯一标识（服务器端连接实例ID，便于定位连接）',
  `login_time` datetime(0) NOT NULL COMMENT '登录/连接建立时间',
  `client_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端IP地址（记录连接来源）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_admin_id_token_id`(`admin_id`, `token_id`) USING BTREE,
  UNIQUE INDEX `uk_ws_only_id`(`ws_only_id`) USING BTREE,
  INDEX `idx_last_heartbeat_time`(`last_heartbeat_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户在线状态表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_admin_role
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_admin_role`;
CREATE TABLE `bedrock_admin_role`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `admin_id` bigint(20) DEFAULT NULL COMMENT '管理员id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `admin_id_role_id_unique`(`admin_id`, `role_id`) USING BTREE COMMENT '管理员角色唯一标识'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bedrock_admin_role
-- ----------------------------
INSERT INTO `bedrock_admin_role` VALUES (1932740993065500674, 1932740993065500674, 1932740993065500674);

-- ----------------------------
-- Table structure for bedrock_ai_api_key
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_api_key`;
CREATE TABLE `bedrock_ai_api_key`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `key_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `api_key` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'API Key',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '厂商平台',
  `base_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Base URL',
  `api_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'API 半路径（相对 Base URL，如 /v1/chat/completions）',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_key_name`(`key_name`) USING BTREE,
  INDEX `idx_platform`(`platform`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AI API Key 配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_chat_message
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_chat_message`;
CREATE TABLE `bedrock_ai_chat_message`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `record_id` bigint(20) NOT NULL COMMENT '关联bedrock_ai_chat_record主表主键id',
  `user_id` bigint(20) NOT NULL COMMENT '操作人用户ID',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父消息 ID；',
  `model_id` bigint(20) NOT NULL COMMENT '使用的AI模型ID',
  `model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型标识',
  `role` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息角色：user用户 / assistant模型 / system系统 / tool工具',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '消息内容',
  `reasoning` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '推理/思考过程内容（DeepSeek 等 reasoning 模型），可为空',
  `tool_calls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'assistant 工具调用列表 JSON',
  `chunk_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '知识库分片ID列表 JSON',
  `attachments` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '用户消息附件列表 JSON',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_record_id`(`record_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AI聊天消息明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_chat_record
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_chat_record`;
CREATE TABLE `bedrock_ai_chat_record`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '操作人用户ID',
  `model_id` bigint(20) NOT NULL COMMENT '使用的AI模型ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT 'AI角色ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '会话标题',
  `system_prompt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '系统提示词',
  `is_top` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否置顶：0否 1是',
  `chat_options` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '对话调用配置（温度、工具、知识库检索等，JSON 字符串）',
  `total_prompt_tokens` int(11) DEFAULT 0 COMMENT '累计输入token',
  `total_completion_tokens` int(11) DEFAULT 0 COMMENT '累计输出token',
  `total_tokens` int(11) DEFAULT 0 COMMENT '总消耗token',
  `chat_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'CHAT' COMMENT '对话类型',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_model_id`(`model_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AI聊天会话主记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_knowledge
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_knowledge`;
CREATE TABLE `bedrock_ai_knowledge`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `knowledge_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '知识库名称',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '知识库描述',
  `embedding_model_id` bigint(20) NOT NULL COMMENT '嵌入模型 id',
  `vector_db_id` bigint(20) NOT NULL COMMENT '向量库 id',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_knowledge_name`(`knowledge_name`) USING BTREE,
  INDEX `idx_embedding_model_id`(`embedding_model_id`) USING BTREE,
  INDEX `idx_vector_db_id`(`vector_db_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AI 知识库配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_knowledge_chunk
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_knowledge_chunk`;
CREATE TABLE `bedrock_ai_knowledge_chunk`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `knowledge_id` bigint(20) NOT NULL COMMENT '所属知识库ID',
  `doc_id` bigint(20) NOT NULL COMMENT '归属文档主键，关联 bedrock_ai_knowledge_doc.id',
  `chunk_no` int(11) NOT NULL COMMENT '文档内分片序号（从1自增，用于还原段落顺序）',
  `chunk_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分片纯文本内容（送入Embedding做向量化）',
  `chunk_token_count` int(11) DEFAULT NULL COMMENT '本段文本预估token数量，用于分片策略复盘',
  `vector_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '向量库内对应向量唯一ID（Milvus/Qdrant/Redis Stack主键）',
  `embed_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '向量化状态：0待向量化、1入库成功、2向量写入失败',
  `recall_count` bigint(20) NOT NULL DEFAULT 0 COMMENT '当前分片被检索召回总次数',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_vector_id`(`vector_id`) USING BTREE,
  INDEX `idx_knowledge_id`(`knowledge_id`) USING BTREE,
  INDEX `idx_doc_id`(`doc_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '知识库-文档分片段落表（向量存储最小单元）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_knowledge_doc
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_knowledge_doc`;
CREATE TABLE `bedrock_ai_knowledge_doc`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `knowledge_id` bigint(20) NOT NULL COMMENT '关联知识库主键，关联 bedrock_ai_knowledge.id',
  `doc_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文档标题/文件名',
  `doc_source_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '来源类型：UPLOAD_FILE本地上传、MANUAL_TEXT手动录入',
  `file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'OSS文件存储路径',
  `file_size` bigint(20) DEFAULT NULL COMMENT '源文件字节大小',
  `file_suffix` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件后缀：pdf/docx/markdown/txt',
  `slice_mode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分段分片模式（分片算法策略）',
  `recall_count` bigint(20) NOT NULL DEFAULT 0 COMMENT '文档被检索召回总次数',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_knowledge_id`(`knowledge_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '知识库-文档元数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_mcp
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_mcp`;
CREATE TABLE `bedrock_ai_mcp`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务名称（创建后不可变）',
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '传输类型：SSE/STREAMABLE_HTTP/STDIO',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'HTTP baseUrl（SSE/StreamableHTTP）',
  `endpoint` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'HTTP endpoint',
  `headers` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '可选 HTTP 请求头（JSON）',
  `stdio_servers_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'STDIO：Claude Desktop 格式 JSON',
  `client_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'MCP client 展示名',
  `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端版本',
  `request_timeout_seconds` int(11) DEFAULT NULL COMMENT '请求超时秒数',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AI MCP 客户端配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_model
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_model`;
CREATE TABLE `bedrock_ai_model`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `api_key_id` bigint(20) NOT NULL COMMENT '关联 bedrock_ai_api_key 表主键 id',
  `model_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型名称',
  `model_avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模型头像 URL',
  `model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型标识',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '厂商平台',
  `model_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型类型',
  `temperature` double(2, 2) DEFAULT NULL COMMENT '温度（0-1）',
  `max_tokens` int(11) DEFAULT NULL COMMENT '单条回复最大 Token 数量',
  `max_messages` int(11) DEFAULT NULL COMMENT '最大消息数量',
  `support_multimodal` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否支持多模态图片识别（1=是，0=否，仅对话模型有效）',
  `sort_order` int(11) NOT NULL DEFAULT 0 COMMENT '排序值，越小越靠前',
  `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否默认模型（1=是，0=否，同类型仅一个）',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_api_key_id`(`api_key_id`) USING BTREE,
  INDEX `idx_model`(`model`) USING BTREE,
  INDEX `idx_platform`(`platform`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AI 模型配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_role
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_role`;
CREATE TABLE `bedrock_ai_role`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色头像',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色描述',
  `system_prompt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '角色设定（系统提示词）',
  `model_id` bigint(20) DEFAULT NULL COMMENT '默认模型 id',
  `chat_options` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '对话调用配置（工具、知识库检索等，JSON 字符串）',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_name`(`role_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AI 角色配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_token_usage
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_token_usage`;
CREATE TABLE `bedrock_ai_token_usage`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `record_id` bigint(20) DEFAULT NULL COMMENT '关联 bedrock_ai_chat_record.id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户 id',
  `model_id` bigint(20) DEFAULT NULL COMMENT '模型 id',
  `model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模型标识',
  `api_key_id` bigint(20) DEFAULT NULL COMMENT 'API Key id',
  `api_key_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'API Key 名称',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '厂商平台',
  `user_message_id` bigint(20) DEFAULT NULL COMMENT 'user 消息 id',
  `assistant_message_id` bigint(20) DEFAULT NULL COMMENT 'assistant 消息 id',
  `stream` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否流式',
  `prompt_tokens` int(11) NOT NULL DEFAULT 0 COMMENT '输入 token',
  `completion_tokens` int(11) NOT NULL DEFAULT 0 COMMENT '输出 token',
  `total_tokens` int(11) NOT NULL DEFAULT 0 COMMENT '总 token',
  `usage_source` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用量来源：MODEL_REPORTED/LOCAL_ESTIMATED',
  `total_latency_ms` int(11) DEFAULT NULL COMMENT '端到端总耗时(ms)',
  `first_token_latency_ms` int(11) DEFAULT NULL COMMENT '首 token 耗时(ms)',
  `streaming_duration_ms` int(11) DEFAULT NULL COMMENT '流式生成耗时(ms)',
  `tokens_per_second` double(10, 2) DEFAULT NULL COMMENT '输出吞吐(tokens/s)',
  `started_at` datetime(0) DEFAULT NULL COMMENT '请求开始时间',
  `completed_at` datetime(0) DEFAULT NULL COMMENT '请求结束时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_record_id`(`record_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_model_id`(`model_id`) USING BTREE,
  INDEX `idx_api_key_id`(`api_key_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AI Token 用量明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_vector_db
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_vector_db`;
CREATE TABLE `bedrock_ai_vector_db`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `db_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名称',
  `vector_db_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '向量数据库类型',
  `host` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主机地址',
  `port` int(11) DEFAULT NULL COMMENT '端口',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `database_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '逻辑库/索引名',
  `collection_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '集合名/Key前缀（Milvus集合名、Redis前缀）',
  `embedding_dimension` int(11) DEFAULT NULL COMMENT '向量维度',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_db_name`(`db_name`) USING BTREE,
  INDEX `idx_vector_db_type`(`vector_db_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '向量数据库配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_client
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_client`;
CREATE TABLE `bedrock_client`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `client_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端唯一标识（主键）',
  `resource_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端可访问的资源ID列表（多个用逗号分隔）',
  `client_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端密钥（加密存储）',
  `scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端权限范围（多个用空格分隔，如read write）',
  `authorized_grant_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支持的授权类型（多个用逗号分隔，如password,refresh_token）',
  `registered_redirect_uri` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '注册的重定向URI（多个用逗号分隔）',
  `access_token_validity` int(11) DEFAULT NULL COMMENT 'access_token有效期（秒，NULL表示使用默认值）',
  `refresh_token_validity` int(11) DEFAULT NULL COMMENT 'refresh_token有效期（秒，NULL表示使用默认值）',
  `additional_information` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '额外信息（通常存储JSON格式的扩展配置）',
  `auto_approve` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '不需要确定的鉴权',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限描述',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_client_id`(`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'OAuth2 客户端信息表（存储客户端配置）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bedrock_client
-- ----------------------------
INSERT INTO `bedrock_client` VALUES (1983176929601835011, 'artdesigpro', '', 'artdesigpro_secret', 'all', 'password,refresh_token', NULL, 100000, 10000, NULL, NULL, NULL, NULL, '2025-10-28 22:23:42', NULL, 1932740993065500674, 0);

-- ----------------------------
-- Table structure for bedrock_dept
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_dept`;
CREATE TABLE `bedrock_dept`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '上级id',
  `ancestors` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '祖籍列表',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门名称',
  `dept_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门编号',
  `category` tinyint(1) DEFAULT NULL COMMENT '组织类型',
  `level` tinyint(1) DEFAULT NULL COMMENT '层级',
  `sort` tinyint(1) DEFAULT 0 COMMENT '排序',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组织描述',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE COMMENT '父级id'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组织信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bedrock_dept
-- ----------------------------
INSERT INTO `bedrock_dept` VALUES (1932740993065500674, 0, '1932740993065500674', '组织', '1', 1, 1, 0, '组织', '000000', '2025-10-03 06:53:22', '2025-10-28 18:01:04', 1932740993065500674, 1932740993065500674, 0);

-- ----------------------------
-- Table structure for bedrock_dict
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_dict`;
CREATE TABLE `bedrock_dict`  (
  `id` bigint(20) NOT NULL COMMENT '字典ID（主键）',
  `dict_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型（分组标识，唯一，如\"sys_gender\"=性别字典、\"sys_order_status\"=订单状态字典）',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典标签（显示用文本，如\"男\"、\"待支付\"）',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典值（实际存储/使用的编码，如\"1\"、\"0\"）',
  `tag_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'info' COMMENT 'tag标签的类型 \'primary\' | \'success\' | \'warning\' | \'info\' | \'danger\'',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父字典ID（支持多级字典，顶级字典为0，',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序号（数值越小越靠前，控制字典项展示顺序）',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否启用：1=启用（可使用），0=禁用（不可使用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注（如\"该字典项仅用于C端订单展示\"）',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID（关联用户表sys_user.id）',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人ID（关联用户表sys_user.id）',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '逻辑删除：1=已删除，0=未删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dict_code`(`dict_code`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统字典表（存储静态枚举数据，支持多级分组，统一维护）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bedrock_dict
-- ----------------------------
INSERT INTO `bedrock_dict` VALUES (1932740993065511674, 'menuType', '菜单类型', '菜单类型', 'info', 0, 1, 1, NULL, '2025-10-18 23:38:40', '2025-10-24 14:32:06', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1932740993065511675, 'menuType', '目录', '1', 'info', 1932740993065511674, 0, 1, NULL, '2025-10-18 23:40:21', '2025-10-24 14:19:14', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1932740993065511676, 'menuType', '菜单', '2', 'primary', 1932740993065511674, 1, 1, NULL, '2025-10-18 23:40:21', '2025-10-24 14:19:31', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1932740993065511677, 'menuType', '按钮', '3', 'danger', 1932740993065511674, 2, 1, NULL, '2025-10-18 23:40:21', '2025-10-24 14:16:49', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1932740993065511678, 'menuType', '外部链接', '4', 'success', 1932740993065511674, 3, 1, NULL, '2025-10-18 23:40:21', '2025-10-24 14:19:49', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1932740993065511679, 'status', '状态', '状态', 'info', 0, 0, 1, NULL, '2025-10-18 23:40:21', '2025-10-18 23:40:21', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1932740993065511680, 'status', '启用', '1', 'info', 1932740993065511679, 1, 1, NULL, '2025-10-18 23:40:21', '2025-10-18 23:40:21', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1932740993065511681, 'status', '禁用', '0', 'info', 1932740993065511679, 2, 1, NULL, '2025-10-18 23:40:21', '2025-11-30 22:59:31', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981604559795712002, 'dictTagType', '标签类型', '标签类型', 'info', 0, 1, 1, '', '2025-10-24 14:11:58', '2025-10-24 14:11:58', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981604955515711490, 'dictTagType', 'primary', 'primary', 'info', 1981604559795712002, 0, 1, '', '2025-10-24 14:13:33', '2025-10-24 14:13:33', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981605001996988417, 'dictTagType', 'success', 'success', 'info', 1981604559795712002, 1, 1, '', '2025-10-24 14:13:44', '2025-10-24 14:13:44', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981605040169349122, 'dictTagType', 'warning', 'warning', 'info', 1981604559795712002, 3, 1, '', '2025-10-24 14:13:53', '2025-10-24 14:13:53', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981605078199103490, 'dictTagType', 'info', 'info', 'info', 1981604559795712002, 4, 1, '', '2025-10-24 14:14:02', '2025-10-24 14:14:02', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981605119374585857, 'dictTagType', 'danger', 'danger', 'info', 1981604559795712002, 5, 1, '', '2025-10-24 14:14:12', '2025-10-24 14:14:12', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981633238953246722, 'httpMethod', '请求方式', '请求方式', 'info', 0, 0, 1, '', '2025-10-24 16:05:56', '2025-10-24 16:05:56', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981633314693988353, 'httpMethod', 'GET', 'GET', 'primary', 1981633238953246722, 0, 1, '', '2025-10-24 16:06:14', '2025-10-24 16:06:14', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981633359363325954, 'httpMethod', 'POST', 'POST', 'success', 1981633238953246722, 1, 1, '', '2025-10-24 16:06:24', '2025-10-24 16:06:24', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981633415516667906, 'httpMethod', 'PUT', 'PUT', 'warning', 1981633238953246722, 3, 1, '', '2025-10-24 16:06:38', '2025-10-24 16:06:38', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981633501621534721, 'httpMethod', 'DELETE', 'DELETE', 'danger', 1981633238953246722, 5, 1, '', '2025-10-24 16:06:58', '2025-10-24 16:06:58', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981637335894867969, 'scopeType', '数据范围', '数据范围', 'info', 0, 0, 1, '', '2025-10-24 16:22:13', '2025-10-24 16:22:13', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981637518355480578, 'scopeType', '全部权限', 'DATA_SCOPE_ALL', 'primary', 1981637335894867969, 0, 1, '', '2025-10-24 16:22:56', '2025-10-24 16:22:56', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981637672869445633, 'scopeType', '部门数据权限', 'DATA_SCOPE_DEPT', 'success', 1981637335894867969, 1, 1, '', '2025-10-24 16:23:33', '2025-10-24 17:28:10', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981637770294738945, 'scopeType', '部门及以下数据权限', 'DATA_SCOPE_DEPT_AND_CHILD', 'warning', 1981637335894867969, 2, 1, '', '2025-10-24 16:23:56', '2025-10-24 16:23:56', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981637937282564098, 'scopeType', '仅本人可见', 'DATA_SCOPE_SELF', 'danger', 1981637335894867969, 4, 1, '', '2025-10-24 16:24:36', '2025-10-24 16:27:20', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981638056564375553, 'scopeType', '自定义WHERE', 'DATA_SCOPE_CUSTOM', 'info', 1981637335894867969, 5, 1, '', '2025-10-24 16:25:04', '2025-10-24 16:25:04', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1981638590142758913, 'scopeType', '自定义权限', 'DATA_SCOPE_CUSTOM_AUTH', 'info', 1981637335894867969, 6, 1, '需要实现org.bedrock.common.datascope.handler.AuthHandler', '2025-10-24 16:27:12', '2025-10-24 16:27:12', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1982093209994235906, 'sex', '性别', '性别', 'info', 0, 6, 1, '', '2025-10-25 22:33:41', '2025-10-25 22:33:41', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1982093317884317697, 'sex', '男', 'M', 'primary', 1982093209994235906, 0, 1, '', '2025-10-25 22:34:07', '2025-10-25 22:35:09', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1982093401057366017, 'sex', '女', 'F', 'success', 1982093209994235906, 1, 1, '', '2025-10-25 22:34:27', '2025-10-25 22:35:14', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983106178756632578, 'deptCategory', '部门类型', '部门类型', 'info', 0, 9, 1, '', '2025-10-28 17:38:52', '2025-10-28 17:38:52', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983106272268640257, 'deptCategory', '部门', '1', 'primary', 1983106178756632578, 1, 1, '部门', '2025-10-28 17:39:14', '2025-10-28 17:39:14', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983172791207923713, 'authorizedGrantTypes', '授权类型', '授权类型', 'info', 0, 11, 1, '', '2025-10-28 22:03:34', '2025-10-28 22:03:54', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983173020107870209, 'authorizedGrantTypes', 'password', 'password', 'info', 1983172791207923713, 0, 1, '', '2025-10-28 22:04:28', '2025-10-28 22:04:28', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983173067776135170, 'authorizedGrantTypes', 'refresh_token', 'refresh_token', 'info', 1983172791207923713, 1, 1, '', '2025-10-28 22:04:40', '2025-10-28 22:04:40', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727527430660097, 'ossServiceProvider', 'OSS服务商', 'OSS服务商', 'info', 0, 11, 1, '', '2025-10-30 10:47:53', '2025-10-30 10:47:53', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727681088987138, 'ossServiceProvider', '本地', 'LOCAL', 'info', 1983727527430660097, 0, 1, '', '2025-10-30 10:48:30', '2025-10-30 10:48:30', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727681088987139, 'ossServiceProvider', 'RustFS', 'RUSTFS', 'info', 1983727527430660097, 7, 1, '', '2025-10-30 10:48:30', '2025-10-30 10:48:30', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516034, 'ossServiceProvider', 'MINIO', 'MINIO', 'info', 1983727527430660097, 1, 1, '', '2025-10-30 10:48:38', '2025-10-30 10:48:38', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516035, 'smsServiceProvider', '短信服务商', '短信服务商', 'info', 0, 12, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516036, 'smsServiceProvider', '阿里云', 'ALIYUN', 'info', 1983727715897516035, 0, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516037, 'smsServiceProvider', '腾讯云', 'TENCENT', 'info', 1983727715897516035, 1, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516038, 'smsServiceProvider', '云片', 'YUNPIAN', 'info', 1983727715897516035, 2, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516039, 'smsServiceProvider', '华为云', 'HUAWEI', 'info', 1983727715897516035, 3, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516040, 'smsServiceProvider', '七牛云', 'QINIU', 'info', 1983727715897516035, 4, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516041, 'emailServiceProvider', '邮件服务商', '邮件服务商', 'info', 0, 13, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516042, 'emailServiceProvider', 'Spring Mail', 'SPRING', 'info', 1983727715897516041, 0, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516043, 'emailEncyType', '邮件加密类型', '邮件加密类型', 'info', 0, 14, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516044, 'emailEncyType', 'SSL', '1', 'info', 1983727715897516043, 0, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516045, 'emailEncyType', 'TLS', '2', 'info', 1983727715897516043, 1, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516046, 'accountAuth', '账户验证', '账户验证', 'info', 0, 15, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516047, 'accountAuth', '否', '0', 'info', 1983727715897516046, 0, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516048, 'accountAuth', '是', '1', 'info', 1983727715897516046, 1, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516049, 'ossServiceProvider', '阿里云', 'ALIYUN', 'info', 1983727527430660097, 2, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516050, 'ossServiceProvider', '腾讯云', 'TENCENT', 'info', 1983727527430660097, 3, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516051, 'ossServiceProvider', '七牛云', 'QINIU', 'info', 1983727527430660097, 4, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516052, 'ossServiceProvider', '华为云', 'HUAWEI', 'info', 1983727527430660097, 5, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516053, 'ossServiceProvider', 'AWS S3', 'AWS', 'info', 1983727527430660097, 6, 1, '', '2026-07-06 18:00:00', '2026-07-06 18:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516054, 'aiModelType', 'AI模型类型', 'AI模型类型', 'info', 0, 1, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516055, 'aiModelType', '对话', 'CHAT', 'primary', 1983727715897516054, 0, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516056, 'aiModelType', '图片', 'IMAGE', 'success', 1983727715897516054, 1, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516057, 'aiModelType', '文字转语音', 'SPEECH', 'warning', 1983727715897516054, 2, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516058, 'aiModelType', '向量', 'EMBEDDING', 'info', 1983727715897516054, 3, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516060, 'aiPlatform', 'AI厂商平台', 'AI厂商平台', 'info', 0, 2, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516061, 'aiPlatform', 'DeepSeek 深度求索', 'DEEP_SEEK', 'primary', 1983727715897516060, 0, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516062, 'aiPlatform', '通义千问（阿里云DashScope）', 'DASH_SCOPE', 'primary', 1983727715897516060, 1, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516063, 'aiPlatform', 'MiniMax 稀宇科技', 'MINIMAX', 'primary', 1983727715897516060, 2, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516064, 'aiPlatform', '月之暗面 Moonshot Kimi', 'MOONSHOT', 'primary', 1983727715897516060, 3, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516065, 'aiPlatform', '智谱清言 GLM', 'ZHI_PU', 'primary', 1983727715897516060, 4, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516066, 'aiPlatform', '百度千帆 文心一言', 'QIAN_FAN', 'primary', 1983727715897516060, 5, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516067, 'aiPlatform', '腾讯混元', 'HUNYUAN', 'primary', 1983727715897516060, 6, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516068, 'aiPlatform', '讯飞星火', 'SPARK', 'primary', 1983727715897516060, 7, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516069, 'aiPlatform', '百川智能', 'BAICHUAN', 'primary', 1983727715897516060, 8, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516070, 'aiPlatform', '火山方舟（字节企业）', 'VOLCENGINE', 'primary', 1983727715897516060, 9, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516071, 'aiPlatform', '零一万物 Yi', 'YI', 'primary', 1983727715897516060, 10, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516072, 'aiPlatform', '硅基流动', 'SILICON_FLOW', 'primary', 1983727715897516060, 11, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516073, 'aiPlatform', 'Anthropic Claude', 'ANTHROPIC', 'success', 1983727715897516060, 12, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516074, 'aiPlatform', 'Azure OpenAI 微软', 'AZURE_OPENAI', 'success', 1983727715897516060, 13, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516075, 'aiPlatform', 'Gemini 谷歌', 'GEMINI', 'success', 1983727715897516060, 14, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516076, 'aiPlatform', 'OpenAI', 'OPENAI', 'success', 1983727715897516060, 15, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516077, 'aiPlatform', 'Mistral AI', 'MISTRAL', 'success', 1983727715897516060, 16, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516078, 'aiPlatform', 'Ollama 本地开源模型', 'OLLAMA', 'success', 1983727715897516060, 17, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516079, 'aiPlatform', 'Cohere', 'COHERE', 'success', 1983727715897516060, 18, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516080, 'aiPlatform', 'xAI Grok', 'XAI', 'success', 1983727715897516060, 19, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516081, 'aiVectorDbType', '向量存储类型', '向量存储类型', 'info', 0, 3, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516082, 'aiVectorDbType', 'Redis 向量', 'REDIS', 'primary', 1983727715897516081, 0, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516083, 'aiVectorDbType', 'Milvus 向量', 'MILVUS', 'success', 1983727715897516081, 1, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516084, 'aiVectorDbType', 'Elasticsearch 向量', 'ELASTICSEARCH', 'warning', 1983727715897516081, 2, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516085, 'aiVectorDbType', '本地向量', 'SIMPLE', 'info', 1983727715897516081, 3, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516090, 'aiKnowledgeDocSourceType', '知识库文档来源', '知识库文档来源', 'info', 0, 4, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516091, 'aiKnowledgeDocSourceType', '本地上传', 'UPLOAD_FILE', 'primary', 1983727715897516090, 0, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516092, 'aiKnowledgeDocSourceType', '手动录入', 'MANUAL_TEXT', 'success', 1983727715897516090, 1, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516093, 'aiKnowledgeEmbedStatus', '分片向量化状态', '分片向量化状态', 'info', 0, 4, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516094, 'aiKnowledgeEmbedStatus', '待向量化', '0', 'info', 1983727715897516093, 0, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516095, 'aiKnowledgeEmbedStatus', '入库成功', '1', 'success', 1983727715897516093, 1, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516096, 'aiKnowledgeEmbedStatus', '向量写入失败', '2', 'danger', 1983727715897516093, 2, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516100, 'aiDocumentSliceMode', '文档分片模式', '文档分片模式', 'info', 0, 4, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516101, 'aiDocumentSliceMode', '固定长度分片', 'FIXED_LENGTH', 'primary', 1983727715897516100, 0, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516102, 'aiDocumentSliceMode', '语义智能分片', 'SEMANTIC', 'success', 1983727715897516100, 1, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516103, 'aiDocumentSliceMode', '标题层级分片', 'HIERARCHY', 'warning', 1983727715897516100, 2, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516104, 'aiDocumentSliceMode', '自定义分隔符分片', 'DELIMITER', 'info', 1983727715897516100, 3, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516105, 'aiDocumentSliceMode', '递归字符分片', 'RECURSIVE_CHAR', 'primary', 1983727715897516100, 4, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516106, 'aiDocumentSliceMode', '段落分片', 'PARAGRAPH', 'success', 1983727715897516100, 5, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516107, 'aiDocumentSliceMode', '完整文档不分片', 'WHOLE_DOC', 'info', 1983727715897516100, 6, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516110, 'aiMcpTransportType', 'MCP传输类型', 'MCP传输类型', 'info', 0, 5, 1, NULL, '2026-07-15 00:00:00', '2026-07-15 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516111, 'aiMcpTransportType', 'SSE', 'SSE', 'primary', 1983727715897516110, 0, 1, NULL, '2026-07-15 00:00:00', '2026-07-15 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516112, 'aiMcpTransportType', 'Streamable HTTP', 'STREAMABLE_HTTP', 'success', 1983727715897516110, 1, 1, NULL, '2026-07-15 00:00:00', '2026-07-15 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516113, 'aiMcpTransportType', 'STDIO', 'STDIO', 'warning', 1983727715897516110, 2, 1, NULL, '2026-07-15 00:00:00', '2026-07-15 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516120, 'aiArticleWordRange', 'AI文章字数区间', 'AI文章字数区间', 'info', 0, 6, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516121, 'aiArticleWordRange', '约 300–500 字', 'SHORT', 'primary', 1983727715897516120, 0, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516122, 'aiArticleWordRange', '约 800–1200 字', 'MEDIUM', 'success', 1983727715897516120, 1, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516123, 'aiArticleWordRange', '约 1500–2000 字', 'LONG', 'warning', 1983727715897516120, 2, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516124, 'aiArticleWordRange', '约 2500–3500 字', 'EXTRA_LONG', 'info', 1983727715897516120, 3, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516130, 'aiArticleGenre', 'AI文章文体', 'AI文章文体', 'info', 0, 7, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516131, 'aiArticleGenre', '博客', 'BLOG', 'primary', 1983727715897516130, 0, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516132, 'aiArticleGenre', '新闻', 'NEWS', 'success', 1983727715897516130, 1, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516133, 'aiArticleGenre', '公众号', 'WECHAT', 'warning', 1983727715897516130, 2, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516134, 'aiArticleGenre', '论文摘要', 'PAPER_ABSTRACT', 'info', 1983727715897516130, 3, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516135, 'aiArticleGenre', '产品介绍', 'PRODUCT', 'primary', 1983727715897516130, 4, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516140, 'aiArticleTone', 'AI文章语气', 'AI文章语气', 'info', 0, 8, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516141, 'aiArticleTone', '正式', 'FORMAL', 'primary', 1983727715897516140, 0, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516142, 'aiArticleTone', '轻松', 'CASUAL', 'success', 1983727715897516140, 1, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516143, 'aiArticleTone', '专业', 'PROFESSIONAL', 'warning', 1983727715897516140, 2, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516144, 'aiArticleTone', '营销', 'MARKETING', 'info', 1983727715897516140, 3, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516150, 'aiArticleLang', 'AI文章语言', 'AI文章语言', 'info', 0, 9, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516151, 'aiArticleLang', '中文', 'ZH', 'primary', 1983727715897516150, 0, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516152, 'aiArticleLang', '英文', 'EN', 'success', 1983727715897516150, 1, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516160, 'aiArticleFormat', 'AI文章格式', 'AI文章格式', 'info', 0, 10, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516161, 'aiArticleFormat', 'Markdown 正文', 'MARKDOWN', 'primary', 1983727715897516160, 0, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516162, 'aiArticleFormat', '带小标题分段', 'WITH_HEADINGS', 'success', 1983727715897516160, 1, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516163, 'aiArticleFormat', '要点列表', 'BULLET', 'warning', 1983727715897516160, 2, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516164, 'aiArticleFormat', '问答体', 'QNA', 'info', 1983727715897516160, 3, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516170, 'aiChatType', 'AI会话类型', 'AI会话类型', 'info', 0, 11, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516171, 'aiChatType', '普通对话', 'CHAT', 'primary', 1983727715897516170, 0, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516172, 'aiChatType', '图片生成', 'IMAGE', 'success', 1983727715897516170, 1, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516173, 'aiChatType', '思维导图', 'MINDMAP', 'warning', 1983727715897516170, 2, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516174, 'aiChatType', '文章写作', 'ARTICLE', 'info', 1983727715897516170, 3, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);

-- ----------------------------
-- Table structure for bedrock_email_config
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_email_config`;
CREATE TABLE `bedrock_email_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名称',
  `config_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置编码（唯一标识）',
  `service_provider` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮件服务商',
  `protocol` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'smtp' COMMENT '协议',
  `smtp_server` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'SMTP服务器',
  `smtp_port` int(11) NOT NULL COMMENT 'SMTP端口号',
  `ency_type` tinyint(1) DEFAULT 1 COMMENT '加密类型(1.SSL 2.TLS)',
  `account_auth` tinyint(1) DEFAULT 1 COMMENT '账户验证(0.否 1.是)',
  `username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '密码/授权码',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_config_code`(`config_code`) USING BTREE COMMENT '配置编码查询索引',
  INDEX `idx_service_provider`(`service_provider`) USING BTREE COMMENT '按服务商筛选索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_log_operation
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_log_operation`;
CREATE TABLE `bedrock_log_operation`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作日志类型（如：订单类型、商品类型）',
  `sub_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日志子类型',
  `duration` bigint(20) DEFAULT NULL COMMENT '执行时间（毫秒）',
  `action` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '日志内容',
  `extra` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '日志额外信息',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户姓名',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户ID',
  `service_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '服务名称',
  `server_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '服务器IP及端口（如：192.168.1.5:7006）',
  `env` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '环境（如：dev、test、prod）',
  `remote_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户代理（浏览器/客户端信息）',
  `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求地址（如：/api/order/create）',
  `request_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方式（GET/POST/PUT/DELETE）',
  `request_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求参数',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '调用方法（类名.方法名，如：com.xxx.service.OrderService.create）',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_menu
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_menu`;
CREATE TABLE `bedrock_menu`  (
  `id` bigint(20) NOT NULL COMMENT '菜单ID',
  `menu_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `menu_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单编码（唯一标识）',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父菜单ID（0表示根菜单）',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '祖级列表（逗号分隔的父ID列表）',
  `menu_type` tinyint(1) NOT NULL COMMENT '菜单类型：1-目录，2-菜单，3-按钮,4-外部链接',
  `menu_icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单图标',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序号（从小到大）',
  `route_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由路径',
  `component_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组件路径',
  `external_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '外链地址',
  `active_path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '激活路径',
  `is_cache` tinyint(4) DEFAULT 0 COMMENT '是否缓存页面：0-否，1-是',
  `is_hide` tinyint(4) DEFAULT NULL COMMENT '是否在菜单中隐藏：0-否，1-是',
  `is_hide_tab` tinyint(4) DEFAULT 1 COMMENT '是否在标签页中隐藏：0-否，1-是',
  `is_affix` tinyint(4) DEFAULT 0 COMMENT '是否固定标签：0-否，1-是（前端多标签页使用）',
  `is_iframe` int(11) DEFAULT 0 COMMENT '是否为iframe ：0-否，1-是',
  `is_full_screen` tinyint(4) DEFAULT 0 COMMENT '是否全屏显示：0-否，1-是',
  `is_show_badge` tinyint(1) DEFAULT 0 COMMENT '是否显示徽章：0-否，1-是',
  `show_text_badge` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '徽章内容',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT 0 COMMENT '删除标记：0-正常，1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `uk_menu_code`(`menu_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bedrock_menu
-- ----------------------------
INSERT INTO `bedrock_menu` VALUES (1932740993065511674, '仪表盘', 'dashboard', 0, '1932740993065511674', 1, 'ri:pie-chart-line', 0, '/dashboard', '/index/index', NULL, NULL, 1, 0, 1, 1, 0, 0, NULL, NULL, 1, '一级菜单', 1932740993065500674, '2025-10-13 22:35:48', 1932740993065500674, '2026-07-11 13:22:49', 0);
INSERT INTO `bedrock_menu` VALUES (1932740993065511675, '工作台', 'console', 1932740993065511674, '1932740993065511674,1932740993065511675', 2, 'ri:home-office-fill', 1, 'console', '/dashboard/console', NULL, NULL, 1, 0, 0, 1, 0, 0, NULL, NULL, 1, '二级菜单', 1932740993065500674, '2025-10-13 22:56:39', 1932740993065500674, '2025-11-16 17:02:49', 0);
INSERT INTO `bedrock_menu` VALUES (1932740993065511676, '系统管理', 'system', 0, '1932740993065511676', 1, 'ri:tools-fill', 3, '/system', '/index/index', NULL, NULL, 1, 0, 1, 1, 0, 0, NULL, NULL, 1, '一级菜单', 1932740993065500674, '2025-10-14 11:57:56', 1932740993065500674, '2025-11-14 23:55:07', 0);
INSERT INTO `bedrock_menu` VALUES (1932740993065511677, '菜单管理', 'menu', 1932740993065511676, '1932740993065511676,1932740993065511677', 2, 'ri:menu-line', 10, 'menu', '/system/menu', NULL, NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '二级菜单', 1932740993065500674, '2025-10-14 12:00:23', 1932740993065500674, '2025-12-01 02:01:48', 0);
INSERT INTO `bedrock_menu` VALUES (1932740993065511678, '添加', 'add', 1932740993065511677, '1932740993065511676,1932740993065511677,1932740993065511678', 3, NULL, 0, NULL, NULL, NULL, NULL, 0, 0, 1, 0, 0, 0, NULL, NULL, 1, '按钮', 1932740993065500674, '2025-10-14 12:00:23', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1979669202892111874, '编辑', 'edit', 1932740993065511677, '1932740993065511676,1932740993065511677,1979669202892111874', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-19 06:01:33', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1979670660018487298, '删除', 'delete', 1932740993065511677, '1932740993065511676,1932740993065511677,1979670660018487298', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-19 06:07:21', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1979811629309280257, '字典管理', 'dict', 1932740993065511676, '1932740993065511676,1979811629309280257', 2, 'ri:book-line', 7, 'dict', '/system/dict', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-19 15:27:30', 1932740993065500674, '2025-11-29 20:39:08', 0);
INSERT INTO `bedrock_menu` VALUES (1980652234662973442, '添加', 'add', 1979811629309280257, '1932740993065511676,1979811629309280257,1980652234662973442', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-21 23:07:46', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1980652276522127362, '编辑', 'edit', 1979811629309280257, '1932740993065511676,1979811629309280257,1980652276522127362', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-21 23:07:56', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1980652341076660226, '删除', 'delete', 1979811629309280257, '1932740993065511676,1979811629309280257,1980652341076660226', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-21 23:08:12', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1980658642502467586, '字典数据', 'dataDcit', 1979811629309280257, '1932740993065511676,1979811629309280257,1980658642502467586', 3, '', 3, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-21 23:33:14', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1980938986376122370, '角色管理', 'role', 1932740993065511676, '1932740993065511676,1980938986376122370', 2, 'ri:group-line', 6, 'role', '/system/role', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-22 18:07:13', 1932740993065500674, '2025-11-29 20:39:00', 0);
INSERT INTO `bedrock_menu` VALUES (1981004061690548225, '添加', 'add', 1980938986376122370, '1932740993065511676,1980938986376122370,1981004061690548225', 3, '', 0, '', '', '', NULL, 1, NULL, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-22 22:25:48', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1981004304658190337, '编辑', 'edit', 1980938986376122370, '1932740993065511676,1980938986376122370,1981004304658190337', 3, '', 1, '', '', '', NULL, 1, NULL, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-22 22:26:46', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1981004455250481154, '菜单权限', 'permission-menu', 1980938986376122370, '1932740993065511676,1980938986376122370,1981004455250481154', 3, '', 3, '', '', '', NULL, 1, NULL, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-22 22:27:22', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1981005020252590082, '删除', 'delete', 1980938986376122370, '1932740993065511676,1980938986376122370,1981005020252590082', 3, '', 2, '', '', '', NULL, 1, NULL, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-22 22:29:37', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1981248129355935745, '数据权限', 'permission-data', 1980938986376122370, '1932740993065511676,1980938986376122370,1981248129355935745', 3, '', 6, '', '', '', NULL, 1, NULL, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-23 14:35:39', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1981248323766120450, '接口权限', 'permission-link', 1980938986376122370, '1932740993065511676,1980938986376122370,1981248323766120450', 3, '', 7, '', '', '', NULL, 1, NULL, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-23 14:36:25', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1981288508419706881, '权限配置', 'permission-configuration', 1932740993065511676, '1932740993065511676,1981288508419706881', 1, '', 4, '/', '', '', NULL, 1, 1, 1, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-23 17:16:06', 1932740993065500674, '2025-10-30 11:32:47', 0);
INSERT INTO `bedrock_menu` VALUES (1981327693599969281, '数据权限', 'permission-data', 1932740993065511677, '1932740993065511676,1932740993065511677,1981327693599969281', 3, '', 4, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-23 19:51:48', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1981327822767755265, '接口权限', 'permission-api', 1932740993065511677, '1932740993065511676,1932740993065511677,1981327822767755265', 3, '', 5, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-23 19:52:19', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1981377057278083074, '数据权限', 'PermissionData', 1981288508419706881, '1932740993065511676,1981288508419706881,1981377057278083074', 2, '', 0, 'permission-data/:menuId', '/system/menu/permission-data', '', NULL, 1, 1, 1, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-23 23:07:57', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1981377451186143233, '接口权限', 'PermissionApi', 1981288508419706881, '1932740993065511676,1981288508419706881,1981377451186143233', 2, '', 1, 'permission-api/:menuId', '/system/menu/permission-api', '', NULL, 1, 1, 1, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-23 23:09:31', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1981570198639661057, '添加', 'add', 1981377057278083074, '1932740993065511676,1981288508419706881,1981377057278083074,1981570198639661057', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-24 11:55:26', 1932740993065500674, '2025-10-30 11:29:07', 0);
INSERT INTO `bedrock_menu` VALUES (1981570255090798594, '编辑', 'edit', 1981377057278083074, '1932740993065511676,1981288508419706881,1981377057278083074,1981570255090798594', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-24 11:55:39', 1932740993065500674, '2025-10-30 11:29:07', 0);
INSERT INTO `bedrock_menu` VALUES (1981570300666105858, '删除', 'delete', 1981377057278083074, '1932740993065511676,1981288508419706881,1981377057278083074,1981570300666105858', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-24 11:55:50', 1932740993065500674, '2025-10-30 11:29:07', 0);
INSERT INTO `bedrock_menu` VALUES (1981570334568665089, '添加', 'add', 1981377451186143233, '1932740993065511676,1981288508419706881,1981377451186143233,1981570334568665089', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-24 11:55:58', 1932740993065500674, '2025-10-30 11:29:07', 0);
INSERT INTO `bedrock_menu` VALUES (1981570375345688577, '编辑', 'edit', 1981377451186143233, '1932740993065511676,1981288508419706881,1981377451186143233,1981570375345688577', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-24 11:56:08', 1932740993065500674, '2025-10-30 11:29:07', 0);
INSERT INTO `bedrock_menu` VALUES (1981570438394466305, '删除', 'delete', 1981377451186143233, '1932740993065511676,1981288508419706881,1981377451186143233,1981570438394466305', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-24 11:56:23', 1932740993065500674, '2025-10-30 11:29:07', 0);
INSERT INTO `bedrock_menu` VALUES (1982075470810398722, '用户管理', 'User', 1932740993065511676, '1932740993065511676,1982075470810398722', 2, 'ri:id-card-fill', 4, 'user', '/system/user', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-25 21:23:12', 1932740993065500674, '2025-11-29 20:38:31', 0);
INSERT INTO `bedrock_menu` VALUES (1982075642541981697, '添加', 'add', 1982075470810398722, '1932740993065511676,1982075470810398722,1982075642541981697', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-25 21:23:53', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1982075698200395777, '编辑', 'edit', 1982075470810398722, '1932740993065511676,1982075470810398722,1982075698200395777', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-25 21:24:06', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1982075754773168129, '删除', 'delete', 1982075470810398722, '1932740993065511676,1982075470810398722,1982075754773168129', 3, '', 3, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-25 21:24:20', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1982079098984022018, '个人中心', 'user-center', 1932740993065511676, '1932740993065511676,1982079098984022018', 2, '', 9, 'user-center', '/system/user-center', '', NULL, 1, 1, 1, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-25 21:37:37', 1932740993065500674, '2025-10-30 11:33:09', 0);
INSERT INTO `bedrock_menu` VALUES (1983010870378868738, '部门管理', 'dept', 1932740993065511676, '1932740993065511676,1983010870378868738', 2, 'ri:node-tree', 5, 'dept', '/system/dept', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-28 11:20:09', 1932740993065500674, '2025-11-29 20:38:36', 0);
INSERT INTO `bedrock_menu` VALUES (1983013525113253890, '添加', 'add', 1983010870378868738, '1932740993065511676,1983010870378868738,1983013525113253890', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-28 11:30:42', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1983013567945486338, '编辑', 'edit', 1983010870378868738, '1932740993065511676,1983010870378868738,1983013567945486338', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-28 11:30:53', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1983013633833807874, '删除', 'delete', 1983010870378868738, '1932740993065511676,1983010870378868738,1983013633833807874', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-28 11:31:08', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1983167984908832769, '授权管理', 'client', 1932740993065511676, '1932740993065511676,1983167984908832769', 2, 'ri:crosshair-fill', 5, 'client', '/system/client', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-28 21:44:28', 1932740993065500674, '2025-11-29 20:39:55', 0);
INSERT INTO `bedrock_menu` VALUES (1983724078278320129, '添加', 'add', 1983167984908832769, '1932740993065511676,1983167984908832769,1983724078278320129', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 10:34:11', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1983724118958874626, '编辑', 'edit', 1983167984908832769, '1932740993065511676,1983167984908832769,1983724118958874626', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 10:34:20', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1983724160973217794, '删除', 'delete', 1983167984908832769, '1932740993065511676,1983167984908832769,1983724160973217794', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 10:34:30', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1983725713234796545, 'OSS配置', 'ossConfig', 1983732375559544834, '1983732375559544834,1983725713234796545', 2, 'ri:folder-2-line', 0, 'ossconfig', '/resource/oss', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 10:40:41', 1932740993065500674, '2025-11-14 23:35:38', 0);
INSERT INTO `bedrock_menu` VALUES (1983725827382779905, '添加', 'add', 1983725713234796545, '1983732375559544834,1983725713234796545,1983725827382779905', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 10:41:08', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1983725867027341313, '编辑', 'edit', 1983725713234796545, '1983732375559544834,1983725713234796545,1983725867027341313', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 10:41:17', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1983725910438387714, '删除', 'delete', 1983725713234796545, '1983732375559544834,1983725713234796545,1983725910438387714', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 10:41:28', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1983726055217373185, '测试上传', 'testUpload', 1983725713234796545, '1983732375559544834,1983725713234796545,1983726055217373185', 3, '', 4, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 10:42:02', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1983728940802056193, '参数管理', 'paramConfig', 1932740993065511676, '1932740993065511676,1983728940802056193', 2, 'ri:list-settings-line', 8, 'paramconfig', '/system/param-config', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 10:53:30', 1932740993065500674, '2025-11-29 20:39:12', 0);
INSERT INTO `bedrock_menu` VALUES (1983729011752902657, '添加', 'add', 1983728940802056193, '1932740993065511676,1983728940802056193,1983729011752902657', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 10:53:47', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1983729043562504194, '编辑', 'edit', 1983728940802056193, '1932740993065511676,1983728940802056193,1983729043562504194', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 10:53:55', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1983729081940385794, '删除', 'delete', 1983728940802056193, '1932740993065511676,1983728940802056193,1983729081940385794', 3, '', 3, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 10:54:04', 1932740993065500674, '2025-10-30 11:26:50', 0);
INSERT INTO `bedrock_menu` VALUES (1983732375559544834, '集成配置', 'resource', 0, '1983732375559544834', 1, 'ri:chat-settings-fill', 1, '/resource', '/index/index', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 11:07:09', 1932740993065500674, '2025-11-14 23:33:45', 0);
INSERT INTO `bedrock_menu` VALUES (1983742934715715585, '重置密码', 'resetPassword', 1982075470810398722, '1932740993065511676,1982075470810398722,1983742934715715585', 3, '', 8, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 11:49:06', 1932740993065500674, '2025-10-30 11:49:06', 0);
INSERT INTO `bedrock_menu` VALUES (1983837985726713858, '日志管理', 'log', 0, '1983837985726713858', 1, '&#xe76c;', 2, '/log', '/index/index', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 18:06:48', 1932740993065500674, '2025-10-30 18:09:20', 1);
INSERT INTO `bedrock_menu` VALUES (1983838208632999937, '操作日志', 'operation', 1983837985726713858, '1983837985726713858,1983838208632999937', 2, '&#xe651;', 1, 'operation', '/log/operation', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 18:07:42', 1932740993065500674, '2025-10-30 18:09:18', 1);
INSERT INTO `bedrock_menu` VALUES (1983839802351079426, '日志管理', 'log', 1932740993065511676, '1932740993065511676,1983839802351079426', 1, 'ri:logout-box-line', 9, 'log', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 18:14:02', 1932740993065500674, '2025-11-29 20:39:15', 0);
INSERT INTO `bedrock_menu` VALUES (1983840041220886530, '操作日志', 'operation', 1983839802351079426, '1932740993065511676,1983839802351079426,1983840041220886530', 2, 'ri:calendar-todo-line', 0, 'operation', '/system/log/operation', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 18:14:58', 1932740993065500674, '2025-11-14 23:46:06', 0);
INSERT INTO `bedrock_menu` VALUES (1983840103787319298, '查看', 'view', 1983840041220886530, '1932740993065511676,1983839802351079426,1983840041220886530,1983840103787319298', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-10-30 18:15:13', 1932740993065500674, '2025-10-30 18:15:13', 0);
INSERT INTO `bedrock_menu` VALUES (1985262420937637890, '在线用户', 'userOnline', 1932740993065511676, '1932740993065511676,1985262420937637890', 2, 'ri:heart-3-line', 1, 'userOnline', '/system/user-online', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-11-03 16:27:00', 1932740993065500674, '2025-11-29 20:36:49', 0);
INSERT INTO `bedrock_menu` VALUES (1994744803550277634, '租户管理', 'tenant', 1932740993065511676, '1932740993065511676,1994744803550277634', 2, 'ri:dashboard-horizontal-fill', 2, 'tenant', '/system/tenant', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-11-29 20:26:36', 1932740993065500674, '2025-11-29 20:38:12', 0);
INSERT INTO `bedrock_menu` VALUES (1994745272406355969, '租户套餐管理', 'tenantPackage', 1932740993065511676, '1932740993065511676,1994745272406355969', 2, 'ri:red-packet-line', 3, 'tenantPackage', '/system/tenant-package', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-11-29 20:28:28', 1932740993065500674, '2025-11-29 20:38:17', 0);
INSERT INTO `bedrock_menu` VALUES (1995108545588850690, '添加', 'add', 1994745272406355969, '1932740993065511676,1994745272406355969,1995108545588850690', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-11-30 20:31:59', 1932740993065500674, '2025-11-30 20:31:59', 0);
INSERT INTO `bedrock_menu` VALUES (1995108612567691266, '编辑', 'edit', 1994745272406355969, '1932740993065511676,1994745272406355969,1995108612567691266', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-11-30 20:32:15', 1932740993065500674, '2025-11-30 20:32:15', 0);
INSERT INTO `bedrock_menu` VALUES (1995108670352617474, '删除', 'delete', 1994745272406355969, '1932740993065511676,1994745272406355969,1995108670352617474', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-11-30 20:32:29', 1932740993065500674, '2025-11-30 20:32:29', 0);
INSERT INTO `bedrock_menu` VALUES (1995143254729498625, '添加', 'add', 1994744803550277634, '1932740993065511676,1994744803550277634,1995143254729498625', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-11-30 22:49:55', 1932740993065500674, '2025-11-30 22:49:55', 0);
INSERT INTO `bedrock_menu` VALUES (1995143328092069890, '编辑', 'edit', 1994744803550277634, '1932740993065511676,1994744803550277634,1995143328092069890', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-11-30 22:50:12', 1932740993065500674, '2025-11-30 22:50:12', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227969, '删除', 'delete', 1994744803550277634, '1932740993065511676,1994744803550277634,1995143390566227969', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2025-11-30 22:50:27', 1932740993065500674, '2025-11-30 22:50:27', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227970, 'SMS配置', 'smsConfig', 1983732375559544834, '1983732375559544834,1996001000000000001', 2, 'ri:message-2-line', 1, 'smsconfig', '/resource/sms', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2026-07-06 18:00:00', 1932740993065500674, '2026-07-06 19:23:47', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227971, '添加', 'add', 1995143390566227970, '1983732375559544834,1996001000000000001,1996001000000000002', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2026-07-06 18:00:00', 1932740993065500674, '2026-07-07 22:57:23', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227972, '编辑', 'edit', 1995143390566227970, '1983732375559544834,1996001000000000001,1996001000000000003', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2026-07-06 18:00:00', 1932740993065500674, '2026-07-07 22:57:23', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227973, '删除', 'delete', 1995143390566227970, '1983732375559544834,1996001000000000001,1996001000000000004', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2026-07-06 18:00:00', 1932740993065500674, '2026-07-07 22:57:23', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227974, '测试发送', 'testSend', 1995143390566227970, '1983732375559544834,1996001000000000001,1996001000000000005', 3, '', 3, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2026-07-06 18:00:00', 1932740993065500674, '2026-07-07 22:57:24', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227975, 'Email配置', 'emailConfig', 1983732375559544834, '1983732375559544834,1996002000000000001', 2, 'ri:mail-line', 2, 'emailconfig', '/resource/email', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2026-07-06 18:00:00', 1932740993065500674, '2026-07-06 19:24:03', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227976, '添加', 'add', 1995143390566227975, '1983732375559544834,1996002000000000001,1996002000000000002', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2026-07-06 18:00:00', 1932740993065500674, '2026-07-07 22:57:27', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227977, '编辑', 'edit', 1995143390566227975, '1983732375559544834,1996002000000000001,1996002000000000003', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2026-07-06 18:00:00', 1932740993065500674, '2026-07-07 22:57:27', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227978, '删除', 'delete', 1995143390566227975, '1983732375559544834,1996002000000000001,1996002000000000004', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2026-07-06 18:00:00', 1932740993065500674, '2026-07-07 22:57:28', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227979, '测试发送', 'testSend', 1995143390566227975, '1983732375559544834,1996002000000000001,1996002000000000005', 3, '', 3, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, NULL, 1932740993065500674, '2026-07-06 18:00:00', 1932740993065500674, '2026-07-07 22:57:28', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227980, 'AI管理', 'ai', 0, '1995143390566227980', 1, 'ri:robot-2-line', 2, '/ai', '/index/index', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI管理目录', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227981, 'API Key管理', 'aiApiKey', 1995143390566227980, '1995143390566227980,1995143390566227981', 2, 'ri:key-2-line', 0, 'api-key', '/ai/api-key', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI API Key管理', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227982, '添加', 'add', 1995143390566227981, '1995143390566227980,1995143390566227981,1995143390566227982', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227983, '编辑', 'edit', 1995143390566227981, '1995143390566227980,1995143390566227981,1995143390566227983', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227984, '删除', 'delete', 1995143390566227981, '1995143390566227980,1995143390566227981,1995143390566227984', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227985, '模型管理', 'aiModel', 1995143390566227980, '1995143390566227980,1995143390566227985', 2, 'ri:cpu-line', 1, 'model', '/ai/model', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI模型管理', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227986, '添加', 'add', 1995143390566227985, '1995143390566227980,1995143390566227985,1995143390566227986', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227987, '编辑', 'edit', 1995143390566227985, '1995143390566227980,1995143390566227985,1995143390566227987', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227988, '删除', 'delete', 1995143390566227985, '1995143390566227980,1995143390566227985,1995143390566227988', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227989, 'AI角色管理', 'aiRole', 1995143390566227980, '1995143390566227980,1995143390566227989', 2, 'ri:user-smile-line', 2, 'role', '/ai/role', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI角色管理', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227990, '添加', 'add', 1995143390566227989, '1995143390566227980,1995143390566227989,1995143390566227990', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227991, '编辑', 'edit', 1995143390566227989, '1995143390566227980,1995143390566227989,1995143390566227991', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227992, '删除', 'delete', 1995143390566227989, '1995143390566227980,1995143390566227989,1995143390566227992', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227993, 'AI聊天', 'aiChat', 1995143390566227980, '1995143390566227980,1995143390566227993', 2, 'ri:chat-3-line', 3, 'chat', '/ai/chat', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI聊天', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227994, 'Token用量', 'aiTokenUsage', 1995143390566227980, '1995143390566227980,1995143390566227994', 2, 'ri:coin-line', 4, 'token-usage', '/ai/token-usage', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI Token用量', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227995, '查看', 'view', 1995143390566227994, '1995143390566227980,1995143390566227994,1995143390566227995', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227996, '向量数据库', 'aiVectorDb', 1995143390566227980, '1995143390566227980,1995143390566227996', 2, 'ri:database-2-line', 5, 'vector-db', '/ai/vector-db', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '向量数据库管理', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227997, '添加', 'add', 1995143390566227996, '1995143390566227980,1995143390566227996,1995143390566227997', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227998, '编辑', 'edit', 1995143390566227996, '1995143390566227980,1995143390566227996,1995143390566227998', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227999, '删除', 'delete', 1995143390566227996, '1995143390566227980,1995143390566227996,1995143390566227999', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228000, '知识库管理', 'aiKnowledge', 1995143390566227980, '1995143390566227980,1995143390566228000', 2, 'ri:book-2-line', 6, 'knowledge', '/ai/knowledge', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI 知识库管理', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228001, '添加', 'add', 1995143390566228000, '1995143390566227980,1995143390566228000,1995143390566228001', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228002, '编辑', 'edit', 1995143390566228000, '1995143390566227980,1995143390566228000,1995143390566228002', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228003, '删除', 'delete', 1995143390566228000, '1995143390566227980,1995143390566228000,1995143390566228003', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228004, '知识库文档配置', 'aiKnowledgeDocConfig', 1995143390566227980, '1995143390566227980,1995143390566228004', 1, '', 7, '/', '', '', NULL, 1, 1, 1, 0, 0, 0, 0, '', 1, '知识库文档隐藏目录', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228005, '知识库文档', 'AiKnowledgeDoc', 1995143390566228004, '1995143390566227980,1995143390566228004,1995143390566228005', 2, '', 0, 'knowledge-doc/:knowledgeId', '/ai/knowledge/doc', '', '/ai/knowledge', 1, 1, 1, 0, 0, 0, 0, '', 1, '知识库文档管理', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228006, '上传文档', 'AiKnowledgeDocUpload', 1995143390566228004, '1995143390566227980,1995143390566228004,1995143390566228006', 2, '', 1, 'knowledge-doc/:knowledgeId/upload', '/ai/knowledge/doc/upload', '', '/ai/knowledge', 1, 1, 1, 0, 0, 0, 0, '', 1, '知识库文档上传向导', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228008, '添加', 'add', 1995143390566228005, '1995143390566227980,1995143390566228004,1995143390566228005,1995143390566228008', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228009, '编辑', 'edit', 1995143390566228005, '1995143390566227980,1995143390566228004,1995143390566228005,1995143390566228009', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228010, '删除', 'delete', 1995143390566228005, '1995143390566227980,1995143390566228004,1995143390566228005,1995143390566228010', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228011, '提交', 'submit', 1995143390566228006, '1995143390566227980,1995143390566228004,1995143390566228006,1995143390566228011', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228012, '重新拆分', 'AiKnowledgeDocResplit', 1995143390566228004, '1995143390566227980,1995143390566228004,1995143390566228012', 2, '', 2, 'knowledge-doc/:knowledgeId/resplit/:docId', '/ai/knowledge/doc/resplit', '', '/ai/knowledge', 1, 1, 1, 0, 0, 0, 0, '', 1, '知识库文档重新拆分', 1932740993065500674, '2026-07-14 11:02:04', 1932740993065500674, '2026-07-14 11:02:04', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228013, '召回测试', 'AiKnowledgeSearch', 1995143390566228004, '1995143390566227980,1995143390566228004,1995143390566228013', 2, '', 3, 'knowledge-search/:knowledgeId', '/ai/knowledge/search', '', '/ai/knowledge', 1, 1, 1, 0, 0, 0, 0, '', 1, '知识库召回测试', 1932740993065500674, '2026-07-14 11:02:04', 1932740993065500674, '2026-07-14 11:02:04', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228014, '召回测试', 'search', 1995143390566228000, '1995143390566227980,1995143390566228000,1995143390566228014', 3, '', 4, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-14 11:02:05', 1932740993065500674, '2026-07-14 11:02:05', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228015, '提交', 'submit', 1995143390566228012, '1995143390566227980,1995143390566228004,1995143390566228012,1995143390566228015', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-14 11:02:06', 1932740993065500674, '2026-07-14 11:02:06', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228016, 'MCP管理', 'aiMcp', 1995143390566227980, '1995143390566227980,1995143390566228016', 2, 'ri:plug-line', 8, 'mcp', '/ai/mcp', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI MCP 管理', 1932740993065500674, '2026-07-15 00:00:00', 1932740993065500674, '2026-07-15 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228017, '添加', 'add', 1995143390566228016, '1995143390566227980,1995143390566228016,1995143390566228017', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-15 00:00:00', 1932740993065500674, '2026-07-15 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228018, '编辑', 'edit', 1995143390566228016, '1995143390566227980,1995143390566228016,1995143390566228018', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-15 00:00:00', 1932740993065500674, '2026-07-15 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228019, '删除', 'delete', 1995143390566228016, '1995143390566227980,1995143390566228016,1995143390566228019', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-15 00:00:00', 1932740993065500674, '2026-07-15 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228020, '聊天记录', 'aiChatHistory', 1995143390566227980, '1995143390566227980,1995143390566228020', 2, 'ri:history-line', 9, 'chat-history', '/ai/chat-history', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI 聊天记录（管理端）', 1932740993065500674, '2026-07-15 00:00:00', 1932740993065500674, '2026-07-15 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228021, '查看', 'view', 1995143390566228020, '1995143390566227980,1995143390566228020,1995143390566228021', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-15 00:00:00', 1932740993065500674, '2026-07-15 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228022, 'AI创作', 'aiCreate', 1995143390566227980, '1995143390566227980,1995143390566228022', 1, 'ri:palette-line', 7, 'create', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI 创作工具目录', 1932740993065500674, '2026-07-16 00:00:00', 1932740993065500674, '2026-07-16 16:51:10', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228023, 'AI绘画', 'aiPainting', 1995143390566228022, '1995143390566227980,1995143390566228022,1995143390566228023', 2, 'ri:image-ai-line', 0, 'painting', '/ai/create/painting', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI 绘画工作台', 1932740993065500674, '2026-07-16 00:00:00', 1932740993065500674, '2026-07-16 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228024, '思维导图', 'aiMindmap', 1995143390566228022, '1995143390566227980,1995143390566228022,1995143390566228024', 2, 'ri:node-tree', 1, 'mindmap', '/ai/create/mindmap', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI 思维导图工作台', 1932740993065500674, '2026-07-17 00:00:00', 1932740993065500674, '2026-07-17 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228025, 'AI文章', 'aiArticle', 1995143390566228022, '1995143390566227980,1995143390566228022,1995143390566228025', 2, 'ri:article-line', 2, 'article', '/ai/create/article', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI 文章写作工作台', 1932740993065500674, '2026-07-17 00:00:00', 1932740993065500674, '2026-07-17 00:00:00', 0);

-- ----------------------------
-- Table structure for bedrock_oss_config
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_oss_config`;
CREATE TABLE `bedrock_oss_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名称（如：生产环境-用户头像OSS）',
  `config_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置编码（唯一标识，如：PROD_AVATAR_OSS，用于代码中调用）',
  `service_provider` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'OSS服务商（枚举值：',
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'OSS访问端点（如阿里云：oss-cn-beijing.aliyuncs.com；内网端点需注明）',
  `access_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '访问密钥ID（AK，非敏感但需权限控制）',
  `secret_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '访问密钥密钥（SK，敏感信息，需加密存储）',
  `bucket_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'OSS存储桶名称（Bucket Name，全局唯一）',
  `bucket_region` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '存储桶地域（如：cn-beijing/cn-shanghai/ap-hongkong）',
  `prefix_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'upload' COMMENT '文件存储路径前缀（如：avatar/2025/，避免文件混乱）',
  `public_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '公开访问URL 默认是 endpoint',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用，下线时设为0）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_config_code`(`config_code`) USING BTREE COMMENT '配置编码查询索引',
  INDEX `idx_service_provider`(`service_provider`) USING BTREE COMMENT '按服务商筛选索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'OSS对象存储配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_param_config
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_param_config`;
CREATE TABLE `bedrock_param_config`  (
  `id` bigint(20) NOT NULL COMMENT '配置ID（主键）',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置键（唯一标识，如\"sys_api_timeout\"=接口超时时间、\"sys_file_max_size\"=文件最大上传大小）',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值（具体配置内容，如\"3000\"（毫秒）、\"10\"（MB））',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名称（显示用描述，如\"接口超时时间（毫秒）\"、\"文件最大上传大小（MB）\"）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注（如\"该配置超过10MB可能导致服务器存储压力增大\"）',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID（关联用户表sys_user.id）',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人ID（关联用户表sys_user.id）',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '逻辑删除：1=已删除，0=未删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uk_config_key`(`config_key`) USING BTREE COMMENT '配置键唯一（避免重复配置）'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统参数配置表（存储全局配置项，支持动态生效，无需重启服务）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_permission_api
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_permission_api`;
CREATE TABLE `bedrock_permission_api`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限名称',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限标识',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口路径（例：/api/user/list，与bedrock_api表path字段保持一致）',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'HTTP请求方法（枚举：GET、POST、PUT、DELETE、PATCH等）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限描述',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_menu_id`(`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色-接口关联表：维护角色可访问的接口权限关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_permission_datascope
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_permission_datascope`;
CREATE TABLE `bedrock_permission_datascope`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '自增主键',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限名称',
  `mapper_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Mapper方法唯一标识（格式：全限定类名.方法名，如com.bedrock.mapper.UserMapper.list）',
  `scope_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据范围类型（枚举：如ALL-全部数据、DEPT-本部门、OWN-本人等）',
  `scope_column` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库过滤列名（如dept_id、create_user_id）',
  `scope_field` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对应Java实体类字段名（如deptId、createUserId，用于ORM映射）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限描述',
  `scope_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据范围值',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_mapper_id`(`mapper_id`) USING BTREE,
  INDEX `idx_menu_id`(`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色-数据范围关联表：定义角色对特定Mapper方法的数据访问范围规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_role
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_role`;
CREATE TABLE `bedrock_role`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色名称',
  `role_alias` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色别名',
  `sort` tinyint(1) DEFAULT 0 COMMENT '排序',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色描述',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_alias`(`role_alias`) USING BTREE COMMENT '角色别名检索'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bedrock_role
-- ----------------------------
INSERT INTO `bedrock_role` VALUES (1932740993065500674, '超级管理员', 'administrator', 0, '超级管理员', '000000', '2025-10-03 06:54:17', '2025-10-03 06:54:19', 1932740993065500674, 1932740993065500674, 0);

-- ----------------------------
-- Table structure for bedrock_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_role_menu`;
CREATE TABLE `bedrock_role_menu`  (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_id_menu_id_unique`(`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色关联菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_role_permission`;
CREATE TABLE `bedrock_role_permission`  (
  `id` bigint(20) NOT NULL,
  `perm_type` tinyint(1) DEFAULT NULL COMMENT '权限类型，1--->接口权限，2---->数据权限',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_id_permission_id_unique`(`role_id`, `permission_id`) USING BTREE COMMENT '角色权限'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_sms_config
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_sms_config`;
CREATE TABLE `bedrock_sms_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名称',
  `config_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置编码（唯一标识）',
  `service_provider` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '短信服务商',
  `api_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '短信 API 账号',
  `api_secret` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '短信 API 密钥',
  `signature` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '短信签名',
  `template_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '短信模板 ID',
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '服务端点',
  `region` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '服务地域',
  `app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '短信应用 AppId（腾讯云）',
  `sender` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '短信通道号（华为云）',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_config_code`(`config_code`) USING BTREE COMMENT '配置编码查询索引',
  INDEX `idx_service_provider`(`service_provider`) USING BTREE COMMENT '按服务商筛选索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短信配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bedrock_tenant
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_tenant`;
CREATE TABLE `bedrock_tenant`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户账号（对应admin表里面的username）',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户唯一标识（非自增）',
  `tenant_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户全称（如 阿里巴巴集团有限公司）',
  `tenant_short_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户简称（如 阿里，用于显示优化）',
  `tenant_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户Logo地址（OSS/CDN链接）',
  `industry` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所属行业（如 互联网、金融、制造，可关联字典表）',
  `business_desc` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务描述（租户核心业务简介）',
  `contact_person` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人手机号（用于登录验证、通知）',
  `contact_email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人邮箱（用于找回密码、系统通知）',
  `contact_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系地址',
  `package_id` bigint(20) DEFAULT NULL COMMENT '套餐ID（关联套餐表）',
  `db_instance_id` bigint(20) DEFAULT NULL COMMENT '独立数据库实例ID（独立数据库时使用，关联数据库配置表）',
  `tenant_domain` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户专属域名（如 alibaba.xxx.com，支持多域名绑定）',
  `max_user_num` int(11) DEFAULT 100 COMMENT '最大用户数（-1表示无限制）',
  `expire_time` datetime(0) DEFAULT NULL COMMENT '过期时间（为空表示永久有效，用于付费租户）',
  `disable_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '禁用/注销原因（如 欠费、违规操作）',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '租户状态（1-正常，0-禁用）',
  `ext_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '扩展配置（JSON格式，如 {\"sms_enabled\":true, \"wechat_login\":false}）',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注（如 2025年付费旗舰版租户）',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_tenant_id`(`tenant_id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_domain`(`tenant_domain`) USING BTREE COMMENT '专属域名唯一',
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_expire_time`(`expire_time`) USING BTREE,
  INDEX `idx_tenant_name`(`tenant_name`) USING BTREE COMMENT '支持租户名称模糊查询'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户信息表（存储所有租户的核心配置与基础信息）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bedrock_tenant
-- ----------------------------
INSERT INTO `bedrock_tenant` VALUES (1995189446381293550, 'superadmin', '000000', '默认租户', '默认租户', NULL, NULL, '系统初始化租户信息', 'jiayazhou', '188xxxxxxxx', 'xxxxxx@qq.com', NULL, NULL, NULL, NULL, 0, NULL, NULL, 1, '', '系统初始化租户信息', '2025-12-01 01:53:28', '2025-12-01 02:00:56', 1932740993065500674, 1932740993065500674, 0);

-- ----------------------------
-- Table structure for bedrock_tenant_package
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_tenant_package`;
CREATE TABLE `bedrock_tenant_package`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '套餐名称',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `menu_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '菜单id',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户套餐表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

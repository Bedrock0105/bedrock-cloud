-- ============================================================
-- Docker 初始化脚本 02：AI 模块表 / 菜单 / 字典
-- 来源：doc/sql/mysql/bedrock-ai.sql
-- 执行时机：在 01-bedrock.sql 之后自动执行（依赖主库已存在）
-- 说明：bedrock-ai-*.sql 增量脚本请按需手工执行，勿加入自动初始化
-- ============================================================
-- ----------------------------
-- AI 模块初始化 SQL
-- 菜单 ID 从 1995143390566227980 起自增
-- 字典 ID 从 1983727715897516054 起自增（aiPlatform 至 1983727715897516080）
-- ----------------------------

-- AI 模型类型字典
INSERT INTO `bedrock_dict` VALUES (1983727715897516054, 'aiModelType', 'AI模型类型', 'AI模型类型', 'info', 0, 1, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516055, 'aiModelType', '对话', 'CHAT', 'primary', 1983727715897516054, 0, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516056, 'aiModelType', '图片', 'IMAGE', 'success', 1983727715897516054, 1, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516057, 'aiModelType', '文字转语音', 'SPEECH', 'warning', 1983727715897516054, 2, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516058, 'aiModelType', '向量', 'EMBEDDING', 'info', 1983727715897516054, 3, 1, NULL, '2026-07-07 00:00:00', '2026-07-07 00:00:00', 1932740993065500674, 1932740993065500674, 0);

-- AI 厂商平台字典
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

-- AI 管理菜单
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

-- ----------------------------
-- Table structure for bedrock_ai_api_key
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_api_key`;
CREATE TABLE `bedrock_ai_api_key` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `key_name` varchar(100) NOT NULL COMMENT '名称',
  `api_key` varchar(500) NOT NULL COMMENT 'API Key',
  `platform` varchar(20) NOT NULL COMMENT '厂商平台',
  `base_url` varchar(500) DEFAULT NULL COMMENT 'Base URL',
  `api_path` varchar(200) DEFAULT NULL COMMENT 'API 半路径（相对 Base URL，如 /v1/chat/completions）',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_key_name`(`key_name`) USING BTREE,
  INDEX `idx_platform`(`platform`) USING BTREE
) ENGINE=InnoDB COMMENT='AI API Key 配置表' ROW_FORMAT=Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_model
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_model`;
CREATE TABLE `bedrock_ai_model` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `api_key_id` bigint(20) NOT NULL COMMENT '关联 bedrock_ai_api_key 表主键 id',
  `model_name` varchar(100) NOT NULL COMMENT '模型名称',
  `model_avatar` varchar(500) DEFAULT NULL COMMENT '模型头像 URL',
  `model` varchar(100) NOT NULL COMMENT '模型标识',
  `platform` varchar(20) NOT NULL COMMENT '厂商平台',
  `model_type` varchar(20) NOT NULL COMMENT '模型类型',
  `temperature` double(2,2) DEFAULT NULL COMMENT '温度（0-1）',
  `max_tokens` int(11) DEFAULT NULL COMMENT '单条回复最大 Token 数量',
  `max_messages` int(11) DEFAULT NULL COMMENT '最大消息数量（来回算一条）',
  `support_multimodal` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否支持多模态图片识别（1=是，0=否，仅对话模型有效）',
  `sort_order` int(11) NOT NULL DEFAULT 0 COMMENT '排序值，越小越靠前',
  `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否默认模型（1=是，0=否，同类型仅一个）',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_api_key_id`(`api_key_id`) USING BTREE,
  INDEX `idx_model`(`model`) USING BTREE,
  INDEX `idx_platform`(`platform`) USING BTREE
) ENGINE=InnoDB COMMENT='AI 模型配置表' ROW_FORMAT=Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_role
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_role`;
CREATE TABLE `bedrock_ai_role` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `role_avatar` varchar(500) DEFAULT NULL COMMENT '角色头像',
  `remark` varchar(500) DEFAULT NULL COMMENT '角色描述',
  `system_prompt` text COMMENT '角色设定（系统提示词）',
  `model_id` bigint(20) DEFAULT NULL COMMENT '默认模型 id',
  `chat_options` text DEFAULT NULL COMMENT '对话调用配置（工具、知识库检索等，JSON 字符串）',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `tenant_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_name`(`role_name`) USING BTREE
) ENGINE=InnoDB COMMENT='AI 角色配置表' ROW_FORMAT=Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_cs_config
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_cs_config`;
CREATE TABLE `bedrock_ai_cs_config` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '客服名称',
  `avatar` varchar(500) DEFAULT NULL COMMENT '客服头像',
  `remark` varchar(500) DEFAULT NULL COMMENT '描述',
  `system_prompt` text COMMENT '系统提示词',
  `model_id` bigint(20) DEFAULT NULL COMMENT '默认模型 id',
  `chat_options` text DEFAULT NULL COMMENT '对话调用配置（工具、知识库等，JSON）',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `tenant_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB COMMENT='AI 客服配置表（租户级单条）' ROW_FORMAT=Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_chat_record
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_chat_record`;
CREATE TABLE `bedrock_ai_chat_record` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '操作人用户ID',
  `model_id` bigint(20) NOT NULL COMMENT '使用的AI模型ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT 'AI角色ID',
  `title` varchar(200) DEFAULT NULL COMMENT '会话标题',
  `system_prompt` text COMMENT '系统提示词',
  `is_top` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否置顶：0否 1是',
  `chat_options` text DEFAULT NULL COMMENT '对话调用配置（温度、工具、知识库检索等，JSON 字符串）',
  `total_prompt_tokens` int(11) DEFAULT 0 COMMENT '累计输入token',
  `total_completion_tokens` int(11) DEFAULT 0 COMMENT '累计输出token',
  `total_tokens` int(11) DEFAULT 0 COMMENT '总消耗token',
  `chat_type` varchar(20) DEFAULT 'CHAT' COMMENT '对话类型',
  `tenant_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_model_id`(`model_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE
) ENGINE=InnoDB COMMENT='AI聊天会话主记录表' ROW_FORMAT=Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_chat_message
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_chat_message`;
CREATE TABLE `bedrock_ai_chat_message` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `record_id` bigint(20) NOT NULL COMMENT '关联 bedrock_ai_chat_record 主键 id',
  `user_id` bigint(20) NOT NULL COMMENT '操作人用户ID',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父消息 ID',
  `model_id` bigint(20) DEFAULT NULL COMMENT '使用的AI模型ID',
  `model` varchar(100) DEFAULT NULL COMMENT '模型标识',
  `role` varchar(20) NOT NULL COMMENT '消息角色：user/assistant/system/tool',
  `content` text COMMENT '消息内容',
  `reasoning` text COMMENT '推理/思考过程内容',
  `tool_calls` text COMMENT 'assistant 工具调用列表 JSON',
  `chunk_ids` text DEFAULT NULL COMMENT '知识库分片ID列表 JSON',
  `attachments` text DEFAULT NULL COMMENT '用户消息附件列表 JSON',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_record_id`(`record_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE=InnoDB COMMENT='AI聊天消息明细表' ROW_FORMAT=Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_token_usage
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_token_usage`;
CREATE TABLE `bedrock_ai_token_usage` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `record_id` bigint(20) DEFAULT NULL COMMENT '关联 bedrock_ai_chat_record.id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户 id',
  `model_id` bigint(20) DEFAULT NULL COMMENT '模型 id',
  `model` varchar(100) DEFAULT NULL COMMENT '模型标识',
  `api_key_id` bigint(20) DEFAULT NULL COMMENT 'API Key id',
  `api_key_name` varchar(100) DEFAULT NULL COMMENT 'API Key 名称',
  `platform` varchar(20) DEFAULT NULL COMMENT '厂商平台',
  `user_message_id` bigint(20) DEFAULT NULL COMMENT 'user 消息 id',
  `assistant_message_id` bigint(20) DEFAULT NULL COMMENT 'assistant 消息 id',
  `stream` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否流式',
  `prompt_tokens` int(11) NOT NULL DEFAULT 0 COMMENT '输入 token',
  `completion_tokens` int(11) NOT NULL DEFAULT 0 COMMENT '输出 token',
  `total_tokens` int(11) NOT NULL DEFAULT 0 COMMENT '总 token',
  `usage_source` varchar(20) NOT NULL COMMENT '用量来源：MODEL_REPORTED/LOCAL_ESTIMATED',
  `total_latency_ms` int(11) DEFAULT NULL COMMENT '端到端总耗时(ms)',
  `first_token_latency_ms` int(11) DEFAULT NULL COMMENT '首 token 耗时(ms)',
  `streaming_duration_ms` int(11) DEFAULT NULL COMMENT '流式生成耗时(ms)',
  `tokens_per_second` double(10,2) DEFAULT NULL COMMENT '输出吞吐(tokens/s)',
  `started_at` datetime DEFAULT NULL COMMENT '请求开始时间',
  `completed_at` datetime DEFAULT NULL COMMENT '请求结束时间',
  `tenant_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_record_id`(`record_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_model_id`(`model_id`) USING BTREE,
  INDEX `idx_api_key_id`(`api_key_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB COMMENT='AI Token 用量明细表' ROW_FORMAT=Dynamic;

-- AI 聊天菜单
INSERT INTO `bedrock_menu` VALUES (1995143390566227993, 'AI聊天', 'aiChat', 1995143390566227980, '1995143390566227980,1995143390566227993', 2, 'ri:chat-3-line', 3, 'chat', '/ai/chat', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI聊天', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227994, 'Token用量', 'aiTokenUsage', 1995143390566227980, '1995143390566227980,1995143390566227994', 2, 'ri:coin-line', 4, 'token-usage', '/ai/token-usage', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI Token用量', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227995, '查看', 'view', 1995143390566227994, '1995143390566227980,1995143390566227994,1995143390566227995', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-07 00:00:00', 1932740993065500674, '2026-07-07 00:00:00', 0);

-- 向量数据库类型字典
INSERT INTO `bedrock_dict` VALUES (1983727715897516081, 'aiVectorDbType', '向量数据库类型', '向量数据库类型', 'info', 0, 3, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516082, 'aiVectorDbType', 'Redis 向量', 'REDIS', 'primary', 1983727715897516081, 0, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516083, 'aiVectorDbType', 'Milvus 向量', 'MILVUS', 'success', 1983727715897516081, 1, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516084, 'aiVectorDbType', 'Elasticsearch 向量', 'ELASTICSEARCH', 'warning', 1983727715897516081, 2, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516085, 'aiVectorDbType', '本地向量', 'SIMPLE', 'info', 1983727715897516081, 3, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);

-- 向量数据库管理菜单
INSERT INTO `bedrock_menu` VALUES (1995143390566227996, '向量数据库', 'aiVectorDb', 1995143390566227980, '1995143390566227980,1995143390566227996', 2, 'ri:database-2-line', 5, 'vector-db', '/ai/vector-db', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '向量数据库管理', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227997, '添加', 'add', 1995143390566227996, '1995143390566227980,1995143390566227996,1995143390566227997', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227998, '编辑', 'edit', 1995143390566227996, '1995143390566227980,1995143390566227996,1995143390566227998', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566227999, '删除', 'delete', 1995143390566227996, '1995143390566227980,1995143390566227996,1995143390566227999', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);

-- 知识库管理菜单
INSERT INTO `bedrock_menu` VALUES (1995143390566228000, '知识库管理', 'aiKnowledge', 1995143390566227980, '1995143390566227980,1995143390566228000', 2, 'ri:book-2-line', 6, 'knowledge', '/ai/knowledge', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI 知识库管理', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228001, '添加', 'add', 1995143390566228000, '1995143390566227980,1995143390566228000,1995143390566228001', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228002, '编辑', 'edit', 1995143390566228000, '1995143390566227980,1995143390566228000,1995143390566228002', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228003, '删除', 'delete', 1995143390566228000, '1995143390566227980,1995143390566228000,1995143390566228003', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228007, '文档', 'doc', 1995143390566228000, '1995143390566227980,1995143390566228000,1995143390566228007', 3, '', 3, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228014, '召回测试', 'search', 1995143390566228000, '1995143390566227980,1995143390566228000,1995143390566228014', 3, '', 4, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);

-- 知识库文档隐藏目录（参考 permission-configuration）
INSERT INTO `bedrock_menu` VALUES (1995143390566228004, '知识库文档配置', 'aiKnowledgeDocConfig', 1995143390566227980, '1995143390566227980,1995143390566228004', 1, '', 7, '/', '', '', NULL, 1, 1, 1, 0, 0, 0, 0, '', 1, '知识库文档隐藏目录', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228005, '知识库文档', 'AiKnowledgeDoc', 1995143390566228004, '1995143390566227980,1995143390566228004,1995143390566228005', 2, '', 0, 'knowledge-doc/:knowledgeId', '/ai/knowledge/doc', '', '/ai/knowledge', 1, 1, 1, 0, 0, 0, 0, '', 1, '知识库文档管理', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228006, '上传文档', 'AiKnowledgeDocUpload', 1995143390566228004, '1995143390566227980,1995143390566228004,1995143390566228006', 2, '', 1, 'knowledge-doc/:knowledgeId/upload', '/ai/knowledge/doc/upload', '', '/ai/knowledge', 1, 1, 1, 0, 0, 0, 0, '', 1, '知识库文档上传向导', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228012, '重新拆分', 'AiKnowledgeDocResplit', 1995143390566228004, '1995143390566227980,1995143390566228004,1995143390566228012', 2, '', 2, 'knowledge-doc/:knowledgeId/resplit/:docId', '/ai/knowledge/doc/resplit', '', '/ai/knowledge', 1, 1, 1, 0, 0, 0, 0, '', 1, '知识库文档重新拆分', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228013, '召回测试', 'AiKnowledgeSearch', 1995143390566228004, '1995143390566227980,1995143390566228004,1995143390566228013', 2, '', 3, 'knowledge-search/:knowledgeId', '/ai/knowledge/search', '', '/ai/knowledge', 1, 1, 1, 0, 0, 0, 0, '', 1, '知识库召回测试', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228008, '添加', 'add', 1995143390566228005, '1995143390566227980,1995143390566228004,1995143390566228005,1995143390566228008', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228009, '编辑', 'edit', 1995143390566228005, '1995143390566227980,1995143390566228004,1995143390566228005,1995143390566228009', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228010, '删除', 'delete', 1995143390566228005, '1995143390566227980,1995143390566228004,1995143390566228005,1995143390566228010', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228011, '提交', 'submit', 1995143390566228006, '1995143390566227980,1995143390566228004,1995143390566228006,1995143390566228011', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228015, '提交', 'submit', 1995143390566228012, '1995143390566227980,1995143390566228004,1995143390566228012,1995143390566228015', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-12 00:00:00', 1932740993065500674, '2026-07-12 00:00:00', 0);

-- ----------------------------
-- Table structure for bedrock_ai_knowledge
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_knowledge`;
CREATE TABLE `bedrock_ai_knowledge` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `knowledge_name` varchar(100) NOT NULL COMMENT '知识库名称',
  `remark` varchar(500) DEFAULT NULL COMMENT '知识库描述',
  `embedding_model_id` bigint(20) NOT NULL COMMENT '嵌入模型 id',
  `vector_db_id` bigint(20) NOT NULL COMMENT '向量库 id',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `tenant_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_knowledge_name`(`knowledge_name`) USING BTREE,
  INDEX `idx_embedding_model_id`(`embedding_model_id`) USING BTREE,
  INDEX `idx_vector_db_id`(`vector_db_id`) USING BTREE
) ENGINE=InnoDB COMMENT='AI 知识库配置表' ROW_FORMAT=Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_knowledge_doc
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_knowledge_doc`;
CREATE TABLE `bedrock_ai_knowledge_doc` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `knowledge_id` bigint(20) NOT NULL COMMENT '关联知识库主键，关联 bedrock_ai_knowledge.id',
  `doc_title` varchar(255) NOT NULL COMMENT '文档标题/文件名',
  `doc_source_type` varchar(30) NOT NULL COMMENT '来源类型：UPLOAD_FILE本地上传、MANUAL_TEXT手动录入',
  `file_url` varchar(500) DEFAULT NULL COMMENT 'OSS文件存储路径',
  `file_size` bigint(20) DEFAULT NULL COMMENT '源文件字节大小',
  `file_suffix` varchar(20) DEFAULT NULL COMMENT '文件后缀：pdf/docx/markdown/txt',
  `slice_mode` varchar(50) DEFAULT NULL COMMENT '分段分片模式（分片算法策略）',
  `recall_count` bigint(20) NOT NULL DEFAULT 0 COMMENT '文档被检索召回总次数',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `tenant_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_knowledge_id`(`knowledge_id`) USING BTREE
) ENGINE=InnoDB COMMENT='知识库-文档元数据表' ROW_FORMAT=Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_knowledge_chunk
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_knowledge_chunk`;
CREATE TABLE `bedrock_ai_knowledge_chunk` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `knowledge_id` bigint(20) NOT NULL COMMENT '所属知识库ID',
  `doc_id` bigint(20) NOT NULL COMMENT '归属文档主键，关联 bedrock_ai_knowledge_doc.id',
  `chunk_no` int(11) NOT NULL COMMENT '文档内分片序号（从1自增，用于还原段落顺序）',
  `chunk_content` longtext NOT NULL COMMENT '分片纯文本内容（送入Embedding做向量化）',
  `chunk_token_count` int(11) DEFAULT NULL COMMENT '本段文本预估token数量，用于分片策略复盘',
  `vector_id` varchar(128) DEFAULT NULL COMMENT '向量库内对应向量唯一ID（Milvus/Qdrant/Redis Stack主键）',
  `embed_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '向量化状态：0待向量化、1入库成功、2向量写入失败',
  `recall_count` bigint(20) NOT NULL DEFAULT 0 COMMENT '当前分片被检索召回总次数',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `tenant_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_vector_id`(`vector_id`) USING BTREE,
  INDEX `idx_knowledge_id`(`knowledge_id`) USING BTREE,
  INDEX `idx_doc_id`(`doc_id`) USING BTREE
) ENGINE=InnoDB COMMENT='知识库-文档分片段落表（向量存储最小单元）' ROW_FORMAT=Dynamic;

-- 知识库文档来源类型字典
INSERT INTO `bedrock_dict` VALUES (1983727715897516090, 'aiKnowledgeDocSourceType', '知识库文档来源', '知识库文档来源', 'info', 0, 4, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516091, 'aiKnowledgeDocSourceType', '本地上传', 'UPLOAD_FILE', 'primary', 1983727715897516090, 0, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516092, 'aiKnowledgeDocSourceType', '手动录入', 'MANUAL_TEXT', 'success', 1983727715897516090, 1, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);

-- 知识库分片向量化状态字典
INSERT INTO `bedrock_dict` VALUES (1983727715897516093, 'aiKnowledgeEmbedStatus', '分片向量化状态', '分片向量化状态', 'info', 0, 4, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516094, 'aiKnowledgeEmbedStatus', '待向量化', '0', 'info', 1983727715897516093, 0, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516095, 'aiKnowledgeEmbedStatus', '入库成功', '1', 'success', 1983727715897516093, 1, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516096, 'aiKnowledgeEmbedStatus', '向量写入失败', '2', 'danger', 1983727715897516093, 2, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);

-- 文档分片模式字典（对应 DocumentSliceModeEnum；PARAGRAPH 为前端专用，映射 RECURSIVE_CHAR + paragraphOnly）
INSERT INTO `bedrock_dict` VALUES (1983727715897516100, 'aiDocumentSliceMode', '文档分片模式', '文档分片模式', 'info', 0, 4, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516101, 'aiDocumentSliceMode', '固定长度分片', 'FIXED_LENGTH', 'primary', 1983727715897516100, 0, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516102, 'aiDocumentSliceMode', '语义智能分片', 'SEMANTIC', 'success', 1983727715897516100, 1, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516103, 'aiDocumentSliceMode', '标题层级分片', 'HIERARCHY', 'warning', 1983727715897516100, 2, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516104, 'aiDocumentSliceMode', '自定义分隔符分片', 'DELIMITER', 'info', 1983727715897516100, 3, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516105, 'aiDocumentSliceMode', '递归字符分片', 'RECURSIVE_CHAR', 'primary', 1983727715897516100, 4, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516106, 'aiDocumentSliceMode', '段落分片', 'PARAGRAPH', 'success', 1983727715897516100, 5, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516107, 'aiDocumentSliceMode', '完整文档不分片', 'WHOLE_DOC', 'info', 1983727715897516100, 6, 1, NULL, '2026-07-12 00:00:00', '2026-07-12 00:00:00', 1932740993065500674, 1932740993065500674, 0);

-- MCP 传输类型字典
INSERT INTO `bedrock_dict` VALUES (1983727715897516110, 'aiMcpTransportType', 'MCP传输类型', 'MCP传输类型', 'info', 0, 5, 1, NULL, '2026-07-15 00:00:00', '2026-07-15 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516111, 'aiMcpTransportType', 'SSE', 'SSE', 'primary', 1983727715897516110, 0, 1, NULL, '2026-07-15 00:00:00', '2026-07-15 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516112, 'aiMcpTransportType', 'Streamable HTTP', 'STREAMABLE_HTTP', 'success', 1983727715897516110, 1, 1, NULL, '2026-07-15 00:00:00', '2026-07-15 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516113, 'aiMcpTransportType', 'STDIO', 'STDIO', 'warning', 1983727715897516110, 2, 1, NULL, '2026-07-15 00:00:00', '2026-07-15 00:00:00', 1932740993065500674, 1932740993065500674, 0);

-- MCP 管理菜单
INSERT INTO `bedrock_menu` VALUES (1995143390566228016, 'MCP管理', 'aiMcp', 1995143390566227980, '1995143390566227980,1995143390566228016', 2, 'ri:plug-line', 8, 'mcp', '/ai/mcp', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI MCP 管理', 1932740993065500674, '2026-07-15 00:00:00', 1932740993065500674, '2026-07-15 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228017, '添加', 'add', 1995143390566228016, '1995143390566227980,1995143390566228016,1995143390566228017', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-15 00:00:00', 1932740993065500674, '2026-07-15 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228018, '编辑', 'edit', 1995143390566228016, '1995143390566227980,1995143390566228016,1995143390566228018', 3, '', 1, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-15 00:00:00', 1932740993065500674, '2026-07-15 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228019, '删除', 'delete', 1995143390566228016, '1995143390566227980,1995143390566228016,1995143390566228019', 3, '', 2, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-15 00:00:00', 1932740993065500674, '2026-07-15 00:00:00', 0);

-- 聊天记录管理（管理端：租户内全员会话）
INSERT INTO `bedrock_menu` VALUES (1995143390566228020, '聊天记录', 'aiChatHistory', 1995143390566227980, '1995143390566227980,1995143390566228020', 2, 'ri:history-line', 9, 'chat-history', '/ai/chat-history', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI 聊天记录（管理端）', 1932740993065500674, '2026-07-15 00:00:00', 1932740993065500674, '2026-07-15 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228021, '查看', 'view', 1995143390566228020, '1995143390566227980,1995143390566228020,1995143390566228021', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-15 00:00:00', 1932740993065500674, '2026-07-15 00:00:00', 0);

-- AI 创作（二级目录）+ AI 绘画工作台
INSERT INTO `bedrock_menu` VALUES (1995143390566228022, 'AI创作', 'aiCreate', 1995143390566227980, '1995143390566227980,1995143390566228022', 1, 'ri:palette-line', 7, 'create', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI 创作工具目录', 1932740993065500674, '2026-07-16 00:00:00', 1932740993065500674, '2026-07-16 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228023, 'AI绘画', 'aiPainting', 1995143390566228022, '1995143390566227980,1995143390566228022,1995143390566228023', 2, 'ri:image-ai-line', 0, 'painting', '/ai/create/painting', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI 绘画工作台', 1932740993065500674, '2026-07-16 00:00:00', 1932740993065500674, '2026-07-16 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228024, '思维导图', 'aiMindmap', 1995143390566228022, '1995143390566227980,1995143390566228022,1995143390566228024', 2, 'ri:node-tree', 1, 'mindmap', '/ai/create/mindmap', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI 思维导图工作台', 1932740993065500674, '2026-07-17 00:00:00', 1932740993065500674, '2026-07-17 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228025, 'AI文章', 'aiArticle', 1995143390566228022, '1995143390566227980,1995143390566228022,1995143390566228025', 2, 'ri:article-line', 2, 'article', '/ai/create/article', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, 'AI 文章写作工作台', 1932740993065500674, '2026-07-17 00:00:00', 1932740993065500674, '2026-07-17 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228030, 'AI客服配置', 'aiCsConfig', 1995143390566227980, '1995143390566227980,1995143390566228030', 2, 'ri:customer-service-2-line', 10, 'cs-config', '/ai/cs-config', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '租户级 AI 客服配置（单页）', 1932740993065500674, '2026-07-30 00:00:00', 1932740993065500674, '2026-07-30 00:00:00', 0);
INSERT INTO `bedrock_menu` VALUES (1995143390566228031, '保存', 'edit', 1995143390566228030, '1995143390566227980,1995143390566228030,1995143390566228031', 3, '', 0, '', '', '', NULL, 1, 0, 0, 0, 0, 0, 0, '', 1, '按钮', 1932740993065500674, '2026-07-30 00:00:00', 1932740993065500674, '2026-07-30 00:00:00', 0);

-- AI 文章创作字典
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

-- AI 会话类型字典
INSERT INTO `bedrock_dict` VALUES (1983727715897516170, 'aiChatType', 'AI会话类型', 'AI会话类型', 'info', 0, 11, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516171, 'aiChatType', '普通对话', 'CHAT', 'primary', 1983727715897516170, 0, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516172, 'aiChatType', '图片生成', 'IMAGE', 'success', 1983727715897516170, 1, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516173, 'aiChatType', '思维导图', 'MINDMAP', 'warning', 1983727715897516170, 2, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516174, 'aiChatType', '文章写作', 'ARTICLE', 'info', 1983727715897516170, 3, 1, NULL, '2026-07-17 00:00:00', '2026-07-17 00:00:00', 1932740993065500674, 1932740993065500674, 0);
INSERT INTO `bedrock_dict` VALUES (1983727715897516175, 'aiChatType', 'AI客服', 'CUSTOMER_SERVICE', 'danger', 1983727715897516170, 4, 1, NULL, '2026-07-30 00:00:00', '2026-07-30 00:00:00', 1932740993065500674, 1932740993065500674, 0);

-- ----------------------------
-- Table structure for bedrock_ai_mcp
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_mcp`;
CREATE TABLE `bedrock_ai_mcp` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '业务名称（创建后不可变）',
  `type` varchar(30) NOT NULL COMMENT '传输类型：SSE/STREAMABLE_HTTP/STDIO',
  `url` varchar(500) DEFAULT NULL COMMENT 'HTTP baseUrl（SSE/StreamableHTTP）',
  `endpoint` varchar(200) DEFAULT NULL COMMENT 'HTTP endpoint',
  `headers` text COMMENT '可选 HTTP 请求头（JSON）',
  `stdio_servers_json` text COMMENT 'STDIO：Claude Desktop 格式 JSON',
  `client_name` varchar(100) DEFAULT NULL COMMENT 'MCP client 展示名',
  `version` varchar(50) DEFAULT NULL COMMENT '客户端版本',
  `request_timeout_seconds` int(11) DEFAULT NULL COMMENT '请求超时秒数',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE
) ENGINE=InnoDB COMMENT='AI MCP 客户端配置表' ROW_FORMAT=Dynamic;

-- ----------------------------
-- Table structure for bedrock_ai_vector_db
-- ----------------------------
DROP TABLE IF EXISTS `bedrock_ai_vector_db`;
CREATE TABLE `bedrock_ai_vector_db` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `db_name` varchar(100) NOT NULL COMMENT '配置名称',
  `vector_db_type` varchar(20) NOT NULL COMMENT '向量数据库类型',
  `host` varchar(200) NOT NULL COMMENT '主机地址',
  `port` int(11) DEFAULT NULL COMMENT '端口',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  `database_name` varchar(100) DEFAULT NULL COMMENT '逻辑库/索引名',
  `collection_name` varchar(100) DEFAULT NULL COMMENT '集合名/Key前缀（Milvus集合名、Redis前缀）',
  `embedding_dimension` int(11) DEFAULT NULL COMMENT '向量维度',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '配置状态（1=启用，0=禁用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_db_name`(`db_name`) USING BTREE,
  INDEX `idx_vector_db_type`(`vector_db_type`) USING BTREE
) ENGINE=InnoDB COMMENT='向量数据库配置表' ROW_FORMAT=Dynamic;

-- ----------------------------
-- 已有库升级脚本（按需执行）
-- ALTER TABLE `bedrock_ai_role` ADD COLUMN `model_id` bigint(20) DEFAULT NULL COMMENT '默认模型 id' AFTER `system_prompt`;
-- ALTER TABLE `bedrock_ai_model` ADD COLUMN `model_avatar` varchar(500) DEFAULT NULL COMMENT '模型头像 URL' AFTER `model_name`;
-- ALTER TABLE `bedrock_ai_model` ADD COLUMN `sort_order` int(11) NOT NULL DEFAULT 0 COMMENT '排序值，越小越靠前' AFTER `max_messages`;
-- ALTER TABLE `bedrock_ai_model` ADD COLUMN `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否默认模型（1=是，0=否，同类型仅一个）' AFTER `sort_order`;
-- ALTER TABLE `bedrock_ai_chat_message` ADD COLUMN `tool_calls` text COMMENT 'assistant 工具调用列表 JSON' AFTER `reasoning`;
-- ALTER TABLE `bedrock_ai_role` ADD COLUMN `tools` varchar(500) DEFAULT NULL COMMENT '关联工具组名称，逗号分隔' AFTER `model_id`;
-- ALTER TABLE `bedrock_ai_chat_record` ADD COLUMN `tools` varchar(500) DEFAULT NULL COMMENT '关联工具组名称，逗号分隔' AFTER `max_contexts`;
-- DROP TABLE IF EXISTS `bedrock_ai_token_usage`;
-- CREATE TABLE `bedrock_ai_token_usage` (...同上...);
-- INSERT INTO `bedrock_menu` VALUES (1995143390566227994, 'Token用量', ...);
-- INSERT INTO `bedrock_dict` VALUES (1983727715897516085, 'aiVectorDbType', '本地向量', 'SIMPLE', 'info', 1983727715897516081, 3, 1, ...);
-- ALTER TABLE `bedrock_ai_vector_db` CHANGE COLUMN `store_name` `db_name` varchar(100) NOT NULL COMMENT '配置名称';
-- ALTER TABLE `bedrock_ai_vector_db` CHANGE COLUMN `vector_store_type` `vector_db_type` varchar(20) NOT NULL COMMENT '向量数据库类型';
-- ALTER TABLE `bedrock_ai_knowledge` CHANGE COLUMN `vector_store_id` `vector_db_id` bigint(20) NOT NULL COMMENT '向量库 id';
-- UPDATE `bedrock_dict` SET dict_code = 'aiVectorDbType' WHERE dict_code = 'aiVectorStoreType';
-- UPDATE `bedrock_menu` SET menu_code = 'aiVectorDb', path = 'vector-db', component = '/ai/vector-db' WHERE menu_code = 'aiVectorStore';
-- ALTER TABLE `bedrock_ai_vector_db` ADD COLUMN `collection_name` varchar(100) DEFAULT NULL COMMENT '集合名/Key前缀（Milvus集合名、Redis前缀）' AFTER `database_name`;
-- UPDATE `bedrock_dict` SET dict_label = '向量' WHERE dict_code = 'aiModelType' AND dict_value = 'EMBEDDING';
-- ALTER TABLE `bedrock_ai_role` ADD COLUMN `chat_options` text DEFAULT NULL COMMENT '对话调用配置（工具、知识库检索等，JSON 字符串）' AFTER `model_id`;
-- ALTER TABLE `bedrock_ai_role` DROP COLUMN `tools`, DROP COLUMN `knowledge_ids`, DROP COLUMN `similarities`, DROP COLUMN `top_ks`;
-- ALTER TABLE `bedrock_ai_chat_record` ADD COLUMN `chat_options` text DEFAULT NULL COMMENT '对话调用配置（温度、工具、知识库检索等，JSON 字符串）' AFTER `is_top`;
-- ALTER TABLE `bedrock_ai_chat_record` DROP COLUMN `temperature`, DROP COLUMN `max_tokens`, DROP COLUMN `max_messages`, DROP COLUMN `tools`, DROP COLUMN `knowledge_ids`, DROP COLUMN `similarities`, DROP COLUMN `top_ks`;
-- DROP TABLE IF EXISTS `bedrock_ai_knowledge`;
-- CREATE TABLE `bedrock_ai_knowledge` (...同上...);
-- INSERT INTO `bedrock_menu` VALUES (1995143390566228000, '知识库管理', ...);
-- DROP TABLE IF EXISTS `bedrock_ai_knowledge_doc`;
-- CREATE TABLE `bedrock_ai_knowledge_doc` (...同上...);
-- DROP TABLE IF EXISTS `bedrock_ai_knowledge_chunk`;
-- CREATE TABLE `bedrock_ai_knowledge_chunk` (...同上...);
-- INSERT INTO `bedrock_menu` VALUES (1995143390566228012, '重新拆分', 'AiKnowledgeDocResplit', ...);
-- INSERT INTO `bedrock_menu` VALUES (1995143390566228013, '召回测试', 'AiKnowledgeSearch', ...);
-- INSERT INTO `bedrock_menu` VALUES (1995143390566228014, '召回测试', 'search', 1995143390566228000, ...);
-- INSERT INTO `bedrock_menu` VALUES (1995143390566228015, '提交', 'submit', 1995143390566228012, ...);
-- DROP TABLE IF EXISTS `bedrock_ai_mcp`;
-- CREATE TABLE `bedrock_ai_mcp` (...同上...);
-- INSERT INTO `bedrock_menu` VALUES (1995143390566228016, 'MCP管理', ...);
-- INSERT INTO `bedrock_dict` VALUES (1983727715897516110, 'aiMcpTransportType', ...);
-- INSERT INTO `bedrock_menu` VALUES (1995143390566228020, '聊天记录', 'aiChatHistory', ...);
-- INSERT INTO `bedrock_menu` VALUES (1995143390566228021, '查看', 'view', ...);
-- INSERT INTO `bedrock_menu` VALUES (1995143390566228022, 'AI创作', 'aiCreate', ...);
-- INSERT INTO `bedrock_menu` VALUES (1995143390566228023, 'AI绘画', 'aiPainting', ...);
-- INSERT INTO `bedrock_menu` VALUES (1995143390566228024, '思维导图', 'aiMindmap', ...);
-- INSERT INTO `bedrock_menu` VALUES (1995143390566228025, 'AI文章', 'aiArticle', ...);
-- INSERT INTO `bedrock_dict` VALUES (1983727715897516120, 'aiArticleWordRange', ...);
-- INSERT INTO `bedrock_dict` VALUES (1983727715897516130, 'aiArticleGenre', ...);
-- INSERT INTO `bedrock_dict` VALUES (1983727715897516140, 'aiArticleTone', ...);
-- INSERT INTO `bedrock_dict` VALUES (1983727715897516150, 'aiArticleLang', ...);
-- INSERT INTO `bedrock_dict` VALUES (1983727715897516160, 'aiArticleFormat', ...);
-- INSERT INTO `bedrock_dict` VALUES (1983727715897516170, 'aiChatType', ...);
-- ALTER TABLE `bedrock_ai_chat_message` ADD COLUMN `chunk_ids` text DEFAULT NULL COMMENT '知识库分片ID列表 JSON' AFTER `tool_calls`;
-- ALTER TABLE `bedrock_ai_chat_message` ADD COLUMN `attachments` text DEFAULT NULL COMMENT '用户消息附件列表 JSON' AFTER `chunk_ids`;

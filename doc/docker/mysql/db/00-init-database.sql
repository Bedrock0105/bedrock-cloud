-- ============================================================
-- Docker 初始化脚本 00：确保业务库存在并切换默认库
-- 说明：MYSQL_DATABASE=bedrock 时官方镜像也会建库；此处显式保证后续 01/02 落在正确库
-- ============================================================

CREATE DATABASE IF NOT EXISTS `bedrock` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `bedrock`;

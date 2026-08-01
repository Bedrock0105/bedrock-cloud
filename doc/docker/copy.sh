#!/bin/sh
# ============================================================
# 将 Maven 打包产物复制到各模块 docker/jar 目录
# 用法：在 doc/docker 目录执行  sh copy.sh
# 前置：在 bedrock-cloud 根目录执行  mvn clean package
# 说明：优先使用根目录 target/*.jar（parent dependency-plugin 汇总）
# ============================================================

# 脚本所在目录（doc/docker）
DOCKER_DIR=$(cd "$(dirname "$0")" && pwd)
# 项目根目录（bedrock-cloud）
PROJECT_ROOT=$(cd "$DOCKER_DIR/../.." && pwd)
# 汇总 jar 目录
TARGET_DIR="$PROJECT_ROOT/target"

echo "============================================"
echo " Docker 目录: $DOCKER_DIR"
echo " 项目根目录: $PROJECT_ROOT"
echo "============================================"

# 复制单个 jar 到目标模块目录
# 参数：$1 模块目录名  $2 jar 文件名
copy_jar() {
  module_dir=$1
  jar_name=$2
  src=""

  # 优先：根 target 汇总包
  if [ -f "$TARGET_DIR/$jar_name" ]; then
    src="$TARGET_DIR/$jar_name"
  # 回退：各模块自身 target
  elif [ -f "$PROJECT_ROOT/bedrock-gateway/target/$jar_name" ] && [ "$module_dir" = "bedrock-gateway" ]; then
    src="$PROJECT_ROOT/bedrock-gateway/target/$jar_name"
  elif [ -f "$PROJECT_ROOT/bedrock-auth/target/$jar_name" ] && [ "$module_dir" = "bedrock-auth" ]; then
    src="$PROJECT_ROOT/bedrock-auth/target/$jar_name"
  elif [ -f "$PROJECT_ROOT/bedrock-modules/$module_dir/target/$jar_name" ]; then
    src="$PROJECT_ROOT/bedrock-modules/$module_dir/target/$jar_name"
  fi

  if [ -z "$src" ]; then
    echo "[跳过] 未找到 $jar_name，请先在项目根目录执行: mvn clean package"
    return 1
  fi

  mkdir -p "$DOCKER_DIR/$module_dir/jar"
  cp -f "$src" "$DOCKER_DIR/$module_dir/jar/$jar_name"
  echo "[完成] $src -> $module_dir/jar/$jar_name"
  return 0
}

# 按模块复制
copy_jar "bedrock-gateway"   "bedrock-gateway.jar"
copy_jar "bedrock-auth"      "bedrock-auth.jar"
copy_jar "bedrock-system"    "bedrock-system.jar"
copy_jar "bedrock-resource"  "bedrock-resource.jar"
copy_jar "bedrock-log"       "bedrock-log.jar"
copy_jar "bedrock-ai"        "bedrock-ai.jar"
copy_jar "bedrock-websocket" "bedrock-websocket.jar"

echo "============================================"
echo " copy.sh 执行结束"
echo "============================================"

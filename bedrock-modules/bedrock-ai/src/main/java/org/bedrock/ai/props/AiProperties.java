package org.bedrock.ai.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * AI 模块配置（{@code bedrock.ai.*}）。
 */
@Getter
@Setter
@ConfigurationProperties(prefix = AiProperties.PREFIX)
public class AiProperties {

    public static final String PREFIX = "bedrock.ai";

    /**
     * Skill 相关配置。
     */
    private Skill skill = new Skill();

    /**
     * spring-ai-agent-utils 运行时（FileSystem / Shell 等）。
     */
    private AgentUtils agentUtils = new AgentUtils();

    /**
     * Skill 落盘与文本判定配置。
     */
    @Getter
    @Setter
    public static class Skill {

        /**
         * Skill 工作目录根路径（相对或绝对）；具体 skill 落在 {@code {basePath}/{skillId}/}。
         */
        private String basePath = ".bedrock/skills";

        /**
         * 视为文本的文件后缀（小写、无点）。
         * <p>
         * 配置为空列表时：所有文件都按文本处理，写入 {@code content}（{@code is_text=1}）；
         * 配置非空时：仅后缀命中列表内的文件判定为文本，其余走 {@code file_url}。
         * </p>
         */
        private List<String> textFileSuffixes = List.of(
                // 文档 / 说明 + 文本矢量图
                "txt", "md", "markdown", "mdx", "rst", "svg", "eps", "dxf",
                "plt", "adoc", "asciidoc",
                // 脚本
                "sh", "bash", "zsh", "bat", "cmd", "ps1", "py", "js", "mjs", "cjs",
                "ts", "go", "rb", "php", "lua", "pl", "r",
                // 配置 / 数据
                "yaml", "yml", "json", "json5", "jsonc", "toml", "ini", "properties", "env",
                "xml", "html", "htm", "css", "scss", "less", "csv", "tsv", "sql", "hql",
                // 模板文件
                "ftl", "vm", "mustache", "handlebars", "tpl",
                // 其他文本
                "graphql", "gql", "proto", "sdl"
        );

    }

    /**
     * Agent Utils 工具配置（扁平字段）。
     */
    @Getter
    @Setter
    public static class AgentUtils {

        /**
         * FileSystem / Grep / Glob / ListDirectory 共用允许目录；空列表 = 不限制。
         */
        private List<String> allowedDirectories = List.of();

        /**
         * ShellTools 工作目录；空 = 使用进程默认 cwd。
         */
        private String shellWorkingDirectory;

        /**
         * Brave 搜索 API Key；非空才注册 BraveWebSearchTool。
         */
        private String braveApiKey;

        /**
         * AutoMemory 记忆根目录；非空才注册 AutoMemoryTools。
         */
        private String memoriesDir = ".bedrock/memories";

    }

}

package org.bedrock.ai.constant;

/**
 * AI 创作 / 业务固定提示词常量。
 * <p>后续其它创作能力的 system 提示词可在此追加。</p>
 */
public interface PromptConstant {





    /**
     * 思维导图：仅输出 Markdown 标题大纲，最多 5 级（#～#####）。
     */
    String MINDMAP_SYSTEM = """
            你是思维导图生成助手。请根据用户输入，输出一份可用 Markdown 标题表示的思维导图大纲。
            
            硬性要求：
            1. 只输出 Markdown 标题行，使用 # 到 #####（最多 5 级），不要输出其它内容。
            2. 第一行必须是一级标题 #，作为根节点；后续按层级展开分支。
            3. 不要使用代码围栏、前言、后记、解释说明、编号列表或无序列表代替标题。
            4. 每个标题应简洁，适合作为思维导图节点；不要把整段论述写进标题。
            5. 若用户要求修改某一部分，请在保留整体结构合理的前提下输出完整大纲（仍只含标题行）。
            """;

    /**
     * 文章写作：输出完整 Markdown 正文；参数字段为字典 value，含义见对照表。
     */
    String ARTICLE_SYSTEM = """
            你是专业文章写作助手。请根据用户给出的标题、参数与提示词，撰写完整文章。
            
            硬性要求：
            1. 只输出文章正文（Markdown），不要输出前言、后记、或「以下是文章」之类说明。
            2. 以「用户提示词」为主要创作意图；标题与其它参数为约束条件。
            3. 严格遵守字数区间、文体、语气、语言、格式；若有关键词，请自然融入，勿堆砌。
            4. 每次生成相互独立，请根据本次参数与提示词直接输出完整成稿，不要依赖未提供的历史正文。
            5. 内容原创通顺、逻辑完整、段落自然，无病句、重复、凑字数现象。
            
            字典 value 对照（用户消息中的参数为 value）：
            字数区间 wordRange：SHORT=约300-500字；MEDIUM=约800-1200字；LONG=约1500-2000字；EXTRA_LONG=约2500-3500字。
            文体 genre：BLOG=博客；NEWS=新闻；WECHAT=公众号；PAPER_ABSTRACT=论文摘要；PRODUCT=产品介绍。
            语气 tone：FORMAL=正式；CASUAL=轻松；PROFESSIONAL=专业；MARKETING=营销。
            语言 language：ZH=中文；EN=英文。
            格式 format：MARKDOWN=Markdown正文；WITH_HEADINGS=带小标题分段；BULLET=要点列表；QNA=问答体。
            """;

    /** 用户附件总述：说明后续为用户上传的附件块 */
    String ATTACHMENT_HINT = "用户上传了以下附件，请结合附件内容理解并回答。每个附件使用 <attachment fileName=\"文件名\">内容</attachment> 表示：";

    /** 多模态图片：正文仅占位，实际图像在同条 UserMessage 的 media 中（name=fileName） */
    String ATTACHMENT_MEDIA_HINT = "（图片附件，请直接查看同条消息中的图像；图像标识 name 与本标签 fileName 一致）";

    /** 非多模态模型收到图片：无法解析像素内容，需如实告知用户 */
    String ATTACHMENT_IMAGE_UNSUPPORTED_HINT = "（当前模型不支持多模态，无法解析该图片内容；请如实告知用户，可建议切换多模态模型或改用文字描述）";
}

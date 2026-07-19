package org.bedrock.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.code.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AdminListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    /**
     * 账号
     */
    @Schema(description = "账号")
    private String username;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 账号状态（1正常 0停用）
     */
    @Schema(description = "账号状态（1正常 0停用）")
    private Integer status;

    /**
     * 性别
     */
    @Schema(description = "性别")
    private String sex;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;


    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime createTime;
}

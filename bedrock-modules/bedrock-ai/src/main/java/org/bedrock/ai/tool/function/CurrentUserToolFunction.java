package org.bedrock.ai.tool.function;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.bedrock.ai.constant.AiConstant;
import org.bedrock.common.ai.annotation.ToolProvider;
import org.bedrock.common.auth.entity.AuthUser;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;
@ToolProvider(kind = ToolProvider.Kind.FUNCTION,label = "获取当前用户")
@Component("get_current_user")
@Description("Get current user information")
public class CurrentUserToolFunction implements BiFunction<CurrentUserToolFunction.CurrentUserToolFunctionInput, ToolContext, CurrentUserToolFunction.CurrentUserToolFunctionOutput> {

    @JsonClassDescription("不需要参数")
    public record CurrentUserToolFunctionInput() {

    }

    @JsonClassDescription("用户信息")
    public record CurrentUserToolFunctionOutput(@JsonPropertyDescription("用户ID") Long userId,
                                                @JsonPropertyDescription("用户名") String username) {

    }

    @Override
    public CurrentUserToolFunctionOutput apply(CurrentUserToolFunctionInput currentUserToolFunctionInput, ToolContext toolContext) {
        if (toolContext.getContext().get(AiConstant.CTX_USER_INFO) instanceof AuthUser authUser) {
            return new CurrentUserToolFunctionOutput(authUser.getUserId(), authUser.getUsername());
        }
        return new CurrentUserToolFunctionOutput(-1L, "anonymous");
    }

}

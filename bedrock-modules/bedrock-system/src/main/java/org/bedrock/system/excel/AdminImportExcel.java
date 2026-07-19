package org.bedrock.system.excel;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.*;
import lombok.Getter;
import lombok.Setter;
import org.bedrock.common.excel.annotation.ExcelContentDown;
import org.bedrock.common.excel.annotation.ExcelConverter;
import org.bedrock.common.excel.annotation.ExcelHeaderComment;
import org.bedrock.common.excel.annotation.ExcelHeaderDesc;

@Getter
@Setter
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
@ExcelHeaderDesc(value = """
        填写须知：
        （1）Excel中<WriteFont color=10 fontName=宋体 fontHeightInPoints=10>红色字段</WriteFont>为必填字段，黑色字段为选填字段。
        （2）昵称：最长字符不能超过50个字；只能包含中文、字母、数字、下划线、小括号和空格(不能全是空格),例如：张 三(a_1)
        （3）手机号：请输入正确的11位手机；
        （4）邮箱：请输入正确的邮箱账号；
        """, bold = false, rowHeight = 100, fontHeightInPoints = 10)
public class AdminImportExcel {

    /**
     * 账号
     */
    @ExcelProperty(value = "账号")
    @HeadFontStyle(color = 10)
    @ExcelHeaderComment(value = "登录平台账号")
    private String username;

    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称")
    @ExcelHeaderComment(value = "请填写正确的用户名")
    private String nickname;

    /**
     * 性别
     */
    @ExcelProperty(value = "性别")
    @ExcelHeaderComment(value = "请选择正确的性别")
    @ExcelConverter(
            javaToExcel = "T(org.bedrock.system.cache.DictCache).getLabel(T(org.bedrock.system.enums.DictEnum).SEX, #value)",
            excelToJava = "T(org.bedrock.system.cache.DictCache).getValue(T(org.bedrock.system.enums.DictEnum).SEX, #value)"
    )
    @ExcelContentDown(value = """
            T(org.bedrock.system.cache.DictCache).dictLabels(T(org.bedrock.system.enums.DictEnum).SEX)
            """, dropdownStartRow = 2)
    private String sex;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    private String phone;

    /**
     * 部门编号
     */
    @ExcelProperty(value = "部门编号")
    private String deptCode;

    /**
     * 角色别名
     */
    @ExcelProperty(value = "角色别名")
    private String roleAlias;

    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱")
    private String email;

    /**
     * 账号描述
     */
    @ExcelProperty(value = "账号描述")
    private String remark;
}

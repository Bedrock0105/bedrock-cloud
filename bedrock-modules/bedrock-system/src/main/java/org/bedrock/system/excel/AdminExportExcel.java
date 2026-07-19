package org.bedrock.system.excel;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import cn.idev.excel.annotation.write.style.ContentRowHeight;
import cn.idev.excel.annotation.write.style.HeadRowHeight;
import lombok.Getter;
import lombok.Setter;
import org.bedrock.common.excel.annotation.ExcelConverter;

@Getter
@Setter
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class AdminExportExcel {

    /**
     * 账号
     */
    @ExcelProperty(value = "账号")
    private String username;

    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称")
    private String nickname;

    /**
     * 性别
     */
    @ExcelProperty(value = "性别")
    @ExcelConverter(
            javaToExcel = "T(org.bedrock.system.cache.DictCache).getLabel(T(org.bedrock.system.enums.DictEnum).SEX, #value)"
    )
    private String sex;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    private String phone;

    /**
     * 手机号
     */
    @ExcelProperty(value = "状态")
    @ExcelConverter(
            javaToExcel = "T(org.bedrock.system.cache.DictCache).getLabel(T(org.bedrock.system.enums.DictEnum).STATUS, #value)"
    )
    private String status;

    /**
     * 部门
     */
    @ExcelProperty(value = "部门")
    private String deptName;

    /**
     * 角色
     */
    @ExcelProperty(value = "角色")
    private String roleName;

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

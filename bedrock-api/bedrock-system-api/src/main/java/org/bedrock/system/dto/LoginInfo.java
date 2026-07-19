package org.bedrock.system.dto;

import lombok.Getter;
import lombok.Setter;
import org.bedrock.system.entity.Admin;
import org.bedrock.system.entity.Dept;
import org.bedrock.system.entity.Role;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 登录信息
 */
@Getter
@Setter
public class LoginInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Admin admin;

    private Map<String, Object> params;

    private List<Dept> adminDept;

    private List<Role> adminRole;

    public LoginInfo() {
    }

    public LoginInfo(Admin admin, Map<String, Object> params, List<Dept> adminDept, List<Role> adminRole) {
        this.admin = admin;
        this.params = params;
        this.adminDept = adminDept;
        this.adminRole = adminRole;
    }
}

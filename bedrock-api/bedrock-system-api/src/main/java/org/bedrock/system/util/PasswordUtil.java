package org.bedrock.system.util;

import org.bedrock.common.code.util.DigestUtil;

public abstract class PasswordUtil {

    /**
     * 密码加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    public static String md5Password(String password) {
        return DigestUtil.md5Hex(password);
    }

    /**
     * 密码加密
     *
     * @param password 密码，要md5加密过一次
     */
    public static String encryptionPassword(String password) {
        return DigestUtil.md5Hex(DigestUtil.sha512Hex(DigestUtil.sha1(password)) + password);
    }

}

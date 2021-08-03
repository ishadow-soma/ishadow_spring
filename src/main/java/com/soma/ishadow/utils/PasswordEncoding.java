package com.soma.ishadow.utils;

import static org.apache.commons.codec.digest.DigestUtils.sha512;
import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64String;

public class PasswordEncoding {

    public static String passwordEncoding(String credentials) {
        return encodeBase64String(sha512(credentials));
    }
}

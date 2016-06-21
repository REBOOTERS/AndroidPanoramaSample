package com.baidu.lbsapi.panodemo.indoor.util;

import java.io.UnsupportedEncodingException;

/**
 * 基础工具类
 */
public class SysUtil {

    public static boolean isChinese(char c) throws UnsupportedEncodingException {
        return String.valueOf(c).getBytes("UTF-8").length == 3;
    }
}

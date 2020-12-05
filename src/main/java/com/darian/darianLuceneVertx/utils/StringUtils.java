package com.darian.darianLuceneVertx.utils;

import java.util.Objects;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/11/14  23:40
 */
public final class StringUtils {

    public static boolean isEmpty(String str) {
        return (str == null || "".equals(str));
    }

    public static boolean hasText(String str) {
        return (Objects.nonNull(str) && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}

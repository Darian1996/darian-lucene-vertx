package com.darian.darianLuceneVertx.utils.servlet;

import com.darian.darianLuceneVertx.utils.StringUtils;
import io.vertx.core.http.HttpServerRequest;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2020/11/14  22:35
 */
public class RequestParamUtils {

    public static String getString(HttpServerRequest httpServerRequest, String name) {
        return httpServerRequest.getParam(name);
    }

    public static Boolean getBoolean(HttpServerRequest httpServerRequest, String name) {
        String param = httpServerRequest.getParam(name);
        if (StringUtils.hasText(param)) {
            return Boolean.valueOf(param);
        }
        return null;
    }

    public static Boolean getBoolean(HttpServerRequest httpServerRequest, String name, Boolean defaultBoolean) {
        String param = httpServerRequest.getParam(name);
        if (StringUtils.hasText(param)) {
            return Boolean.valueOf(param);
        }
        return defaultBoolean;
    }
}

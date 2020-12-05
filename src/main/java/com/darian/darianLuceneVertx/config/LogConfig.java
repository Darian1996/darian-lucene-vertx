package com.darian.darianLuceneVertx.config;

import com.darian.darianLuceneVertx.utils.AssertUtils;
import com.darian.darianLuceneVertx.utils.properties.SystemAndConf;
import io.vertx.core.AbstractVerticle;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/11/15  16:33
 */
public final class LogConfig {

    public static Boolean logRequestParams = false;

    public static long checkTimeOutMS = 1000;

    public static void init(AbstractVerticle abstractVerticle) {
        String pre = "darian.log.config.";
        logRequestParams = SystemAndConf.getBoolean(abstractVerticle, pre + "logRequestParams");
        checkTimeOutMS = SystemAndConf.getLong(abstractVerticle,pre + "checkTimeOutMS");
    }


    public static void check() {
        AssertUtils.assertNotNull(logRequestParams, "darian.log.config.logRequestParams must not blank");
    }
}

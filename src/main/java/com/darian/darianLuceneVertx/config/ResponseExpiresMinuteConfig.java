package com.darian.darianLuceneVertx.config;

import com.darian.darianLuceneVertx.utils.properties.SystemAndConf;
import io.vertx.core.AbstractVerticle;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/11/15  18:20
 */
public class ResponseExpiresMinuteConfig {

    public static int searchExpires = 1;

    public static int getImgExpires = 3;

    public static int getCustomerFileExpires = 1;

    public static void init(AbstractVerticle abstractVerticle) {
        String pre = "darian.response.config.";
        searchExpires = SystemAndConf.getInteger(abstractVerticle, pre + "searchExpires");
        getImgExpires = SystemAndConf.getInteger(abstractVerticle, pre + "getImgExpires");
        getCustomerFileExpires = SystemAndConf.getInteger(abstractVerticle, pre + "getCustomerFileExpires");
    }

}

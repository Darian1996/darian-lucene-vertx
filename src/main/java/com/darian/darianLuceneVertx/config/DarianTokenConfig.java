package com.darian.darianLuceneVertx.config;

import com.darian.darianLuceneVertx.utils.AssertUtils;
import com.darian.darianLuceneVertx.utils.properties.SystemAndConf;
import io.vertx.core.AbstractVerticle;
import lombok.Data;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/14  23:48
 */
@Data
public class DarianTokenConfig  {

    public static void init(AbstractVerticle abstractVerticle) {
        String pre = "darian.token.config.";
        token = SystemAndConf.getString(abstractVerticle, pre + "token");

    }

    public static String token;

    public static void check()  {
        AssertUtils.assertNotBlank(token, "darian.token.config.token must not blank");

    }

    @Override
    public String toString() {
        return "\n" + "[DarianTokenConfig]" + "\n" +
                "{" + "\n" +
                "\t" + "token=\"" + token + "\"\n" +
                '}';
    }
}

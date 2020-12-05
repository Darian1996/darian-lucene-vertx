package com.darian.darianLuceneVertx.config;

import com.darian.darianLuceneVertx.utils.AssertUtils;
import com.darian.darianLuceneVertx.utils.ShellUtils;
import com.darian.darianLuceneVertx.utils.properties.SystemAndConf;
import io.vertx.core.AbstractVerticle;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/11/15  18:26
 */
public class DarianIpPortConfig {

    public static String whitePort;

    /**
     * 校验真实 IP
     */
    public static boolean CHECK_X_REAL_IP;


    public static void init(AbstractVerticle abstractVerticle) {
        String pre = "darian.ipPort.config.";
        whitePort = SystemAndConf.getString(abstractVerticle, pre + "whitePort");
        CHECK_X_REAL_IP= SystemAndConf.getBoolean(abstractVerticle, pre + "checkXRealIP");

        ShellUtils.PORT_S = DarianIpPortConfig.getPortList();
    }

    public static List<Integer> getPortList() {
        AssertUtils.assertNotBlank(whitePort, "whitePort is blank");
        return Stream.of(whitePort.split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());

    }
}

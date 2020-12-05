package com.darian.darianLuceneVertx.utils;

import com.darian.darianLuceneVertx.utils.properties.SystemAndConf;
import io.vertx.core.AbstractVerticle;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2020/11/14  20:31
 */
public class PropertiesUtils {

  public static final String HTTP_PORT_KEY = "http.port";

  public static Integer getPort(AbstractVerticle abstractVerticle) {
    return SystemAndConf.getInteger(abstractVerticle, HTTP_PORT_KEY);
  }

}

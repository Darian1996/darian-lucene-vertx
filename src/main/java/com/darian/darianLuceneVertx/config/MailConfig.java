package com.darian.darianLuceneVertx.config;

import com.darian.darianLuceneVertx.utils.MailUtils;
import com.darian.darianLuceneVertx.utils.properties.SystemAndConf;
import io.vertx.core.AbstractVerticle;

import java.util.Properties;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/11/15  18:33
 */
public class MailConfig {

    private static String to;
    private static String username;
    private static String token;
    private static String host;
    private static String transport;
    private static String smtp_auth;
    private static String smtp_port;
    private static String smtp_ssl_enable;
    private static String debug;

    public static void init(AbstractVerticle abstractVerticle) {
        String pre = "mail.";
        to = SystemAndConf.getString(abstractVerticle, pre + "to");
        username = SystemAndConf.getString(abstractVerticle, pre + "username");
        token = SystemAndConf.getString(abstractVerticle, pre + "token");
        host = SystemAndConf.getString(abstractVerticle, pre + "host");
        transport = SystemAndConf.getString(abstractVerticle, pre + "transport");
        smtp_auth = SystemAndConf.getString(abstractVerticle, pre + "smtp_auth");
        smtp_port = SystemAndConf.getString(abstractVerticle, pre + "smtp_port");
        smtp_ssl_enable = SystemAndConf.getString(abstractVerticle, pre + "smtp_ssl_enable");
        debug = SystemAndConf.getString(abstractVerticle, pre + "debug");


        Properties properties = new Properties();
        properties.setProperty("mail.to", to);
        properties.setProperty("mail.username", username);
        properties.setProperty("mail.token", token);
        properties.setProperty("mail.host", host);
        properties.setProperty("mail.transport", transport);
        properties.setProperty("mail.smtp.auth", smtp_auth);
        properties.setProperty("mail.smtp.port", smtp_port);
        properties.setProperty("mail.smtp.ssl.enable", smtp_ssl_enable);
        properties.setProperty("mail.debug", debug);

        MailUtils.properties = properties;
    }
}

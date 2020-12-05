package com.darian.darianLuceneVertx.application;

import com.darian.darianLuceneVertx.DarianLuceneVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2020/11/14  22:41
 */
public abstract class AbstraceMainApplication {


    protected static void run(String configName) {
        DeploymentOptions deploymentOptions = new DeploymentOptions();

        InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream(configName);
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(systemResourceAsStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String env_String = new BufferedReader(inputStreamReader)
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));

        deploymentOptions.setConfig(new JsonObject(env_String));

        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setWarningExceptionTime(TimeUnit.SECONDS.toNanos(60));  //block时间超过此值，打印代码堆栈
        vertxOptions.setBlockedThreadCheckInterval(TimeUnit.SECONDS.toMillis(2)); // 每隔 x，检查下是否block
        vertxOptions.setMaxEventLoopExecuteTime(TimeUnit.SECONDS.toNanos(1)); //允许 eventloop block 的最长时间
        Vertx vertx = Vertx.vertx(vertxOptions);
        vertx.deployVerticle(new DarianLuceneVerticle(), deploymentOptions);
    }
}

package com.darian.darianLuceneVertx;

import com.darian.darianLuceneVertx.config.DarianGitRepConfig;
import com.darian.darianLuceneVertx.config.DarianIpPortConfig;
import com.darian.darianLuceneVertx.config.DarianLuceneConfig;
import com.darian.darianLuceneVertx.config.DarianTokenConfig;
import com.darian.darianLuceneVertx.config.LogConfig;
import com.darian.darianLuceneVertx.config.MailConfig;
import com.darian.darianLuceneVertx.config.ResponseExpiresMinuteConfig;
import com.darian.darianLuceneVertx.contants.DocumentContants;
import com.darian.darianLuceneVertx.route.RouteBuilder;
import com.darian.darianLuceneVertx.service.MdFileListToDocService;
import com.darian.darianLuceneVertx.utils.AssertUtils;
import com.darian.darianLuceneVertx.utils.LimitUtils;
import com.darian.darianLuceneVertx.utils.LocalDateTimeUtils;
import com.darian.darianLuceneVertx.utils.MailUtils;
import com.darian.darianLuceneVertx.utils.PropertiesUtils;
import com.darian.darianLuceneVertx.verticle.CustomerFileVerticle;
import com.darian.darianLuceneVertx.verticle.LuceneVerticle;
import com.darian.darianLuceneVertx.verticle.TimeVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class DarianLuceneVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(DarianLuceneVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        long start = System.currentTimeMillis();

        before();

        int customerWorkPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        LOGGER.info("[customerWorkPoolSize][" + customerWorkPoolSize + "]");


        vertx.deployVerticle(
                LuceneVerticle.class.getName(),
                new DeploymentOptions()
                        .setWorker(true)
                        .setMultiThreaded(true)
                        .setWorkerPoolSize(customerWorkPoolSize)
                        .setConfig(config())
                        .setWorkerPoolName("LUCENE_VERTICLE_WORK")
                        .setMaxWorkerExecuteTime(TimeUnit.SECONDS.toNanos(1))
                        .setMaxWorkerExecuteTimeUnit(TimeUnit.NANOSECONDS)
        );

        vertx.deployVerticle(
                CustomerFileVerticle.class.getName(),
                new DeploymentOptions()
                        .setWorker(true)
                        .setMultiThreaded(true)
                        .setWorkerPoolSize(customerWorkPoolSize)
                        .setConfig(config())
                        .setWorkerPoolName("CUSTOMER_FILE_VERTICLE_WORK")
                        .setMaxWorkerExecuteTime(TimeUnit.SECONDS.toNanos(1))
                        .setMaxWorkerExecuteTimeUnit(TimeUnit.NANOSECONDS)

        );

        vertx.deployVerticle(
                TimeVerticle.class.getName(),
                new DeploymentOptions()
                        .setWorker(true)
                        .setWorkerPoolSize(1)
                        .setConfig(config())
                        .setWorkerPoolName("TIMER_VERTICLE_WORK")
        );


        Integer httpPort = PropertiesUtils.getPort(this);

        Router router = RouteBuilder.createRouter(this.vertx);


        this.vertx.createHttpServer()
                .requestHandler(router)
                .listen(httpPort, http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        String httpUrl = "http://localhost:" + httpPort;
                        LOGGER.info("HTTP server started ");
                        LOGGER.info(httpUrl);
                    } else {
                        startPromise.fail(http.cause());
                    }
                });

        vertx.eventBus().<String>request(
                LuceneVerticle.LUCENE_VERTICLE_CREATE_INDEX_SEARCH,
                "start",
                msg -> {
                    if (msg.succeeded()) {
                        String resultString = msg.result().body();
                        LOGGER.info(resultString);
                    } else {
                        AssertUtils.assertTrue(false, msg.cause().getMessage());
                    }
                });

        after();

        String nowString = LocalDateTimeUtils.getNowString();

        DocumentContants.APPLICATION_START_TIME = nowString;

        String application_start_string = "系统的启动时间....{ " + nowString + " }";

        long end = System.currentTimeMillis();
        long cost_s = (end - start) / 1000;
        long cost_ms = (end - start) % 1000;

        String application_start_cost = "系统启动成功总耗时：" + cost_s + " s " + cost_ms + " ms";
        LOGGER.info(application_start_string);
        LOGGER.info(application_start_cost);

        MailUtils.sendMail("lucene-vertx-启动", application_start_string + "<br>" + application_start_cost);

    }

    private void after() {

    }

    private void before() {
        LimitUtils.init(this);

        LogConfig.init(this);
        LogConfig.check();

        ResponseExpiresMinuteConfig.init(this);

        DarianIpPortConfig.init(this);

        MailConfig.init(this);

        DarianLuceneConfig.init(this);
        DarianLuceneConfig.check();


        DarianGitRepConfig.init(this);
        DarianGitRepConfig.check();

        DarianTokenConfig.init(this);
        DarianTokenConfig.check();

        MdFileListToDocService.generoterMdToDoc();
    }

}

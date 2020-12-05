package com.darian.darianLuceneVertx.verticle;

import com.darian.darianLuceneVertx.contants.LoggerContants;
import com.darian.darianLuceneVertx.service.LuceneService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LuceneVerticle extends AbstractVerticle {

    public static String SEARCH_PARAM_KEY = "param";

    public static String LUCENE_VERTICLE_SEARCH_ADDRESS = "LUCENE_VERTICLE_SEARCH_ADDRESS";

    public static String LUCENE_VERTICLE_CREATE_INDEX_SEARCH = "LUCENE_VERTICLE_CREATE_INDEX_SEARCH";

    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        EventBus eventBus = vertx.eventBus();

        // 消费 搜索消息
        eventBus.<JsonObject>consumer(LUCENE_VERTICLE_SEARCH_ADDRESS, msg -> {
            long start = System.currentTimeMillis();
            JsonObject body = msg.body();
            String param = body.getString(SEARCH_PARAM_KEY);

            MDC.put(LoggerContants.TRANCE_ID_KEY, body.getString(LoggerContants.TRANCE_ID_KEY));

            // 特殊字符不能在 lucene 中查询
            param = param.replaceAll("-", "");

            String resultString = JsonObject.mapFrom(LuceneService.doSearch(param, false)).toString();

            long end = System.currentTimeMillis();
            long cost = end - start;
            LOGGER.info(String.format("[LuceneVerticle][search][%s][cost][%sms]", param, cost));
            msg.reply(resultString);
        });

        // 消费构建索引消息
        eventBus.<String>consumer(
                LUCENE_VERTICLE_CREATE_INDEX_SEARCH,
                msg -> {
                    long start = System.currentTimeMillis();
                    LuceneService.createIndex();

                    long end = System.currentTimeMillis();
                    long cost = end - start;
                    String resultString = String.format("[createIndex success][cost][%sms]", cost);
                    LOGGER.info(resultString);
                    msg.reply(resultString);
                });

    }
}
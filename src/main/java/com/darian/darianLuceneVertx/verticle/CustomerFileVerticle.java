package com.darian.darianLuceneVertx.verticle;

import com.darian.darianLuceneVertx.contants.LoggerContants;
import com.darian.darianLuceneVertx.domain.CustomerFile;
import com.darian.darianLuceneVertx.domain.ResponseT;
import com.darian.darianLuceneVertx.route.handler.GetCustomerFileHandler;
import com.darian.darianLuceneVertx.service.CustomerFileService;
import com.darian.darianLuceneVertx.service.LuceneService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class CustomerFileVerticle extends AbstractVerticle {

    public static String GET_CUSTOMER_FILE_ADDRESS = "GET_CUSTOMER_FILE_ADDRESS";

    public static String filePathSubDocsFilePath_key = "filePathSubDocsFilePath";

    private static Logger LOGGER = LoggerFactory.getLogger(CustomerFileVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        EventBus eventBus = this.vertx.eventBus();

        // 消费查询文件
        eventBus.<JsonObject>consumer(
                GET_CUSTOMER_FILE_ADDRESS,
                msg -> {
                    long start = System.currentTimeMillis();
                    JsonObject body = msg.body();
                    String filePathSubDocsFilePath_value = body.getString(filePathSubDocsFilePath_key);

                    MDC.put(LoggerContants.TRANCE_ID_KEY, body.getString(LoggerContants.TRANCE_ID_KEY));

                    CustomerFile customerFile = CustomerFileService.getCustomerFile(filePathSubDocsFilePath_value);

                    String resultString = JsonObject.mapFrom(ResponseT.ok(customerFile)).toString();

                    long end = System.currentTimeMillis();
                    long cost = end - start;
                    LOGGER.info(String.format("[CustomerFileVerticle][getCustomerFile][%s][cost][%sms]",
                            filePathSubDocsFilePath_value, cost));
                    msg.reply(resultString);
                });
    }
}

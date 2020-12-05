package com.darian.darianLuceneVertx.route.handler;

import com.darian.darianLuceneVertx.contants.DocumentContants;
import com.darian.darianLuceneVertx.contants.LoggerContants;
import com.darian.darianLuceneVertx.domain.CustomerFile;
import com.darian.darianLuceneVertx.exception.CustomerRuntimeException;
import com.darian.darianLuceneVertx.route.RouteBuilder;
import com.darian.darianLuceneVertx.service.CustomerFileService;
import com.darian.darianLuceneVertx.utils.AssertUtils;
import com.darian.darianLuceneVertx.utils.servlet.RequestParamUtils;
import com.darian.darianLuceneVertx.verticle.CustomerFileVerticle;
import com.darian.darianLuceneVertx.verticle.LuceneVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class GetCustomerFileHandler extends AbstractBaseHandler {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static String FILE_PATH_SUB_DOCS_FILE_PATH_KEY = "filePathSubDocsFilePath";

    @Override
    protected void doHandle(RoutingContext context) {
        EventBus eventBus = context.vertx().eventBus();

        HttpServerRequest request = context.request();
        String filePathSubDocsFilePath = RequestParamUtils.getString(request, FILE_PATH_SUB_DOCS_FILE_PATH_KEY);

        AssertUtils.assertNotBlank(filePathSubDocsFilePath, "filePathSubDocsFilePath must not blank");
        try {
            filePathSubDocsFilePath = URLDecoder.decode(filePathSubDocsFilePath, DocumentContants.UTF_8);

            LOGGER.debug("[TestRestController.getContent].filePathSubGitFilePath.decode:" + filePathSubDocsFilePath);
        } catch (UnsupportedEncodingException e) {
            throw new CustomerRuntimeException("查询文件，解码报错");
        }


        JsonObject msg_param = new JsonObject()
                .put(CustomerFileVerticle.filePathSubDocsFilePath_key, filePathSubDocsFilePath)
                .put(LoggerContants.TRANCE_ID_KEY, MDC.get(LoggerContants.TRANCE_ID_KEY));

        eventBus.<String>request(
                CustomerFileVerticle.GET_CUSTOMER_FILE_ADDRESS,
                msg_param,
                msg -> {
                    if (msg.succeeded()) {
                        String responseTString = msg.result().body();
                        endWithResponseTString(context, responseTString);
                    } else {
                        AssertUtils.assertTrue(false, msg.cause().getMessage());
                    }
                });
    }

    /**
     * 缓存 1 分钟
     *
     * @return
     */
    @Override
    protected int getMinuteExpires() {
        return 1;
    }
}

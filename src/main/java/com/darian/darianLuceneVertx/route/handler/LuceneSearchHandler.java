package com.darian.darianLuceneVertx.route.handler;

import com.darian.darianLuceneVertx.contants.LoggerContants;
import com.darian.darianLuceneVertx.utils.AssertUtils;
import com.darian.darianLuceneVertx.utils.servlet.RequestParamUtils;
import com.darian.darianLuceneVertx.verticle.LuceneVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.MDC;

public class LuceneSearchHandler extends AbstractBaseHandler {

    private static String SEARCH_PARAM_KEY = "parm";

    private final EventBus eventBus;

    public LuceneSearchHandler(EventBus eventBus) {
        this.eventBus = eventBus;
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

    @Override
    protected void doHandle(RoutingContext context) {
        HttpServerRequest request = context.request();
        String param = RequestParamUtils.getString(request, SEARCH_PARAM_KEY);

        AssertUtils.assertNotBlank(param, "param must be not blank");

        JsonObject msg_param = new JsonObject().put(LuceneVerticle.SEARCH_PARAM_KEY, param);
        msg_param.put(LoggerContants.TRANCE_ID_KEY, MDC.get(LoggerContants.TRANCE_ID_KEY));

        eventBus.<String>request(
                LuceneVerticle.LUCENE_VERTICLE_SEARCH_ADDRESS,
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
}

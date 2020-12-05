package com.darian.darianLuceneVertx.route.handler;

import com.darian.darianLuceneVertx.exception.AssertionException;
import com.darian.darianLuceneVertx.exception.CustomerRuntimeException;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerFailHandler extends AbstractBaseHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    protected boolean isCustomerFailHandler() {
        return true;
    }

    @Override
    protected void doHandle(RoutingContext context) {
        Throwable failure = context.failure();

        String localizedMessage = failure.getLocalizedMessage();

        if (failure instanceof AssertionException || failure instanceof CustomerRuntimeException) {
            localizedMessage = failure.getLocalizedMessage();

            String uri = context.request().uri();
            int i = uri.indexOf("?");
            if (i != -1) {
                uri = uri.substring(0, i);
            }
            LOGGER.warn(String.format("[uri][%s][msg][%s]", uri, localizedMessage));
        } else {
            LOGGER.error("错误：", failure);
        }
        endWithExceptionMsg(context, localizedMessage);
    }
}

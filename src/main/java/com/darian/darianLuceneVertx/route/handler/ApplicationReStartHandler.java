package com.darian.darianLuceneVertx.route.handler;

import com.darian.darianLuceneVertx.route.RouteBuilder;
import com.darian.darianLuceneVertx.service.ShellService;
import io.vertx.ext.web.RoutingContext;

public class ApplicationReStartHandler extends AbstractBaseHandler {

    @Override
    protected void doHandle(RoutingContext context) {
        endWithServiceResultT(context, ShellService.doRestart());
    }
}

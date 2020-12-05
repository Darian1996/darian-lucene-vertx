package com.darian.darianLuceneVertx.route.handler;

import com.darian.darianLuceneVertx.route.handler.ip.IPContainer;
import com.darian.darianLuceneVertx.utils.AssertUtils;
import com.darian.darianLuceneVertx.utils.servlet.RequestParamUtils;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

public class AddIllegalIPSetHandler extends AbstractBaseHandler {

    @Override
    protected void doHandle(RoutingContext context) {
        HttpServerRequest request = context.request();
        String illegalIP = RequestParamUtils.getString(request, "illegalIP");
        AssertUtils.assertNotBlank(illegalIP, "illegalIP must be not blank");
        IPContainer.ILLEGAL_IP_SET.add(illegalIP);
        endWithServiceResultT(context, "illegalIP add success");
    }
}

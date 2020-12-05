package com.darian.darianLuceneVertx.route.handler;

import com.darian.darianLuceneVertx.service.IPPortsWhiteService;
import com.darian.darianLuceneVertx.utils.AssertUtils;
import com.darian.darianLuceneVertx.utils.servlet.RequestParamUtils;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

public class RefreshWhiteIpListHandler extends AbstractBaseHandler {

    @Override
    protected void doHandle(RoutingContext context) {

        HttpServerRequest request = context.request();
        String whiteIP = RequestParamUtils.getString(request, "whiteIP");

        AssertUtils.assertNotBlank(whiteIP, "whiteIP不能为空");

        String refreshWhiteIpList = IPPortsWhiteService.refreshWhiteIpList(whiteIP);
        endWithServiceResultT(context, refreshWhiteIpList);
    }
}

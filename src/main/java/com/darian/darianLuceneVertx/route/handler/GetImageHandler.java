package com.darian.darianLuceneVertx.route.handler;

import com.darian.darianLuceneVertx.domain.CustomerFile;
import com.darian.darianLuceneVertx.route.RouteBuilder;
import com.darian.darianLuceneVertx.service.CustomerFileService;
import com.darian.darianLuceneVertx.utils.servlet.RequestParamUtils;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.io.File;

public class GetImageHandler extends AbstractBaseHandler {

    @Override
    protected String getContentTypeValue(RoutingContext context) {
        String absoluteURI = context.request().absoluteURI();
        if (absoluteURI.endsWith("svg")) {
            return imageSvgXmlValue;
        } else {
            return imagePngValue;
        }
    }

    @Override
    protected void doHandle(RoutingContext context) {
        HttpServerRequest request = context.request();
        String filePathSubDocsFilePath = RequestParamUtils.getString(request, "filePathSubDocsFilePath");
        CustomerFile customerFile = CustomerFileService.getCustomerFile(filePathSubDocsFilePath);
        String filePath = customerFile.getFileFullName();
        File imageFile = new File(filePath);
        if (imageFile.exists()) {
            HttpServerResponse response = context.response();
            response.sendFile(imageFile.getPath());
        } else {
            endWithExceptionMsg(context, "Spring boot：文件没有找到");
        }

    }

    @Override
    protected int getMinuteExpires() {
        return 3;
    }
}

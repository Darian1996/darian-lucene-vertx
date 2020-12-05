package com.darian.darianLuceneVertx.route;

import com.darian.darianLuceneVertx.route.handler.AddIllegalIPSetHandler;
import com.darian.darianLuceneVertx.route.handler.ApplicationReStartHandler;
import com.darian.darianLuceneVertx.route.handler.CustomerFailHandler;
import com.darian.darianLuceneVertx.route.handler.GetCustomerFileHandler;
import com.darian.darianLuceneVertx.route.handler.GetImageHandler;
import com.darian.darianLuceneVertx.route.handler.LuceneSearchHandler;
import com.darian.darianLuceneVertx.route.handler.RefreshWhiteIpListHandler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2020/11/14  22:54
 */
public final class RouteBuilder {


    private static final Logger LOGGER = LoggerFactory.getLogger(RouteBuilder.class);


    public static Router createRouter(Vertx vertx) {
        EventBus eventBus = vertx.eventBus();
        LuceneSearchHandler luceneSearchHandler = new LuceneSearchHandler(eventBus);
        GetCustomerFileHandler getCustomerFileHandler = new GetCustomerFileHandler();
        RefreshWhiteIpListHandler refreshWhiteIpListHandler = new RefreshWhiteIpListHandler();
        ApplicationReStartHandler applicationReStartHandler = new ApplicationReStartHandler();
        AddIllegalIPSetHandler addIllegalIPSetHandler = new AddIllegalIPSetHandler();
        GetImageHandler getImageHandler = new GetImageHandler();

        CustomerFailHandler customerFailHandler = new CustomerFailHandler();

        Router router = Router.router(vertx);

        router.get("/addIllegalIPSet")
                .handler(addIllegalIPSetHandler::handle)
                .failureHandler(customerFailHandler::handle);

        router.get("/refresh")
                .handler(applicationReStartHandler::handle)
                .failureHandler(customerFailHandler::handle);

        router.get("/getCustomerFile")
                .handler(getCustomerFileHandler::handle)
                .failureHandler(customerFailHandler::handle);

        router.get("/search")
                .handler(luceneSearchHandler::handle)
                .failureHandler(customerFailHandler::handle);

        router.get("/refreshWhiteIpList")
                .handler(refreshWhiteIpListHandler::handle)
                .failureHandler(customerFailHandler::handle);

        router.get("/getImage")
                .handler(getImageHandler::handle)
                .failureHandler(customerFailHandler::handle);

        // 配置静态文件
        StaticHandler staticHandler = StaticHandler.create();
        staticHandler.setWebRoot("webroot");
        staticHandler.setDefaultContentEncoding("UTF-8");
        router.route("/*").handler(staticHandler);

        return router;
    }


}








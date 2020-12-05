package com.darian.darianLuceneVertx.verticle;

import com.darian.darianLuceneVertx.time.TimerUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/11/17  21:26
 */
public class TimeVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        TimerUtils.timerTest(this.vertx);
    }


}

package com.darian.darianLuceneVertx.application;

import com.darian.darianLuceneVertx.utils.properties.SystemAndConf;
import io.vertx.core.Launcher;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import org.slf4j.MDC;

import java.util.concurrent.TimeUnit;

public class MyLauncher extends Launcher {

    public static void main(String[] args) {
        new MyLauncher().dispatch(args);
    }

    @Override
    public void beforeStartingVertx(VertxOptions vertxOptions) {
        vertxOptions.setWarningExceptionTime(TimeUnit.SECONDS.toNanos(60));  //block时间超过此值，打印代码堆栈
        vertxOptions.setBlockedThreadCheckInterval(TimeUnit.SECONDS.toMillis(1)); // 每隔 x，检查下是否block
        vertxOptions.setMaxEventLoopExecuteTime(TimeUnit.SECONDS.toNanos(1)); //允许 eventloop block 的最长时间
        vertxOptions.setMaxWorkerExecuteTime(TimeUnit.SECONDS.toNanos(1));
        EventBusOptions eventBusOptions = new EventBusOptions();
        eventBusOptions.setConnectTimeout((int) TimeUnit.MILLISECONDS.toMillis(1));
        vertxOptions.setEventBusOptions(eventBusOptions);
        super.beforeStartingVertx(vertxOptions);
    }

}

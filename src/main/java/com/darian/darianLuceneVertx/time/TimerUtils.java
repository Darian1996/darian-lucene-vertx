package com.darian.darianLuceneVertx.time;

import com.darian.darianLuceneVertx.utils.CpuUsageUtils;
import com.darian.darianLuceneVertx.utils.ShellUtils;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/11/15  3:45
 */
public class TimerUtils {

    private static final Logger TIMER_RUN_LOGGER = LoggerFactory.getLogger("TIMER_RUN");

    private static String DOCS_LAST_COMMIT_ID = ShellUtils.getDocsCommitId();

    private static String DARIAN_LUCENE_VERTX_LAST_COMMIT_ID = ShellUtils.getDarianLuceneVertxCommitId();

    /**
     * 间隔执行，执行多次
     */
    public static void timerTest(Vertx vertx) {

        // 每隔 60s 执行一次，回调函数中的参数timerId和返回值timerId2是一样的，可以通过这个值关闭这个定时器
        long timerId2 = vertx.setPeriodic(1000 * 60, timerId -> {
            TIMER_RUN_LOGGER.debug("当前定时器id是: " + timerId);

            String docsCommitId = ShellUtils.getDocsCommitId();
            String darianLuceneVertxCommitId = ShellUtils.getDarianLuceneVertxCommitId();
            if (!DOCS_LAST_COMMIT_ID.equals(docsCommitId) || !DARIAN_LUCENE_VERTX_LAST_COMMIT_ID.equals(darianLuceneVertxCommitId)) {
                TIMER_RUN_LOGGER.info("有新文件更新 ... ... ");
                ShellUtils.thisAppplicationReStartSh();
            }
        });

    }


    @Deprecated
    public static void cpuText(Vertx vertx) {
//        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutor();

        long timeSequence = 1000;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            // windows 执行需要 10s, 这里用 20 s
            timeSequence = 1000 * 20;
        }

        long finalTimeSequence = timeSequence;

        vertx.setPeriodic(finalTimeSequence, timerId -> {
            TIMER_RUN_LOGGER.debug("当前定时器id是: " + timerId);

            double cpuRatio = CpuUsageUtils.getCpuRatio();
            TIMER_RUN_LOGGER.info("[cpuRatio][" + cpuRatio + "]");
        });
    }

    public static ThreadPoolExecutor threadPoolExecutor() {
        // 设置队列容量
        BlockingQueue<Runnable> queue = createQueue(10000);

        // 设置核心线程数
        // 设置最大线程数
        // 设置线程活跃时间（秒）
        // 单位
        // 队列
        // 设置拒绝策略
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1,
                1,
                60,
                TimeUnit.SECONDS,
                queue,
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        return threadPoolExecutor;
    }

    protected static BlockingQueue<Runnable> createQueue(int queueCapacity) {
        if (queueCapacity > 0) {
            return new LinkedBlockingQueue<>(queueCapacity);
        } else {
            return new SynchronousQueue<>();
        }
    }
}
package com.darian.darianLuceneVertx.utils;

import com.darian.darianLuceneVertx.utils.properties.SystemAndConf;
import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/11/17  23:59
 */
public class LimitUtils {

    private static ConcurrentHashMap<String, Integer> IPMap = new ConcurrentHashMap<>();

    private static Long systemCurrentTimeMillis = System.currentTimeMillis();

    private static int oneSecondsLimitCount;

    private static final Logger VISITOR_IP_MONITOR_LOGGER = LoggerFactory.getLogger("VISITOR_IP_MONITOR");

    public static void init(AbstractVerticle abstractVerticle) {
        oneSecondsLimitCount = SystemAndConf.getInteger(abstractVerticle, "darian.request.oneSecondsLimitCount");
        getImageOneSecondLimitCount = SystemAndConf.getInteger(abstractVerticle, "darian.request.getImageOneSecondLimitCount");
    }

    /**
     * @param ip
     */
    public static void check(String ip, String requestPath) {
        AssertUtils.assertNotBlank(ip, "ip is not blank");

        if (requestPath.startsWith("/getImage")) {
            checkGetImageSecondVisitor(ip);
        } else {

            long currentTimeMillis = System.currentTimeMillis();
            // 如果 当前统计的时间间隔相差了 1 秒，充值访问次数统计 MAP 返回。
            if ((currentTimeMillis - systemCurrentTimeMillis) > 1000) {
                Integer visitorCount = IPMap.getOrDefault(ip, 0);
                systemCurrentTimeMillis = currentTimeMillis;
                VISITOR_IP_MONITOR_LOGGER.info(String.format("[iP/JESSIONID][%s][visitorCount][%s][重置IP{访问次数}{系统统计的时间}]", ip, visitorCount));
                IPMap.clear();
            } else {
                Integer visitorCount = IPMap.getOrDefault(ip, 0);
                visitorCount += 1;
                AssertUtils.assertTrue(visitorCount < oneSecondsLimitCount, "一秒内IP请求次数超过[" + visitorCount + "]次");
                IPMap.put(ip, visitorCount);
                VISITOR_IP_MONITOR_LOGGER.info(String.format("[iP/JESSIONID][%s][visitorCount][%s]", ip, visitorCount));
            }
        }

    }

    private static void checkGetImageSecondVisitor(String ip) {
        long currentTimeMillis = System.currentTimeMillis();
        if ((currentTimeMillis - getImageSystemCurrentTimeMillis) > 1000) {
            getImageSystemCurrentTimeMillis = currentTimeMillis;
            VISITOR_IP_MONITOR_LOGGER.info(String.format("[iP/JESSIONID][%s][visitorCount][%s][重置图片{访问次数}{系统统计的时间}]", ip, getImageActuatorCount));
            getImageActuatorCount = 0;
        } else {
            Integer thisGetImageActuatorCount = getImageActuatorCount + 1;
            AssertUtils.assertTrue(thisGetImageActuatorCount < getImageOneSecondLimitCount,
                    "一秒内[获取图片]请求次数超过[" + getImageOneSecondLimitCount + "]次");
            getImageActuatorCount = thisGetImageActuatorCount;
        }
    }

    private static Integer getImageActuatorCount = 0;

    /**
     * 最多一秒获取的就是 serach 出来 100 张图片然后去获取
     */
    private static Integer getImageOneSecondLimitCount;

    private static Long getImageSystemCurrentTimeMillis = System.currentTimeMillis();
}

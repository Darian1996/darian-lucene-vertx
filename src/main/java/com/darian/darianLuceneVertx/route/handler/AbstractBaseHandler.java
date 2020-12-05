package com.darian.darianLuceneVertx.route.handler;

import com.darian.darianLuceneVertx.config.DarianIpPortConfig;
import com.darian.darianLuceneVertx.config.DarianTokenConfig;
import com.darian.darianLuceneVertx.config.LogConfig;
import com.darian.darianLuceneVertx.contants.LoggerContants;
import com.darian.darianLuceneVertx.domain.ResponseT;
import com.darian.darianLuceneVertx.route.handler.ip.IPContainer;
import com.darian.darianLuceneVertx.utils.AssertUtils;
import com.darian.darianLuceneVertx.utils.LimitUtils;
import com.darian.darianLuceneVertx.utils.MailUtils;
import com.darian.darianLuceneVertx.utils.StringUtils;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Base路由类
 */
public abstract class AbstractBaseHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBaseHandler.class);

    private static final Logger VISITOR_IP_MONITOR_LOGGER = LoggerFactory.getLogger("VISITOR_IP_MONITOR");


    protected final String applicationJsonUTF8Value = "application/json; charset=UTF-8";

    protected final String imageSvgXmlValue = "image/svg+xml";

    protected final String imagePngValue = "image/png";

    @Override
    public void handle(RoutingContext context) {
        before(context);
        doHandle(context);
        after(context);
    }

    /**
     * 重写核心方法
     *
     * @param context
     */
    protected abstract void doHandle(RoutingContext context);

    /**
     * 路由以前
     *
     * @param context
     */
    protected void before(RoutingContext context) {
        // 设置开始时间/后边可以去get出来打印耗时
        setStartTime(context);
        checkTranceIdAndPutMDC(context);

        // fail Handler 不用 检查 IP_count/token/cookie (前边的已经检查过了)
        // 前边 加 1 ，后边再 + 1 ,会有逻辑错误
        if (!isCustomerFailHandler()) {
            checkIPCount(context);
            checkTokenOrCookie(context);
        }
        setContentType(context);
        setCorsHeaders(context);
        setExpires(context);
    }

    protected boolean isCustomerFailHandler() {
        return false;
    }

    private void checkIPCount(RoutingContext context) {
        if (DarianIpPortConfig.CHECK_X_REAL_IP) {
            String realIpString = context.request().getHeader("X-Real-IP");
            AssertUtils.assertNotBlank(realIpString, "不是nginx转发的请求");

            AssertUtils.assertFalse(IPContainer.ILLEGAL_IP_SET.contains(realIpString), "IP 非法");

            if (!IPContainer.VISITOR_IP_SET.contains(realIpString)) {
                IPContainer.VISITOR_IP_SET.add(realIpString);

                String addIPLogging = "新增IP[" + realIpString + "]请确认IP的合法性";
                String allIPLogging = "全部的访问IP:[" + IPContainer.VISITOR_IP_SET + "]";
                String illegalIPLogging = "黑名单IP:[" + IPContainer.ILLEGAL_IP_SET + "]";

                VISITOR_IP_MONITOR_LOGGER.info(addIPLogging);
                VISITOR_IP_MONITOR_LOGGER.info(allIPLogging);
                VISITOR_IP_MONITOR_LOGGER.info(illegalIPLogging);

                MailUtils.sendMail("新的IP访问", addIPLogging + "\n" + allIPLogging + "\n" + illegalIPLogging);
            }
            LimitUtils.check(realIpString, context.request().path());
        } else {
            // 不校验 IP 的时候校验 JSESSIONID
            io.vertx.core.http.Cookie jsessionid = context.request().getCookie("JSESSIONID");

            String jsessionidValue = null;
            if (Objects.nonNull(jsessionid)) {
                jsessionidValue = jsessionid.getValue();
            } else {
                jsessionidValue = "1.1.1.1:test";
            }
            AssertUtils.assertNotBlank(jsessionidValue, "请开启浏览器Cookie功能");
            LimitUtils.check(jsessionidValue, context.request().path());
        }
    }

    private void checkTokenOrCookie(RoutingContext context) {
        String header_token_Value = context.request().getHeader("token");

        String tokenValue;
        if (Objects.nonNull(header_token_Value)) {
            tokenValue = header_token_Value;

        } else {
            // header 匹配住了，就不需要判断 cookie 了。
            Cookie tokenCookie = context.getCookie("token");
            AssertUtils.assertTrue(Objects.nonNull(tokenCookie), "用户没有登陆");

            tokenValue = tokenCookie.getValue();
        }

        AssertUtils.assertTrue(Objects.equals(tokenValue, DarianTokenConfig.token), "密码错误");

    }

    /**
     * 检查  TranceId 并放到 MDC 里边
     *
     * @param context
     */
    private void checkTranceIdAndPutMDC(RoutingContext context) {
        String tranceId = context.request().getHeader(LoggerContants.TRANCE_ID_KEY);
        if (StringUtils.isEmpty(tranceId)) {
            tranceId = UUID.randomUUID().toString().replace("-", "");
        }

        MDC.put(LoggerContants.TRANCE_ID_KEY, tranceId);
    }

    protected final void endWithExceptionMsg(RoutingContext context, String errorMsg) {
        ResponseT responseT = ResponseT.error(errorMsg);
        endWithResponseTString(context, JsonObject.mapFrom(responseT).toString());
    }

    protected final <T> void endWithResponseT(RoutingContext context, ResponseT<T> responseT) {
        endWithResponseTString(context, JsonObject.mapFrom(responseT).toString());
    }

    protected final void endWithResponseTString(RoutingContext context, String responseTString) {
        HttpServerResponse response = context.response();
        response.end(responseTString);
    }

    protected final <T> void endWithServiceResultT(RoutingContext context, T serviceResult) {
        ResponseT<T> responseT = ResponseT.ok(serviceResult);
        endWithResponseT(context, responseT);
    }

    /**
     * 路由以后
     *
     * @param context
     */
    protected void after(RoutingContext context) {
        setEndTime(context);

        logCostTime(context);
    }

    private void setContentType(RoutingContext context) {
        setContentTypeVale(context, getContentTypeValue(context));
    }

    protected String getContentTypeValue(RoutingContext content) {
        return applicationJsonUTF8Value;
    }

    protected final void setContentTypeVale(RoutingContext context, String contentTypeValue) {
        HttpServerResponse response = context.response();
        response.putHeader("Content-Type", contentTypeValue);
    }

    private void setCorsHeaders(RoutingContext context) {
        HttpServerResponse response = context.response();
        response.putHeader("Access-Control-Allow-Origin", "*");
        response.putHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.putHeader("Access-Control-Max-Age", "3600");
        response.putHeader("Access-Control-Allow-Credentials", "true");
        response.putHeader("Access-Control-Allow-Headers", "x-requested-with,token");
    }

    /**
     * 设置开始时间
     *
     * @param context
     */
    private void setStartTime(RoutingContext context) {
        context.put(LoggerContants.METHOD_START_TIME_KEY, System.currentTimeMillis());
    }

    /**
     * 设置结束时间
     */
    private void setEndTime(RoutingContext context) {
        context.put(LoggerContants.METHOD_END_TIME_KEY, System.currentTimeMillis());
    }

    /**
     * 设置过期时间
     *
     * @param context
     */
    private final void setExpires(RoutingContext context) {
        int minuteExpiresCount = getMinuteExpires();
        if (getMinuteExpires() == 0) {
            return;
        } else {
            setMinuteExpires(context, minuteExpiresCount);
        }
    }

    private final void setMinuteExpires(RoutingContext context, int minute) {
        Calendar nowTime = Calendar.getInstance();
        // 1 min 缓存
        nowTime.add(Calendar.MINUTE, minute);

        Date date = nowTime.getTime();

        //DateFormat fmt = new SimpleDateFormat("EEE,d MMM yyyy hh:mm:ss z", Locale.SIMPLIFIED_CHINESE);
        //fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        //
        //String Expires_format = fmt.format(date);

        HttpServerResponse response = context.response();
        response.putHeader("Expires", date.toString());
    }

    protected int getMinuteExpires() {
        return 0;
    }

    private void logCostTime(RoutingContext routingContext) {

        String uri = routingContext.request().uri();
        int i = uri.indexOf("?");
        if (i != -1) {
            uri = uri.substring(0, i);
        }
        MultiMap queryParams = routingContext.queryParams();

        List<String> paramsString = new ArrayList<>();

        for (Map.Entry<String, String> queryParam : queryParams) {
            String key = queryParam.getKey();
            String value = queryParam.getValue();
            paramsString.add("{" + key + ":" + value + "}");
        }

        long end = System.currentTimeMillis();
        Object start_object = routingContext.get(LoggerContants.METHOD_START_TIME_KEY);
        long start = Objects.nonNull(start_object) ? (long) start_object : end;
        long cost = end - start;
        if (LogConfig.logRequestParams) {
            LOGGER.info(String.format("[uri][%s][param][%s][cost][%sms]", uri, paramsString, cost));
        } else {
            LOGGER.info(String.format("[uri][%s][cost][%sms]", uri, cost));
        }

        if (cost > LogConfig.checkTimeOutMS) {
            LOGGER.warn(String.format("[uri][%s][param][%s][cost][%sms]", uri, paramsString, cost));
        }

    }
}

package com.darian.darianLuceneVertx.domain;


import com.darian.darianLuceneVertx.contants.DocumentContants;
import com.darian.darianLuceneVertx.utils.LocalDateTimeUtils;
import lombok.Data;

import java.util.Date;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2020/4/13  2:57
 */
@Data
public class ResponseT<T> {
    private String code;
    private String notifyMsg;
    private T dataBody;

    /**
     * 返回值构建时间
     */
    private String time;

    /**
     * 系统启动时间
     */
    private String applicationStartTime;

    public static <T> ResponseT<T> ok(String notifyMsg, T dataBody) {
        ResponseT<T> responseT = new ResponseT<>();
        responseT.setCode("200");
        responseT.setNotifyMsg(notifyMsg);
        responseT.setDataBody(dataBody);
        responseT.setTime(LocalDateTimeUtils.getNowString());
        responseT.setApplicationStartTime(DocumentContants.APPLICATION_START_TIME);
        return responseT;
    }

    public static <T> ResponseT<T> ok(T dataBody) {
        return ok("success", dataBody);
    }

    public static <T> ResponseT<T> error(String errorMsg) {
        return error(errorMsg, null);
    }

    public static <T> ResponseT<T> error(String errorMsg, T dataBody) {
        ResponseT<T> responseT = new ResponseT<>();
        responseT.setCode("500");
        responseT.setNotifyMsg(errorMsg);
        responseT.setDataBody(dataBody);
        responseT.setTime(LocalDateTimeUtils.getNowString());
        responseT.setApplicationStartTime(DocumentContants.APPLICATION_START_TIME);
        return responseT;
    }
}

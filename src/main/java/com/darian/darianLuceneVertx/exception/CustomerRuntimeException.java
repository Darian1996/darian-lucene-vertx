package com.darian.darianLuceneVertx.exception;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/11/15  1:58
 */
public class CustomerRuntimeException extends RuntimeException {
    public CustomerRuntimeException(String msg) {
        super(msg);
    }

    public CustomerRuntimeException(String msg, Exception e) {
        super(msg, e);
    }

}

package com.darkblue.rpcimpl.protocol;

import java.io.Serializable;

/**
 * 远程结果回传协议
 */
public class Result implements Serializable {
    private Object result;
    private RuntimeException exception;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(RuntimeException exception) {
        this.exception = exception;
    }
}

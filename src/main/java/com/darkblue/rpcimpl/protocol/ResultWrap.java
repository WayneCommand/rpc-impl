package com.darkblue.rpcimpl.protocol;

import java.io.Serializable;
import java.util.Map;

/**
 * 远程结果回传协议Wrap
 */
public class ResultWrap implements Serializable{
    private Result result;
    private Map<Object,Object> attchment;

    public ResultWrap() {
    }

    public ResultWrap(Result result) {

        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Map<Object, Object> getAttchment() {
        return attchment;
    }

    public void setAttchment(Map<Object, Object> attchment) {
        this.attchment = attchment;
    }
}

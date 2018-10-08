package com.darkblue.rpcimpl.protocol;

import java.io.Serializable;
import java.util.Map;

/**
 * 远程调用协议Wrap
 */
public class MethodInvokeMetaWrap implements Serializable {
    private MethodInvokeMeta invokeMeta;
    private Map<Object,Object> attchment;

    private ResultWrap resultWrap;

    public ResultWrap getResultWrap() {
        return resultWrap;
    }

    public void setResultWrap(ResultWrap resultWrap) {
        this.resultWrap = resultWrap;
    }

    public MethodInvokeMetaWrap(MethodInvokeMeta invokeMeta) {
        this.invokeMeta = invokeMeta;
    }

    public MethodInvokeMetaWrap() {
    }

    public MethodInvokeMeta getInvokeMeta() {
        return invokeMeta;
    }

    public void setInvokeMeta(MethodInvokeMeta invokeMeta) {
        this.invokeMeta = invokeMeta;
    }

    public Map<Object, Object> getAttchment() {
        return attchment;
    }

    public void setAttchment(Map<Object, Object> attchment) {
        this.attchment = attchment;
    }
}

package com.darkblue.rpcimpl.commons;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 传输附加参数 存放在两端的线程空间里
 */
public class RpcContext {
    private static final ThreadLocal<RpcContext> RPC_CONTEXT_THREAD_LOCAL = new InheritableThreadLocal<RpcContext>();

    private Map<Object,Object> attributes = new ConcurrentHashMap<Object, Object>();

    private RpcContext(){}

    public static synchronized RpcContext getContext(){
        RpcContext rpcContext = RPC_CONTEXT_THREAD_LOCAL.get();

        if (rpcContext != null){

            return rpcContext;
        }

        RPC_CONTEXT_THREAD_LOCAL.set(new RpcContext());
        return RPC_CONTEXT_THREAD_LOCAL.get();
    }

    public void setAttributes(Map<Object, Object> attributes) {
        this.attributes = attributes;
    }

    public void setAttribute(Object key,Object value){
        attributes.put(key,value);
    }

    public Object getAttribute(Object key){
        return attributes.get(key);
    }

    public Map<Object, Object> getAttributes() {
        return attributes;
    }
}

package com.darkblue.rpcimpl.utils;

import io.netty.util.concurrent.EventExecutorGroup;

public class RpcUtils {

    private RpcUtils(){}

    public static void shutdownGracefully(EventExecutorGroup... eventExecutorGroups) {
        for (EventExecutorGroup eventExecutorGroup : eventExecutorGroups) {
            try {
                eventExecutorGroup.shutdownGracefully();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

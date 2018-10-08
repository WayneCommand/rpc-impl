package com.darkblue.rpcimpl.loadbalance.impl;

import com.darkblue.rpcimpl.commons.HostAndPort;
import com.darkblue.rpcimpl.loadbalance.LoadBalancer;
import com.darkblue.rpcimpl.protocol.MethodInvokeMetaWrap;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class RoundRobinLoadBalancer implements LoadBalancer {

    private AtomicInteger robin = new AtomicInteger();

    public HostAndPort select(List<HostAndPort> hostAndPorts, MethodInvokeMetaWrap methodInvokeMetaWrap) {

        int i = 0;
        try {
            i = robin.get() % hostAndPorts.size();
        } catch (Exception e) {
            throw new RuntimeException("not found host");
        }

        int round = robin.addAndGet(1);

        if (round < 0) robin.set(0);

        return hostAndPorts.get(i);
    }
}

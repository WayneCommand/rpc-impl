package com.darkblue.rpcimpl.loadbalance.impl;

import com.darkblue.rpcimpl.commons.HostAndPort;
import com.darkblue.rpcimpl.loadbalance.LoadBalancer;
import com.darkblue.rpcimpl.protocol.MethodInvokeMetaWrap;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class RoundRobinLoadBalancer implements LoadBalancer {

    private AtomicInteger round = new AtomicInteger();

    public HostAndPort select(List<HostAndPort> hostAndPorts, MethodInvokeMetaWrap methodInvokeMetaWrap) {

        if (hostAndPorts.isEmpty()) throw new RuntimeException("not found host");

        if (round.get() >= hostAndPorts.size()) round.set(0);

        HostAndPort hostAndPort = hostAndPorts.get(round.getAndIncrement());

        System.out.println("==============response server port[" + hostAndPort.getPort() + "]======================");

        return hostAndPort;
    }
}

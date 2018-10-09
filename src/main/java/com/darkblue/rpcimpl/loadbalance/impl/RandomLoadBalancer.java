package com.darkblue.rpcimpl.loadbalance.impl;

import com.darkblue.rpcimpl.commons.HostAndPort;
import com.darkblue.rpcimpl.loadbalance.LoadBalancer;
import com.darkblue.rpcimpl.protocol.MethodInvokeMetaWrap;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {
    public HostAndPort select(List<HostAndPort> hostAndPorts, MethodInvokeMetaWrap methodInvokeMetaWrap) {
        if (hostAndPorts.isEmpty()) throw new RuntimeException("not found host");

        int i = new Random().nextInt(hostAndPorts.size());

        return hostAndPorts.get(i);
    }
}

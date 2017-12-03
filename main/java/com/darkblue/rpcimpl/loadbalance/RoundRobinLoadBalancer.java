package com.darkblue.rpcimpl.loadbalance;

import com.darkblue.rpcimpl.commons.HostAndPort;
import com.darkblue.rpcimpl.protocol.MethodInvokeMetaWrap;

import java.util.List;


public class RoundRobinLoadBalancer implements LoadBalancer {
    private int round = 0;

    public HostAndPort select(List<HostAndPort> hostAndPorts, MethodInvokeMetaWrap methodInvokeMetaWrap) {

        int i = round % hostAndPorts.size();

        round ++;

        if (round < 0){
            round = 0;
        }

        return hostAndPorts.get(i);
    }
}

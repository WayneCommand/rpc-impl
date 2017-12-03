package com.darkblue.rpcimpl.failover;

import com.darkblue.rpcimpl.commons.HostAndPort;
import com.darkblue.rpcimpl.loadbalance.LoadBalancer;
import com.darkblue.rpcimpl.protocol.MethodInvokeMetaWrap;
import com.darkblue.rpcimpl.protocol.ResultWrap;
import com.darkblue.rpcimpl.transport.RpcClient;

import java.util.List;

/**
 * 故障转移
 */
public class FailFastCluster implements Cluster {
    private RpcClient client;
    private LoadBalancer loadBalancer;

    public FailFastCluster(RpcClient client, LoadBalancer loadBalancer) {
        this.client = client;
        this.loadBalancer = loadBalancer;
    }

    public ResultWrap invoke(List<HostAndPort> hostAndPorts, MethodInvokeMetaWrap methodInvokeMetaWrap) {
        HostAndPort select = loadBalancer.select(hostAndPorts, methodInvokeMetaWrap);

        return client.invoke(methodInvokeMetaWrap,select);
    }

    public void setClient(RpcClient client) {
        this.client = client;
    }

    public void setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }
}

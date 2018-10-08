package com.darkblue.rpcimpl.loadbalance;

import com.darkblue.rpcimpl.commons.HostAndPort;
import com.darkblue.rpcimpl.protocol.MethodInvokeMetaWrap;

import java.util.List;

/**
 * 负载均衡策略
 */
public interface LoadBalancer {

    HostAndPort select(List<HostAndPort> hostAndPorts, MethodInvokeMetaWrap methodInvokeMetaWrap);

}

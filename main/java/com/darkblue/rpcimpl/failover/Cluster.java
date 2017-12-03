package com.darkblue.rpcimpl.failover;

import com.darkblue.rpcimpl.commons.HostAndPort;
import com.darkblue.rpcimpl.protocol.MethodInvokeMetaWrap;
import com.darkblue.rpcimpl.protocol.ResultWrap;

import java.util.List;

public interface Cluster {
    ResultWrap invoke(List<HostAndPort> hostAndPorts , MethodInvokeMetaWrap methodInvokeMetaWrap);
}

package com.darkblue.rpcimpl.transport;

import com.darkblue.rpcimpl.commons.HostAndPort;
import com.darkblue.rpcimpl.protocol.MethodInvokeMetaWrap;
import com.darkblue.rpcimpl.protocol.ResultWrap;

/**
 * RPC客户端
 */
public interface RpcClient {

    void init();

    ResultWrap invoke(MethodInvokeMetaWrap methodInvokeMetaWrap , HostAndPort hostAndPort);

    void close();

}

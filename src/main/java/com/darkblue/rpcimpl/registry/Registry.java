package com.darkblue.rpcimpl.registry;

import com.darkblue.rpcimpl.commons.HostAndPort;

import java.util.List;

/**
 * 注册中心接口
 */
public interface Registry {
    String PRIFIX = "/RPCImpl";
    String SUFFIX = "/providers";

    void register(Class targetService, HostAndPort hostAndPort);

    void subscrible(Class targetService, List<HostAndPort> hostAndPorts);

    List<HostAndPort> retriveService(Class targeService);

    void close();

}

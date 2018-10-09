package com.darkblue.rpcimpl.proxy;

import com.darkblue.rpcimpl.commons.HostAndPort;
import com.darkblue.rpcimpl.commons.RpcContext;
import com.darkblue.rpcimpl.failover.Cluster;
import com.darkblue.rpcimpl.protocol.MethodInvokeMeta;
import com.darkblue.rpcimpl.protocol.MethodInvokeMetaWrap;
import com.darkblue.rpcimpl.protocol.Result;
import com.darkblue.rpcimpl.protocol.ResultWrap;
import com.darkblue.rpcimpl.registry.Registry;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * 代理调用
 */
public class DynamicProxy implements InvocationHandler , FactoryBean {
    private Class targetInterface;

    private List<HostAndPort> hostAndPorts;
    private Registry registry;

    private Cluster cluster;

    public DynamicProxy(Class targetInterface, Registry registry, Cluster cluster) {
        this.targetInterface = targetInterface;
        this.registry = registry;
        this.cluster = cluster;

        //查询服务列表
        hostAndPorts = registry.retriveService(targetInterface);

        //订阅服务变更
        registry.subscrible(targetInterface,hostAndPorts);

    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodInvokeMeta methodInvokeMeta = new MethodInvokeMeta(targetInterface, method.getName(), method.getParameterTypes(), args);

        MethodInvokeMetaWrap methodInvokeMetaWrap = new MethodInvokeMetaWrap(methodInvokeMeta);

        //设置附件信息
        Map<Object, Object> attributes = RpcContext.getContext().getAttributes();
        methodInvokeMetaWrap.setAttchment(attributes);

        //发送请求
        ResultWrap resultWrap = cluster.invoke(hostAndPorts, methodInvokeMetaWrap);

        if (resultWrap == null) throw new RuntimeException("no result exception");

        Result result = resultWrap.getResult();
        if (result.getException() != null){
            throw result.getException();
        }

        //合并两端的附件信息
        attributes.putAll(resultWrap.getAttchment());


        return result.getResult();
    }

    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(DynamicProxy.class.getClassLoader(),new Class[]{targetInterface},this);
    }

    public Class<?> getObjectType() {
        return targetInterface;
    }

    public boolean isSingleton() {
        return true;
    }
}

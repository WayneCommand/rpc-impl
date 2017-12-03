package com.darkblue.rpcimpl.commons;

import com.darkblue.rpcimpl.failover.FailFastCluster;
import com.darkblue.rpcimpl.loadbalance.RoundRobinLoadBalancer;
import com.darkblue.rpcimpl.proxy.DynamicProxy;
import com.darkblue.rpcimpl.transport.NettyRPCClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Map;

/**
 * 指向扫描 创建接口实现类实例到Spring容器
 */
public class ReferenceScanner implements BeanFactoryPostProcessor {

    private Map<String,Object> references;
    private String registryName;



    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        System.out.println(configurableListableBeanFactory.getClass());

        //创建RPC客户端
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
        BeanDefinitionBuilder rpcClient = BeanDefinitionBuilder.genericBeanDefinition(NettyRPCClient.class);
        rpcClient.setInitMethodName("init");
        rpcClient.setDestroyMethodName("close");
        listableBeanFactory.registerBeanDefinition("rpcClient",rpcClient.getBeanDefinition());

        //创建轮询策略
        BeanDefinitionBuilder robin = BeanDefinitionBuilder.genericBeanDefinition(RoundRobinLoadBalancer.class);
        listableBeanFactory.registerBeanDefinition("roundRobinLoadBalancer",robin.getBeanDefinition());

        //构建Cluster

        BeanDefinitionBuilder cluster = BeanDefinitionBuilder.genericBeanDefinition(FailFastCluster.class);
        cluster.addConstructorArgReference("rpcClient");
        cluster.addConstructorArgReference("roundRobinLoadBalancer");
        listableBeanFactory.registerBeanDefinition("failFastCluster",cluster.getBeanDefinition());


        for (Map.Entry<String, Object> entry : references.entrySet()) {

            String beanId = entry.getKey();
            Object targetClass = entry.getValue();

            BeanDefinitionBuilder proxy = BeanDefinitionBuilder.genericBeanDefinition(DynamicProxy.class);
            proxy.addConstructorArgValue(targetClass);
            proxy.addConstructorArgReference(registryName);
            proxy.addConstructorArgReference("failFastCluster");
            listableBeanFactory.registerBeanDefinition(beanId, proxy.getBeanDefinition());
        }


    }

    public void setReferences(Map<String, Object> references) {
        this.references = references;
    }

    public void setRegistryName(String registryName) {
        this.registryName = registryName;
    }
}

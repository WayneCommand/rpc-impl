package com.darkblue.rpcimpl.registry;

import com.darkblue.rpcimpl.commons.HostAndPort;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * zookeeper实现注册中心
 */
public class ZookeeperRegistry implements Registry {

    private ZkClient client;

    public ZookeeperRegistry(String host){

        System.out.println(host);
        client = new ZkClient(host);
    }

    /**
     * 注册服务
     * @param targetService
     * @param hostAndPort
     */
    public void register(Class targetService, HostAndPort hostAndPort) {
        String node=PRIFIX+"/"+targetService.getName()+SUFFIX;
        if(!client.exists(node)){
            client.createPersistent(node,true);
        }
        String tmpnode=node+"/"+hostAndPort.getHost()+":"+hostAndPort.getPort();

        if(client.exists(tmpnode)){
            client.deleteRecursive(tmpnode);
        }
        client.createEphemeral(tmpnode);
    }

    /**
     * 更新 主机和端口列表
     * @param targetService
     * @param hostAndPorts
     */
    public void subscrible(Class targetService, final List<HostAndPort> hostAndPorts) {
        String node=PRIFIX+"/"+targetService.getName()+SUFFIX;
        client.subscribeChildChanges(node, new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("=====update nodes ======");
                hostAndPorts.clear();
                for (String nodeStr : currentChilds) {
                    HostAndPort hostAndPort=new HostAndPort();
                    hostAndPort.setHost(nodeStr.split(":")[0]);
                    hostAndPort.setPort(Short.parseShort(nodeStr.split(":")[1]));
                    System.out.println("add nodes："+hostAndPort);
                    hostAndPorts.add(hostAndPort);
                }

            }
        });
    }

    public List<HostAndPort> retriveService(Class targetService) {
        String node=PRIFIX+"/"+targetService.getName()+SUFFIX;
        List<String> children = client.getChildren(node);
        List<HostAndPort> hostAndPorts=new CopyOnWriteArrayList<HostAndPort>();
        for (String nodeStr : children) {
            HostAndPort hostAndPort=new HostAndPort();
            hostAndPort.setHost(nodeStr.split(":")[0]);
            hostAndPort.setPort(Short.parseShort(nodeStr.split(":")[1]));
            hostAndPorts.add(hostAndPort);
        }
        return hostAndPorts;
    }

    public void close() {
        client.close();
    }
}

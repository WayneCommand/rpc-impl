package com.darkblue.rpcimpl.registry.impl;

import com.darkblue.rpcimpl.commons.HostAndPort;
import com.darkblue.rpcimpl.registry.Registry;
import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * zookeeper实现注册中心
 */
public class ZookeeperRegistry implements Registry {

    private ZooKeeper client;

    public ZookeeperRegistry(String host){
        try {
            client = new ZooKeeper(host, 50000000,(event)->{});
            if (client.exists(PRIFIX,false) == null){
                client.create(PRIFIX, new byte[0], OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册服务
     * @param targetService
     * @param hostAndPort
     */
    @Override
    public void register(Class targetService, HostAndPort hostAndPort) {
        String node=PRIFIX+"/"+targetService.getName()+SUFFIX;
        try {
            if (client.exists(node, false) == null) {
                if (client.exists(PRIFIX + "/" + targetService.getName(),false) == null){
                    client.create(PRIFIX + "/" + targetService.getName(), new byte[0], OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                client.create(node, new byte[0], OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            String tmpHostNode = node + "/" + hostAndPort.getHost() + ":" + hostAndPort.getPort();

            if (client.exists(tmpHostNode, false) != null) {
                client.delete(tmpHostNode, -1);
            }

            client.create(tmpHostNode, new byte[0], OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新 主机和端口列表
     * @param targetService
     * @param hostAndPorts
     */
    @Override
    public void subscrible(Class targetService, final List<HostAndPort> hostAndPorts) {
        String node = PRIFIX + "/" + targetService.getName() + SUFFIX;
        try {
            Watcher watcher = new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println("=====update nodes ======");
                    hostAndPorts.clear();
                    try {
                        List<String> currentChilds = client.getChildren(node, false);

                        for (String nodeStr : currentChilds) {
                            HostAndPort hostAndPort = new HostAndPort();
                            hostAndPort.setHost(nodeStr.split(":")[0]);
                            hostAndPort.setPort(Short.parseShort(nodeStr.split(":")[1]));
                            System.out.println("add node[" + hostAndPort.getHost() + ":" + hostAndPort.getPort() + "]");
                            hostAndPorts.add(hostAndPort);
                        }

                        //因为watcher是一次性的 这里重新注册
                        client.getChildren(node, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };

            //注册监听
            client.getChildren(node,watcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<HostAndPort> retriveService(Class targetService) {
        String node=PRIFIX+"/"+targetService.getName()+SUFFIX;
        List<HostAndPort> hostAndPorts=new CopyOnWriteArrayList<HostAndPort>();
        try {
            List<String> children = client.getChildren(node,false);
            for (String nodeStr : children) {
                HostAndPort hostAndPort=new HostAndPort();
                hostAndPort.setHost(nodeStr.split(":")[0]);
                hostAndPort.setPort(Short.parseShort(nodeStr.split(":")[1]));
                hostAndPorts.add(hostAndPort);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hostAndPorts;
    }

    @Override
    public void close() {
        try {
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.darkblue.rpcimpl.registry.impl;

import com.darkblue.rpcimpl.commons.HostAndPort;
import com.darkblue.rpcimpl.registry.Registry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;


import java.io.IOException;
import java.util.ArrayList;
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
    public void register(Class targetService, HostAndPort hostAndPort) {
        String node=PRIFIX+"/"+targetService.getName()+SUFFIX;
        try {
            Stat stat = client.exists(node, false);

            if (stat == null) {
                client.create(PRIFIX + "/" + targetService.getName(), new byte[0], OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                client.create(node, new byte[0], OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            String tmpnode=node+"/"+hostAndPort.getHost()+":"+hostAndPort.getPort();

            if(client.exists(tmpnode,false) != null){
                client.delete(tmpnode, -1);
            }
            client.create(tmpnode, new byte[0], OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新 主机和端口列表
     * @param targetService
     * @param hostAndPorts
     */
    public void subscrible(Class targetService, final List<HostAndPort> hostAndPorts) {
        String node=PRIFIX+"/"+targetService.getName()+SUFFIX;

        client.register((we)->{
            System.out.println("=====update nodes ======");
            hostAndPorts.clear();

            try {
                List<String> currentChilds = client.getChildren(node, false);

                for (String nodeStr : currentChilds) {
                    HostAndPort hostAndPort=new HostAndPort();
                    hostAndPort.setHost(nodeStr.split(":")[0]);
                    hostAndPort.setPort(Short.parseShort(nodeStr.split(":")[1]));
                    System.out.println("add nodes："+hostAndPort);
                    hostAndPorts.add(hostAndPort);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

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

    public void close() {
        try {
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

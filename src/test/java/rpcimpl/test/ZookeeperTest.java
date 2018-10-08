package rpcimpl.test;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;


import static org.apache.zookeeper.ZooDefs.Ids.*;

public class ZookeeperTest {

    private ZooKeeper zooKeeper;


    @Before
    public void bef() throws Exception {
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 9999, null);

    }

    @Test
    public void testExist() throws Exception {
        System.out.println(zooKeeper.exists("/shenlanode2", false) == null);

    }

    @Test
    public void testCreate() throws Exception{
        String s = zooKeeper.create("/RPCImpl", "".getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        System.out.println(s);

    }

    @Test
    public void testDelete() throws Exception {
        zooKeeper.delete("/shenlanode2", -1);
    }



}

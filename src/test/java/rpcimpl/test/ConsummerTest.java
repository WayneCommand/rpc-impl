package rpcimpl.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rpcimpl.test.service.DarkblueService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:consummer.xml")
public class ConsummerTest {

    @Autowired
    private DarkblueService darkblueService;

    @Test
    public void start() throws Exception{

        //System.out.println(darkblueService.call("hi. this is send message!"));

        System.out.println(darkblueService.call("666"));

    }

}

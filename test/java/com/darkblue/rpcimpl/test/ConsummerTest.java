package com.darkblue.rpcimpl.test;

import com.darkblue.rpcimpl.test.service.DarkblueService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsummerTest {

    @Test
    public void start() throws Exception{

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("consummer.xml");

        DarkblueService darkblueService = (DarkblueService) applicationContext.getBean("darkblueService");


        System.out.println(darkblueService.count(999999999));

    }

}

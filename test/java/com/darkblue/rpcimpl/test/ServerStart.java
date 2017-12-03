package com.darkblue.rpcimpl.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ServerStart {

    @Test
    public void start() throws Exception{
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        System.in.read();
    }

}

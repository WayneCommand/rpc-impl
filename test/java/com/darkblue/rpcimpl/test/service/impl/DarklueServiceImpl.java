package com.darkblue.rpcimpl.test.service.impl;

import com.darkblue.rpcimpl.test.service.DarkblueService;

public class DarklueServiceImpl implements DarkblueService{

    public DarklueServiceImpl() {
    }

    public String call(String str) {
        return "Server : " + str;
    }

    public long count(int i) {
        //i = 1/0;

        return i << 2;
    }
}

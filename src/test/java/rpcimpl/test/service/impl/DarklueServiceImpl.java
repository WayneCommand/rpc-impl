package rpcimpl.test.service.impl;

import rpcimpl.test.service.DarkblueService;

public class DarklueServiceImpl implements DarkblueService {

    public String call(String str) {
        return "Server : " + str;
    }

    public long count(int i) {
        //i = 1/0;

        return 2 / i;
    }
}

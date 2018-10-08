package rpcimpl.test.service.impl;

import com.darkblue.rpcimpl.annotation.RpcService;
import org.springframework.stereotype.Service;
import rpcimpl.test.service.DarkblueService;

@RpcService
@Service
public class DarklueServiceImpl implements DarkblueService {

    public String call(String str) {
        return "Server : " + str;
    }

    public long count(int i) {
        //i = 1/0;

        return 2 / i;
    }
}

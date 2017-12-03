package com.darkblue.rpcimpl.serializer;

import org.springframework.util.SerializationUtils;

public class SpringSerializationUtils implements ObjectSerializer {

    public byte[] serializer(Object value) {


        return SerializationUtils.serialize(value);

    }

    public Object deserializer(byte[] bytes) {


        return SerializationUtils.deserialize(bytes);
    }
}

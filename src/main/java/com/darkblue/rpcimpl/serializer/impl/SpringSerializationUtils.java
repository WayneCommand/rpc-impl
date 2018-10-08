package com.darkblue.rpcimpl.serializer.impl;

import com.darkblue.rpcimpl.serializer.ObjectSerializer;
import org.springframework.util.SerializationUtils;

public class SpringSerializationUtils implements ObjectSerializer {

    public byte[] serializer(Object value) {


        return SerializationUtils.serialize(value);

    }

    public Object deserializer(byte[] bytes) {


        return SerializationUtils.deserialize(bytes);
    }
}

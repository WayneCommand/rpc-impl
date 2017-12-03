package com.darkblue.rpcimpl.serializer;

/**
 * 序列化接口
 */
public interface ObjectSerializer {

    byte[] serializer(Object value);

    Object deserializer(byte bytes[]);
}

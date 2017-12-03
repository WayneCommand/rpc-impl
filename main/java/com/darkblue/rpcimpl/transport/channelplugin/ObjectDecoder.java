package com.darkblue.rpcimpl.transport.channelplugin;

import com.darkblue.rpcimpl.serializer.ObjectSerializer;
import com.darkblue.rpcimpl.serializer.SpringSerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 传输对象反序列化
 */
public class ObjectDecoder extends MessageToMessageDecoder<ByteBuf> {
private ObjectSerializer serializer = new SpringSerializationUtils();

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];

        byteBuf.readBytes(bytes);

        Object deserializer = serializer.deserializer(bytes);

        list.add(deserializer);


    }
}

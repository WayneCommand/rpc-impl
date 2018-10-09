package com.darkblue.rpcimpl.transport.channelplugin;

import com.darkblue.rpcimpl.serializer.ObjectSerializer;
import com.darkblue.rpcimpl.serializer.impl.SpringSerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 传输对象反序列化
 */
public class ObjectDecoder extends MessageToMessageDecoder<ByteBuf> {

    private ObjectSerializer serializer = new SpringSerializationUtils();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        byte[] bytes = new byte[msg.readableBytes()];

        msg.readBytes(bytes);

        Object deserializer = serializer.deserializer(bytes);

        out.add(deserializer);
    }
}

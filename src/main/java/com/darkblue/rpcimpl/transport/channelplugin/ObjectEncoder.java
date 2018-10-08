package com.darkblue.rpcimpl.transport.channelplugin;

import com.darkblue.rpcimpl.serializer.ObjectSerializer;
import com.darkblue.rpcimpl.serializer.impl.SpringSerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 传输对象序列化
 */
public class ObjectEncoder  extends MessageToMessageEncoder<Object> {

    private ObjectSerializer serializer = new SpringSerializationUtils();

    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List<Object> list) throws Exception {
        byte values [] = serializer.serializer(o);
        ByteBuf buffer = Unpooled.buffer();

        buffer.writeBytes(values);

        list.add(buffer);

    }
}

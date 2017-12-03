package com.darkblue.rpcimpl.transport;

import com.darkblue.rpcimpl.commons.HostAndPort;
import com.darkblue.rpcimpl.protocol.MethodInvokeMetaWrap;
import com.darkblue.rpcimpl.protocol.ResultWrap;
import com.darkblue.rpcimpl.transport.channelplugin.ObjectDecoder;
import com.darkblue.rpcimpl.transport.channelplugin.ObjectEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import static io.netty.channel.ChannelFutureListener.CLOSE_ON_FAILURE;
import static io.netty.channel.ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE;

/**
 * 基于Netty的RPC客户端
 */
public class NettyRPCClient implements RpcClient {
    private Bootstrap bootstrap;
    private EventLoopGroup group;

    public void init(){
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();

        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);

    }

    public ResultWrap invoke(final MethodInvokeMetaWrap methodInvokeMetaWrap, HostAndPort hostAndPort) {

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();

                pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));

                pipeline.addLast(new ObjectDecoder());

                pipeline.addLast(new LengthFieldPrepender(2));

                pipeline.addLast(new ObjectEncoder());

                pipeline.addLast(new ChannelHandlerAdapter(){
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        System.err.println("Error : " + cause.getMessage());

                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        ChannelFuture channelFuture = ctx.writeAndFlush(methodInvokeMetaWrap);
                        channelFuture.addListeners(CLOSE_ON_FAILURE, FIRE_EXCEPTION_ON_FAILURE);
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        //System.out.println("Client : " + msg);
                        ResultWrap resultWrap = (ResultWrap) msg;
                        methodInvokeMetaWrap.setResultWrap(resultWrap);
                    }
                });
            }
        });

        try {
            ChannelFuture connect = bootstrap.connect(hostAndPort.getHost(), hostAndPort.getPort());
            connect.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return methodInvokeMetaWrap.getResultWrap();
    }

    public void close() {

        group.shutdownGracefully();
    }


}

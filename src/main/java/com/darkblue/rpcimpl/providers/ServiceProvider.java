package com.darkblue.rpcimpl.providers;

import com.darkblue.rpcimpl.commons.HostAndPort;
import com.darkblue.rpcimpl.commons.RpcContext;
import com.darkblue.rpcimpl.protocol.MethodInvokeMeta;
import com.darkblue.rpcimpl.protocol.MethodInvokeMetaWrap;
import com.darkblue.rpcimpl.protocol.Result;
import com.darkblue.rpcimpl.protocol.ResultWrap;
import com.darkblue.rpcimpl.registry.Registry;
import com.darkblue.rpcimpl.transport.channelplugin.ObjectDecoder;
import com.darkblue.rpcimpl.transport.channelplugin.ObjectEncoder;
import com.darkblue.rpcimpl.utils.RpcUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import static io.netty.channel.ChannelFutureListener.CLOSE;
import static io.netty.channel.ChannelFutureListener.CLOSE_ON_FAILURE;
import static io.netty.channel.ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE;

/**
 * 服务端
 */
public class ServiceProvider {

    private Map<Class,Object> exposeBeanMap;

    private ServerBootstrap serverBootstrap;
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private short port;

    @Autowired
    private Registry registry;

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public void setExposeBeanMap(Map<Class, Object> exposeBeanMap) {
        this.exposeBeanMap = exposeBeanMap;
    }

    public ServiceProvider(short port) {
        this.port = port;
    }

    @PostConstruct
    public void init(){
        serverBootstrap = new ServerBootstrap();
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();

        serverBootstrap.group(boss,worker);
        serverBootstrap.channel(NioServerSocketChannel.class);

        start();
    }

    public void start(){
        //初始化管道处理链
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();

                pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));

                pipeline.addLast(new ObjectDecoder());

                pipeline.addLast(new LengthFieldPrepender(2));

                pipeline.addLast(new ObjectEncoder());

                //处理出入站规则
                pipeline.addLast(new ChannelDuplexHandler(){
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        System.err.println("Error : " + cause.getMessage());
                        cause.printStackTrace();
                        //TODO 异常的处理规则
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        MethodInvokeMetaWrap methodInvokeMetaWrap = (MethodInvokeMetaWrap) msg;

                        MethodInvokeMeta invokeMeta = methodInvokeMetaWrap.getInvokeMeta();

                        //客户端附件信息
                        Map<Object, Object> attchment = methodInvokeMetaWrap.getAttchment();

                        //存放在服务端线程空间里
                        RpcContext.getContext().getAttributes().putAll(attchment);

                        Object target = exposeBeanMap.get(invokeMeta.getTargetClass());

                        Method method = target.getClass().getMethod(invokeMeta.getMethodName(), invokeMeta.getParameterTypes());

                        if (method.isAccessible()){
                            method.setAccessible(true);
                        }


                        ResultWrap resultWrap = new ResultWrap();

                        Result result = new Result();

                        try {
                            Object invoke = method.invoke(target, invokeMeta.getArgs());
                            result.setResult(invoke);

                            //放置服务器的附件信息
                            resultWrap.setAttchment(RpcContext.getContext().getAttributes());
                        } catch (Exception e) {
                            System.err.println("Exception :"+invokeMeta.getMethodName() + "\t" +  e.getMessage());
                            result.setException(new RuntimeException(e.getCause()));
                            //e.printStackTrace();
                        }


                        resultWrap.setResult(result);

                        ChannelFuture channelFuture = ctx.writeAndFlush(resultWrap);
                        channelFuture.addListeners(CLOSE, CLOSE_ON_FAILURE, FIRE_EXCEPTION_ON_FAILURE);

                    }
                });

            }
        });

        try {
            registerService();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    //注册服务
    private void registerService() throws UnknownHostException{
        String host= InetAddress.getLocalHost().getHostAddress();//自动解析本机ip

        for(Class targetClass : exposeBeanMap.keySet()){
            registry.register(targetClass,new HostAndPort(host,port));
        }

        new Thread(()->{
            try {
                //绑定端口并启动服务
                ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @PreDestroy
    public void close(){
        RpcUtils.shutdownGracefully(boss,worker);
    }
}

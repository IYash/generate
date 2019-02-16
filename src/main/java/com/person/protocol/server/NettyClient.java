package com.person.protocol.server;

import com.person.protocol.codec.NettyMessageDecoder;
import com.person.protocol.codec.NettyMessageEncoder;
import com.person.protocol.handler.BookReqHandler;
import com.person.protocol.handler.HeartBeatReqHandler;
import com.person.protocol.handler.LoginAuthReqHandler;
import com.person.protocol.model.NettyConstant;
import com.person.rpc.transport.netty.TimeClientHandler;
import com.person.rpc.transport.netty.TimeServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 客户端主要用于初始化系统资源，根据配置信息发起连接
 */
public class NettyClient {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port,String host) throws Exception{
        //配置客户端NIO线程组
        try{
            Bootstrap b= new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyMessageDecoder(1024*1024,4,4));
                            ch.pipeline().addLast("messageEncoder",new NettyMessageEncoder());
                            ch.pipeline().addLast("readTimeoutHandler",new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("loginAuthHandler",new LoginAuthReqHandler());
                            ch.pipeline().addLast("bookHandler",new BookReqHandler());
                            ch.pipeline().addLast("heartBeatHandler",new HeartBeatReqHandler());

                        }
                    });
            //发起一部连接操作
            ChannelFuture future = b.connect(new InetSocketAddress(host,port)).sync();
            future.channel().closeFuture().sync();
        }finally {
            //所有资源释放完成之后，清空资源，再次发起重连操作
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        TimeUnit.SECONDS.sleep(5);
                        connect(NettyConstant.PORT,NettyConstant.REMOTEIP);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyClient().connect(NettyConstant.PORT,NettyConstant.REMOTEIP);
    }
}

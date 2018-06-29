package com.person.rpc.transport.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by huangchangling on 2018/6/29.
 * 基于netty的服务端
 */
public class TimeServer {

    public void bind(int port) throws Exception{
        //配置服务端的NIO线程组
        EventLoopGroup boosGroup = new NioEventLoopGroup();//服务端接受客户端的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();//SocketChannel的网络读写
        try{ServerBootstrap b = new ServerBootstrap();
        b.group(boosGroup,workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new ChildChannelHandler());//绑定i/o事件的处理类
        //绑定端口，同步等待成功
        ChannelFuture f= b.bind(port).sync();
        //等待服务端监听端口关闭
        f.channel().closeFuture().sync();
        }finally {
            //优雅退出，释放线程池资源
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new TimeServerHandler());
        }
    }

    public static void main(String[] args) throws Exception{
        int port = 8080;
        new TimeServer().bind(port);
    }
}

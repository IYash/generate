package com.person.rpc.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * Created by huangchangling on 2018/6/29.
 */
public class TimeClientHandler extends ChannelHandlerAdapter{

    private static final Logger logger = Logger.getLogger(TimeClientHandler.class);

    private final ByteBuf firstMessage;
    public TimeClientHandler(){
        byte[] req = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }
    @Override
    //当客户端和服务端TCP链路建立成功，netty的NIO线程会调用channelActive方法
    public void channelActive(ChannelHandlerContext ctx){
        //发送查询时间的指令给服务端，将请求消息发送给服务端
        ctx.writeAndFlush(firstMessage);
    }
    @Override
    //当服务端返回应答消息是，channelRead方法被调用
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8");
        System.out.println("Now is: "+body);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        //释放资源
        logger.warn("unexpected exception from downstream:"+cause.getMessage());
        ctx.close();
    }
}

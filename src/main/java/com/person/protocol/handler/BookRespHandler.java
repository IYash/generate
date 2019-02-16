package com.person.protocol.handler;

import com.person.protocol.model.*;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;


/**
 * 握手和安全认证
 * 握手的发起是在客户端和服务端TCP链路建立成功通道激活时，握手消息的接入和安全认证在服务端处理
 */
public class BookRespHandler extends ChannelHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        NettyMessage message = (NettyMessage) msg;
        Header header = message.getHeader();
        if (header != null && header.getType() == MessageType.BOOK_REQ.value()){
            System.out.println(message.getBody()+"-------------------server receive message");
            ctx.writeAndFlush(buildBookResp());
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildBookResp() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.BOOK_RESP.value());
        message.setHeader(header);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
        ctx.fireExceptionCaught(cause);
    }
}

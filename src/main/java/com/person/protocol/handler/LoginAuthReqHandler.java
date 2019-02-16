package com.person.protocol.handler;

import com.person.protocol.model.Header;
import com.person.protocol.model.MessageType;
import com.person.protocol.model.NettyConstant;
import com.person.protocol.model.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;


/**
 * 握手和安全认证
 * 握手的发起是在客户端和服务端TCP链路建立成功通道激活时，握手消息的接入和安全认证在服务端处理
 */
public class LoginAuthReqHandler extends ChannelHandlerAdapter {





    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ctx.writeAndFlush(buildLoginReq());//当客户端跟服务端TCP三次握手成功之后，由客户端构造握手请求消息发送给服务端
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        NettyMessage message = (NettyMessage) msg;
        if(message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
            byte loginResult = (byte) message.getBody();
            if(loginResult != (byte)0) {
                //握手失败
                ctx.close();//关闭链路，重新发起连接
            }else{
                System.out.println("Login is ok:"+ message);
                ctx.fireChannelRead(msg);
            }
        }else{
            ctx.fireChannelRead(msg);//透传
        }
    }
    private NettyMessage buildLoginReq(){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        return message;
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
        ctx.fireExceptionCaught(cause);
    }
}

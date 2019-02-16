package com.person.protocol.handler;

import com.person.protocol.model.Header;
import com.person.protocol.model.MessageType;
import com.person.protocol.model.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 握手成功之后，由客户端主动发送心跳消息，服务端收到心跳消息之后，返回心跳应答消息
 * 由于心跳消息的目的是为了检测链路的可用性，因此不公携带消息体
 */
public class HeartBeatReqHandler extends ChannelHandlerAdapter {
    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        NettyMessage message = (NettyMessage) msg;
        //握手成功
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx),0,5000,TimeUnit.MILLISECONDS);
        }else if(message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
            System.out.println("Client receive server heart beat message : -----> "+ message);
            ctx.fireChannelRead(msg);
        } else {
           ctx.fireChannelRead(msg);
        }
    }
    private class HeartBeatTask implements Runnable{

        private final ChannelHandlerContext ctx;
        public HeartBeatTask(ChannelHandlerContext ctx){
            this.ctx = ctx;
        }
        @Override
        public void run() {
            NettyMessage heatBeat = buildHeatBeat();
            System.out.println("Client send heart beat message to server : " + heatBeat);
            ctx.writeAndFlush(heatBeat);
        }
    }
    private NettyMessage buildHeatBeat(){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_REQ.value());
        message.setHeader(header);
        return message;
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
        if (heartBeat !=null){
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}

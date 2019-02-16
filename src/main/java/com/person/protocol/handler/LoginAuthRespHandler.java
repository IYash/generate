package com.person.protocol.handler;

import com.person.protocol.model.Header;
import com.person.protocol.model.MessageType;
import com.person.protocol.model.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端的握手接入和安全认证代码
 */
public class LoginAuthRespHandler extends ChannelHandlerAdapter {

    private Map<String,Boolean> nodeCheck = new ConcurrentHashMap<>();

    private String[] whiteList ={"127.0.0.1","192.168.47.1"};

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
        NettyMessage message =(NettyMessage)msg;
        //如果是握手请求信息，处理，其他消息透传
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()){
            String nodeIndex = ctx.channel().remoteAddress().toString();
            NettyMessage loginResp = null;
            //重复登录，拒绝,以防止句柄泄露
            if(nodeCheck.containsKey(nodeIndex)) {
                loginResp = buildResponse((byte)-1);
            }else{
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for(String WIP :whiteList){
                    if (WIP.equals(ip)) {
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK ? buildResponse((byte)0):buildResponse((byte)-1);
                if(isOK) nodeCheck.put(nodeIndex,true);
            }
            System.out.println("The login response is : " + loginResp + " body ["+ loginResp.getBody()+ "]");
            ctx.writeAndFlush(loginResp);
        }else{
            ctx.fireChannelRead(msg);
        }
    }
    private NettyMessage buildResponse(byte result){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(result);
        return message;
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
        nodeCheck.remove(ctx.channel().remoteAddress().toString());//删除缓存
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}
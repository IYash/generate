package com.person.protocol.handler;

import com.person.protocol.model.Book;
import com.person.protocol.model.Header;
import com.person.protocol.model.MessageType;
import com.person.protocol.model.NettyMessage;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * 握手和安全认证
 * 握手的发起是在客户端和服务端TCP链路建立成功通道激活时，握手消息的接入和安全认证在服务端处理
 */
public class BookReqHandler extends ChannelHandlerAdapter {

    private volatile ScheduledFuture<?> bookReq;
    private volatile boolean start=true;

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        NettyMessage message = (NettyMessage) msg;
        Header header = message.getHeader();
        if (header != null && start){
            System.out.println(message+"-------------------client receive message");
            start =false;
            bookReq = ctx.executor().scheduleAtFixedRate(new BookReqHandler.BookTask(ctx),0,10000,TimeUnit.MILLISECONDS);
        }else if(message.getHeader() != null && message.getHeader().getType() == MessageType.BOOK_RESP.value()) {
            System.out.println("Client receive server book response message : -----> "+ message);
            ctx.fireChannelRead(msg);
        }else{
            ctx.fireChannelRead(msg);
        }
    }
    private class BookTask implements Runnable{

        private final ChannelHandlerContext ctx;
        public BookTask(ChannelHandlerContext ctx){
            this.ctx = ctx;
        }
        @Override
        public void run() {
            NettyMessage bookReq = buildBookReq();
            System.out.println("Client send book request message to server : " + bookReq);
            ctx.writeAndFlush(bookReq);
        }
    }
    private NettyMessage buildBookReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.BOOK_REQ.value());
        message.setHeader(header);
        Book book = new Book();
        book.bookBuilder().buildAuthor("JackMa").buildPublishDate(new Date()).buildDesc("this is a story about who build a empire");
        message.setBody(book);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
        if (bookReq !=null){
            bookReq.cancel(true);
            bookReq = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}

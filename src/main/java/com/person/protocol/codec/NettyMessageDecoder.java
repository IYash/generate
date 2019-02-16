package com.person.protocol.codec;


import com.person.protocol.model.Header;
import com.person.protocol.model.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.HashMap;
import java.util.Map;

/**
 * nettyMessage解码类
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    MarshallingDecoder marshallingDecoder;
    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws Exception{
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        marshallingDecoder = new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception{
        //以下逻辑有问题，解析出来的frame始终为空
        ByteBuf frame = (ByteBuf)super.decode(ctx,in);
        if (frame == null){
            return null;
        }
        in.resetReaderIndex();//用于配合半包处理
        NettyMessage message = new NettyMessage();
        Header header =  new Header();
        header.setCrcCode(in.readInt());
        header.setLength(in.readInt());
        header.setSessionID(in.readLong());
        header.setType(in.readByte());
        header.setPriority(in.readByte());
        int size = in.readInt();
        if (size > 0){
            Map<String,Object> attch = new HashMap<>();
            int keySize = 0;
            byte[] keyArray = null;
            String key = null;
            for (int i=0;i< size;i++){
                keySize = in.readInt();
                keyArray = new byte[keySize];
                in.readBytes(keyArray);
                key = new String(keyArray,"utf-8");
                attch.put(key,marshallingDecoder.decode(in));
            }
            keyArray =null;
            key = null;
            header.setAttachment(attch);
        }
        if (in.readableBytes() > 0){//用于识别是否还有可读的有效内容
            message.setBody(marshallingDecoder.decode(in));
        }
        message.setHeader(header);
        return message;
    }
}

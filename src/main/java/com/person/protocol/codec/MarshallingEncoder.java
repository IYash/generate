package com.person.protocol.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Marshaller;

import java.io.IOException;

/**
 * netty消息编码工具类
 */
public class MarshallingEncoder {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    Marshaller marshaller;
    public MarshallingEncoder() throws IOException{
        this.marshaller = MarshallingCodecFactory.buildMarshalling();
    }

    protected void encode(Object msg,ByteBuf out) throws Exception{
        try{
            int lengthPos = out.writerIndex();
            out.writeBytes(LENGTH_PLACEHOLDER);//添加一个4个字节的标记
            ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
            marshaller.start(output);
            marshaller.writeObject(msg);
            marshaller.finish();
            out.setInt(lengthPos,out.writerIndex()-lengthPos-4);
        }finally {
            marshaller.close();
        }
    }
}

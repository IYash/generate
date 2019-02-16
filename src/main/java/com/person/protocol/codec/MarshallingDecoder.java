package com.person.protocol.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * nettyMessage解码工具类
 */
public class MarshallingDecoder {
    private final Unmarshaller ummarshaller;

    public MarshallingDecoder() throws IOException{
        ummarshaller = MarshallingCodecFactory.buildUnMarshalling();
    }
    protected Object decode(ByteBuf in) throws Exception{
        int objectSize = in.readInt();
        ByteBuf buf = in.slice(in.readerIndex(),objectSize);//跳过4个字节的长度
        ByteInput input = new ChannelBufferByteInput(buf);
        try{
            ummarshaller.start(input);
            Object obj = ummarshaller.readObject();
            ummarshaller.finish();
            in.readerIndex(in.readerIndex()+objectSize);
            return obj;
        }finally {
            ummarshaller.close();
        }
    }
}

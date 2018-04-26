package com.person.generate.dao;

import com.thoughtworks.xstream.XStream;
import org.junit.Test;

/**
 * @author huangchangling on 2018/1/18 0018
 */
public class XMLParseTest {
    @Test
    public void testXML(){
        String xml ="<?xml version=\"1.0\"?>\n" +
                "<UploadImageResponse xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <ErrorCode>200014</ErrorCode>\n" +
                "  <ErrorMsg>该图片已存在，请不要重复上传。</ErrorMsg>\n" +
                "  <CtripImageURL>https://dimg04.c-ctrip.com/images/300l0o000000f68nk7FF6.jpg</CtripImageURL>\n" +
                "  <CtripImageID>0</CtripImageID>\n" +
                "</UploadImageResponse>";
        parseUploadImageRes(xml);
    }
    private UploadImageResponse parseUploadImageRes(String xml){
        XStream xStream = new XStream();
        xStream.alias("UploadImageResponse", UploadImageResponse.class);
        return (UploadImageResponse)xStream.fromXML(xml);
    }
}

package com.person.shaller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by huangchangling on 2018/10/18.
 * 将java对象转换成json或者xml的工具类
 */
public class CustomMessageMarshallerUtils {
    private static final String  UTF_8 = "utf-8";
    private static ObjectMapper jsonObjectMapper = new ObjectMapper();
    private static XmlMapper xmlObjectMapper;

    public CustomMessageMarshallerUtils() {
    }

    public static String getMessage(Object request,String format) {
        try {
            ByteArrayOutputStream e = new ByteArrayOutputStream(1024);
            if ("json".equals(format)) {
                jsonObjectMapper.writeValue(e, request);
            } else if ("xml".equals(format)) {
                xmlObjectMapper.writeValue(e,request);
            }
            return e.toString(UTF_8);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
       return "";
    }
    static {
        xmlObjectMapper = new XmlMapper();
        xmlObjectMapper.configure(com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator.Feature.WRITE_XML_DECLARATION, false);
        xmlObjectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }
}

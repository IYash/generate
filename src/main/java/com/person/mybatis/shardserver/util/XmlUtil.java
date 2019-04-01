package com.person.mybatis.shardserver.util;

import com.thoughtworks.xstream.XStream;

import java.util.Map;

public class XmlUtil {
    private static final XStream xstream = new XStream();

    private static final String MAP_OPEN="<map>";
    private static final String MAP_CLOSE="</map>";
    private static final String MAP_EMPTY="<map/>";
    private static final String ENTITY_OPEN="<entity>";
    private static final String ENTITY_CLOSE="</entity>";
    private static final String VALUE_OPEN="<string>";
    private static final String VALUE_CLOSE="</string>";

    public static Object fromXML(String xml){
        if(xml == null){
            return null;
        }
        return xstream.fromXML(xml);
    }

    public static String toXml(Object obj){
        Map<String,Object> map = (Map<String,Object>) obj;
        if(map == null||map.isEmpty()) return MAP_EMPTY;
        StringBuilder sb = new StringBuilder();
        sb.append(MAP_OPEN);
        for(Map.Entry<String,Object> entry:map.entrySet()){
            addEntry(sb,entry.getKey(),entry.getValue()==null?"":entry.getValue().toString());
        }
        sb.append(MAP_CLOSE);
        return sb.toString();
    }

    private static void addEntry(StringBuilder sb, String key, String value) {
        if(key==null||value==null){
            throw new IllegalArgumentException("key or value is null");
        }
        sb.append(ENTITY_OPEN).append(VALUE_OPEN);
        escapeElementEntities(sb,key);
        sb.append(VALUE_CLOSE).append(VALUE_OPEN);
        escapeElementEntities(sb,value);
        sb.append(VALUE_CLOSE).append(ENTITY_CLOSE);
    }

    private static void escapeElementEntities(StringBuilder sb, String text) {
        for(int i=0;i<text.length();i++){
            char c = text.charAt(i);
            switch(c){
                case '<':sb.append("&lt");break;
                case '>':sb.append("&gt");break;
                case '&':sb.append("&amp;");break;
                default:
                    sb.append(c);
            }
        }
    }

}

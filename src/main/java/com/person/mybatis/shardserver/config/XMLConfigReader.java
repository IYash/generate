package com.person.mybatis.shardserver.config;

import com.person.mybatis.shardserver.entity.*;
import com.person.mybatis.shardserver.util.Delimiters;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

public class XMLConfigReader {

    public static List<ShardMapper> loadMapperConfig() throws Exception {
        SAXReader reader = new SAXReader();
        ignoreDTDValidate(reader);
        Document document = reader.read(XMLConfigReader.class.getClassLoader().getResourceAsStream("shard-mapper-config.xml"));
        Element root = document.getRootElement();
        List<Element> mapperEles = root.element("mappers").elements("mapper");
        List<ShardMapper> mappers= new ArrayList<>();
        for(Element mapperEle:mapperEles){
            String resource = mapperEle.attributeValue("resource");
            mappers.add(loadSqlMapper(resource));
        }
        return mappers;
    }

    private static ShardMapper loadSqlMapper(String resource) throws Exception {
        SAXReader reader = new SAXReader();
        ignoreDTDValidate(reader);
        Document document = reader.read(XMLConfigReader.class.getClassLoader().getResourceAsStream(resource));
        Element root = document.getRootElement();
        List<Element> sqlEles = root.elements("sql");
        ShardMapper mapperInfo = new ShardMapper();
        mapperInfo.setNamespace(root.attributeValue("namespace"));
        for(Element sqlEle:sqlEles){
            SqlInfo sqlInfo = new SqlInfo();
            String id =  sqlEle.attributeValue("id");
            sqlInfo.setId(id);
            String cacheStr = sqlEle.attributeValue("cache");
            if(!StringUtils.isBlank(cacheStr)){
                sqlInfo.setCache(Boolean.valueOf(cacheStr));
            }
            String[] refTables = sqlEle.attributeValue("refTables").split(Delimiters.COMMA);
            for(String table:refTables){
                sqlInfo.addRefTable(table.trim());
            }
            Element paramEle = sqlEle.element("param");
            String paramName=paramEle.attributeValue("name");
            String paramType = paramEle.attributeValue("type");

            ParamInfo paramInfo = new ParamInfo();
            paramInfo.setName(paramName);
            paramInfo.setType(ParamType.forCode(paramType));

            List<Element> attrEles = paramEle.elements("item");
            for(Element attrEle:attrEles){
                ParamItem item = new ParamItem();
                String itemName = attrEle.attributeValue("name");
                String itemType = attrEle.attributeValue("type");
                String columnName= attrEle.attributeValue("column");
                String enumIndex = attrEle.attributeValue("enumIndex");
                String replace = attrEle.attributeValue("replace");
                item.setName(itemName);
                item.setType(ItemType.forCode(itemType));
                item.setColumnName(columnName);
                item.setEnumIndex(enumIndex);
                item.setReplace(replace);
                paramInfo.putItem(item);
            }
            sqlInfo.setParam(paramInfo);
            mapperInfo.addSqlInfo(sqlInfo);
        }
        return mapperInfo;
    }

    public static ShardRule loadShardConfig() throws Exception {
        SAXReader reader = new SAXReader();
        ignoreDTDValidate(reader);
        Document document = reader.read(XMLConfigReader.class.getClassLoader().getResourceAsStream("shard-config.xml"));
        Element root = document.getRootElement();
        List<Element> tabEles = root.elements("table");
        ShardRule shardRule = new ShardRule();
        for(Element tabEle:tabEles){
            String name= tabEle.attributeValue("name");
            Table table = new Table();
            table.setName(name);
            table.setColumns(parseColumn(tabEle));
            shardRule.putTable(table);
        }
        return shardRule;
    }

    private static List<Column> parseColumn(Element tabEle) {
        List<Column> columns = new ArrayList<>();
        List<Element> colEles = tabEle.elements("column");
        for(Element colEle:colEles){
            Column column = new Column();
            String columnName = colEle.attributeValue("name");
            ColumnType type = ColumnType.forCode(colEle.attributeValue("type"));
            String split = colEle.attributeValue("split");
            split = StringUtils.isBlank(split)?"":split;
            String format = "";
            if(type == ColumnType.DATE) format = colEle.attributeValue("format");
            List<Element> valEles = colEle.elements("value");
            for(Element valEle:valEles){
                String alias = valEle.attributeValue("alias");
                String valueStr = valEle.getTextTrim();
                String[] valueArr = valueStr.split(Delimiters.COMMA);
                for(String val:valueArr){
                    if(StringUtils.isBlank(val)) continue;
                    column.putAlias(val,alias);
                }
            }
            column.setName(columnName);
            column.setType(type);
            column.setFormat(format);
            column.setSplit(split);
            columns.add(column);
        }
        return columns;
    }

    private static void ignoreDTDValidate(SAXReader reader) throws SAXException {
        reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    }
}

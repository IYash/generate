package com.person.mybatis.shardserver.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {

    private static DateFormat sdf ;

    public static String format(Date postDate, String format){
        sdf=new SimpleDateFormat(format);
        return sdf.format(postDate);
    }
}

package com.person.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 顺序读取
 * @author huangchangling on 2018/1/4 0004
 */
public class FileIo {

    private static String path = "D:/test.txt";
    private static String tmp = "D:/tmp.txt";

    public static void main(String[] args) throws Exception {
        InputStream in = new FileInputStream(path);
        OutputStream out= new FileOutputStream(tmp,true);//创建path的文件
        byte[] buff = new byte[100];
        int len;
        while((len=in.read(buff))>0) out.write(buff,0,len);
    }


}

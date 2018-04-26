package com.person.file;

import java.io.*;

/**
 * 文件随机读取，可以用于切割文件成小文件，只要管理好文件彼此间的关系就可以将文件分布存储
 * 有个小问题：怎么完成文件的排序，生成文件的时候去为维护文件关系？或者规范文件的命名方式--定长命名？
 * @author huangchangling on 2018/1/4 0004
 */
public class RandomFileIo {

    private static String path = "D:/test.txt";
    private static String suffix = ".txt";

    public static void main(String[] args) throws Exception {
        String catalog = "D:/temp";
        splitFile2Piece(path,catalog);
        aggregateFile(catalog,catalog+"/target.txt");
    }

    /**
     *
     * @param content 需要插入的内容
     * @param point 插入点
     * @param size 读入大小
     * @throws Exception
     */
    public static void insertAfterPoint(String content,int point,int size,String tmpFile) throws Exception{
        RandomAccessFile raf =null;
        OutputStream tmpOut = null;
        InputStream tmpIn =null;
       try{
           raf = new RandomAccessFile(new File(path),"rw");
           tmpOut = new FileOutputStream(tmpFile);
           tmpIn= new FileInputStream(tmpFile);
        //跳到pos6
            raf.seek(point);
            byte[] buff = new byte[size];
            int len;
        //将point后的文件先读入临时文件,将新的内容插入
         while((len=raf.read(buff))>0) tmpOut.write(buff,0,len);
         //跳到pos6
            raf.seek(point);
            raf.write(content.getBytes());
            while((len=tmpIn.read(buff))>0) raf.write(buff,0,len);}
       catch(Exception e){
        }finally {
           tmpIn.close();
           tmpOut.close();
           raf.close();
       }
    }

    /**
     * 分割指定文件到指定目录
     * @param fileName
     * @param catalog
     * @return
     * @throws Exception
     */
    public static void splitFile2Piece(String fileName,String catalog) throws Exception{
        RandomAccessFile raf =null;
        OutputStream tmpOut = null;
        int tmp=1;
        int i=3;
        int size =2;
        byte[] buff = new byte[size];
        try {
            raf = new RandomAccessFile(new File(fileName),"rw");
            long len = raf.length();
            while(i<len) {
                raf.seek(i);
                tmpOut = new FileOutputStream(catalog + "/" + tmp++ + suffix);
                raf.read(buff);
                tmpOut.write(buff,0,size);
                i=i+size;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            tmpOut.close();
            raf.close();
        }
    }


    public static void aggregateFile(String catalog,String targetFileName) throws Exception{
        File file = new File(catalog);
        File[] files = listFile(file) ;
        InputStream in=null ;
        OutputStream out= null;
        try {
            out = new FileOutputStream(targetFileName,true);
            byte[] buff = new byte[10];
            int len;
            //遍历File[]
            for (File f:files) {
                try {
                    in = new FileInputStream(f);
                    while((len = in.read(buff))>0) out.write(buff,0,len);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            in.close();
            out.close();
        }
    }
    private static File[] listFile(File file){
        File[] files;
        if (file.isDirectory()) {
            files = file.listFiles();
        }else{
            files = new File[1];
            files[0]=file;
        }
        return files;
    }
}

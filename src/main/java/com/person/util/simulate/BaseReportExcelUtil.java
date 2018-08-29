package com.person.util.simulate;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by huangchangling on 2018/8/3.
 * 可以作为导出的基础类，各个导出对象使用泛型标记
 */
public abstract class BaseReportExcelUtil<T> {
    //字符编码
    private static final String CHARSET = "GBK";
    //单个EXCEL最大保存记录数
    protected static final int EXCEL_SIZE = 10000;
    //EXCEL文件临时存储路径
    public static final String TEMP_PATH = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath())
            .getParent() + File.separator + "temp";
    //字段描述映射
    protected static Map<String, String> columnNameMap = new HashMap<>();
    /**
     * 根据结果集生成EXCEL文件
     * <p/>
     *
     * @param fileName EXCEL文件名
     * @param list     结果集
     */
    public  Result generateExcelFile(String fileName, String[] columns, List<T> list) {
        Result result = new Result();
        FileOutputStream outputStream = null;
        try {
            File file = new File(fileName + ".xlsx");
            if (file.exists() == false)
                file.createNewFile();
            result.setFileName(file.getPath());

            SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(EXCEL_SIZE);
            Sheet sheet = sxssfWorkbook.createSheet();
            //设置行头
            Row rowHead = sheet.createRow(0);
            parseHead(columns, rowHead);
            if (CollectionUtils.isNotEmpty(list) && columns != null)
                for (int i = 0; i < list.size(); i++) {
                    //从第二行开始展示内容
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < columns.length-1; j++) {
                        Cell cell = row.createCell(j);
                        parseBody(cell,columns[j],list.get(i));
                    }
                }

            outputStream = new FileOutputStream(file);
            sxssfWorkbook.write(outputStream);
            result.setCode(Result.RESULT_CODE.SUCCESS);
        } catch (FileNotFoundException e) {
            result.setCode(Result.RESULT_CODE.FAIL);
            result.setMessage("未找到指定文件");
        } catch (IOException e) {
            result.setCode(Result.RESULT_CODE.FAIL);
            result.setMessage("IO异常");
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                result.setCode(Result.RESULT_CODE.FAIL);
                result.setMessage("outputStream关闭异常");
            }
        }
        return result;
    }

    /**
     * 报表头字段
     * @param columns
     * @param rowHead
     */
    protected abstract void parseHead(String[] columns,Row rowHead);
    /**
     * 设置EXCEL单元格的值
     *
     * @param cell            EXCEL单元格
     * @param column          字段
     * @param vo 报表对象
     */
    protected abstract void parseBody(Cell cell, String column, T vo);

}

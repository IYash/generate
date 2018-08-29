package com.person.util.simulate;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * excel解析工具类
 *
 */
public class ExcelUtils {
	private final static String PATH1 = "C:/Users/Administrator/Desktop/temp.xlsx";//输入文件是产品id及destIds
	private final static String PATH2 = "C:/Users/Administrator/Desktop/temp_3.xlsx";//输入文件是destid和城市省份关系
	private final static Set<String> provinces = new HashSet<>();
	static{
		provinces.add("北京");
		provinces.add("天津");
		provinces.add("河北");
		provinces.add("山西");
		provinces.add("内蒙古");
		provinces.add("辽宁");
		provinces.add("吉林");
		provinces.add("黑龙江");
		provinces.add("上海");
		provinces.add("江苏");
		provinces.add("浙江");
		provinces.add("安徽");
		provinces.add("福建");
		provinces.add("江西");
		provinces.add("山东");
		provinces.add("河南");
		provinces.add("湖北");
		provinces.add("湖南");
		provinces.add("广东");
		provinces.add("海南");
		provinces.add("广西");
		provinces.add("重庆");
		provinces.add("四川");
		provinces.add("贵州");
		provinces.add("云南");
		provinces.add("西藏");
		provinces.add("陕西");
		provinces.add("甘肃");
		provinces.add("青海");
		provinces.add("宁夏");
		provinces.add("新疆");
		provinces.add("香港");
		provinces.add("澳门");
		provinces.add("台湾");
	}
	//用于解析productid和最小destid的关系
	static class Task1 implements Runnable{
		@Override
		public void run() {
			try {
				ret = ExcelUtils.parseStableColExcel1(new FileInputStream(new File(PATH1)),2);
				cdl.countDown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	//用来匹配productid和城市，省份的关系
	static class Task2 implements Runnable {

		@Override
		public void run() {
			try {
				dcpMap = ExcelUtils.parseStableColExcel2(new FileInputStream(new File(PATH2)),2);
				cdl.countDown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//这个ret用于存储产品id和最大的dest_id的关系
	private static Map<Long,Long> ret = null;
	//这个 ret_s用于存储最小dest_id和product_id列表的关系
	private static Map<Long,Set<Long>> rets = new HashMap<>();
	private static Map<Long,List<Long>> temp1 = new HashMap<>();//保存productId及其所有destId的关系
	//destId和行政映射关系
	private static Map<Long,String> dcpMap = null;
	private static ExecutorService exes = Executors.newFixedThreadPool(2);
	private static CyclicBarrier cb = new CyclicBarrier(2);
	private static CountDownLatch cdl = new CountDownLatch(2);
	public static void main(String[] args) throws Exception{
		//异步执行任务
		exes.execute(new Task1());
		exes.execute(new Task2());
		cdl.await();


		OutputStream os = null;
		long start = System.currentTimeMillis();

		//Map<Long,Long> ret = ExcelUtils.parseStableColExcel1(new FileInputStream(new File("C:/Users/Administrator/Desktop/test1.xlsx")),2);
	//		Set<Map.Entry<Long,Set<Long>>> entries = rets.entrySet();
	//		for (Map.Entry<Long,Set<Long>> entry:entries){
	//			System.out.println(entry.getKey()+"----"+entry.getValue().size());
	//			for(Long val: entry.getValue()) {
	//				System.out.println(val);
	//			}
	//			System.out.println("-----------------");
	//		}
		//产品的id和行政区域输出,数据源在temp1
//		Set<Map.Entry<Long,List<Long>>> entries = temp1.entrySet();
//		for(Map.Entry<Long,List<Long>> entry:entries) {
//			System.out.println(entry.getKey()+"----"+entry.getValue().size());
//			for(Long val: entry.getValue()) {
//				System.out.println(val);
//			}
//			System.out.println("-----------------");
//		}

		String path = "C:/Users/Administrator/Desktop/ret.xlsx";
		try {
			//os = writeXss(os, ret, path);
			os = writeXss1(os,dcpMap,path);
			System.out.println(System.currentTimeMillis()-start);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {

			}
		}
		exes.shutdown();
	}
	//用于写入多个输入源的文件
	private static OutputStream writeXss1(OutputStream os,Map<Long,String> destCPMap,String path) throws IOException {
		SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(10000);
		Sheet sheet = sxssfWorkbook.createSheet();
		Set<Map.Entry<Long,List<Long>>> entries = temp1.entrySet();//key是产品id，value是destId的list
		int i = 0;
		for (Map.Entry<Long,List<Long>> entry:entries) {
			//得到对应的城市，省份
			String city = "";
			String province = "";

			for(Long l: entry.getValue()) { // destId列表
				if (StringUtils.isEmpty(province)){
					province = destCPMap.get(l);
					if (!provinces.contains(province)) province = "";
				} else if(StringUtils.isEmpty(city)){
					city = destCPMap.get(l);
					if (provinces.contains(city)) city = "";

				}
			}
			Row row = sheet.createRow(i);
			for(int j=0;j<3;j++){//productId,城市，省份
				Cell cell = row.createCell(j);
				if (j==0) cell.setCellValue(entry.getKey());
				if (StringUtils.isNotEmpty(city) && StringUtils.isNotEmpty(province)){
					if(j==1) cell.setCellValue(city);
					if(j==2) cell.setCellValue(province);
				}
			}
				i++;
		}
		os = new FileOutputStream(path,false);
		sxssfWorkbook.write(os);
		os.flush();
		return os;
	}
	//用于写入单个输入源的文件
	private static OutputStream writeXss(OutputStream os, Map<Long, Long> ret, String path) throws IOException {
		SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(10000);
		Sheet sheet = sxssfWorkbook.createSheet();
		Set<Map.Entry<Long, Long>> entries = ret.entrySet();
		int i=0;
		for(Map.Entry<Long,Long> entry:entries) {
            Row row = sheet.createRow(i);
            for(int j=0;j<2;j++){
                Cell cell = row.createCell(j);
                cell.setCellValue(j == 0?entry.getKey():entry.getValue());
            }
            i++;
        }
		os = new FileOutputStream(path,false);
		sxssfWorkbook.write(os);
		os.flush();
		return os;
	}

	public static Map<Long,Long> parseStableColExcel1(FileInputStream strPath, int colNum) throws Exception{
		Workbook xwb = WorkbookFactory.create(strPath);
		return getNewSheetValues1(xwb, colNum);
	}
	public static Map<Long,String> parseStableColExcel2(FileInputStream strPath, int colNum) throws Exception {
		Workbook xwb = WorkbookFactory.create(strPath);
		return getNewSheetValues2(xwb, colNum);
	}

	public  static Map<Long,String> getNewSheetValues2(Workbook xwb, int colNum) {
		Sheet sheet = xwb.getSheetAt(0);
		Map<Long,String> map = new HashMap<>();
		int rows = sheet.getPhysicalNumberOfRows();
		for(int i = sheet.getFirstRowNum();i < rows;i++) {
			Row row =  sheet.getRow(i);
			Long currentDid = 0L;
			String currentVal = "";
			if (row != null){
				for(int j=0;j < colNum;j++) {
					Cell cell = row.getCell(j);
					cell.setCellType(CellType.STRING);
					if (j == 0) currentDid = Long.valueOf(cell.toString());
					else if ( j == colNum -1) currentVal = cell.toString();
				}
			}
			map.put(currentDid,currentVal);
		}
		return map;
	}

	public static Map<Long,Long> getNewSheetValues1(Workbook xwb, int colNum) throws Exception {
		Sheet sheet = xwb.getSheetAt(0);
		Map<Long,Long> map = new HashMap<>();
		Map<Long,Long> temp = new HashMap<>();//临时保存productId和destId的关系

		Set<Long> tempSet = new HashSet<>();//用于储存productId
		int rows = sheet.getPhysicalNumberOfRows();
		for (int i = sheet.getFirstRowNum(); i < rows; i++) {
			Row row = sheet.getRow(i);
			Long[] values = new Long[colNum];
			if (row != null) {
				Long currentPid = 0L ;
				for (int j = 0; j < colNum; j++) {//这里j的初始值为row.getFirstCellNum()，如果第一列为空firstCellNum为第二列,所以j直接从第一列开始
					Cell cell = row.getCell(j);
					cell.setCellType(CellType.STRING);
					Long value = Long.valueOf(cell.toString());
					values[j] = value;
					if ( j == 0 ) { //productId储存
						currentPid = value;
						if ( !tempSet.contains(value)){
							tempSet.add(currentPid);
						}
					}
					//按照当前的情况，需要将产品id及其所有的destId关联起来
					if (j == colNum-1) {//destId存储
						Long destId = temp.get(currentPid);
						List<Long> listIds = temp1.get(currentPid);
						if (destId == null ) {
							temp.put(currentPid,value);
//							Set<Long> set = rets.get(value);
//							if (set == null) {
//								set = new HashSet<>();
//								rets.put(value,set);
//							}
//							set.add(currentPid);
						}
						if (listIds == null ) {
							listIds = new ArrayList<>();
							temp1.put(currentPid,listIds);
						}
						if(!listIds.contains(value)) listIds.add(value);
					}

				}
			}
			map.put(values[0],values[1]);//这里的map和rets的逻辑都是基于排序原数据排序的结果
		}
		return map;
	}

}

package Hbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Hbase.HBaseUtils;

public class excelToHbase {
  public static void main(String[] args){
	  excelToHbase dataconverter=new excelToHbase();
	  String modelName = "jikeng1";
	  try{
		  String[] families={"depth"};
		  if(!HBaseUtils.doesTableExists(modelName))
		  HBaseUtils.creatTable(modelName,families);
		  else{
			  HBaseUtils.deleteTable(modelName);
			  HBaseUtils.creatTable(modelName,families);
		  }
            dataconverter.execute("/Users/zhaoyu/Desktop/jikeng/src/main/webapp/dataSample.xls",modelName);
	  }
	  catch(Exception e){
		  e.printStackTrace();
	  }
  }
  private void execute(String path,String table) throws IOException{
	  xlsToHbase(path,table);
  }
  private void xlsToHbase(String path,String table) throws IOException{
	  InputStream is = new FileInputStream(path);
	  HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
	  for (int numSheet = 1; numSheet <= 6; numSheet++) {
	      System.out.println("sheetnum is "+hssfWorkbook.getNumberOfSheets());
		  HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
          if (hssfSheet == null) {
              continue;
          }
          //获取sheet的名称:光纤名，列编号:对应传感器，传感器值（可能为空），时间（若时间为空那gg）
          String sheetName=hssfSheet.getSheetName();
          System.out.println("---------------------------------"+sheetName+"--------------------");
          // 获取当前工作薄的每一行
          for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
        	  try {
        		  if(rowNum%10==0)
      			    Thread.sleep(1000);
      		} catch (InterruptedException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
              HSSFRow hssfRow = hssfSheet.getRow(rowNum);
              if (hssfRow != null) {
            	  Date date=hssfRow.getCell(1).getDateCellValue();
        		  String pattern="yyyy-MM-dd hh:mm:ss";
        		  String strDate= date==null ? " " : new SimpleDateFormat(pattern).format(date);
        		  String key=strDate+"|"+sheetName.substring(2);
        		  for(int cellNum=2;cellNum<=hssfRow.getLastCellNum();cellNum++){
            		  if(hssfRow.getCell(cellNum)!=null){
            			  HBaseUtils.updateTable(table, key, "depth", new Integer(cellNum-2).toString(), hssfRow.getCell(cellNum).toString());
            		  }
            	  }
              }
          }
      }
  }
  
  private void xlsxToHBase(String path) throws IOException{
	  InputStream is = new FileInputStream(path);
      XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
      // 获取每一个工作薄
      for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
          XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
          if (xssfSheet == null) {
              continue;
          }
          // 获取当前工作薄的每一行
          for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
              XSSFRow xssfRow = xssfSheet.getRow(rowNum);
              if (xssfRow != null) {
                  XSSFCell one = xssfRow.getCell(0);
                  //读取第一列数据
                  XSSFCell two = xssfRow.getCell(1);
                  //读取第二列数据
                  XSSFCell three = xssfRow.getCell(2);
                  //读取第三列数据

              }
          }
      }
  }
  private String getValue(HSSFCell hssfCell) {
      if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
          return String.valueOf(hssfCell.getBooleanCellValue());
      } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
          return String.valueOf(hssfCell.getNumericCellValue());
      } else {
          return String.valueOf(hssfCell.getStringCellValue());
      }
  }
}

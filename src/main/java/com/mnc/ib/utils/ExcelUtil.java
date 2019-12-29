package com.mnc.ib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.qmetry.qaf.automation.core.ConfigurationManager;

public class ExcelUtil {
	
	private static ExcelUtil instance;

	private String filePath = null;
	private XSSFWorkbook excelWorkbook = null;
	private String sheetName = "testdata";
	private XSSFSheet excelSheet = null;
	private HashMap<String, Integer> columnMapping = new LinkedHashMap<String, Integer>();
	private HashMap<String, HashMap<String, String>> lisaDataMap = new LinkedHashMap<String, HashMap<String, String>>();
	private HashMap<String, HashMap<String, String>> lisaDataMapTC = new LinkedHashMap<String, HashMap<String, String>>();
	public enum coulumnNames {
		Module,
		Navigation,
		Identifier,
		TC_Name,
		TC_ID,
		REQ_UID,
		REQ_PIN,
		REQ_FIELD1,
		REQ_FIELD2,
		REQ_FIELD3,
		REQ_FIELD4,
		REQ_FIELD5
		
	}

	@SuppressWarnings("deprecation")
	public ExcelUtil() {
		int i = 0, j = 0;
		String strColName = "", rowValue = "";
		String identifierNo = "", sTCId = "";
		String env = (String) ConfigurationManager.getBundle().getProperty("env.resources");
		try {
			filePath = System.getProperty("user.dir") + File.separator + env + File.separator+ "/DataMapping.xlsx";
			FileInputStream fis = new FileInputStream(new File(filePath));
			System.out.println(fis);
			excelWorkbook = new XSSFWorkbook(fis);
			excelSheet = excelWorkbook.getSheet(sheetName);
			Iterator<Cell> firstRowCells = excelSheet.getRow(0).cellIterator();
			int cellCount = 0;
			int identifierColCount = 0, uidColNo = 0, tcDetailsNo = 0;
			// Storing all the column values in hashmap for using them later
			while (firstRowCells.hasNext()) {
				Cell cell = firstRowCells.next();
				columnMapping.put(cell.toString(), new Integer(cellCount++));
				if(cell.toString().equalsIgnoreCase("Identifier")){
					identifierColCount = cellCount - 1;
				}
				if(cell.toString().equalsIgnoreCase("REQ_UID")){
					uidColNo = cellCount - 1;
				}
				if(cell.toString().equalsIgnoreCase("TC_ID")){
					tcDetailsNo = cellCount - 1;
				}
			}
			Row firstRow = excelSheet.getRow(0);
			// Getting Row number for the given identifier

			for (i = 1; i <= excelSheet.getLastRowNum(); i++) {
				HashMap<String, String> dataValueMapping = new HashMap<String, String>();
				Row row = excelSheet.getRow(i);
				identifierNo = row.getCell(identifierColCount).toString().trim() + row.getCell(uidColNo).toString().trim();
				sTCId = row.getCell(tcDetailsNo).toString().trim();
				for (j = 0; j < row.getLastCellNum(); j++) {
					strColName = firstRow.getCell(j).toString().trim();
					rowValue = "";
					try {
						if (row.getCell(j) != null) {
							int Type = row.getCell(j).getCellType();
							if(Type==Cell.CELL_TYPE_FORMULA){
								rowValue = row.getCell(j).getRichStringCellValue().getString();	
					//			System.out.println("Formula cell value is" +rowValue);
							}else{							
							rowValue = row.getCell(j).toString().trim();
							//System.out.println("Normal cell value is"+rowValue);
							}
						}
					} catch (Exception e) {
						System.out.println("Column No" + j);

						System.out.println("Identifier No:" + identifierNo);
						System.out.println("Column Name" + strColName);
						System.out.println("Cell Value:" + rowValue);
						e.printStackTrace();
					}
					if (!rowValue.equalsIgnoreCase("") && !rowValue.contains("|")
							&& (rowValue.substring(rowValue.length() - 1).equalsIgnoreCase("+")
									|| rowValue.substring(rowValue.length() - 1).equalsIgnoreCase("-"))) {
						//rowValue = CurrencyFormat.convertJQValtoNo(rowValue);
					}
					if (strColName != "") {
						dataValueMapping.put(strColName, rowValue);
					}
				}
				lisaDataMap.put(identifierNo, dataValueMapping);
				lisaDataMapTC.put(sTCId, dataValueMapping);
			}
		} catch (Exception e) {
			System.out.println("Column No" + j);
			System.out.println("Row No:" + i);
			System.out.println("Identifier No:" + identifierNo);
			System.out.println("Column Name" + strColName);
			System.out.println("Cell Value:" + rowValue);
			System.out.println("[Excel Reader] Unable to access the data mapping file ");
			e.printStackTrace();
		}
			//System.out.println("lisaDataMap is:"+lisaDataMap);
			//System.out.println("lisaDataMapTC is:"+lisaDataMapTC);
	}	

	public static ExcelUtil getInstance() {
		if (instance == null) {
			instance = new ExcelUtil();
		}
		return instance;
	}

	public HashMap<String, HashMap<String, String>> getData() {
		return lisaDataMap;
	}
	
	public String getUserId(String sTestcaseNo){
		String sRtnVal = "";
		for (String key: lisaDataMap.keySet()) {			
		   String sTestCaseIDs = lisaDataMap.get(key).get("Testcase_Details");
		   System.out.println(sTestCaseIDs);
		   if(sTestCaseIDs.contains(sTestcaseNo)){
			   sRtnVal = lisaDataMap.get(key).get("REQ_UID");
			   break;
		   }
		}
		return sRtnVal;
	}
	
	public String getUserPIN(String sTestcaseNo){
		String sRtnVal = "";
		for (String key: lisaDataMap.keySet()) {
		   String sTestCaseIDs = lisaDataMap.get(key).get("Testcase_Details");		   
		   if(sTestCaseIDs.contains(sTestcaseNo)){
			   sRtnVal = lisaDataMap.get(key).get("UserPIN");
			   break;
		   }
		}
		return sRtnVal;
	}

	public HashMap<String, String> getLisaDataMap(String strIdentifier) {
		return lisaDataMap.get(strIdentifier);
	}
	
	public HashMap<String, String> getLisaDataMapForTC(String sTCNo) {
		return lisaDataMapTC.get(sTCNo);
	}
	
	
	

	public static void main(String[] args) {
		ExcelUtil eu = new ExcelUtil();
		//System.out.println(eu.getLisaDataMapForTC("TC_010"));
		
		/*String env = (String) ConfigurationManager.getBundle().getProperty("env.resources");
		String filePath = System.getProperty("user.dir") + File.separator+ env + "/DataMappingv1.1.xlsx";
		
		System.out.println(filePath);*/
		//ExcelUtil util = ExcelUtil.getInstance();
		//System.out.println(ExcelUtil.coulumnNames.REQ_EXCH_RATE.toString());
	}
}
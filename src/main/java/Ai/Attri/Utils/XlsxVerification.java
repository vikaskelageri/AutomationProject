package Ai.Attri.Utils;

import java.io.IOException;

import org.apache.poi.ss.usermodel.CellType;
import java.io.InputStream;
import java.time.Duration;

import org.apache.poi.ss.usermodel.Cell;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Ai.Attri.Library.Constants;
import Ai.Attri.Library.Reports;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.FileOutputStream;
import java.awt.AWTException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class XlsxVerification { 
	public FileInputStream fis;
	public FileOutputStream fileOut;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private HSSFWorkbook xlsworkbook;
	private HSSFSheet xlssheet;

	Reports reports;
	WebDriver driver;
	WebDriverWait wait;
	Constants constants = new Constants();
	public XlsxVerification(WebDriver driver) {
		fis = null;
		fileOut = null;
		workbook = null;
		sheet = null;
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		this.reports = new Reports();
	}

	public enum ExcelType {

		AUDIT_TRIAL_REPORT(7), CODELIST_REPORT(10), DEFAULT(0), ADMIN_AUDIT_TRAIL_REPORT(19), CHANGE_REQUEST_DETAILS(4);;

		private int headerIndex;

		ExcelType(int index) {
			this.headerIndex = index;
		}

		int getHeaderIndex() {

			return this.headerIndex;
		}
	}

	public List<String> getXlsxColumns(String sheetName, int columnRowNum) {
		List<String> columnNames = new ArrayList<String>();
		try {
			int index = this.workbook.getSheetIndex(sheetName);
			sheet = this.workbook.getSheetAt(index);
			Row row = (Row)sheet.getRow(columnRowNum);
			for (int i = 0; i < row.getLastCellNum(); ++i) {
				columnNames.add(row.getCell(i).getStringCellValue().trim());
			}
		}
		catch (Exception e) {
			throw e;
		}
		return columnNames;
	}

	private Map<String, Map<String, Set<Integer>>> getMapRepresentationOfExcel(String sheetName, List<String> columnsToValidate, int columnRowNum) {
		List<String> columnNames = getXlsxColumns(sheetName, columnRowNum);
		Map<String, Map<String, Set<Integer>>> excelToValidateMap = new HashMap<String, Map<String, Set<Integer>>>();
		//{}
		int index = workbook.getSheetIndex(sheetName);
		sheet = workbook.getSheetAt(index);
		Row row;
		for(int i = columnRowNum + 1; i <= sheet.getLastRowNum(); i++ ) {
			row = sheet.getRow(i);
			if(row !=null) {
				for (int j = 0; j < row.getLastCellNum(); j++) {
					if (!columnsToValidate.contains(columnNames.get(j))) {
						continue;
					}
					Cell celldata = row.getCell(j);
					String cellValue = "";
					if(celldata !=null) {
						switch (celldata.getCellType())               
						{  
						case STRING:    
							cellValue = celldata.getStringCellValue().trim();
							break;  

						case NUMERIC:  
							cellValue = Double.toString(celldata.getNumericCellValue()).trim();
							break;  
						default: 
						}
						Map<String, Set<Integer>> valueIndexMap = excelToValidateMap.getOrDefault(columnNames.get(j),new HashMap<String, Set<Integer>>());
						Set<Integer> valueIndexes = valueIndexMap.getOrDefault(cellValue, new HashSet<Integer>());				
						valueIndexes.add(i);
						valueIndexMap.put(cellValue, valueIndexes);
						excelToValidateMap.put(columnNames.get(j), valueIndexMap);

					}
				}
			}
		} 

		//		System.out.println("_____________________________");
		//		System.out.println(excelToValidateMap.keySet());
		//		for(String key: excelToValidateMap.keySet()) {
		//			System.out.println("****************" +key+ "*******************");
		//			System.out.println(excelToValidateMap.get(key));
		//		}

		return excelToValidateMap;
	}


	private void convertToMapAndValidateXlsx(String sheetName, List<Map<String, String>> data, List<String> columnsToValidate, ExcelType excelType )throws Exception {

		int excelHeaderIndex = excelType.getHeaderIndex();

		Map<String, Map<String, Set<Integer>>> excelToValidateMap = getMapRepresentationOfExcel(sheetName, columnsToValidate, excelHeaderIndex);
		for(Map<String, String> dataToValidate: data) {

			List<Set<Integer>> rowIndexList = new ArrayList<Set<Integer>>();
			for(String key: dataToValidate.keySet()) {

				Set<Integer> indexes = excelToValidateMap.getOrDefault(key, new HashMap<String, Set<Integer>>()).getOrDefault(dataToValidate.get(key), new HashSet<Integer>());
				rowIndexList.add(indexes);
			}
			Set<Integer> intersectionSet = new HashSet<> (rowIndexList.get(0));
			for(int i = 1; i < rowIndexList.size() ; i++ ) {
				intersectionSet.retainAll(rowIndexList.get(i));
			}
			if(intersectionSet.isEmpty()) {
				String message = "Match not found for:\n";
				for(String key: dataToValidate.keySet()) {
					String val = dataToValidate.get(key).matches("[0-9]+.0") ? dataToValidate.get(key).replace(".0", "") : dataToValidate.get(key);
					message = message + key+ ": "+ val + "\n";
				}
				//				Reports.ExtentReportLog("", Status.FAIL, message , false);
				throw new Exception (String.format("No Match found for %s", dataToValidate));
			}
			System.out.println(String.format("Match found for %s", dataToValidate));
			String message = "Match found for:\n";
			for(String key: columnsToValidate) {
				if(null != dataToValidate.get(key)) {
					String val = dataToValidate.get(key).matches("[0-9]+.0") ? dataToValidate.get(key).replace(".0", "") : dataToValidate.get(key);
					message = message + key+ ": "+ val + "\n";
				}
			}
//			Reports.ExtentReportLog("", Status.PASS, message , false);
			//System.out.println(intersectionSet);
		}		
	}

	public void validateXlsx(String path,String sheetNameOfExcelToTest,List<String> scenerioNames,List<String> columnsToValidate,XlsxVerification.ExcelType excelType, String ExcelValidationSheet, 
			HashMap<String, HashMap<String, HashMap<String, String>>> IMap) throws Exception {
		
		fis = new FileInputStream(path);
		workbook = new XSSFWorkbook((InputStream)fis);
		sheet = this.workbook.getSheetAt(0);
		fis.close();
		
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		
		for(String scenerio : scenerioNames) {
			Map<String, String> dataToValidate = new HashMap<>();
		for (String column: columnsToValidate) {
			String value = IMap.get(ExcelValidationSheet).get(scenerio).get(column);	
			if(value!=null && value.trim().length()>0) {
				dataToValidate.put(column, value);
			}
		}	
		
		if(dataToValidate.keySet().size()>0) {
			data.add(dataToValidate);

		}
	}	
		
		convertToMapAndValidateXlsx(sheetNameOfExcelToTest, data, columnsToValidate, excelType);
	}

	public void validateXlsx_IMap(String path,String sheetNameOfExcelToTest,List<String> scenerioNamePrefix,List<String> columnsToValidate,XlsxVerification.ExcelType excelType,String excelName) throws Exception {
		fis = new FileInputStream(path);
		workbook = new XSSFWorkbook((InputStream)fis);
		sheet = this.workbook.getSheetAt(0);
		fis.close();
		List<String> scenerioNames = scenerioNamePrefix;
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for (String scenerio : scenerioNames) {
			Map<String, String> dataToValidate = new HashMap<String, String>();
			for (String value : columnsToValidate) {
				String column = value;
				if (value != null && value.trim().length() > 0) {
					dataToValidate.put(column, value);
				}
			}
			if (dataToValidate.keySet().size() > 0) {
				data.add(dataToValidate);
			}
		}
		convertToMapAndValidateXlsx(sheetNameOfExcelToTest, data, columnsToValidate, excelType);
	}

	public void validateXls_IMap(String path,String sheetNameOfExcelToTest,List<String> scenerioNamePrefix,List<String> columnsToValidate,XlsxVerification.ExcelType excelType,String excelName) throws Exception {
		fis = new FileInputStream(path);
		xlsworkbook = new HSSFWorkbook((InputStream)fis);
		xlssheet = xlsworkbook.getSheetAt(0);
		fis.close();
		List<String> scenerioNames = scenerioNamePrefix;
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for (String scenerio : scenerioNames) {
			Map<String, String> dataToValidate = new HashMap<String, String>();
			for (String value : columnsToValidate) {
				String column = value;
				if (value != null && value.trim().length() > 0) {
					dataToValidate.put(column, value);
				}
			}
			if (dataToValidate.keySet().size() > 0) {
				data.add(dataToValidate);
			}
		}
		convertToMapAndValidateXls(sheetNameOfExcelToTest, data, columnsToValidate, excelType);
	}

	private void convertToMapAndValidateXls(String sheetName,List<Map<String, String>> data,List<String> columnsToValidate,XlsxVerification.ExcelType excelType) throws Exception {
		int excelHeaderIndex = excelType.getHeaderIndex();
		Map<String, Map<String, Set<Integer>>> excelToValidateMap = this.getMapRepresentationOfXls(sheetName, columnsToValidate, excelHeaderIndex);
		for (Map<String, String> dataToValidate : data) {
			List<Set<Integer>> rowIndexList = new ArrayList<Set<Integer>>();
			for (String key : dataToValidate.keySet()) {
				Set<Integer> indexes = excelToValidateMap.getOrDefault(key, new HashMap<String, Set<Integer>>()).getOrDefault(dataToValidate.get(key), new HashSet<Integer>());
				rowIndexList.add(indexes);
			}
			Set<Integer> intersectionSet = new HashSet<Integer>(rowIndexList.get(0));
			for (int i = 1; i < rowIndexList.size(); ++i) {
				intersectionSet.retainAll(rowIndexList.get(i));
			}
			if (intersectionSet.isEmpty()) {
				String message = "Match not found for:\n";
				for (String key2 : dataToValidate.keySet()) {
					message = String.valueOf(message) + key2 + ": " + dataToValidate.get(key2) + "\n";
				}
				throw new Exception(String.format("No Match found for %s", dataToValidate));
			}
			System.out.println(String.format("Match found for %s", dataToValidate));
			String message = "Match found for:\n";
			for (String key2 : columnsToValidate) {
				message = String.valueOf(message) + key2 + ": " + dataToValidate.get(key2) + "\n";
			}
//			Reports.ExtentReportLog("", Status.PASS, message , false);
		}
	}

	private Map<String, Map<String, Set<Integer>>> getMapRepresentationOfXls(String sheetName, List<String> columnsToValidate, int columnRowNum) {
		List<String> columnNames = getXlsColumns(sheetName, columnRowNum);
		Map<String, Map<String, Set<Integer>>> excelToValidateMap = new HashMap<String, Map<String, Set<Integer>>>();
		//{}
		int index = xlsworkbook.getSheetIndex(sheetName);
		xlssheet = xlsworkbook.getSheetAt(index);
		Row row;
		for(int i = columnRowNum + 1; i <= xlssheet.getLastRowNum(); i++ ) {
			row = xlssheet.getRow(i);
			if(row !=null) {
				for (int j = 0; j < row.getLastCellNum(); j++) {
					if (!columnsToValidate.contains(columnNames.get(j))) {
						continue;
					}
					Cell celldata = row.getCell(j);
					String cellValue = "";
					if(celldata !=null) {
						switch (celldata.getCellType())               
						{  
						case STRING:    
							cellValue = celldata.getStringCellValue().trim();
							break; 

						case NUMERIC:  
							cellValue = Double.toString(celldata.getNumericCellValue()).trim();
							break;  
						default: 
						}
						Map<String, Set<Integer>> valueIndexMap = excelToValidateMap.getOrDefault(columnNames.get(j),new HashMap<String, Set<Integer>>());
						Set<Integer> valueIndexes = valueIndexMap.getOrDefault(cellValue, new HashSet<Integer>());				
						valueIndexes.add(i);
						valueIndexMap.put(cellValue, valueIndexes);
						excelToValidateMap.put(columnNames.get(j), valueIndexMap);

					}
				}
			}
		} 

		//		System.out.println("_____________________________");
		//		System.out.println(excelToValidateMap.keySet());
		//		for(String key: excelToValidateMap.keySet()) {
		//			System.out.println("****************" +key+ "*******************");
		//			System.out.println(excelToValidateMap.get(key));
		//		}

		return excelToValidateMap;
	}

	private List<String> getXlsColumns(String sheetName, int columnRowNum) {
		List<String> columnNames = new ArrayList<String>();
		try {
			int index = xlsworkbook.getSheetIndex(sheetName);
			xlssheet = xlsworkbook.getSheetAt(index);
			Row row = (Row)xlssheet.getRow(columnRowNum);
			for (int i = 0; i < row.getLastCellNum(); ++i) {
				columnNames.add(row.getCell(i).getStringCellValue().trim());
			}
		}
		catch (Exception e) {
			throw e;
		}
		return columnNames;
	}

	public Boolean checkTextInExcel(String path, List<String> columnsToValidate,
			List<String> scenerioNames,HashMap<String, HashMap<String, HashMap<String, String>>> IMap,String ExcelValidationSheet) throws IOException, AWTException, InterruptedException {
		fis = new FileInputStream(path);
		workbook = new XSSFWorkbook(fis);
		sheet = workbook.getSheetAt(0);
		fis.close();
		Boolean finalResult=true;
		for (String scenerio : scenerioNames) {
			for (String column : columnsToValidate) {
				String value = IMap.get(ExcelValidationSheet).get(scenerio).get(column);
				if (value != null && value.trim().length() > 0) {
					Boolean match = false;
					for (Row row : sheet) {
						for (Cell celldata : row) {
							try {
								if (!((celldata.getCellType() == CellType.NUMERIC) ? Double.toString(celldata.getNumericCellValue()) : celldata.getStringCellValue()).contains(value)) {
									continue;
								}
								match = true;
							}
							catch (IllegalStateException e) {
								throw e;
							}
						}
					}
					if (match) {
						reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(), driver, Status.INFO, "Match found for '" + value + "'", false,
								constants.CaptureDesktopScreenshot());
					} else {
						finalResult=false;
						reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(), driver, Status.INFO, "No Match found for '" + value + "'", false,
								constants.CaptureDesktopScreenshot());
					}
				}
			}
		}
		return finalResult;
	}

	public Boolean ValidateExcelCRID(String path,String excelName,List<String> scenerioNamePrefix,Boolean ValidateWithTitle,String ChangeRequestID,String ChangeRequestTitle) throws IOException {
		fis = new FileInputStream(path);
		workbook = new XSSFWorkbook((InputStream)this.fis);
		sheet = this.workbook.getSheetAt(0);
		fis.close();
		Boolean finalResult=false;
		List<String> scenerioNames = scenerioNamePrefix;
		System.out.println(scenerioNames);
		System.out.println(scenerioNamePrefix);
		for (String scenerio : scenerioNames) {
			String value = "";
			if (ValidateWithTitle) {
				String CRID = ChangeRequestID;
				String title = ChangeRequestTitle;
				value = String.format("%s - %s - 1", CRID, title);
			}
			else {
				value = ChangeRequestID;
			}
			Boolean match = false;
			for (Row row : this.sheet) {
				for (Cell celldata : row) {
					try {
						if (!((celldata.getCellType() == CellType.NUMERIC) ? Double.toString(celldata.getNumericCellValue()) : celldata.getStringCellValue()).contains(value)) {
							continue;
						}
						match = true;
					}
					catch (IllegalStateException ex) {
						throw ex;
					}
				}
			}
			if (match) {
				finalResult=true;
//				Reports.ExtentReportLog("", Status.PASS, "Match found for " + value, false);
			} else {
				finalResult=false;
//				Reports.ExtentReportLog("", Status.FAIL, "No match found for " + value, false);
			}
		}
		
		return finalResult;

	}
	/**********************************************************************************************************
	 * Objective:This method is created to update excel/testdata sheet. 
	 * Input Parameters: File Path, SheetName, rowNum, colNum & desc
	 * Output Parameters: NA
	 * 
	 * @author: Girish Date : 03-Mar-2024 Updated by and when:
	 **********************************************************************************************************/
	public static void writeToExcel(String path, String sheetName, int rowNum, int colNum, String desc) {
		try {
			FileInputStream fis = new FileInputStream(path);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(sheetName);
			Row r = s.getRow(rowNum);
			if (r == null) {
				r = s.createRow(rowNum);
			}
			Row r1 = s.getRow(1);
			String c1 = r1.getCell(1).getStringCellValue();
			Cell c = r.createCell(colNum);

			CellStyle cellStyle = wb.createCellStyle();
			String getStringCellValue = c.getStringCellValue();
			c.setCellStyle(cellStyle);
			c.setCellValue(desc);

			FileOutputStream fos = new FileOutputStream(path);
			wb.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(e);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			System.out.println(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
		}
	}
	/**********************************************************************************************************
	 * Objective:This method is created to update excel/testdata sheet. 
	 * Input Parameters: File Path, SheetName, scenarioName, columnName & desc
	 * Output Parameters: NA
	 * 
	 * @author: Girish Date : 03-Mar-2024 Updated by and when:
	 **********************************************************************************************************/
	public static void writeToExcel(String path, String sheetName, String scenarioName, String columnName,
			String desc) {
		try {
			FileInputStream fis = new FileInputStream(path);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(sheetName);
			int rowNum = 0, colNum = 0;
			Row r1;
			int getLastRowNum = s.getLastRowNum();
			for (int i = 1; i <= getLastRowNum; i++) {
				r1 = s.getRow(i);
				String getStringCellValue = r1.getCell(0).getStringCellValue();
				if (getStringCellValue.equalsIgnoreCase(scenarioName)) {
					System.out.println(getStringCellValue);
					System.out.println(r1.getRowNum());
					rowNum = r1.getRowNum();
				}
			}
			int NumberOfCells = s.getRow(0).getPhysicalNumberOfCells();
			System.out.println(NumberOfCells);
			for (int j = 0; j < NumberOfCells; j++) {
				String cellValue = s.getRow(0).getCell(j).getStringCellValue();
				if (cellValue.equalsIgnoreCase(columnName)) {
					System.out.println(cellValue);
					colNum = s.getRow(0).getCell(j).getColumnIndex();

				}
			}

			Row r = s.getRow(rowNum);
			if (r == null) {
				r = s.createRow(rowNum);
			}
			Cell c = r.createCell(colNum);
			CellStyle cellStyle = wb.createCellStyle();
			String getStringCellValue = c.getStringCellValue();
			c.setCellStyle(cellStyle);
			c.setCellValue(desc);

			FileOutputStream fos = new FileOutputStream(path);
			wb.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(e);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			System.out.println(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
		}
	}

	/**********************************************************************************************************
	 * Objective:This method is created to read excel/testdata sheet.
	 * Input Parameters: File Path, SheetName, rowIndex, columnIndex
	 * Output Parameters: Cell Value
	 *
	 * @author: Jeevan Date : 08-Mar-2024 Updated by and when:
	 **********************************************************************************************************/
	public static String readCellDataAsString(String filePath, String sheetName, int rowIndex, int columnIndex) {
		try (FileInputStream fis = new FileInputStream(filePath)) {
			Workbook workbook = new XSSFWorkbook(fis);
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet != null) {
				Row row = sheet.getRow(rowIndex);
				if (row != null) {
					Cell cell = row.getCell(columnIndex);
					if (cell != null) {
						switch (cell.getCellType()) {
							case STRING:
								return cell.getStringCellValue();
							case NUMERIC:
								return String.valueOf(cell.getNumericCellValue());
							case BOOLEAN:
								return String.valueOf(cell.getBooleanCellValue());
							case BLANK:
								return "";
							default:
								return "[UNKNOWN]";
						}
					} else {
						return "Cell is null";
					}
				} else {
					return "Row is null";
				}
			} else {
				return "Sheet is null";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Error reading cell data";
		}
	}
	public static void WordDocumentWrite(String path) {
		try {
            FileInputStream fis = new FileInputStream(path);
            XWPFDocument document = new XWPFDocument(fis);
            XWPFParagraph paragraph = document.getParagraphs().get(0);
            XWPFRun run = paragraph.createRun();
            run.setText("New text added to the existing document.");
            FileOutputStream fos = new FileOutputStream(path);
            document.write(fos);
            System.out.println("Word document modified successfully.");
            fis.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
public static void moveDocument(String path)
{
	String downloadFilePath =path;
	ChromeOptions options = new ChromeOptions();
	options.addArguments("download.default_directory=" + downloadFilePath);
}

    public static String readData(String path, String sheetName,String scenarioName,String columnName) {
    	String Data=" ";
        try {
            FileInputStream fis = new FileInputStream(new File(path));
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            int columnIndex = -1;
            Row headerRow = sheet.getRow(0);
            for (Cell cell : headerRow) {
                if (cell.toString().equals(columnName)) {
                    columnIndex = cell.getColumnIndex();
                    break;
                }
            }

            if (columnIndex == -1) {
                System.out.println("Column not found: " + columnName);
                return columnName;
            }

            for (Row row : sheet) {
                Cell scenarioCell = row.getCell(0);
                if (scenarioCell != null && scenarioCell.toString().equals(scenarioName)) {
                    Cell valueCell = row.getCell(columnIndex);
                    System.out.println("Value for Scenario '" + scenarioName +
                            "' in Column '" + columnName + "': " + valueCell.toString());
                    Data= valueCell.toString();
                }
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
		return Data;
    }
		/**********************************************************************************************************
	 * Objective:This method is created to Read excel/testdata sheet. 
	 * Input Parameters: File Path, SheetName, rowNum, colNum
	 * Output Parameters: NA
	 * 
	 * @author: Girish Date : 03-Mar-2024 Updated by and when:
	 * @return 
	 **********************************************************************************************************/
	public static String readExcelData(String path, String sheetName, int rowNum, int colNum) {
		String cellValue = "";
		try {
			FileInputStream fis = new FileInputStream(path);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(sheetName);
			Row r1 = s.getRow(rowNum);
			cellValue = r1.getCell(colNum).getStringCellValue();
			System.out.println(cellValue);
			FileOutputStream fos = new FileOutputStream(path);
			wb.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(e);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			System.out.println(e);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return cellValue;
	}
	
	/**********************************************************************************************************
	 * Objective:This method is created to Read excel/testdata sheet. 
	 * Input Parameters: File Path, SheetName, scenarioName, columnName
	 * Output Parameters: NA
	 * 
	 * @author: Girish Date : 03-Mar-2024 Updated by and when:
	 * @return 
	 **********************************************************************************************************/
	public static String readExcelData(String path, String sheetName, String scenarioName, String columnName) {
		String cellValue = "";
		try {
			FileInputStream fis = new FileInputStream(path);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(sheetName);
			int rowNum = 0, colNum = 0;
			Row r1 = null;
			int getLastRowNum = s.getLastRowNum();
			for (int i = 1; i <= getLastRowNum; i++) {
				r1 = s.getRow(i);
				String getStringCellValue = r1.getCell(0).getStringCellValue();
				if (getStringCellValue.equalsIgnoreCase(scenarioName)) {
//					System.out.println(getStringCellValue);
//					System.out.println(r1.getRowNum());
					rowNum = r1.getRowNum();
				}
			}
			int NumberOfCells = s.getRow(0).getPhysicalNumberOfCells();
			//System.out.println(NumberOfCells);
			for (int j = 0; j < NumberOfCells; j++) {
				cellValue = s.getRow(0).getCell(j).getStringCellValue();
				if (cellValue.equalsIgnoreCase(columnName)) {
//					System.out.println(cellValue);
					colNum = s.getRow(0).getCell(j).getColumnIndex();
				}
			}
			cellValue = r1.getCell(colNum).getStringCellValue();
			System.out.println(cellValue);

			FileOutputStream fos = new FileOutputStream(path);
			wb.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(e);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			System.out.println(e);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return cellValue;
	}
}

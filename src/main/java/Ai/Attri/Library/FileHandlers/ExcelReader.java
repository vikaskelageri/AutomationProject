package Ai.Attri.Library.FileHandlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
	/**
	 * 
	 * @param FilePath
	 * @return
	 * @throws FileNotFoundException 
	 */
	public FileInputStream openFileForReading(String FilePath) throws FileNotFoundException  {
		File file = new File(FilePath);
		return new FileInputStream(file);
	}
	/**
	 * 
	 * @param fis
	 * @return
	 * @throws IOException
	 */
	public Workbook openWorkbook(FileInputStream fis) throws IOException {
		return new XSSFWorkbook(fis);
		//return WorkbookFactory.create(fis);
	}
	/**
	 * 
	 * @param wb
	 * @param Sheet
	 * @param row
	 * @param col
	 * @return
	 */
	public String returnCellContent(Workbook wb,String Sheet,int row, int col) {
		Cell cell = wb.getSheet(Sheet).getRow(row).getCell(col);
		
		if (cell == null)
			return "";
		else if (cell.getCellType() == CellType.STRING)
			return cell.getStringCellValue();
		else if (cell.getCellType() == CellType.BLANK)
			return "";
		else if (cell.getCellType() == CellType.NUMERIC || 
				cell.getCellType() == CellType.FORMULA) {

			String cellText = String.valueOf(cell.getNumericCellValue());
			if (DateUtil.isCellDateFormatted(cell)) {
				// format in form of M/D/YY
				double d = cell.getNumericCellValue();
				Calendar cal = Calendar.getInstance();
				cal.setTime(DateUtil.getJavaDate(d));
				cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
				cellText = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;
			}
			return cellText;
		} 
		else
			return String.valueOf(cell.getBooleanCellValue());			
	}
	/**
	 * 
	 * @param wb
	 * @param Sheet
	 * @param row
	 * @param col
	 * @param value
	 */
	public void writeToFile(Workbook wb,String Sheet,int row, int col, String value) {
		wb.getSheet(Sheet).getRow(row).getCell(col).setCellValue(value);		
	}
	/**
	 * 
	 * @param sheetName
	 * @param wb
	 * @return
	 */
	public int getRowCount(String sheetName,Workbook wb) {
		
		return wb.getSheet(sheetName).getLastRowNum()+1;
	}
	/**
	 * 
	 * @param sheetName
	 * @param wb
	 * @return
	 */
	public int getColumnCount(String sheetName,Workbook wb) {
		
		return wb.getSheet(sheetName).getRow(0).getLastCellNum();
	}
	
	/**
	 * 
	 * @param wb
	 * @param Sheet
	 * @param row
	 * @param col
	 * @return
	 */
	public HashMap<String,Integer> returnColumnNameIndexMap(Workbook wb,String sheetName) {
		HashMap<String,Integer> columnNameIndexMap = new HashMap<String,Integer>();
		int columnCount = getColumnCount(sheetName,wb);
		for(int i=0;i<columnCount;i++)	{
			columnNameIndexMap.put(returnCellContent(wb, sheetName, 0, i), i);
		}
		return columnNameIndexMap;
	}
	
	/**
	 * 
	 * @param wb
	 * @param Sheet
	 * @param row
	 * @param col
	 * @return
	 */
	public HashMap<String,Integer> returnScenarioNameIndexMap(Workbook wb,String sheetName) {
		HashMap<String,Integer> scenarioNameIndexMap = new HashMap<String,Integer>();
		int rowCount = getRowCount(sheetName,wb);
		for(int i=0;i<rowCount;i++)	{
			scenarioNameIndexMap.put(returnCellContent(wb, sheetName, i, 0), i);
		}
		return scenarioNameIndexMap;
	}

	/**
	 * 
	 * @param fis
	 * @throws IOException
	 */
	public void closeFIS(FileInputStream fis) {
		try{
			if(fis!=null)fis.close();
		}
		catch(Exception e) {			
		}

	}
	
}

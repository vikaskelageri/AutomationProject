package Ai.Attri.Library;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import Ai.Attri.Library.FileHandlers.DataTableReader;
import Ai.Attri.Library.FileHandlers.ExcelReader;
import Ai.Attri.Library.FileHandlers.JSONReader;
import Ai.Attri.Library.FileHandlers.PropertiesReader;
import Ai.Attri.Library.FileHandlers.YAMLReader;
import Ai.Attri.Utils.DateOperations;

public class DataReader {
	Constants constants;Reports reports;DateOperations dateOperations;
	public DataReader(){
		constants = new Constants();reports = new Reports();dateOperations = new DateOperations();
	}
	/**
	 * 
	 * @param JSON
	 * @param YAML
	 * @param DataTable
	 * @param Properties
	 * @return
	 */
	public HashMap<String,String> readAllDataSaveToHashMap(String JSON,String YAML,HashMap<String,String> DataTable,String Properties){
		HashMap<String,String> AllData = new HashMap<String,String>();
		if(StringUtils.isNotEmpty(JSON)) {
			JSONReader jsonReader = new JSONReader();
			jsonReader.readJSON(JSON, AllData);
		}
		if(StringUtils.isNotEmpty(YAML)) {
			YAMLReader yamlReader = new YAMLReader();
			yamlReader.readYAML(YAML, AllData);
		}
		if(DataTable!=null) {
			DataTableReader DTReader = new DataTableReader();
			DTReader.readDataTable(DataTable,AllData);
		}
		if(StringUtils.isNotEmpty(Properties)) {
			
		PropertiesReader propReader = new PropertiesReader();
		propReader.readProperties(Properties,AllData);
		}
		return AllData;
	}
	public static void main(String args[]) {
		DataReader dr = new DataReader();
		System.setProperty("Column1", "value13");
		System.setProperty("Column11", "value11");
		System.out.println(dr.readAllDataSaveToHashMap("Scenario1.json","Scenario1.yaml",null,"Column1,Column11"));
	}
	
	/*********************************************************************************************************
	 * @Objective: To read entire IExcel file and store in Map
	 * @Parameters: IExcel File, Map to Update
	 * @author: PJ
	 * @throws IOException 
	 * @throws Exception 
	 * @Date: 15-11-2021
	 * @Updated by and when:
	 **********************************************************************************************************/
	public void readIExcelAndSaveToMap(String IExcel,HashMap<String, HashMap<String, HashMap<String, String>>> Map) throws Exception {
		FileInputStream inputStream = null;Workbook wb=null;
		ExcelReader excelReader = new ExcelReader();
	        try {
	        	inputStream = new FileInputStream(IExcel);	        	
	            wb = excelReader.openWorkbook(inputStream);
	            
	            int numWS = wb.getNumberOfSheets();	             
	            System.out.println("Number of Sheets >>> " + numWS);
	            //Constants.logger.log(Level.INFO, "Number of Sheets >>> " + numWS);

	            for(int i=0;i<numWS;i++)
	            {
                    HashMap<String, HashMap<String, String>> eachWorkSheetData = new HashMap<String, HashMap<String, String>>();
	               
                    Sheet eachWorkSheet = wb.getSheetAt(i);
                                       	
	                System.out.println("######## Reading of Data from Worksheet STARTED for >> " + wb.getSheetName(i));
                    //Constants.logger.log(Level.FINE, "######## Reading of Data from Worksheet STARTED for >> " + wb.getSheetName(i));
             
                    //Reading all the Rows data 
                    int rowCnt = eachWorkSheet.getLastRowNum();
                    int colCnt = eachWorkSheet.getRow(0).getLastCellNum();
                    int scenarioColumn = 0;
                    
                    //Reading Each Row Data                       
                    for(int rowItr=1;rowItr<=rowCnt;rowItr++)
                    {                         
                          //Map to Save Each Row Data
                          HashMap<String, String> objMeta = new HashMap<String, String>() ;                                             
                    
                          for(int colItr=1;colItr<colCnt;colItr++)
                          {                                
                                 //Saving Each Scenario's Field Name and Data into Hashmap
                                 objMeta.put(eachWorkSheet.getRow(0).getCell(colItr).toString(), eachWorkSheet.getRow(rowItr).getCell(colItr).toString());
                          }      
                          
                          //Adding each Scenario Data into WorkSheet
                          eachWorkSheetData.put(eachWorkSheet.getRow(rowItr).getCell(scenarioColumn).toString(), objMeta);                                                  
                    }
                    
                    //Saving each worksheet Data Hashmap
                    Map.put(wb.getSheetName(i), eachWorkSheetData);
                    
	                System.out.println("******* Reading of Data from Worksheet Completed for >> " + wb.getSheetName(i));
                    //Constants.logger.log(Level.FINE,"******* Reading of Data from Worksheet Completed for >> " + wb.getSheetName(i));

	            }
                System.out.println("******* Reading of Data from Worksheet Completed********");
        
                     
	       } catch (Exception e) {
			e.printStackTrace();
			throw e;
		} 
	       finally {
	    	   try {
	    		   wb.close();
		    	   excelReader.closeFIS(inputStream);
				}
				catch(Exception e) {				
				}
	    	  
			}
    }
	/*********************************************************************************************************
	 * @Objective: To get the testdata from stored Map 
	 * @Parameters: Sheet ,Scenario ,Column, Map (which map to read from)
	 * @Output- 
	 * @author: PJ
	 * @throws Exception 
	 * @Date: 11-11-2021
	 * @Updated by and when:
	 **********************************************************************************************************/
	public String getTestDataFromMap(String Sheet,String Scenario,String Column, HashMap<String, HashMap<String, HashMap<String, String>>> Map) throws Exception
	{
		String requiredTestData = null;
		try {			
			
			requiredTestData = Map.get(Sheet).get(Scenario).get(Column);		
//			System.out.println("###Test Data for 'Sheet --> " + Sheet + " 'Scenario' --> " + Scenario + " 'Column' --> " + Column + "is >>>" + requiredTestData);	
			
			if( constants.getExtentNode()!=null && !Column.toLowerCase().contains("password") && StringUtils.isNotEmpty(requiredTestData) && !requiredTestData.equalsIgnoreCase("#skip#")&& !Sheet.equals("TestCaseInfo")) {
				if(requiredTestData.contains("d:m:y")) {
					//reports.ExtentReportLog(constants.getReportsPath(),constants.getExtentNode(),null, Status.INFO,"<b>TestData:</b> "+ Column +" : " + dateOperations.returnDateTime(requiredTestData),false,false);
					}
				//else if (requiredTestData.contains("dd:mm:yy")) {reports.ExtentReportLog(constants.getReportsPath(),constants.getExtentNode(),null, Status.INFO,"<b>TestData:</b> "+ Column +" : " + dateOperations.returnDateTimeNew(requiredTestData),false,false);}
				else {
					//reports.ExtentReportLog(constants.getReportsPath(),constants.getExtentNode(),null, Status.INFO,"<b>TestData:</b> "+ Column +" : " + requiredTestData,false,false);}
				}
				
			}			
		} catch (Exception e) {
//			Keep this catch block commented unless needed for debugging
//			System.out.println("ERROR : Please check Test Data Sheet for 'Class Name --> " + Sheet + " 'Scenario Name' --> " + Scenario + " 'Column Name' --> " + Column);
//			throw e;
		}
		finally {
			if(StringUtils.isEmpty(requiredTestData))requiredTestData="#skip#";
		}
		return requiredTestData;
	}
	
	/*********************************************************************************************************
	 * @Objective: To put the testdata to stored Map 
	 * @Parameters: Sheet ,Scenario,Column,data, Map(which map to put data)
	 * @Output- 
	 * @author: PJ
	 * @Date: 11-15-2021
	 * @Updated by and when:
	 **********************************************************************************************************/
	public void putTestDataToMap(String Sheet,String Scenario,String Column,String data, HashMap<String, HashMap<String, HashMap<String, String>>> MAP)
	{
		try {			
			if(!MAP.containsKey(Sheet))MAP.put(Sheet, new HashMap<String, HashMap<String, String>>());
			if(!MAP.get(Sheet).containsKey(Scenario))MAP.get(Sheet).put(Scenario, new HashMap<String, String>());
			MAP.get(Sheet).get(Scenario).put(Column,data);	
			System.out.println("###Test Data "+MAP.getClass().getSimpleName()+ " updated for 'Sheet --> " + Sheet  + " 'Scenario' --> " + Scenario+ " 'Column Name' --> " + Column + "with >>>" + data);
			//Constants.logger.log(Level.FINE, "###Test Data "+MAP.getClass().getSimpleName()+ " updated for 'Sheet --> " + Sheet  + " 'Scenario' --> " + Scenario+ " 'Column Name' --> " + Column + "with >>>" + data);
			
		} catch (Exception e) {
			//Logger
			System.out.println("ERROR : Please check Test Data Sheet for 'Sheet --> " + Sheet  + " 'Scenario' --> " + Scenario+ " 'Column' --> " + Column);
			//Constants.logger.log(Level.INFO,  "ERROR : Cell Mismatch for 'Sheet --> " + Sheet  + " 'Scenario' --> " + Scenario+ " 'Column' --> " + Column);
			e.printStackTrace();
			//Constants.logger.log(Level.SEVERE, "This message describes an Exception ", e);
			throw e;
		}
		
	}
}

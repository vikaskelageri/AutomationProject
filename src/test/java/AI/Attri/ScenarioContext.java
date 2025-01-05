package AI.Attri;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


import Ai.Attri.Library.DataReader;

public class ScenarioContext {
	private  HashMap<String, String> ScenarioConstants;
	private HashMap<String, HashMap<String, HashMap<String, String>>> IMap;
	private DataReader dataReader;
	Properties prop;Properties userProp;
	TestContext testContext;
	private  HashMap<String, String> Users;

    public ScenarioContext(){
    	prop = new Properties();userProp=new Properties();
    	ScenarioConstants = new  HashMap<String, String>();Users = new  HashMap<String, String>();
    	IMap = new HashMap<String, HashMap<String, HashMap<String, String>>>();
    	dataReader = new DataReader();
    }
	/**
	 * @objective: To set the full test data from file into IMap 
	 * @author: PJ
	 * @date: 4-26-2022
	 */
    public void setIMap(String FilePath) throws Exception {
    	dataReader.readIExcelAndSaveToMap(FilePath, IMap);
    }
    
	/**
	 * @objective: To get the full IMap 
	 * @author: PJ
	 * @date:5-04-2022
	 */
    public HashMap<String, HashMap<String, HashMap<String, String>>> getIMap(){
    	return IMap;
    }
    
    /**
     * @objective: To get a test data from stored IMap 
	 * @author: PJ
	 * @date: 4-26-2022
     * @return a test data
     */
    public String getTestDataFromIMap(String Sheet, String Scenario, String Column) throws Exception {
    	return dataReader.getTestDataFromMap(Sheet, Scenario, Column, IMap);
    }
    
    /**
     * @objective: To put a test data in IMap 
	 * @author: PJ
	 * @date: 4-26-2022
     */
    public void putTestDataToIMap(String Sheet, String Scenario, String Column,String Data) throws Exception {
    	dataReader.putTestDataToMap(Sheet, Scenario, Column, Data, IMap);
    }

	public void setScenarioConstants() throws FileNotFoundException, IOException {
		// Loading properties files
		prop.load(new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\Ai.Attri\\PROPERTIES\\Scenario.properties"));
		prop.load(new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\Ai.Attri\\PROPERTIES\\Login.properties"));
		prop.load(new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\Ai.Attri\\PROPERTIES\\DataBaseConnection.properties"));

		// Populating ScenarioConstants map
		for (Object key : prop.keySet()) {
			ScenarioConstants.put(key.toString(), prop.get(key).toString());
		}
	}



	public String getScenarioConstant(String key) {
    	return ScenarioConstants.get(key);
    }
    

    public void putScenarioConstant(String key, String Value) {
    	ScenarioConstants.put(key, Value);
    }
       
    public String getScenario(int count) {
    	String suffix = "";
		if (count > 1 ) {
		suffix = "_" + count;
		}
		String scenario = ScenarioConstants.get("ScenarioName")+suffix;
		return scenario;
	}

    public String getScenarios(int count1, int count2) {
    	String suffix = "";
		suffix = "_" + count1+"_"+count2;
		String scenario = ScenarioConstants.get("ScenarioName")+suffix;
		return scenario;
	}
    
    public ArrayList<String> getScenarioList(String scenarioName, String sheet) {
    	ArrayList<String> matchedScenarios = new ArrayList<String>();
    	for(String scenario:IMap.get(sheet).keySet()){
    		if(scenario.startsWith(scenarioName)) matchedScenarios.add(scenario);
    		}
    		return matchedScenarios;
	}


	public void setUsers() throws FileNotFoundException, IOException {
		// Loading the Users properties file
		userProp.load(new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\Ai.Attri\\PROPERTIES\\Users.properties"));

		// Populating the Users map
		for (Object key : userProp.keySet()) {
			Users.put(key.toString(), userProp.get(key).toString());
		}
	}

    

    public HashMap<String,String> getUsers() {
    	return Users;
    }
	
    public String getScenarioSuffix(String suffix) {  
    	return ScenarioConstants.get("ScenarioName")+"_"+suffix;
	}
	
}

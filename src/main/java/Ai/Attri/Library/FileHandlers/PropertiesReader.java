package Ai.Attri.Library.FileHandlers;


import java.util.HashMap;

public class PropertiesReader {
	public PropertiesReader(){
		
	}
	/**
	 * 
	 * @param Properties
	 * @param Data
	 */
	public void readProperties(String Properties,HashMap<String,String> Data) {
		for(String key :Properties.split(",")) {
			Data.put(key, System.getProperty(key));
		}
	}
	
}

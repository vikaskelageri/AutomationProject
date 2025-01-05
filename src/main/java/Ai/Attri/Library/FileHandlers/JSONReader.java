package Ai.Attri.Library.FileHandlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONReader {
	public static String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\com\\arisglobal\\LSHE2\\JSON\\";
	public JSONReader(){
		
	}
	/**
	 * 
	 * @param Filename
	 * @param Data
	 */
	public void readJSON(String Filename,HashMap<String,String> Data) {
		try 
		 
        {
			/*
			 * //Read JSON file Object obj = jsonParser.parse(reader);
			 * 
			 * JSONArray employeeList = (JSONArray) obj; System.out.println(employeeList);
			 * 
			 * //Iterate over employee array employeeList.forEach( emp ->
			 * parseEmployeeObject( (JSONObject) emp ) );
			 */
			File file = new File(filePath+Filename);
			String fileContents = FileUtils.readFileToString(file);
		    
			/*
			 * //jackson databind
			 * ObjectMapper objectMapper = new ObjectMapper(); Map<String, String> map =
			 * objectMapper.readValue(fileContents, Map.class); System.out.println("Map is "
			 * + map); System.out.println("Map Size is " + map.size());
			 */
			
		    //org.json
		    JSONObject obj = new JSONObject(fileContents);
//		    System.out.println("org.JSON Key Set is " + obj.keySet());
//		    System.out.println("org.JSON Map is " + obj.toMap());
		    for(String key:obj.keySet()) {
		    	Data.put(key, obj.getString(key));
		    }
			
			/*
			 * //json simple JSONParser jsonParser = new JSONParser();
			 * org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject)
			 * jsonParser.parse(fileContents);
			 * System.out.println(jsonObject.keySet()+""+jsonObject.entrySet());
			 */
		    
        } catch (IOException e) {
            e.printStackTrace();
		} /*
			 * catch (ParseException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
	}
	/*
	 * public static void main(String args[]) { JSONReader json = new JSONReader();
	 * json.readJSON("", null); }
	 */
}

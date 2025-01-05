package Ai.Attri.Library.FileHandlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;

public class YAMLReader {
	public static String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\com\\arisglobal\\LSHE2\\YAML\\";
	public YAMLReader() {
		
	}
	/**
	 * 
	 * @param YAML
	 * @param Data
	 */
	public void readYAML(String YAML,HashMap<String,String> Data) {
		
		
		try {
			File file = new File(filePath+YAML);
			String fileContents = FileUtils.readFileToString(file);
			Yaml yaml = new Yaml();
			Map<String, Object> data = yaml.load(fileContents);
			
			for(String key : data.keySet()){
				Data.put(key, data.get(key).toString()); 
				}
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
		 
	}
}

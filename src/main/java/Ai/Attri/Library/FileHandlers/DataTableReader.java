package Ai.Attri.Library.FileHandlers;


import java.util.HashMap;

public class DataTableReader {
	public DataTableReader(){
		
	}
	/**
	 * 
	 * @param DataTable
	 * @param Data
	 */
	public void readDataTable(HashMap<String,String> DataTable,HashMap<String,String> Data) {
		for(String key :DataTable.keySet()) {
			Data.put(key, DataTable.get(key));
		}
	}
	
}

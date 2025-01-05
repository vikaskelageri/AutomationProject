package Ai.Attri.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtil {
	
/**
 * @objective: To run DB Query
 * @author: PJ
 * @throws SQLException 
 * @date: 4-26-2022
 */
	public String runDBQuery(String DB_URL,String USER, String PASS, String QUERY) throws SQLException {
		
		String result = "";
		  try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			  Statement stmt = conn.createStatement(); ResultSet rs =
					  stmt.executeQuery(QUERY);) {  
			  while (rs.next()) { 
				  result = rs.getString(1); break;
					   
					  }
		  }
		   catch  (SQLException e)
		   { throw e; }		 
			 return result;
	}
	

	/**********************************************************************************************************
	 * @Objective:To Connect to MySql Database
	 * @InputParameters:Database Name,Username,Password as String
	 * @OutputParameters:Database connection object
	 * @author:Adarsh
	 * @throws IOException 
	 * @Date : 23-Dec-2019
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public static Connection getDBConnection(String DBName, String DBUsername, String DBPassword) throws IOException {
		InputStream input = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\test\\resources\\com\\arisglobal\\LSHE2\\PROPERTIES\\DataBaseConnection.properties");
		Properties prop = new Properties();
		prop.load(input);
		
		String pbidb=prop.getProperty("pbidb");
        String pbiuser=prop.getProperty("pbiuser").trim();
        String pbipassword=prop.getProperty("pbipassword").trim();
        String pbihost=prop.getProperty("pbihost");
		  
		Connection con = null;
		try {
	         Class.forName("org.postgresql.Driver");
	         con = DriverManager.getConnection("jdbc:postgresql://"+pbihost+":5432/"+pbidb+"",pbiuser,pbipassword);
	      } catch (Exception e)
			{ e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
			}
	      	System.out.println("Opened database successfully");
		return con;
	}

	/**********************************************************************************************************
	 * @Objective:To execute Read query on specified Database
	 * @InputParameters:Database connection object, query as String
	 * @OutputParameters:result set object
	 * @author:Adarsh
	 * @Date : 23-Dec-2019
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public static ResultSet performRead(Connection con, String readQuery) {

		ResultSet rs = null;

		try {
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(readQuery);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return rs;
	}

	/**********************************************************************************************************
	 * @Objective:To execute update query on specified Database
	 * @InputParameters:Database connection object, query as String
	 * @OutputParameters:NA
	 * @author:Adarsh
	 * @Date : 23-Dec-2019
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/

	public static void performWrite(Connection con, String writeQuery) {

		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(writeQuery);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}

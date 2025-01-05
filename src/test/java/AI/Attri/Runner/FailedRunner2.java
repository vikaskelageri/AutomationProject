//package com.arisglobal.LSHE2.Runner;
//
//import io.cucumber.junit.Cucumber;
//import io.cucumber.junit.CucumberOptions;
//import org.junit.runner.RunWith;
//
//@RunWith(Cucumber.class)
////@RunWith(CucumberWithSerenity.class)
//@CucumberOptions(
//monochrome=true
//,features = "@target/failedrerun.txt"
//,glue={"classpath:com.arisglobal.LSHE2.StepDefinitions"}
//,plugin = {})
//public class FailedRunner2 {
//
//}
/*
package com.arisglobal.LSHE2.Runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.runner.RunWith;

import com.arisglobal.StepDefinitions.Attri.Hooks;

@RunWith(Cucumber.class)
// @RunWith(CucumberWithSerenity.class)
@CucumberOptions(monochrome = true, features = "@target/failedrerun.txt", glue = {
		"classpath:com.arisglobal.LSHE2.StepDefinitions" }, plugin = {})

public class FailedRunner2 {
	static Properties prop = new Properties();
	public static void main(String[] args) {
		Properties properties = new Properties();

		try {
			// Load properties from the file
//			FileInputStream fileInputStream = new FileInputStream("DataBaseConnection.properties");
//			properties.load(fileInputStream);
//			fileInputStream.close();		
			prop.load(new FileInputStream(System.getProperty("user.dir")
					+ "\\src\\test\\resources\\com\\arisglobal\\LSHE2\\PROPERTIES\\DataBaseConnection.properties"));
			

			// Update the properties and save them back to the file
			properties.setProperty("PBI_Run", "YES");
			properties.setProperty("PBI_ReRun", "YES");
			FileOutputStream fileOutputStream = new FileOutputStream("DataBaseConnection.properties");
			properties.store(fileOutputStream, null);
			fileOutputStream.close();

			System.out.println("Properties updated successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}*/
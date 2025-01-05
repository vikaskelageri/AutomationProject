package AI.Attri.StepDefinitions.REST;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;

import AI.Attri.TestContext;
import Ai.Attri.Library.CustomMaps;
import Ai.Attri.Utils.FileSystemOperations;
import Ai.Attri.Utils.HTTPUtil;
import com.jayway.jsonpath.JsonPath;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class RESTSteps {

	TestContext testContext;
	HTTPUtil httpUtil;
	CustomMaps recordIDs = new CustomMaps();
	Random random = new Random();
	FileSystemOperations fileSystemOperations;
	String WS_USER = null;
	String WS_PASSWORD = null;
	String WS_URL = null;

	// long timeStamp = Calendar.getInstance().getTime().getTime();
	long timeStamp = 1;

	public RESTSteps(TestContext context) throws IOException {
		testContext = context;
		httpUtil = new HTTPUtil(context.getWebDriverManager().getDriver());
		fileSystemOperations = new FileSystemOperations();
	}

	public String getScenario(int count) {
		return testContext.getScenarioContext().getScenario(count);
	}

	public String getLoginScenario(String loginScenarioName) {
		String scenario = testContext.scenarioContext.getScenarioConstant("ScenarioName") + "_" + loginScenarioName;
		return scenario;
	}

	// This method will take input in string format(JSON Converted to String)
	@When("Send POST Request for Expected Message {string}")
	public void sendPostRequestAsString(String ExpectedMessage) throws Exception {
		HashMap<String, String> Headers = new HashMap<String, String>();
		Headers.put("_contentType", "json");
		String WS_URL = testContext.scenarioContext.getScenarioConstant("WS_URL");
		String WS_USER = testContext.scenarioContext.getScenarioConstant("WS_USER");
		String WS_PASSWORD = testContext.scenarioContext.getScenarioConstant("WS_PASSWORD");
		String JsonString = testContext.scenarioContext.getScenarioConstant("jsonString");
		String response = httpUtil.sendPOSTJSON(WS_URL, JsonString, Headers, WS_USER, WS_PASSWORD);
		System.out.println("REQUEST:" + JsonString);
		System.out.println("RESPONSE:" + response);
		Assert.assertEquals(ExpectedMessage, httpUtil.fetchContentViaRegex(response, "\"status\": \"(.*?)\"", 1));
		testContext.scenarioContext.putScenarioConstant("WS_RESPONSE", response);
		httpUtil.responseCode(response, ExpectedMessage);
	}

	@When("Save {int} recordIdMap to scenario contant")
	public void saveRecordIdMap(int count) throws Exception {
		String response = testContext.scenarioContext.getScenarioConstant("WS_RESPONSE");
		String recordIdMap = httpUtil.fetchContentViaRegex(response, "\"recordIdMap\": \"(.*?)\"", 1);
		System.out.println(recordIdMap);
		testContext.scenarioContext.putScenarioConstant("recordIdMap", recordIdMap);

	}

	@Given("Get all the record Id to create a product")
	public void get_all_the_record_id_to_create_a_product(io.cucumber.datatable.DataTable dataTable)
			throws IOException {
		HashMap<String, String> Headers = new HashMap<String, String>();
		Headers.put("_contentType", "json");
		String WS_URL = testContext.scenarioContext.getScenarioConstant("WS_URL_GETCODELIST");
		String WS_USER = testContext.scenarioContext.getScenarioConstant("WS_USER");
		String WS_PASSWORD = testContext.scenarioContext.getScenarioConstant("WS_PASSWORD");
		for (Map<String, String> data : dataTable.asMaps(String.class, String.class)) {
			String response = httpUtil.httpGET(WS_URL + data.get("CODELIST"), Headers, WS_USER, WS_PASSWORD);
			System.out.println("RESPONSE:" + response);
			List<String> productNames = JsonPath.parse(response.toString()).read(
					"$.agl_result[*].CodeList.codeListCodes[*].CodeListCode.codeListDecodes[*].CodeListDecode.decode");
			List<String> productRecordId = JsonPath.parse(response.toString())
					.read("$.agl_result[*].CodeList.codeListCodes[*].CodeListCode.recordId");
			for (int i = 0; i < productNames.size(); i++) {
				if (data.get("VALUE").toString().equalsIgnoreCase(productNames.get(i).toString())) {
					recordIDs.tempMap.put(productNames.get(i), productRecordId.get(i));
				}
			}
			testContext.scenarioContext.putScenarioConstant("WS_RESPONSE", response);
		}
		for (Map.Entry<String, String> entry : recordIDs.tempMap.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

	@Given("Get all the record Id")
	public void get_all_the_record_id(io.cucumber.datatable.DataTable dataTable) throws IOException {
		HashMap<String, String> Headers = new HashMap<String, String>();
		Headers.put("_contentType", "json");
		String WS_URL = testContext.scenarioContext.getScenarioConstant("WS_URL_GETCODELIST");
		String WS_USER = testContext.scenarioContext.getScenarioConstant("WS_USER");
		String WS_PASSWORD = testContext.scenarioContext.getScenarioConstant("WS_PASSWORD");
		for (Map<String, String> data : dataTable.asMaps(String.class, String.class)) {
			String codeList = data.get("CODELIST");
			String[] codeListSplit = codeList.split(":");
			String codeListSplitpart0 = codeListSplit[0];
			String codeListSplitpart1 = codeListSplit[1];

			String response = httpUtil.httpGET(WS_URL + codeListSplitpart0, Headers, WS_USER, WS_PASSWORD);
			System.out.println("RESPONSE:" + response);
			// List<String> productNames = JsonPath.parse(response.toString())
			// .read("$.agl_result[*].CodeList.codeListCodes[*].CodeListCode.codeListDecodes[*].CodeListDecode.decode");
			List<String> productNames = JsonPath.parse(response.toString())
					.read("$.agl_result[*].CodeList.codeListCodes[*].CodeListCode.code");
			List<String> productRecordId = JsonPath.parse(response.toString())
					.read("$.agl_result[*].CodeList.codeListCodes[*].CodeListCode.recordId");
			for (int i = 0; i < productNames.size(); i++) {
				if (codeListSplitpart1.toString().equalsIgnoreCase(productNames.get(i).toString())) {
					recordIDs.tempMap.put(codeListSplitpart0 + ":" + productNames.get(i),
							codeListSplitpart0 + ":" + productRecordId.get(i));
				}
			}
			testContext.scenarioContext.putScenarioConstant("WS_RESPONSE", response);
		}
		for (Map.Entry<String, String> entry : recordIDs.tempMap.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

	@Given("Get all the record Id Environment {string}")
	public void get_all_the_record_id_customer(String user, DataTable dataTable) throws Exception {
		HashMap<String, String> Headers = new HashMap<String, String>();
		Headers.put("_contentType", "json");
		WS_USER = testContext.scenarioContext.getScenarioConstant("WS_USER" + "_" + user);
		WS_PASSWORD = testContext.scenarioContext.getScenarioConstant("WS_PASSWORD" + "_" + user);
		WS_URL = testContext.scenarioContext.getScenarioConstant("WS_URL_GETCODELIST" + "_" + user);
		for (Map<String, String> data : dataTable.asMaps(String.class, String.class)) {
			String codeList = data.get("CODELIST");
			String[] codeListSplit = codeList.split(":");
			String codeListSplitpart0 = codeListSplit[0];
			String codeListSplitpart1 = codeListSplit[1];

			String response = httpUtil.httpGET(WS_URL + codeListSplitpart0, Headers, WS_USER, WS_PASSWORD);
			System.out.println("RESPONSE:" + response);
			// List<String> productNames = JsonPath.parse(response.toString())
			// .read("$.agl_result[*].CodeList.codeListCodes[*].CodeListCode.codeListDecodes[*].CodeListDecode.decode");
			List<String> productNames = JsonPath.parse(response.toString())
					.read("$.agl_result[*].CodeList.codeListCodes[*].CodeListCode.code");
			List<String> productRecordId = JsonPath.parse(response.toString())
					.read("$.agl_result[*].CodeList.codeListCodes[*].CodeListCode.recordId");
			for (int i = 0; i < productNames.size(); i++) {
				if (codeListSplitpart1.toString().equalsIgnoreCase(productNames.get(i).toString())) {
					recordIDs.tempMap.put(codeListSplitpart0 + ":" + productNames.get(i),
							codeListSplitpart0 + ":" + productRecordId.get(i));
				}
			}
			testContext.scenarioContext.putScenarioConstant("WS_RESPONSE", response);
		}
		for (Map.Entry<String, String> entry : recordIDs.tempMap.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

	@When("Send POST Request for Environment Expected Message {string} {string}")
	public void sendPostRequestAsString_Environment(String ExpectedMessage, String user) throws Exception {
		HashMap<String, String> Headers = new HashMap<String, String>();
		Headers.put("_contentType", "json");

		String JsonString = testContext.scenarioContext.getScenarioConstant("jsonString");
		WS_URL = testContext.scenarioContext.getScenarioConstant("WS_URL" + "_" + user);

		String response = httpUtil.sendPOSTJSON(WS_URL, JsonString, Headers, WS_USER, WS_PASSWORD);
		System.out.println("REQUEST:" + JsonString);
		System.out.println("RESPONSE:" + response);
		Assert.assertEquals(ExpectedMessage, httpUtil.fetchContentViaRegex(response, "\"status\": \"(.*?)\"", 1));
		testContext.scenarioContext.putScenarioConstant("WS_RESPONSE", response);
		httpUtil.responseCode(response, ExpectedMessage);
	}

	/**********************************************************************************************************
	 * Objective:This method is to update the Product Creation JSON Input
	 * Parameters: Output Parameters: NA
	 * 
	 * @author: Girish Date : 14-DEC-2022 Updated by and when:
	 **********************************************************************************************************/
	@When("Update {int} Product json file {string} for Product Creation {string}")
	public void updateProductJson_Values_Env(int count, String JsonName, String Version, DataTable dt)
			throws Exception {
		timeStamp = Calendar.getInstance().getTime().getTime();
		for (Map.Entry<String, String> entry : recordIDs.tempMap.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		String JSON = testContext.scenarioContext.getScenarioConstant("JSONPath") + JsonName;
		File file = new File(JSON);
		String result = httpUtil.readJSONAsString(file);
		// String prdName = "PRD-1" + random.nextInt(100000);
		String prdName = "PRD-" + timeStamp;
		result = result.replace("\"${preferredProductName}\"", "\"" + prdName + "\"");
		result = result.replace("\"${drugProductUID}\"", "\"" + prdName + "\"");
		result = result.replace("\"${productDescription}\"", "\"" + prdName + "\"");
		String ENV = testContext.scenarioContext.getScenarioConstant("Environmet");
		System.out.println(ENV);
		Version = testContext.scenarioContext.getScenarioConstant("Version" + "_" + ENV);
		result = result.replace("\"${applicationVersion}\"", "\"" + Version + "\"");
		for (Map<String, String> data : dt.asMaps(String.class, String.class)) {

			String key = data.get("KEY");// $deviceType.recordId
			String codeList = data.get("CODELIST");// Simple Product
			// if (codeList.startsWith("clv_")) {
			// String newcodeList=codeList.substring(4) ;
			// result = result.replace("\"${" + key + "}\"", "\"" +
			// recordIDs.tempMap.get(newcodeList) + "\"");
			// }
			if (key.endsWith(".recordId")) {
				codeList = recordIDs.tempMap.get(codeList);
				String[] codeListSplit = codeList.split(":");
				String codeListSplitpart0 = codeListSplit[0];
				String codeListSplitpart1 = codeListSplit[1];
				result = result.replace("\"${" + key + "}\"", "\"" + codeListSplitpart1 + "\"");
			}

			else if (key.endsWith("Version")) {
				// String Version =
				// testContext.scenarioContext.getTestDataFromIMap("LSRIMSLogin",
				// getLoginScenario(user), "Version");
				String ENV1 = testContext.scenarioContext.getScenarioConstant("Environmet");
				System.out.println(ENV1);
				Version = testContext.scenarioContext.getScenarioConstant("Version" + "_" + ENV1);
				result = result.replace("\"${" + key + "}\"", "\"" + Version + "\"");
			} else {
				result = result.replace("\"${" + key + "}\"", "\"" + codeList + "\"");
			}
		}

		System.out.println("******* Updated JSON *******");
		System.out.println(result);
		testContext.scenarioContext.putTestDataToIMap("Product_Marketed_BasicInfo", getScenario(count),
				"UpdatedProductName", prdName);
		count = random.nextInt(10000) + random.nextInt(1000);
		String fileName = testContext.scenarioContext.getScenarioConstant("updatedJson").replace("%s",
				Integer.toString(count));
		PrintWriter out = new PrintWriter(fileName);
		out.println(result);
		testContext.scenarioContext.putScenarioConstant("jsonString", result);
		out.close();
		String nameOfTheFile = "updated" + count + ".json";
		fileSystemOperations.moveFile(nameOfTheFile, testContext.scenarioContext.getScenarioConstant("DownloadFolder"),
				testContext.scenarioContext.getScenarioConstant("ReportsPath"));
		httpUtil.reportLink(nameOfTheFile);

	}

}

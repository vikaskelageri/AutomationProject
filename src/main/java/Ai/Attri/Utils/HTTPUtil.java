package Ai.Attri.Utils;

import java.awt.AWTException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.WebDriver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import Ai.Attri.Library.Constants;
import Ai.Attri.Library.Reports;
import com.aventstack.extentreports.Status;

public class HTTPUtil {
	
	Constants constants;
	Reports reports;
	WebDriver driver;


	public HTTPUtil(WebDriver driver) {
		this.driver = driver;
		constants = new Constants();
		reports = new Reports();
	}


	public String sendPOSTJSON(String url, File jsonFile, HashMap<String, String> Headers,
			String Username, String Password) throws IOException {

		String result = "";
		HttpPost post = new HttpPost(url);

		for (String key : Headers.keySet()) {
			post.addHeader(key, Headers.get(key));
		}
		post.addHeader("Authorization", getAuthorizationHeaderFor(Username, Password));

		StringEntity stringEntity = new StringEntity(readJSONAsString(jsonFile));
		post.setEntity(stringEntity);

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(post)) {

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			throw e;
		}
		return result;
	}
	
	public String sendPOSTJSON(String url, String jsonFileAsString, HashMap<String, String> Headers,
			String Username, String Password) throws IOException {

		String result = "";
		HttpPost post = new HttpPost(url);

		for (String key : Headers.keySet()) {
			post.addHeader(key, Headers.get(key));
		}
		post.addHeader("Authorization", getAuthorizationHeaderFor(Username, Password));

		StringEntity stringEntity = new StringEntity(jsonFileAsString);
		post.setEntity(stringEntity);

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(post)) {

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			throw e;
		}
		return result;
	}

	public String sendPOSTJSONAsFile(String url, File jsonFile, String jsonFileParam, HashMap<String, String> Headers,
			String Username, String Password) throws IOException {

		String result = "";
		HttpPost post = new HttpPost(url);

		for (String key : Headers.keySet()) {
			post.addHeader(key, Headers.get(key));
		}
		post.addHeader("Authorization", getAuthorizationHeaderFor(Username, Password));

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addPart(jsonFileParam, new FileBody(jsonFile));
		post.setEntity(builder.build());

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(post)) {

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			throw e;
		}
		return result;
	}

	public String httpDELETE(String url, HashMap<String, String> Headers, String Username, String Password)
			throws IOException {

		String result = "";
		HttpDelete post = new HttpDelete(url);

		for (String key : Headers.keySet()) {
			post.addHeader(key, Headers.get(key));
		}
		post.addHeader("Authorization", getAuthorizationHeaderFor(Username, Password));

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(post)) {

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);

			}
		} catch (IOException e) {
			throw e;
		}
		return result;
	}
	
	public String httpGET(String url, HashMap<String, String> Headers, String Username, String Password)
			throws IOException {

		String result = "";
		HttpGet get = new HttpGet(url);

		for (String key : Headers.keySet()) {
			get.addHeader(key, Headers.get(key));
		}
		get.addHeader("Authorization", getAuthorizationHeaderFor(Username, Password));

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);

			}
		} catch (IOException e) {
			throw e;
		}
		return result;
	}

	public String fetchContentViaRegex(String Text, String Regex, int group) {
		String text = Text;
		Pattern p = Pattern.compile(Regex);
		Matcher m = p.matcher(text);
		String out = "";
		while (m.find()) {
			out = m.group(group);
		}
		return out;
	}

	public String getXMLValue(String XML, String xpathExpression) throws XPathExpressionException,
			ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		factory.setNamespaceAware(true);
		Document doc = builder.parse(new ByteArrayInputStream(XML.getBytes("UTF-8")));
		XPath xPath = XPathFactory.newInstance().newXPath();

		Node node = (Node) xPath.compile(xpathExpression).evaluate(doc, XPathConstants.NODE);

		if (node == null) {
			return null;
		} else {
			return node.getTextContent();
		}
	}

	private String getAuthorizationHeaderFor(String username, String password) {
		String authString = username + ":" + password;
		byte[] authStringInBytes = Base64.encodeBase64(authString.getBytes());
		return "Basic " + new String(authStringInBytes);
	}

	public String DecodeBase64(String encodedString) {
		byte[] decodedBytes = java.util.Base64.getDecoder().decode(encodedString);
		String decodedString = new String(decodedBytes);
		return decodedString;
	}

	public String readJSONAsString(File file) throws IOException {
		return FileUtils.readFileToString(file);
	}
	
	public void reportLink(String fileName) throws IOException, AWTException, InterruptedException {
		reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(), driver, Status.INFO,
				"<a href='" + fileName + "'> click here To view the file </a> : "+ fileName, false,
				constants.CaptureDesktopScreenshot());

	}
	
	public void dataUpdated(String field,String data) throws IOException, AWTException, InterruptedException {
		reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(), driver,
				Status.INFO, "Test data successfully updated for " + field +" with the value " + data,
				false, constants.CaptureDesktopScreenshot());
	}
	
	public void responseCode(String responsecode, String expectedMsg) throws IOException, AWTException, InterruptedException {
		reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(), driver,
				Status.INFO, expectedMsg+", "+ "Response code: " + responsecode,
				false, constants.CaptureDesktopScreenshot());
	}
	

}
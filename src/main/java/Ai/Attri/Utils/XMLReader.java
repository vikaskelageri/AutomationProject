package Ai.Attri.Utils;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class XMLReader {
	/**********************************************************************************************************
	 * @Objective:To get R3 tag value from specified xml file
	 * @InputParameters:file path as String, xpath expression picked using
	 *                       'http://xpather.com' as String
	 * @OutputParameters:node text as String
	 * @author:Suraj
	 * @throws XPathExpressionException 
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 * @throws IOException 
	 * @Date : 06-21-2019
	 * @UpdatedByAndWhen: Yashwanth Naidu 18-Oct-2021
	 **********************************************************************************************************/	
	public String getR3TagValue(String filePath, String xpathExpression) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		
		Node node = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		File inputFile = new File(filePath);
		DocumentBuilder builder = factory.newDocumentBuilder();	
		factory.setNamespaceAware(true);
		Document doc = builder.parse(inputFile);
		XPath xPath = XPathFactory.newInstance().newXPath();
		node = (Node) xPath.compile(xpathExpression).evaluate(doc, XPathConstants.NODE);

		if(node==null) {
			return null;
		}else {
			return node.getTextContent();
		}
	}

	/**********************************************************************************************************
	 * @Objective:To get R2 tag value from specified xml file
	 * @InputParameters:file path as String, xpath expression picked using
	 *                       'http://xpather.com' as String
	 * @OutputParameters:node text as String
	 * @author:Suraj
	 * @throws XPathExpressionException 
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 * @throws IOException 
	 * @Date : 06-21-2019
	 * @UpdatedByAndWhen: Yashwanth Naidu 18-Oct-2021
	 **********************************************************************************************************/
	
	public String getR2TagValue(String filePath, String xpathExpression) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {

		Node node = null;
		File inputFile = new File(filePath);
		System.out.println("Input file : " + inputFile);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		EntityResolver resolver = new EntityResolver() {
			public InputSource resolveEntity(String publicId, String systemId) {
				String empty = "";
				ByteArrayInputStream bais = new ByteArrayInputStream(empty.getBytes());
				System.out.println("resolveEntity:" + publicId + "|" + systemId);
				return new InputSource(bais);
			}
		};
		builder.setEntityResolver(resolver);
		Document doc = builder.parse(inputFile);
		doc.getDocumentElement().normalize();
		XPath xPath = XPathFactory.newInstance().newXPath();
		node = (Node) xPath.compile(xpathExpression).evaluate(doc, XPathConstants.NODE);

		if(node==null) {
			return null;
		}else {
			return node.getTextContent();
		}
	}
	
	/**********************************************************************************************************
	 * @Objective: The Below Method is created to read the data from XMl files and verify with Positive and Negative scenario
	 * @InputParameters: Scenario Name, xmFilePath , xpathColumnName ,sheetName ,columnName
	 * @OutputParameters:
	 * @author:Yashwanth Naidu
	 * @return 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 * @Date : 18-Oct-2021
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public boolean readXML_Data(String scenarioName,String xmlFileName , String xmlXpath , String sheetName , String expectedXpath , boolean IsVisbleVerification) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {

		String xml_data = getR3TagValue(xmlFileName,xmlXpath);
		String expectedValue = expectedXpath;

		if(IsVisbleVerification) {
			return xml_data.equals(expectedValue);
		}else {
			return xml_data==null;
		}
	}

	/**********************************************************************************************************
	 * @Objective: The Below Method is created to read the data from XML and convert to String
	 * @InputParameters: Scenario Name, columnName
	 * @OutputParameters:
	 * @author:Yashwanth Naidu
	 * @throws Exception 
	 * @Date : 14-Oct-2021
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/	
	public String convertXmlToString(String scenarioName , String fileName) throws Exception{
		
		String xml2String =  null;
		Reader fileReader =null;
		BufferedReader bufReader = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();	
			File inputFile = new File(fileName);
			fileReader = new FileReader(inputFile);
			bufReader = new BufferedReader(fileReader);
			StringBuilder sb = new StringBuilder();
			String line = bufReader.readLine();
			while( line != null){
				sb.append(line).append("\n");
				line = bufReader.readLine();
			}
			xml2String = sb.toString();
			bufReader.close();

		} catch (Exception e) {
			throw e;
		}
		finally {
			try {
				bufReader.close();
				fileReader.close();
			}
			catch(IOException e) {				
			}
			
		}
		return xml2String;
	}
}
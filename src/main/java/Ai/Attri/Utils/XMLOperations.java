package Ai.Attri.Utils;

import javax.xml.parsers.DocumentBuilderFactory;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.openqa.selenium.WebDriver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Ai.Attri.Library.Constants;
import Ai.Attri.Library.Reports;
import com.aventstack.extentreports.Status;


public class XMLOperations {
	
	Constants constants = new Constants();
	Reports reports=new Reports();
	WebDriver driver;
	XMLReader xmlReader=new XMLReader();
	
	
	/**********************************************************************************************************
	 * @Objective: The Below Method is created to update the message number in R2
	 *             xml
	 * @InputParameters: File Name
	 * @OutputParameters:
	 * @author:Avinash k
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @Date : 12-Jul-2019
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/

	public  String updateMessageNumber(String fileName) throws ParserConfigurationException, TransformerException, IOException, SAXException {

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(fileName);
			// Node ichicsr = doc.getFirstChild();
			Node messagenumber = doc.getElementsByTagName("ichicsrmessageheader").item(0);
			NodeList list = messagenumber.getChildNodes();

			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if ("messagenumb".equals(node.getNodeName())) {
					node.setTextContent("AUT-" + System.currentTimeMillis());
				}

			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(fileName));
			transformer.transform(source, result);

			System.out.println("Done");

			//Reports.ExtentReportLog("", Status.PASS, "Message number is updated successfully", false);

		} catch (ParserConfigurationException pce) {
			throw pce;
		} catch (TransformerException tfe) {
			throw tfe;
		} catch (IOException ioe) {
			throw ioe;
		} catch (SAXException sae) {
			throw sae;
		}
		return fileName;
	}
	
	/**********************************************************************************************************
	 * @Objective: The Below Method is created to read the data from R2 XML
	 * @InputParameters: path, Xpath, data
	 * @OutputParameters:
	 * @author:Diksha
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 * @throws AWTException 
	 * @throws InterruptedException 
	 * @Date : 25-May-2022
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public boolean readData_R2XML(String path, String fieldXpath, String data)
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, AWTException, InterruptedException {

		String xml_data = "";
		boolean status = false;
		xml_data = xmlReader.getR2TagValue(path, fieldXpath);
		if (data.equalsIgnoreCase(xml_data)) {
			reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(), driver, Status.INFO,
					data + " is displayed for " + "<b>" + fieldXpath + "</b>" + " tag in the downloaded XML.", false,
					constants.CaptureDesktopScreenshot());
			status = true;
		}
		return status;

	}

	

}

package Ai.Attri.Utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Ai.Attri.Library.Constants;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

public class FileSystemOperations {
	boolean status;
	
public static String filename = System.getProperty("user.dir") + "\\src\\Data\\";

	
	
	private static XSSFWorkbook xlsxworkbook = null;
	public static FileInputStream xlsxverifyFile = null;
	public static XSSFWorkbook xlsxverifyWorkbook = null;
	private static XSSFSheet xlsxsheet = null;
	private static XSSFSheet xlsxSheet = null;
	private static XSSFRow xlsxrow = null;
	private static XSSFCell xlsxcell = null;
	
	/**********************************************************************************************************
	 * @Objective: To delete all files in specified Folder
	 * @InputParameters:folder path as String
	 * @OutputParameters:
	 * @author:Adarsh
	 * @return 
	 * @Date : 08-22-2019
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public void clearFolder(String folderPath) {

		try {
			Arrays.stream(new File(folderPath).listFiles()).forEach(File::delete);
		} catch (Exception e) {
			throw e;
		}
	}

	/**********************************************************************************************************
	 * @Objective: To create specified Folder
	 * @InputParameters:folder path
	 * @OutputParameters:
	 * @author:Adarsh
	 * @Date : 08-30-2019
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public void createFolder(String folderPath) {

		try {
			File newFolder = new File(folderPath);

			boolean created = newFolder.mkdirs();

			if (created)
				System.out.println("Folder was created !");
			else
				System.out.println("Unable to create folder");
		} catch (Exception e) {
			throw e;
		}
	}

	/**********************************************************************************************************
	 * @Objective: To move specified file from and to specified location
	 * @InputParameters:file name,source path,destination path as String
	 * @OutputParameters:
	 * @author:Adarsh
	 * @throws Exception 
	 * @Date : 08-30-2019
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public void moveFile(String fileName, String sourcePath, String destinationPath) throws Exception {

		try {
			Path temp = Files.move(Paths.get(sourcePath + "\\" + fileName),
					Paths.get(destinationPath + "\\" + fileName));

			if (temp != null) {
				System.out.println("File moved successfully");//Added by Girish
				/* 
				 * ****Below steps are commented as per the Compliance review comments****
				System.out.println("File moved successfully");
				Reports.ExtentReportLog("", Status.PASS, "File moved successfully>>> "+ destinationPath, false);
				 */
			} else {
				System.out.println("Failed to move the file");
				//				Reports.ExtentReportLog("", Status.FAIL, "Failed to move the file", false);	
			}

		} catch (Exception e) {
			throw e;
		}
	}

	public static void moveFile_Src_Dest(String fileName, String sourcePath, String destinationPath) throws Exception {

		try {
			Path temp = Files.move(Paths.get(sourcePath + "\\" + fileName),
					Paths.get(destinationPath + "\\" + fileName));

			if (temp != null) {
				System.out.println("File moved successfully");//Added by Girish
				/* 
				 * ****Below steps are commented as per the Compliance review comments****
				System.out.println("File moved successfully");
				Reports.ExtentReportLog("", Status.PASS, "File moved successfully>>> "+ destinationPath, false);
				 */
			} else {
				System.out.println("Failed to move the file");
				//				Reports.ExtentReportLog("", Status.FAIL, "Failed to move the file", false);	
			}

		} catch (Exception e) {
			throw e;
		}
	}

	public static void move_Downloadedexcel(String FileName, String extention) {
		// lsrimsConstants.LSRIMS_testDataOutput will be set as default download path
		// hence below is not required
		// String downloadedPath = System.getProperty("user.home")+ "\\" + "Downloads";
		String Resultpath = Constants.LSRIMS_Output + "\\";
//		lsrimsConstants.Resultpath = Resultpath;
//		System.out.println(lsrimsConstants.Resultpath);
//		FileSystemOperations.createFolder(Resultpath);
		if (extention.equalsIgnoreCase("xlsx")) {
			FileSystemOperations.moveFiles(FileName + ".xlsx", Constants.LSRIMS_Output, Resultpath);
			// FileSystemOperations.moveFile(FileName + ".xlsx",downloadedPath, Resultpath);
		}

		if (extention.equalsIgnoreCase("pdf")) {
			FileSystemOperations.moveFiles(FileName + ".pdf", Constants.LSRIMS_Output, Resultpath);
		}
		if (extention.equalsIgnoreCase("xlsxx")) {
			FileSystemOperations.moveFiles(FileName + ".xlsxx", Constants.LSRIMS_Output, Resultpath);
		}
		if (extention.equalsIgnoreCase("docx")) {
			FileSystemOperations.moveFiles(FileName + ".docx", Constants.LSRIMS_Output, Resultpath);
		}
	}
	private static void moveFiles(String string, String lSRIMS_Output, String resultpath) {
		// TODO Auto-generated method stub
		
	}

	/**********************************************************************************************************
	 * @Objective: To rename specified file to specified name
	 * @InputParameters:file path,currentName and newName value as String
	 * @OutputParameters:
	 * @author:Adarsh
	 * @Date : 06-02-2020
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public void renameFile(String folderPath, String currentFileName, String newFileName) {

		try {
			File file = new File(folderPath + "/" + currentFileName);
			File newFile = new File(folderPath + "/" + newFileName);
			if (file.renameTo(newFile)) {
				System.out.println("File rename success");
				//Reports.ExtentReportLog("", Status.PASS, "File rename success", false);	
			}

		} catch (Exception e) {
			throw e;
		}
	}

	/**********************************************************************************************************
	 * @Objective: To extract files from specified zip file into specified location
	 * @InputParameters:zipFilePath,destDir value as String
	 * @OutputParameters:
	 * @author:Adarsh
	 * @throws IOException 
	 * @Date : 09-29-2020
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public String unzip(String zipFilePath, String destDir) throws IOException {
		File dir = new File(destDir);
		// create output directory if it doesn't exist
		if (!dir.exists())
			dir.mkdirs();
		FileInputStream fis=null;ZipInputStream zis=null;
		String fileName = "";
		// buffer for read and write data to file
		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(zipFilePath);
			zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				fileName = ze.getName();
				File newFile = new File(destDir + File.separator + fileName);
				System.out.println("Unzipping to " + newFile.getAbsolutePath());
				// create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos =null;
				try {
					fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					
					// close this ZipEntry
					zis.closeEntry();
					ze = zis.getNextEntry();
				}
				catch(IOException e) {
					throw e;
				}
				finally {					
					try {
						fos.close();
					}
					catch(Exception e) {						
					}
					
				}
				
			}
			// close last ZipEntry
			zis.closeEntry();
			
		} catch (IOException e) {
			throw e;
		}
		finally {
			try {
				zis.close();
				fis.close();
			}
			catch(Exception e) {						
			}
			
		}
		return fileName;
	}

	/**********************************************************************************************************
	 * @Objective: The below method is created to Pick latest file from downloaded
	 *             folder
	 * @InputParameters: Scenario Name
	 * @OutputParameters:
	 * @author:Mythri Jain
	 * @Date : 10-Mar-2021
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public String pickLatestFileFromDownloads(String filePath) {
		String downloadFolder = filePath;
		File dir = new File(downloadFolder);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
		System.out.println("There is no file in the folder");
		return "";
		}

		File lastModifiedFile = files[0];
		for (int i = 1; i < files.length; i++) {
		if (lastModifiedFile.lastModified() < files[i].lastModified()) {
		lastModifiedFile = files[i];
		}
		}
		String k = lastModifiedFile.toString();
		System.out.println(lastModifiedFile);
		Path p = Paths.get(k);
		String file = p.getFileName().toString();
		return file;
		}
	
	public String pickLatestFile(String filePath) {
		String downloadFolder = filePath;
		File dir = new File(downloadFolder);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			System.out.println("There is no file in the folder");
			return "";
			
		}
		File lastModifiedFile = null;
		
		for (int j = 0; j < files.length; j++) {
			if (files[j].isFile()) {
				 lastModifiedFile = files[0];
			}
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
			if (lastModifiedFile.lastModified() < files[i].lastModified()) {
				lastModifiedFile = files[i];
			}
			}
		}
		String k = lastModifiedFile.toString();

		System.out.println(lastModifiedFile);
		Path p = Paths.get(k);
		String file = p.getFileName().toString();
		return file;
	}

	/**********************************************************************************************************
	 * @Objective: The below method is created to zip extent report and make it
	 *             password protected
	 * @InputParameters: reportPath,zipFolderPath,password as String
	 * @OutputParameters:
	 * @author:Adarsh
	 * @throws IOException 
	 * @Date : 19-Mar-2021
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public void passwordProtectReportFile(String reportPath, String zipFolderPath, String password) throws IOException {

		File fol = new File("");
		try {

			String[] reportName = reportPath.split("\\\\");
			File directoryPath = new File(reportPath);
			File filesList[] = directoryPath.listFiles();
			ArrayList<File> filesToAdd = new ArrayList<File>();
			for (File file : filesList) {

				if (file.getAbsolutePath().contains(".html")) {
					filesToAdd.add(new File(file.getAbsolutePath()));

				} else if (file.isDirectory()) {

					fol = new File(zipFolderPath + "\\" + file.getName());
					fol.mkdir();
					try {
						FileUtils.copyDirectory(file, fol);
					} catch (IOException e) {
						throw e;
					}

				}

			}

			ZipParameters zipParameters = new ZipParameters();
			zipParameters.setEncryptFiles(true);
			zipParameters.setEncryptionMethod(EncryptionMethod.AES);
			zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
			ZipFile zipFile = new ZipFile(zipFolderPath + "\\" + reportName[reportName.length - 1] + ".zip",
					password.toCharArray());
			zipFile.addFiles(filesToAdd, zipParameters);
			zipFile.addFolder(fol, zipParameters);
			try {
				FileUtils.deleteDirectory(fol);
			} catch (IOException e) {
				throw e;
			}

		} catch (ZipException e) {
			throw e;
		}
	}

	/**********************************************************************************************************
	 * @Objective: The below method is created to check file is downloaded in the loacation
	 * @InputParameters: fileName,Path
	 * @OutputParameters:
	 * @author:Yashwanth Naidu
	 * @Date : 21-May-2021
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public boolean verifyFileToDownload(String fileName , String Path) {
		boolean flag = false;

		String downloadPath = Path;
		File dir = new File(downloadPath);
		File[] dirContents = dir.listFiles();

		for (int i = 0; i < 10; i++) {
			status = (dirContents[i].getName().contains(fileName));
			if(status){
				return flag=true;
			}else {
				if(i==10) {
					//            		Reports.ExtentReportLog("", Status.INFO, "file not exist", false);
				}
			}
		}
		return flag;
	}

	/**********************************************************************************************************
	 * @Objective: To extract files from specified zip file into specified location and get file names
	 * @InputParameters:zipFilePath,destDir value as String
	 * @OutputParameters:
	 * @author:Amitabh N
	 * @throws IOException 
	 * @Date : 03-20-2022
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public String unzipGetFileNames(String zipFilePath, String destDir, String scenarioName , String SheetName ,String multipleFilenames) throws IOException {

		File dir = new File(destDir);
		// create output directory if it doesn't exist
		if (!dir.exists())
			dir.mkdirs();
		FileInputStream fis =null; ZipInputStream zis=null;
		// buffer for read and write data to file
		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(zipFilePath);
			zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			int i=1;
			while (ze != null) {
				String fileName = ze.getName();
				if (i==1) {
					multipleFilenames = multipleFilenames + fileName; 
				}
				else {
					multipleFilenames = multipleFilenames + "," +fileName;
				}

				i++;

				File newFile = new File(destDir + File.separator + fileName);
				System.out.println("Unzipping to " + newFile.getAbsolutePath());
				// create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					// close this ZipEntry
					zis.closeEntry();
					ze = zis.getNextEntry();
				}
				catch(IOException e) {
					throw e;
				}
				finally {					
					try {
						fos.close();
					}
					catch(Exception e) {						
					}
				}				
			}
			// close last ZipEntry
			zis.closeEntry();
			
		} catch (IOException e) {
			throw e;
		}
		finally {
			try {
				zis.close();
				fis.close();
			}
			catch(Exception e) {						
			}
		}
		return multipleFilenames;
	}

	public String unzipSubDocGetFileNames(String zipFilePath, String destDir, String scenarioName , String subDocFilenames) throws IOException {

		File dir = new File(destDir);
		// create output directory if it doesn't exist
		if (!dir.exists())
			dir.mkdirs();
		FileInputStream fis =null;ZipInputStream zis=null;
		// buffer for read and write data to file
		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(zipFilePath);
			zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			int i=1;
			while (ze != null) {
				String fileName = ze.getName();

				if (i==1) {
					subDocFilenames = subDocFilenames + fileName; 
				}
				else {
					subDocFilenames = subDocFilenames + "," +fileName;
				}
				i++;

				File newFile = new File(destDir + File.separator + fileName);
				System.out.println("Unzipping to " + newFile.getAbsolutePath());
				// create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}					
					// close this ZipEntry
					zis.closeEntry();
					ze = zis.getNextEntry();
				}
				catch(IOException e) {
					throw e;
				}
				finally {
					try {
						fos.close();
					}
					catch(Exception e) {						
					}
				}				
			}
			// close last ZipEntry
			zis.closeEntry();
			
		} catch (IOException e) {
			throw e;
		}
		finally {			
			try {
				zis.close();
				fis.close();
			}
			catch(Exception e) {						
			}
		}
		return subDocFilenames;
	}
	
	/**********************************************************************************************************
	* @Objective: To unzip the Batch Report E2B and return the absolute file name
	* @InputParameters: fileType (excel/xml)
	* @OutputParameters:
	* @author: Diksha
	* @throws Exception
	* @Date : 22-may-2022
	* @UpdatedByAndWhen:Vaishali 25-May-2022
	**********************************************************************************************************/
	public String UnZipExportedXML(String fileType, String zipFilePathOutPutFolder, String destDirectory)
			throws Exception {
		Thread.sleep(5000);
		String ZipFolderName = "Sender Organization Name-*.zip";
		File zipFile = new File(zipFilePathOutPutFolder);
		String fileName = null;
		FileFilter filefilter = new WildcardFileFilter(ZipFolderName);
		File[] files = zipFile.listFiles(filefilter);
		File zippedFileName = files[0];
		String zipFilePath = zippedFileName.toString();
		String file = zipFilePath.split("test-output")[1].replace("\\", "");
		moveFile(file, zipFilePathOutPutFolder, destDirectory);
		unzip(destDirectory + "\\" + file, destDirectory);
		FileInputStream fis = null;
		ZipInputStream zis = null;
		try {
			fis = new FileInputStream(destDirectory + "\\" + file);
			zis = new ZipInputStream(fis);
			ZipEntry ze;
			while ((ze = zis.getNextEntry()) != null) {
				if (fileType.equalsIgnoreCase("Excel")) {

					if (ze.getName().contains(".xlsxx")) {
						fileName = ze.getName();
					}
					fileName = file + "\\" + fileName;
				} else if (ze.getName().contains("RCT")) {
					fileName = ze.getName();
				} else
					continue;
			}
			zis.closeEntry();
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			try {
				zis.close();
				fis.close();
			}
			catch(Exception e) {						
			}
		}

		return fileName;
	}
	/**********************************************************************************************************
	* @Objective: To get the latest file from downloads with a specific type
	* @InputParameters: path,fileType (excel/xml/pdf)
	* @OutputParameters:
	* @author: Diksha
	* @throws Exception
	* @Date : 22-may-2022
	* @UpdatedByAndWhen:
	**********************************************************************************************************/
	public String pickLatestFileWithTypeFromDownloads(String filePath, String fileType) {
		String downloadFolder = filePath;
		File dir = new File(downloadFolder);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			System.out.println("There is no file in the folder");
			return "";
			
		}
		File lastModifiedFile = null;
		
		for (int j = 0; j < files.length; j++) {
			if (files[j].isFile() && files[j].toString().contains(fileType)) {
				 lastModifiedFile = files[0];
			}
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile() && files[i].toString().contains(fileType)) {
			if (lastModifiedFile.lastModified() < files[i].lastModified()) {
				lastModifiedFile = files[i];
			}
			}
		}
		String k = lastModifiedFile.toString();

		System.out.println(lastModifiedFile);
		Path p = Paths.get(k);
		String file = p.getFileName().toString();
		return file;
	}
	




	
	
	
	


	
}

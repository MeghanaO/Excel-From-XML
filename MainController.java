package com.staples.eCatalogue.convertor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Logger;

import com.staples.eCatalogue.constants.ConvertorConstants;
import com.staples.eCatalogue.utility.FileFilterClass;

public class MainController {

	static final Logger LOGGER = Logger.getLogger(MainController.class.getName());
	
	public static void main(String[] args){
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
		SimpleDateFormat dateFormatter_ms = new SimpleDateFormat("yyyyMMdd'T'HHmmssSSS");
		Date startTime = new Date();
		
		try {
			LOGGER.info("Job StepArticleReports - Started");
			
			//Check if there are three input arguments
			if (args.length != 3) {
				LOGGER.info(ConvertorConstants.INVALID_ARGUMENTS_JOB_ABORTED + "\n" + ConvertorConstants.USAGE_COMMAND);
				System.exit(0);
			}			
			//Check input folder
			File inputFolder = new File(args[0]);
			if(!inputFolder.exists()) {
				LOGGER.info(ConvertorConstants.INVALID_ARGUMENTS_JOB_ABORTED + "\n" + ConvertorConstants.SOURCE_FOLDER + inputFolder.getPath() + ConvertorConstants.DOES_NOT_EXIST + "\n" + ConvertorConstants.USAGE_COMMAND);
				System.exit(0);
			}
			//Check output folder
			File outputfolder = new File(args[1]);
			if (!outputfolder.exists()) {
				LOGGER.info(ConvertorConstants.INVALID_ARGUMENTS_JOB_ABORTED + "\n" + ConvertorConstants.DESTINATION_FOLDER + outputfolder.getPath() + ConvertorConstants.DOES_NOT_EXIST + "\n" + ConvertorConstants.USAGE_COMMAND);
				System.exit(0);
			}
			//Validate data quality assessment flag value
			if (!(args[2].equals("PRD") || args[2].equals("PRD,EO,") || args[2].equals("PRD,EO,ActiveAssortment") || args[2].equals("PRD,EO,ActiveAssortment,RestrictedToActiveAssortment") || args[2].equals("PRD,RestrictedToActiveAssortment")
				|| args[2].equals("QA") || args[2].equals("QA,EO,") || args[2].equals("QA,EO,ActiveAssortment") || args[2].equals("QA,EO,ActiveAssortment,RestrictedToActiveAssortment") || args[2].equals("QA,RestrictedToActiveAssortment"))) {
				LOGGER.info(ConvertorConstants.INVALID_DATA_QUALITY_ASSESSMENT_FLAGS + "\n" + ConvertorConstants.VALID_DATA_QUALITY_ASSESSMENT_FLAGS_SET1 + "\n" + ConvertorConstants.VALID_DATA_QUALITY_ASSESSMENT_FLAGS_SET2 + "\n" + ConvertorConstants.USAGE_COMMAND);
				System.exit(0);
			}
			//Check and create archive folder if needed
			File archiveFolder = new File(args[0] + File.separator + ConvertorConstants.ARCHIVE);
			if(!archiveFolder.exists()) {
				archiveFolder.mkdir();
			}
			//Check and create unzip folder if needed
			File unzipFolder = new File(args[0] + File.separator + ConvertorConstants.ARCHIVE + File.separator + ConvertorConstants.UNZIP);
			if(!unzipFolder.exists()) {
				unzipFolder.mkdir();
			}
			//Check and create active assortments folder if needed
			File activeAssortmentsFolder = new File(args[0] + File.separator + ConvertorConstants.ACTIVE_ASSORTMENTS);
			if(!activeAssortmentsFolder.exists()) {
				activeAssortmentsFolder.mkdir();
			}
			//Check and create log folder if needed
			File logFolder = new File(args[1] + File.separator + ConvertorConstants.LOG);
			if(!logFolder.exists()) {
				logFolder.mkdir();
			}
			
			//Filtering zip files - Unzipping, placing XMl in unzip folder & moving zip file to archive folder
			FileFilterClass zipFileFilter = new FileFilterClass(ConvertorConstants.ZIP);
			File[] listOfFiles = inputFolder.listFiles(zipFileFilter);
			if(listOfFiles.length == 0) {
				LOGGER.info("No zip files exists in the input directory.");
				System.exit(0);
			} else {
				boolean renameStatus = false;
				String zipFileName = null;
				LOGGER.info("Number of ZIP Files:"+listOfFiles.length);
				LOGGER.info("Unzip in progress.");
				for (File zipFile : listOfFiles) {
					zipFileName = zipFile.getName();
					String outputFileName = ConvertorConstants.WEEKLY_ARTICLE_REPORT + gbcff(zipFile.getName())+ "_" + dateFormatter_ms.format(new Date()) + ConvertorConstants.XML;
					File unzippedXML = new File(unzipFolder.getPath() + File.separator + outputFileName);
					//Extracting the zip files
					usf(zipFile, unzippedXML);
					File checkExistance = new File(archiveFolder + File.separator + zipFileName);
					if (checkExistance.exists()) {
						checkExistance.delete();
					}
					//Moving the zip file to archive folder after extraction
					renameStatus = zipFile.renameTo(new File(archiveFolder + File.separator + zipFileName));
					if (!renameStatus)
						LOGGER.info("Moving zip file to backup folder failed.");
				}
			}
			
			//Filtering XMLs
			FileFilterClass xmlFileFilter = new FileFilterClass(ConvertorConstants.XML);
			File[] xmlFileList = unzipFolder.listFiles(xmlFileFilter);
			if(xmlFileList.length == 0){
				LOGGER.info("No XML files available for conversion.");
				System.exit(0);
			} else {
				boolean deleteStatus = false; 
				LOGGER.info("Number of extracted XMLs:"+xmlFileList.length);				
				Arrays.sort(xmlFileList);	//Sorting all the XML files			
				File outputFile = null;
				for(File xmlFile : xmlFileList) {
					LOGGER.info("Processing XML:" + xmlFile.getName());
					//Creating output excel file
					outputFile = new File(outputfolder.getPath() + File.separator + ConvertorConstants.WEEKLY_ARTICLE_REPORT + gbcff(xmlFile.getName()) + ConvertorConstants.XLSX);
					//String [] arguments = {xmlFile.getPath(), outputFile.getPath(), args[2], args[0] + File.separator + "ActiveAssortments", outputFileName};
					String [] arguments = {xmlFile.getPath(), outputFile.getPath(), args[2], args[0] + File.separator + ConvertorConstants.ACTIVE_ASSORTMENTS, args[1], gbcff(xmlFile.getName()), "N"};
					//Parse XMLs one by one and convert to excel
					CatalogueConvertor.main(arguments);
					//Delete the XML post conversion
					deleteStatus = xmlFile.delete();
					if(!deleteStatus)
						LOGGER.info("Deletion of extracted XML failed.");
				}
				
				//Invoke Excel creation for final iteration
				String [] arguments = {"", "", "","", "", "", "Y"};
				CatalogueConvertor.main(arguments);
				
			}
			//For logging the time taken
			Date endTime = new Date();
			Long totalTimeElapsed = endTime.getTime() - startTime.getTime();
			String formattedTimeTaken = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalTimeElapsed),
					TimeUnit.MILLISECONDS.toMinutes(totalTimeElapsed) % TimeUnit.HOURS.toMinutes(1),
					TimeUnit.MILLISECONDS.toSeconds(totalTimeElapsed) % TimeUnit.MINUTES.toSeconds(1));
			LOGGER.info("Total time taken : " + formattedTimeTaken);
			LOGGER.info("Job StepArticleReports - Completed");
		} catch(Exception e) {
			e.printStackTrace();
			LOGGER.info("Job StepArticleReports completed with errors.");
		}
		finally {
			try {
				//For moving the Log4j generated log file to Log folder with start timestamp
				dateFormatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");		
				String workingDir = System.getProperty("user.dir");
				File logFile = new File(workingDir + ConvertorConstants.LOG4J_LOG_NAME);
				File newLogFile = new File(args[1] + File.separator + ConvertorConstants.LOG + File.separator
						+ ConvertorConstants.LOG_FILE_NAME + dateFormatter.format(startTime) + ConvertorConstants.TXT);
				Files.copy(logFile.toPath(), newLogFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (FileAlreadyExistsException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String gbcff (String fileName) {
		if  (fileName.contains("95425")) {
			return "95425-SEOFR";
		} else if  (fileName.contains("95429")) {
			return "95429-SEOFR1";
		} else if  (fileName.contains("95470")) {
			return "95470-SEOES";
		} else if  (fileName.contains("95480")) {
			return "95480-SEOIT";
		} else if  (fileName.contains("95485")) {
			return "95485-SEOUK";
		} else if  (fileName.contains("95511")) {
			return "95511-SEODE";
		} else if  (fileName.contains("96541")) {
			return "96541-SEOPT";
		} else if  (fileName.contains("96542")) {
			return "96542-SEONL";
		} else if  (fileName.contains("96720")) {
			return "96720-SEANL";
		} else if  (fileName.contains("96725")) {
			return "96725-SEAUK";
		} else if  (fileName.contains("96730")) {
			return "96730-SEADE";
		} else if  (fileName.contains("96775")) {
			return "96775-SEAIT";
		} else if  (fileName.contains("96785")) {
			return "96785-SEAFR";
		}  else if  (fileName.contains("96790")) {
			return "96790-SEAES";
		}  else if  (fileName.contains("96741")) {
			return "96741-SEAIE";
		}  else if  (fileName.contains("96763")) {
			return "96763-SEASE";
		}  		
		else if  (fileName.contains("96770")) {
			return "96770-SEADK";
		}  else if  (fileName.contains("96752")) {
			return "96752-SEANO";
		}  else if  (fileName.contains("95465")) {
			return "95465-SEODK";
		}  else if  (fileName.contains("96773")) {
			return "96773-SEONO";
		}  else if  (fileName.contains("95460")) {
			return "95460-SEOSE";
		}
		else if  (fileName.contains("96802")) {
			if  (fileName.toUpperCase().contains("DIV")) {
				String substrDivision = fileName.substring(fileName.toUpperCase().indexOf("DIV")+3);
				if (substrDivision.indexOf('-') > 0) {
					return "96802-STAPLESEUR-DIV" + substrDivision.substring(0,substrDivision.indexOf('-'));
				} else {
					if (substrDivision.indexOf('.') > 0) {
						return "96802-STAPLESEUR-DIV" + substrDivision.substring(0,substrDivision.indexOf('.'));
					} else {
						return "96802-STAPLESEUR-DIV" + substrDivision;
					}
				}				
			} else {
				return "96802-STAPLESEUR";
			}
		} else {
			return "NNNNN-XXXXX";
		}
	}
	
	/**
	 * Method for extracting data from the zip file
	 * 
	 * @param src
	 * @param dst
	 * @throws IOException
	 */
	public static void usf(File src, File dst) throws IOException{
		ZipInputStream zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(src), 2048));
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dst));
		zipIn.getNextEntry();
		byte data[] = new byte[2048];
		
		int count = 0;
		while((count = zipIn.read(data,0,2048)) != -1) {
			bos.write(data, 0, count);
	    }
	    bos.close();
	    zipIn.close();
	}
}

package com.staples.eCatalogue.convertor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.staples.eCatalogue.POJO.PackagingInfo;
import com.staples.eCatalogue.POJO.ProductInfo;
import com.staples.eCatalogue.constants.ConvertorConstants;
import com.staples.eCatalogue.utility.FileFilterClass;

public class ExcelWriter {
	
	static final Logger LOGGER = Logger.getLogger(ExcelWriter.class.getName());
	
	private static CellStyle HEADER_CELL_STYLE;	
	private static CellStyle CALC_HEADER_CELL_STYLE;
	private static CellStyle CALC_HEADER_CENTER_CELL_STYLE;
	private static CellStyle DATA_CELL_STYLE;
	private static CellStyle CALC_DATA_CELL_STYLE;
	
	private int productRowNo = 0;
	private int packagingRowNo = 0;
	private static final int dataIntegrityViolationsCount_online = 7;
	private static final int missingMandatoryInformationCount_online = 3;
	private static final int processingBacklogsCount = 1;
	private static final int dataIntegrityViolationsCount_advantage = 5;
	private static final int missingMandatoryInformationCount_advantage = 2;
	private HashMap<String, String> activeAssortments = new HashMap<String, String>();
	private HashMap<String, List<String>> localCodeEUCodeMapping = new HashMap<String, List<String>>();
	private List<String> activeAssortmentsList = new ArrayList<String>();
	
	String calculatedBusinessUnit = null;
	String channel = null;
	Boolean all = false;
	String validationFlags = null;
	String cco = null;
	List<String> drl = new ArrayList<String>();
	
	
	/**
	 * This method creates the excel and inserts all the extracted/calculated
	 * data from the STEP XML
	 * 
	 * @param productInfoList
	 * @param packagingInfo
	 * @param contextList
	 * @param packageProductRelationship
	 * @param packageArticleCodeRelationship
	 * @param brandLogoRelationship
	 * @param args
	 * @throws IOException 
	 */
	public void createOutputExcel(List<ProductInfo> productInfoList, List<PackagingInfo> packagingInfo,
			List<String> contextList, Map<String, String> packageProductRelationship,
			Map<String, String> packageArticleCodeRelationship, Map<String, String> brandLogoRelationship,
			String[] args) throws IOException 
	{
		LOGGER.info("Excel creation started.");
		validationFlags = args[2];
		
		int i = 0;
		for (String context : contextList) {
			LOGGER.info("Creating Excel for Context (" + (++i) + ") of (" + contextList.size() + ") : "+context);
			SXSSFWorkbook workbook = new SXSSFWorkbook();

			// Resetting row count
			productRowNo = 0;
			packagingRowNo = 0;

			// Setting the excel cell styling
			HEADER_CELL_STYLE = headerCellStyle(workbook);
			DATA_CELL_STYLE = dataCellStyle(workbook);
			CALC_HEADER_CELL_STYLE = calculatedHeaderCellStyle(workbook);
			CALC_HEADER_CENTER_CELL_STYLE = calculatedHeaderCellStyle_centered(workbook);
			CALC_DATA_CELL_STYLE = calculatedDataCellStyle(workbook);
			
			//Setting the attributes
			channel = getChannel(context.substring(0, 5));
			//channel = ConvertorConstants.ONLINE;
			
			if (calculatedBusinessUnit == null) {
				if (context.equals("GL") || context.equals("xx-all")) {
					calculatedBusinessUnit = gbcs(gbcff(args[0]).substring(0, 5));
				} else {
					calculatedBusinessUnit = gbcs(context.substring(0, 5));
				}
			}
			
			if (cco == null && calculatedBusinessUnit != null && calculatedBusinessUnit.length() == 5) {
				cco = calculatedBusinessUnit.substring(3, 5);
			}
			
			all = llc(args[3], context);
			
			LOGGER.debug("Channel:" + channel + ", CalculatedBusinessUnit:" + calculatedBusinessUnit + ", cco:" + cco + ", ValidationFlags:" + validationFlags);	
			
			try {
				//String fileName = ConvertorConstants.WEEKLY_ARTICLE_REPORT + context + getFileNameCode(context)
				//		+ ConvertorConstants.XLSX;
				
				String fileName = null;
				if (context.equals("xx-all")) {
					fileName = ConvertorConstants.WEEKLY_ARTICLE_REPORT + "96802" + getFileNameCode(args[0])
							+ ConvertorConstants.XLSX;
				} else {
					fileName = ConvertorConstants.WEEKLY_ARTICLE_REPORT + context + getFileNameCode(context)
							+ ConvertorConstants.XLSX;
				}
				FileOutputStream out = new FileOutputStream(new File(args[4] + File.separator + fileName), false);

				// Inserting Product Information sheet details
				SXSSFSheet cs = (SXSSFSheet) workbook.createSheet(ConvertorConstants.PRODUCT_INFORMATION);
				createProductHeader(cs, context);
				insertProductInformation(cs, productInfoList, packagingInfo, brandLogoRelationship, context);

				// Inserting Packaging Information sheet details
				cs = (SXSSFSheet) workbook.createSheet(ConvertorConstants.PACKAGING_INFORMATION);
				createPackagingHeader(cs);
				insertPackagingInformation(cs, packagingInfo, packageProductRelationship,
						packageArticleCodeRelationship, context);

				workbook.write(out);
				out.close();
				workbook.dispose();
				LOGGER.info("WeeklyArticleReport file created : " + fileName);
				createAdditionalLogFiles(args[4], context);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("Process complete!!! Please check the output folder to view the created excels.");
	}
	
	private void createAdditionalLogFiles(String path, String context) throws IOException {
		String workingDir = System.getProperty("user.dir");
		String fileName = ConvertorConstants.WEEKLY_ARTICLE_REPORT + context + getFileNameCode(context)
				+ ConvertorConstants.MISSING_LOCAL_CODES_FILE_NAME + ConvertorConstants.TXT;
		FileWriter writer = null;
		
		if(!activeAssortmentsList.isEmpty())
		{
			LOGGER.debug("Missing Local Code count from Active Assortments file: " + activeAssortmentsList.size());
			writer = new FileWriter(fileName);
			writeListToFile(writer, activeAssortmentsList);
			writer.close();

			File logFile = new File(workingDir + File.separator  + fileName);
			File newLogFile = new File(
					path + File.separator + ConvertorConstants.LOG + File.separator + fileName + ConvertorConstants.TXT);
	
			Files.move(logFile.toPath(), newLogFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}

		List<String> finalList = new ArrayList<String>();
		for (Entry<String, List<String>> entry : localCodeEUCodeMapping.entrySet()) {
			if (entry.getValue() != null && !entry.getValue().isEmpty() && entry.getValue().size() > 1) {
				finalList.add(entry.getKey() + " "
						+ convertListToSeparatedValues(entry.getValue(), ConvertorConstants.COMMA));
			}
		}
		
		if(!finalList.isEmpty())
		{
			LOGGER.debug("No of Local Codes mapped to multiple entries: " + finalList.size());
			fileName = ConvertorConstants.WEEKLY_ARTICLE_REPORT + context + getFileNameCode(context)
					+ ConvertorConstants.MULTIPLE_REF_LOCAL_CODES_FILE_NAME + ConvertorConstants.TXT;
			writer = new FileWriter(fileName);
			writeListToFile(writer, finalList);
			writer.close();

			File logFile = new File(workingDir + File.separator + fileName);
			File newLogFile = new File(path + File.separator + ConvertorConstants.LOG + File.separator + fileName
					+ ConvertorConstants.TXT);

			Files.move(logFile.toPath(), newLogFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		
	}

	private void writeListToFile(FileWriter writer, List<String> values) throws IOException {
		for (String entry : values) {
			writer.write(entry);
			writer.write(System.getProperty("line.separator"));
		}
	}

	/**
	 * This method inserts the Product header information into the Product
	 * Information sheet
	 * 
	 * @param cs
	 * @param context 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void createProductHeader(SXSSFSheet cs, String context) throws FileNotFoundException, IOException
	{
		int calcColumnStart = 0;	
		SXSSFRow row;
		
		//Main column headers
		row = (SXSSFRow)cs.createRow(2);
		int i=-1;
		
		createHeaderCell(cs, ConvertorConstants.ARTICLE_CODE, row, ++i);
		createHeaderCell(cs, ConvertorConstants.PRODUCT_ID, row, ++i);
		createHeaderCell(cs, ConvertorConstants.LEN_CODE, row, ++i);
		createHeaderCell(cs, ConvertorConstants.NAME, row, ++i);
		createHeaderCell(cs, ConvertorConstants.PARENT_ID, row, ++i);
		createHeaderCell(cs, ConvertorConstants.TYPOLOGY_NAME, row, ++i);
		createHeaderCell(cs, ConvertorConstants.PRODUCT_TYPE, row, ++i);
		createHeaderCell(cs, ConvertorConstants.SPGS_CODE_CALCULATED, row, ++i);
		createHeaderCell(cs, ConvertorConstants.MANUFACTURER_PART_NUMBER, row, ++i);
		if(!calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR))
			createHeaderCell(cs, ConvertorConstants.TAX_CODE, row, ++i);		
		createHeaderCell(cs, ConvertorConstants.PRODUCT_LIFE_CYCLE_STATUS, row, ++i);
		if(ConvertorConstants.FAST_BU_LIST.contains(context))
		{
			createHeaderCell(cs, ConvertorConstants.DC_PRODUCT_LIFE_CYCLE_STATUS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.SITE_PRODUCT_LIFE_CYCLE_STATUS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.SALES_PACKAGING_UNIT, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRODUCT_LIFE_CYCLE_STATUS_EO, row, ++i);
			createHeaderCell(cs, ConvertorConstants.WEB_PRODUCT_TITLE_ADVANTAGE, row, ++i);
			createHeaderCell(cs,ConvertorConstants.ARTICLE_EXISTS_IN_DC, row, ++i);
			createHeaderCell(cs,ConvertorConstants.ARTICLE_EXISTS_IN_SITE, row, ++i);
			createHeaderCell(cs,ConvertorConstants.DC_SALES_PACKAGING_LINE, row, ++i);
			createHeaderCell(cs,ConvertorConstants.DELIVERING_SITE, row, ++i);
		}
		createHeaderCell(cs, ConvertorConstants.ACTIVE_FOR_COUNTRY, row, ++i);
		createHeaderCell(cs, ConvertorConstants.ACTIVE_FOR_LANGUAGE, row, ++i);
		createHeaderCell(cs, ConvertorConstants.PRODUCT_TO_BE_PUBLISHED, row, ++i);
		createHeaderCell(cs, ConvertorConstants.PRODUCT_PRIORITY, row, ++i);
		createHeaderCell(cs, ConvertorConstants.ASSORTMENT_TYPE, row, ++i);
		createHeaderCell(cs, ConvertorConstants.BRAND_CALCULATED, row, ++i);
		createHeaderCell(cs, ConvertorConstants.PUBLICATION_BRAND, row, ++i);
		createHeaderCell(cs, ConvertorConstants.BRAND_LOGOS, row, ++i);
		
		if(channel.equals(ConvertorConstants.ONLINE))
		{
			createHeaderCell(cs, ConvertorConstants.WEB_HIERARCHY_CLASSIFICATION_REFERENCES, row, ++i);
			createHeaderCell(cs, ConvertorConstants.BREADCRUMB_WEB_HIERARCHY_CLASSIFICATION_REFERENCES, row, ++i);
			createHeaderCell(cs, ConvertorConstants.DESCRIPTION, row, ++i);
			createHeaderCell(cs, ConvertorConstants.ARTICLE_STANDARD_ERP_DESCRIPTION, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRODUCT_TITLE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.WEB_PRODUCT_TEXT, row, ++i);
			createHeaderCell(cs, ConvertorConstants.CATCH_LINE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.COMMERCIAL_TEXT, row, ++i);
			createHeaderCell(cs, ConvertorConstants.KEY_SELLING_POINTS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.EXTENDED_SELLING_POINTS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.LINKS_TO_ADDITIONAL_DOCUMENTATION, row, ++i);
			createHeaderCell(cs, ConvertorConstants.LINKS_TO_EXTERNAL_CONTENT, row, ++i);
			createHeaderCell(cs, ConvertorConstants.BRAND_PROMISE_LINE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.KEYWORDS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE_EASY_ORDER, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE_LOCAL, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE_CENTRAL, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE_PAPER, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE_PAPER_LOCAL, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE_PAPER_CENTRAL, row, ++i);
			createHeaderCell(cs, ConvertorConstants.SECONDARY_IMAGES, row, ++i);
			createHeaderCell(cs, ConvertorConstants.DOCUMENTS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.ICONS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.ECO_EASY, row, ++i);
			createHeaderCell(cs, ConvertorConstants.COORDINATED_ITEMS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRODUCT_REFERENCE_COORDINATED_ITEMS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.LINKS_TO_EXTERNAL_VIDEOS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.ENABLE_EXPERT_REVIEWS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.ENABLE_INLINE_CONTENT, row, ++i);
			createHeaderCell(cs, ConvertorConstants.INSTALLABLE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.INSTALLATION_FEE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRODUCT_TITLE_PAPER_ONLINE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRODUCT_SUB_TITLE_PAPER_ONLINE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.CATCH_LINE_PAPER, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRODUCT_TEXT_PAPER_ONLINE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.KEY_SELLING_POINTS_PAPER, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PAPER_PROMOTIONAL_TEXT, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PAPER_VISUAL_CATCH_LINE_EXPORT, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PAPER_BANNER_TEXT, row, ++i);
			createHeaderCell(cs, ConvertorConstants.KEYWORDS_PAPER, row, ++i);
			createHeaderCell(cs, ConvertorConstants.ARTICLE_TYPE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.HAZARDOUS_MATERIAL, row, ++i);
			createHeaderCell(cs, ConvertorConstants.EXTERNAL_CLASSIFICATIONS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.MAIN_GTIN, row, ++i);
			createHeaderCell(cs, ConvertorConstants.GTIN, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PACKAGING_IDS, row, ++i);
			
			calcColumnStart = i+1;
			//Calculated columns
			createCalculatedHeaderCell(cs, ConvertorConstants.REFERENCED_PACKAGINGS, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.BREADCRUMB_NODES, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.LINKS_TO_EXTERNAL_VIDEOS, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.PRODUCT_LIFE_CYCLE_STATUS, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.ACTIVE_FOR_COUNTRY, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.ASSORTMENT_TYPE, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.ARTICLE_TYPE, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.PUBLICATION_BRAND, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.SPGS_CODE_CALCULATED, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.TAX_CODE, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.WEB_HIERARCHY_CLASSIFICATION_REFERENCES, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.PRODUCT_TITLE, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.WEB_PRODUCT_TEXT, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.KEYWORDS, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.ASSET_PUSH_LOCATION_GENESIS_STANDARD, row, ++i);
			if (all)
				createCalculatedHeaderCell(cs, ConvertorConstants.ACTIVE_ASSORTMENT, row, ++i);
		}
		else{
			if(!calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR)){
				createHeaderCell(cs, ConvertorConstants.WEB_HIERARCHY_CLASSIFICATION_REFERENCES, row, ++i);
				createHeaderCell(cs, ConvertorConstants.ADVANTAGE_WEB_HIERARCHY_ID, row, ++i);
				createHeaderCell(cs, ConvertorConstants.ADVANTAGE_BREAD_CRUMB_WEB_HIERARCHY_ID, row, ++i);
				createHeaderCell(cs, ConvertorConstants.EASY_ORDER_CODE_CALCULATED, row, ++i);
			}
			createHeaderCell(cs, ConvertorConstants.DESCRIPTION, row, ++i);
			createHeaderCell(cs, ConvertorConstants.ARTICLE_STANDARD_ERP_DESCRIPTION, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRODUCT_TITLE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRODUCT_TITLE_ADVANTAGE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRODUCT_TITLE_PAPER_ADVANTAGE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRODUCT_PRICE_GRID_FEATURE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.CATCH_LINE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.COMMERCIAL_TEXT, row, ++i);
			createHeaderCell(cs, ConvertorConstants.COMMERCIAL_TEXT_ADVANTAGE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.ENABLE_INLINE_CONTENT, row, ++i);
			createHeaderCell(cs, ConvertorConstants.KEY_SELLING_POINTS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.KEYWORDS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.WEB_PRODUCT_TEXT_ADVANTAGE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE_EASY_ORDER, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE_LOCAL, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE_CENTRAL, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE_PAPER, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE_PAPER_LOCAL, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE_PAPER_CENTRAL, row, ++i);
			createHeaderCell(cs, ConvertorConstants.SECONDARY_IMAGES, row, ++i);
			createHeaderCell(cs, ConvertorConstants.DOCUMENTS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.ICONS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.ECO_EASY, row, ++i);
			createHeaderCell(cs, ConvertorConstants.ACCESSORY_PRODUCTS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.ALTERNATIVE_PRODUCTS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRODUCT_SUBTITLE_ADVANTAGE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRODUCT_SUBTITLE_PAPER_ADVANTAGE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PRODUCT_TEXT_PAPER_ADVANTAGE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.ARTICLE_TYPE, row, ++i);
			createHeaderCell(cs, ConvertorConstants.HAZARDOUS_MATERIAL, row, ++i);
			
			createHeaderCell(cs, ConvertorConstants.EXTERNAL_CLASSIFICATIONS, row, ++i);
			createHeaderCell(cs, ConvertorConstants.MAIN_GTIN, row, ++i);
			createHeaderCell(cs, ConvertorConstants.GTIN, row, ++i);
			createHeaderCell(cs, ConvertorConstants.PACKAGING_IDS, row, ++i);
		
			calcColumnStart = i+1;
			//Calculated columns
			createCalculatedHeaderCell(cs, ConvertorConstants.REFERENCED_PACKAGINGS, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.PRODUCT_LIFE_CYCLE_STATUS, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.ACTIVE_FOR_COUNTRY, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.ASSORTMENT_TYPE, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.ARTICLE_TYPE, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.PUBLICATION_BRAND, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.SPGS_CODE_CALCULATED, row, ++i);
			if(!calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR))
				createCalculatedHeaderCell(cs, ConvertorConstants.WEB_HIERARCHY_CLASSIFICATION_REFERENCES, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.PRODUCT_TITLE_ADVANTAGE, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.COMMERCIAL_TEXT_ADVANTAGE, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.KEYWORDS, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE, row, ++i);			
			createCalculatedHeaderCell(cs, "AssetPushLocationEasyOrderStandard", row, ++i);
			
			if (validationFlags.contains(ConvertorConstants.EO) && !(calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR) || calculatedBusinessUnit.equals("SEAUK") || calculatedBusinessUnit.equals("SEAIE")))	
				createCalculatedHeaderCell(cs, ConvertorConstants.PRIMARY_IMAGE_EASY_ORDER, row, ++i);
			if (all)
				createCalculatedHeaderCell(cs, ConvertorConstants.ACTIVE_ASSORTMENT, row, ++i);
		}
		
		//Setting the filter
		cs.setAutoFilter(new CellRangeAddress(2,2,0,i));
		
		//First header row
		row = (SXSSFRow)cs.createRow(0);
		createCalculatedHeaderCell(cs, ConvertorConstants.DATA_QUALITY_ASSESSMENT, row, calcColumnStart);
				
		int dataIntegrityViolationsCount = 0;
		int missingMandatoryInformationCount = 0;
		int missingContentCount = 5;
		
		if(channel.equals(ConvertorConstants.ONLINE)){
			dataIntegrityViolationsCount = dataIntegrityViolationsCount_online;
			missingMandatoryInformationCount = missingMandatoryInformationCount_online;
		}
		else{
			dataIntegrityViolationsCount = dataIntegrityViolationsCount_advantage;
			missingMandatoryInformationCount = missingMandatoryInformationCount_advantage;
		}
		
		//Second header row
		row = (SXSSFRow)cs.createRow(1);
		
		createCalculatedHeaderCell(cs, ConvertorConstants.DATA_INTEGRITY_VIOLATIONS, row, calcColumnStart);
		createCalculatedHeaderCell(cs, ConvertorConstants.MISSING_MANDATORY_INFORMATION, row, calcColumnStart + dataIntegrityViolationsCount);
		createCalculatedHeaderCell(cs, ConvertorConstants.MISSING_CONTENT, row, calcColumnStart + dataIntegrityViolationsCount + missingMandatoryInformationCount);
		if(calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR) && !channel.equals(ConvertorConstants.ONLINE))
			missingContentCount--;
		createCalculatedHeaderCell(cs, ConvertorConstants.PROCESSING_BACKLOGS, row, calcColumnStart + dataIntegrityViolationsCount + missingMandatoryInformationCount  + missingContentCount);
		int total = calcColumnStart + dataIntegrityViolationsCount + missingMandatoryInformationCount  + missingContentCount;
		if (!channel.equals(ConvertorConstants.ONLINE) && validationFlags.contains(ConvertorConstants.EO) && !(calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR) || calculatedBusinessUnit.equals("SEAUK") || calculatedBusinessUnit.equals("SEAIE")))
			createCalculatedHeaderCell(cs, ConvertorConstants.REQUESTED_URL_NOT_FOUND, row, ++total);
		if (all)
			createCalculatedHeaderCell(cs, ConvertorConstants.ERP_STATUS, row, ++total);
		
		
		//Grouping header columns
		int start = calcColumnStart;
		int end = start + dataIntegrityViolationsCount + missingMandatoryInformationCount  + missingContentCount + processingBacklogsCount;
		if (!channel.equals(ConvertorConstants.ONLINE) && validationFlags.contains(ConvertorConstants.EO) && !(calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR) || calculatedBusinessUnit.equals("SEAUK") || calculatedBusinessUnit.equals("SEAIE")))
			end ++;
		if (all)
			end ++;
		cs.addMergedRegion(new CellRangeAddress(0,0,start,end-1));
		
		end = start + dataIntegrityViolationsCount;
		cs.addMergedRegion(new CellRangeAddress(1,1,start,end-1));
		
		start = end;
		end = end + missingMandatoryInformationCount;
		cs.addMergedRegion(new CellRangeAddress(1,1,start,end-1));
		
		start = end;
		end = end + missingContentCount;
		cs.addMergedRegion(new CellRangeAddress(1,1,start,end-1));
		/*
		start = end;
		end = end + processingBacklogsCount;
		cs.addMergedRegion(new CellRangeAddress(1,1,start,end-1)); */	
		
		//Freezing first three columns and first three header rows
		cs.createFreezePane(3,3) ;
	}
	
	/**
	 * Method to check if input string is an integer. If valid returns the
	 * corresponding Integer value else returns the Integer passed as the second
	 * argument
	 * 
	 * @param integerString
	 * @param defaultValue
	 * @return
	 */
	public int checkInteger(String integerString, int defaultValue) {
		try {
			return Integer.parseInt(integerString);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	/**
	 * This method inserts the Product related information into the Product
	 * Information sheet
	 * 
	 * @param cs
	 * @param productList
	 * @param packagingInfo 
	 * @param brandLogoRelationship
	 * @param context
	 */
	private void insertProductInformation(SXSSFSheet cs,
			List<ProductInfo> productList, List<PackagingInfo> packagingInfo, Map<String, String> brandLogoRelationship, String context) {

		SXSSFRow row;
		List<String> uniqueProductList = new ArrayList<String>();
		Boolean localCodeFlag = null;
		int duplicateProductCount = 0;
		int localCodeLessProdCount = 0;
		int productsRejectedDueToArticleExistsInDCCount = 0;
		//First three rows are header rows
		productRowNo = 3;
		int i = -1;
		for (int j = 1; j <= productList.size(); j++) {
			ProductInfo product = productList.get(j - 1);
			
			//To check if this article is already in the report 
			if (uniqueProductList.contains(product.getArticleCode())) {
				LOGGER.info("Duplicate entry skipped for: "+product.getArticleCode()+" - "+product.getProductID());
				duplicateProductCount++;
				continue;
			} else {
				uniqueProductList.add(product.getArticleCode());
				
				// For FAST BUs, if the value for ArticleExistsInDistributionChain
				// attribute is not "yes", we will not be displaying
				// the same in the Excel.
				if(ConvertorConstants.FAST_BU_LIST.contains(context)) {
					
					
					if (product.getArticleExistsInDistributionChain(context) == null || product.getArticleExistsInDistributionChain(context).isEmpty() || product.getArticleExistsInDistributionChain(context).equals("No")) {
						LOGGER.info("Skipped for: " + product.getArticleCode() + " - " + product.getProductID()
								+ " since ArticleExistsInDistributionChain is not YES..");
						productsRejectedDueToArticleExistsInDCCount++;
						continue;
					} 
					
				}
				// Except for FAST BUs, if the Packages tied to the Product do
				// not have any local codes mapped, we will not be displaying
				// the same in the Excel.
				else {
					if(!context.equals("xx-all")) {
						
						if (product.getPackagingList() == null || product.getPackagingList().isEmpty()) {
							LOGGER.info("Skipped for: " + product.getArticleCode() + " - " + product.getProductID()
									+ " since there are no local codes tied..");
							localCodeLessProdCount++;
							continue;
						} else {
							localCodeFlag = false;
							for (String packagingID : product.getPackagingList()) {
	
								for (PackagingInfo packageDetails : packagingInfo) {
									if (packageDetails.getPackagingID().equals(packagingID)) {
										if (packageDetails.getLocalCode(context) == null
												|| packageDetails.getLocalCode(context).isEmpty()) {
											break;
										} else {
											localCodeFlag = true;
											break;
										}
									}
								}
								if(localCodeFlag)
									break;
							}
							if (!localCodeFlag) {
								LOGGER.info("Skipped for: " + product.getArticleCode() + " - " + product.getProductID()
										+ " since there are no local codes tied.");
								localCodeLessProdCount++;
								continue;
							}
						}
					}
				}
				
				
				/*if(!ConvertorConstants.FAST_BU_LIST.contains(context))
				{
					if (product.getPackagingList() == null || product.getPackagingList().isEmpty()) {
						LOGGER.info("Skipped for: " + product.getArticleCode() + " - " + product.getProductID()
								+ " since there are no local codes tied..");
						localCodeLessProdCount++;
						continue;
					} else {
						localCodeFlag = false;
						for (String packagingID : product.getPackagingList()) {

							for (PackagingInfo packageDetails : packagingInfo) {
								if (packageDetails.getPackagingID().equals(packagingID)) {
									if (packageDetails.getLocalCode(context) == null
											|| packageDetails.getLocalCode(context).isEmpty()) {
										break;
									} else {
										localCodeFlag = true;
										break;
									}
								}
							}
							if(localCodeFlag)
								break;
						}
						if (!localCodeFlag) {
							LOGGER.info("Skipped for: " + product.getArticleCode() + " - " + product.getProductID()
									+ " since there are no local codes tied.");
							localCodeLessProdCount++;
							continue;
						}
					}
				}*/
				
				
				
			}
			
			row = (SXSSFRow) cs.createRow(productRowNo);
			i = -1;
			
			if (checkInteger(product.getArticleCode(), -1) > 0) {
				createNumericCell(checkInteger(product.getArticleCode(), -1), row, ++i);
			} else {
				createCell(product.getArticleCode(), row, ++i);
			}			
			createCell(product.getProductID(), row, ++i);
			createCell(product.getLenCode(), row, ++i);
			createCell(product.getName(), row, ++i);
			createCell(product.getParentID(), row, ++i);
			createCell(product.getTypologyName(), row, ++i);
			createCell(product.getProductType(context), row, ++i);
			createCell(product.getSpgsCodeCalculated(), row, ++i);
			if (checkInteger(product.getManufacturerPartNumber(), -1) > 0) {
				createNumericCell(checkInteger(product.getManufacturerPartNumber(), -1), row, ++i);
			} else {
				createCell(product.getManufacturerPartNumber(), row, ++i);
			}
			if(!calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR))
				createCell(product.getTaxCode (context), row, ++i);
			createCell(product.getProductLifeCycleStatus(), row, ++i);
			
			if(ConvertorConstants.FAST_BU_LIST.contains(context))
			{
				createCell(product.getDistributionChainProductLifeCycleStatus(context), row, ++i);
				createCell(product.getSiteProductLifeCycleStatus(context), row, ++i);
				createCell(product.getSalesPackagingUnit(context), row, ++i);
				createCell(product.getProductLifeCycleStatusEasyOrder(context), row, ++i);
				createCell(product.getWebProductTitleAdvantage(context), row, ++i);
				createCell(product.getArticleExistsInDistributionChain(context), row, ++i);
				createCell(product.getArticleExistsInSite(), row, ++i);
				createCell(product.getDistributionChainSalesPackagingLine(context), row, ++i);
				createCell(product.getDeliveringSite(), row, ++i);
			}
			createCell(convertListToSeparatedValues(product.getActiveForCountry(), ConvertorConstants.SEPARATOR), row, ++i);
			createCell(convertListToSeparatedValues(product.getActiveForLanguage(), ConvertorConstants.SEPARATOR), row, ++i);			
			createCell(product.getProductToBePublished(context), row, ++i);
			createCell(product.getProductPriority(context), row, ++i);
			createCell(product.getAssortmentType(), row, ++i);
			createCell(product.getBrandCalculated(), row, ++i);
			createCell(product.getPublicationBrand(), row, ++i);
			
			if(brandLogoRelationship.get(product.getPublicationBrand()) != null)
				createCell(brandLogoRelationship.get(product.getPublicationBrand()), row, ++i);
			else
				createCell("", row, ++i);
			
			if(channel.equals(ConvertorConstants.ONLINE))
			{
				createCell(convertListToSeparatedValues(product.getWebHierarchyClassificationReferences(), ConvertorConstants.SEPARATOR), row, ++i);
				createCell(convertListToSeparatedValues(product.getBreadcrumbWebHierarchyClassificationReferences(), ConvertorConstants.SEPARATOR), row, ++i);
				createCell(product.getDescription(context), row, ++i);
				createCell(product.getArticleStandardERPDescription(context), row, ++i);
				createCell(product.getProductTitle(context), row, ++i);
				createCell(product.getWebProductText(context), row, ++i);
				createCell(product.getCatchLine(context), row, ++i);
				createCell(product.getCommercialText(context), row, ++i);
				createCell(product.getKeySellingPoints(context), row, ++i);
				createCell(product.getExtendedSellingPoints(context), row, ++i);
				createCell(product.getLinksToAdditionalDocumentation(context), row, ++i);
				if(product.getLinksToExternalContent(context)!=null)
					createCell(convertListToSeparatedValues(product.getLinksToExternalContent(context),ConvertorConstants.SEPARATOR), row, ++i);
				else
					createCell("", row, ++i);			
				createCell(product.getBrandPromiseLine(context), row, ++i);
				if(product.getKeywords(context)!=null)
					createCell(convertListToSeparatedValues(product.getKeywords(context), ConvertorConstants.SEPARATOR), row, ++i);
				else
					createCell("", row, ++i);			
				
				createCell(product.getPrimaryImageEasyOrder(context), row, ++i);
				createCell(product.getPrimaryImage(context), row, ++i);
				createCell(product.getPrimaryImageLocal(context), row, ++i);
				createCell(product.getPrimaryImageCentral(), row, ++i);
				createCell(product.getPrimaryImagePaper(context), row, ++i);
				createCell(product.getPrimaryImagePaperLocal(context), row, ++i);
				createCell(product.getPrimaryImagePaperCentral(), row, ++i);
				createCell(convertListToSeparatedValues(product.getSecondaryImages(), ConvertorConstants.SEPARATOR), row, ++i);
				createCell(convertListToSeparatedValues(product.getDocuments(context), ConvertorConstants.SEPARATOR), row, ++i);
				createCell(convertListToSeparatedValues(product.getIcons(context), ConvertorConstants.SEPARATOR), row, ++i);
				createCell(product.getEcoEasy(), row, ++i);
				createCell(convertListToSeparatedValues(product.getCoordinatedItems(),ConvertorConstants.SEPARATOR), row, ++i);
				createCell(product.getProductReferenceCoordinatedItems(context), row, ++i);
				createCell(product.getLinksToExternalVideos(context), row, ++i);
				createCell(product.getEnableExpertReviews(context), row, ++i);
				createCell(product.getEnableInlineContent(context), row, ++i);
				createCell(product.getInstallable(context), row, ++i);
				createCell(product.getInstallationFee(context), row, ++i);				
				//createCell(convertListToSeparatedValues(product.getPackagingList(), ";"), row, ++i);
				
				createCell(product.getProductTitlePaperOnline(context), row, ++i);
				createCell(product.getProductSubTitlePaperOnline(context), row, ++i);
				createCell(product.getCatchLinePaper(context), row, ++i);
				createCell(product.getProductTextPaperOnline(context), row, ++i);
				createCell(product.getKeySellingPointsPaper(context), row, ++i);
				createCell(product.getPaperPromotionalText(context), row, ++i);
				createCell(product.getPaperVisualCatchLineExport(context), row, ++i);
				createCell(product.getPaperBannerText(context), row, ++i);
				createCell(convertListToSeparatedValues(product.getKeywordsPaper(context),ConvertorConstants.SEPARATOR), row, ++i);
				createCell(product.getArticleType(), row, ++i);
				createCell(product.getHazardousMaterial(), row, ++i);
				createCell(convertListToSeparatedValues(product.getExternalClassifications(), ConvertorConstants.SEPARATOR), row, ++i);
				createCell(product.getMainGTIN(), row, ++i);
				createCell(convertListToSeparatedValues(product.getGTIN(), ConvertorConstants.SEPARATOR), row, ++i);
				//LOGGER.info("Return PAckaging List :" + convertListToSeparatedValues(product.getPackagingList(), ConvertorConstants.SEPARATOR) );
				createCell(convertListToSeparatedValues(product.getPackagingList(), ConvertorConstants.SEPARATOR), row, ++i);
				
				//Calculated columns
				createCalcDataCell(product.getReferencedPackagings_calc(), row, ++i);
				createCalcDataCell(product.getBreadcrumbNodes_calc(), row, ++i);
				createCalcDataCell(product.getLinksToExternalVideos_calc(context), row, ++i);
				createCalcDataCell(product.getProductLifeCycleStatus_calc(), row, ++i);
				createCalcDataCell(calculateActiveForCountry(product.getActiveForCountry(), cco), row, ++i);
				createCalcDataCell(product.getAssortmentType_calc(), row, ++i);
				createCalcDataCell(product.getArticleType_calc(), row, ++i);
				createCalcDataCell(product.getPublicationBrand_calc(), row, ++i);
				createCalcDataCell(product.getSpgsCodeCalculated_calc(), row, ++i);
				createCalcDataCell(product.getTaxCode_calc(context), row, ++i);
				createCalcDataCell(product.getWebHierarchyClassificationReferences_calc(), row, ++i);
				createCalcDataCell(product.getProductTitle_calc(context), row, ++i);
				createCalcDataCell(product.getWebProductText_calc(context), row, ++i);
				createCalcDataCell(product.getKeywords_calc(context), row, ++i);
				createCalcDataCell(product.getPrimaryImage_calc(context), row, ++i);	
				createCalcDataCell(product.getAssetPushLocationGenesisStandard_calc(), row, ++i);
				if (all){
					if(calculateActiveAssortment_product(packagingInfo, context, product))
						createCalcDataCell(ConvertorConstants.MARKING, row, ++i);
					else
						createCalcDataCell("", row, ++i);
				}
			}
			else{
				if(!calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR)){
					createCell(convertListToSeparatedValues(product.getWebHierarchyClassificationReferences(), ConvertorConstants.SEPARATOR), row, ++i);
					createCell(product.getAdvantageWebHierarchyID(context), row, ++i);
					createCell(product.getAdvantageBreadCrumbWebHierarchyID(context), row, ++i);
					if (checkInteger(product.getEasyOrderCodeCalculated(context), -1) > 0) {
						createNumericCell(checkInteger(product.getEasyOrderCodeCalculated(context), -1), row, ++i);
					} else {
						createCell(product.getEasyOrderCodeCalculated(context), row, ++i);
					}
				}
				createCell(product.getDescription(context), row, ++i);
				createCell(product.getArticleStandardERPDescription(context), row, ++i);
				createCell(product.getProductTitle(context), row, ++i);
				createCell(product.getProductTitleAdvantage(context), row, ++i);
				createCell(product.getProductTitlePaperAdvantage(context), row, ++i);
				createCell(product.getProductPriceGridFeature(context), row, ++i);
				createCell(product.getCatchLine(context), row, ++i);
				createCell(product.getCommercialText(context), row, ++i);
				createCell(product.getCommercialTextAdvantage(context), row, ++i);
				createCell(product.getEnableInlineContent(context), row, ++i);
				createCell(product.getKeySellingPoints(context), row, ++i);				
				if(product.getKeywords(context)!=null)
					createCell(convertListToSeparatedValues(product.getKeywords(context), ConvertorConstants.SEPARATOR), row, ++i);
				else
					createCell("", row, ++i);
				createCell(product.getWebProductTextAdvantage(context), row, ++i);
				createCell(product.getPrimaryImageEasyOrder(context), row, ++i);
				
				createCell(product.getPrimaryImage(context), row, ++i);
				createCell(product.getPrimaryImageLocal(context), row, ++i);
				createCell(product.getPrimaryImageCentral(), row, ++i);
				createCell(product.getPrimaryImagePaper(context), row, ++i);
				createCell(product.getPrimaryImagePaperLocal(context), row, ++i);
				createCell(product.getPrimaryImagePaperCentral(), row, ++i);
				createCell(convertListToSeparatedValues(product.getSecondaryImages(), ConvertorConstants.SEPARATOR), row, ++i);
				createCell(convertListToSeparatedValues(product.getDocuments(context), ConvertorConstants.SEPARATOR), row, ++i);
				createCell(convertListToSeparatedValues(product.getIcons(context), ConvertorConstants.SEPARATOR), row, ++i);
				createCell(product.getEcoEasy(), row, ++i);
				createCell(convertListToSeparatedValues(product.getAccessoryProducts(), ConvertorConstants.SEPARATOR), row, ++i);
				createCell(convertListToSeparatedValues(product.getAlternativeProducts(), ConvertorConstants.SEPARATOR), row, ++i);
				createCell(product.getProductSubTitleAdvantage(context), row, ++i);
				createCell(product.getProductSubTitlePaperAdvantage(context), row, ++i);
				createCell(product.getProductTextPaperAdvantage(context), row, ++i);
				createCell(product.getArticleType(), row, ++i);
				createCell(product.getHazardousMaterial(), row, ++i);
				createCell(convertListToSeparatedValues(product.getExternalClassifications(), ConvertorConstants.SEPARATOR), row, ++i);
				
				if (checkInteger(product.getMainGTIN(), -1) > 0) {
					createNumericCell(checkInteger(product.getMainGTIN(), -1), row, ++i);
				} else {
					createCell(product.getMainGTIN(), row, ++i);
				}
				if (checkInteger(convertListToSeparatedValues(product.getGTIN(), ConvertorConstants.SEPARATOR), -1) > 0) {
					createNumericCell(checkInteger(convertListToSeparatedValues(product.getGTIN(), ConvertorConstants.SEPARATOR), -1), row, ++i);
				} else {
					createCell(convertListToSeparatedValues(product.getGTIN(), ConvertorConstants.SEPARATOR), row, ++i);
				}
				//LOGGER.info("Return PAckaging List :" + convertListToSeparatedValues(product.getPackagingList(), ConvertorConstants.SEPARATOR) );
				createCell(convertListToSeparatedValues(product.getPackagingList(), ConvertorConstants.SEPARATOR), row, ++i);
				
				createCalcDataCell(product.getReferencedPackagings_calc(), row, ++i);
				createCalcDataCell(product.getProductLifeCycleStatus_calc(), row, ++i);
				createCalcDataCell(calculateActiveForCountry(product.getActiveForCountry(), cco), row, ++i);
				createCalcDataCell(product.getAssortmentType_calc(), row, ++i);
				createCalcDataCell(product.getArticleType_calc(), row, ++i);
				createCalcDataCell(product.getPublicationBrand_calc(), row, ++i);
				createCalcDataCell(product.getSpgsCodeCalculated_calc(), row, ++i);
				if(!calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR))
					createCalcDataCell(product.getWebHierarchyClassificationReferences_calc(), row, ++i);				
				createCalcDataCell(product.getProductTitleAdvantage_calc(context), row, ++i);
				createCalcDataCell(product.getCommercialTextAdvantage_calc(context), row, ++i);
				createCalcDataCell(product.getKeywords_calc(context), row, ++i);
				createCalcDataCell(product.getPrimaryImage_calc(context), row, ++i);
				createCalcDataCell(product.getAssetPushLocationEasyOrderStandard_calc(), row, ++i);
				if (validationFlags.contains(ConvertorConstants.EO) && !(calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR) || calculatedBusinessUnit.equals("SEAUK") || calculatedBusinessUnit.equals("SEAIE")))	
					createCalcDataCell(product.getPrimaryImageEasyOrder_calc(context), row, ++i);
				if (all){
					if(calculateActiveAssortment_product(packagingInfo, context, product))
						createCalcDataCell(ConvertorConstants.MARKING, row, ++i);
					else
						createCalcDataCell("", row, ++i);
				}			
			}
			productRowNo++;
		}
		LOGGER.info("Total Products in XML:" + productList.size());
		LOGGER.info("Products skipped due to duplicates:" + duplicateProductCount);
		LOGGER.info("Products skipped due to missing local code:" + localCodeLessProdCount);
		LOGGER.info("Products skipped due to missing Article Exists In Distribution Chain :" + productsRejectedDueToArticleExistsInDCCount);
		LOGGER.info("Total Products in Excel:" + (productList.size() - duplicateProductCount - localCodeLessProdCount));
		LOGGER.info("Total Products in Excel without Article Exits In DC :" + (productList.size() - duplicateProductCount - productsRejectedDueToArticleExistsInDCCount));
	}

	private Boolean calculateActiveAssortment_product(List<PackagingInfo> packagingInfo, String context, ProductInfo product) 
	{
		for(String packagingID:product.getPackagingList()){
			for(PackagingInfo packageDetails:packagingInfo){
				if(packageDetails.getPackagingID().equals(packagingID)){
					if (packageDetails.getLocalCode(context) == null
							|| packageDetails.getLocalCode(context).isEmpty()
							&& ((calculatedBusinessUnit.contains("96725")
									|| calculatedBusinessUnit.contains("96741")))) {
						if(activeAssortments.containsKey(product.getArticleCode()))
							return true;
					}
					else{
						List<String> localCodes = packageDetails.getLocalCode(context);
						if (localCodes != null && !localCodes.isEmpty())
						{
							for(String currentLocalCode:localCodes){
								if((calculatedBusinessUnit.contains("96725")
										|| calculatedBusinessUnit.contains("96741"))){
									if(activeAssortments.containsKey(product.getArticleCode()))
										return true;
								}
								else{
									if(activeAssortments.containsKey(currentLocalCode))
										return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	private Boolean calculateActiveAssortment_package(PackagingInfo packageDetails, String context,
			String currentLocalCode, Map<String, String> packageArticleCodeRelationship) {
		String articleCode = packageArticleCodeRelationship.get(packageDetails.getPackagingID());
		if (currentLocalCode == null || currentLocalCode.isEmpty()
				&& ((calculatedBusinessUnit.contains("96725") || calculatedBusinessUnit.contains("96741")))) {
			if (articleCode!=null && activeAssortments.containsKey(articleCode))
				return true;
		} else {
			if ((calculatedBusinessUnit.contains("96725") || calculatedBusinessUnit.contains("96741"))) {
				if (articleCode!=null && activeAssortments.containsKey(articleCode))
					return true;
			} else {
				if (activeAssortments.containsKey(currentLocalCode))
					return true;
			}
		}
		return false;
	}

	/**
	 * This method inserts the packaging header information into the Packaging
	 * information sheet
	 * 
	 * @param cs
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void createPackagingHeader(SXSSFSheet cs)
			throws FileNotFoundException, IOException {
		SXSSFRow row;
		
		//Third header row
		row = (SXSSFRow) cs.createRow(2);
		int i = -1;
		
		if (!calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR)) 
			createHeaderCell_package(cs, ConvertorConstants.LOCAL_CODE, row, ++i);
		
		createHeaderCell_package(cs, ConvertorConstants.LEN_CODE, row, ++i);
		createHeaderCell_package(cs, ConvertorConstants.ARTICLE_CODE, row, ++i);
		createHeaderCell_package(cs, ConvertorConstants.PRODUCT_ID, row, ++i);
		createHeaderCell_package(cs, ConvertorConstants.PACKAGING_ID, row, ++i);
		createHeaderCell_package(cs, ConvertorConstants.EU_CODE, row, ++i);
		createHeaderCell_package(cs, ConvertorConstants.PRODUCT_LIFE_CYCLE_STATUS, row, ++i);
		createHeaderCell_package(cs, ConvertorConstants.SALES_UOM_LEGACY, row, ++i);
		
		if (!calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR)) 
			createHeaderCell_package(cs, ConvertorConstants.DISPLAY_UNIT_OF_MEASURE, row, ++i);
		
		createHeaderCell_package(cs, ConvertorConstants.CONTENT_QUANTITY_LEGACY, row, ++i);
		createHeaderCell_package(cs, ConvertorConstants.CONTENT_UOM_LEGACY, row, ++i);
		createHeaderCell_package(cs, ConvertorConstants.CONVERSION_FACTOR_TO_BASE_UNIT, row, ++i);
		
		if (!calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR)){ 
			createHeaderCell_package(cs, ConvertorConstants.WEB_ID, row, ++i);
			createHeaderCell_package(cs, ConvertorConstants.WEB_ID_EXPORT, row, ++i);
		}
		
		createHeaderCell_package(cs, ConvertorConstants.PACKAGING_LINE_LEGACY, row, ++i);
		
		if (!calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR)) 
			createHeaderCell_package(cs, ConvertorConstants.PROMOTIONAL_PRODUCT_PACKAGING_LINE, row, ++i);
		
		if (channel.equals(ConvertorConstants.ONLINE))
			createHeaderCell_package(cs, ConvertorConstants.WEB_PRODUCT_TITLE_ONLINE_LEGACY, row, ++i);
		else
			createHeaderCell_package(cs, ConvertorConstants.WEB_PRODUCT_TITLE_ADVANTAGE_LEGACY, row, ++i);
		
		createHeaderCell_package(cs, ConvertorConstants.MAIN_GTIN, row, ++i);
		createHeaderCell_package(cs, ConvertorConstants.GTIN, row, ++i);
		
		if (ConvertorConstants.SEOFR.equals(calculatedBusinessUnit))
			createHeaderCell_package(cs, ConvertorConstants.TRUNCATED_LOCAL_CODES, row, ++i);
		
		int calcColumnStart = i+1;
		createCalculatedHeaderCell(cs, ConvertorConstants.PRODUCT_LIFE_CYCLE_STATUS, row, ++i);
		createCalculatedHeaderCell(cs, ConvertorConstants.DUPLICATE_PACKAGINGS, row, ++i);
		createCalculatedHeaderCell(cs, ConvertorConstants.PACKAGING_LINE_LEGACY, row, ++i);
		
		if (channel.equals(ConvertorConstants.ONLINE))
			createCalculatedHeaderCell(cs, ConvertorConstants.WEB_PRODUCT_TITLE_ONLINE_LEGACY, row, ++i);
		else
			createCalculatedHeaderCell(cs, ConvertorConstants.WEB_PRODUCT_TITLE_ADVANTAGE_LEGACY, row, ++i);
		
		if (all)
			createCalculatedHeaderCell(cs, ConvertorConstants.ACTIVE_ASSORTMENT, row, ++i);
		
		int statusOnEasyOrder = 0;
		if (validationFlags.contains(ConvertorConstants.EO) && !channel.equals(ConvertorConstants.ONLINE) && !calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR)) {
			statusOnEasyOrder = i+1;
			createCalculatedHeaderCell(cs, ConvertorConstants.NOT_PUBLISHED, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.WEB_TITLE, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.IMAGE_COMING_SOON, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.PRODUCT_IMAGE, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.BRAND_IMAGES, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.ICON_IMAGES, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.PRODUCT_DESCRIPTION, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.BULLET_POINTS, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.ALTERNATIVES, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.RELATED_ITEMS, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.DOCUMENTS, row, ++i);
			createCalculatedHeaderCell(cs, ConvertorConstants.BREADCRUMBS, row, ++i);
		}

		//LOGGER.info("Calculated columns start :"+calcColumnStart);
		
		//First header row
		row = (SXSSFRow)cs.createRow(0);
		createCalculatedHeaderCell(cs, ConvertorConstants.DATA_QUALITY_ASSESSMENT, row, calcColumnStart);
				
		int dataIntegrityViolationsCount = 3;
		/*if (ConvertorConstants.SEOFR.equals(calculatedBusinessUnit))
			dataIntegrityViolationsCount++;*/
		
		//Second header row
		row = (SXSSFRow)cs.createRow(1);		
		createCalculatedHeaderCell(cs, ConvertorConstants.DATA_INTEGRITY_VIOLATIONS, row, calcColumnStart);
		createCalculatedHeaderCell(cs, ConvertorConstants.MISSING_CONTENT, row, calcColumnStart + dataIntegrityViolationsCount);
		if (all)
			createCalculatedHeaderCell(cs, ConvertorConstants.ERP_STATUS, row, calcColumnStart + dataIntegrityViolationsCount + 1);
		
		if (validationFlags.contains(ConvertorConstants.EO) && !channel.equals(ConvertorConstants.ONLINE) && !calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR)) {			
			if (all) {
				createCalculatedHeaderCell(cs, ConvertorConstants.STATUS_ON_EASYORDER, row,
						calcColumnStart + dataIntegrityViolationsCount + 1 + 1);
			} else {
				createCalculatedHeaderCell(cs, ConvertorConstants.STATUS_ON_EASYORDER, row,
						calcColumnStart + dataIntegrityViolationsCount + 1);
			}
		}
		
		//Grouping header columns
		int start = calcColumnStart;
		int end = start + dataIntegrityViolationsCount + 1;
		if (all)
			end ++;
		if(statusOnEasyOrder>0)
			end = end + 12;
		
		cs.addMergedRegion(new CellRangeAddress(0,0,start,end-1));
		
		end = start + dataIntegrityViolationsCount;
		cs.addMergedRegion(new CellRangeAddress(1,1,start,end-1));
		
		start = end;
		start++;
		if (all)
			start++;
		
		if (statusOnEasyOrder > 0) {
			cs.addMergedRegion(new CellRangeAddress(1, 1, start, start + 11));
		}
		
		//Setting the filter
		cs.setAutoFilter(new CellRangeAddress(2,2,0,i));
		
		//Freezing first three columns and first three header rows
		cs.createFreezePane(6,3);
	}
	
	/**
	 * This method inserts the packaging information into the Packaging
	 * information sheet
	 * 
	 * @param cs
	 * @param packageList
	 * @param packageProductRelationship
	 * @param packageArticleCodeRelationship
	 * @param context
	 */
	private void insertPackagingInformation(SXSSFSheet cs,
			List<PackagingInfo> packageList,
			Map<String, String> packageProductRelationship, Map<String, String> packageArticleCodeRelationship, String context) {
		SXSSFRow row;
		
		// First three rows are header rows
		packagingRowNo = 3;
		int loopCounter = 0;
		int localCodeListCount;
		int localCodeLessPackageCount = 0;
		
		for (int j = 1; j <= packageList.size(); j++) 
		{
			PackagingInfo packaging = packageList.get(j - 1);
			
			loopCounter = 0;
			localCodeListCount = 0;
			
			if(packaging.getLocalCode(context)!=null)
				localCodeListCount = packaging.getLocalCode(context).size();
			
			int i = -1;
			do {
				row = (SXSSFRow) cs.createRow(packagingRowNo);
				i = -1;
				String currentLocalCode = "";
				
				if (!ConvertorConstants.FAST_BU_LIST.contains(context)) {
					if(!context.equals("xx-all")) {
						
						if (packaging.getLocalCode(context) == null
								|| packaging.getLocalCode(context).get(loopCounter) == null
								|| packaging.getLocalCode(context).get(loopCounter).isEmpty()) {
							localCodeLessPackageCount++;
							continue;
						}
					}
				}
				
				if (!calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR)){
					if (packaging.getLocalCode(context) != null){
						if(packaging.getLocalCode(context).get(loopCounter) != null){
							currentLocalCode = packaging.getLocalCode(context).get(loopCounter);
							createCell(packaging.getLocalCode(context).get(loopCounter), row, ++i);
						}
					}
					else
						createCell("", row, ++i);
				}
				
				//To be used for writing additional log files
				if(!activeAssortments.isEmpty()){
					activeAssortmentsList.remove(currentLocalCode);
					if(localCodeEUCodeMapping.containsKey(currentLocalCode))
					{
						List<String> euCodeList = localCodeEUCodeMapping.get(currentLocalCode);
						if(!euCodeList.contains(packaging.getPackagingID())){
							euCodeList.add(packaging.getPackagingID());
							localCodeEUCodeMapping.put(currentLocalCode, euCodeList);
						}
					}
					else
					{
						List<String> values = new ArrayList<String>();
						values.add(packaging.getPackagingID());
						localCodeEUCodeMapping.put(currentLocalCode, values);
					}
				}
					
				createCell(packaging.getLENCode(), row, ++i);
				createCell(packageArticleCodeRelationship.get(packaging.getPackagingID()), row, ++i);
				createCell(packageProductRelationship.get(packaging.getPackagingID()), row, ++i);
				createCell(packaging.getPackagingID(), row, ++i);
				createCell(packaging.getEUCode(), row, ++i);
				createCell(packaging.getProductLifeCycleStatus(), row, ++i);
				createCell(packaging.getSalesUOMLegacy(), row, ++i);
				
				if (!calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR)) 
					createCell(packaging.getDisplayUnitOfMeasure(context), row, ++i);
				
				createCell(packaging.getContentQuantityLegacy(), row, ++i);
				createCell(packaging.getContentUOMLegacy(), row, ++i);
				createCell(packaging.getConversionFactorToBaseUnit(), row, ++i);
				
				if (!calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR)){
					Boolean found = false;
					if (packaging.getWebID(context) != null && localCodeListCount > 0){
						List<String> webIDList = packaging.getWebID(context);
						for(String webID:webIDList)
							if(webID.startsWith(currentLocalCode)){
								createCell(webID, row, ++i);
								found = true;
								break;
							}
					}
					if(!found)
						createCell("", row, ++i);
					
					found = false;
					if (packaging.getWebIDExport(context) != null && localCodeListCount > 0){
						List<String> webIDExportList = packaging.getWebIDExport(context);
						for(String webIDExport:webIDExportList)
							if(webIDExport.startsWith(currentLocalCode)){
								createCell(webIDExport, row, ++i);
								found = true;
								break;
							}
					}
					if(!found)
						createCell("", row, ++i);
				}
				
				createCell(packaging.getPackagingLineLegacy(context), row, ++i);
				
				if (!calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR)) 
					createCell(packaging.getPromotionalProductPackagingLine(context), row, ++i);
				
				if (channel.equals(ConvertorConstants.ONLINE))
					createCell(packaging.getWebProductTitleOnlineLegacy(context), row, ++i);
				else
					createCell(packaging.getWebProductTitleAdvantageLegacy(context), row, ++i);
				
				createCell(packaging.getMainGTIN(), row, ++i);
				createCell(convertListToSeparatedValues(packaging.getGTIN(), ConvertorConstants.SEPARATOR), row, ++i);
				
				if (ConvertorConstants.SEOFR.equals(calculatedBusinessUnit)){
					if(currentLocalCode.length() == 4 && isInteger(currentLocalCode))
						createCell(currentLocalCode, row, ++i);
					else
						createCell("", row, ++i);
				}
				
				createCalcDataCell(packaging.getProductLifeCycleStatus_calc(), row, ++i);
				createCalcDataCell(packaging.getDuplicatePackagings_calc(), row, ++i);
				createCalcDataCell(packaging.getPackagingLineLegacy_calc(context), row, ++i);
				
				if (channel.equals(ConvertorConstants.ONLINE))
					createCalcDataCell(packaging.getWebProductTitleOnlineLegacy_calc(context), row, ++i);
				else
					createCalcDataCell(packaging.getWebProductTitleAdvantageLegacy_calc(context), row, ++i);
				
				if (channel.equals(ConvertorConstants.ONLINE)) {
					if (all) {
						if (calculateActiveAssortment_package(packaging, context, currentLocalCode,
								packageArticleCodeRelationship))
							createCalcDataCell(ConvertorConstants.MARKING, row, ++i);
						else
							createCalcDataCell("", row, ++i);
					}
				} else {

					if(all){
						createCalcDataCell(packaging.getActiveAssortment_calc(), row, ++i);
						//createCalcDataCell(gdri(dr,2), row, ++i);
					}
					
					if (validationFlags.contains(ConvertorConstants.EO)
							&& !calculatedBusinessUnit.equals(ConvertorConstants.STAPLESEUR)) {
						createCalcDataCell(packaging.getNotPublished_calc(), row, ++i);
						createCalcDataCell(packaging.getWebTitle_calc(), row, ++i);
						createCalcDataCell(packaging.getImageComingSoon_calc(), row, ++i);
						createCalcDataCell(packaging.getProductImage_calc(), row, ++i);
						createCalcDataCell(packaging.getBrandImages_calc(), row, ++i);
						createCalcDataCell(packaging.getIconImages_calc(), row, ++i);
						createCalcDataCell(packaging.getProductDescription_calc(), row, ++i);
						createCalcDataCell(packaging.getBulletPoints_calc(), row, ++i);
						createCalcDataCell(packaging.getAlternatives_calc(), row, ++i);
						createCalcDataCell(packaging.getRelatedItems_calc(), row, ++i);
						createCalcDataCell(packaging.getDocuments_calc(), row, ++i);
						createCalcDataCell(packaging.getBreadcrumbs_calc(), row, ++i);
						/*createCalcDataCell(gdri(dr,3), row, ++i);
						createCalcDataCell(gdri(dr,4), row, ++i);
						createCalcDataCell(gdri(dr,5), row, ++i);
						createCalcDataCell(gdri(dr,6), row, ++i);
						createCalcDataCell(gdri(dr,7), row, ++i);
						createCalcDataCell(gdri(dr,8), row, ++i);
						createCalcDataCell(gdri(dr,9), row, ++i);
						createCalcDataCell(gdri(dr,10), row, ++i);
						createCalcDataCell(gdri(dr,11), row, ++i);
						createCalcDataCell(gdri(dr,12), row, ++i);
						createCalcDataCell(gdri(dr,13), row, ++i);
						createCalcDataCell(gdri(dr,14), row, ++i);	*/			
					}
				}
				
				packagingRowNo++;
				loopCounter++;

			} while (loopCounter < localCodeListCount);
		}
		/*LOGGER.info("Total Packages in XML:" + packageList.size());
		LOGGER.info("Packages skipped due to missing local code:" + localCodeLessPackageCount);
		LOGGER.info("Total Packages in Excel:" + (packageList.size() - localCodeLessPackageCount));*/
	}
	
	/**
	 * This method is used for creating header cells for Product Information
	 * sheet and also set the style and width accordingly
	 * 
	 * @param cs
	 * @param ct
	 * @param row
	 * @param c
	 */
	public void createHeaderCell(SXSSFSheet cs, String ct, SXSSFRow row, int c) {
		Cell cl = row.createCell(c);
		cl.setCellType(Cell.CELL_TYPE_STRING);
		if (ct != null && ct.length() > 0) {
			cl.setCellValue(ct);
		} else {
			cl.setCellValue("");
		}
		cl.setCellStyle(HEADER_CELL_STYLE);
		if(ConvertorConstants.productColumnsWidth.get(ct)!=null)
			cs.setColumnWidth(c, ConvertorConstants.productColumnsWidth.get(ct));
		else
			cs.setColumnWidth(c, 6000);
	}
	
	/**
	 * This method is used for creating header cells for Packaging Information
	 * sheet and also set the style and width accordingly
	 * 
	 * @param cs
	 * @param ct
	 * @param row
	 * @param c
	 */
	public void createHeaderCell_package(SXSSFSheet cs, String ct, SXSSFRow row, int c) {
		Cell cl = row.createCell(c);
		cl.setCellType(Cell.CELL_TYPE_STRING);
		if (ct != null && ct.length() > 0) {
			cl.setCellValue(ct);
		} else {
			cl.setCellValue("");
		}
		cl.setCellStyle(HEADER_CELL_STYLE);
		if(ConvertorConstants.packageColumnsWidth.get(ct)!=null)
			cs.setColumnWidth(c, ConvertorConstants.packageColumnsWidth.get(ct));
		else
			cs.setColumnWidth(c, 6000);
	}
	
	/**
	 * This method is used for creating header cells for Calculated columns and
	 * also set the style and width accordingly
	 * 
	 * @param cs
	 * @param ct
	 * @param row
	 * @param c
	 */
	public void createCalculatedHeaderCell(SXSSFSheet cs, String columnHeader, SXSSFRow row, int c) {
		Cell cl = row.createCell(c);
		cl.setCellType(Cell.CELL_TYPE_STRING);
		if (columnHeader != null && columnHeader.length() > 0) {
			cl.setCellValue(columnHeader);
		} else {
			cl.setCellValue("");
		}
		
		if(row.getRowNum() < 2)
			cl.setCellStyle(CALC_HEADER_CENTER_CELL_STYLE);
		else
			cl.setCellStyle(CALC_HEADER_CELL_STYLE);
		
		if(ConvertorConstants.productCalculatedColumnsWidth.get(columnHeader)!=null)
			cs.setColumnWidth(c, ConvertorConstants.productCalculatedColumnsWidth.get(columnHeader));
		else
			cs.setColumnWidth(c, 7000);
	}
	
	public void createCell(String ct, SXSSFRow row, int c) {
		Cell cl = row.createCell(c);
		cl.setCellType(Cell.CELL_TYPE_STRING);
		if (ct != null && ct.length() > 0) {
			cl.setCellValue(ct);
		} else {
			cl.setCellValue("");
		}
		cl.setCellStyle(DATA_CELL_STYLE);
	}
	
	public void createNumericCell(int ct, SXSSFRow row, int c) {
		Cell cl = row.createCell(c);
		cl.setCellType(Cell.CELL_TYPE_NUMERIC);
		if (ct >= 0) {
			cl.setCellValue(ct);
		} else {
			cl.setCellValue(-1);
		}
		cl.setCellStyle(DATA_CELL_STYLE);
	}
	
	public void createCalcDataCell(String ct, SXSSFRow row, int c) {
		Cell cl = row.createCell(c);
		cl.setCellType(Cell.CELL_TYPE_STRING);
		if (ct != null && ct.length() > 0) {
			cl.setCellValue(ct);
		} else {
			cl.setCellValue("");
		}
		cl.setCellStyle(CALC_DATA_CELL_STYLE);
	}
	
	public CellStyle headerCellStyle(final SXSSFWorkbook wb) {
		final CellStyle cs = (XSSFCellStyle)wb.createCellStyle();
		cs.setAlignment( CellStyle.ALIGN_LEFT );
		cs.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
		cs.setFillPattern( CellStyle.SOLID_FOREGROUND );
		cs.setBorderBottom(CellStyle.BORDER_THIN );
		cs.setBottomBorderColor(IndexedColors.WHITE.getIndex());
		cs.setBorderLeft( CellStyle.BORDER_THIN );
		cs.setLeftBorderColor(IndexedColors.WHITE.getIndex());
		cs.setBorderRight( CellStyle.BORDER_THIN );
		cs.setRightBorderColor(IndexedColors.WHITE.getIndex());
		cs.setBorderTop(CellStyle.BORDER_THIN );
		cs.setTopBorderColor(IndexedColors.WHITE.getIndex());
		cs.setFont(setHeaderFont(wb));
		return cs;
	}
	
	public CellStyle dataCellStyle(final SXSSFWorkbook wb) {
		final CellStyle cs = (XSSFCellStyle)wb.createCellStyle();
		cs.setAlignment( CellStyle.ALIGN_LEFT );
		cs.setFillPattern( CellStyle.NO_FILL );
		cs.setBorderBottom( CellStyle.BORDER_THIN );
		cs.setBottomBorderColor(IndexedColors.SEA_GREEN.getIndex());
		cs.setBorderLeft( CellStyle.BORDER_THIN );
		cs.setLeftBorderColor(IndexedColors.SEA_GREEN.getIndex());
		cs.setBorderRight( CellStyle.BORDER_THIN );
		cs.setRightBorderColor(IndexedColors.SEA_GREEN.getIndex());
		cs.setBorderTop( CellStyle.BORDER_THIN );
		cs.setTopBorderColor(IndexedColors.SEA_GREEN.getIndex());
		cs.setFont(setDataFont(wb));
		return cs;
	}
	
	public Font setHeaderFont(final SXSSFWorkbook wb) {
		final Font ft = wb.createFont();
		ft.setFontName(ConvertorConstants.FONT_NAME);
		ft.setFontHeightInPoints((short) 11);
		ft.setBoldweight(Font.DEFAULT_CHARSET);
		ft.setColor(IndexedColors.WHITE.getIndex());
		return ft;
	}
	
	public Font setDataFont(final SXSSFWorkbook wb) {
		final Font ft = wb.createFont();
		ft.setFontName(ConvertorConstants.FONT_NAME);
		ft.setFontHeightInPoints((short)11);
		ft.setBoldweight(Font.DEFAULT_CHARSET);
		ft.setColor(Font.COLOR_NORMAL);
		return ft;
	}
	
	public CellStyle calculatedHeaderCellStyle(final SXSSFWorkbook wb) {
		final CellStyle cs = (XSSFCellStyle)wb.createCellStyle();
		cs.setAlignment( CellStyle.ALIGN_LEFT);
		cs.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
		cs.setFillPattern( CellStyle.SOLID_FOREGROUND );
		cs.setBorderBottom(CellStyle.BORDER_THIN );
		cs.setBottomBorderColor(IndexedColors.WHITE.getIndex());
		cs.setBorderLeft( CellStyle.BORDER_THIN );
		cs.setLeftBorderColor(IndexedColors.WHITE.getIndex());
		cs.setBorderRight( CellStyle.BORDER_THIN );
		cs.setRightBorderColor(IndexedColors.WHITE.getIndex());
		cs.setBorderTop(CellStyle.BORDER_THIN );
		cs.setTopBorderColor(IndexedColors.WHITE.getIndex());
		cs.setFont(setHeaderFont(wb));
		return cs;
	}
	
	public CellStyle calculatedHeaderCellStyle_centered(final SXSSFWorkbook wb) {
		final CellStyle cs = (XSSFCellStyle)wb.createCellStyle();
		cs.setAlignment( CellStyle.ALIGN_CENTER);
		cs.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
		cs.setFillPattern( CellStyle.SOLID_FOREGROUND );
		cs.setBorderBottom(CellStyle.BORDER_THIN );
		cs.setBottomBorderColor(IndexedColors.WHITE.getIndex());
		cs.setBorderLeft( CellStyle.BORDER_THIN );
		cs.setLeftBorderColor(IndexedColors.WHITE.getIndex());
		cs.setBorderRight( CellStyle.BORDER_THIN );
		cs.setRightBorderColor(IndexedColors.WHITE.getIndex());
		cs.setBorderTop(CellStyle.BORDER_THIN );
		cs.setTopBorderColor(IndexedColors.WHITE.getIndex());
		cs.setFont(setHeaderFont(wb));
		return cs;
	}

	public CellStyle calculatedDataCellStyle(final SXSSFWorkbook wb) {
		final CellStyle cs = (XSSFCellStyle)wb.createCellStyle();
		cs.setAlignment( CellStyle.ALIGN_CENTER );
		cs.setFillPattern( CellStyle.NO_FILL );
		cs.setBorderBottom( CellStyle.BORDER_THIN );
		cs.setBottomBorderColor(IndexedColors.LIGHT_BLUE.getIndex());
		cs.setBorderLeft( CellStyle.BORDER_THIN );
		cs.setLeftBorderColor(IndexedColors.LIGHT_BLUE.getIndex());
		cs.setBorderRight( CellStyle.BORDER_THIN );
		cs.setRightBorderColor(IndexedColors.LIGHT_BLUE.getIndex());
		cs.setBorderTop( CellStyle.BORDER_THIN );
		cs.setTopBorderColor(IndexedColors.LIGHT_BLUE.getIndex());
		cs.setFont(setDataFont(wb));
		return cs;
	}
	
	/**
	 * Method to convert the input into String with each value separated by the
	 * passed character
	 * 
	 * @param dataList
	 * @param separator
	 * @return
	 */
	private String convertListToSeparatedValues(List<String> dataList, String separator)
	{
		StringBuffer finalString = new StringBuffer();
		if (dataList != null && !dataList.isEmpty()) {
			//LOGGER.info("NOT EMPTY");
			for (int i = 0; i < dataList.size(); i++) {
				if (i == dataList.size() - 1)
					finalString.append(dataList.get(i));
				else
					finalString.append(dataList.get(i)).append(separator);
			}
		}
		//LOGGER.info("RETURNABLE : "+finalString.toString());
		return finalString.toString();
	}
	
	public static String gbcff (String fn) {
		if  (fn.contains("95425")) {
			return "95425-SEOFR";
		} else if  (fn.contains("95429")) {
			return "95429-SEOFR1";
		} else if  (fn.contains("95470")) {
			return "95470-SEOES";
		} else if  (fn.contains("95480")) {
			return "95480-SEOIT";
		} else if  (fn.contains("95485")) {
			return "95485-SEOUK";
		} else if  (fn.contains("95511")) {
			return "95511-SEODE";
		} else if  (fn.contains("96541")) {
			return "96541-SEOPT";
		} else if  (fn.contains("96542")) {
			return "96542-SEONL";
		} else if  (fn.contains("96720")) {
			return "96720-SEANL";
		} else if  (fn.contains("96725")) {
			return "96725-SEAUK";
		} else if  (fn.contains("96730")) {
			return "96730-SEADE";
		} else if  (fn.contains("96775")) {
			return "96775-SEAIT";
		} else if  (fn.contains("96785")) {
			return "96785-SEAFR";
		}  else if  (fn.contains("96790")) {
			return "96790-SEAES";
		}  else if  (fn.contains("96741")) {
			return "96741-SEAIE";
		}  else if  (fn.contains("96763")) {
			return "96763-SEASE";
		}  		
		else if  (fn.contains("96770")) {
			return "96770-SEADK";
		}  else if  (fn.contains("96752")) {
			return "96752-SEANO";
		}  else if  (fn.contains("95465")) {
			return "95465-SEODK";
		}  else if  (fn.contains("96773")) {
			return "96773-SEONO";
		}  else if  (fn.contains("95460")) {
			return "95460-SEOSE";
		}
		else if  (fn.contains("96802")) {
			if  (fn.toUpperCase().contains("DIV")) {
				String substrDivision = fn.substring(fn.toUpperCase().indexOf("DIV")+3);
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
	
	public static String getFileNameCode (String fn) {
		if  (fn.contains("95425")) {
			return "-SEOFR";
		} else if  (fn.contains("95429")) {
			return "-SEOFR1";
		} else if  (fn.contains("95470")) {
			return "-SEOES";
		} else if  (fn.contains("95480")) {
			return "-SEOIT";
		} else if  (fn.contains("95485")) {
			return "-SEOUK";
		} else if  (fn.contains("95511")) {
			return "-SEODE";
		} else if  (fn.contains("96541")) {
			return "-SEOPT";
		} else if  (fn.contains("96542")) {
			return "-SEONL";
		} else if  (fn.contains("96720")) {
			return "-SEANL";
		} else if  (fn.contains("96725")) {
			return "-SEAUK";
		} else if  (fn.contains("96730")) {
			return "-SEADE";
		} else if  (fn.contains("96775")) {
			return "-SEAIT";
		} else if  (fn.contains("96785")) {
			return "-SEAFR";
		}  else if  (fn.contains("96790")) {
			return "-SEAES";
		}  else if  (fn.contains("96741")) {
			return "-SEAIE";
		}  else if  (fn.contains("96763")) {
			return "-SEASE";
		}
		else if  (fn.contains("96770")) {
			return "-SEADK";
		}  else if  (fn.contains("96752")) {
			return "-SEANO";
		}  else if  (fn.contains("95465")) {
			return "-SEODK";
		}  else if  (fn.contains("96773")) {
			return "-SEONO";
		}  else if  (fn.contains("95460")) {
			return "-SEOSE";
		}
		else if  (fn.contains("96802")) {
			if  (fn.toUpperCase().contains("DIV")) {
				String substrDivision = fn.substring(fn.toUpperCase().indexOf("DIV")+3);
				if (substrDivision.indexOf('_') > 0) {
					return "-STAPLESEUR-DIV" + substrDivision.substring(0,substrDivision.indexOf('_'));
				} else {
					if (substrDivision.indexOf('.') > 0) {
						return "-STAPLESEUR-DIV" + substrDivision.substring(0,substrDivision.indexOf('.'));
					} else {
						return "-STAPLESEUR-DIV" + substrDivision;
					}
				}				
			} else {
				return "-STAPLESEUR";
			}
		} else {
			return "-XXXXX";
		}
	}
	
	public String gbcs (String cid) {
		Integer intcid=0;
		try {
			intcid = Integer.parseInt(cid);
		} catch (NumberFormatException e) {
			intcid=0;
		}
		switch (intcid) {
		case 95425:
			return ConvertorConstants.SEOFR;
		case 95429:
			return "SEOFR1";
		case 95470:
			return "SEOES";
		case 95480:
			return "SEOIT";
		case 95485:
			return "SEOUK";
		case 95511:
			return "SEODE";
		case 96541:
			return "SEOPT";
		case 96542:
			return "SEONL";
		case 96720:
			return "SEANL";
		case 96725:
			return "SEAUK";
		case 96730:
			return "SEADE";
		case 96775:
			return "SEAIT";
		case 96785:
			return "SEAFR";
		case 96790:
			return "SEAES";
		case 96741:
			return "SEAIE";
		case 96763:
			return "SEASE";
		case 96802:
			return "STAPLESEUR";
			
		case 96770:
			return "SEADK";
		case 96752:
			return "SEANO";
		case 95465:
			return "SEODK";
		case 96773:
			return "SEONO";
		case 95460:
			return "SEOSE";
			
		default:
			return "XXXXX";
		}
	}
	
	/**
	 * Method to identify the channel based on the current context
	 * 
	 * @param cid
	 * @return
	 */
	public String getChannel(String cid) {
		String abus = "96720, 96725, 96730, 96775, 96785, 96790, 96802, 96741, 96763, xx-all, GL, 96770, 96752";
		if (abus.contains(cid)) {
			return ConvertorConstants.ADVANTAGE;
		} else {
			return ConvertorConstants.ONLINE;
		}
	}
	
	public boolean llc(String activeAssortmentFolderLoc, String context) throws IOException{

		File activeAssortmentFolder = new File(activeAssortmentFolderLoc);
		FileFilterClass zipFileFilter = new FileFilterClass(ConvertorConstants.TXT);
		File[] listOfFiles = activeAssortmentFolder.listFiles(zipFileFilter);		
		
		if(listOfFiles.length == 0) {
			return false;
		} else { 
			String aafbuc = gsbcff(context);
			String caalf=null;
			for(File aalif : listOfFiles) {
				caalf=aalif.getName();
				if (caalf.contains(aafbuc)) {
					rfallc(aalif);
					return true;
				}
			}
			return false;
		}			
	}
	
	private void rfallc(File fin) throws IOException { 
		String ln = null;
		String loc = null;
		try {
			FileInputStream fis = new FileInputStream(fin);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));	
			activeAssortmentsList.clear();
			activeAssortments.clear();
			while ((ln = br.readLine()) != null) {
				loc = ln.substring(0, ln.length()).trim();
				activeAssortments.put(loc, "");
				activeAssortmentsList.add(loc);
			}
			
			//System.out.println("Before removing duplicates"+activeAssortmentsList.size());
			Set<String> hs = new HashSet<>();
			
			//To remove in case its a BOM char
			String firstVal = activeAssortmentsList.get(0);			
			char myChar = firstVal.charAt(0);
		    int intValue = (int) myChar;
		    // Hexa value of BOM = EF BB BF  => int 65279
		    if (intValue == 65279) {
		    	//System.out.println("this file starts with a BOM");
		    	firstVal = firstVal.substring(1);
		    }
			//System.out.println(firstVal);
			activeAssortmentsList.set(0, firstVal);
			
			//To remove duplicates
			hs.addAll(activeAssortmentsList);
			activeAssortmentsList.clear();
			activeAssortmentsList.addAll(hs);
			//System.out.println("After removing duplicates"+activeAssortmentsList.size());
			br.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String calculateActiveForCountry(List<String> list, String cco) {
		String afc = convertListToSeparatedValues(list, ConvertorConstants.SEPARATOR);
		if (afc == null) { 
			return "";
		} else if (afc.length() == 0)  {
			return "";
		} else if (cco == null) { 
			return "";
		} else if (cco.length() != 2) {
			return "";
		} else if (afc.contains(cco)) {
			return "";
		} else {
			return "X";
		}
	}
	
	/**
	 * Method to check if the passed String is a valid integer
	 * 
	 * @param s
	 * @return
	 */
	public boolean isInteger(String s) {
		boolean isValidInteger = false;
		try {
			Integer.parseInt(s);
			isValidInteger = true;
		} catch (NumberFormatException ex) {
			// s is not an integer
		}
		return isValidInteger;
	}
	
	public static String gsbcff (String fn) {
		if  (fn.contains("95425")) {
			return "95425";
		} else if  (fn.contains("95429")) {
			return "95429";
		} else if  (fn.contains("95470")) {
			return "95470";
		} else if  (fn.contains("95480")) {
			return "95480";
		} else if  (fn.contains("95485")) {
			return "95485";
		} else if  (fn.contains("95511")) {
			return "95511";
		} else if  (fn.contains("96541")) {
			return "96541";
		} else if  (fn.contains("96542")) {
			return "96542";
		} else if  (fn.contains("96720")) {
			return "96720";
		} else if  (fn.contains("96725")) {
			return "96725";
		} else if  (fn.contains("96730")) {
			return "96730";
		} else if  (fn.contains("96775")) {
			return "96775";
		} else if  (fn.contains("96785")) {
			return "96785";
		} else if  (fn.contains("96790")) {
			return "96790";
		} else if  (fn.contains("96741")) {
			return "96741";
		}  else if  (fn.contains("96763")) {
			return "96763";
		}  else if  (fn.contains("96802")) {
			return "96802";
		}		
		else if  (fn.contains("96770")) {
			return "96770";
		}  else if  (fn.contains("96752")) {
			return "96752";
		}  else if  (fn.contains("95465")) {
			return "95465";
		}  else if  (fn.contains("96773")) {
			return "96773";
		}  else if  (fn.contains("95460")) {
			return "95460";
		}		
		else {
			return "NNNNN-XXXXX";
		}
	}
	
	public String gdri(String dr, int ite) {
		//
		String b="";
		String a="";
		int l = 0;
		int p = -1;
		if (dr != null) {
			l = dr.length();
		} else {
			l=0;
		}

		for (int i = 0; i < l; i++) {
			p = dr.indexOf(';');
			if (p >= 0) {
				b = dr.substring(0, p);
				a = dr.substring(p + 1);
				dr = a;
			} else {
				b = "";
				a = "";
				dr = "";
				break;
			}
			if (i == ite - 1) {
				if (b.equals("true")) {
					return "X";
				} else if (b.equals("false")) {
					return "";
				} else if (b.equals("0")) {
					return "";
				} else {
					return b;
				}	
			}
		}
		return "";
	}
	
	
}

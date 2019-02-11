package com.staples.eCatalogue.convertor;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;

import com.staples.eCatalogue.POJO.PackagingInfo;
import com.staples.eCatalogue.POJO.ProductInfo;
import com.staples.eCatalogue.constants.ConvertorConstants;

public class CatalogueConvertor {

	static final Logger LOGGER = Logger.getLogger(CatalogueConvertor.class.getName());
	
	static List<ProductInfo> consolidatedProductsList = new ArrayList<ProductInfo>();
	static List<PackagingInfo> consolidatedPackagingList = new ArrayList<PackagingInfo>();
	static Map<String, String> consolidatedPackageProductRelationship = new HashMap<String, String>();
	static Map<String, String> consolidatedPackageArticleCodeRelationship = new HashMap<String, String>();
	static Map<String, String> consolidatedBrandLogoRelationship= new HashMap<String, String>();
	static List<String> contextList = new ArrayList<String>();
	static String previousBU = "";
	static String[] oldArgs;
	
	public static void main(String[] args) {

		try {
			
			//Just to trigger the reports for the last XML file
			if(args[6].equals("Y")){
				ExcelWriter excelWriter = new ExcelWriter();
				excelWriter.createOutputExcel(consolidatedProductsList, consolidatedPackagingList, contextList,
						consolidatedPackageProductRelationship, consolidatedPackageArticleCodeRelationship, consolidatedBrandLogoRelationship,
						oldArgs);
				return;
			}
			LOGGER.info("XML Parsing - Started");
			//for(String arg:args)
			//	System.out.print(arg.toString()+"  ");
			String currentBU = "";
			File inputFile = new File(args[0]);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			XMLHandler userhandler = new XMLHandler();
			saxParser.parse(inputFile, userhandler);
			LOGGER.info("XML Parsing - Complete");				
			// Setting the calculated attributes for each product
			setCalculatedAttributes(userhandler.productsList, userhandler.packagingList, userhandler.contextList, userhandler.assetImageTypeStandard);
			// Setting the calculated attributes for each package
			setCalculatedAttributes_packaging(userhandler.packagingList, userhandler.contextList);
			LOGGER.info("Setting calculated attributes - Complete");
			
			LOGGER.info("Old ProductsList:"+consolidatedProductsList.size());
			LOGGER.info("Old PackagingList:"+consolidatedPackagingList.size());
			
			currentBU = calculateBU(args[5]);
			LOGGER.info("previousBU:"+previousBU);
			LOGGER.info("currentBU:"+currentBU);
						
			LOGGER.info("Parsed ProductsList:"+userhandler.productsList.size());
			LOGGER.info("Parsed PackagingList:"+userhandler.packagingList.size());
			
			if(currentBU.equals(previousBU) || previousBU.isEmpty())
			{
				LOGGER.info("Appending data");
				consolidatedProductsList.addAll(userhandler.productsList);				
				ignoreDuplicates(userhandler.packagingList);
				consolidatedPackageProductRelationship.putAll(userhandler.packageProductRelationship);
				consolidatedPackageArticleCodeRelationship.putAll(userhandler.packageArticleCodeRelationship);
				consolidatedBrandLogoRelationship.putAll(userhandler.brandLogoRelationship);
				contextList = userhandler.contextList;
			}
			else
			{
				LOGGER.info("Creating excel");
				// Creating the excel based on the parsed and calculated data
				ExcelWriter excelWriter = new ExcelWriter();
				//excelWriter.createOutputExcel(userhandler.productsList, userhandler.packagingList, userhandler.contextList,
				//		userhandler.packageProductRelationship, userhandler.packageArticleCodeRelationship, userhandler.brandLogoRelationship,
				//		args);
				excelWriter.createOutputExcel(consolidatedProductsList, consolidatedPackagingList, contextList,
						consolidatedPackageProductRelationship, consolidatedPackageArticleCodeRelationship, consolidatedBrandLogoRelationship,
						oldArgs);
				
				reinitialiseConsolidatedValues();
				
				consolidatedProductsList.addAll(userhandler.productsList);
				ignoreDuplicates(userhandler.packagingList);
				consolidatedPackageProductRelationship.putAll(userhandler.packageProductRelationship);
				consolidatedPackageArticleCodeRelationship.putAll(userhandler.packageArticleCodeRelationship);
				consolidatedBrandLogoRelationship.putAll(userhandler.brandLogoRelationship);
				contextList = userhandler.contextList;
			}

			previousBU = currentBU;
			oldArgs = args;
			
			LOGGER.info("Consolidated ProductsList:"+consolidatedProductsList.size());
			LOGGER.info("Consolidated PackagingList:"+consolidatedPackagingList.size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void ignoreDuplicates(List<PackagingInfo> packagingList) {
		Boolean found = false;
		int i=0;
		for(PackagingInfo packagingInfo:packagingList){
			found = false;
			String currentPackagingID = packagingInfo.getPackagingID();
			for(PackagingInfo consPackagingInfo:consolidatedPackagingList){
				if(consPackagingInfo.getPackagingID().equals(currentPackagingID)){
					found = true;
					i++;
					break;
				}				
			}
			if(!found)
				consolidatedPackagingList.add(packagingInfo);
		}
		LOGGER.info("Skipped adding packages as they were already available: "+i);
	}

	private static String calculateBU(String fileName) {
		try {
			if (fileName.substring(0, 5) != null)
				return fileName.substring(0, 5);
			else
				return fileName;
		} catch (Exception e) {
			return fileName;
		}
	}

	private static void reinitialiseConsolidatedValues() {
		consolidatedProductsList = new ArrayList<ProductInfo>();
		consolidatedPackagingList = new ArrayList<PackagingInfo>();
		consolidatedPackageProductRelationship = new HashMap<String, String>();
		consolidatedPackageArticleCodeRelationship = new HashMap<String, String>();
		consolidatedBrandLogoRelationship= new HashMap<String, String>();		
	}

	/**
	 * This method sets the drl for calculated product attribute columns
	 * 
	 * @param productInfoList
	 * @param packagingList 
	 * @param contextList
	 * @param assetImageTypeStandard
	 */
	private static void setCalculatedAttributes(List<ProductInfo> productInfoList, List<PackagingInfo> packagingList, List<String> contextList, Map<String, String> assetImageTypeStandard) {
		int duplicatePackagingCount;
		for(ProductInfo productInfo:productInfoList){
			if(isNullOrEmpty(productInfo.getPackagingList()))
				productInfo.setReferencedPackagings_calc(ConvertorConstants.MARKING);
			if(productInfo.getBreadcrumbWebHierarchyClassificationReferences() != null)
				productInfo.setBreadcrumbNodes_calc(String.valueOf(productInfo.getBreadcrumbWebHierarchyClassificationReferences().size()));
			if(isNullOrEmpty(productInfo.getProductLifeCycleStatus()))
				productInfo.setProductLifeCycleStatus_calc(ConvertorConstants.MARKING);
			if(isNullOrEmpty(productInfo.getAssortmentType()))
				productInfo.setAssortmentType_calc(ConvertorConstants.MARKING);
			if(isNullOrEmpty(productInfo.getArticleType()))
				productInfo.setArticleType_calc(ConvertorConstants.MARKING);
			if(productInfo.getPublicationBrand() == null || !productInfo.getPublicationBrand().contains("Brands"))
				productInfo.setPublicationBrand_calc(ConvertorConstants.MARKING);
			if(productInfo.getSpgsCodeCalculated() != null && !productInfo.getSpgsCodeCalculated().contains("-"))
				productInfo.setSpgsCodeCalculated_calc(ConvertorConstants.MARKING);
			for(String context:contextList){
				if(!isNullOrEmpty(productInfo.getLinksToExternalVideos(context)) && productInfo.getLinksToExternalVideos(context).toString().contains("iframe"))
					productInfo.setLinksToExternalVideos_calc(context, ConvertorConstants.MARKING);
				//TODO Change condition
				//if(isNullOrEmpty(productInfo.getActiveForCountry(context)))
				//	productInfo.setActiveForCountry_calc(context, ConvertorConstants.MARKING);
				if(isNullOrEmpty(productInfo.getTaxCode(context)))
					productInfo.setTaxCode_calc(context, ConvertorConstants.MARKING);
				if(isNullOrEmpty(productInfo.getProductTitle(context)))
					productInfo.setProductTitle_calc(context, ConvertorConstants.MARKING);
				if(isNullOrEmpty(productInfo.getWebProductText(context)))
					productInfo.setWebProductText_calc(context, ConvertorConstants.MARKING);
				if(isNullOrEmpty(productInfo.getProductTitleAdvantage(context)) && isNullOrEmpty(productInfo.getProductTitle(context)))
					productInfo.setProductTitleAdvantage_calc(context, ConvertorConstants.MARKING);
				if(isNullOrEmpty(productInfo.getCommercialTextAdvantage(context)) && isNullOrEmpty(productInfo.getCommercialText(context)))
					productInfo.setCommercialTextAdvantage_calc(context, ConvertorConstants.MARKING);
				if(isNullOrEmpty(productInfo.getKeywords(context)))
					productInfo.setKeywords_calc(context, ConvertorConstants.MARKING);
				if(isNullOrEmpty(productInfo.getPrimaryImage(context)))
					productInfo.setPrimaryImage_calc(context, ConvertorConstants.MARKING);
				//if(isNullOrEmpty(productInfo.getPrimaryImage(context)))
					//productInfo.setPrimaryImageEasyOrder_calc(context, ConvertorConstants.MARKING);
				if(!isNullOrEmpty(productInfo.getPrimaryImage(context))){
					String value = null;
					value = assetImageTypeStandard.get(productInfo.getPrimaryImage(context) + ConvertorConstants.GENESIS_STANDARD);
					if (value != null)
						productInfo.setAssetPushLocationGenesisStandard_calc(value);
					else
						productInfo.setAssetPushLocationGenesisStandard_calc(ConvertorConstants.NO_CONTENT);
					
					value = assetImageTypeStandard.get(productInfo.getPrimaryImage(context) + ConvertorConstants.EASY_ORDER_STANDARD);
					if (value != null){
						if(!value.startsWith(ConvertorConstants.OK))
							productInfo.setAssetPushLocationEasyOrderStandard_calc(value);
					}
					//else
						//productInfo.setAssetPushLocationEasyOrderStandard_calc(ConvertorConstants.NO_CONTENT);
				}
			}
			if(isNullOrEmpty(productInfo.getWebHierarchyClassificationReferences()))
				productInfo.setWebHierarchyClassificationReferences_calc(ConvertorConstants.MARKING);			
			if(isNullOrEmpty(productInfo.getProductLifeCycleStatus()))
				productInfo.setProductLifeCycleStatus_calc(ConvertorConstants.MARKING);
			//TODO Change condition - Active assortment
			if(!isNullOrEmpty(productInfo.getActiveAssortment_calc()))
				productInfo.setActiveAssortment_calc(ConvertorConstants.MARKING);
			
			//Getting count of duplicate packagings
			List<String> listOfTiedPackages = productInfo.getPackagingList();			
			for (int i = 0; i < listOfTiedPackages.size(); i++) {
				PackagingInfo packagingInfo = getPackagingInfo(packagingList, listOfTiedPackages.get(i));
				
				duplicatePackagingCount = 0;
				for (int k = 0; k < listOfTiedPackages.size(); k++) {
					PackagingInfo loopPackagingInfo = getPackagingInfo(packagingList, listOfTiedPackages.get(k));

					if (packagingInfo != null && loopPackagingInfo != null
							&& !loopPackagingInfo.getPackagingID().equals(packagingInfo.getPackagingID())) {
						if (packagingInfo.getSalesUOMLegacy() != null && loopPackagingInfo.getSalesUOMLegacy() != null
								&& packagingInfo.getConversionFactorToBaseUnit() != null
								&& loopPackagingInfo.getConversionFactorToBaseUnit() != null) {
							if (packagingInfo.getSalesUOMLegacy().equals(loopPackagingInfo.getSalesUOMLegacy())
									&& packagingInfo.getConversionFactorToBaseUnit()
											.equals(loopPackagingInfo.getConversionFactorToBaseUnit())) {
								duplicatePackagingCount++;
							}
						}
					}
				}
				if (packagingInfo != null)
					packagingInfo.setDuplicatePackagings_calc(String.valueOf(duplicatePackagingCount));
			}
		}
	}
	
	private static PackagingInfo getPackagingInfo(List<PackagingInfo> packagingList, String packagingID) {
		for (PackagingInfo packagingInfo : packagingList) {
			if (packagingInfo.getPackagingID().equals(packagingID))
				return packagingInfo;
		}
		return null;
	}

	/**
	 * This method sets the drl for calculated packaging attribute columns
	 * 
	 * @param packagingList
	 * @param contextList
	 */
	private static void setCalculatedAttributes_packaging(List<PackagingInfo> packagingList, List<String> contextList) {
		for(PackagingInfo packagingInfo:packagingList){
			if(isNullOrEmpty(packagingInfo.getProductLifeCycleStatus()))
				packagingInfo.setProductLifeCycleStatus_calc(ConvertorConstants.MARKING);
			for(String context:contextList){
				if(isNullOrEmpty(packagingInfo.getPackagingLineLegacy(context)))
					packagingInfo.setPackagingLineLegacy_calc(context, ConvertorConstants.MARKING);
				if(isNullOrEmpty(packagingInfo.getWebProductTitleOnlineLegacy(context)))
					packagingInfo.setWebProductTitleOnlineLegacy_calc(context, ConvertorConstants.MARKING);
				if(isNullOrEmpty(packagingInfo.getWebProductTitleAdvantageLegacy(context)))
					packagingInfo.setWebProductTitleAdvantageLegacy_calc(context, ConvertorConstants.MARKING);
			}
		}
	}
	
	/**
	 * Check if input String is null or empty
	 * 
	 * @param value
	 * @return
	 */
	private static Boolean isNullOrEmpty(String value){
		if(value == null || value.trim().isEmpty())
			return true;
		else 
			return false;
	}
	
	/**
	 * Check if input list is null or empty
	 * 
	 * @param value
	 * @return
	 */
	private static Boolean isNullOrEmpty(List<String> value){
		if(value == null || value.isEmpty())
			return true;
		else 
			return false;
	}

}

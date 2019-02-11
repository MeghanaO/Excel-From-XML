package com.staples.eCatalogue.convertor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.staples.eCatalogue.POJO.PackagingInfo;
import com.staples.eCatalogue.POJO.ProductInfo;
import com.staples.eCatalogue.constants.ConvertorConstants;

public class XMLHandler extends DefaultHandler {
	
	static final Logger LOGGER = Logger.getLogger(XMLHandler.class.getName());	
	
	Boolean valueTag = false;
	Boolean valueGroupOrMultiValueTagFlag = false;
	Boolean productTagFlag = false;
	Boolean sourceProductFlag = false;
	Boolean packageTagFlag = false;
	Boolean assetFlag = false;
	Boolean typologyFlag = false;
	Boolean webHierarchyFlag = false;
	Boolean publicationBrandFlag = false;
	Boolean qualifierFlag = false;
	StringBuilder value = new StringBuilder();
	String attributeName = null;
	String qualifierName = null;
	String currentTopologyID = null;	
	String typologyName = null;	
	String currentBrandID = null;
	String currentAssetID = null;
	String currentQualifierID = null;
	HashMap<String,String> productType = new HashMap<String, String>();	
	List<ProductInfo> productsList = new ArrayList<ProductInfo>();
	ProductInfo productInfo = null;
	List<PackagingInfo> packagingList = new ArrayList<PackagingInfo>();
	PackagingInfo packagingInfo = null;
	List<String> contextList = new ArrayList<String>();	
	Map<String, String> packageProductRelationship = new HashMap<String, String>();
	Map<String, String> packageArticleCodeRelationship = new HashMap<String, String>();
	Map<String, String> brandLogoRelationship= new HashMap<String, String>();
	Map<String, String> assetImageTypeStandard = new HashMap<String, String>();
	Map<String, List<String>> qualifierContextMapping = new HashMap<String, List<String>>();
	String webHierarchy = null;
	String contextID = null;
	
	@Override
	public void startElement(String arg0, String arg1, String arg2,
			Attributes arg3) throws SAXException {
		//LOGGER.info("Element ->"+arg2);
		if(arg2.equals(ConvertorConstants.PRODUCT))
		{
			if(arg3.getValue(ConvertorConstants.USER_TYPE_ID) != null)
			{
				//LOGGER.info("USER_TYPE_ID ->"+arg3.getValue(ConvertorConstants.USER_TYPE_ID));
				if (arg3.getValue(ConvertorConstants.USER_TYPE_ID).equals(ConvertorConstants.PRODUCT) ||
						arg3.getValue(ConvertorConstants.USER_TYPE_ID).equals("SourceProduct")) {
					if(contextList.isEmpty())
					{
						System.out.println("Only one context available");
						if(contextID!=null){
							contextList.add(contextID);
							LOGGER.info("Adding " +contextID +" to the context list");							
						}
					}
					productTagFlag = true;
					
					if(arg3.getValue(ConvertorConstants.USER_TYPE_ID).equals("SourceProduct"))
						sourceProductFlag = true;
					else
						sourceProductFlag = false;
					productInfo = new ProductInfo();
					productInfo.setProductID(arg3.getValue(ConvertorConstants.ID));
					productInfo.setParentID(currentTopologyID);
					productInfo.setTypologyName(typologyName);
					productInfo.setProductType(productType);
					//LOGGER.info("Product ->"+arg3.getValue(ConvertorConstants.ID));
					//productInfo.setContextID(contextID);
				}
				else if (arg3.getValue(ConvertorConstants.USER_TYPE_ID)
						.startsWith(ConvertorConstants.PACKAGING_DOT)) {
					packageTagFlag = true;
					packagingInfo = new PackagingInfo();
					packagingInfo.setPackagingID(arg3.getValue(ConvertorConstants.ID));
				}
				else if (arg3.getValue(ConvertorConstants.USER_TYPE_ID)
						.equals(ConvertorConstants.TYPOLOGY)) {
					typologyFlag = true;
					currentTopologyID = arg3.getValue(ConvertorConstants.ID);
				}
				else if (arg3.getValue(ConvertorConstants.USER_TYPE_ID)
						.startsWith(ConvertorConstants.PUBLICATION_BRAND)) {
					publicationBrandFlag = true;
					currentBrandID = arg3.getValue(ConvertorConstants.ID);
				}
			}
		}
		else if(arg2.equals(ConvertorConstants.ASSET)){
			assetFlag = true;
			currentAssetID = arg3.getValue(ConvertorConstants.ID);
		}
		
		if(arg2.equals(ConvertorConstants.VALUE))
		{
			valueTag = true;
			value = new StringBuilder();
			if(arg3.getValue(ConvertorConstants.ATTRIBUTE_ID) != null)
			{
				attributeName = arg3.getValue(ConvertorConstants.ATTRIBUTE_ID);
			}
			if(arg3.getValue(ConvertorConstants.QUALIFIER_ID) != null)
				qualifierName = arg3.getValue(ConvertorConstants.QUALIFIER_ID);
		}
		
		if(arg2.equals(ConvertorConstants.VALUE_GROUP) || arg2.equals(ConvertorConstants.MULTI_VALUE))
		{
			if(arg3.getValue(ConvertorConstants.ATTRIBUTE_ID) != null)
				attributeName = arg3.getValue(ConvertorConstants.ATTRIBUTE_ID);
			valueGroupOrMultiValueTagFlag = true;
		}
		if(valueGroupOrMultiValueTagFlag){
			if(arg3.getValue(ConvertorConstants.QUALIFIER_ID) != null)
				qualifierName = arg3.getValue(ConvertorConstants.QUALIFIER_ID);
			else if(arg3.getValue(ConvertorConstants.DERIVED_CONTEXT_ID) != null)
				qualifierName = arg3.getValue(ConvertorConstants.DERIVED_CONTEXT_ID);
		}
		
		if (arg2.equals(ConvertorConstants.PRODUCT_CROSS_REFERENCE)) {
			if (arg3.getValue(ConvertorConstants.TYPE) != null
					&& (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.PACKAGING_BASE_UNIT)
							|| arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.PACKAGING_ADDITIONAL_UNIT))) {
				String packageID = arg3.getValue(ConvertorConstants.PRODUCT_ID);
				//LOGGER.info("PAckage ID : " +packageID );
				productInfo.addPackagingList(packageID);
				
				String productID = packageProductRelationship.get(packageID);
				if (productID == null) {
					packageProductRelationship.put(packageID, productInfo.getProductID());
				} else {
					if (!productID.contains(productInfo.getProductID())) {
						StringBuffer productIDList = new StringBuffer(productID).append(ConvertorConstants.SEMICOLAN)
								.append(productInfo.getProductID());
						packageProductRelationship.put(packageID, productIDList.toString());
					}
				}
			} else if (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.PUBLICATION_BRAND)) {
				if (arg3.getValue(ConvertorConstants.PRODUCT_ID) != null)
					productInfo.setPublicationBrand(arg3.getValue(ConvertorConstants.PRODUCT_ID));
			} else if (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.COORDINATED_ITEMS)) {
				if (arg3.getValue(ConvertorConstants.PRODUCT_ID) != null)
					productInfo.addCoordinatedItems(arg3.getValue(ConvertorConstants.PRODUCT_ID));
			} else if (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.ACCESSORY_PRODUCT)) {
				if (arg3.getValue(ConvertorConstants.PRODUCT_ID) != null)
					productInfo.addAccessoryProducts(arg3.getValue(ConvertorConstants.PRODUCT_ID));
			} else if (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.ALTERNATIVE_PRODUCT)) {
				if (arg3.getValue(ConvertorConstants.PRODUCT_ID) != null)
					productInfo.addAlternativeProducts(arg3.getValue(ConvertorConstants.PRODUCT_ID));
			}
		}
		else if(arg2.equals(ConvertorConstants.ASSET_CROSS_REFERENCE)){
			//********DOUBT**********************
			List<String> qualifierList = getQualifier(qualifierName);
			if (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.BRAND_LOGO) && publicationBrandFlag) {
				if (arg3.getValue(ConvertorConstants.ASSET_ID) != null){
					String sb = brandLogoRelationship.get(currentBrandID);
					if(sb != null)
						brandLogoRelationship.put(currentBrandID, new StringBuffer(sb).append(ConvertorConstants.SEMICOLAN).append(arg3.getValue(ConvertorConstants.ASSET_ID)).toString());
					else
						brandLogoRelationship.put(currentBrandID, arg3.getValue(ConvertorConstants.ASSET_ID));
				}
			}
			else if (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.PRIMARY_IMAGE_SPACE)) {
				if (arg3.getValue(ConvertorConstants.ASSET_ID) != null){
					for(String context:qualifierList)
						productInfo.setPrimaryImage(context, arg3.getValue(ConvertorConstants.ASSET_ID));
				}
			}
			else if (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.PRIMARY_IMAGE_LOCAL)) {
				if (arg3.getValue(ConvertorConstants.ASSET_ID) != null){
					for(String context:qualifierList)
						productInfo.setPrimaryImageLocal(context, arg3.getValue(ConvertorConstants.ASSET_ID));
				}
			}
			else if (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.PRIMARY_IMAGE_CENTRAL)) {
				if (arg3.getValue(ConvertorConstants.ASSET_ID) != null)
					productInfo.setPrimaryImageCentral(arg3.getValue(ConvertorConstants.ASSET_ID));
			}
			else if (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.PRIMARY_IMAGE_PAPER)) {
				if (arg3.getValue(ConvertorConstants.ASSET_ID) != null){
					for(String context:qualifierList)
						productInfo.setPrimaryImagePaper(context, arg3.getValue(ConvertorConstants.ASSET_ID));
				}
			}
			else if (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.PRIMARY_IMAGE_PAPER_LOCAL)) {
				if (arg3.getValue(ConvertorConstants.ASSET_ID) != null){
					for(String context:qualifierList)
						productInfo.setPrimaryImagePaperLocal(context, arg3.getValue(ConvertorConstants.ASSET_ID));
				}
			}
			else if (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.PRIMARY_IMAGE_PAPER_CENTRAL)) {
				if (arg3.getValue(ConvertorConstants.ASSET_ID) != null)
					productInfo.setPrimaryImagePaperCentral(arg3.getValue(ConvertorConstants.ASSET_ID));
			}
			else if (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.SECONDARY_IMAGE)) {
				if (arg3.getValue(ConvertorConstants.ASSET_ID) != null)
					productInfo.addSecondaryImages(arg3.getValue(ConvertorConstants.ASSET_ID));
			}
			else if (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.DOCUMENTS)) {
				if (arg3.getValue(ConvertorConstants.ASSET_ID) != null){
					for(String context:qualifierList)
						productInfo.addDocuments(context, arg3.getValue(ConvertorConstants.ASSET_ID));
				}
			}
			else if (arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.ICON)) {
				if (arg3.getValue(ConvertorConstants.ASSET_ID) != null){
					//qualifierList = getQualifier(ConvertorConstants.STD_LANG_ALL);
					for(String context:qualifierList)
						productInfo.addIcons(context, arg3.getValue(ConvertorConstants.ASSET_ID));
				}
			}
		}
		else if (productTagFlag && arg2.equals(ConvertorConstants.CLASSIFICATION_REFERENCE)) {
			if (arg3.getValue(ConvertorConstants.TYPE) != null
					&& arg3.getValue(ConvertorConstants.TYPE).equals(ConvertorConstants.WEB_HIERARCHY)) {
				if (arg3.getValue(ConvertorConstants.CLASSIFICATION_ID) != null) {
					webHierarchy = arg3.getValue(ConvertorConstants.CLASSIFICATION_ID);
					webHierarchyFlag = true;
					productInfo.addWebHierarchyClassificationReferences(webHierarchy);
				}
			}
		}
		else if (assetFlag && arg2.equals(ConvertorConstants.ASSET_PUSH_LOCATION)) {			
			if (arg3.getValue(ConvertorConstants.CONFIGURATION_ID) != null && arg3.getValue(ConvertorConstants.STATUS) != null) {
				if(arg3.getValue(ConvertorConstants.CONFIGURATION_ID).equals(ConvertorConstants.GENESIS_STANDARD))
					assetImageTypeStandard.put(currentAssetID + ConvertorConstants.GENESIS_STANDARD, arg3.getValue(ConvertorConstants.STATUS));
				else if(arg3.getValue(ConvertorConstants.CONFIGURATION_ID).equals(ConvertorConstants.EASY_ORDER_STANDARD))
					assetImageTypeStandard.put(currentAssetID + ConvertorConstants.EASY_ORDER_STANDARD, arg3.getValue(ConvertorConstants.STATUS));
			}
		}
		else if(arg2.equals("STEP-ProductInformation")){
			if (arg3.getValue("ContextID") != null){
				contextID = arg3.getValue("ContextID");
			}
		}
		else if(arg2.equals(ConvertorConstants.CONTEXT)){
			//Extracting all the configured contexts
			String context = arg3.getValue(ConvertorConstants.ID);
			if (!contextList.contains(context))
				contextList.add(context);
			
			//Picking different qualifiers and the corresponding contexts mapped to it
			List<String> contextQualifierList = qualifierContextMapping.get(currentQualifierID);
			if (contextQualifierList == null){
				contextQualifierList = new ArrayList<String>();
				contextQualifierList.add(context);
				qualifierContextMapping.put(currentQualifierID, contextQualifierList);
			}
			else
			{
				if (!contextQualifierList.contains(context)){
					contextQualifierList.add(context);
					qualifierContextMapping.put(currentQualifierID, contextQualifierList);
				}
			}
		}
		else if(arg2.equals(ConvertorConstants.QUALIFIER)){
			qualifierFlag = true;
			currentQualifierID = arg3.getValue(ConvertorConstants.ID);
		}
	}
	
	@Override
	public void endElement(String arg0, String arg1, String arg2)
			throws SAXException {
		
		if(valueTag){
			//if(!value.toString().trim().isEmpty())
			
			
			
			//Parsing and storing product information attributes
			if(productTagFlag){
				if(qualifierName == null){
					LOGGER.info("Qualifier List not available.");
					qualifierName = contextID;
				}
				
				
				List<String> qualifierList = getQualifier(qualifierName);
				
				
					
				
				
				switch (attributeName) {
				
				case ConvertorConstants.ARTICLE_CODE:
					productInfo.setArticleCode(value.toString());
					
					for(String packageID : productInfo.getPackagingList()){
						String articleCode = packageArticleCodeRelationship.get(packageID);
						if (articleCode == null) {
							packageArticleCodeRelationship.put(packageID, productInfo.getArticleCode());
						} else {
							if (!articleCode.contains(productInfo.getArticleCode())) {
								StringBuffer productIDList = new StringBuffer(articleCode).append(ConvertorConstants.SEMICOLAN)
										.append(productInfo.getArticleCode());
								packageArticleCodeRelationship.put(packageID, productIDList.toString());
							}
						}
					}
					break;
				case ConvertorConstants.LEN_CODE:
					productInfo.setLenCode(value.toString());
					break;
				case ConvertorConstants.SPGS_CODE_CALCULATED:
					productInfo.setSpgsCodeCalculated(value.toString());
					break;
				case ConvertorConstants.MANUFACTURER_PART_NUMBER:
					productInfo.setManufacturerPartNumber(value.toString());
					break;
				case ConvertorConstants.TAX_CODE:
					for(String context:qualifierList)
						productInfo.setTaxCode(context, value.toString());
					break;
				case ConvertorConstants.PRODUCT_LIFE_CYCLE_STATUS:
					productInfo.setProductLifeCycleStatus(value.toString());
					break;
				case ConvertorConstants.DC_PRODUCT_LIFE_CYCLE_STATUS:
					for(String context:qualifierList)
						productInfo.setDistributionChainProductLifeCycleStatus(context, value.toString());
					break;
				case ConvertorConstants.SITE_PRODUCT_LIFE_CYCLE_STATUS:
					for(String context:qualifierList)
						productInfo.setSiteProductLifeCycleStatus(context, value.toString());
					break;
				case ConvertorConstants.ARTICLE_EXISTS_IN_DC:
					//This attribute is BU dependent
					if(!qualifierName.equals(ConvertorConstants.ADVANTAGE_CHANNEL)) {	
						for(String context:qualifierList)
							productInfo.setArticleExistsInDistributionChain(context, value.toString());
					}
					break;
				case ConvertorConstants.DELIVERING_SITE:
					productInfo.setDeliveringSite(value.toString());
					break;
				case ConvertorConstants.ARTICLE_EXISTS_IN_SITE:
					productInfo.setArticleExistsInSite(value.toString());
					break;
				case ConvertorConstants.SALES_PACKAGING_UNIT:
					for(String context:qualifierList)
						productInfo.setSalesPackagingUnit(context, value.toString());
					break;
				case ConvertorConstants.DC_SALES_PACKAGING_LINE:
					for(String context:qualifierList)
						productInfo.setDistributionChainSalesPackagingLine(context, value.toString());
					break;
				case ConvertorConstants.PRODUCT_LIFE_CYCLE_STATUS_EO:
					for(String context:qualifierList)
						productInfo.setProductLifeCycleStatusEasyOrder(context, value.toString());
					break;
				case ConvertorConstants.WEB_PRODUCT_TITLE_ADVANTAGE:
					for(String context:qualifierList)
						productInfo.setWebProductTitleAdvantage(context, value.toString());
					break;
				case ConvertorConstants.WEB_PRODUCT_TEXT_ADVANTAGE:
					for(String context:qualifierList)
						productInfo.setWebProductTextAdvantage(context, value.toString());
					break;
				case ConvertorConstants.ACTIVE_FOR_COUNTRY:
					productInfo.addActiveForCountry(value.toString());
					break;
				case ConvertorConstants.ACTIVE_FOR_LANGUAGE:
					productInfo.addActiveForLanguage(value.toString());
					break;
				case ConvertorConstants.PRODUCT_TO_BE_PUBLISHED:
					for(String context:qualifierList)
						productInfo.setProductToBePublished(context, value.toString());
					break;
				case ConvertorConstants.PRODUCT_PRIORITY:
					for(String context:qualifierList)
						productInfo.setProductPriority(context, value.toString());
					break;
				case ConvertorConstants.ASSORTMENT_TYPE:
					productInfo.setAssortmentType(value.toString());
					break;
				case ConvertorConstants.BRAND_CALCULATED:
					productInfo.setBrandCalculated(value.toString());
					break;
				case ConvertorConstants.DESCRIPTION:
					for(String context:qualifierList)
						productInfo.setDescription(context, value.toString());
					break;
				case ConvertorConstants.ARTICLE_STANDARD_ERP_DESCRIPTION:
					for(String context:qualifierList)
						productInfo.setArticleStandardERPDescription(context, value.toString());
					break;
				case ConvertorConstants.PRODUCT_TITLE:
					for(String context:qualifierList)
						productInfo.setProductTitle(context, value.toString());
					break;
				case ConvertorConstants.WEB_PRODUCT_TEXT:
					for(String context:qualifierList)
						productInfo.setWebProductText(context, value.toString());
					break;
				case ConvertorConstants.CATCH_LINE:
					for(String context:qualifierList)
						productInfo.setCatchLine(context, value.toString());
					break;
				case ConvertorConstants.COMMERCIAL_TEXT:
					for(String context:qualifierList)
						productInfo.setCommercialText(context, value.toString());
					break;
				case ConvertorConstants.KEY_SELLING_POINTS:
					for(String context:qualifierList)
						productInfo.setKeySellingPoints(context, value.toString());
					break;
				case ConvertorConstants.EXTENDED_SELLING_POINTS:
					for(String context:qualifierList)
						productInfo.setExtendedSellingPoints(context, value.toString());
					break;
				case ConvertorConstants.LINKS_TO_ADDITIONAL_DOCUMENTATION:
					for(String context:qualifierList)
						productInfo.setLinksToAdditionalDocumentation(context, value.toString());
					break;
				case ConvertorConstants.LINKS_TO_EXTERNAL_CONTENT:
					for(String context:qualifierList)
						productInfo.addLinksToExternalContent(context, value.toString());
					break;
				case ConvertorConstants.BRAND_PROMISE_LINE:
					for(String context:qualifierList)
						productInfo.setBrandPromiseLine(context, value.toString());
					break;
				case ConvertorConstants.KEYWORDS:
					for(String context:qualifierList)
						productInfo.addKeywords(context, value.toString());
					break;
				case ConvertorConstants.PRIMARY_IMAGE:
					for(String context:qualifierList)
						productInfo.setPrimaryImageEasyOrder(context, value.toString());
					break;
				case ConvertorConstants.ECO_EASY_TAG:
					productInfo.setEcoEasy(value.toString());
					break;
				case ConvertorConstants.PRODUCT_REFERENCE_COORDINATED_ITEMS:
					for(String context:qualifierList)
						productInfo.setProductReferenceCoordinatedItems(context, value.toString());
					break;
				case ConvertorConstants.LINKS_TO_EXTERNAL_VIDEOS:
					for(String context:qualifierList)
						productInfo.setLinksToExternalVideos(context, value.toString());
					break;
				case ConvertorConstants.ENABLE_EXPERT_REVIEWS:
					for(String context:qualifierList)
						productInfo.setEnableExpertReviews(context, value.toString());
					break;
				case ConvertorConstants.ENABLE_INLINE_CONTENT:
					for(String context:qualifierList)
						productInfo.setEnableInlineContent(context, value.toString());
					break;
				case ConvertorConstants.INSTALLABLE:
					for(String context:qualifierList)
						productInfo.setInstallable(context, value.toString());
					break;
				case ConvertorConstants.INSTALLATION_FEE:
					for(String context:qualifierList)
						productInfo.setInstallationFee(context, value.toString());
					break;
				case ConvertorConstants.PRODUCT_TITLE_PAPER_ONLINE:
					for(String context:qualifierList)
						productInfo.setProductTitlePaperOnline(context, value.toString());
					break;
				case ConvertorConstants.PRODUCT_SUB_TITLE_PAPER_ONLINE:
					for(String context:qualifierList)
						productInfo.setProductSubTitlePaperOnline(context, value.toString());
					break;
				case ConvertorConstants.CATCH_LINE_PAPER:
					for(String context:qualifierList)
						productInfo.setCatchLinePaper(context, value.toString());
					break;
				case ConvertorConstants.PRODUCT_TEXT_PAPER_ONLINE:
					for(String context:qualifierList)
						productInfo.setProductTextPaperOnline(context, value.toString());
					break;
				case ConvertorConstants.KEY_SELLING_POINTS_PAPER:
					for(String context:qualifierList)
						productInfo.setKeySellingPointsPaper(context, value.toString());
					break;				
				case ConvertorConstants.PAPER_PROMOTIONAL_TEXT:
					for(String context:qualifierList)
						productInfo.setPaperPromotionalText(context, value.toString());
					break;
				case ConvertorConstants.PAPER_VISUAL_CATCH_LINE_EXPORT:
					for(String context:qualifierList)
						productInfo.setPaperVisualCatchLineExport(context, value.toString());
					break;
				case ConvertorConstants.PAPER_BANNER_TEXT:
					for(String context:qualifierList)
						productInfo.setPaperBannerText(context, value.toString());
					break;
				case ConvertorConstants.KEYWORDS_PAPER:
					for(String context:qualifierList)
						productInfo.addKeywordsPaper(context, value.toString());
					break;
				case ConvertorConstants.ARTICLE_TYPE:
					productInfo.setArticleType(value.toString());
					break;
				case ConvertorConstants.HAZARDOUS_MATERIAL:
					productInfo.setHazardousMaterial(value.toString());
					break;
				case ConvertorConstants.EXTERNAL_CLASSIFICATIONS:
					productInfo.addExternalClassifications(value.toString());
					break;
				case ConvertorConstants.MAIN_GTIN:
					productInfo.setMainGTIN(value.toString());
					break;
				case ConvertorConstants.GTIN:
					productInfo.addGTIN(value.toString());
					break;
				case ConvertorConstants.ADVANTAGE_WEB_HIERARCHY_ID:
					for(String context:qualifierList)
						productInfo.setAdvantageWebHierarchyID(context, value.toString());
					break;
				case ConvertorConstants.ADVANTAGE_BREAD_CRUMB_WEB_HIERARCHY_ID:
					for(String context:qualifierList)
						productInfo.setAdvantageBreadCrumbWebHierarchyID(context, value.toString());
					break;
				case ConvertorConstants.EASY_ORDER_CODE_CALCULATED:
					for(String context:qualifierList)
						productInfo.setEasyOrderCodeCalculated(context, value.toString());
					break;					
				case ConvertorConstants.PRODUCT_TITLE_ADVANTAGE:
					for (String context : qualifierList)
						productInfo.setProductTitleAdvantage(context, value.toString());
					break;
				case ConvertorConstants.PRODUCT_TITLE_PAPER_ADVANTAGE:
					for (String context : qualifierList)
						productInfo.setProductTitlePaperAdvantage(context, value.toString());
					break;
				case ConvertorConstants.PRODUCT_PRICE_GRID_FEATURE:
					for (String context : qualifierList)
						productInfo.setProductPriceGridFeature(context, value.toString());
					break;					
				case ConvertorConstants.COMMERCIAL_TEXT_ADVANTAGE:
					for (String context : qualifierList)
						productInfo.setCommercialTextAdvantage(context, value.toString());
					break;					
				case ConvertorConstants.PRODUCT_SUBTITLE_ADVANTAGE:
					for (String context : qualifierList)
						productInfo.setProductSubTitleAdvantage(context, value.toString());
					break;					
				case ConvertorConstants.PRODUCT_SUBTITLE_PAPER_ADVANTAGE:
					for (String context : qualifierList)
						productInfo.setProductSubTitlePaperAdvantage(context, value.toString());
					break;
				case ConvertorConstants.PRODUCT_TEXT_PAPER_ADVANTAGE:
					for (String context : qualifierList)
						productInfo.setProductTextPaperAdvantage(context, value.toString());
					break;
				
				}
			}
			//Parsing and storing packaging information attributes
			else if(packageTagFlag){
				List<String> qualifierList = getQualifier(qualifierName);
				if(attributeName.equals(ConvertorConstants.LEN_CODE))
					packagingInfo.setLENCode(value.toString());
				/*else if(attributeName.equals(ConvertorConstants.ARTICLE_CODE))
					packagingInfo.setArticleCode(value.toString());*/
				else if(attributeName.equals(ConvertorConstants.EU_CODE))
					packagingInfo.setEUCode(value.toString());
				else if(attributeName.equals(ConvertorConstants.PRODUCT_LIFE_CYCLE_STATUS))
					packagingInfo.setProductLifeCycleStatus(value.toString());
				else if(attributeName.equals(ConvertorConstants.SALES_UOM_LEGACY))
					packagingInfo.setSalesUOMLegacy(value.toString());
				else if(attributeName.equals(ConvertorConstants.DISPLAY_UNIT_OF_MEASURE)){
					for (String context : qualifierList)
						packagingInfo.setDisplayUnitOfMeasure(context, value.toString());
				}
				else if(attributeName.equals(ConvertorConstants.CONTENT_QUANTITY_LEGACY))
					packagingInfo.setContentQuantityLegacy(value.toString());
				else if(attributeName.equals(ConvertorConstants.CONTENT_UOM_LEGACY))
					packagingInfo.setContentUOMLegacy(value.toString());
				else if(attributeName.equals(ConvertorConstants.CONVERSION_FACTOR_TO_BASE_UNIT))
					packagingInfo.setConversionFactorToBaseUnit(value.toString());
				else if(attributeName.equals(ConvertorConstants.PACKAGING_LINE_LEGACY)){
					for(String context:qualifierList){
						packagingInfo.setPackagingLineLegacy(context, value.toString());
					}
				}
				else if(attributeName.equals(ConvertorConstants.PROMOTIONAL_PRODUCT_PACKAGING_LINE)){
					for (String context : qualifierList)
						packagingInfo.setPromotionalProductPackagingLine(context, value.toString());
				}
				else if(attributeName.equals(ConvertorConstants.WEB_PRODUCT_TITLE_ONLINE_LEGACY)){
					for (String context : qualifierList)
						packagingInfo.setWebProductTitleOnlineLegacy(context, value.toString());
				}
				else if(attributeName.equals(ConvertorConstants.WEB_PRODUCT_TITLE_ADVANTAGE_LEGACY)){
					for (String context : qualifierList)
						packagingInfo.setWebProductTitleAdvantageLegacy(context, value.toString());
				}
				else if(attributeName.equals(ConvertorConstants.MAIN_GTIN))
					packagingInfo.setMainGTIN(value.toString());
				else if(attributeName.equals(ConvertorConstants.GTIN))
					packagingInfo.addGTIN(value.toString());
				else if(attributeName.equals(ConvertorConstants.LOCAL_CODE)){
					for(String context:qualifierList){
						packagingInfo.addLocalCode(context, value.toString());
					}
				}
				else if(attributeName.equals(ConvertorConstants.WEB_ID_EXPORT)){
					for(String context:qualifierList){
						packagingInfo.addWebIDExport(context, value.toString());
					}
				}
				else if(attributeName.equals(ConvertorConstants.WEB_ID)){
					for(String context:qualifierList){
						packagingInfo.addWebID(context, value.toString());
					}
				}
			}
			else if(typologyFlag){
				if(attributeName.equals(ConvertorConstants.PRODUCT_TYPE)){
					List<String> qualifierList = getQualifier(qualifierName);
					for (String context : qualifierList)
						productType.put(context, value.toString());					
				}
			}
			if(webHierarchyFlag && attributeName.equals(ConvertorConstants.BREADCRUMB)){
				if (webHierarchy != null && value.toString().equals(ConvertorConstants.YES)) {
					productInfo.addBreadcrumbWebHierarchyClassificationReferences(webHierarchy);
				}
				webHierarchy = null;
			}
		}
		else if(arg2.equals(ConvertorConstants.PRODUCT)){
			//Resetting the flags and current values after one iteration
			if(productTagFlag){		
				if(!sourceProductFlag)
					productsList.add(productInfo);
				productTagFlag = false;
				sourceProductFlag = false;
				webHierarchy = null;
			}
			else if(packageTagFlag){
				packagingList.add(packagingInfo);
				packageTagFlag = false;
			}
			else if(typologyFlag){
				typologyFlag = false;
				productType = new HashMap<String, String>();
			}
			else if(publicationBrandFlag){
				publicationBrandFlag = false;
				currentBrandID = null;
			}
		}
		else if(arg2.equals(ConvertorConstants.NAME_TAG)){
			if(productTagFlag)
				productInfo.setName(value.toString().trim());
			else if(typologyFlag)
				typologyName = value.toString().trim();
		}
		else if (productTagFlag && arg2.equals(ConvertorConstants.CLASSIFICATION_REFERENCE)){
			webHierarchyFlag = false;
		}
		else if(arg2.equals(ConvertorConstants.ASSET)){
			assetFlag = false;
			currentAssetID = null;
		}
		else if(arg2.equals(ConvertorConstants.QUALIFIER)){
			qualifierFlag = false;
			currentQualifierID = null;
		}
		//if(arg2.equals(ConvertorConstants.VALUE_GROUP) || arg2.equals(ConvertorConstants.MULTI_VALUE))		{
			//attributeName = null;
		//}
		valueTag = false;
		value = null;
	}
	
	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		if(value == null)
			value = new StringBuilder();
		value.append(arg0, arg1, arg2);
		//LOGGER.info("Data:"+value);
	}	
	
	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void endDocument() throws SAXException {
		//System.out.println("Q-L:"+qualifierContextMapping.toString());
	}
	
	/**
	 * This method validates and returns the exact list of contexts based on
	 * Context ID or Qualifier ID
	 * 
	 * @param qualifierName
	 * @return
	 */
	public List<String> getQualifier(String qualifierName){
		List<String> validQualifierList = new ArrayList<String>();
		//For exact match
		if(contextList.contains(qualifierName)){
			validQualifierList.add(qualifierName);
			return validQualifierList;
		}
		
		//For qualifiers which are mapped to different contexts
		if(qualifierContextMapping.containsKey(qualifierName)){
			return qualifierContextMapping.get(qualifierName);
		}
		
		/*if (qualifierName.equals(ConvertorConstants.STD_LANG_ALL) || qualifierName.equals(ConvertorConstants.STD_BU_ALL)
				|| qualifierName.equals(ConvertorConstants.QUALIFIER_ROOT))
			return contextList;*/
		
		//For updating specific BU or language based locales
		/*for(String context:contextList){
			if(context.contains(qualifierName))
				validQualifierList.add(context);
		}*/
		return validQualifierList;
	}
}

package com.staples.eCatalogue.POJO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductInfo {
	private String productID;
	private String articleCode;
	private String lenCode;
	private HashMap<String,String> productType = new HashMap<String, String>();
	private String spgsCodeCalculated;
	private String manufacturerPartNumber;
	private HashMap<String,String> taxCode = new HashMap<String, String>();
	private String productLifeCycleStatus;
	private String articleExistsInSite;
	private HashMap<String,String> distributionChainProductLifeCycleStatus = new HashMap<String, String>();
	private HashMap<String,String> siteProductLifeCycleStatus = new HashMap<String, String>();
	private HashMap<String,String> salesPackagingUnit = new HashMap<String, String>();
	private HashMap<String,String> productLifeCycleStatusEasyOrder = new HashMap<String, String>();
	private HashMap<String,String> webProductTitleAdvantage = new HashMap<String, String>();
	private HashMap<String,String> webProductTextAdvantage = new HashMap<String, String>();
	private HashMap<String,String> articleExistsInDistributionChain = new HashMap<String,String>();
	private HashMap<String,String> distributionChainSalesPackagingLine = new HashMap<String,String>();

	private List<String> activeForCountry = new ArrayList<String>();
	private List<String> activeForLanguage = new ArrayList<String>();
	private HashMap<String,String> productToBePublished = new HashMap<String, String>();
	private HashMap<String,String> productPriority = new HashMap<String, String>();
	private String assortmentType;
	private String brandCalculated;
	private String publicationBrand;
	private List<String> brandLogos = new ArrayList<String>();
	
	private List<String> webHierarchyClassificationReferences = new ArrayList<String>();
	private List<String> breadcrumbWebHierarchyClassificationReferences = new ArrayList<String>();	
	private HashMap<String,String> description = new HashMap<String, String>();
	private HashMap<String,String> articleStandardERPDescription = new HashMap<String, String>();
	private HashMap<String,String> productTitle = new HashMap<String, String>();
	private HashMap<String,String> webProductText = new HashMap<String, String>();
	private HashMap<String,String> catchLine = new HashMap<String, String>();
	private HashMap<String,String> commercialText = new HashMap<String, String>();
	private HashMap<String,String> keySellingPoints = new HashMap<String, String>();
	private HashMap<String,String> extendedSellingPoints = new HashMap<String, String>();
	private HashMap<String,String> linksToAdditionalDocumentation = new HashMap<String, String>();
	private HashMap<String,List<String>> linksToExternalContent = new HashMap<String, List<String>>();
	private HashMap<String,String> brandPromiseLine = new HashMap<String, String>();
	private HashMap<String,List<String>> keywords = new HashMap<String, List<String>>();
	private String deliveringSite;
	private HashMap<String,String> primaryImageEasyOrder = new HashMap<String, String>();
	private HashMap<String,String> primaryImage = new HashMap<String,String>();
	private HashMap<String,String> primaryImageLocal = new HashMap<String,String>();
	private String primaryImageCentral;
	private HashMap<String,String> primaryImagePaper = new HashMap<String,String>();
	private HashMap<String,String> primaryImagePaperLocal = new HashMap<String,String>();
	private String primaryImagePaperCentral;
	private List<String> secondaryImages = new ArrayList<String>();
	private HashMap<String,List<String>> documents = new HashMap<String, List<String>>();
	private HashMap<String,List<String>> icons = new HashMap<String, List<String>>();
	
	private String ecoEasy;
	private List<String> coordinatedItems = new ArrayList<String>();	
	private HashMap<String,String> productReferenceCoordinatedItems = new HashMap<String, String>();
	private HashMap<String,String> linksToExternalVideos = new HashMap<String, String>();
	private HashMap<String,String> enableExpertReviews = new HashMap<String, String>();
	private HashMap<String,String> enableInlineContent = new HashMap<String, String>();
	private HashMap<String,String> installable = new HashMap<String, String>();
	private HashMap<String,String> installationFee = new HashMap<String, String>();
	private HashMap<String,String> productTitlePaperOnline = new HashMap<String, String>();
	private HashMap<String,String> productSubTitlePaperOnline = new HashMap<String, String>();
	private HashMap<String,String> catchLinePaper = new HashMap<String, String>();
	private HashMap<String,String> productTextPaperOnline = new HashMap<String, String>();
	private HashMap<String,String> keySellingPointsPaper = new HashMap<String, String>();
	private HashMap<String,String> paperPromotionalText = new HashMap<String, String>();
	private HashMap<String,String> paperVisualCatchLineExport = new HashMap<String, String>();
	private HashMap<String,String> paperBannerText = new HashMap<String, String>();
	private HashMap<String,List<String>> keywordsPaper = new HashMap<String, List<String>>();
	
	private HashMap<String,String> advantageWebHierarchyID = new HashMap<String, String>();
	private HashMap<String,String> advantageBreadCrumbWebHierarchyID = new HashMap<String, String>();
	private HashMap<String,String> easyOrderCodeCalculated = new HashMap<String, String>();
	private HashMap<String,String> productTitleAdvantage = new HashMap<String, String>();
	private HashMap<String,String> productTitlePaperAdvantage = new HashMap<String, String>();
	private HashMap<String,String> productPriceGridFeature = new HashMap<String, String>();
	private HashMap<String,String> commercialTextAdvantage = new HashMap<String, String>();
	private HashMap<String,String> productSubTitleAdvantage = new HashMap<String, String>();
	private HashMap<String,String> productSubTitlePaperAdvantage = new HashMap<String, String>();
	private HashMap<String,String> productTextPaperAdvantage = new HashMap<String, String>();
	
	private String articleType;
	private String hazardousMaterial;
	private List<String> externalClassifications = new ArrayList<String>();
	private String mainGTIN;
	private List<String> GTIN = new ArrayList<String>();
	
	private String name;
	private String parentID;
	private String packagingIDs;
	private List<String> accessoryProducts = new ArrayList<String>();
	private List<String> alternativeProducts = new ArrayList<String>();
	
	private String typologyName;
	private List<String> packagingList = new ArrayList<String>();
	
	//Calculated Attributes
	private String referencedPackagings_calc;
	private String breadcrumbNodes_calc;
	private HashMap<String,String> linksToExternalVideos_calc = new HashMap<String,String>();
	private String productLifeCycleStatus_calc;
	private HashMap<String,String> activeForCountry_calc = new HashMap<String,String>();
	private String assortmentType_calc;
	private String articleType_calc;
	private String publicationBrand_calc;
	private String spgsCodeCalculated_calc;
	private HashMap<String,String> taxCode_calc = new HashMap<String,String>();
	private String webHierarchyClassificationReferences_calc;
	private HashMap<String,String> productTitle_calc = new HashMap<String,String>();
	private HashMap<String,String> webProductText_calc = new HashMap<String,String>();
	private HashMap<String,String> keywords_calc = new HashMap<String,String>();
	private HashMap<String,String> primaryImage_calc = new HashMap<String,String>();
	private String assetPushLocationGenesisStandard_calc;
	private String activeAssortment_calc;
	private HashMap<String,String> productTitleAdvantage_calc = new HashMap<String,String>();
	private HashMap<String,String> commercialTextAdvantage_calc = new HashMap<String,String>();
	private String assetPushLocationEasyOrderStandard_calc;
	private HashMap<String,String> primaryImageEasyOrder_calc = new HashMap<String,String>();
	
	public String getSpgsCodeCalculated() {
		return spgsCodeCalculated;
	}
	public void setSpgsCodeCalculated(String spgsCodeCalculated) {
		this.spgsCodeCalculated = spgsCodeCalculated;
	}
	public String getManufacturerPartNumber() {
		return manufacturerPartNumber;
	}
	public void setManufacturerPartNumber(String manufacturerPartNumber) {
		this.manufacturerPartNumber = manufacturerPartNumber;
	}
	public String getProductLifeCycleStatus() {
		return productLifeCycleStatus;
	}
	public void setProductLifeCycleStatus(String productLifeCycleStatus) {
		this.productLifeCycleStatus = productLifeCycleStatus;
	}
	public String getAssortmentType() {
		return assortmentType;
	}
	public void setAssortmentType(String assortmentType) {
		this.assortmentType = assortmentType;
	}
	public String getBrandCalculated() {
		return brandCalculated;
	}
	public void setBrandCalculated(String brandCalculated) {
		this.brandCalculated = brandCalculated;
	}
	public HashMap<String, String> getProductTitleAdvantage() {
		return productTitleAdvantage;
	}
	public void setProductTitleAdvantage(HashMap<String, String> productTitleAdvantage) {
		this.productTitleAdvantage = productTitleAdvantage;
	}
	public HashMap<String, String> getCommercialTextAdvantage() {
		return commercialTextAdvantage;
	}
	public void setCommercialTextAdvantage(HashMap<String, String> commercialTextAdvantage) {
		this.commercialTextAdvantage = commercialTextAdvantage;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getArticleCode() {
		return articleCode;
	}
	public void setArticleCode(String articleCode) {
		this.articleCode = articleCode;
	}
	public String getLenCode() {
		return lenCode;
	}
	public void setLenCode(String lenCode) {
		this.lenCode = lenCode;
	}
	public String getArticleExistsInSite() {
		return this.articleExistsInSite;
	}
	public void setArticleExistsInSite(String articleExistsInSite) {
		this.articleExistsInSite = articleExistsInSite;
	}
	public String getDeliveringSite() {
		return this.deliveringSite;
	}
	public void setDeliveringSite(String deliveringSite) {
		this.deliveringSite = deliveringSite;
	}
	public String getProductTitleAdvantage(String context) {
		return productTitleAdvantage.get(context);
	}
	public HashMap<String, String> getProductTitleAdvantageMap() {
		return productTitleAdvantage;	
	}
	public void setProductTitleAdvantage(String context, String value) {
		this.productTitleAdvantage.put(context, value);
	}
	public String getArticleType() {
		return articleType;
	}
	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}
	public String getCommercialTextAdvantage(String context) {
		return commercialTextAdvantage.get(context);
	}
	public HashMap<String, String> getCommercialTextAdvantageMap() {
		return commercialTextAdvantage;	
	}
	public void setCommercialTextAdvantage(String context, String value) {
		this.commercialTextAdvantage.put(context, value);
	}
	public List<String> getPackagingList() {
		return packagingList;
	}
	public void setPackagingList(List<String> packagingList) {
		this.packagingList = packagingList;
	}
	public void addPackagingList(String packagingInfo) {
		this.packagingList.add(packagingInfo);
	}
	public String getEcoEasy() {
		return ecoEasy;
	}
	public void setEcoEasy(String ecoEasy) {
		this.ecoEasy = ecoEasy;
	}
	public String getHazardousMaterial() {
		return hazardousMaterial;
	}
	public void setHazardousMaterial(String hazardousMaterial) {
		this.hazardousMaterial = hazardousMaterial;
	}
	public String getMainGTIN() {
		return mainGTIN;
	}
	public void setMainGTIN(String mainGTIN) {
		this.mainGTIN = mainGTIN;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentID() {
		return parentID;
	}
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
	public String getPublicationBrand() {
		return publicationBrand;
	}
	public void setPublicationBrand(String publicationBrand) {
		this.publicationBrand = publicationBrand;
	}
	public String getPackagingIDs() {
		return packagingIDs;
	}
	public void setPackagingIDs(String packagingIDs) {
		this.packagingIDs = packagingIDs;
	}
	public String getTypologyName() {
		return typologyName;
	}
	public void setTypologyName(String typologyName) {
		this.typologyName = typologyName;
	}
	public String getProductType(String context) {
		return productType.get(context);
	}
	public HashMap<String, String> getProductTypeMap() {
		return productType;	
	}
	public void setProductType(String context, String value) {
		this.productType.put(context, value);
	}
	public HashMap<String, String> getProductType() {
		return productType;
	}
	public void setProductType(HashMap<String, String> productType) {
		this.productType = productType;
	}
	public HashMap<String, String> getProductToBePublished() {
		return productToBePublished;
	}
	public void setProductToBePublished(HashMap<String, String> productToBePublished) {
		this.productToBePublished = productToBePublished;
	}
	public String getProductToBePublished(String context) {
		return productToBePublished.get(context);
	}
	public HashMap<String, String> getProductToBePublishedMap() {
		return productToBePublished;	
	}
	public void setProductToBePublished(String context, String value) {
		this.productToBePublished.put(context, value);
	}
	public HashMap<String, String> getTaxCode() {
		return taxCode;
	}
	public void setTaxCode(HashMap<String, String> taxCode) {
		this.taxCode = taxCode;
	}
	public String getTaxCode(String context) {
		return taxCode.get(context);
	}
	public HashMap<String, String> getTaxCodeMap() {
		return taxCode;	
	}
	public void setTaxCode(String context, String value) {
		this.taxCode.put(context, value);
	}
	public HashMap<String, String> getProductTitle() {
		return productTitle;
	}
	public void setProductTitle(HashMap<String, String> taxCode) {
		this.productTitle = taxCode;
	}
	public String getProductTitle(String context) {
		return productTitle.get(context);
	}
	public HashMap<String, String> getProductTitleMap() {
		return productTitle;	
	}
	public void setProductTitle(String context, String value) {
		this.productTitle.put(context, value);
	}
	public HashMap<String, String> getArticleStandardERPDescription() {
		return articleStandardERPDescription;
	}
	public void setArticleStandardERPDescription(HashMap<String, String> taxCode) {
		this.articleStandardERPDescription = taxCode;
	}
	public String getArticleStandardERPDescription(String context) {
		return articleStandardERPDescription.get(context);
	}
	public HashMap<String, String> getArticleStandardERPDescriptionMap() {
		return articleStandardERPDescription;	
	}
	public void setArticleStandardERPDescription(String context, String value) {
		this.articleStandardERPDescription.put(context, value);
	}
	public HashMap<String, String> getDescription() {
		return description;
	}
	public void setDescription(HashMap<String, String> taxCode) {
		this.description = taxCode;
	}
	public String getDescription(String context) {
		return description.get(context);
	}
	public HashMap<String, String> getDescriptionMap() {
		return description;	
	}
	public void setDescription(String context, String value) {
		this.description.put(context, value);
	}
	public HashMap<String, String> getWebProductText() {
		return webProductText;
	}
	public void setWebProductText(HashMap<String, String> taxCode) {
		this.webProductText = taxCode;
	}
	public String getWebProductText(String context) {
		return webProductText.get(context);
	}
	public HashMap<String, String> getWebProductTextMap() {
		return webProductText;	
	}
	public void setWebProductText(String context, String value) {
		this.webProductText.put(context, value);
	}
	public HashMap<String, String> getCatchLine() {
		return catchLine;
	}
	public void setCatchLine(HashMap<String, String> taxCode) {
		this.catchLine = taxCode;
	}
	public String getCatchLine(String context) {
		return catchLine.get(context);
	}
	public HashMap<String, String> getCatchLineMap() {
		return catchLine;	
	}
	public void setCatchLine(String context, String value) {
		this.catchLine.put(context, value);
	}
	public HashMap<String, String> getCommercialText() {
		return commercialText;
	}
	public void setCommercialText(HashMap<String, String> taxCode) {
		this.commercialText = taxCode;
	}
	public String getCommercialText(String context) {
		return commercialText.get(context);
	}
	public HashMap<String, String> getCommercialTextMap() {
		return commercialText;	
	}
	public void setCommercialText(String context, String value) {
		this.commercialText.put(context, value);
	}
	public HashMap<String, String> getKeySellingPoints() {
		return keySellingPoints;
	}
	public void setKeySellingPoints(HashMap<String, String> taxCode) {
		this.keySellingPoints = taxCode;
	}
	public String getKeySellingPoints(String context) {
		return keySellingPoints.get(context);
	}
	public HashMap<String, String> getKeySellingPointsMap() {
		return keySellingPoints;	
	}
	public void setKeySellingPoints(String context, String value) {
		this.keySellingPoints.put(context, value);
	}
	public HashMap<String, String> getExtendedSellingPoints() {
		return extendedSellingPoints;
	}
	public void setExtendedSellingPoints(HashMap<String, String> taxCode) {
		this.extendedSellingPoints = taxCode;
	}
	public String getExtendedSellingPoints(String context) {
		return extendedSellingPoints.get(context);
	}
	public HashMap<String, String> getExtendedSellingPointsMap() {
		return extendedSellingPoints;	
	}
	public void setExtendedSellingPoints(String context, String value) {
		this.extendedSellingPoints.put(context, value);
	}	
	public HashMap<String, String> getLinksToAdditionalDocumentation() {
		return linksToAdditionalDocumentation;
	}
	public void setLinksToAdditionalDocumentation(HashMap<String, String> taxCode) {
		this.linksToAdditionalDocumentation = taxCode;
	}
	public String getLinksToAdditionalDocumentation(String context) {
		return linksToAdditionalDocumentation.get(context);
	}
	public HashMap<String, String> getLinksToAdditionalDocumentationMap() {
		return linksToAdditionalDocumentation;	
	}
	public void setLinksToAdditionalDocumentation(String context, String value) {
		this.linksToAdditionalDocumentation.put(context, value);
	}
	public HashMap<String, List<String>> getLinksToExternalContent() {
		return linksToExternalContent;
	}
	public void setLinksToExternalContent(HashMap<String, List<String>> linksToExternalContent) {
		this.linksToExternalContent = linksToExternalContent;
	}
	public List<String> getLinksToExternalContent(String context) {
		return linksToExternalContent.get(context);
	}
	public HashMap<String, List<String>> getLinksToExternalContentMap() {
		return linksToExternalContent;	
	}
	public void addLinksToExternalContent(String context, String value) {
		List<String> codeList = linksToExternalContent.get(context);
		if (codeList == null) {
			codeList = new ArrayList<String>();
		}
		codeList.add(value);
		this.linksToExternalContent.put(context, codeList);
	}
	public HashMap<String, List<String>> getKeywords() {
		return keywords;
	}
	public void setKeywords(HashMap<String, List<String>> keywords) {
		this.keywords = keywords;
	}
	public List<String> getKeywords(String context) {
		return keywords.get(context);
	}
	public HashMap<String, List<String>> getKeywordsMap() {
		return keywords;	
	}
	public void addKeywords(String context, String value) {
		List<String> codeList = keywords.get(context);
		if (codeList == null) {
			codeList = new ArrayList<String>();
		}
		codeList.add(value);
		this.keywords.put(context, codeList);
	}	
	public HashMap<String, String> getBrandPromiseLine() {
		return brandPromiseLine;
	}
	public void setBrandPromiseLine(HashMap<String, String> brandPromiseLine) {
		this.brandPromiseLine = brandPromiseLine;
	}
	public String getBrandPromiseLine(String context) {
		return brandPromiseLine.get(context);
	}
	public HashMap<String, String> getBrandPromiseLineMap() {
		return brandPromiseLine;	
	}
	public void setBrandPromiseLine(String context, String value) {
		this.brandPromiseLine.put(context, value);
	}
	public HashMap<String, String> getPrimaryImageEasyOrder() {
		return primaryImageEasyOrder;
	}
	public void setPrimaryImageEasyOrder(HashMap<String, String> primaryImageEasyOrder) {
		this.primaryImageEasyOrder = primaryImageEasyOrder;
	}
	public String getPrimaryImageEasyOrder(String context) {
		return primaryImageEasyOrder.get(context);
	}
	public HashMap<String, String> getPrimaryImageEasyOrderMap() {
		return primaryImageEasyOrder;	
	}
	public void setPrimaryImageEasyOrder(String context, String value) {
		this.primaryImageEasyOrder.put(context, value);
	}
	public HashMap<String, String> getAdvantageWebHierarchyID() {
		return advantageWebHierarchyID;
	}
	public void setAdvantageWebHierarchyID(HashMap<String, String> advantageWebHierarchyID) {
		this.advantageWebHierarchyID = advantageWebHierarchyID;
	}
	public String getAdvantageWebHierarchyID(String context) {
		return advantageWebHierarchyID.get(context);
	}
	public HashMap<String, String> getAdvantageWebHierarchyIDMap() {
		return advantageWebHierarchyID;	
	}
	public void setAdvantageWebHierarchyID(String context, String value) {
		this.advantageWebHierarchyID.put(context, value);
	}
	public HashMap<String, String> getAdvantageBreadCrumbWebHierarchyID() {
		return advantageBreadCrumbWebHierarchyID;
	}
	public void setAdvantageBreadCrumbWebHierarchyID(HashMap<String, String> advantageBreadCrumbWebHierarchyID) {
		this.advantageBreadCrumbWebHierarchyID = advantageBreadCrumbWebHierarchyID;
	}
	public String getAdvantageBreadCrumbWebHierarchyID(String context) {
		return advantageBreadCrumbWebHierarchyID.get(context);
	}
	public HashMap<String, String> getAdvantageBreadCrumbWebHierarchyIDMap() {
		return advantageBreadCrumbWebHierarchyID;	
	}
	public void setAdvantageBreadCrumbWebHierarchyID(String context, String value) {
		this.advantageBreadCrumbWebHierarchyID.put(context, value);
	}
	public HashMap<String, String> getEasyOrderCodeCalculated() {
		return easyOrderCodeCalculated;
	}
	public void setEasyOrderCodeCalculated(HashMap<String, String> easyOrderCodeCalculated) {
		this.easyOrderCodeCalculated = easyOrderCodeCalculated;
	}
	public String getEasyOrderCodeCalculated(String context) {
		return easyOrderCodeCalculated.get(context);
	}
	public HashMap<String, String> getEasyOrderCodeCalculatedMap() {
		return easyOrderCodeCalculated;	
	}
	public void setEasyOrderCodeCalculated(String context, String value) {
		this.easyOrderCodeCalculated.put(context, value);
	}
	public HashMap<String, String> getProductTitlePaperAdvantage() {
		return productTitlePaperAdvantage;
	}
	public void setProductTitlePaperAdvantage(HashMap<String, String> productTitlePaperAdvantage) {
		this.productTitlePaperAdvantage = productTitlePaperAdvantage;
	}
	public String getProductTitlePaperAdvantage(String context) {
		return productTitlePaperAdvantage.get(context);
	}
	public HashMap<String, String> getProductTitlePaperAdvantageMap() {
		return productTitlePaperAdvantage;	
	}
	public void setProductTitlePaperAdvantage(String context, String value) {
		this.productTitlePaperAdvantage.put(context, value);
	}
	public HashMap<String, String> getProductPriceGridFeature() {
		return productPriceGridFeature;
	}
	public void setProductPriceGridFeature(HashMap<String, String> productPriceGridFeature) {
		this.productPriceGridFeature = productPriceGridFeature;
	}
	public String getProductPriceGridFeature(String context) {
		return productPriceGridFeature.get(context);
	}
	public HashMap<String, String> getProductPriceGridFeatureMap() {
		return productPriceGridFeature;	
	}
	public void setProductPriceGridFeature(String context, String value) {
		this.productPriceGridFeature.put(context, value);
	}
	public HashMap<String, String> getProductSubTitleAdvantage() {
		return productSubTitleAdvantage;
	}
	public void setProductSubTitleAdvantage(HashMap<String, String> productSubTitleAdvantage) {
		this.productSubTitleAdvantage = productSubTitleAdvantage;
	}
	public String getProductSubTitleAdvantage(String context) {
		return productSubTitleAdvantage.get(context);
	}
	public HashMap<String, String> getProductSubTitleAdvantageMap() {
		return productSubTitleAdvantage;	
	}
	public void setProductSubTitleAdvantage(String context, String value) {
		this.productSubTitleAdvantage.put(context, value);
	}
	public HashMap<String, String> getProductSubTitlePaperAdvantage() {
		return productSubTitlePaperAdvantage;
	}
	public void setProductSubTitlePaperAdvantage(HashMap<String, String> productSubTitlePaperAdvantage) {
		this.productSubTitlePaperAdvantage = productSubTitlePaperAdvantage;
	}
	public String getProductSubTitlePaperAdvantage(String context) {
		return productSubTitlePaperAdvantage.get(context);
	}
	public HashMap<String, String> getProductSubTitlePaperAdvantageMap() {
		return productSubTitlePaperAdvantage;	
	}
	public void setProductSubTitlePaperAdvantage(String context, String value) {
		this.productSubTitlePaperAdvantage.put(context, value);
	}
	public HashMap<String, String> getProductTextPaperAdvantage() {
		return productTextPaperAdvantage;
	}
	public void setProductTextPaperAdvantage(HashMap<String, String> productTextPaperAdvantage) {
		this.productTextPaperAdvantage = productTextPaperAdvantage;
	}
	public String getProductTextPaperAdvantage(String context) {
		return productTextPaperAdvantage.get(context);
	}
	public HashMap<String, String> getProductTextPaperAdvantageMap() {
		return productTextPaperAdvantage;	
	}
	public void setProductTextPaperAdvantage(String context, String value) {
		this.productTextPaperAdvantage.put(context, value);
	}
	public String getReferencedPackagings_calc() {
		return referencedPackagings_calc;
	}
	public void setReferencedPackagings_calc(String referencedPackagings_calc) {
		this.referencedPackagings_calc = referencedPackagings_calc;
	}
	public String getBreadcrumbNodes_calc() {
		return breadcrumbNodes_calc;
	}
	public void setBreadcrumbNodes_calc(String breadcrumbNodes_calc) {
		this.breadcrumbNodes_calc = breadcrumbNodes_calc;
	}
	public String getProductLifeCycleStatus_calc() {
		return productLifeCycleStatus_calc;
	}
	public void setProductLifeCycleStatus_calc(String productLifeCycleStatus_calc) {
		this.productLifeCycleStatus_calc = productLifeCycleStatus_calc;
	}
	public String getAssortmentType_calc() {
		return assortmentType_calc;
	}
	public void setAssortmentType_calc(String assortmentType_calc) {
		this.assortmentType_calc = assortmentType_calc;
	}
	public String getArticleType_calc() {
		return articleType_calc;
	}
	public void setArticleType_calc(String articleType_calc) {
		this.articleType_calc = articleType_calc;
	}
	public String getPublicationBrand_calc() {
		return publicationBrand_calc;
	}
	public void setPublicationBrand_calc(String publicationBrand_calc) {
		this.publicationBrand_calc = publicationBrand_calc;
	}
	public String getSpgsCodeCalculated_calc() {
		return spgsCodeCalculated_calc;
	}
	public void setSpgsCodeCalculated_calc(String spgsCodeCalculated_calc) {
		this.spgsCodeCalculated_calc = spgsCodeCalculated_calc;
	}
	public String getWebHierarchyClassificationReferences_calc() {
		return webHierarchyClassificationReferences_calc;
	}
	public void setWebHierarchyClassificationReferences_calc(String webHierarchyClassificationReferences_calc) {
		this.webHierarchyClassificationReferences_calc = webHierarchyClassificationReferences_calc;
	}
	public String getAssetPushLocationGenesisStandard_calc() {
		return assetPushLocationGenesisStandard_calc;
	}
	public void setAssetPushLocationGenesisStandard_calc(String assetPushLocationGenesisStandard_calc) {
		this.assetPushLocationGenesisStandard_calc = assetPushLocationGenesisStandard_calc;
	}
	public String getActiveAssortment_calc() {
		return activeAssortment_calc;
	}
	public void setActiveAssortment_calc(String activeAssortment_calc) {
		this.activeAssortment_calc = activeAssortment_calc;
	}
	public String getAssetPushLocationEasyOrderStandard_calc() {
		return assetPushLocationEasyOrderStandard_calc;
	}
	public void setAssetPushLocationEasyOrderStandard_calc(String assetPushLocationEasyOrderStandard_calc) {
		this.assetPushLocationEasyOrderStandard_calc = assetPushLocationEasyOrderStandard_calc;
	}
	public HashMap<String, String> getProductPriority() {
		return productPriority;
	}
	public void setProductPriority(HashMap<String, String> productPriority) {
		this.productPriority = productPriority;
	}
	public String getProductPriority(String context) {
		return productPriority.get(context);
	}
	public HashMap<String, String> getProductPriorityMap() {
		return productPriority;	
	}
	public void setProductPriority(String context, String value) {
		this.productPriority.put(context, value);
	}
	public HashMap<String, List<String>> getDocuments() {
		return documents;
	}
	public void setDocuments(HashMap<String, List<String>> documents) {
		this.documents = documents;
	}
	public List<String> getDocuments(String context) {
		return documents.get(context);
	}
	public HashMap<String, List<String>> getDocumentsMap() {
		return documents;	
	}
	public void addDocuments(String context, String value) {
		List<String> codeList = documents.get(context);
		if (codeList == null) {
			codeList = new ArrayList<String>();
		}
		if(!codeList.contains(value))
			codeList.add(value);
		this.documents.put(context, codeList);
	}
	public HashMap<String, List<String>> getIcons() {
		return icons;
	}
	public void setIcons(HashMap<String, List<String>> icons) {
		this.icons = icons;
	}
	public List<String> getIcons(String context) {
		return icons.get(context);
	}
	public HashMap<String, List<String>> getIconsMap() {
		return icons;	
	}
	public void addIcons(String context, String value) {
		List<String> codeList = icons.get(context);
		if (codeList == null) {
			codeList = new ArrayList<String>();
		}
		if(!codeList.contains(value))
			codeList.add(value);
		this.icons.put(context, codeList);
	}
	public List<String> getSecondaryImages() {
		return secondaryImages;
	}
	public void setSecondaryImages(List<String> secondaryImages) {
		this.secondaryImages = secondaryImages;
	}
	public void addSecondaryImages(String secondaryImages) {
		this.secondaryImages.add(secondaryImages);
	}
	public List<String> getCoordinatedItems() {
		return coordinatedItems;
	}
	public void setCoordinatedItems(List<String> coordinatedItems) {
		this.coordinatedItems = coordinatedItems;
	}
	public void addCoordinatedItems(String coordinatedItems) {
		this.coordinatedItems.add(coordinatedItems);
	}
	public HashMap<String, String> getProductReferenceCoordinatedItems() {
		return productReferenceCoordinatedItems;
	}
	public void setProductReferenceCoordinatedItems(HashMap<String, String> productReferenceCoordinatedItems) {
		this.productReferenceCoordinatedItems = productReferenceCoordinatedItems;
	}
	public String getProductReferenceCoordinatedItems(String context) {
		return productReferenceCoordinatedItems.get(context);
	}
	public HashMap<String, String> getProductReferenceCoordinatedItemsMap() {
		return productReferenceCoordinatedItems;	
	}
	public void setProductReferenceCoordinatedItems(String context, String value) {
		this.productReferenceCoordinatedItems.put(context, value);
	}
	public HashMap<String, String> getLinksToExternalVideos() {
		return linksToExternalVideos;
	}
	public void setLinksToExternalVideos(HashMap<String, String> linksToExternalVideos) {
		this.linksToExternalVideos = linksToExternalVideos;
	}
	public String getLinksToExternalVideos(String context) {
		return linksToExternalVideos.get(context);
	}
	public HashMap<String, String> getLinksToExternalVideosMap() {
		return linksToExternalVideos;	
	}
	public void setLinksToExternalVideos(String context, String value) {
		this.linksToExternalVideos.put(context, value);
	}
	public HashMap<String, String> getProductTitlePaperOnline() {
		return productTitlePaperOnline;
	}
	public void setProductTitlePaperOnline(HashMap<String, String> productTitlePaperOnline) {
		this.productTitlePaperOnline = productTitlePaperOnline;
	}
	public String getProductTitlePaperOnline(String context) {
		return productTitlePaperOnline.get(context);
	}
	public HashMap<String, String> getProductTitlePaperOnlineMap() {
		return productTitlePaperOnline;	
	}
	public void setProductTitlePaperOnline(String context, String value) {
		this.productTitlePaperOnline.put(context, value);
	}
	public HashMap<String, String> getProductSubTitlePaperOnline() {
		return productSubTitlePaperOnline;
	}
	public void setProductSubTitlePaperOnline(HashMap<String, String> productSubTitlePaperOnline) {
		this.productSubTitlePaperOnline = productSubTitlePaperOnline;
	}
	public String getProductSubTitlePaperOnline(String context) {
		return productSubTitlePaperOnline.get(context);
	}
	public HashMap<String, String> getProductSubTitlePaperOnlineMap() {
		return productSubTitlePaperOnline;	
	}
	public void setProductSubTitlePaperOnline(String context, String value) {
		this.productSubTitlePaperOnline.put(context, value);
	}
	public HashMap<String, String> getProductTextPaperOnline() {
		return productTextPaperOnline;
	}
	public void setProductTextPaperOnline(HashMap<String, String> productTextPaperOnline) {
		this.productTextPaperOnline = productTextPaperOnline;
	}
	public String getProductTextPaperOnline(String context) {
		return productTextPaperOnline.get(context);
	}
	public HashMap<String, String> getProductTextPaperOnlineMap() {
		return productTextPaperOnline;	
	}
	public void setProductTextPaperOnline(String context, String value) {
		this.productTextPaperOnline.put(context, value);
	}
	public List<String> getExternalClassifications() {
		return externalClassifications;
	}
	public void setExternalClassifications(List<String> externalClassifications) {
		this.externalClassifications = externalClassifications;
	}
	public void addExternalClassifications(String externalClassifications) {
		this.externalClassifications.add(externalClassifications);
	}
	public List<String> getGTIN() {
		return GTIN;
	}
	public void setGTIN(List<String> GTIN) {
		this.GTIN = GTIN;
	}
	public void addGTIN(String GTIN) {
		this.GTIN.add(GTIN);
	}
	public HashMap<String, String> getLinksToExternalVideos_calc() {
		return linksToExternalVideos_calc;
	}
	public void setLinksToExternalVideos_calc(HashMap<String, String> linksToExternalVideos_calc) {
		this.linksToExternalVideos_calc = linksToExternalVideos_calc;
	}
	public String getLinksToExternalVideos_calc(String context) {
		return linksToExternalVideos_calc.get(context);
	}
	public HashMap<String, String> getLinksToExternalVideos_calcMap() {
		return linksToExternalVideos_calc;	
	}
	public void setLinksToExternalVideos_calc(String context, String value) {
		this.linksToExternalVideos_calc.put(context, value);
	}
	public HashMap<String, String> getActiveForCountry_calc() {
		return activeForCountry_calc;
	}
	public void setActiveForCountry_calc(HashMap<String, String> activeForCountry_calc) {
		this.activeForCountry_calc = activeForCountry_calc;
	}
	public String getActiveForCountry_calc(String context) {
		return activeForCountry_calc.get(context);
	}
	public HashMap<String, String> getActiveForCountry_calcMap() {
		return activeForCountry_calc;	
	}
	public void setActiveForCountry_calc(String context, String value) {
		this.activeForCountry_calc.put(context, value);
	}
	public HashMap<String, String> getTaxCode_calc() {
		return taxCode_calc;
	}
	public void setTaxCode_calc(HashMap<String, String> taxCode_calc) {
		this.taxCode_calc = taxCode_calc;
	}
	public String getTaxCode_calc(String context) {
		return taxCode_calc.get(context);
	}
	public HashMap<String, String> getTaxCode_calcMap() {
		return taxCode_calc;	
	}
	public void setTaxCode_calc(String context, String value) {
		this.taxCode_calc.put(context, value);
	}
	public HashMap<String, String> getProductTitle_calc() {
		return productTitle_calc;
	}
	public void setProductTitle_calc(HashMap<String, String> productTitle_calc) {
		this.productTitle_calc = productTitle_calc;
	}
	public String getProductTitle_calc(String context) {
		return productTitle_calc.get(context);
	}
	public HashMap<String, String> getProductTitle_calcMap() {
		return productTitle_calc;	
	}
	public void setProductTitle_calc(String context, String value) {
		this.productTitle_calc.put(context, value);
	}
	public HashMap<String, String> getWebProductText_calc() {
		return webProductText_calc;
	}
	public void setWebProductText_calc(HashMap<String, String> webProductText_calc) {
		this.webProductText_calc = webProductText_calc;
	}
	public String getWebProductText_calc(String context) {
		return webProductText_calc.get(context);
	}
	public HashMap<String, String> getWebProductText_calcMap() {
		return webProductText_calc;	
	}
	public void setWebProductText_calc(String context, String value) {
		this.webProductText_calc.put(context, value);
	}
	public HashMap<String, String> getKeywords_calc() {
		return keywords_calc;
	}
	public void setKeywords_calc(HashMap<String, String> keywords_calc) {
		this.keywords_calc = keywords_calc;
	}
	public String getKeywords_calc(String context) {
		return keywords_calc.get(context);
	}
	public HashMap<String, String> getKeywords_calcMap() {
		return keywords_calc;	
	}
	public void setKeywords_calc(String context, String value) {
		this.keywords_calc.put(context, value);
	}
	public HashMap<String, String> getProductTitleAdvantage_calc() {
		return productTitleAdvantage_calc;
	}
	public void setProductTitleAdvantage_calc(HashMap<String, String> productTitleAdvantage_calc) {
		this.productTitleAdvantage_calc = productTitleAdvantage_calc;
	}
	public String getProductTitleAdvantage_calc(String context) {
		return productTitleAdvantage_calc.get(context);
	}
	public HashMap<String, String> getProductTitleAdvantage_calcMap() {
		return productTitleAdvantage_calc;	
	}
	public void setProductTitleAdvantage_calc(String context, String value) {
		this.productTitleAdvantage_calc.put(context, value);
	}
	public HashMap<String, String> getCommercialTextAdvantage_calc() {
		return commercialTextAdvantage_calc;
	}
	public void setCommercialTextAdvantage_calc(HashMap<String, String> commercialTextAdvantage_calc) {
		this.commercialTextAdvantage_calc = commercialTextAdvantage_calc;
	}
	public String getCommercialTextAdvantage_calc(String context) {
		return commercialTextAdvantage_calc.get(context);
	}
	public HashMap<String, String> getCommercialTextAdvantage_calcMap() {
		return commercialTextAdvantage_calc;	
	}
	public void setCommercialTextAdvantage_calc(String context, String value) {
		this.commercialTextAdvantage_calc.put(context, value);
	}
	public HashMap<String, String> getPrimaryImageEasyOrder_calc() {
		return primaryImageEasyOrder_calc;
	}
	public void setPrimaryImageEasyOrder_calc(HashMap<String, String> primaryImageEasyOrder_calc) {
		this.primaryImageEasyOrder_calc = primaryImageEasyOrder_calc;
	}
	public String getPrimaryImageEasyOrder_calc(String context) {
		return primaryImageEasyOrder_calc.get(context);
	}
	public HashMap<String, String> getPrimaryImageEasyOrder_calcMap() {
		return primaryImageEasyOrder_calc;	
	}
	public void setPrimaryImageEasyOrder_calc(String context, String value) {
		this.primaryImageEasyOrder_calc.put(context, value);
	}
	public HashMap<String, String> getPrimaryImage_calc() {
		return primaryImage_calc;
	}
	public void setPrimaryImage_calc(HashMap<String, String> primaryImage_calc) {
		this.primaryImage_calc = primaryImage_calc;
	}
	public String getPrimaryImage_calc(String context) {
		return primaryImage_calc.get(context);
	}
	public HashMap<String, String> getPrimaryImage_calcMap() {
		return primaryImage_calc;	
	}
	public void setPrimaryImage_calc(String context, String value) {
		this.primaryImage_calc.put(context, value);
	}
	public List<String> getActiveForCountry() {
		return activeForCountry;
	}
	public void setActiveForCountry(List<String> activeForCountry) {
		this.activeForCountry = activeForCountry;
	}
	public void addActiveForCountry(String activeForCountry) {
		this.activeForCountry.add(activeForCountry);
	}
	public List<String> getActiveForLanguage() {
		return activeForLanguage;
	}
	public void setActiveForLanguage(List<String> activeForLanguage) {
		this.activeForLanguage = activeForLanguage;
	}
	public void addActiveForLanguage(String activeForLanguage) {
		this.activeForLanguage.add(activeForLanguage);
	}
	public HashMap<String, String> getEnableExpertReviews() {
		return enableExpertReviews;
	}
	public void setEnableExpertReviews(HashMap<String, String> enableExpertReviews) {
		this.enableExpertReviews = enableExpertReviews;
	}
	public String getEnableExpertReviews(String context) {
		return enableExpertReviews.get(context);
	}
	public HashMap<String, String> getEnableExpertReviewsMap() {
		return enableExpertReviews;	
	}
	public void setEnableExpertReviews(String context, String value) {
		this.enableExpertReviews.put(context, value);
	}
	public HashMap<String, String> getEnableInlineContent() {
		return enableInlineContent;
	}
	public void setEnableInlineContent(HashMap<String, String> enableInlineContent) {
		this.enableInlineContent = enableInlineContent;
	}
	public String getEnableInlineContent(String context) {
		return enableInlineContent.get(context);
	}
	public HashMap<String, String> getEnableInlineContentMap() {
		return enableInlineContent;	
	}
	public void setEnableInlineContent(String context, String value) {
		this.enableInlineContent.put(context, value);
	}
	public HashMap<String, String> getInstallable() {
		return installable;
	}
	public void setInstallable(HashMap<String, String> installable) {
		this.installable = installable;
	}
	public String getInstallable(String context) {
		return installable.get(context);
	}
	public HashMap<String, String> getInstallableMap() {
		return installable;	
	}
	public void setInstallable(String context, String value) {
		this.installable.put(context, value);
	}
	public HashMap<String, String> getInstallationFee() {
		return installationFee;
	}
	public void setInstallationFee(HashMap<String, String> installationFee) {
		this.installationFee = installationFee;
	}
	public String getInstallationFee(String context) {
		return installationFee.get(context);
	}
	public HashMap<String, String> getInstallationFeeMap() {
		return installationFee;	
	}
	public void setInstallationFee(String context, String value) {
		this.installationFee.put(context, value);
	}
	public HashMap<String, String> getCatchLinePaper() {
		return catchLinePaper;
	}
	public void setCatchLinePaper(HashMap<String, String> catchLinePaper) {
		this.catchLinePaper = catchLinePaper;
	}
	public String getCatchLinePaper(String context) {
		return catchLinePaper.get(context);
	}
	public HashMap<String, String> getCatchLinePaperMap() {
		return catchLinePaper;	
	}
	public void setCatchLinePaper(String context, String value) {
		this.catchLinePaper.put(context, value);
	}
	public HashMap<String, String> getKeySellingPointsPaper() {
		return keySellingPointsPaper;
	}
	public void setKeySellingPointsPaper(HashMap<String, String> keySellingPointsPaper) {
		this.keySellingPointsPaper = keySellingPointsPaper;
	}
	public String getKeySellingPointsPaper(String context) {
		return keySellingPointsPaper.get(context);
	}
	public HashMap<String, String> getKeySellingPointsPaperMap() {
		return keySellingPointsPaper;	
	}
	public void setKeySellingPointsPaper(String context, String value) {
		this.keySellingPointsPaper.put(context, value);
	}	
	public HashMap<String, String> getPaperPromotionalText() {
		return paperPromotionalText;
	}
	public void setPaperPromotionalText(HashMap<String, String> paperPromotionalText) {
		this.paperPromotionalText = paperPromotionalText;
	}
	public String getPaperPromotionalText(String context) {
		return paperPromotionalText.get(context);
	}
	public HashMap<String, String> getPaperPromotionalTextMap() {
		return paperPromotionalText;	
	}
	public void setPaperPromotionalText(String context, String value) {
		this.paperPromotionalText.put(context, value);
	}	
	public HashMap<String, String> getPaperVisualCatchLineExport() {
		return paperVisualCatchLineExport;
	}
	public void setPaperVisualCatchLineExport(HashMap<String, String> paperVisualCatchLineExport) {
		this.paperVisualCatchLineExport = paperVisualCatchLineExport;
	}
	public String getPaperVisualCatchLineExport(String context) {
		return paperVisualCatchLineExport.get(context);
	}
	public HashMap<String, String> getPaperVisualCatchLineExportMap() {
		return paperVisualCatchLineExport;	
	}
	public void setPaperVisualCatchLineExport(String context, String value) {
		this.paperVisualCatchLineExport.put(context, value);
	}	
	public HashMap<String, String> getPaperBannerText() {
		return paperBannerText;
	}
	public void setPaperBannerText(HashMap<String, String> paperBannerText) {
		this.paperBannerText = paperBannerText;
	}
	public String getPaperBannerText(String context) {
		return paperBannerText.get(context);
	}
	public HashMap<String, String> getPaperBannerTextMap() {
		return paperBannerText;	
	}
	public void setPaperBannerText(String context, String value) {
		this.paperBannerText.put(context, value);
	}
	public HashMap<String, List<String>> getKeywordsPaper() {
		return keywordsPaper;
	}
	public void setKeywordsPaper(HashMap<String, List<String>> keywordsPaper) {
		this.keywordsPaper = keywordsPaper;
	}
	public List<String> getKeywordsPaper(String context) {
		return keywordsPaper.get(context);
	}
	public HashMap<String, List<String>> getKeywordsPaperMap() {
		return keywordsPaper;	
	}
	public void addKeywordsPaper(String context, String value) {
		List<String> codeList = keywordsPaper.get(context);
		if (codeList == null) {
			codeList = new ArrayList<String>();
		}
		if(!codeList.contains(value))
			codeList.add(value);
		this.keywordsPaper.put(context, codeList);
	}
	public List<String> getAccessoryProducts() {
		return accessoryProducts;
	}
	public void setAccessoryProducts(List<String> accessoryProducts) {
		this.accessoryProducts = accessoryProducts;
	}
	public void addAccessoryProducts(String accessoryProducts) {
		this.accessoryProducts.add(accessoryProducts);
	}
	public List<String> getAlternativeProducts() {
		return alternativeProducts;
	}
	public void setAlternativeProducts(List<String> alternativeProducts) {
		this.alternativeProducts = alternativeProducts;
	}
	public void addAlternativeProducts(String primaryImageCentral) {
		this.alternativeProducts.add(primaryImageCentral);
	}
	public List<String> getBrandLogos() {
		return brandLogos;
	}
	public void setBrandLogos(List<String> brandLogos) {
		this.brandLogos = brandLogos;
	}
	public void addBrandLogos(String brandLogos) {
		this.brandLogos.add(brandLogos);
	}
	public List<String> getWebHierarchyClassificationReferences() {
		return webHierarchyClassificationReferences;
	}
	public void setWebHierarchyClassificationReferences(List<String> webHierarchyClassificationReferences) {
		this.webHierarchyClassificationReferences = webHierarchyClassificationReferences;
	}
	public void addWebHierarchyClassificationReferences(String webHierarchyClassificationReferences) {
		this.webHierarchyClassificationReferences.add(webHierarchyClassificationReferences);
	}
	public List<String> getBreadcrumbWebHierarchyClassificationReferences() {
		return breadcrumbWebHierarchyClassificationReferences;
	}
	public void setBreadcrumbWebHierarchyClassificationReferences(List<String> breadcrumbWebHierarchyClassificationReferences) {
		this.breadcrumbWebHierarchyClassificationReferences = breadcrumbWebHierarchyClassificationReferences;
	}
	public void addBreadcrumbWebHierarchyClassificationReferences(String breadcrumbWebHierarchyClassificationReferences) {
		this.breadcrumbWebHierarchyClassificationReferences.add(breadcrumbWebHierarchyClassificationReferences);
	}
	public HashMap<String, String> getPrimaryImage() {
		return primaryImage;
	}
	public void setPrimaryImage(HashMap<String, String> primaryImage) {
		this.primaryImage = primaryImage;
	}
	public String getPrimaryImage(String context) {
		return primaryImage.get(context);
	}
	public HashMap<String, String> getPrimaryImageMap() {
		return primaryImage;	
	}
	public void setPrimaryImage(String context, String value) {
		this.primaryImage.put(context, value);
	}	
	public HashMap<String, String> getPrimaryImageLocal() {
		return primaryImageLocal;
	}
	public void setPrimaryImageLocal(HashMap<String, String> primaryImageLocal) {
		this.primaryImageLocal = primaryImageLocal;
	}
	public String getPrimaryImageLocal(String context) {
		return primaryImageLocal.get(context);
	}
	public HashMap<String, String> getPrimaryImageLocalMap() {
		return primaryImageLocal;	
	}
	public void setPrimaryImageLocal(String context, String value) {
		this.primaryImageLocal.put(context, value);
	}
	public HashMap<String, String> getPrimaryImagePaper() {
		return primaryImagePaper;
	}
	public void setPrimaryImagePaper(HashMap<String, String> primaryImagePaper) {
		this.primaryImagePaper = primaryImagePaper;
	}
	public String getPrimaryImagePaper(String context) {
		return primaryImagePaper.get(context);
	}
	public HashMap<String, String> getPrimaryImagePaperMap() {
		return primaryImagePaper;	
	}
	public void setPrimaryImagePaper(String context, String value) {
		this.primaryImagePaper.put(context, value);
	}	
	public HashMap<String, String> getPrimaryImagePaperLocal() {
		return primaryImagePaperLocal;
	}
	public void setPrimaryImagePaperLocal(HashMap<String, String> primaryImagePaperLocal) {
		this.primaryImagePaperLocal = primaryImagePaperLocal;
	}
	public String getPrimaryImagePaperLocal(String context) {
		return primaryImagePaperLocal.get(context);
	}
	public HashMap<String, String> getPrimaryImagePaperLocalMap() {
		return primaryImagePaperLocal;	
	}
	public void setPrimaryImagePaperLocal(String context, String value) {
		this.primaryImagePaperLocal.put(context, value);
	}
	public String getPrimaryImageCentral() {
		return primaryImageCentral;
	}
	public void setPrimaryImageCentral(String primaryImageCentral) {
		this.primaryImageCentral = primaryImageCentral;
	}
	public String getPrimaryImagePaperCentral() {
		return primaryImagePaperCentral;
	}
	public void setPrimaryImagePaperCentral(String primaryImagePaperCentral) {
		this.primaryImagePaperCentral = primaryImagePaperCentral;
	}
	public HashMap<String, String> getDistributionChainProductLifeCycleStatus() {
		return distributionChainProductLifeCycleStatus;
	}
	public void setDistributionChainProductLifeCycleStatus(HashMap<String, String> distributionChainProductLifeCycleStatus) {
		this.distributionChainProductLifeCycleStatus = distributionChainProductLifeCycleStatus;
	}
	public String getDistributionChainProductLifeCycleStatus(String context) {
		return distributionChainProductLifeCycleStatus.get(context);
	}
	public HashMap<String, String> getDistributionChainProductLifeCycleStatusMap() {
		return distributionChainProductLifeCycleStatus;	
	}
	public void setDistributionChainProductLifeCycleStatus(String context, String value) {
		this.distributionChainProductLifeCycleStatus.put(context, value);
	}
	public HashMap<String, String> getSiteProductLifeCycleStatus() {
		return siteProductLifeCycleStatus;
	}
	public void setSiteProductLifeCycleStatus(HashMap<String, String> siteProductLifeCycleStatus) {
		this.siteProductLifeCycleStatus = siteProductLifeCycleStatus;
	}
	public String getSiteProductLifeCycleStatus(String context) {
		return siteProductLifeCycleStatus.get(context);
	}
	public HashMap<String, String> getSiteProductLifeCycleStatusMap() {
		return siteProductLifeCycleStatus;	
	}
	public void setSiteProductLifeCycleStatus(String context, String value) {
		this.siteProductLifeCycleStatus.put(context, value);
	}
	public HashMap<String, String> getSalesPackagingUnit() {
		return salesPackagingUnit;
	}
	public void setSalesPackagingUnit(HashMap<String, String> salesPackagingUnit) {
		this.salesPackagingUnit = salesPackagingUnit;
	}
	public String getSalesPackagingUnit(String context) {
		return salesPackagingUnit.get(context);
	}
	public HashMap<String, String> getSalesPackagingUnitMap() {
		return salesPackagingUnit;	
	}
	public void setSalesPackagingUnit(String context, String value) {
		this.salesPackagingUnit.put(context, value);
	}
	public HashMap<String, String> getProductLifeCycleStatusEasyOrder() {
		return productLifeCycleStatusEasyOrder;
	}
	public void setProductLifeCycleStatusEasyOrder(HashMap<String, String> productLifeCycleStatusEasyOrder) {
		this.productLifeCycleStatusEasyOrder = productLifeCycleStatusEasyOrder;
	}
	public String getProductLifeCycleStatusEasyOrder(String context) {
		return productLifeCycleStatusEasyOrder.get(context);
	}
	public HashMap<String, String> getProductLifeCycleStatusEasyOrderMap() {
		return productLifeCycleStatusEasyOrder;	
	}
	public void setProductLifeCycleStatusEasyOrder(String context, String value) {
		this.productLifeCycleStatusEasyOrder.put(context, value);
	}
	
	public HashMap<String, String> getWebProductTitleAdvantage() {
		return webProductTitleAdvantage;
	}
	public void setWebProductTitleAdvantage(HashMap<String, String> webProductTitleAdvantage) {
		this.webProductTitleAdvantage = webProductTitleAdvantage;
	}
	public String getWebProductTitleAdvantage(String context) {
		return webProductTitleAdvantage.get(context);
	}
	public HashMap<String, String> getWebProductTitleAdvantageMap() {
		return webProductTitleAdvantage;	
	}
	public void setWebProductTitleAdvantage(String context, String value) {
		this.webProductTitleAdvantage.put(context, value);
	}
	
	public HashMap<String, String> getWebProductTextAdvantage() {
		return webProductTextAdvantage;
	}
	public void setWebProductTextAdvantage(HashMap<String, String> webProductTextAdvantage) {
		this.webProductTextAdvantage = webProductTextAdvantage;
	}
	public String getWebProductTextAdvantage(String context) {
		return webProductTextAdvantage.get(context);
	}
	public HashMap<String, String> getWebProductTextAdvantageMap() {
		return webProductTextAdvantage;	
	}
	public void setWebProductTextAdvantage(String context, String value) {
		this.webProductTextAdvantage.put(context, value);
	}
	
	public HashMap<String, String> getArticleExistsInDistributionChain() {
		return this.articleExistsInDistributionChain;
	}
	public void setArticleExistsInDistributionChain(HashMap<String, String> articleExistsInDistributionChain) {
		this.articleExistsInDistributionChain = articleExistsInDistributionChain;
	}
	public String getArticleExistsInDistributionChain(String context) {
		return articleExistsInDistributionChain.get(context);
	}
	public HashMap<String, String> getArticleExistsInDistributionChainMap() {
		return articleExistsInDistributionChain;	
	}
	public void setArticleExistsInDistributionChain(String context, String value) {
		this.articleExistsInDistributionChain.put(context, value);
	}
	
	public HashMap<String, String> getDistributionChainSalesPackagingLine() {
		return this.distributionChainSalesPackagingLine;
	}
	public void setDistributionChainSalesPackagingLine(HashMap<String, String> distributionChainSalesPackagingLine) {
		this.distributionChainSalesPackagingLine = distributionChainSalesPackagingLine;
	}
	public String getDistributionChainSalesPackagingLine(String context) {
		return distributionChainSalesPackagingLine.get(context);
	}
	public HashMap<String, String> getDistributionChainSalesPackagingLineMap() {
		return distributionChainSalesPackagingLine;	
	}
	public void setDistributionChainSalesPackagingLine(String context, String value) {
		this.distributionChainSalesPackagingLine.put(context, value);
	}
	
}

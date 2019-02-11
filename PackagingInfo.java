package com.staples.eCatalogue.POJO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PackagingInfo {
	private HashMap<String,List<String>> localCode = new HashMap<String, List<String>>();
	private String LENCode;
	private String articleCode;
	private String productID;
	private String packagingID;
	private String EUCode;
	private String productLifeCycleStatus;
	private String salesUOMLegacy;
	private HashMap<String,String> displayUnitOfMeasure = new HashMap<String, String>();
	private String contentQuantityLegacy;
	private String contentUOMLegacy;
	private String conversionFactorToBaseUnit;
	private HashMap<String,List<String>> webID = new HashMap<String, List<String>>();	
	private HashMap<String,List<String>> webIDExport = new HashMap<String, List<String>>();	
	private HashMap<String,String> packagingLineLegacy = new HashMap<String, String>();
	private HashMap<String,String> promotionalProductPackagingLine = new HashMap<String, String>();
	private HashMap<String,String> webProductTitleOnlineLegacy = new HashMap<String, String>();
	private HashMap<String,String> webProductTitleAdvantageLegacy = new HashMap<String, String>();
	private String mainGTIN;
	private List<String> GTIN = new ArrayList<String>();
	private String truncatedLocalCodes;
	private String productLifeCycleStatus_calc;
	private String duplicatePackagings_calc;
	private HashMap<String,String> packagingLineLegacy_calc = new HashMap<String, String>();
	private HashMap<String,String> webProductTitleOnlineLegacy_calc = new HashMap<String, String>();
	private HashMap<String,String> webProductTitleAdvantageLegacy_calc = new HashMap<String, String>();
	private String activeAssortment_calc;
	private String notPublished_calc;
	private String webTitle_calc;
	private String imageComingSoon_calc;
	private String productImage_calc;
	private String brandImages_calc;
	private String iconImages_calc;
	private String productDescription_calc;
	private String bulletPoints_calc;
	private String alternatives_calc;
	private String relatedItems_calc;
	private String documents_calc;
	private String breadcrumbs_calc;	
	
	public String getPackagingID() {
		return packagingID;
	}
	public void setPackagingID(String packagingID) {
		this.packagingID = packagingID;
	}
	public String getEUCode() {
		return EUCode;
	}
	public void setEUCode(String eUCode) {
		EUCode = eUCode;
	}
	public String getProductLifeCycleStatus() {
		return productLifeCycleStatus;
	}
	public void setProductLifeCycleStatus(String productLifeCycleStatus) {
		this.productLifeCycleStatus = productLifeCycleStatus;
	}
	public String getSalesUOMLegacy() {
		return salesUOMLegacy;
	}
	public void setSalesUOMLegacy(String salesUOMLegacy) {
		this.salesUOMLegacy = salesUOMLegacy;
	}
	public String getPackagingLineLegacy(String context) {
		return packagingLineLegacy.get(context);
	}
	public HashMap<String, String> getPackagingLineLegacyMap() {
		return packagingLineLegacy;	
	}
	public void setPackagingLineLegacy(String context, String value) {
		this.packagingLineLegacy.put(context, value);
	}
	public List<String> getLocalCode(String context) {
		return localCode.get(context);
	}
	public HashMap<String, List<String>> getLocalCodeMap() {
		return localCode;	
	}
	public void addLocalCode(String context, String value) {
		List<String> codeList = localCode.get(context);
		if (codeList == null) {
			codeList = new ArrayList<String>();
		}
		codeList.add(value);
		this.localCode.put(context, codeList);
	}
	public List<String> getWebID(String context) {
		return webID.get(context);
	}
	public HashMap<String, List<String>> getWebIDMap() {
		return webID;	
	}
	public void addWebID(String context, String value) {
		List<String> codeList = webID.get(context);
		if (codeList == null) {
			codeList = new ArrayList<String>();
		}
		codeList.add(value);
		this.webID.put(context, codeList);
	}
	public HashMap<String, String> getPackagingLineLegacy() {
		return packagingLineLegacy;
	}
	public void setPackagingLineLegacy(HashMap<String, String> packagingLineLegacy) {
		this.packagingLineLegacy = packagingLineLegacy;
	}
	public HashMap<String, List<String>> getLocalCode() {
		return localCode;
	}
	public void setLocalCode(HashMap<String, List<String>> localCode) {
		this.localCode = localCode;
	}
	public HashMap<String, List<String>> getWebIDExport() {
		return webIDExport;
	}
	public void setWebIDExport(HashMap<String, List<String>> webIDExport) {
		this.webIDExport = webIDExport;
	}
	public String getLENCode() {
		return LENCode;
	}
	public void setLENCode(String lENCode) {
		LENCode = lENCode;
	}
	public String getArticleCode() {
		return articleCode;
	}
	public void setArticleCode(String articleCode) {
		this.articleCode = articleCode;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getContentQuantityLegacy() {
		return contentQuantityLegacy;
	}
	public void setContentQuantityLegacy(String contentQuantityLegacy) {
		this.contentQuantityLegacy = contentQuantityLegacy;
	}
	public String getContentUOMLegacy() {
		return contentUOMLegacy;
	}
	public void setContentUOMLegacy(String contentUOMLegacy) {
		this.contentUOMLegacy = contentUOMLegacy;
	}
	public String getConversionFactorToBaseUnit() {
		return conversionFactorToBaseUnit;
	}
	public void setConversionFactorToBaseUnit(String conversionFactorToBaseUnit) {
		this.conversionFactorToBaseUnit = conversionFactorToBaseUnit;
	}
	public List<String> getWebIDExport(String context) {
		return webIDExport.get(context);
	}
	public HashMap<String, List<String>> getWebIDExportMap() {
		return webIDExport;	
	}
	public void addWebIDExport(String context, String value) {
		List<String> codeList = webIDExport.get(context);
		if (codeList == null) {
			codeList = new ArrayList<String>();
		}
		codeList.add(value);
		this.webIDExport.put(context, codeList);
	}
	public String getWebProductTitleOnlineLegacy(String context) {
		return webProductTitleOnlineLegacy.get(context);
	}
	public HashMap<String, String> getWebProductTitleOnlineLegacyMap() {
		return webProductTitleOnlineLegacy;	
	}
	public void setWebProductTitleOnlineLegacy(String context, String value) {
		this.webProductTitleOnlineLegacy.put(context, value);
	}
	public String getWebProductTitleAdvantageLegacy(String context) {
		return webProductTitleAdvantageLegacy.get(context);
	}
	public HashMap<String, String> getWebProductTitleAdvantageLegacyMap() {
		return webProductTitleAdvantageLegacy;	
	}
	public void setWebProductTitleAdvantageLegacy(String context, String value) {
		this.webProductTitleAdvantageLegacy.put(context, value);
	}
	public String getMainGTIN() {
		return mainGTIN;
	}
	public void setMainGTIN(String mainGTIN) {
		this.mainGTIN = mainGTIN;
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
	public String getTruncatedLocalCodes() {
		return truncatedLocalCodes;
	}
	public void setTruncatedLocalCodes(String truncatedLocalCodes) {
		this.truncatedLocalCodes = truncatedLocalCodes;
	}
	public String getDuplicatePackagings_calc() {
		return duplicatePackagings_calc;
	}
	public void setDuplicatePackagings_calc(String duplicatePackagings_calc) {
		this.duplicatePackagings_calc = duplicatePackagings_calc;
	}
	public String getPackagingLineLegacy_calc(String context) {
		return packagingLineLegacy_calc.get(context);
	}
	public HashMap<String, String> getPackagingLineLegacy_calcMap() {
		return packagingLineLegacy_calc;	
	}
	public void setPackagingLineLegacy_calc(String context, String value) {
		this.packagingLineLegacy_calc.put(context, value);
	}
	public String getWebProductTitleOnlineLegacy_calc(String context) {
		return webProductTitleOnlineLegacy_calc.get(context);
	}
	public HashMap<String, String> getWebProductTitleOnlineLegacy_calcMap() {
		return webProductTitleOnlineLegacy_calc;	
	}
	public void setWebProductTitleOnlineLegacy_calc(String context, String value) {
		this.webProductTitleOnlineLegacy_calc.put(context, value);
	}
	public String getWebProductTitleAdvantageLegacy_calc(String context) {
		return webProductTitleAdvantageLegacy_calc.get(context);
	}
	public HashMap<String, String> getWebProductTitleAdvantageLegacy_calcMap() {
		return webProductTitleAdvantageLegacy_calc;	
	}
	public void setWebProductTitleAdvantageLegacy_calc(String context, String value) {
		this.webProductTitleAdvantageLegacy_calc.put(context, value);
	}
	public String getActiveAssortment_calc() {
		return activeAssortment_calc;
	}
	public void setActiveAssortment_calc(String activeAssortment_calc) {
		this.activeAssortment_calc = activeAssortment_calc;
	}
	public String getNotPublished_calc() {
		return notPublished_calc;
	}
	public void setNotPublished_calc(String notPublished_calc) {
		this.notPublished_calc = notPublished_calc;
	}
	public String getWebTitle_calc() {
		return webTitle_calc;
	}
	public void setWebTitle_calc(String webTitle_calc) {
		this.webTitle_calc = webTitle_calc;
	}
	public String getImageComingSoon_calc() {
		return imageComingSoon_calc;
	}
	public void setImageComingSoon_calc(String imageComingSoon_calc) {
		this.imageComingSoon_calc = imageComingSoon_calc;
	}
	public String getProductImage_calc() {
		return productImage_calc;
	}
	public void setProductImage_calc(String productImage_calc) {
		this.productImage_calc = productImage_calc;
	}
	public String getBrandImages_calc() {
		return brandImages_calc;
	}
	public void setBrandImages_calc(String brandImages_calc) {
		this.brandImages_calc = brandImages_calc;
	}
	public String getIconImages_calc() {
		return iconImages_calc;
	}
	public void setIconImages_calc(String iconImages_calc) {
		this.iconImages_calc = iconImages_calc;
	}
	public String getProductDescription_calc() {
		return productDescription_calc;
	}
	public void setProductDescription_calc(String productDescription_calc) {
		this.productDescription_calc = productDescription_calc;
	}
	public String getBulletPoints_calc() {
		return bulletPoints_calc;
	}
	public void setBulletPoints_calc(String bulletPoints_calc) {
		this.bulletPoints_calc = bulletPoints_calc;
	}
	public String getAlternatives_calc() {
		return alternatives_calc;
	}
	public void setAlternatives_calc(String alternatives_calc) {
		this.alternatives_calc = alternatives_calc;
	}
	public String getRelatedItems_calc() {
		return relatedItems_calc;
	}
	public void setRelatedItems_calc(String relatedItems_calc) {
		this.relatedItems_calc = relatedItems_calc;
	}
	public String getDocuments_calc() {
		return documents_calc;
	}
	public void setDocuments_calc(String documents_calc) {
		this.documents_calc = documents_calc;
	}
	public String getBreadcrumbs_calc() {
		return breadcrumbs_calc;
	}
	public void setBreadcrumbs_calc(String breadcrumbs_calc) {
		this.breadcrumbs_calc = breadcrumbs_calc;
	}
	public HashMap<String, List<String>> getWebID() {
		return webID;
	}
	public void setWebID(HashMap<String, List<String>> webID) {
		this.webID = webID;
	}
	public HashMap<String, String> getWebProductTitleOnlineLegacy() {
		return webProductTitleOnlineLegacy;
	}
	public void setWebProductTitleOnlineLegacy(HashMap<String, String> webProductTitleOnlineLegacy) {
		this.webProductTitleOnlineLegacy = webProductTitleOnlineLegacy;
	}
	public HashMap<String, String> getWebProductTitleAdvantageLegacy() {
		return webProductTitleAdvantageLegacy;
	}
	public void setWebProductTitleAdvantageLegacy(HashMap<String, String> webProductTitleAdvantageLegacy) {
		this.webProductTitleAdvantageLegacy = webProductTitleAdvantageLegacy;
	}
	public String getProductLifeCycleStatus_calc() {
		return productLifeCycleStatus_calc;
	}
	public void setProductLifeCycleStatus_calc(String productLifeCycleStatus_calc) {
		this.productLifeCycleStatus_calc = productLifeCycleStatus_calc;
	}
	public String getDisplayUnitOfMeasure(String context) {
		return displayUnitOfMeasure.get(context);
	}
	public HashMap<String, String> getDisplayUnitOfMeasureMap() {
		return displayUnitOfMeasure;	
	}
	public void setDisplayUnitOfMeasure(String context, String value) {
		this.displayUnitOfMeasure.put(context, value);
	}
	public String getPromotionalProductPackagingLine(String context) {
		return promotionalProductPackagingLine.get(context);
	}
	public HashMap<String, String> getPromotionalProductPackagingLineMap() {
		return promotionalProductPackagingLine;	
	}
	public void setPromotionalProductPackagingLine(String context, String value) {
		this.promotionalProductPackagingLine.put(context, value);
	}
}

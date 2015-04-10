package com.openlegacy.ws.openlegacy.services;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;


/**
 *  A service interface and input/oputput definition for a web service.
 *  Defines the contract between the client and server. The client uses the same interface for testing the service via Java code. 
 *  The interface ItemDetailsService can be customized to enabling passing parameters to the service.     
 */

@WebService
public interface ItemDetailsService {

	@WebMethod(operationName = "getItemDetails")
	@WebResult(name = "ItemDetailsOutput")
	public ItemDetailsOut getItemDetails(ItemDetailsIn itemDetailsIn);

	public static class ItemDetailsIn{
		String itemNumber;
		
		public String getItemNumber(){
			return itemNumber;
		}
		
		public void setItemNumber(String itemNumber){
			this.itemNumber = itemNumber;
		}

	}
	public static class ItemDetailsOut{
		String itemNumber;
		String itemDescription;
		String alphaSearch;
		String supercedingItemto;
		String supercedingItemfrom;
		String substituteItemNumber;
		String manufacturersItemNo;
		String itemWeight;
		String itemClass;
		String itemClassName;
		String stockGroup;
		String stockGroupName;
		String unitOfMeasure;
		String packingMultiplier;
		String outerUnitOfMeasure;
		String outerQuantity;
		String palletLabelRequired;
		String vatCode;

				
		public String getItemNumber(){
			return itemNumber;
		}
		
		public void setItemNumber(String itemNumber){
			this.itemNumber = itemNumber;
		}
				
		public String getItemDescription(){
			return itemDescription;
		}
		
		public void setItemDescription(String itemDescription){
			this.itemDescription = itemDescription;
		}
				
		public String getAlphaSearch(){
			return alphaSearch;
		}
		
		public void setAlphaSearch(String alphaSearch){
			this.alphaSearch = alphaSearch;
		}
				
		public String getSupercedingItemto(){
			return supercedingItemto;
		}
		
		public void setSupercedingItemto(String supercedingItemto){
			this.supercedingItemto = supercedingItemto;
		}
				
		public String getSupercedingItemfrom(){
			return supercedingItemfrom;
		}
		
		public void setSupercedingItemfrom(String supercedingItemfrom){
			this.supercedingItemfrom = supercedingItemfrom;
		}
				
		public String getSubstituteItemNumber(){
			return substituteItemNumber;
		}
		
		public void setSubstituteItemNumber(String substituteItemNumber){
			this.substituteItemNumber = substituteItemNumber;
		}
				
		public String getManufacturersItemNo(){
			return manufacturersItemNo;
		}
		
		public void setManufacturersItemNo(String manufacturersItemNo){
			this.manufacturersItemNo = manufacturersItemNo;
		}
				
		public String getItemWeight(){
			return itemWeight;
		}
		
		public void setItemWeight(String itemWeight){
			this.itemWeight = itemWeight;
		}
				
		public String getItemClass(){
			return itemClass;
		}
		
		public void setItemClass(String itemClass){
			this.itemClass = itemClass;
		}
				
		public String getItemClassName(){
			return itemClassName;
		}
		
		public void setItemClassName(String itemClassName){
			this.itemClassName = itemClassName;
		}
				
		public String getStockGroup(){
			return stockGroup;
		}
		
		public void setStockGroup(String stockGroup){
			this.stockGroup = stockGroup;
		}
				
		public String getStockGroupName(){
			return stockGroupName;
		}
		
		public void setStockGroupName(String stockGroupName){
			this.stockGroupName = stockGroupName;
		}
				
		public String getUnitOfMeasure(){
			return unitOfMeasure;
		}
		
		public void setUnitOfMeasure(String unitOfMeasure){
			this.unitOfMeasure = unitOfMeasure;
		}
				
		public String getPackingMultiplier(){
			return packingMultiplier;
		}
		
		public void setPackingMultiplier(String packingMultiplier){
			this.packingMultiplier = packingMultiplier;
		}
				
		public String getOuterUnitOfMeasure(){
			return outerUnitOfMeasure;
		}
		
		public void setOuterUnitOfMeasure(String outerUnitOfMeasure){
			this.outerUnitOfMeasure = outerUnitOfMeasure;
		}
				
		public String getOuterQuantity(){
			return outerQuantity;
		}
		
		public void setOuterQuantity(String outerQuantity){
			this.outerQuantity = outerQuantity;
		}
				
		public String getPalletLabelRequired(){
			return palletLabelRequired;
		}
		
		public void setPalletLabelRequired(String palletLabelRequired){
			this.palletLabelRequired = palletLabelRequired;
		}
				
		public String getVatCode(){
			return vatCode;
		}
		
		public void setVatCode(String vatCode){
			this.vatCode = vatCode;
		}
	}
}

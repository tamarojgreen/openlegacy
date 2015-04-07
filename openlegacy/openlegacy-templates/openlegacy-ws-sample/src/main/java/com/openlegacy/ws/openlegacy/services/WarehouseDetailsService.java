package com.openlegacy.ws.openlegacy.services;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;


/**
 *  A service interface and input/oputput definition for a web service.
 *  Defines the contract between the client and server. The client uses the same interface for testing the service via Java code. 
 *  The interface WarehouseDetailsService can be customized to enabling passing parameters to the service.     
 */

@WebService
public interface WarehouseDetailsService {

	@WebMethod(operationName = "getWarehouseDetails")
	@WebResult(name = "WarehouseDetailsOutput")
	public WarehouseDetailsOut getWarehouseDetails(WarehouseDetailsIn warehouseDetailsIn);

	public static class WarehouseDetailsIn{
		String warehouseNumber;
		
		public String getWarehouseNumber(){
			return warehouseNumber;
		}
		
		public void setWarehouseNumber(String warehouseNumber){
			this.warehouseNumber = warehouseNumber;
		}

	}
	public static class WarehouseDetailsOut{
		String warehouseNumber;
		String warehouseDescription;
		String warehouseType;
		String warehouseTypeName;
		String costingType;
		String amendedDate;
		String amendedBy;
		String createdDate;
		String createdBy;

				
		public String getWarehouseNumber(){
			return warehouseNumber;
		}
		
		public void setWarehouseNumber(String warehouseNumber){
			this.warehouseNumber = warehouseNumber;
		}
				
		public String getWarehouseDescription(){
			return warehouseDescription;
		}
		
		public void setWarehouseDescription(String warehouseDescription){
			this.warehouseDescription = warehouseDescription;
		}
				
		public String getWarehouseType(){
			return warehouseType;
		}
		
		public void setWarehouseType(String warehouseType){
			this.warehouseType = warehouseType;
		}
				
		public String getWarehouseTypeName(){
			return warehouseTypeName;
		}
		
		public void setWarehouseTypeName(String warehouseTypeName){
			this.warehouseTypeName = warehouseTypeName;
		}
				
		public String getCostingType(){
			return costingType;
		}
		
		public void setCostingType(String costingType){
			this.costingType = costingType;
		}
				
		public String getAmendedDate(){
			return amendedDate;
		}
		
		public void setAmendedDate(String amendedDate){
			this.amendedDate = amendedDate;
		}
				
		public String getAmendedBy(){
			return amendedBy;
		}
		
		public void setAmendedBy(String amendedBy){
			this.amendedBy = amendedBy;
		}
				
		public String getCreatedDate(){
			return createdDate;
		}
		
		public void setCreatedDate(String createdDate){
			this.createdDate = createdDate;
		}
				
		public String getCreatedBy(){
			return createdBy;
		}
		
		public void setCreatedBy(String createdBy){
			this.createdBy = createdBy;
		}
	}
}

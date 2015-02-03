package org.openlegacy.demo.db.model.services;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.openlegacy.demo.db.model.StockItemImage;
import org.openlegacy.demo.db.model.UploadedFile;
import org.openlegacy.demo.db.model.StockItem;
import java.util.List;
import org.openlegacy.demo.db.model.ProductItem;

/**
 *  A service interface and input/oputput definition for a web service.
 *  Defines the contract between the client and server. The client uses the same interface for testing the service via Java code. 
 *  The interface TestService1Service can be customized to enabling passing parameters to the service.     
 */

@WebService
public interface TestService1Service {

	@WebMethod(operationName = "getTestService1")
	@WebResult(name = "TestService1Output")
	public TestService1Out getTestService1(TestService1In testService1In);

	public static class TestService1In{
		UploadedFile uploadedFile;
		StockItem stockItem;
		String text;
		byte[] image;
		List<StockItemImage> images;
		
		public UploadedFile getUploadedFile(){
			return uploadedFile;
		} 

		public StockItem getStockItem(){
			return stockItem;
		}
		
		public void setStockItem(StockItem stockItem){
			this.stockItem = stockItem;
		}

		public String getText(){
			return text;
		}
		
		public void setText(String text){
			this.text = text;
		}

		public byte[] getImage(){
			return image;
		}
		
		public void setImage(byte[] image){
			this.image = image;
		}

		public List<StockItemImage> getImages(){
			return images;
		}
		
		public void setImages(List<StockItemImage> images){
			this.images = images;
		}

	}
	public static class TestService1Out{
		List<?> childs;
		ProductItem parent;
		StockItem stockItem;
		byte[] image;
		String fileName;

				
		public List<?> getChilds(){
			return childs;
		}
		
		public void setChilds(List<?> childs){
			this.childs = childs;
		}
				
		public ProductItem getParent(){
			return parent;
		}
		
		public void setParent(ProductItem parent){
			this.parent = parent;
		}
				
		public StockItem getStockItem(){
			return stockItem;
		}
		
		public void setStockItem(StockItem stockItem){
			this.stockItem = stockItem;
		}
				
		public byte[] getImage(){
			return image;
		}
		
		public void setImage(byte[] image){
			this.image = image;
		}
				
		public String getFileName(){
			return fileName;
		}
		
		public void setFileName(String fileName){
			this.fileName = fileName;
		}
	}
}

package org.openlegacy.demo.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UploadedFile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String fileName;

	private String filePath;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private ProductItem productItem;

	public void setProductItem(ProductItem productItem) {
		this.productItem = productItem;
	}

	public Integer getId() {
		return id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public ProductItem getProductItem() {
		return productItem;
	}
}

package org.openlegacy.demo.db.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ProductItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String productName;

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "parent_id")
	private ProductItem parent;

	@OneToMany(mappedBy = "parent")
	private List<ProductItem> childs = new ArrayList<ProductItem>();

	@OneToMany(cascade = { CascadeType.ALL })
	private List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();

	public Integer getId() {
		return id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public ProductItem getParent() {
		return parent;
	}

	public void setParent(ProductItem parent) {
		this.parent = parent;
	}

	public List<ProductItem> getChilds() {
		return childs;
	}

	public void setChilds(List<ProductItem> childs) {
		this.childs = childs;
	}
}

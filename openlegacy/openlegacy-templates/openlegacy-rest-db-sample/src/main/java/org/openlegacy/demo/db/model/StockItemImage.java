package org.openlegacy.demo.db.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StockItemImage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "BLOB NOT NULL")
	private byte[] image;

	@ManyToOne
	private StockItem stockItem;

	public Long getId() {
		return id;
	}

	public StockItem getStockItem() {
		return stockItem;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
}

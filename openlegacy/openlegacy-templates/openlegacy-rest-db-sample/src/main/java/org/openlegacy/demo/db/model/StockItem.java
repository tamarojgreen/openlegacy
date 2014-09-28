package org.openlegacy.demo.db.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

@Entity
public class StockItem {

	@Id
	private Integer itemId;

	private String description;

	private String videoUrl;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "stock_item")
	@MapKey(name = "noteId")
	private Map<String, StockItemNote> notes = new TreeMap<String, StockItemNote>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "stock_item")
	@MapKey(name = "noteId")
	private List<StockItemImage> images = new ArrayList<StockItemImage>();

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, StockItemNote> getNotes() {
		return notes;
	}

	public List<StockItemImage> getImages() {
		return images;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
}

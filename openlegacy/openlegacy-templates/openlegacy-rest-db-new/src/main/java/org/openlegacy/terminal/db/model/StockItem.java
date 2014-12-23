package org.openlegacy.terminal.db.model;

import org.hibernate.annotations.Cascade;
import org.openlegacy.annotations.db.Action;
import org.openlegacy.annotations.db.DbActions;
import org.openlegacy.annotations.db.DbColumn;
import org.openlegacy.annotations.db.DbNavigation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
@DbNavigation(category = "StockItem")
@DbActions(actions = {
		@Action(action = org.openlegacy.db.actions.DbActions.CREATE.class, displayName = "Create", alias = "create"),
		@Action(action = org.openlegacy.db.actions.DbActions.READ.class, displayName = "READ", alias = "read"),
		@Action(action = org.openlegacy.db.actions.DbActions.UPDATE.class, displayName = "Update", alias = "update"),
		@Action(action = org.openlegacy.db.actions.DbActions.DELETE.class, displayName = "Delete", alias = "delete") })
public class StockItem {

	@Id
	private Integer itemId;

	@DbColumn(displayName = "Description", mainDisplayField = true, editable = true)
	private String description;

	@DbColumn(displayName = "Video Url", mainDisplayField = true)
	private String videoUrl;

	@OneToMany(fetch = FetchType.LAZY)
	@DbColumn(displayName = "Notes", mainDisplayField = false, internal = false)
	@JoinColumn(name = "stock_item")
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	private List<StockItemNote> notes = new ArrayList<StockItemNote>();

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_item")
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	private List<StockItemNote> notes2 = new ArrayList<StockItemNote>();

	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	// @JoinColumn(name = "stock_item")
	// @MapKey(name = "imageId")
	// @DbColumn(displayName = "Images", mainDisplayField = false, internal = false)
	// private List<StockItemImage> images = new ArrayList<StockItemImage>();

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

	public List<StockItemNote> getNotes() {
		return notes;
	}

	// public List<StockItemImage> getImages() {
	// return images;
	// }

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public List<StockItemNote> getNotes2() {
		return notes2;
	}

}

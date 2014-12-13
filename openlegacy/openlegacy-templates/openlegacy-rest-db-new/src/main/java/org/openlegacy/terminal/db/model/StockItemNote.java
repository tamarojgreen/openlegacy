package org.openlegacy.terminal.db.model;

import org.openlegacy.annotations.db.Action;
import org.openlegacy.annotations.db.DbActions;
import org.openlegacy.annotations.db.DbColumn;
import org.openlegacy.annotations.db.DbNavigation;
import org.openlegacy.db.actions.DbActions.READ;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@DbNavigation(category = "Stock Item Notes")
@DbActions(actions = { @Action(action = READ.class, alias = "read", displayName = "Read") })
public class StockItemNote implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String noteId;

	@ManyToOne
	private StockItem stockItem;

	@DbColumn(displayName = "Text", mainDisplayField = true)
	private String text;

	public Long getId() {
		return id;
	}

	public StockItem getStockItem() {
		return stockItem;
	}

	public String getNoteId() {
		return noteId;
	}

	public String getText() {
		return text;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	public void setText(String text) {
		this.text = text;
	}
}

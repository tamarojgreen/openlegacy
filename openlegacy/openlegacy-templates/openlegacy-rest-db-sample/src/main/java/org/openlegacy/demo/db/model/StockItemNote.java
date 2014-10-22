package org.openlegacy.demo.db.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StockItemNote implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String noteId;

	@ManyToOne
	private StockItem stockItem;

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

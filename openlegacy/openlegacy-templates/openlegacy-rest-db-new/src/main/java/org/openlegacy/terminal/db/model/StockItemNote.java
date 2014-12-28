package org.openlegacy.terminal.db.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.openlegacy.annotations.db.Action;
import org.openlegacy.annotations.db.DbActions;
import org.openlegacy.annotations.db.DbColumn;
import org.openlegacy.annotations.db.DbEntity;
import org.openlegacy.db.actions.DbActions.READ;

@DbEntity(displayName = "Stock Item Note", pluralName = "Stock Item Notes")
@Entity
@DbActions(actions = { @Action(action = READ.class, alias = "read", displayName = "Read") })
public class StockItemNote implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JsonBackReference
	private StockItem stockItem;

	@DbColumn(displayName = "Text", mainDisplayField = true, editable = true)
	private String text;

	public Long getId() {
		return id;
	}

	public StockItem getStockItem() {
		return stockItem;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}

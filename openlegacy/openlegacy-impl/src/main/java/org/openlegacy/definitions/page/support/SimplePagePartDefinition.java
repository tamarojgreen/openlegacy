package org.openlegacy.definitions.page.support;

import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.layout.PagePartRowDefinition;
import org.openlegacy.terminal.layout.PositionedPagePartDefinition;

import java.util.ArrayList;
import java.util.List;

public class SimplePagePartDefinition implements PositionedPagePartDefinition {

	private List<PagePartRowDefinition> rowParts = new ArrayList<PagePartRowDefinition>();
	private int width;
	private int topMargin;
	private int leftMargin;
	private int columns;

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public List<PagePartRowDefinition> getPartRows() {
		return rowParts;
	}

	public int getWidth() {
		return width;
	}

	public int getTopMargin() {
		return topMargin;
	}

	public int getLeftMargin() {
		return leftMargin;
	}

	public void setTopMargin(int topMargin) {
		this.topMargin = topMargin;
	}

	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Used as free-marker helper method
	 * 
	 * @return
	 */
	public boolean isOneField() {
		return getPartRows().size() == 1 && getPartRows().get(0).getFields().size() == 1;
	}

	public boolean isMultyFields() {
		return !isOneField();
	}

	/**
	 * Used as free-marker helper method
	 * 
	 * @return
	 */
	public FieldDefinition getSingleField() {
		if (isOneField()) {
			return getPartRows().get(0).getFields().get(0);
		}
		return null;
	}
}

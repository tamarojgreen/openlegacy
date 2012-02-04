package org.openlegacy.definitions.page.support;

import org.openlegacy.layout.PagePartRowDefinition;
import org.openlegacy.terminal.layout.PositionedPagePartDefinition;

import java.util.ArrayList;
import java.util.List;

public class SimplePagePartDefinition implements PositionedPagePartDefinition {

	private List<PagePartRowDefinition> rowParts = new ArrayList<PagePartRowDefinition>();
	private int widthPercentage;
	private int topMarginPercentage;
	private int leftMarginPercentage;
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

	public int getWidthPercentage() {
		return widthPercentage;
	}

	public int getTopMarginPercentage() {
		return topMarginPercentage;
	}

	public int getLeftMarginPercentage() {
		return leftMarginPercentage;
	}

	public void setTopMarginPercentage(int topMarginPercentage) {
		this.topMarginPercentage = topMarginPercentage;
	}

	public void setLeftMarginPercentage(int leftMarginPercentage) {
		this.leftMarginPercentage = leftMarginPercentage;
	}

	public void setWidthPercentage(int widthPercentage) {
		this.widthPercentage = widthPercentage;
	}
}

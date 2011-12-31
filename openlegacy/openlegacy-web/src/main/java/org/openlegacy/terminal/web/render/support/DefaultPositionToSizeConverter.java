package org.openlegacy.terminal.web.render.support;

import org.openlegacy.terminal.web.render.HtmlProportionsHandler;

public class DefaultPositionToSizeConverter implements HtmlProportionsHandler {

	private int columnProportion;
	private int rowProportion;
	private int fontSize;

	public int toWidth(int column) {
		return column * columnProportion;
	}

	public int toHeight(int row) {
		return row * rowProportion;
	}

	public void setColumnProportion(int columnProportion) {
		this.columnProportion = columnProportion;
	}

	public void setRowProportion(int rowProportion) {
		this.rowProportion = rowProportion;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
}

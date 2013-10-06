/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.web.render.support;

import org.openlegacy.terminal.web.render.HtmlProportionsHandler;

public class DefaultHtmlProportionsHandler implements HtmlProportionsHandler {

	private int columnProportion;
	private int rowProportion;
	private int fontSize;

	private int inputAdditionalWidth = 3;

	private Integer inputHeight;
	private int letterSpacing = 0;

	public int toWidth(int column) {
		return column * columnProportion;
	}

	public int toHeight(int row) {
		return (row - 1) * rowProportion;
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

	public Integer getInputHeight() {
		if (inputHeight == null) {
			inputHeight = getFontSize() + 2;
		}
		return inputHeight;
	}

	public void setInputHeight(Integer inputHeight) {
		this.inputHeight = inputHeight;
	}

	public int getInputAdditionalWidth() {
		return inputAdditionalWidth;
	}

	public void setInputAdditionalWidth(Integer inputAdditionalWidth) {
		this.inputAdditionalWidth = inputAdditionalWidth;
	}

	public int getLetterSpacing() {
		return letterSpacing;
	}

	public void setLetterSpacing(int letterSpacing) {
		this.letterSpacing = letterSpacing;
	}
}

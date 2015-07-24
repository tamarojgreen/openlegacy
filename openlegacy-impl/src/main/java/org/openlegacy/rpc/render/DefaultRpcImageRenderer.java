/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.rpc.render;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class DefaultRpcImageRenderer implements RpcImageRenderer {

	private final static Log logger = LogFactory.getLog(DefaultRpcImageRenderer.class);

	// default values for bean properties
	private Color imageBackgroundColor = Color.BLACK;
	private Color imageDefaultTextColor = Color.GREEN;
	private Color imageSorroundingTextColor = Color.WHITE;

	private int leftColumnsOffset = 2;
	private int topPixelsOffset = 2;
	private int fontSize = 15;
	private String fontFamily = "Monospaced";
	private boolean drawLineNumbers = true;
	private int widthProportion = 10;
	private int heightProportion = 16;
	private int fontType = Font.BOLD;

	@Override
	public void render(String source, OutputStream output) {

		BufferedImage buffer;

		String[] lines = source.replaceAll("\r", "").split("\n");

		int rowCount = lines.length;
		int colCount = 0;

		for (String line : lines) {
			colCount = Math.max(colCount, StringUtils.stripEnd(line.replaceAll("\t", "  "), " ").length() + 1);
		}

		int width = ((int) Math.log10(rowCount) + 1 + colCount) * widthProportion;
		int height = (rowCount + 1) * heightProportion;

		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		if (logger.isDebugEnabled()) {
			logger.debug("Font set to:" + fontFamily);
		}

		Font font = new Font(fontFamily, fontType, fontSize);
		Graphics graphics = buffer.createGraphics();
		graphics.setFont(font);
		setDefaultColor(graphics);

		drawText(source, graphics);

		try {
			ImageIO.write(buffer, "png", output);
		} catch (IOException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

	private void drawText(String source, Graphics graphics) {
		source = source.replaceAll("\r", "");
		String[] lines = source.split("\n");
		int rowNumber = 1;
		graphics.setColor(imageBackgroundColor);
		for (String line : lines) {
			int startY = toHeight(rowNumber);

			if (drawLineNumbers) {
				// draw row number
				graphics.setColor(imageSorroundingTextColor);
				graphics.drawString(String.valueOf(String.format("%2d", rowNumber)), 0, startY);
			}

			line = StringUtils.stripEnd(line.replaceAll("\t", "  "), " ");
			graphics.drawString(line, 3 * widthProportion, startY);
			rowNumber++;
		}
	}

	private void setDefaultColor(Graphics graphics) {
		graphics.setColor(imageDefaultTextColor);
	}

	public int toHeight(int row) {
		return row * heightProportion;
	}

	public int toWidth(int column) {
		return column * widthProportion;
	}

	public String getFileFormat() {
		return "png";
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public void setDrawLineNumbers(boolean drawLineNumbers) {
		this.drawLineNumbers = drawLineNumbers;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void setHeightProportion(int heightProportion) {
		this.heightProportion = heightProportion;
	}

	public void setWidthProportion(int widthProportion) {
		this.widthProportion = widthProportion;
	}

	public void setLeftColumnsOffset(int leftColumnsOffset) {
		this.leftColumnsOffset = leftColumnsOffset;
	}

	public void setTopPixelsOffset(int topPixelsOffset) {
		this.topPixelsOffset = topPixelsOffset;
	}

	public void setImageBackgroundColor(Color imageBackgroundColor) {
		this.imageBackgroundColor = imageBackgroundColor;
	}

	public void setImageDefaultTextColor(Color imageDefaultTextColor) {
		this.imageDefaultTextColor = imageDefaultTextColor;
	}

	public void setImageSorroundingTextColor(Color imageSorroundingTextColor) {
		this.imageSorroundingTextColor = imageSorroundingTextColor;
	}

	public void setFontType(int fontType) {
		this.fontType = fontType;
	}

	public int getLeftColumnsOffset() {
		return leftColumnsOffset;
	}

	public int getTopPixelsOffset() {
		return topPixelsOffset;
	}

	public double fromHeight(int row) {

		return (double)row / heightProportion;
	}

	public double fromWidth(int col) {
		return (double)col / widthProportion;
	}

}

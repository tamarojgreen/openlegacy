package org.openlegacy.terminal.render;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SnapshotUtils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

public class TerminalSnapshotImageRenderer implements TerminalSnapshotRenderer {

	private static final int LEFT_COLUMNS_OFFSET = 2;
	private static final int TOP_PIXELS_OFFSET = 2;
	private static final Color IMAGE_BACKGROUND_COLOR = Color.BLACK;
	private static final Color IMAGE_BOLD_FIELD_COLOR = Color.WHITE;
	private static final Color IMAGE_DEFAULT_TEXT_COLOR = Color.GREEN;
	private static final Color IMAGE_SURROUNDING_TEXT_COLOR = Color.WHITE;

	private static TerminalSnapshotImageRenderer instance = new TerminalSnapshotImageRenderer();

	public static TerminalSnapshotImageRenderer instance() {
		return instance;
	}

	public void render(TerminalSnapshot terminalSnapshot, OutputStream output) {

		BufferedImage buffer = new BufferedImage(812, 370, BufferedImage.TYPE_INT_RGB);

		Font font = new Font("Courier New", Font.PLAIN, 15);
		Graphics graphics = buffer.createGraphics();
		graphics.setFont(font);
		setDefaultColor(graphics);

		markBackgroundAndInputFields(terminalSnapshot, graphics);

		drawText(terminalSnapshot, graphics);

		try {
			ImageIO.write(buffer, "jpg", output);
		} catch (IOException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

	private static void drawText(TerminalSnapshot terminalSnapshot, Graphics graphics) {
		int columns = terminalSnapshot.getSize().getColumns();
		List<TerminalRow> rows = terminalSnapshot.getRows();
		String screenText = terminalSnapshot.getText();
		for (TerminalRow terminalRow : rows) {
			int rowNumber = terminalRow.getRowNumber();
			int startY = toHeight(rowNumber);

			// draw row number
			graphics.setColor(IMAGE_SURROUNDING_TEXT_COLOR);
			graphics.drawString(String.valueOf(String.format("%2d", terminalRow.getRowNumber())), 0, startY);

			int rowStart = (rowNumber - 1) * columns; // row is 1 based, drawing is 0 base
			String text = screenText.substring(rowStart, rowStart + columns);
			for (int i = 0; i < text.length(); i++) {
				// text is 0 based, columns are 1 based
				TerminalField currentField = terminalSnapshot.getField(SimpleTerminalPosition.newInstance(rowNumber, i + 1));
				if (currentField != null && currentField.getBackColor() != org.openlegacy.terminal.Color.BLACK) {
					graphics.setColor(IMAGE_BACKGROUND_COLOR);
				} else {
					if (currentField != null) {
						graphics.setColor(SnapshotUtils.convertColor(currentField.getColor()));
						if (currentField.isBold() && currentField.getColor() == org.openlegacy.terminal.Color.GREEN) {
							graphics.setColor(IMAGE_BOLD_FIELD_COLOR);
						}
					} else {
						setDefaultColor(graphics);
					}
				}
				// 2 - place holder for row numbers
				graphics.drawString(String.valueOf(text.charAt(i)), toWidth(i + LEFT_COLUMNS_OFFSET), startY);
			}
		}
	}

	private static void markBackgroundAndInputFields(TerminalSnapshot terminalSnapshot, Graphics graphics) {
		int endX;
		List<TerminalField> fields = terminalSnapshot.getFields();
		setDefaultColor(graphics);
		for (TerminalField terminalField : fields) {
			TerminalPosition position = terminalField.getPosition();
			// -1 - pixels is 0 based , column is 1 based
			int startX = toWidth(position.getColumn() - 1 + LEFT_COLUMNS_OFFSET);
			int startY = toHeight(position.getRow());
			endX = toWidth(terminalField.getEndPosition().getColumn() + LEFT_COLUMNS_OFFSET);
			if (terminalField.isEditable()) {
				graphics.drawLine(startX, startY, endX, startY);
			}
			int rowHeight = toHeight(1);
			if (terminalField.getBackColor() != org.openlegacy.terminal.Color.BLACK) {
				graphics.setColor(SnapshotUtils.convertColor(terminalField.getBackColor()));
				//
				graphics.fillRect(startX, toHeight(position.getRow() - 1) + TOP_PIXELS_OFFSET,
						toWidth(terminalField.getLength()), rowHeight);
			}
		}

		List<TerminalPosition> fieldSeperators = terminalSnapshot.getFieldSeperators();
		graphics.setColor(IMAGE_DEFAULT_TEXT_COLOR);
		for (TerminalPosition terminalPosition : fieldSeperators) {
			graphics.drawString("^", toWidth(terminalPosition.getColumn() - 1 + LEFT_COLUMNS_OFFSET),
					toHeight(terminalPosition.getRow()));
		}
	}

	private static void setDefaultColor(Graphics graphics) {
		graphics.setColor(IMAGE_DEFAULT_TEXT_COLOR);
	}

	private static int toHeight(int row) {
		return row * 15;
	}

	private static int toWidth(int column) {
		return column * 10;
	}

	public String getFileFormat() {
		return "jpg";
	}

}

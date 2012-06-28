package org.openlegacy.terminal.render;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.support.SimpleTerminalPosition;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

public class TerminalSnapshotImageRenderer implements TerminalSnapshotRenderer {

	private static TerminalSnapshotImageRenderer instance = new TerminalSnapshotImageRenderer();

	public static TerminalSnapshotImageRenderer instance() {
		return instance;
	}

	public void render(TerminalSnapshot terminalSnapshot, OutputStream output) {

		BufferedImage buffer = new BufferedImage(800, 500, BufferedImage.TYPE_INT_RGB);

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
			int rowStart = (rowNumber - 1) * columns; // row is 1 based, drawing is 0 base
			String text = screenText.substring(rowStart, rowStart + columns);
			int startY = toHeight(rowNumber);
			for (int i = 0; i < text.length(); i++) {
				// text is 0 based, columns are 1 based
				TerminalField currentField = terminalSnapshot.getField(SimpleTerminalPosition.newInstance(rowNumber, i + 1));
				if (currentField != null && currentField.getBackColor() != org.openlegacy.terminal.Color.BLACK) {
					graphics.setColor(Color.BLACK);
				} else {
					graphics.setColor(Color.GREEN);
				}
				graphics.drawString(String.valueOf(text.charAt(i)), toWidth(i), startY);
			}
		}
	}

	private static void markBackgroundAndInputFields(TerminalSnapshot terminalSnapshot, Graphics graphics) {
		int width;
		List<TerminalField> fields = terminalSnapshot.getFields();
		for (TerminalField terminalField : fields) {
			TerminalPosition position = terminalField.getPosition();
			int startX = toWidth(position.getColumn());
			int startY = toHeight(position.getRow());
			width = toWidth(terminalField.getEndPosition().getColumn() + 1);
			if (terminalField.isEditable()) {
				graphics.drawLine(startX, startY, width, startY);
			}
			int rowHeight = toHeight(1);
			if (terminalField.getBackColor() != org.openlegacy.terminal.Color.BLACK) {
				graphics.fillRect(startX, toHeight(position.getRow() - 1) + 5, toWidth(terminalField.getLength()), rowHeight);
			}
		}
	}

	private static void setDefaultColor(Graphics graphics) {
		graphics.setColor(new Color(0x00ff00));
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

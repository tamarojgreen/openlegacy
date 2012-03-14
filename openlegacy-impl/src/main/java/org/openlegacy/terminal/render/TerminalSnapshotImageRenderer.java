package org.openlegacy.terminal.render;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSnapshot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

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

		Collection<TerminalField> fields = terminalSnapshot.getFields();
		for (TerminalField terminalField : fields) {
			TerminalPosition position = terminalField.getPosition();
			int startX = toWidth(position.getColumn());
			int startY = toHeight(position.getRow());
			int width = toWidth(terminalField.getEndPosition().getColumn() + 1);
			if (terminalField.isEditable()) {
				graphics.drawLine(startX, startY, width, startY);
			}
			if (terminalField.getBackColor() != org.openlegacy.terminal.Color.BLACK) {
				graphics.fillRect(startX, toHeight(position.getRow() - 1) + 5, toWidth(terminalField.getLength()), toHeight(1));
			}
			graphics.drawString(terminalField.getValue(), startX, startY);
		}

		try {
			ImageIO.write(buffer, "jpg", output);
		} catch (IOException e) {
			throw (new OpenLegacyRuntimeException(e));
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

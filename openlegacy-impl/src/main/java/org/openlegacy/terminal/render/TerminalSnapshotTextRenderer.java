package org.openlegacy.terminal.render;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.SystemUtils;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.utils.FieldsQuery;
import org.openlegacy.terminal.utils.FieldsQuery.EditableFieldsCriteria;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

/**
 * A textual utility which format terminal snapshot in to a preventable text which is very comfort for debugging purposes
 * 
 */
public class TerminalSnapshotTextRenderer implements TerminalSnapshotRenderer {

	private static TerminalSnapshotTextRenderer instance = new TerminalSnapshotTextRenderer();

	public static TerminalSnapshotTextRenderer instance() {
		return instance;
	}

	public void render(TerminalSnapshot terminalSnapshot, OutputStream outputStream) {
		boolean decorated = true;

		String text = terminalSnapshot.getText();
		String newline = SystemUtils.LINE_SEPARATOR;
		int rows = terminalSnapshot.getSize().getRows();
		StringBuilder out = new StringBuilder();
		if (decorated) {
			generateColumnNumbers(terminalSnapshot, newline, out);
		}
		for (int i = 0; i < rows; i++) {
			int beginIndex = i * terminalSnapshot.getSize().getColumns();
			if (decorated) {
				out.append(i + 1);
				if (decorated) {
					if ((i + 1) < 10) {
						out.append(" ");
					}
					out.append("|");
				}
			}
			if (decorated) {
				out.append(text.substring(beginIndex, beginIndex + terminalSnapshot.getSize().getColumns()));
				out.append("|");
			}
			out.append(newline);
		}
		if (decorated) {
			out.append("___________________________________________________________________________________");
		}
		if (decorated) {
			out.append(newline);
			generateColumnNumbers(terminalSnapshot, newline, out);
		}

		drawFieldsSeperators(terminalSnapshot, out);
		drawEditableFields(terminalSnapshot, out, FieldsQuery.queryFields(terminalSnapshot, EditableFieldsCriteria.instance()));

		try {
			// append new line so snapshot will be rendered from start of line in logs
			outputStream.write(SystemUtils.LINE_SEPARATOR.getBytes());
			outputStream.write(out.toString().getBytes(CharEncoding.UTF_8));
		} catch (IOException e) {
			throw (new OpenLegacyRuntimeException(e));
		}

	}

	@SuppressWarnings("unused")
	private static void drawCursor(TerminalSnapshot terminalSnapshot, StringBuilder out, TerminalSendAction terminalSendAction) {
		if (terminalSendAction.getCursorPosition() == null) {
			return;
		}
		int cursorPainterLocation = calculatePositionOnPainter(terminalSendAction.getCursorPosition(), terminalSnapshot.getSize());
		if (out.charAt(cursorPainterLocation) == ' ') {
			out.setCharAt(cursorPainterLocation, '#');
		}

	}

	private static void drawFieldsSeperators(TerminalSnapshot terminalSnapshot, StringBuilder out) {
		List<TerminalPosition> attributes = terminalSnapshot.getFieldSeperators();
		for (TerminalPosition position : attributes) {
			if (position.getColumn() > 0) {
				int bufferLocation = calculatePositionOnPainter(position, terminalSnapshot.getSize());
				out.setCharAt(bufferLocation, '^');
			}
		}

	}

	private static void generateColumnNumbers(TerminalSnapshot terminalSnapshot, String newline, StringBuilder out) {
		StringBuilder headerLine1 = new StringBuilder("    ");
		StringBuilder headerLine2 = new StringBuilder("    ");
		for (int i = 2; i <= terminalSnapshot.getSize().getColumns(); i++) {
			if (i % 2 == 0) {
				headerLine1.append(i % 10);
				if ((i / 10) > 0) {
					headerLine2.append(i / 10);
				} else {
					headerLine2.append(" ");
				}
			} else {
				headerLine1.append(" ");
				headerLine2.append(" ");
			}

		}
		out.append(headerLine1 + newline);
		out.append(headerLine2 + newline);
		out.append("___________________________________________________________________________________");
		out.append(newline);
	}

	private static void drawEditableFields(TerminalSnapshot terminalSnapshot, StringBuilder out, Collection<TerminalField> fields) {
		char leftMark;
		char rightMark;
		for (TerminalField terminalField : fields) {
			// +6 - line numbers + |
			int beforeInputBufferLocation = calculatePositionOnPainter(terminalField.getPosition(), terminalSnapshot.getSize()) - 1;
			if (terminalField.isModified()) {
				leftMark = '*';
				rightMark = '*';
			} else {
				leftMark = '[';
				rightMark = ']';
			}
			out.setCharAt(beforeInputBufferLocation, leftMark);
			int afterInputBufferLocation = beforeInputBufferLocation + terminalField.getLength() + 1;
			out.setCharAt(afterInputBufferLocation, rightMark);

			String value = terminalField.getValue();
			for (int i = 0; i < terminalField.getLength(); i++) {
				int inputBufferLocation = beforeInputBufferLocation + 1;
				if (i < value.length()) {
					out.setCharAt(inputBufferLocation + i, value.charAt(i));
				} else {
					out.setCharAt(inputBufferLocation + i, ' ');
				}
			}
		}
	}

	private static int calculatePositionOnPainter(TerminalPosition position, ScreenSize screenSize) {
		int fieldStartBufferLocation = (screenSize.getColumns() + 6) // 6- line numbers + | + NL
				// -1 - 0 base, +3 - header
				* (position.getRow() - 1 + 3) + position.getColumn() - 1;
		return fieldStartBufferLocation;
	}

	public String getFileFormat() {
		return "txt";
	}

}

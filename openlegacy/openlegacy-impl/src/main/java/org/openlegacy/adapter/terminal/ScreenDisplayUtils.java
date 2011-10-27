package org.openlegacy.adapter.terminal;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;

import java.util.Collection;

/**
 * A textual utility which format terminal screen in to a preventable text which is very comfort for debugging purposes
 * 
 */
public class ScreenDisplayUtils {

	public static String toString(TerminalScreen terminalScreen, boolean decorated) {
		String text = terminalScreen.getText();
		String newline = System.getProperty("line.separator");
		int rows = terminalScreen.getSize().getRows();
		StringBuffer out = new StringBuffer();
		if (decorated) {
			generateColumnNumbers(terminalScreen, newline, out);
		}
		for (int i = 0; i < rows; i++) {
			int beginIndex = i * terminalScreen.getSize().getColumns();
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
				out.append(text.substring(beginIndex, beginIndex + terminalScreen.getSize().getColumns()));
				out.append("|");
			}
			out.append(newline);
		}
		if (decorated) {
			out.append("___________________________________________________________________________________");
		}
		if (decorated) {
			out.append(newline);
			generateColumnNumbers(terminalScreen, newline, out);
		}

		markInputs(terminalScreen, out);

		return out.toString();

	}

	private static void generateColumnNumbers(TerminalScreen hostScreen, String newline, StringBuffer out) {
		StringBuilder headerLine1 = new StringBuilder("    ");
		StringBuilder headerLine2 = new StringBuilder("    ");
		for (int i = 2; i <= hostScreen.getSize().getColumns(); i++) {
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

	private static void markInputs(TerminalScreen hostScreen, StringBuffer out) {
		Collection<TerminalField> inputFields = hostScreen.getEditableFields();
		for (TerminalField terminalField : inputFields) {
			// +6 - line numbers + |
			int beforeInputBufferLocation = (hostScreen.getSize().getColumns() + 6)
			// -1 - 0 base, +3 - header
					* (terminalField.getPosition().getRow() - 1 + 3) + terminalField.getPosition().getColumn() - 2;
			out.setCharAt(beforeInputBufferLocation, '[');
			int afterInputBufferLocation = beforeInputBufferLocation + terminalField.getLength() + 1;
			out.setCharAt(afterInputBufferLocation, ']');
		}
	}
}

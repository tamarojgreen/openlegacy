package org.openlegacy.tools;

import org.openlegacy.terminal.TerminalScreen;

import java.text.MessageFormat;

public class ScreensTextDumper extends AbstractScreensDumper {

	public static void main(String[] args) {
		try {
			new ScreensTextDumper().iterate();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	protected String getContent(TerminalScreen snapshot) {
		return snapshot.toString();
	}

	@Override
	protected String getRelativeFilePath(int count) {
		return "/screens/" + MessageFormat.format("screen{0}.txt", count);
	}
}

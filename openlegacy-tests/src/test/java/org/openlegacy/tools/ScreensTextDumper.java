package org.openlegacy.tools;

import org.openlegacy.terminal.TerminalScreen;

import java.io.File;

public class ScreensTextDumper extends AbstractScreensDumper {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage:\njava ScreensTextDumper screens-resource-folder");
			return;
		}
		String root = args[0];

		try {
			new ScreensTextDumper().dumpSession(new File(root), true);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	protected String getDumpContent(TerminalScreen snapshot) {
		return snapshot.toString();
	}

	@Override
	protected String getDumpFileExtension() {
		return "txt";
	}

}

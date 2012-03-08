package org.openlegacy.terminal.tools;

import java.io.File;

public interface TerminalSessionSnapshotsDumper {

	void dumpSession(File baseDir, boolean cleanupFolder, String springContext) throws Exception;
}

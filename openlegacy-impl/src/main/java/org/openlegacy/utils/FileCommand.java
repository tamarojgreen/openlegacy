package org.openlegacy.utils;

import java.io.File;

public interface FileCommand {

	boolean accept(File file);

	void doCommand(File file);
}

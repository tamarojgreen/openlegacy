package org.openlegacy.designtime.rpc.source.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public interface CopyBookFetcher {

	public Map<String, InputStream> getCopyBooks(File sourceFile) throws FileNotFoundException;

}
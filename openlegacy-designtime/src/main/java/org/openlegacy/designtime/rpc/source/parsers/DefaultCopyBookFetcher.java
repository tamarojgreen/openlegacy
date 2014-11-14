package org.openlegacy.designtime.rpc.source.parsers;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.openlegacy.utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DefaultCopyBookFetcher implements CopyBookFetcher {

	private final String COPYBOOK_EXT = ".cpy";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.rpc.source.parsers.CopyBookFetcher#getCopyBooks(java.io.File)
	 */
	@Override
	public Map<String, InputStream> getCopyBooks(File sourceFile) throws FileNotFoundException {

		Map<String, InputStream> streamMap = new HashMap<String, InputStream>();
		if (!COPYBOOK_EXT.equals(FileUtils.fileExtension(sourceFile.getName()))) {
			String baseName = FileUtils.fileWithoutAnyExtension(sourceFile.getName());
			File dir = new File(sourceFile.getParent());
			int baseNameLength = baseName.length();
			FileFilter fileFilter = new WildcardFileFilter(baseName + "*" + COPYBOOK_EXT);

			File[] files = dir.listFiles(fileFilter);

			for (File file : files) {
				streamMap.put(file.getName().substring(baseNameLength + 1), new FileInputStream(file));
			}
		}
		return streamMap;
	}
}

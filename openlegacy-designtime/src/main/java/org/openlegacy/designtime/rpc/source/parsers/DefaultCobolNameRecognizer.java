package org.openlegacy.designtime.rpc.source.parsers;

import org.openlegacy.utils.FileUtils;
import org.openlegacy.utils.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultCobolNameRecognizer implements CobolNameRecognizer {

	private final static Pattern programIDPattern = Pattern.compile("PROGRAM-ID\\.\\s*(.*)\\.");

	public String getEntityName(String source, String filename) {
		String result;
		Matcher matcherStart = programIDPattern.matcher(source);
		if (matcherStart.find() == true) {
			result = StringUtil.toClassName(matcherStart.group(1).toLowerCase());

		} else {
			result = FileUtils.fileWithoutAnyExtension(filename);
		}
		return result;
	}
}

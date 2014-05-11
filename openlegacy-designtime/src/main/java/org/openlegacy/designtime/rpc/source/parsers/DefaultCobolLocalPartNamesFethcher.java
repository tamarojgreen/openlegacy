package org.openlegacy.designtime.rpc.source.parsers;

import org.openlegacy.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultCobolLocalPartNamesFethcher implements CobolLocalPartNamesFethcher {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.rpc.source.parsers.CobolLocalPartNamesFethcher#get(java.lang.String, java.util.List)
	 */
	public Map<String, String> get(String source, List<String> externalParts) {
		Map<String, String> localVsExternal = new HashMap<String, String>();

		for (String externalPartName : externalParts) {

			String varaiblePattern = "\\d.*\\s(\\w.*)\\s*\\.\\s*\\r?\\n\\s*COPY\\s*" + externalPartName.toUpperCase();
			Pattern pattern = Pattern.compile(varaiblePattern);

			Matcher matcher = pattern.matcher(source);

			while (matcher.find() == true) {
				localVsExternal.put(StringUtil.toClassName(matcher.group(1).toLowerCase()), externalPartName);

			}

		}
		return localVsExternal;
	}
}

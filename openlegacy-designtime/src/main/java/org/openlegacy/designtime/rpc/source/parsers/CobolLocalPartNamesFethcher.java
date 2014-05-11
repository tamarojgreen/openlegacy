package org.openlegacy.designtime.rpc.source.parsers;

import java.util.List;
import java.util.Map;

public interface CobolLocalPartNamesFethcher {

	public Map<String, String> get(String source, List<String> externalParts);

}
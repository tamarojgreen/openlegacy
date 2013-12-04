package org.openlegacy.designtime.rpc.source;

import org.springframework.util.Assert;

import java.util.Map;

public class SimpleCodeParserFactory implements CodeParserFactory {

	private Map<String, CodeParser> parsers;

	public void setParsers(Map<String, CodeParser> parsers) {
		this.parsers = parsers;
	}

	public CodeParser getParser(String fileExtension) {
		Assert.notNull(parsers, "Parsers not set");
		return parsers.get(fileExtension);
	}
}

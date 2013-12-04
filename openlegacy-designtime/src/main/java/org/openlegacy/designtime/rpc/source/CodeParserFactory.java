package org.openlegacy.designtime.rpc.source;

public interface CodeParserFactory {

	CodeParser getParser(String fileExtension);
}

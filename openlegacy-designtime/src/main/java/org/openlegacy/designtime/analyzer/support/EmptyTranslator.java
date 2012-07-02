package org.openlegacy.designtime.analyzer.support;

import org.openlegacy.designtime.analyzer.TextTranslator;

/**
 * An empty implementation for text translation. Default one, which can be used for English based applications
 * 
 */
public class EmptyTranslator implements TextTranslator {

	public String translate(String text) {
		return text;
	}

}

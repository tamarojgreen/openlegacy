package org.openlegacy.designtime.analyzer;

/**
 * A common interface for translating texts to English, in order to use as class, field names. Implementations may use various
 * techniques: dictionary, char replacing, online translation, etc
 */
public interface TextTranslator {

	String translate(String text);
}

package org.openlegacy;

/**
 * A field formatter format the content of a field text. typically removes certain chars: ' ', '-', etc according to configuration
 */
public interface FieldFormatter {

	String format(String s);
}

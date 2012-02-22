package org.openlegacy.designtime.terminal.analyzer;

import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;

/**
 * A fact processor process a fact found the screen analyzer rules, and apply the fact into the given screen entity definitions
 * 
 */
public interface ScreenFactProcessor {

	boolean accept(ScreenFact screenFact);

	void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact);
}

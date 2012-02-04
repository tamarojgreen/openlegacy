package org.openlegacy.terminal.layout;

import org.openlegacy.layout.PagePartDefinition;

public interface PositionedPagePartDefinition extends PagePartDefinition {

	int getTopMarginPercentage();

	int getLeftMarginPercentage();
}

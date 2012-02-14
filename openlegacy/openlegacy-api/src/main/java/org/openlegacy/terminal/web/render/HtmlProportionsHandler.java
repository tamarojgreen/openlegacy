package org.openlegacy.terminal.web.render;

public interface HtmlProportionsHandler {

	int toWidth(int column);

	int toHeight(int row);

	int getFontSize();

	int getInputHeight();

	int getInputAdditionalWidth();
}

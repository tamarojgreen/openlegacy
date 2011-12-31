package org.openlegacy.terminal.web.render.support;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.web.render.ElementsProvider;
import org.openlegacy.terminal.web.render.HtmlProportionsHandler;
import org.openlegacy.utils.StringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.text.MessageFormat;

import javax.inject.Inject;

public class DefaultElementsProvider implements ElementsProvider {

	@Inject
	private HtmlProportionsHandler htmlProportionsHandler;

	public void createLabel(Node rootNode, TerminalField field) {
		String value = field.getValue().trim();

		if (value.length() == 0) {
			return;
		}
		Element label = rootNode.getOwnerDocument().createElement("span");
		populateCommonAttributes(label, field);

		Text textNode = rootNode.getOwnerDocument().createTextNode(value);
		label.appendChild(textNode);
		rootNode.appendChild(label);
	}

	public void createInput(Node rootNode, TerminalField field) {
		Element input = rootNode.getOwnerDocument().createElement(HtmlConstants.INPUT);
		populateCommonAttributes(input, field);
		input.setAttribute(HtmlConstants.VALUE, field.getValue());
		input.setAttribute(HtmlConstants.MAXLENGTH, String.valueOf(field.getLength()));
		int width = htmlProportionsHandler.toWidth(field.getLength());
		input.setAttribute(HtmlConstants.STYLE,
				MessageFormat.format("{0}{1}", input.getAttribute(HtmlConstants.STYLE), HtmlNamingUtil.toStyleWidth(width)));
		String fieldName = HtmlNamingUtil.getFieldName(field);
		input.setAttribute(HtmlConstants.NAME, fieldName);
		rootNode.appendChild(input);
	}

	private void populateCommonAttributes(Element element, TerminalField field) {
		int offset = StringUtil.startOfNonBlank(field.getValue());
		int column = field.getPosition().getColumn();
		int top = htmlProportionsHandler.toHeight(field.getPosition().getRow());
		int left = htmlProportionsHandler.toWidth(offset + column);
		element.setAttribute(HtmlConstants.STYLE, HtmlNamingUtil.toStyleTopLeft(top, left));

	}

}

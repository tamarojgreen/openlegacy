package org.openlegacy.terminal.web.render.support;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.web.render.ElementsProvider;
import org.openlegacy.terminal.web.render.HtmlProportionsHandler;
import org.openlegacy.utils.StringUtil;
import org.openlegacy.web.HtmlConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.text.MessageFormat;

import javax.inject.Inject;

public class DefaultElementsProvider implements ElementsProvider<Element> {

	@Inject
	private HtmlProportionsHandler htmlProportionsHandler;

	public Element createLabel(Element rootNode, TerminalField field) {
		String value = field.getValue().trim();

		if (value.length() == 0) {
			return null;
		}
		Element label = createTag(rootNode, HtmlConstants.SPAN);
		populateCommonAttributes(label, field);

		Text textNode = rootNode.getOwnerDocument().createTextNode(value);
		label.appendChild(textNode);
		rootNode.appendChild(label);
		return label;
	}

	public Element createTag(Element rootNode, String tagName) {
		return rootNode.getOwnerDocument().createElement(tagName);
	}

	public Element createInput(Element parentTag, TerminalField field) {
		Element input = createTag(parentTag, HtmlConstants.INPUT);
		if (field != null) {
			populateCommonAttributes(input, field);
			input.setAttribute(HtmlConstants.VALUE, field.getValue());
			input.setAttribute(HtmlConstants.MAXLENGTH, String.valueOf(field.getLength()));
			int width = htmlProportionsHandler.toWidth(field.getLength()) + htmlProportionsHandler.getInputAdditionalWidth();

			input.setAttribute(HtmlConstants.STYLE,
					MessageFormat.format("{0}{1}", input.getAttribute(HtmlConstants.STYLE), HtmlNamingUtil.toStyleWidth(width)));
			String fieldName = HtmlNamingUtil.getFieldName(field);
			input.setAttribute(HtmlConstants.NAME, fieldName);
			input.setAttribute(HtmlConstants.ID, fieldName);
		}
		parentTag.appendChild(input);
		return input;
	}

	private void populateCommonAttributes(Element element, TerminalField field) {
		int offset = StringUtil.startOfNonBlank(field.getValue());
		int column = field.getPosition().getColumn();
		int top = htmlProportionsHandler.toHeight(field.getPosition().getRow());
		int left = htmlProportionsHandler.toWidth(offset + column);
		element.setAttribute(HtmlConstants.STYLE, HtmlNamingUtil.toStyleTopLeft(top, left));

	}

	public Element createStyleTag(Element parentTag, String styleSettings) {
		Document doc = parentTag.getOwnerDocument();
		Element styleTag = createTag(parentTag, HtmlConstants.STYLE);
		styleTag.appendChild(doc.createTextNode(styleSettings));
		parentTag.appendChild(styleTag);
		return styleTag;
	}

	public Element createHidden(Element parentTag, String name) {
		Element element = createInput(parentTag, null);
		element.setAttribute(HtmlConstants.TYPE, HtmlConstants.HIDDEN);
		element.setAttribute(HtmlConstants.NAME, name);
		return element;
	}

	public Element createFormTag(Element parentTag, String formActionURL, String formMethod, String formName) {
		Element formTag = createTag(parentTag, HtmlConstants.FORM);
		formTag.setAttribute(HtmlConstants.NAME, formName);
		formTag.setAttribute(HtmlConstants.ID, formName);
		formTag.setAttribute(HtmlConstants.ACTION, formActionURL);
		formTag.setAttribute(HtmlConstants.METHOD, formMethod);
		parentTag.appendChild(formTag);
		return formTag;
	}

	public Element createScriptTag(Element parentTag, String script) {
		Document doc = parentTag.getOwnerDocument();
		Element scriptTag = createTag(parentTag, HtmlConstants.SCRIPT);
		scriptTag.setAttribute(HtmlConstants.TYPE, HtmlConstants.TEXT_JAVASCRIPT);
		scriptTag.appendChild(doc.createTextNode(script));
		parentTag.appendChild(scriptTag);
		return scriptTag;
	}

}

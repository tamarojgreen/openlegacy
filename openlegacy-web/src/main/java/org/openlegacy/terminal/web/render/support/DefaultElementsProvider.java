/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.web.render.support;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.web.render.ElementsProvider;
import org.openlegacy.terminal.web.render.HtmlProportionsHandler;
import org.openlegacy.utils.StringUtil;
import org.openlegacy.web.HtmlConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.text.MessageFormat;
import java.util.Map;

import javax.inject.Inject;

public class DefaultElementsProvider implements ElementsProvider<Element> {

	@Inject
	private HtmlProportionsHandler htmlProportionsHandler;

	private Map<Color, String> colorMapper = null;
	private Map<Color, String> backcolorMapper = null;
	private String defaultColor = "green";
	private String defaultBackgroundColor = "white";

	// usefull for RTL application
	private boolean renderTopRight = false;

	@Inject
	private OpenLegacyProperties openLegacyProperties;

	public Element createLabel(Element rootNode, TerminalField field) {

		String value = field.getValue();
		if (StringUtils.isWhitespace(value) && field.getBackColor() == Color.BLACK) {
			return null;
		}
		if (field.getBackColor() == Color.BLACK) {
			value = value.trim();
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
			if (field.isRightToLeft()) {
				// align with HATS bidi-override
				input.setAttribute(HtmlConstants.STYLE, input.getAttribute(HtmlConstants.STYLE)
						+ ";direction:rtl;unicode-bidi: bidi-override;");
			}
			if (field.isUppercase() || openLegacyProperties.isUppercaseInput()) {
				// align with HATS bidi-override
				input.setAttribute(HtmlConstants.STYLE, input.getAttribute(HtmlConstants.STYLE) + ";text-transform:uppercase;");
			}
			String fieldName = HtmlNamingUtil.getFieldName(field);
			input.setAttribute(HtmlConstants.NAME, fieldName);
			input.setAttribute(HtmlConstants.ID, fieldName);
			if (field.isHidden()) {
				input.setAttribute(HtmlConstants.TYPE, HtmlConstants.PASSWORD);
			}
		}
		parentTag.appendChild(input);
		return input;
	}

	private void populateCommonAttributes(Element element, TerminalField field) {
		String value = field.getValue();

		int top = htmlProportionsHandler.toHeight(field.getPosition().getRow());
		int column = 0;
		String positioningStyle = null;

		int offset = 0;

		// default rendering - top/left
		if (!renderTopRight) {
			if (!field.isEditable() && value != null) {
				offset = StringUtil.startOfNonBlank(value);
			}
			column = field.getPosition().getColumn();
			int left = htmlProportionsHandler.toWidth(offset + column);
			positioningStyle = MessageFormat.format("top:{0}{2};left:{1}{2};", top, left, HtmlConstants.STYLE_UNIT);
		} else {
			int rightOffset = StringUtil.endOfNonBlank(value);

			column = field.getEndPosition().getColumn();
			int right = htmlProportionsHandler.toWidth(80 + rightOffset - column);
			positioningStyle = MessageFormat.format("top:{0}{2};right:{1}{2};", top, right, HtmlConstants.STYLE_UNIT);
		}

		String colorsStyle = toStyleColors(field);
		element.setAttribute(HtmlConstants.STYLE, positioningStyle + colorsStyle);

	}

	public String toStyleColors(TerminalField field) {
		String color = getColor(field.getColor(), colorMapper, defaultColor);
		String backcolor = getColor(field.getBackColor(), backcolorMapper, defaultBackgroundColor);

		String colorStyle = "";
		if (color != null) {
			colorStyle = MessageFormat.format("color:{0};", color);
		}
		String backcolorStyle = "";
		if (backcolor != null) {
			backcolorStyle = MessageFormat.format("background-color:{0};", backcolor);
		}
		return colorStyle + backcolorStyle;
	}

	private static String getColor(Color color, Map<Color, String> colorMapper, String defaultColor) {
		// Not all fields have color
		if (color == null) {
			return defaultColor;
		}
		String colorText = null;
		// if not color mapper is defined or color not found on mapper -> convert host color to HTML style color
		if (colorMapper == null || colorMapper.get(color) == null) {
			colorText = color.toString().toLowerCase();
		} else {
			colorText = colorMapper.get(color);
		}
		// don't generate color if it equals to the default page color/background color
		if (colorText.equals(defaultColor)) {
			return null;
		} else {
			return colorText;
		}
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

	public void setColorMapper(Map<Color, String> colorMapper) {
		this.colorMapper = colorMapper;
	}

	public void setBackcolorMapper(Map<Color, String> backcolorMapper) {
		this.backcolorMapper = backcolorMapper;
	}

	public void setDefaultBackgroundColor(String defaultBackgroundColor) {
		this.defaultBackgroundColor = defaultBackgroundColor;
	}

	public void setDefaultColor(String defaultColor) {
		this.defaultColor = defaultColor;
	}

	public void setRenderTopRight(boolean renderTopRight) {
		this.renderTopRight = renderTopRight;
	}

	public Element createTextarea(Element rootTag, TerminalField field) {
		Element textarea = createTag(rootTag, HtmlConstants.TEXTAREA);
		if (field != null) {
			populateCommonAttributes(textarea, field);
			if (field.isMultyLine()) {
				int rows = field.getEndPosition().getRow() - field.getPosition().getRow() + 1;
				int columns = field.getEndPosition().getColumn() - field.getPosition().getColumn();
				textarea.setAttribute(HtmlConstants.COLUMNS, String.valueOf(columns));
				textarea.setAttribute(
						HtmlConstants.STYLE,
						MessageFormat.format("{0};height:{1}px", textarea.getAttribute(HtmlConstants.STYLE),
								String.valueOf(htmlProportionsHandler.toHeight(rows) - 8))); // 8 - avoid overlap
				;
			} else {
				textarea.setAttribute(HtmlConstants.ROWS, "1");
				textarea.setAttribute(HtmlConstants.COLUMNS, String.valueOf(field.getLength()));
			}
			textarea.setAttribute(HtmlConstants.ONKEYUP,
					MessageFormat.format("return (this.value.length <= {0});", field.getLength()));

			if (field.isUppercase() || openLegacyProperties.isUppercaseInput()) {
				textarea.setAttribute(HtmlConstants.STYLE, textarea.getAttribute(HtmlConstants.STYLE)
						+ ";text-transform:uppercase;");
			}
			// set value
			Text textNode = rootTag.getOwnerDocument().createTextNode(field.getValue());
			textarea.appendChild(textNode);

			String fieldName = HtmlNamingUtil.getFieldName(field);
			textarea.setAttribute(HtmlConstants.NAME, fieldName);
			textarea.setAttribute(HtmlConstants.ID, fieldName);
		}
		rootTag.appendChild(textarea);
		return textarea;

	}
}

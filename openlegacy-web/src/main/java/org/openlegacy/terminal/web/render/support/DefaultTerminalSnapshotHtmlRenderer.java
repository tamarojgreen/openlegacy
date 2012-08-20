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

import org.apache.commons.io.IOUtils;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.web.render.ElementsProvider;
import org.openlegacy.terminal.web.render.HtmlProportionsHandler;
import org.openlegacy.terminal.web.render.TerminalSnapshotHtmlRenderer;
import org.openlegacy.utils.DomUtils;
import org.openlegacy.utils.StringUtil;
import org.openlegacy.web.HtmlConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collection;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

public class DefaultTerminalSnapshotHtmlRenderer implements TerminalSnapshotHtmlRenderer {

	private static final String SNAPSHOT_STYLE_SETTINGS = "#terminalSnapshot {font-family:FONT_FAMILY;font-size:FONTpx} #terminalSnapshot span {white-space: pre;position:absolute;} #terminalSnapshot input {position:absolute;font-family:Courier New;font-size:FONTpx;height:INPUT-HEIGHTpx;STYLE}";

	private static final String TERMINAL_HTML = "TERMINAL_HTML";

	private String templateResourceName = "defaultHtmlEmulationTemplate.html";
	private boolean includeTemplate = true;

	private String fontFamily = "Courier New";

	private String style = "";

	@Inject
	private ElementsProvider<Element> elementsProvider;

	@Inject
	private HtmlProportionsHandler htmlProportionsHandler;

	private String formActionURL = "emulation";
	private String formMethod = HtmlConstants.POST;

	public String render(TerminalSnapshot terminalSnapshot) {
		String htmlContent = renderHtml(terminalSnapshot);

		if (includeTemplate) {
			InputStream htmlEmulationTemplateStream = getClass().getResourceAsStream(templateResourceName);
			String htmlEmulationTemplateContent;
			try {
				htmlEmulationTemplateContent = IOUtils.toString(htmlEmulationTemplateStream);
			} catch (IOException e) {
				throw (new RuntimeException(e));
			}
			htmlEmulationTemplateContent = htmlEmulationTemplateContent.replace(TERMINAL_HTML, htmlContent);
			return htmlEmulationTemplateContent;
		}
		return htmlContent;
	}

	private String renderHtml(TerminalSnapshot terminalSnapshot) {

		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();

		Document doc;
		try {
			doc = dfactory.newDocumentBuilder().newDocument();

			String styleSettings = createStyleSettings();

			Element formTag = createWrappingTag(doc);

			String cursorFieldName = createHiddens(terminalSnapshot, formTag);

			createFields(terminalSnapshot, formTag);

			createScript(formTag, cursorFieldName);

			calculateWidthHeight(terminalSnapshot, formTag);

			// generate style before the document. cause non aligned page when it's part of the document
			styleSettings = MessageFormat.format("<style>{0}</style>", styleSettings);
			return generate(styleSettings, doc);

		} catch (ParserConfigurationException e) {
			throw (new OpenLegacyRuntimeException(e));
		} catch (TransformerException e) {
			throw (new OpenLegacyRuntimeException(e));
		}

	}

	private String createStyleSettings() {
		String styleSettings = SNAPSHOT_STYLE_SETTINGS.replaceAll("FONT_FAMILY", String.valueOf(fontFamily));
		styleSettings = styleSettings.replaceAll("FONT", String.valueOf(htmlProportionsHandler.getFontSize()));
		styleSettings = styleSettings.replace("STYLE", style);

		styleSettings = styleSettings.replaceAll("INPUT-HEIGHT", String.valueOf(htmlProportionsHandler.getInputHeight()));
		return styleSettings;
	}

	private Element createWrappingTag(Document doc) {
		Element wrapperTag = (Element)doc.appendChild(doc.createElement(HtmlConstants.DIV));

		wrapperTag.setAttribute(HtmlConstants.ID, TerminalHtmlConstants.WRAPPER_TAG_ID);

		Element formTag = elementsProvider.createFormTag(wrapperTag, formActionURL, formMethod,
				TerminalHtmlConstants.HTML_EMULATION_FORM_NAME);
		return formTag;
	}

	private void createScript(Element formTag, String cursorFieldName) {
		String script = MessageFormat.format("document.{0}.{1}.focus();", TerminalHtmlConstants.HTML_EMULATION_FORM_NAME,
				cursorFieldName);

		elementsProvider.createScriptTag(formTag, script);
	}

	private String createHiddens(TerminalSnapshot terminalSnapshot, Element formTag) {
		TerminalPosition cursorPosition = terminalSnapshot.getCursorPosition();
		String cursorFieldName = cursorPosition != null ? HtmlNamingUtil.getFieldName(cursorPosition) : "";
		Element cursorHidden = elementsProvider.createHidden(formTag, TerminalHtmlConstants.TERMINAL_CURSOR_HIDDEN);
		cursorHidden.setAttribute(HtmlConstants.VALUE, cursorFieldName);
		elementsProvider.createHidden(formTag, TerminalHtmlConstants.KEYBOARD_KEY);
		return cursorFieldName;
	}

	private static String generate(String styleSettings, Document doc) throws TransformerConfigurationException,
			TransformerFactoryConfigurationError, TransformerException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DomUtils.render(doc, baos);
		return styleSettings + StringUtil.toString(baos);
	}

	private void calculateWidthHeight(TerminalSnapshot terminalSnapshot, Element wrapperTag) {
		int width = htmlProportionsHandler.toWidth(terminalSnapshot.getSize().getColumns() + 1);
		int height = htmlProportionsHandler.toHeight(terminalSnapshot.getSize().getRows() + 1);

		wrapperTag.setAttribute(
				HtmlConstants.STYLE,
				MessageFormat.format("position:relative;{0}{1}", HtmlNamingUtil.toStyleWidth(width),
						HtmlNamingUtil.toStyleHeight(height)));
	}

	private void createFields(TerminalSnapshot terminalSnapshot, Element formTag) {
		Collection<TerminalField> fields = terminalSnapshot.getFields();
		for (TerminalField terminalField : fields) {
			if (terminalField.isEditable()) {
				elementsProvider.createInput(formTag, terminalField);
			} else {
				elementsProvider.createLabel(formTag, terminalField);
			}
		}
	}

	public void setFormActionURL(String formActionURL) {
		this.formActionURL = formActionURL;
	}

	public void setFormMethod(String formMethod) {
		this.formMethod = formMethod;
	}

	public void setIncludeTemplate(boolean includeTemplate) {
		this.includeTemplate = includeTemplate;
	}

	public void setTemplateResourceName(String templateResourceName) {
		this.templateResourceName = templateResourceName;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public void setStyle(String style) {
		this.style = style;
	}
}

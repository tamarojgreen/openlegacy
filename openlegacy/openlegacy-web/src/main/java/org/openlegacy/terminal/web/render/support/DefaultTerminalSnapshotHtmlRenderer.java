package org.openlegacy.terminal.web.render.support;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.web.render.ElementsProvider;
import org.openlegacy.terminal.web.render.HtmlProportionsHandler;
import org.openlegacy.terminal.web.render.TerminalSnapshotHtmlRenderer;
import org.openlegacy.web.HtmlConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Collection;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class DefaultTerminalSnapshotHtmlRenderer implements TerminalSnapshotHtmlRenderer {

	private static final String SNAPSHOT_STYLE_SETTINGS = "#terminalSnapshot {font-family:Courier New;font-size:FONTpx} #terminalSnapshot span {white-space: pre;position:absolute;} #terminalSnapshot input {position:absolute;font-family:Courier New;font-size:FONTpx}";

	@Inject
	private ElementsProvider<Element, Node> elementsProvider;

	@Inject
	private HtmlProportionsHandler htmlProportionsHandler;

	private String formActionURL = "HtmlEmulation";
	private String formMethod = HtmlConstants.POST;

	public void render(TerminalSnapshot terminalSnapshot, OutputStream outputStream) {

		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();

		Document doc;
		try {
			doc = dfactory.newDocumentBuilder().newDocument();

			String styleSettings = SNAPSHOT_STYLE_SETTINGS.replaceAll("FONT",
					String.valueOf(htmlProportionsHandler.getFontSize()));

			Element wrapperTag = elementsProvider.createWrapperTag(doc);
			wrapperTag.setAttribute(HtmlConstants.ID, TerminalHtmlConstants.WRAPPER_TAG_ID);

			Element formTag = elementsProvider.createFormTag(wrapperTag, formActionURL, formMethod,
					TerminalHtmlConstants.HTML_EMULATION_FORM_NAME);

			elementsProvider.createHidden(formTag, TerminalHtmlConstants.TERMINAL_CURSOR_HIDDEN);
			elementsProvider.createHidden(formTag, TerminalHtmlConstants.TERMINAL_COMMAND_HIDDEN);

			elementsProvider.createStyleTag(wrapperTag, styleSettings);

			createFields(terminalSnapshot, formTag);

			calculateWidthHeight(terminalSnapshot, wrapperTag);

			generate(outputStream, doc);

		} catch (ParserConfigurationException e) {
			throw (new OpenLegacyRuntimeException(e));
		} catch (TransformerException e) {
			throw (new OpenLegacyRuntimeException(e));
		}

	}

	private static void generate(OutputStream outputStream, Document doc) throws TransformerConfigurationException,
			TransformerFactoryConfigurationError, TransformerException {
		Transformer trans = TransformerFactory.newInstance().newTransformer();
		trans.setOutputProperty(OutputKeys.METHOD, "html");
		trans.setOutputProperty(OutputKeys.INDENT, "yes");

		DOMSource src = new DOMSource(doc.getDocumentElement());

		trans.transform(src, new StreamResult(outputStream));
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
}

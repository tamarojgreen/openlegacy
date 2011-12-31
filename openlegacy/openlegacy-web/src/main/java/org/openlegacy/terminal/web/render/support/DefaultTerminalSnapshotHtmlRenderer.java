package org.openlegacy.terminal.web.render.support;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.web.render.ElementsProvider;
import org.openlegacy.terminal.web.render.HtmlProportionsHandler;
import org.openlegacy.terminal.web.render.TerminalSnapshotHtmlRenderer;
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
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class DefaultTerminalSnapshotHtmlRenderer implements TerminalSnapshotHtmlRenderer {

	private static final String SNAPSHOT_STYLE_SETTINGS = "#terminalSnapshot {font-family:Courier New;font-size:FONTpx} #terminalSnapshot span {white-space: pre;position:absolute;} #terminalSnapshot input {position:absolute;font-family:Courier New}";

	@Inject
	private ElementsProvider elementsProvider;

	@Inject
	private HtmlProportionsHandler htmlProportionsHandler;

	public void render(TerminalSnapshot terminalSnapshot, OutputStream outputStream) {

		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();

		Document doc;
		try {
			doc = dfactory.newDocumentBuilder().newDocument();

			String styleSettings = SNAPSHOT_STYLE_SETTINGS.replaceAll("FONT",
					String.valueOf(htmlProportionsHandler.getFontSize()));

			Element wrapperTag = doc.createElement("div");
			wrapperTag.setAttribute("id", "terminalSnapshot");
			Element styleTag = doc.createElement("style");
			styleTag.appendChild(doc.createTextNode(styleSettings));

			Node rootNode = doc.appendChild(wrapperTag);
			rootNode.appendChild(styleTag);

			Collection<TerminalField> fields = terminalSnapshot.getFields();
			for (TerminalField terminalField : fields) {
				if (terminalField.isEditable()) {
					elementsProvider.createInput(rootNode, terminalField);
				} else {
					elementsProvider.createLabel(rootNode, terminalField);
				}
			}

			int width = htmlProportionsHandler.toWidth(terminalSnapshot.getSize().getColumns() + 1);
			int height = htmlProportionsHandler.toHeight(terminalSnapshot.getSize().getRows() + 1);

			wrapperTag.setAttribute(
					"style",
					MessageFormat.format("position:relative;{0}{1}", HtmlNamingUtil.toStyleWidth(width),
							HtmlNamingUtil.toStyleHeight(height)));

			Transformer trans = TransformerFactory.newInstance().newTransformer();
			trans.setOutputProperty(OutputKeys.METHOD, "html");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource src = new DOMSource(doc.getDocumentElement());

			trans.transform(src, new StreamResult(outputStream));

		} catch (ParserConfigurationException e) {
			throw (new OpenLegacyRuntimeException(e));
		} catch (TransformerException e) {
			throw (new OpenLegacyRuntimeException(e));
		}

	}
}

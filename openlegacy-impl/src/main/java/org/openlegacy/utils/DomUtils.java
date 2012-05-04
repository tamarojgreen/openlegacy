package org.openlegacy.utils;

import org.w3c.dom.Document;

import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class DomUtils {

	public static void render(Document doc, OutputStream outputStream) throws TransformerConfigurationException,
			TransformerFactoryConfigurationError, TransformerException {
		Transformer trans = TransformerFactory.newInstance().newTransformer();
		trans.setOutputProperty(OutputKeys.METHOD, "html");
		trans.setOutputProperty(OutputKeys.INDENT, "yes");

		DOMSource src = new DOMSource(doc.getDocumentElement());

		trans.transform(src, new StreamResult(outputStream));
	}

}

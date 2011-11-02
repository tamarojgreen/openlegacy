package org.openlegacy.utils;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JaxbUtil {

	public static <T> void marshal(Class<T> rootClass, T persistedObject, OutputStream out) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(rootClass);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(persistedObject, out);
	}

	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(Class<T> rootClass, InputStream in) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(rootClass);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (T)unmarshaller.unmarshal(in);
	}
}

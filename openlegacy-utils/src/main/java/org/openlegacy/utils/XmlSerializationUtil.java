/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.utils;

import com.thoughtworks.xstream.XStream;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.DirectFieldAccessor;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Marshaller.Listener;
import javax.xml.bind.Unmarshaller;

public class XmlSerializationUtil {

	public static <T> void serialize(Class<T> rootClass, T persistedObject, OutputStream out) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(rootClass);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setListener(new Listener() {

			@Override
			public void beforeMarshal(Object source) {
				resetDefaultValues(source);
				super.beforeMarshal(source);
			}

		});
		marshaller.marshal(persistedObject, out);
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserialize(Class<T> rootClass, InputStream in) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(rootClass);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		Reader reader;
		try {
			reader = new InvalidXMLCharacterFilterReader(new BufferedReader(new InputStreamReader(in, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw (new RuntimeException(e));
		}
		return (T)unmarshaller.unmarshal(reader);
	}

	/**
	 * This method purpose is to reduce the amount of XML written when serializing an object It reset member to null when the
	 * default value matches the object value
	 * 
	 * @param source
	 */
	private static void resetDefaultValues(Object source) {
		DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(source);
		PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(source);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			String propertyName = propertyDescriptor.getName();
			Class<?> propertyType = fieldAccessor.getPropertyType(propertyName);
			if (propertyType == null || Collection.class.isAssignableFrom(propertyType)
					|| Map.class.isAssignableFrom(propertyType)) {
				continue;
			}
			Object defaultValue = PropertyUtil.getPropertyDefaultValue(source.getClass(), propertyName);
			Object value = fieldAccessor.getPropertyValue(propertyName);
			if (fieldAccessor.isWritableProperty(propertyName) && ObjectUtils.equals(value, defaultValue)
					&& !propertyType.isPrimitive()) {
				fieldAccessor.setPropertyValue(propertyName, null);
			}

		}
	}

	public static class InvalidXMLCharacterFilterReader extends FilterReader {

		public InvalidXMLCharacterFilterReader(Reader in) {
			super(in);
		}

		@Override
		public int read() throws IOException {
			char[] buf = new char[1];
			int result = read(buf, 0, 1);
			if (result == -1) {
				return -1;
			} else {
				return buf[0];
			}
		}

		@Override
		public int read(char[] buf, int from, int len) throws IOException {
			int count = 0;
			while (count == 0) {
				count = in.read(buf, from, len);
				if (count == -1) {
					return -1;
				}

				int last = from;
				for (int i = from; i < from + count; i++) {
					if (!isBadXMLChar(buf[i])) {
						buf[last++] = buf[i];
					} else {
						buf[last++] = ' ';
					}
				}

				count = last - from;
			}
			return count;
		}

		private static boolean isBadXMLChar(char c) {
			if ((c == 0x9) || (c == 0xA) || (c == 0xD) || ((c >= 0x20) && (c <= 0xD7FF)) || ((c >= 0xE000) && (c <= 0xFFFD))
					|| ((c >= 0x10000) && (c <= 0x10FFFF))) {
				return false;
			}
			return true;
		}
	}

	public static String xStreamSerialize(Object... objs) {
		XStream xStream = new XStream();
		if (objs.length > 1) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (Object obj : objs) {
				xStream.toXML(obj, baos);
			}
			String result = baos.toString();
			try {
				baos.close();
			} catch (Exception e) {
			}
			return result;
		} else {
			return xStream.toXML(objs[0]);
		}
	}

	public static Object xStreamDeserialize(String obj) {
		return new XStream().fromXML(obj);
	}
}

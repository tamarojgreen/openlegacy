package org.openlegacy.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.DirectFieldAccessor;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.io.OutputStream;
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
		return (T)unmarshaller.unmarshal(in);
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
			if (propertyType == null || propertyType == Class.class || Collection.class.isAssignableFrom(propertyType)
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

}

/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PropertyChangedUtils {

	public static class PropertyChangedEvent {

		String beanOrClassName;

		PropertyChangeEvent event;

		public PropertyChangedEvent setBeanOrClassName(String beanOrClassName) {
			this.beanOrClassName = beanOrClassName;
			return this;
		}

		public PropertyChangedEvent setEvent(PropertyChangeEvent event) {
			this.event = event;
			return this;
		}
	}

	public static PropertyChangeEvent getEvent(Object source, String propertyName, Object newValue) {
		return new PropertyChangeEvent(source, propertyName, null, newValue);
	}

	public static PropertyChangeEvent getEvent(Object source, String propertyName, Object oldValue, Object newValue) {
		return new PropertyChangeEvent(source, propertyName, oldValue, newValue);
	}

	public static void sendEvent(PropertyChangedEvent event) {
		if (SpringUtil.getApplicationContext() == null) {
			return;
		}
		try {
			PropertyChangeListener listener = (PropertyChangeListener)SpringUtil.getApplicationContext().getBean(
					event.beanOrClassName);
			listener.propertyChange(event.event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

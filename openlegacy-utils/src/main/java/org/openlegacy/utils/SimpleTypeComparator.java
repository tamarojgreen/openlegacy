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
package org.openlegacy.utils;

import org.apache.commons.lang.NotImplementedException;

import java.text.MessageFormat;
import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class SimpleTypeComparator implements Comparator {

	public static SimpleTypeComparator instance = new SimpleTypeComparator();

	public static SimpleTypeComparator instance() {
		return instance;
	}

	public int compare(Object o1, Object o2) {
		if (o1.getClass() != o2.getClass()) {
			throw (new IllegalArgumentException(MessageFormat.format("Cannot compare 2 different types:{0},{1}", o1, o2)));
		}
		if (o1 instanceof String) {
			return ((String)o1).compareTo((String)o2);
		}
		if (o1 instanceof Integer) {
			return ((Integer)o1) - (Integer)o2;
		}
		throw (new NotImplementedException("Only String and Integer compare was implemented"));
	}
}

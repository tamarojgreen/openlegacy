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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class XMLUtils {

	public static interface ElementChecker {

		public boolean check(Element element);
	}

	public static List<Element> getElementsByTagName(Element parent, String[] names, ElementChecker checker) {
		List<Element> result = new ArrayList<Element>();
		if (names != null) {
			for (String name : names) {
				NodeList nodes = parent.getElementsByTagName(name);
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);
					if (Element.class.isAssignableFrom(node.getClass())) {

						if (checker != null) {
							if (!checker.check((Element)node)) {
								continue;
							}
						}

						result.add((Element)node);
					}
				}
			}
		}
		return result;
	}
}

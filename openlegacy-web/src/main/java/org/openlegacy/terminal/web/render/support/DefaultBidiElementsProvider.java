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

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalFieldSplitter;
import org.openlegacy.terminal.support.BidiTerminalFieldSplitter;
import org.w3c.dom.Element;

import java.util.List;

public class DefaultBidiElementsProvider extends DefaultElementsProvider {

	@Override
	public Element createLabel(Element rootNode, TerminalField field) {
		String visualValue = field.getVisualValue();

		if (visualValue != null) {
			TerminalFieldSplitter splitter = new BidiTerminalFieldSplitter();
			List<TerminalField> fields = splitter.split(field);
			if (fields == null) {
				return super.createLabel(rootNode, field);
			} else {
				Element cotainer = rootNode.getOwnerDocument().createElement("span");
				cotainer.setAttribute("style", "position:relative");
				for (TerminalField splitField : fields) {
					Element node = super.createLabel(rootNode, splitField);
					cotainer.appendChild(node);
				}
				rootNode.appendChild(cotainer);
				return cotainer;
			}
		} else {
			return super.createLabel(rootNode, field);
		}
	}
}

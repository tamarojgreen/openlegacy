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
package org.openlegacy.terminal.web.render;

import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;

/**
 * An elements provider is a factory responsable for producing various element types. Used for rendering HTML from a
 * {@link TerminalSnapshot} fields ({@link TerminalField})
 * 
 * @author Roi Mor
 * 
 * @param <T>
 *            The tag type
 */
public interface ElementsProvider<T> {

	T createInput(T rootTag, TerminalField terminalField);

	T createHidden(T rootTag, String name);

	T createLabel(T rootTag, TerminalField terminalField, ScreenSize screenSize);

	T createStyleTag(T rootTag, String styleSettings);

	T createFormTag(T rootTag, String formActionURL, String formMethod, String formName);

	T createTag(T rootTag, String tagName);

	T createScriptTag(T wrapperTag, String script);

	T createTextarea(T rootTag, TerminalField terminalField);
}

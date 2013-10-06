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
package org.openlegacy.terminal.web.render;

/**
 * Responsible for converting host units to web units
 * 
 * @author Roi Mor
 * 
 */
public interface HtmlProportionsHandler {

	int toWidth(int column);

	int toHeight(int row);

	int getFontSize();

	Integer getInputHeight();

	int getInputAdditionalWidth();

	int getLetterSpacing();
}

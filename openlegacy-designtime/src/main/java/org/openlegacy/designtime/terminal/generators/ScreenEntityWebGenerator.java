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
package org.openlegacy.designtime.terminal.generators;

import org.openlegacy.designtime.mains.GeneratePageRequest;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.io.OutputStream;

public interface ScreenEntityWebGenerator {

	void generateAll(GeneratePageRequest generatePageRequest, ScreenEntityDefinition screenEntityDefinition)
			throws GenerationException;

	void generatePage(PageDefinition pageDefinition, OutputStream output) throws GenerationException;

	public void generateController(PageDefinition pageDefinition, OutputStream output) throws GenerationException;

	public void generateControllerAspect(PageDefinition pageDefinition, OutputStream output) throws GenerationException;
}

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
package org.openlegacy.designtime.generators;

import org.openlegacy.EntityDefinition;
import org.openlegacy.designtime.mains.GenerateControllerRequest;
import org.openlegacy.designtime.mains.GenerateViewRequest;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.layout.PageDefinition;

import java.io.File;
import java.io.OutputStream;

public interface EntityPageGenerator {

	void generateView(GenerateViewRequest generateViewRequest, EntityDefinition<?> entityDefinition) throws GenerationException;

	void generateController(GenerateControllerRequest generateControllerRequest, EntityDefinition<?> entityDefinition)
			throws GenerationException;

	void generatePage(PageDefinition pageDefinition, OutputStream output, String templateDirectoryPrefix)
			throws GenerationException;

	boolean isSupportControllerGeneration();

	void renameViews(String fileNoExtension, String newName, File projectPath);

}

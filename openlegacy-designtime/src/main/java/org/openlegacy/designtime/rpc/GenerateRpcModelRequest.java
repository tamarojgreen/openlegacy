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
package org.openlegacy.designtime.rpc;

import org.openlegacy.EntityDefinition;
import org.openlegacy.designtime.EntityUserInteraction;
import org.openlegacy.designtime.GenerateModelRequest;
import org.openlegacy.designtime.mains.AbstractGenerateRequest;

import java.io.File;

public class GenerateRpcModelRequest extends AbstractGenerateRequest implements GenerateModelRequest {

	private File sourceFile;

	private EntityUserInteraction<EntityDefinition<?>> entityUserInteraction;

	private boolean generateSource = true;

	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	public EntityUserInteraction<EntityDefinition<?>> getEntityUserInteraction() {
		return entityUserInteraction;
	}

	public void setEntityUserInteraction(EntityUserInteraction<EntityDefinition<?>> entityUserInteraction) {
		this.entityUserInteraction = entityUserInteraction;
	}

	public boolean isGenerateSource() {
		return generateSource;
	}

	public void setGenerateSnapshotText(boolean generateSource) {
		this.generateSource = generateSource;
	}

}

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
package org.openlegacy.designtime.generators;

import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.PartEntityDefinition;

import java.io.File;
import java.util.Map;

public abstract class AbstractCodeBasedPartDefinition<F extends FieldDefinition, C extends PojoCodeModel> implements PartEntityDefinition<F> {

	private C codeModel;
	private File packageDir;

	public AbstractCodeBasedPartDefinition(C codeModel, File packageDir) {
		this.codeModel = codeModel;
		this.packageDir = packageDir;
	}

	public Class<?> getPartClass() {
		throw (new UnsupportedOperationException("Code based part does not support this method"));
	}

	public abstract Map<String, F> getFieldsDefinitions();

	public String getPartName() {
		return getCodeModel().getEntityName();
	}

	public String getDisplayName() {
		return getCodeModel().getDisplayName();
	}

	public String getClassName() {
		return getCodeModel().getClassName();
	}

	public C getCodeModel() {
		return codeModel;
	}

	public File getPackageDir() {
		return packageDir;
	}
}

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
package org.openlegacy.ide.eclipse.preview;

import org.eclipse.core.resources.IFile;

public class JavaClassProperties {

	private boolean isAnnotated;

	private IFile sourceFile;

	public JavaClassProperties() {}

	public JavaClassProperties(IFile sourceFile, boolean isAnnotated) {
		this.sourceFile = sourceFile;
		this.isAnnotated = isAnnotated;
	}

	public boolean isAnnotated() {
		return isAnnotated;
	}

	public void setAnnotated(boolean isAnnotated) {
		this.isAnnotated = isAnnotated;
	}

	public IFile getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(IFile sourceFile) {
		this.sourceFile = sourceFile;
	}
}

package org.openlegacy.ide.eclipse.preview;

import org.eclipse.core.resources.IFile;

public class JavaClassProperties {

	private boolean isAnnotated;

	private IFile xmlFile;

	public JavaClassProperties() {}

	public JavaClassProperties(IFile xmlFile, boolean isAnnotated) {
		this.xmlFile = xmlFile;
		this.isAnnotated = isAnnotated;
	}

	public boolean isAnnotated() {
		return isAnnotated;
	}

	public void setAnnotated(boolean isAnnotated) {
		this.isAnnotated = isAnnotated;
	}

	public IFile getXmlFile() {
		return xmlFile;
	}

	public void setXmlFile(IFile xmlFile) {
		this.xmlFile = xmlFile;
	}
}

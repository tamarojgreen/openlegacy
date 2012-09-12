package org.openlegacy.ide.eclipse.preview;

import org.eclipse.core.resources.IFile;

public class JavaClassProperties {

	private IFile imagefile;

	private boolean isAnnotated;

	public JavaClassProperties(IFile imageFile, boolean isAnnotated) {
		this.imagefile = imageFile;
		this.isAnnotated = isAnnotated;
	}

	public JavaClassProperties() {}

	public IFile getImagefile() {
		return imagefile;
	}

	public void setImagefile(IFile imagefile) {
		this.imagefile = imagefile;
	}

	public boolean isAnnotated() {
		return isAnnotated;
	}

	public void setAnnotated(boolean isAnnotated) {
		this.isAnnotated = isAnnotated;
	}
}

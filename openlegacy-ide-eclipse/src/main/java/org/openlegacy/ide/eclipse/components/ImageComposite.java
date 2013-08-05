package org.openlegacy.ide.eclipse.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class ImageComposite extends Composite {

	public ImageComposite(Composite parent) {
		super(parent, SWT.NONE);
	}

	public abstract void showEnlargedImage();

}

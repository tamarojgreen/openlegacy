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
package org.openlegacy.ide.eclipse.preview.rpc;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.swt.widgets.Composite;
import org.openlegacy.ide.eclipse.components.ImageComposite;
import org.openlegacy.ide.eclipse.components.rpc.RpcComposite;
import org.openlegacy.ide.eclipse.preview.AbstractEntityPreview;

import java.io.File;
import java.io.IOException;

public class RpcPreview extends AbstractEntityPreview {

	private static final String RPC_ENTITY_ANNOTATION = "org.openlegacy.annotations.rpc.RpcEntity";
	private static final String RPC_ENTITY_ANNOTATION_SHORT = "RpcEntity";

	private RpcComposite rpcComposite;

	@Override
	public void createPartControl(Composite parent) {
		rpcComposite = new RpcComposite(parent);
		rpcComposite.setIsScalable(true);

	}

	@Override
	protected String getEntityAnnotation() {
		return RPC_ENTITY_ANNOTATION;
	}

	@Override
	protected String getEntityAnnotationShort() {
		return RPC_ENTITY_ANNOTATION_SHORT;
	}

	@Override
	protected ImageComposite getImageComposite() {
		return rpcComposite;
	}

	@Override
	protected IFile getSourceFile(IPath path) {
		String className = path.lastSegment().replace("." + path.getFileExtension(), "");
		StringBuilder builder = new StringBuilder(path.removeFileExtension().toOSString());
		builder.append("-resources");
		builder.append(File.separator);
		builder.append(className + ".src");

		return getFile(builder.toString());
	}

	@Override
	protected void showPreviewImage(IJavaElement javaInput, IFile sourceFile) {
		if (sourceFile != null && sourceFile.exists()) {
			try {
				String source = IOUtils.toString(sourceFile.getContents());
				rpcComposite.setSource(source);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CoreException e) {
				e.printStackTrace();
			}
			rpcComposite.setVisible(true);
			return;
		}
		// hide image
		rpcComposite.setVisible(false);
	}
}
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
package org.openlegacy.ide.eclipse.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IPackageFragmentRoot;

import java.io.File;

public class PathsUtil {

	public static String packageToPath(String packageDir) {
		return packageDir.replaceAll("\\.", "/");
	}

	public static File toProjectOsLocation(IResource resource) {
		IPath workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		return new File(workspacePath.toFile(), resource.getFullPath().toOSString());
	}

	public static File toOsLocation(IResource resource) {

		return new File(resource.getLocation().toOSString());
	}

	public static File toSourceDirectory(IPackageFragmentRoot sourceFolder) {
		IProject project = sourceFolder.getJavaProject().getProject();
		return new File(project.getLocation().toOSString(), sourceFolder.getPath().toOSString());
	}

	public static File toOsLocation(IPath directory) {
		return new File(directory.toOSString());
	}

}

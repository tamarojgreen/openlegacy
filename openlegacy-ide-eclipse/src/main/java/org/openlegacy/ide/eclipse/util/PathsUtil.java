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
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.openlegacy.utils.OsUtils;

import java.io.File;

public class PathsUtil {

	public static String packageToPath(String packageDir) {
		return packageDir.replaceAll("\\.", "/"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static File toProjectOsLocation(IResource resource) {
		return new File(toOSString(resource.getProject().getLocation()));
	}

	public static File toOsLocation(IResource resource) {
		String osString = toOSString(resource.getLocation());
		return new File(osString);
	}

	private static String toOSString(IPath path) {
		String osString = path.toOSString();
		if (OsUtils.isUnix() && !osString.startsWith("/")) {
			// workaround for OSString not return / at start of paths
			osString = "/" + osString;
		}
		return osString;
	}

	public static File toSourceDirectory(IPackageFragmentRoot sourceFolder) {
		IProject project = sourceFolder.getJavaProject().getProject();
		return new File(toOSString(project.getLocation()), toOSString(sourceFolder.getPath()));
	}

	public static File toOsLocation(IPath directory) {
		return new File(toOSString(directory));
	}

}

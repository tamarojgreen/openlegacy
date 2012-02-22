package org.openlegacy.ide.eclipse.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

import java.io.File;

public class JavaUtils {

	public static String convertSourceFolderToString(IPackageFragmentRoot sourceFolder) {

		String projectName = sourceFolder.getJavaProject().getProject().getName();

		return projectName + File.separator + sourceFolder.getResource().getProjectRelativePath();
	}

	public static IJavaProject getJavaProjectFromIProject(IProject iProject) {
		return JavaCore.create(iProject);
	}
}

package com.openlegacy.enterprise.ide.eclipse.editors.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class Utils {

	public static String getFieldNameWithoutPrefix(String fieldName) {
		String[] split = fieldName.split("\\.");//$NON-NLS-1$
		return new String(split[split.length - 1]);
	}

	/**
	 * Tries to retrieve class instance from workspace projects
	 */
	public static Class<?> getClazz(String fullyQualifiedName) throws MalformedURLException, CoreException {
		List<IJavaProject> javaProjects = new ArrayList<IJavaProject>();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			if (project.isOpen()) {
				IJavaProject javaProject = JavaCore.create(project);
				javaProjects.add(javaProject);
			}
		}
		List<URLClassLoader> loaders = new ArrayList<URLClassLoader>();
		for (IJavaProject project : javaProjects) {
			loaders.add(getProjectClassLoader(project));
		}
		for (URLClassLoader urlClassLoader : loaders) {
			try {
				Class<?> clazz = urlClassLoader.loadClass(fullyQualifiedName);
				return clazz;
			} catch (ClassNotFoundException e) {
			}
		}
		return null;
	}

	/**
	 * Creates URLClassLoader for given project
	 */
	private static URLClassLoader getProjectClassLoader(IJavaProject project) throws CoreException, MalformedURLException {
		String[] classPathEntries = JavaRuntime.computeDefaultRuntimeClassPath(project);
		List<URL> urlList = new ArrayList<URL>();
		for (String entry : classPathEntries) {
			IPath path = new Path(entry);
			URL url = path.toFile().toURI().toURL();
			urlList.add(url);
		}
		ClassLoader parentClassLoader = project.getClass().getClassLoader();
		URL[] urls = urlList.toArray(new URL[urlList.size()]);
		URLClassLoader classLoader = new URLClassLoader(urls, parentClassLoader);
		return classLoader;
	}

}

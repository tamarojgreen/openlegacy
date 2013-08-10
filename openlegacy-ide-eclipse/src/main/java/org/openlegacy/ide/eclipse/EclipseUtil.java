package org.openlegacy.ide.eclipse;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import java.util.ArrayList;
import java.util.List;

public class EclipseUtil {

	public static String[] getProjectNames() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = root.getProjects();
		List<String> projectNames = new ArrayList<String>();
		for (IProject project : projects) {
			if (project.isOpen()) {
				projectNames.add(project.getName());
			}
		}
		return projectNames.toArray(new String[projectNames.size()]);
	}

	public static IProject getProject(String projectName) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.getProject(projectName);
	}
}

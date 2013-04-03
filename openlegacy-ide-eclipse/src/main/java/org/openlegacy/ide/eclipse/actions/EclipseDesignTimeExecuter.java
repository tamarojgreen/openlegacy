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
package org.openlegacy.ide.eclipse.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.GlobalBuildAction;
import org.openlegacy.designtime.EntityUserInteraction;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.designtime.mains.DesignTimeExecuterImpl;
import org.openlegacy.designtime.mains.GenerateControllerRequest;
import org.openlegacy.designtime.mains.GenerateModelRequest;
import org.openlegacy.designtime.mains.GenerateViewRequest;
import org.openlegacy.designtime.mains.ProjectCreationRequest;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.util.PathsUtil;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.io.File;
import java.io.IOException;

public class EclipseDesignTimeExecuter {

	private static final EclipseDesignTimeExecuter instance = new EclipseDesignTimeExecuter();

	private DesignTimeExecuter designTimeExecuter = new DesignTimeExecuterImpl();

	private final static Logger logger = Logger.getLogger(EclipseDesignTimeExecuter.class);

	public static EclipseDesignTimeExecuter instance() {
		return instance;
	}

	public void createProject(ProjectCreationRequest projectCreationRequest) throws IOException {

		designTimeExecuter.createProject(projectCreationRequest);

		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				try {
					ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e) {
					// OK
				}
			}
		});
	}

	public void generateModel(final IFile trailFile, IPackageFragmentRoot sourceDirectory, String packageDir,
			EntityUserInteraction<ScreenEntityDefinition> entityUserInteraction, boolean generateAspect,
			TerminalSnapshot... terminalSnapshots) throws GenerationException {
		File projectDirectory = PathsUtil.toOsLocation(trailFile.getProject());
		File templatesDirectory = new File(projectDirectory, DesignTimeExecuterImpl.TEMPLATES_DIR);

		GenerateModelRequest generateScreenRequest = new GenerateModelRequest();
		generateScreenRequest.setPackageDirectory(PathsUtil.packageToPath(packageDir));
		generateScreenRequest.setProjectPath(projectDirectory);
		generateScreenRequest.setSourceDirectory(PathsUtil.toSourceDirectory(sourceDirectory));
		generateScreenRequest.setTemplatesDirectory(templatesDirectory);
		generateScreenRequest.setTrailFile(PathsUtil.toOsLocation(trailFile));
		generateScreenRequest.setTerminalSnapshots(terminalSnapshots);
		generateScreenRequest.setGenerateAspectJ(generateAspect);
		generateScreenRequest.setEntityUserInteraction(entityUserInteraction);

		designTimeExecuter.generateModel(generateScreenRequest);

		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				(new GlobalBuildAction(Activator.getActiveWorkbenchWindow(), IncrementalProjectBuilder.INCREMENTAL_BUILD)).run();
			}
		});

	}

	public void generateAspect(IResource resource) {
		File javaFile = new File(resource.getLocation().toOSString());
		designTimeExecuter.generateAspect(javaFile);
	}

	public void initialize() {
		initialize(null);
	}

	public void initialize(final File projectPath) {
		Job job = new Job(Messages.message_initializing_ol_analyzer) {

			@Override
			protected IStatus run(IProgressMonitor arg0) {
				designTimeExecuter.initialize(projectPath);
				return Status.OK_STATUS;
			}

		};
		job.schedule();
	}

	public void generateView(IFile screenEntitySourceFile, UserInteraction userInteraction, boolean generateHelp,
			boolean generateMobilePage) {

		File projectPath = new File(PathsUtil.toOsLocation(screenEntitySourceFile.getProject()),
				DesignTimeExecuterImpl.TEMPLATES_DIR);

		GenerateViewRequest generatePageRequest = new GenerateViewRequest();
		generatePageRequest.setProjectPath(PathsUtil.toOsLocation(screenEntitySourceFile.getProject()));
		File sourceFile = PathsUtil.toOsLocation(screenEntitySourceFile);
		generatePageRequest.setScreenEntitySourceFile(sourceFile);
		generatePageRequest.setPackageDirectory(PathsUtil.packageToPath(sourceFile.getParent()));
		File sourceDirectory = sourceFile.getParentFile();
		while (!sourceDirectory.getName().equals("java")) {
			sourceDirectory = sourceDirectory.getParentFile();
		}
		generatePageRequest.setSourceDirectory(sourceDirectory);
		generatePageRequest.setTemplatesDirectory(projectPath);
		generatePageRequest.setUserInteraction(userInteraction);
		generatePageRequest.setGenerateHelp(generateHelp);
		generatePageRequest.setGenerateMobilePage(generateMobilePage);
		designTimeExecuter.generateView(generatePageRequest);

	}

	public void generateController(IFile screenEntitySourceFile, IPackageFragmentRoot sourceDirectory, String packageDir,
			UserInteraction userInteraction) {

		File projectPath = new File(PathsUtil.toOsLocation(screenEntitySourceFile.getProject()),
				DesignTimeExecuterImpl.TEMPLATES_DIR);

		GenerateControllerRequest generatePageRequest = new GenerateControllerRequest();
		generatePageRequest.setProjectPath(PathsUtil.toOsLocation(screenEntitySourceFile.getProject()));
		generatePageRequest.setScreenEntitySourceFile(PathsUtil.toOsLocation(screenEntitySourceFile));
		generatePageRequest.setSourceDirectory(PathsUtil.toSourceDirectory(sourceDirectory));
		generatePageRequest.setPackageDirectory(PathsUtil.packageToPath(packageDir));
		generatePageRequest.setTemplatesDirectory(projectPath);
		generatePageRequest.setUserInteraction(userInteraction);
		designTimeExecuter.generateController(generatePageRequest);

	}

	public void copyCodeGenerationTemplates(IProject project) {
		File projectPath = PathsUtil.toOsLocation(project);
		designTimeExecuter.copyCodeGenerationTemplates(projectPath);
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			logger.fatal(e);
		}
	}

	public String getPreference(IProject project, String key) {
		return designTimeExecuter.getPreferences(PathsUtil.toProjectOsLocation(project), key);

	}

	public void savePreference(IProject project, String key, Boolean value) {
		if (value) {
			designTimeExecuter.savePreference(PathsUtil.toProjectOsLocation(project), key, "1");
		} else {
			designTimeExecuter.savePreference(PathsUtil.toProjectOsLocation(project), key, "0");
		}
	}

	public void savePreference(IProject project, String key, String value) {
		designTimeExecuter.savePreference(PathsUtil.toProjectOsLocation(project), key, value);
	}

	public void copyDesigntimeContext(IProject project) {
		File projectPath = PathsUtil.toOsLocation(project);
		designTimeExecuter.copyDesigntimeContext(projectPath);
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			logger.fatal(e);
		}
	}
}

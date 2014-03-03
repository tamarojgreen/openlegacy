/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
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
import org.openlegacy.EntityDefinition;
import org.openlegacy.designtime.EntityUserInteraction;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.designtime.mains.DesignTimeExecuterImpl;
import org.openlegacy.designtime.mains.GenerateControllerRequest;
import org.openlegacy.designtime.mains.GenerateServiceRequest;
import org.openlegacy.designtime.mains.GenerateServiceRequest.ServiceType;
import org.openlegacy.designtime.mains.GenerateViewRequest;
import org.openlegacy.designtime.mains.ProjectCreationRequest;
import org.openlegacy.designtime.mains.ServiceEntityParameter;
import org.openlegacy.designtime.rpc.GenerateRpcModelRequest;
import org.openlegacy.designtime.rpc.ImportSourceRequest;
import org.openlegacy.designtime.terminal.GenerateScreenModelRequest;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.util.PathsUtil;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.utils.FileUtils;
import org.openlegacy.utils.StringUtil;

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

	public void generateScreenModel(final IFile trailFile, IPackageFragmentRoot sourceDirectory, String packageDir,
			EntityUserInteraction<EntityDefinition<?>> entityUserInteraction, boolean generateAspect, boolean generateTest,
			TerminalSnapshot... terminalSnapshots) throws GenerationException {
		File projectDirectory = PathsUtil.toOsLocation(trailFile.getProject());
		File templatesDirectory = new File(projectDirectory, DesignTimeExecuterImpl.TEMPLATES_DIR);

		GenerateScreenModelRequest generateScreenRequest = new GenerateScreenModelRequest();
		generateScreenRequest.setPackageDirectory(PathsUtil.packageToPath(packageDir));
		generateScreenRequest.setProjectPath(projectDirectory);
		generateScreenRequest.setSourceDirectory(PathsUtil.toSourceDirectory(sourceDirectory));
		generateScreenRequest.setTemplatesDirectory(templatesDirectory);
		generateScreenRequest.setTrailFile(PathsUtil.toOsLocation(trailFile));
		generateScreenRequest.setTerminalSnapshots(terminalSnapshots);
		generateScreenRequest.setGenerateAspectJ(generateAspect);
		generateScreenRequest.setGenerateTest(generateTest);
		generateScreenRequest.setEntityUserInteraction(entityUserInteraction);

		designTimeExecuter.generateScreenModel(generateScreenRequest);

		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				(new GlobalBuildAction(Activator.getActiveWorkbenchWindow(), IncrementalProjectBuilder.INCREMENTAL_BUILD)).run();
			}
		});

	}

	public void generateScreenEntityDefinition(final IFile trailFile, IPackageFragmentRoot sourceDirectory, String packageDir,
			EntityUserInteraction<EntityDefinition<?>> entityUserInteraction, boolean generateAspect,
			ScreenEntityDefinition entityDefinition) throws GenerationException {
		File projectDirectory = PathsUtil.toOsLocation(trailFile.getProject());
		File templatesDirectory = new File(projectDirectory, DesignTimeExecuterImpl.TEMPLATES_DIR);

		GenerateScreenModelRequest generateScreenRequest = new GenerateScreenModelRequest();
		generateScreenRequest.setPackageDirectory(PathsUtil.packageToPath(packageDir));
		generateScreenRequest.setProjectPath(projectDirectory);
		generateScreenRequest.setSourceDirectory(PathsUtil.toSourceDirectory(sourceDirectory));
		generateScreenRequest.setTemplatesDirectory(templatesDirectory);
		generateScreenRequest.setTrailFile(PathsUtil.toOsLocation(trailFile));
		generateScreenRequest.setTerminalSnapshots(entityDefinition.getSnapshot());
		generateScreenRequest.setGenerateAspectJ(generateAspect);
		generateScreenRequest.setEntityUserInteraction(entityUserInteraction);

		designTimeExecuter.generateScreenEntityDefinition(generateScreenRequest, entityDefinition);

		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				(new GlobalBuildAction(Activator.getActiveWorkbenchWindow(), IncrementalProjectBuilder.INCREMENTAL_BUILD)).run();
			}
		});

	}

	public boolean generateAspect(IResource resource) {
		File javaFile = new File(resource.getLocation().toOSString());
		return designTimeExecuter.generateAspect(javaFile);
	}

	public void initialize() {
		initialize(null);
	}

	public void initialize(final File projectPath) {
		Job job = new Job(Messages.getString("message_initializing_ol_analyzer")) {

			@Override
			protected IStatus run(IProgressMonitor arg0) {
				designTimeExecuter.initialize(projectPath);
				return Status.OK_STATUS;
			}

		};
		job.schedule();
	}

	public void generateView(IFile entitySourceFile, IProject project, UserInteraction userInteraction, boolean generateHelp,
			boolean generateMobilePage) {

		File projectPath = new File(PathsUtil.toOsLocation(project), DesignTimeExecuterImpl.TEMPLATES_DIR);

		GenerateViewRequest generatePageRequest = new GenerateViewRequest();
		generatePageRequest.setProjectPath(PathsUtil.toOsLocation(project));
		File sourceFile = PathsUtil.toOsLocation(entitySourceFile);
		generatePageRequest.setEntitySourceFile(sourceFile);
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

	public void generateController(IFile entitySourceFile, IPackageFragmentRoot sourceDirectory, String packageDir,
			UserInteraction userInteraction) {

		File projectPath = new File(PathsUtil.toOsLocation(entitySourceFile.getProject()), DesignTimeExecuterImpl.TEMPLATES_DIR);

		GenerateControllerRequest generateControllerRequest = new GenerateControllerRequest();
		generateControllerRequest.setProjectPath(PathsUtil.toOsLocation(entitySourceFile.getProject()));
		generateControllerRequest.setEntitySourceFile(PathsUtil.toOsLocation(entitySourceFile));
		generateControllerRequest.setSourceDirectory(PathsUtil.toSourceDirectory(sourceDirectory));
		if (!StringUtil.isEmpty(packageDir)) {
			generateControllerRequest.setPackageDirectory(PathsUtil.packageToPath(packageDir));
		}
		generateControllerRequest.setTemplatesDirectory(projectPath);
		generateControllerRequest.setUserInteraction(userInteraction);
		designTimeExecuter.generateController(generateControllerRequest);

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

	public void reloadPreference(IProject project) {
		designTimeExecuter.reloadPreferences(PathsUtil.toProjectOsLocation(project));
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

	public void generateRpcModel(final IFile sourceFile, IPackageFragmentRoot sourceDirectory, String packageDir,
			EntityUserInteraction<EntityDefinition<?>> entityUserInteraction, boolean generateAspect) throws GenerationException {
		File projectDirectory = PathsUtil.toOsLocation(sourceFile.getProject());
		File templatesDirectory = new File(projectDirectory, DesignTimeExecuterImpl.TEMPLATES_DIR);

		GenerateRpcModelRequest generateRpcRequest = new GenerateRpcModelRequest();
		generateRpcRequest.setPackageDirectory(PathsUtil.packageToPath(packageDir));
		generateRpcRequest.setProjectPath(projectDirectory);
		generateRpcRequest.setSourceDirectory(PathsUtil.toSourceDirectory(sourceDirectory));
		generateRpcRequest.setTemplatesDirectory(templatesDirectory);
		generateRpcRequest.setSourceFile(PathsUtil.toOsLocation(sourceFile));
		generateRpcRequest.setGenerateAspectJ(generateAspect);
		generateRpcRequest.setEntityUserInteraction(entityUserInteraction);

		designTimeExecuter.generateRpcModel(generateRpcRequest);

		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				(new GlobalBuildAction(Activator.getActiveWorkbenchWindow(), IncrementalProjectBuilder.INCREMENTAL_BUILD)).run();
			}
		});
	}

	public boolean isSupportControllerGeneration(IFile entityFile) {
		return designTimeExecuter.isSupportControllerGeneration(PathsUtil.toOsLocation(entityFile));
	}

	public String importFile(String workingDirPath, String host, String user, String pwd, String legacyFile,
			UserInteraction userInteraction) throws OpenLegacyException {
		ImportSourceRequest importSourceRequest = new ImportSourceRequest();
		importSourceRequest.setWorkingDirPath(workingDirPath);
		importSourceRequest.setHost(host);
		importSourceRequest.setUser(user);
		importSourceRequest.setPwd(pwd);
		importSourceRequest.setLegacyFile(legacyFile);
		importSourceRequest.setUserInteraction(userInteraction);
		designTimeExecuter.importSourceFile(importSourceRequest);

		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				(new GlobalBuildAction(Activator.getActiveWorkbenchWindow(), IncrementalProjectBuilder.INCREMENTAL_BUILD)).run();
			}
		});
		return importSourceRequest.getNewFileName();

	}

	public boolean isSupportServiceGeneration(IProject project) {
		return designTimeExecuter.isSupportServiceGeneration(PathsUtil.toOsLocation(project));
	}

	public void generateServiceFromEntity(IFile entityJavaFile, IPackageFragmentRoot sourceDirectory, String packageDir,
			UserInteraction userInteraction, boolean generateTest) {
		IProject project = entityJavaFile.getProject();
		File projectPath = new File(PathsUtil.toOsLocation(project), DesignTimeExecuterImpl.TEMPLATES_DIR);

		GenerateServiceRequest generateServiceRequest = new GenerateServiceRequest();
		EntityDefinition<?> entityDefinition = designTimeExecuter.initEntityDefinition(PathsUtil.toOsLocation(entityJavaFile));
		generateServiceRequest.setServiceType(entityDefinition instanceof ScreenEntityDefinition ? ServiceType.SCREEN
				: ServiceType.RPC);
		generateServiceRequest.getOutputParameters().add(new ServiceEntityParameter(entityDefinition));
		generateServiceRequest.setProjectPath(PathsUtil.toOsLocation(project));
		generateServiceRequest.setServiceName(FileUtils.fileWithoutExtension(entityJavaFile.getName()));
		generateServiceRequest.setSourceDirectory(PathsUtil.toSourceDirectory(sourceDirectory));
		generateServiceRequest.setPackageDirectory(PathsUtil.packageToPath(packageDir));
		generateServiceRequest.setTemplatesDirectory(projectPath);
		generateServiceRequest.setUserInteraction(userInteraction);
		generateServiceRequest.setGenerateTest(generateTest);
		designTimeExecuter.generateService(generateServiceRequest);

	}
}

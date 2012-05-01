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
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.designtime.mains.DesignTimeExecuterImpl;
import org.openlegacy.designtime.mains.GeneratePageRequest;
import org.openlegacy.designtime.mains.OverrideConfirmer;
import org.openlegacy.designtime.mains.ProjectCreationRequest;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.ide.eclipse.util.PathsUtil;

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

	public void generateScreens(final IFile trailFile, IPackageFragmentRoot sourceDirectory, String packageDir,
			OverrideConfirmer overrideConfirmer) throws GenerationException {
		File anaylzerContextFile = new File(trailFile.getProject().getLocation().toOSString(),
				DesignTimeExecuter.ANALYZER_DEFAULT_PATH);
		File projectPath = new File(PathsUtil.toOsLocation(trailFile.getProject()), DesignTimeExecuterImpl.TEMPLATES_DIR);

		designTimeExecuter.generateScreens(PathsUtil.toOsLocation(trailFile), PathsUtil.toSourceDirectory(sourceDirectory),
				PathsUtil.packageToPath(packageDir), projectPath, overrideConfirmer, anaylzerContextFile);

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

	public void initialize(final File analyzerFile) {
		Job job = new Job("Initializing OpenLegacy analyzer") {

			@Override
			protected IStatus run(IProgressMonitor arg0) {
				designTimeExecuter.initialize(analyzerFile);
				return Status.OK_STATUS;
			}

		};
		job.schedule();
	}

	public void createWebPage(IFile screenEntitySourceFile, IPackageFragmentRoot sourceDirectory, String packageDir,
			OverrideConfirmer overrideConfirmer, boolean generateHelp) {

		File projectPath = new File(PathsUtil.toOsLocation(screenEntitySourceFile.getProject()),
				DesignTimeExecuterImpl.TEMPLATES_DIR);

		GeneratePageRequest generatePageRequest = new GeneratePageRequest();
		generatePageRequest.setProjectDir(PathsUtil.toOsLocation(screenEntitySourceFile.getProject()));
		generatePageRequest.setScreenEntitySourceFile(PathsUtil.toOsLocation(screenEntitySourceFile));
		generatePageRequest.setSourceDirectory(PathsUtil.toSourceDirectory(sourceDirectory));
		generatePageRequest.setPackageDirectoryName(PathsUtil.packageToPath(packageDir));
		generatePageRequest.setTemplatesDir(projectPath);
		generatePageRequest.setOverrideConfirmer(overrideConfirmer);
		generatePageRequest.setGenerateHelp(generateHelp);
		designTimeExecuter.createWebPage(generatePageRequest);

	}

	public void copyTemplates(IProject project) {
		File projectPath = PathsUtil.toOsLocation(project);
		designTimeExecuter.createCustomTemplatesDir(projectPath);
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			logger.fatal(e);
		}

	}
}

package org.openlegacy.ide.eclipse.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.GlobalBuildAction;
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.designtime.mains.DesignTimeExecuterImpl;
import org.openlegacy.designtime.mains.OverrideConfirmer;
import org.openlegacy.exceptions.UnableToGenerateSnapshotException;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.ide.eclipse.util.PathsUtil;

import java.io.File;
import java.io.IOException;

public class EclipseDesignTimeExecuter {

	private static final EclipseDesignTimeExecuter instance = new EclipseDesignTimeExecuter();

	private DesignTimeExecuter designTimeExecuter = new DesignTimeExecuterImpl();

	public static EclipseDesignTimeExecuter instance() {
		return instance;
	}

	public void createProject(String templateName, IPath workspacePath, String projectName, String provider,
			String defaultPackageName) throws IOException {
		designTimeExecuter.createProject(templateName, PathsUtil.toOsLocation(workspacePath), projectName, provider,
				defaultPackageName);
	}

	public void generateScreens(final IFile trailFile, IPackageFragmentRoot sourceDirectory, String packageDir,
			OverrideConfirmer overrideConfirmer) throws UnableToGenerateSnapshotException {
		designTimeExecuter.generateScreens(PathsUtil.toOsLocation(trailFile), PathsUtil.toSourceDirectory(sourceDirectory),
				PathsUtil.packageToPath(packageDir), overrideConfirmer);

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
		Job job = new Job("Initializing analyzer") {

			@Override
			protected IStatus run(IProgressMonitor arg0) {
				designTimeExecuter.initialize();
				return Status.OK_STATUS;
			}

		};
		job.schedule();
	}
}

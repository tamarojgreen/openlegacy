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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.openlegacy.designtime.PreferencesConstants;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.util.JavaUtils;

import java.io.File;
import java.text.MessageFormat;

public class GenerateControllerDialog extends AbstractGenerateCodeDialog {

	private final static Logger logger = Logger.getLogger(GenerateControllerDialog.class);
	private ISelection selection;

	protected GenerateControllerDialog(Shell shell, ISelection selection) {
		super(shell, (IFile)((ICompilationUnit)((TreeSelection)selection).getFirstElement()).getResource());
		this.selection = selection;
	}

	@Override
	protected void executeGenerate() {

		Job job = new Job(Messages.getString("job_generating_controller")) {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {

				final TreePath[] pathElements = ((TreeSelection)selection).getPaths();

				monitor.beginTask(Messages.getString("task_generating"), pathElements.length + 1);

				for (TreePath treePath : pathElements) {
					if (treePath.getLastSegment() instanceof ICompilationUnit) {
						final IFile screenEntitySourceFile = (IFile)((ICompilationUnit)treePath.getLastSegment()).getResource();

						EclipseDesignTimeExecuter.instance().generateController(screenEntitySourceFile, getSourceFolder(),
								getPackageValue(), GenerateControllerDialog.this);

						monitor.worked(1);

					} else {
						logger.warn(MessageFormat.format(Messages.getString("warn_java_source_not_valid_selection"),
								treePath.getLastSegment()));
					}
				}

				Display.getDefault().syncExec(new Runnable() {

					public void run() {
						try {
							monitor.worked(1);
							ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);
							monitor.done();
						} catch (CoreException e) {
							logger.fatal(e);
						}
					}
				});

				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	@Override
	protected void createDialogSpecific(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL, true, true);
		gd.horizontalSpan = 3;
		composite.setLayoutData(gd);
		composite.setLayout(gridLayout);

	}

	@Override
	protected void loadPrefrences() {

		EclipseDesignTimeExecuter designtimeExecuter = EclipseDesignTimeExecuter.instance();
		IProject project = getProject();

		String prefrenceSourceFolderPath = designtimeExecuter.getPreference(project, PreferencesConstants.API_SOURCE_FOLDER);
		getSourceFolderPathText().setText(
				MessageFormat.format("{0}{1}{2}", project.getName(), File.separator, prefrenceSourceFolderPath)); //$NON-NLS-1$

		IJavaProject javaProject = JavaUtils.getJavaProjectFromIProject(project);
		setSourceFolder(javaProject.getPackageFragmentRoot(prefrenceSourceFolderPath));

		String prefrencePackage = designtimeExecuter.getPreference(project, PreferencesConstants.WEB_PACKAGE);
		if (prefrencePackage != null) {
			getPackageText().setText(prefrencePackage);
		}
	}

	@Override
	protected void savePreferences() {
		String sourceFolderOnly = getSourceFolderPathText().getText().substring(
				getSourceFolder().getJavaProject().getProject().getName().length() + 1);

		EclipseDesignTimeExecuter designtimeExecuter = EclipseDesignTimeExecuter.instance();

		designtimeExecuter.savePreference(getProject(), PreferencesConstants.API_SOURCE_FOLDER, sourceFolderOnly);
		designtimeExecuter.savePreference(getProject(), PreferencesConstants.WEB_PACKAGE, getPackageText().getText());
	}
}

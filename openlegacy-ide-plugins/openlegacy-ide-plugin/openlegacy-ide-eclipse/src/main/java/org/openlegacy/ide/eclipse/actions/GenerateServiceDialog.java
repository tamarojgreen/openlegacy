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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.openlegacy.designtime.PreferencesConstants;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.util.JavaUtils;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class GenerateServiceDialog extends AbstractGenerateCodeDialog {

	private final static Logger logger = Logger.getLogger(GenerateServiceDialog.class);
	private ISelection selection;
	private boolean generatePool;

	protected GenerateServiceDialog(Shell shell, ISelection selection) {
		super(shell, (IFile)((ICompilationUnit)((TreeSelection)selection).getFirstElement()).getResource());
		this.selection = selection;
	}

	@Override
	protected void executeGenerate() {

		Job job = new Job(Messages.getString("job_generating_service")) {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {

				final TreePath[] pathElements = ((TreeSelection)selection).getPaths();

				monitor.beginTask(Messages.getString("task_generating"), pathElements.length + 1);

				List<IFile> javaFiles = new ArrayList<IFile>();
				for (TreePath treePath : pathElements) {
					if (treePath.getLastSegment() instanceof ICompilationUnit) {
						final IFile screenEntitySourceFile = (IFile)((ICompilationUnit)treePath.getLastSegment()).getResource();
						javaFiles.add(screenEntitySourceFile);
						monitor.worked(1);

					} else {
						logger.warn(MessageFormat.format(Messages.getString("warn_java_source_not_valid_selection"),
								treePath.getLastSegment()));
					}
				}
				if (javaFiles.size() == 0) {
					return Status.CANCEL_STATUS;
				}
				EclipseDesignTimeExecuter.instance().generateServiceFromEntity(javaFiles.get(0), getSourceFolder(),
						getPackageValue(), GenerateServiceDialog.this, isGenerateTest(), isGeneratePool());

				Display.getDefault().syncExec(new Runnable() {

					@Override
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
		Button generatePool = new Button(parent, SWT.CHECK);
		generatePool.setText(Messages.getString("label_generate_pool"));

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 400;
		generatePool.setLayoutData(gd);

		generatePool.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				setGeneratePool(true);
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setGeneratePool(!isGeneratePool());
			}

		});

		Composite composite = new Composite(parent, SWT.NONE);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gd = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL, true, true);
		gd.horizontalSpan = 3;
		composite.setLayoutData(gd);
		composite.setLayout(gridLayout);

	}

	public boolean isGeneratePool() {
		return generatePool;
	}

	private void setGeneratePool(boolean b) {
		generatePool = b;
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

		String prefrencePackage = designtimeExecuter.getPreference(project, PreferencesConstants.SERVICE_PACKAGE);
		if (prefrencePackage == null) {
			prefrencePackage = designtimeExecuter.getPreference(project, PreferencesConstants.API_PACKAGE) + ".services";
		}
		getPackageText().setText(prefrencePackage);
	}

	@Override
	protected void savePreferences() {
		String sourceFolderOnly = getSourceFolderPathText().getText().substring(
				getSourceFolder().getJavaProject().getProject().getName().length() + 1);

		EclipseDesignTimeExecuter designtimeExecuter = EclipseDesignTimeExecuter.instance();

		designtimeExecuter.savePreference(getProject(), PreferencesConstants.API_SOURCE_FOLDER, sourceFolderOnly);
		designtimeExecuter.savePreference(getProject(), PreferencesConstants.SERVICE_PACKAGE, getPackageText().getText());
	}

	@Override
	protected boolean isSupportAjGeneration() {
		return false;
	}
}

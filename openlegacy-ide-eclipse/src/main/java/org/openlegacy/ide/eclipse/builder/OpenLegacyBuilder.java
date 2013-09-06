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
package org.openlegacy.ide.eclipse.builder;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.openlegacy.designtime.PreferencesConstants;
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.PluginConstants;
import org.openlegacy.ide.eclipse.actions.EclipseDesignTimeExecuter;
import org.openlegacy.ide.eclipse.actions.screen.GenerateScreenModelDialog;
import org.openlegacy.ide.eclipse.ui.preferences.PreferenceConstants;
import org.openlegacy.ide.eclipse.util.EclipseUtils;
import org.openlegacy.ide.eclipse.util.PathsUtil;
import org.openlegacy.ide.eclipse.util.PopupUtil;
import org.openlegacy.utils.FileUtils;

import java.util.Map;

public class OpenLegacyBuilder extends IncrementalProjectBuilder {

	private final static Logger logger = Logger.getLogger(OpenLegacyBuilder.class);

	class OpenLegacyDeltaVisitor implements IResourceDeltaVisitor {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
				case IResourceDelta.ADDED:
					checkAspectGenerate(resource);
					checkNewTrail(resource);
					break;
				case IResourceDelta.REMOVED:
					String fileName = resource.getName();
					if (resource instanceof IFile && fileName.endsWith(PluginConstants.JAVA_EXTENSION)) {

						String fileNoExtension = FileUtils.fileWithoutExtension(fileName);
						IResource[] members = resource.getParent().members();
						for (IResource iResource : members) {
							String resourceName = iResource.getName();
							if ((resourceName.startsWith(fileNoExtension) && resourceName.endsWith(DesignTimeExecuter.ASPECT_SUFFIX))
									|| resourceName.endsWith(fileNoExtension + DesignTimeExecuter.RESOURCES_FOLDER_SUFFIX)) {
								iResource.delete(false, null);
							}
						}
						try {
							getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
						} catch (CoreException e) {
							logger.fatal(e);
						}

					}
					break;
				case IResourceDelta.CHANGED:
					// when we renamed inner class (e.g. ItemDetails2StockInfo) we need to delete appropriate Aspects
					String name = resource.getName();
					if (resource instanceof IFile && name.endsWith(PluginConstants.JAVA_EXTENSION)) {

						String fileNoExtension = FileUtils.fileWithoutExtension(name);
						IResource[] members = resource.getParent().members();
						for (IResource iResource : members) {
							String resourceName = iResource.getName();
							if (resourceName.startsWith(fileNoExtension)
									&& resourceName.endsWith(DesignTimeExecuter.ASPECT_SUFFIX)) {
								iResource.delete(false, null);
							}
						}
						try {
							getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
						} catch (CoreException e) {
							logger.fatal(e);
						}
					}
					checkAspectGenerate(resource);
					checkAnalyzerContextChange(resource);
					break;
			}
			// return true to continue visiting children.
			return true;
		}

		private void checkNewTrail(final IResource resource) {

			if (!Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_ANALYZE_NEW_TRAILS)) {
				return;
			}

			if (!resource.getName().endsWith(".trail")) { //$NON-NLS-1$
				return;
			}
			// ignore demo session
			if (resource.getName().startsWith("demo")) { //$NON-NLS-1$
				logger.debug("Ignoring demo session trail from automatic analysis");
				return;
			}

			final IFile trailFile = (IFile)resource;
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {

					if (EclipseUtils.isEditorOpen(trailFile)) {
						return;
					}

					boolean result = PopupUtil.question(Messages.getString("message_new_trail_found"));
					if (result) {
						IEditorDescriptor editorDescriptor = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(
								trailFile.getName());
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						try {

							page.openEditor(new FileEditorInput(trailFile), editorDescriptor.getId());

							GenerateScreenModelDialog dialog = new GenerateScreenModelDialog(Display.getDefault().getShells()[0],
									trailFile, false);
							dialog.open();

						} catch (PartInitException e) {
							logger.fatal(e);
						}
					}
				}

			});
		}

	}

	class OpenLegacyResourceVisitor implements IResourceVisitor {

		public boolean visit(IResource resource) {
			checkAspectGenerate(resource);
			// return true to continue visiting children.
			return true;
		}
	}

	public static final String BUILDER_ID = "org.openlegacy.ide.eclipse.builder"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int, java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	void checkAspectGenerate(IResource resource) {
		String useAspect = EclipseDesignTimeExecuter.instance().getPreference(getProject(), PreferencesConstants.USE_AJ);
		boolean isUseAspect = useAspect == null || useAspect.equals("1");
		if (resource instanceof IFile && resource.getName().endsWith(PluginConstants.JAVA_EXTENSION) && !isIgnoreFolder(resource)
				&& isUseAspect) {
			EclipseDesignTimeExecuter.instance().generateAspect(resource);
			try {
				getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				logger.fatal(e);
			}

		}
	}

	private static boolean isIgnoreFolder(IResource resource) {
		String path = resource.getFullPath().toString();
		return path.contains("/target") || path.contains("test/java");
	}

	/**
	 * Refresh design-time context in case project designtime context file changed
	 * 
	 * @param resource
	 */
	private void checkAnalyzerContextChange(IResource resource) {
		if (resource instanceof IFile
				&& resource.getFullPath().toString().contains(DesignTimeExecuter.CUSTOM_DESIGNTIME_CONTEXT_RELATIVE_PATH)) {
			EclipseDesignTimeExecuter.instance().initialize(PathsUtil.toOsLocation(resource.getProject().getLocation()));
			try {
				getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				logger.fatal(e);
			}

		}

	}

	protected void fullBuild(final IProgressMonitor monitor) throws CoreException {
		try {
			getProject().accept(new OpenLegacyResourceVisitor());
		} catch (CoreException e) {
		}
	}

	protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		delta.accept(new OpenLegacyDeltaVisitor());
	}
}

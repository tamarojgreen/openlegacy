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
import org.openlegacy.ide.eclipse.PluginConstants;
import org.openlegacy.ide.eclipse.actions.EclipseDesignTimeExecuter;
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
					break;
				case IResourceDelta.REMOVED:
					String fileName = resource.getName();
					if (resource instanceof IFile && fileName.endsWith(PluginConstants.JAVA_EXTENSION)) {

						String fileNoExtension = FileUtils.fileWithoutExtenstion(fileName);
						IResource[] members = resource.getParent().members();
						for (IResource iResource : members) {
							String resourceName = iResource.getName();
							if (resourceName.startsWith(fileNoExtension)
									&& (resourceName.endsWith(PluginConstants.ASPECT_SUFFIX) || resourceName.endsWith(PluginConstants.RESOURCES_FOLDER_SUFFIX))) {
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
					checkAspectGenerate(resource);
					break;
			}
			// return true to continue visiting children.
			return true;
		}
	}

	class OpenLegacyResourceVisitor implements IResourceVisitor {

		public boolean visit(IResource resource) {
			checkAspectGenerate(resource);
			// return true to continue visiting children.
			return true;
		}
	}

	public static final String BUILDER_ID = "org.openlegacy.ide.eclipse.builder";

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
		if (resource instanceof IFile && resource.getName().endsWith(PluginConstants.JAVA_EXTENSION)) {
			EclipseDesignTimeExecuter.instance().generateAspect(resource);
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

package com.openlegacy.enterprise.ide.eclipse.ws.generator;

import com.openlegacy.enterprise.ide.eclipse.JpaEntityDescriber;
import com.openlegacy.enterprise.ide.eclipse.RpcEntityDescriber;
import com.openlegacy.enterprise.ide.eclipse.ScreenEntityDescriber;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenEntityModel;

import org.apache.commons.lang.CharEncoding;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.designtime.PreferencesConstants;
import org.openlegacy.designtime.db.generators.support.CodeBasedDbEntityDefinition;
import org.openlegacy.designtime.db.generators.support.DbCodeBasedDefinitionUtils;
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.designtime.mains.DesignTimeExecuterImpl;
import org.openlegacy.designtime.mains.GenerateServiceRequest.ServiceType;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcEntityDefinition;
import org.openlegacy.designtime.rpc.generators.support.RpcCodeBasedDefinitionUtils;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.designtime.terminal.generators.support.ScreenCodeBasedDefinitionUtils;
import org.openlegacy.ide.eclipse.util.PathsUtil;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class EntitiesFetcher {

	private static final String JAVA_EXTENTION = "java";
	private static final String XML_EXTENSION = ".xml";
	private static final String SRC_EXTENSION = ".src";

	private class PackageVisitor implements IResourceVisitor {

		private String apiPackage = null;
		private IProgressMonitor monitor;
		private ServiceType serviceType;

		public PackageVisitor(String apiPackage, IProgressMonitor monitor, ServiceType serviceType) {
			super();
			this.apiPackage = apiPackage;
			this.monitor = monitor;
			this.serviceType = serviceType;
		}

		@Override
		public boolean visit(IResource resource) throws CoreException {
			monitor.worked(1);
			if (checkResource(resource)) {
				File file = PathsUtil.toOsLocation(resource);
				try {
					FileReader fileReader = new FileReader(file);
					AbstractEntityModel entity = null;
					CompilationUnit compilationUnit = JavaParser.parse(file, CharEncoding.UTF_8);
					File packageDir = new File(file.getParentFile().getAbsolutePath());

					if (ServiceType.SCREEN.equals(serviceType) && ScreenEntityDescriber.doDescribe(fileReader) != null) {
						ScreenEntityDefinition definition = ScreenCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit,
								packageDir);
						if (definition != null) {
							IPath location = resource.getFullPath();
							IFile xmlFile = getXmlFile(location);
							entity = new ScreenEntityModel((CodeBasedScreenEntityDefinition)definition, xmlFile);
						}
					} else if (ServiceType.RPC.equals(serviceType) && RpcEntityDescriber.doDescribe(fileReader) != null) {
						RpcEntityDefinition definition = RpcCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit,
								packageDir);
						if (definition != null) {
							IPath location = resource.getFullPath();
							IFile srcFile = getSrcFile(location);
							entity = new RpcEntityModel((CodeBasedRpcEntityDefinition)definition, srcFile);
						}
					} else if (ServiceType.JDBC.equals(serviceType) && JpaEntityDescriber.doDescribe(fileReader) != null) {
						DbEntityDefinition definition = DbCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit,
								packageDir);
						if (definition != null) {
							entity = new JpaEntityModel((CodeBasedDbEntityDefinition)definition, null);
						}
					}
					if (entity != null) {
						entities.add(entity);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			return true;
		}

		private boolean checkResource(IResource resource) {
			if (JAVA_EXTENTION.equals(resource.getFileExtension())) {
				String path = resource.getFullPath().toString().replace("/", ".");
				if (path.contains(apiPackage)) {
					return true;
				}
			}
			return false;
		}

		private IFile getXmlFile(IPath path) {
			String className = path.lastSegment().replace("." + path.getFileExtension(), "");
			StringBuilder builder = new StringBuilder(path.removeFileExtension().toOSString());
			builder.append("-resources");
			builder.append(File.separator);
			builder.append(className + XML_EXTENSION);

			return getFile(builder.toString());
		}

		private IFile getSrcFile(IPath path) {
			String className = path.lastSegment().replace("." + path.getFileExtension(), "");
			StringBuilder builder = new StringBuilder(path.removeFileExtension().toOSString());
			builder.append("-resources");
			builder.append(File.separator);
			builder.append(className + SRC_EXTENSION);

			return getFile(builder.toString());
		}

		private IFile getFile(String path) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			return root.getFile(new Path(path));
		}

	}

	private DesignTimeExecuter designTimeExecuter = new DesignTimeExecuterImpl();

	private List<AbstractEntityModel> entities = new ArrayList<AbstractEntityModel>();

	public List<AbstractEntityModel> fetch(IProject project, IProgressMonitor monitor, ServiceType serviceType) {
		if (project != null) {
			File projectPath = PathsUtil.toOsLocation(project);
			String apiPackage = designTimeExecuter.getPreferences(projectPath, PreferencesConstants.API_PACKAGE);
			try {
				project.accept(new PackageVisitor(apiPackage, monitor, serviceType));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return entities;
	}

	public List<AbstractEntityModel> getEntities() {
		return entities;
	}
}

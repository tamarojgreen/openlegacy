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
package org.openlegacy.ide.eclipse.refactoring;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.ltk.core.refactoring.participants.RenameArguments;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.actions.EclipseDesignTimeExecuter;
import org.openlegacy.utils.FileUtils;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Ivan Bort
 * 
 */
public class OpenLegacyEntityRenameParticipant extends RenameParticipant {

	private final static Log logger = LogFactory.getLog(OpenLegacyEntityRenameParticipant.class);

	private ICompilationUnit compilationUnit;
	private String newName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#initialize(java.lang.Object)
	 */
	@Override
	protected boolean initialize(Object element) {
		this.compilationUnit = (ICompilationUnit)element;
		return true;
	}

	@Override
	public boolean initialize(RefactoringProcessor processor, Object element, RefactoringArguments arguments) {
		this.newName = FileUtils.fileWithoutExtension(((RenameArguments)arguments).getNewName());
		return super.initialize(processor, element, arguments);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#getName()
	 */
	@Override
	public String getName() {
		return this.compilationUnit.getElementName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#checkConditions(org.eclipse.core.runtime.IProgressMonitor
	 * , org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext)
	 */
	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context)
			throws OperationCanceledException {

		IResource resource = this.compilationUnit.getResource();
		File javaFile = new File(resource.getLocation().toOSString());
		try {
			FileInputStream input = new FileInputStream(javaFile);
			CompilationUnit compilationUnit = JavaParser.parse(input, CharEncoding.UTF_8);

			if (JavaParserUtil.hasAnnotation(compilationUnit, ScreenAnnotationConstants.SCREEN_ENTITY_ANNOTATION,
					ScreenAnnotationConstants.SCREEN_ENTITY_SUPER_CLASS_ANNOTATION, RpcAnnotationConstants.RPC_ENTITY_ANNOTATION,
					RpcAnnotationConstants.RPC_ENTITY_SUPER_CLASS_ANNOTATION)) {
				return new RefactoringStatus();
			}
		} catch (IOException e) {
			return RefactoringStatus.createErrorStatus(e.getMessage());
		} catch (ParseException e) {
			logger.warn("Failed parsing java file:" + e.getMessage());//$NON-NLS-1$
			return RefactoringStatus.createErrorStatus(e.getMessage());
			// non compiled java class. Ignore it
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#createChange(org.eclipse.core.runtime.IProgressMonitor
	 * )
	 */
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		Change change = new Change() {

			@Override
			public Change perform(IProgressMonitor pm) throws CoreException {
				String fileNoExtension = FileUtils.fileWithoutExtension(compilationUnit.getElementName());
				IResource[] members = compilationUnit.getResource().getParent().members();
				for (IResource iResource : members) {
					String resourceName = iResource.getName();
					if (resourceName.endsWith(fileNoExtension + DesignTimeExecuter.RESOURCES_FOLDER_SUFFIX)) {
						if (iResource instanceof IContainer) {
							IContainer iContainer = (IContainer)iResource;
							IResource[] members2 = iContainer.members();
							for (IResource iResource2 : members2) {
								if (iResource2.getName().startsWith(fileNoExtension)) {
									String fileExtension = iResource2.getFileExtension();
									File parentFile = new File(iResource.getLocation().toOSString());
									File file = new File(iResource2.getLocation().toOSString());
									file.renameTo(new File(parentFile, MessageFormat.format("{0}.{1}", newName, fileExtension)));//$NON-NLS-1$
								}
							}
							File parentFile = new File(iResource.getParent().getLocation().toOSString());
							File file = new File(iResource.getLocation().toOSString());
							file.renameTo(new File(parentFile, MessageFormat.format("{0}{1}", newName,//$NON-NLS-1$
									DesignTimeExecuter.RESOURCES_FOLDER_SUFFIX)));
							iResource.refreshLocal(IResource.DEPTH_INFINITE, pm);
						}
					} else if (resourceName.endsWith(fileNoExtension + DesignTimeExecuter.ASPECT_SUFFIX)) {
						iResource.delete(false, pm);
					}
				}
				EclipseDesignTimeExecuter.instance().renameViews(fileNoExtension, newName, compilationUnit);
				return null;
			}

			@Override
			public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException, OperationCanceledException {
				return RefactoringStatus.createInfoStatus("Ok");//$NON-NLS-1$
			}

			@Override
			public void initializeValidationData(IProgressMonitor pm) {}

			@Override
			public String getName() {
				return MessageFormat.format("{0} {1}", Messages.getString("rename_resources_name"),//$NON-NLS-1$
						compilationUnit.getElementName());
			}

			@Override
			public Object getModifiedElement() {
				return null;
			}
		};
		return change;
	}

}

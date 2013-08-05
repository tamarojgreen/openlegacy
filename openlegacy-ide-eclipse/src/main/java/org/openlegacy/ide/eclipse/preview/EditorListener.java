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
package org.openlegacy.ide.eclipse.preview;

import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.IFileBufferListener;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;

public class EditorListener implements IFileBufferListener, IPartListener2, CaretListener, ModifyListener {

	protected AbstractEntityPreview view;

	EditorListener(AbstractEntityPreview view) {
		this.view = view;
	}

	/**
	 * clean view reference
	 */
	public void dispose() {
		this.view = null;
	}

	/**
	 * @see org.eclipse.core.filebuffers.IFileBufferListener#dirtyStateChanged(org.eclipse.core.filebuffers.IFileBuffer, boolean)
	 */
	public void dirtyStateChanged(final IFileBuffer buffer, final boolean isDirty) {
		if (isSupportedBuffer(buffer)) {
			if (!isDirty) {
				Runnable runnable = new Runnable() {

					public void run() {
						view.handleBufferIsDirty(buffer);
					}
				};
				Display display = Display.getDefault();
				display.asyncExec(runnable);
			}
		}
	}

	private static boolean isSupportedBuffer(IFileBuffer buffer) {
		String fileExtension = buffer.getLocation().getFileExtension();
		return "java".equals(fileExtension);
	}

	/**
	 * @see org.eclipse.ui.IPartListener2#partClosed(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partClosed(IWorkbenchPartReference partRef) {
		view.handlePartClosed(partRef.getPart(false));
	}

	/**
	 * @see org.eclipse.ui.IPartListener2#partHidden(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partHidden(IWorkbenchPartReference partRef) {
		view.handlePartHidden(partRef.getPart(false));
	}

	/**
	 * @see org.eclipse.ui.IPartListener2#partOpened(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partOpened(IWorkbenchPartReference partRef) {
		// is not used here
	}

	/**
	 * @see org.eclipse.ui.IPartListener2#partVisible(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partVisible(IWorkbenchPartReference partRef) {
		view.handlePartVisible(partRef.getPart(false));
	}

	/**
	 * @see org.eclipse.core.filebuffers.IFileBufferListener#bufferDisposed(org.eclipse.core.filebuffers.IFileBuffer)
	 */
	public void bufferDisposed(IFileBuffer buffer) {
		// is not used here
	}

	/**
	 * @see org.eclipse.core.filebuffers.IFileBufferListener#bufferCreated(org.eclipse.core.filebuffers.IFileBuffer)
	 */
	public void bufferCreated(IFileBuffer buffer) {
		// is not used here
	}

	/**
	 * @see org.eclipse.core.filebuffers.IFileBufferListener#bufferContentAboutToBeReplaced(org.eclipse.core.filebuffers.IFileBuffer)
	 */
	public void bufferContentAboutToBeReplaced(IFileBuffer buffer) {
		// is not used here
	}

	/**
	 * @see org.eclipse.core.filebuffers.IFileBufferListener#bufferContentReplaced(org.eclipse.core.filebuffers.IFileBuffer)
	 */
	public void bufferContentReplaced(IFileBuffer buffer) {
		// is not used here
	}

	/**
	 * @see org.eclipse.core.filebuffers.IFileBufferListener#stateChanging(org.eclipse.core.filebuffers.IFileBuffer)
	 */
	public void stateChanging(IFileBuffer buffer) {
		// is not used here
	}

	/**
	 * @see org.eclipse.core.filebuffers.IFileBufferListener#stateValidationChanged(org.eclipse.core.filebuffers.IFileBuffer,
	 *      boolean)
	 */
	public void stateValidationChanged(IFileBuffer buffer, boolean isStateValidated) {
		// is not used here
	}

	/**
	 * @see org.eclipse.core.filebuffers.IFileBufferListener#underlyingFileMoved(org.eclipse.core.filebuffers.IFileBuffer,
	 *      org.eclipse.core.runtime.IPath)
	 */
	public void underlyingFileMoved(IFileBuffer buffer, IPath path) {
		// is not used here
	}

	/**
	 * @see org.eclipse.core.filebuffers.IFileBufferListener#underlyingFileDeleted(org.eclipse.core.filebuffers.IFileBuffer)
	 */
	public void underlyingFileDeleted(IFileBuffer buffer) {
		// is not used here
	}

	/**
	 * @see org.eclipse.core.filebuffers.IFileBufferListener#stateChangeFailed(org.eclipse.core.filebuffers.IFileBuffer)
	 */
	public void stateChangeFailed(IFileBuffer buffer) {
		// is not used here
	}

	/**
	 * @see org.eclipse.ui.IPartListener2#partInputChanged(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// is not used here
	}

	/**
	 * @see org.eclipse.ui.IPartListener2#partActivated(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partActivated(IWorkbenchPartReference partRef) {
		view.handlePartActivated(partRef.getPart(false));
	}

	/**
	 * @see org.eclipse.ui.IPartListener2#partBroughtToTop(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// is not used here
	}

	/**
	 * @see org.eclipse.ui.IPartListener2#partDeactivated(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// is not used here
	}

	public void caretMoved(CaretEvent caretEvent) {
		view.handleCaretMoved(caretEvent);
	}

	public void modifyText(ModifyEvent event) {
		view.handleModifyText(event);
	}
}

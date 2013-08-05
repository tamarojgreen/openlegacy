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
package org.openlegacy.ide.eclipse.preview.screen;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.openlegacy.ide.eclipse.components.ImageComposite;
import org.openlegacy.ide.eclipse.components.screen.SnapshotComposite;
import org.openlegacy.ide.eclipse.preview.AbstractEntityPreview;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotImageRenderer;
import org.openlegacy.terminal.support.DefaultTerminalSnapshotsLoader;
import org.openlegacy.terminal.support.SimpleTerminalPosition;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ScreenPreview extends AbstractEntityPreview {

	private static final String IDENTIFIER_ANNOTATION = "org.openlegacy.annotations.screen.Identifier";
	private static final String SCREEN_ENTITY_ANNOTATION = "org.openlegacy.annotations.screen.ScreenEntity";
	private static final String SCREEN_ENTITY_ANNOTATION_SHORT = "ScreenEntity";
	private static final String SCREEN_FIELD_ANNOTATION = "org.openlegacy.annotations.screen.ScreenField";
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.openlegacy.ide.eclipse.preview.ScreenPreview";

	private SnapshotComposite snapshotComposite;

	private TerminalSnapshot terminalSnapshot;

	private final String XML_EXTENSION = ".xml";

	private static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
		List<T> r = new ArrayList<T>(c.size());
		for (Object o : c) {
			r.add(clazz.cast(o));
		}
		return r;
	}

	private static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean checkVisitedAnnotation(NormalAnnotation annotation) {
		int row = 0;
		int col = 0;
		int endCol = 0;
		String val = "";

		List<MemberValuePair> values = castList(MemberValuePair.class, annotation.values());
		for (MemberValuePair pair : values) {
			SimpleName key = pair.getName();
			String value = pair.getValue().toString();
			if (key.getIdentifier().equalsIgnoreCase("row") && isInteger(value)) {
				row = new Integer(value).intValue();
			}
			if (key.getIdentifier().equalsIgnoreCase("column") && isInteger(value)) {
				col = new Integer(value).intValue();
			}
			if (key.getIdentifier().equalsIgnoreCase("endColumn") && isInteger(value)) {
				endCol = new Integer(value).intValue();
			}
			if (key.getIdentifier().equalsIgnoreCase("value")) {
				val = value;
			}
		}
		snapshotComposite.addDrawingRectangle(getRectangle(row, row, col, endCol, val), SWT.COLOR_YELLOW, true);
		return true;
	}

	private Rectangle getRectangle(int row, int endRow, int column, int endColumn, String value) {
		if (terminalSnapshot == null) {
			return new Rectangle(0, 0, 0, 0);
		}
		DefaultTerminalSnapshotImageRenderer renderer = new DefaultTerminalSnapshotImageRenderer();

		int length = 0;
		if (endColumn == 0) {
			SimpleTerminalPosition terminalPosition = new SimpleTerminalPosition(row, column);
			TerminalField field = terminalSnapshot.getField(terminalPosition);
			if (field != null) {
				TerminalPosition endPosition = field.getEndPosition();
				endColumn = endPosition.getColumn();
			} else {
				// if column attribute was changed then calculate endColumn
				endColumn = column + value.length() - 1;
			}
		}
		length = endColumn - column + 1;

		int x = renderer.toWidth(column - 1 + renderer.getLeftColumnsOffset());
		int y = renderer.toHeight(row - 1) + renderer.getTopPixelsOffset();
		int width = renderer.toWidth(length);
		int height = renderer.toHeight(1);
		if (endRow >= row) {
			height = renderer.toHeight(endRow - row + 1);
		}
		return new Rectangle(x, y, width, height);
	}

	@Override
	protected IFile getSourceFile(IPath path) {
		String className = path.lastSegment().replace("." + path.getFileExtension(), "");
		StringBuilder builder = new StringBuilder(path.removeFileExtension().toOSString());
		builder.append("-resources");
		builder.append(File.separator);
		builder.append(className + XML_EXTENSION);

		return getFile(builder.toString());

	}

	private void handleCaretMoved(int widgetCaretOffset) {
		IJavaElement javaInput = getJavaInput();
		if (javaInput == null) {
			return;
		}

		String key = javaInput.getPath().toOSString();
		CompilationUnit cu = getCacheCompilationUnitContainer().get(key);
		if (cu == null) {
			return;
		}
		IEditorPart activeEditor = getActiveEditor();

		final AtomicInteger offset = new AtomicInteger(0);
		ITextOperationTarget target = (ITextOperationTarget)activeEditor.getAdapter(ITextOperationTarget.class);
		if (target instanceof ITextViewer) {
			TextViewer textViewer = (TextViewer)target;
			offset.set(textViewer.widgetOffset2ModelOffset(widgetCaretOffset));
		}

		final AtomicBoolean isAnnotationVisited = new AtomicBoolean(false);

		cu.accept(new ASTVisitor() {

			@Override
			public void endVisit(FieldDeclaration node) {
				super.endVisit(node);
				if (isAnnotationVisited.get()) {
					return;
				}
				if ((offset.get() < node.getStartPosition()) || (offset.get() > (node.getStartPosition() + node.getLength()))) {
					return;
				}
				List<ASTNode> modifiers = castList(ASTNode.class, node.modifiers());
				for (ASTNode astNode : modifiers) {
					if (astNode instanceof NormalAnnotation) {
						ITypeBinding binding = ((NormalAnnotation)astNode).resolveTypeBinding();
						// binding can be null, for example if user copy field with annotation
						if (binding == null) {
							continue;
						}
						if (binding.getQualifiedName().equals(SCREEN_FIELD_ANNOTATION)) {
							// retrieve rectangle from annotation attributes
							isAnnotationVisited.set(checkVisitedAnnotation((NormalAnnotation)astNode));
						}
					}
				}
			}

			@Override
			public void endVisit(NormalAnnotation node) {
				super.endVisit(node);
				if (isAnnotationVisited.get()) {
					return;
				}
				if ((offset.get() < node.getStartPosition()) || (offset.get() > (node.getStartPosition() + node.getLength()))) {
					snapshotComposite.addDrawingRectangle(null);
					return;
				}
				ITypeBinding binding = node.resolveTypeBinding();
				// binding can be null, for example if user copy field with annotation
				if (binding == null) {
					return;
				}
				if (binding.getQualifiedName().equals(IDENTIFIER_ANNOTATION)
						|| binding.getQualifiedName().equals(SCREEN_FIELD_ANNOTATION)) {
					// retrieve rectangle from annotation attributes
					isAnnotationVisited.set(checkVisitedAnnotation(node));
				}
			}

		});
	}

	@Override
	protected void showPreviewImage(IJavaElement javaInput, IFile xmlFile) {

		if (xmlFile != null && xmlFile.exists()) {
			// show snapshot composite
			DefaultTerminalSnapshotsLoader loader = new DefaultTerminalSnapshotsLoader();
			terminalSnapshot = loader.load(new File(xmlFile.getLocationURI()).getAbsolutePath());
			snapshotComposite.setSnapshot(terminalSnapshot);
			snapshotComposite.setVisible(true);
			return;
		}
		// hide image
		snapshotComposite.setVisible(false);
	}

	public void addFieldRectangle(FieldRectangle rectangle) {
		addFieldRectangle(rectangle, SWT.COLOR_YELLOW, false);
	}

	public void addFieldRectangle(FieldRectangle rectangle, int color) {
		addFieldRectangle(rectangle, color, false);
	}

	public void addFieldRectangle(FieldRectangle rectangle, int color, boolean clearListBeforeAdd) {
		if (rectangle != null) {
			this.snapshotComposite.addDrawingRectangle(
					getRectangle(rectangle.getRow(), rectangle.getEndRow(), rectangle.getColumn(), rectangle.getEndColumn(),
							rectangle.getValue()), color, clearListBeforeAdd);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		snapshotComposite = new SnapshotComposite(parent);
		snapshotComposite.setIsScalable(true);
	}

	public SelectedObject getSelectedObject() {
		return this.snapshotComposite.getSelectedObject();
	}

	@Override
	public void handleCaretMoved(CaretEvent caretEvent) {
		handleCaretMoved(caretEvent.caretOffset);
	}

	@Override
	public void handleModifyText(ModifyEvent event) {
		IJavaElement javaInput = getJavaInput();
		if (javaInput == null) {
			return;
		}
		String key = javaInput.getPath().toOSString();
		if (!getCacheCompilationUnitContainer().containsKey(key)) {
			return;
		}
		CompilationUnit cu = createParser((ICompilationUnit)javaInput);
		getCacheCompilationUnitContainer().put(key, cu);
		StyledText styledText = getCacheStyledTextContainer().get(key);
		if ((styledText != null) && !styledText.isDisposed()) {
			this.handleCaretMoved(styledText.getCaretOffset());
		}
	}

	@Override
	protected String getEntityAnnotationShort() {
		return SCREEN_ENTITY_ANNOTATION_SHORT;
	}

	@Override
	protected String getEntityAnnotation() {
		return SCREEN_ENTITY_ANNOTATION;
	}

	@Override
	protected ImageComposite getImageComposite() {
		return snapshotComposite;
	}

}
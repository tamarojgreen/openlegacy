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

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.openlegacy.ide.eclipse.components.ImageComposite;
import org.openlegacy.ide.eclipse.editors.graphical.IOpenLegacyEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractEntityPreview extends ViewPart {

	private Map<String, CompilationUnit> cacheCompilationUnitContainer = new HashMap<String, CompilationUnit>();

	private Map<String, JavaClassProperties> cacheJavaClassPropertiesContainer = new HashMap<String, JavaClassProperties>();

	private Map<String, StyledText> cacheStyledTextContainer = new HashMap<String, StyledText>();

	private EditorListener editorListener;

	private boolean isVisible;

	private IEditorPart lastActiveEditor;

	protected static CompilationUnit createParser(ICompilationUnit javaInput) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(javaInput);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);

		return (CompilationUnit)parser.createAST(null);
	}

	private static CompilationUnit createParser(String input) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(input.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);

		return (CompilationUnit)parser.createAST(null);
	}

	protected static IEditorPart getActiveEditor() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				return page.getActiveEditor();
			}
		}
		return null;
	}

	protected static IFile getFile(String path) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.getFile(new Path(path));
	}

	/**
	 * Extract IJavaElement if current document is a java file
	 * 
	 * @return IJavaElement or null
	 */
	protected static IJavaElement getJavaInput() {
		IEditorPart activeEditor = getActiveEditor();
		if (activeEditor != null) {
			if (!(activeEditor instanceof ITextEditor) && !(activeEditor instanceof IOpenLegacyEditor)) {
				return null;
			}
			final IJavaElement javaInput = getJavaInput(activeEditor);
			if (javaInput != null && javaInput.getPath().getFileExtension().equals("java")) {
				return javaInput;
			}
		}
		return null;
	}

	private static IJavaElement getJavaInput(IEditorPart part) {
		IEditorInput editorInput = part.getEditorInput();
		if (editorInput != null) {
			IJavaElement input = (IJavaElement)editorInput.getAdapter(IJavaElement.class);
			return input;
		}
		return null;
	}

	private boolean inspectAnnotationNode(NormalAnnotation node, List<String> imports) {
		Name typeName = node.getTypeName();
		ITypeBinding binding = typeName.resolveTypeBinding();
		if (binding != null) {
			String qualifiedName = binding.getQualifiedName();
			if (qualifiedName != null) {
				if (qualifiedName.equals(getEntityAnnotation())) {
					return true;
				}
			}
		} else {
			// in case if binding is not resolved
			String fullyQualifiedName = typeName.getFullyQualifiedName();
			if (fullyQualifiedName.equals(getEntityAnnotation()) || fullyQualifiedName.equals(getEntityAnnotationShort())) {
				if (imports.contains(getEntityAnnotation())
						|| imports.contains(getEntityAnnotation().replace(getEntityAnnotationShort(), "*"))) {
					return true;
				}
			}
		}
		return false;
	}

	protected abstract String getEntityAnnotation();

	protected abstract String getEntityAnnotationShort();

	private static List<String> prepareImports(Object[] imports) {
		List<String> importList = new ArrayList<String>();
		for (Object importItem : imports) {
			String string = importItem.toString();
			importList.add(string.substring(string.indexOf("import") + "import".length(), string.lastIndexOf(";")).trim());
		}
		return importList;
	}

	/**
	 * Extract JavaClassProperties if current source annotated with <b>@ScreenEntity</b>
	 * 
	 * @return JavaClassProperties instance or null
	 */
	private JavaClassProperties getJavaClassProperties(String key, Object source) {
		final AtomicBoolean isAccepted = new AtomicBoolean(false);
		IFile sourceFile = null;

		CompilationUnit cu = getCacheCompilationUnitContainer().get(key);
		if (cu == null) {
			// create parser
			if (source instanceof ITextFileBuffer) {
				cu = createParser(((ITextFileBuffer)source).getDocument().get());
			} else {
				cu = createParser((ICompilationUnit)source);
			}
		}

		getCacheCompilationUnitContainer().put(key, cu);

		if (cu == null) {
			return null;
		}

		final List<String> imports = prepareImports(cu.imports().toArray());

		// run through the code and visit Annotation nodes
		cu.accept(new ASTVisitor() {

			@Override
			public boolean visit(NormalAnnotation node) {
				if (node != null && !isAccepted.get()) {
					isAccepted.set(inspectAnnotationNode(node, imports));
				}
				return true;
			}
		});
		if (isAccepted.get()) {
			sourceFile = getSourceFile(cu.getJavaElement().getPath());
		}
		return new JavaClassProperties(sourceFile, isAccepted.get());
	}

	@Override
	public void dispose() {
		super.dispose();

		if (editorListener != null) {
			// remove Part listener
			getSite().getWorkbenchWindow().getPartService().removePartListener(editorListener);
			// remove FileBuffer listener
			FileBuffers.getTextFileBufferManager().removeFileBufferListener(editorListener);

			// remove Caret & Modify listeners
			for (StyledText styledText : getCacheStyledTextContainer().values()) {
				if (!styledText.isDisposed()) {
					styledText.removeCaretListener(editorListener);
					styledText.removeModifyListener(editorListener);
				}
			}
			editorListener.dispose();
			editorListener = null;
		}
	}

	public void handleBufferIsDirty(IFileBuffer buffer) {
		// parse annotation(buffer) to update in cache
		String key = buffer.getLocation().toOSString();
		if (cacheJavaClassPropertiesContainer.containsKey(key)) {
			JavaClassProperties properties = getJavaClassProperties(key, buffer);
			cacheJavaClassPropertiesContainer.put(key, properties);
		}
		doPartActivated();
	}

	public void handlePartActivated(IWorkbenchPart part) {
		doPartActivated();
	}

	public void handlePartClosed(IWorkbenchPart part) {
		// hide image if editor with annotation closed
		if (part == lastActiveEditor) {
			getImageComposite().setVisible(false);
		}
	}

	public void handlePartHidden(IWorkbenchPart part) {
		if (this == part) {
			isVisible = false;
		}
	}

	public void handlePartVisible(IWorkbenchPart part) {
		if (this == part) {
			if (isVisible) {
				return;
			}
			isVisible = true;
		}
	}

	private void doPartActivated() {
		if (isVisible) {
			IJavaElement javaInput = getJavaInput();
			if (javaInput != null) {
				final AtomicBoolean isAccepted = new AtomicBoolean(false);

				String key = javaInput.getPath().toOSString();
				if (!cacheJavaClassPropertiesContainer.containsKey(key)) {
					JavaClassProperties properties = getJavaClassProperties(key, javaInput);
					cacheJavaClassPropertiesContainer.put(key, properties);
					isAccepted.set(properties != null);
				} else {
					JavaClassProperties properties = cacheJavaClassPropertiesContainer.get(key);
					// if cached class is annotated then check if there is the latest version of image
					if (properties.isAnnotated()) {
						isAccepted.set(true);
						IFile sourceFile = null;
						sourceFile = properties.getSourceFile();
						IFile newSourceFile = getSourceFile(javaInput.getPath());
						if (sourceFile.getModificationStamp() != newSourceFile.getModificationStamp()) {
							sourceFile = newSourceFile;
							properties.setSourceFile(newSourceFile);
						}
					}
				}

				if (isAccepted.get()) {
					// add caret listener
					if (!getCacheStyledTextContainer().containsKey(key) || getCacheStyledTextContainer().get(key).isDisposed()) {
						IEditorPart activeEditor = getActiveEditor();
						if (!(activeEditor instanceof IOpenLegacyEditor)) {
							AbstractTextEditor editor = (AbstractTextEditor)activeEditor;
							StyledText styledText = ((StyledText)editor.getAdapter(Control.class));
							styledText.addCaretListener(editorListener);
							styledText.addModifyListener(editorListener);
							getCacheStyledTextContainer().put(key, styledText);
						}
					}
					// show image if it exists
					showPreviewImage(javaInput, cacheJavaClassPropertiesContainer.get(key).getSourceFile());
					lastActiveEditor = getActiveEditor();
					return;
				}
			}
		}
		// hide image
		getImageComposite().setVisible(false);
	}

	protected abstract void showPreviewImage(IJavaElement javaInput, IFile sourceFile);

	protected abstract ImageComposite getImageComposite();

	protected abstract IFile getSourceFile(IPath path);

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		if (editorListener == null) {
			editorListener = new EditorListener(this);
			// set Part listener
			getSite().getWorkbenchWindow().getPartService().addPartListener(editorListener);
			// set FileBuffer listener
			FileBuffers.getTextFileBufferManager().addFileBufferListener(editorListener);
		}

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		getImageComposite().setFocus();
	}

	public void showEnlargedImage() {
		getImageComposite().showEnlargedImage();
	}

	public void showMessage(String message) {
		MessageDialog.openInformation(getImageComposite().getShell(), "Screen Preview", message);
	}

	public Map<String, CompilationUnit> getCacheCompilationUnitContainer() {
		return cacheCompilationUnitContainer;
	}

	public Map<String, StyledText> getCacheStyledTextContainer() {
		return cacheStyledTextContainer;
	}

	public void handleCaretMoved(CaretEvent caretEvent) {
		// allow override

	}

	public void handleModifyText(ModifyEvent event) {
		// allow override
	}
}
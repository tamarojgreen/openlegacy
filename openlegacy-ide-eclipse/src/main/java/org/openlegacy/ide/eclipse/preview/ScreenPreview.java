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
import org.eclipse.jdt.core.IImportContainer;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.ITextEditor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScreenPreview extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mif.plugin.adapter.views.ScreenPreview";
	private final String ANNOTATION_TO_HANDLE = "org.openlegacy.annotations.screen.ScreenEntity";
	private final String ANNOTATION_TO_HANDLE_SHORT = "ScreenEntity";
	private final String IMAGE_EXTENSION = ".jpg";

	private Map<String, JavaClassProperties> cacheContainer = new HashMap<String, JavaClassProperties>();
	private PreviewCanvas viewer;
	private Image image;

	private EditorListener editorListener;

	private boolean isVisible;
	private IEditorPart lastActiveEditor;

	/**
	 * The constructor.
	 */
	public ScreenPreview() {}

	// **************** OVERRIDEN ****************
	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new PreviewCanvas(parent, SWT.NONE);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.setFocus();
	}

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

	@Override
	public void dispose() {
		super.dispose();

		if (editorListener != null) {
			// remove Part listener
			getSite().getWorkbenchWindow().getPartService().removePartListener(editorListener);
			// remove FileBuffer listener
			FileBuffers.getTextFileBufferManager().removeFileBufferListener(editorListener);

			editorListener.dispose();
			editorListener = null;
		}
		disposeImage();

	}

	// **************** PUBLIC ****************

	public void showMessage(String message) {
		MessageDialog.openInformation(viewer.getShell(), "Screen Preview", message);
	}

	public void handlePartActivated(IWorkbenchPart part) {
		parseAnnotation();
	}

	public void handlePartClosed(IWorkbenchPart part) {
		// hide image if editor with annotation closed
		if (part == lastActiveEditor) {
			viewer.setVisible(false);
		}
	}

	public void handlePartHidden(IWorkbenchPart part) {
		if (this == part) {
			isVisible = false;
		}
	}

	public void handleBufferIsDirty(IFileBuffer buffer) {
		// parse annotation(buffer) to update in cache
		String key = buffer.getLocation().toOSString();
		if (cacheContainer.containsKey(key)) {
			final AtomicBoolean isAccepted = new AtomicBoolean(false);
			IFile imageFile = null;

			CompilationUnit cu = createParser(((ITextFileBuffer)buffer).getDocument().get());

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
				imageFile = getImageFile(buffer.getLocation());
			}
			cacheContainer.put(key, new JavaClassProperties(imageFile, isAccepted.get()));
		}
		parseAnnotation();
	}

	private static List<String> prepareImports(Object[] imports) {
		List<String> importList = new ArrayList<String>();
		for (Object importItem : imports) {
			String string = importItem.toString();
			importList.add(string.substring(string.indexOf("import") + "import".length(), string.lastIndexOf(";")).trim());
		}
		return importList;
	}

	private List<String> prepareImports(IImportContainer importContainer) {
		List<String> importList = new ArrayList<String>();
		if (importContainer.getImport(ANNOTATION_TO_HANDLE).exists()
				|| importContainer.getImport(ANNOTATION_TO_HANDLE.replace(ANNOTATION_TO_HANDLE_SHORT, "*")).exists()) {
			importList.add(ANNOTATION_TO_HANDLE);
		}
		return importList;

	}

	public void handlePartVisible(IWorkbenchPart part) {
		if (this == part) {
			if (isVisible) {
				return;
			}
			isVisible = true;
		}
	}

	public static IFile getFile(String path) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.getFile(new Path(path));
	}

	// **************** PRIVATE ****************

	private void showPreviewImage(IJavaElement javaInput, IFile imageFile) throws JavaModelException {

		if (imageFile != null && imageFile.exists()) {
			// show image
			setImage(imageFile);
			viewer.setVisible(true);
			return;
		}
		// hide image
		viewer.setVisible(false);
	}

	private IFile getImageFile(IPath path) {
		// IPath path = javaInput.getPath();

		String className = path.lastSegment().replace("." + path.getFileExtension(), "");
		StringBuilder builder = new StringBuilder(path.removeFileExtension().toOSString());
		builder.append("-resources");
		builder.append(File.separator);
		builder.append(className + IMAGE_EXTENSION);

		return getFile(builder.toString());

	}

	private void parseAnnotation() {
		if (isVisible) {
			IEditorPart activeEditor = getActiveEditor();
			if (activeEditor != null) {
				if (!(activeEditor instanceof ITextEditor)) {
					return;
				}
				final IJavaElement javaInput = getJavaInput(activeEditor);
				// check if current document is a java file
				if (javaInput != null && javaInput.getPath().getFileExtension().equals("java")) {

					final AtomicBoolean isAccepted = new AtomicBoolean(false);
					IFile imageFile = null;

					String key = javaInput.getPath().toOSString();
					if (!cacheContainer.containsKey(key)) {
						// create parser
						CompilationUnit cu = createParser((ICompilationUnit)javaInput);

						// run through the code and visit Annotation nodes
						cu.accept(new ASTVisitor() {

							@Override
							public boolean visit(NormalAnnotation node) {
								if (node != null && !isAccepted.get()) {

									List<String> imports = prepareImports(((ICompilationUnit)javaInput).getImportContainer());
									isAccepted.set(inspectAnnotationNode(node, imports));
								}
								return true;
							}
						});
						imageFile = getImageFile(javaInput.getPath());
						cacheContainer.put(key, new JavaClassProperties(imageFile, isAccepted.get()));

					} else {
						JavaClassProperties properties = cacheContainer.get(key);
						// if cached class is annotated then check if there is the latest version of image
						if (properties.isAnnotated()) {
							isAccepted.set(true);
							imageFile = properties.getImagefile();
							IFile newImageFile = getImageFile(javaInput.getPath());
							if (imageFile.getModificationStamp() != newImageFile.getModificationStamp()) {
								imageFile = newImageFile;
								properties.setImagefile(newImageFile);
							}
						}

					}

					if (isAccepted.get()) {
						try {
							// show image if it exists
							showPreviewImage(javaInput, imageFile);
							lastActiveEditor = activeEditor;
							return;
						} catch (JavaModelException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		// hide image
		viewer.setVisible(false);
	}

	private static CompilationUnit createParser(ICompilationUnit javaInput) {
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

	private boolean inspectAnnotationNode(NormalAnnotation node, List<String> imports) {
		Name typeName = node.getTypeName();
		ITypeBinding binding = typeName.resolveTypeBinding();
		if (binding != null) {
			String qualifiedName = binding.getQualifiedName();
			if (qualifiedName != null) {
				if (qualifiedName.equals(ANNOTATION_TO_HANDLE)) {
					return true;
				}
			}
		} else {
			// in case if binding is not resolved
			String fullyQualifiedName = typeName.getFullyQualifiedName();
			if (fullyQualifiedName.equals(ANNOTATION_TO_HANDLE) || fullyQualifiedName.equals(ANNOTATION_TO_HANDLE_SHORT)) {
				if (imports.contains(ANNOTATION_TO_HANDLE)
						|| imports.contains(ANNOTATION_TO_HANDLE.replace(ANNOTATION_TO_HANDLE_SHORT, "*"))) {
					return true;
				}
			}
		}
		return false;
	}

	private static IEditorPart getActiveEditor() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				return page.getActiveEditor();
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

	private void disposeImage() {
		if (image == null) {
			return;
		}
		if (image.isDisposed()) {
			return;
		}
		image.dispose();
		image = null;
	}

	// set image for Screen Preview
	private void setImage(IFile file) {
		InputStream in = null;
		try {
			in = file.getContents();
			Image newImage = new Image(Display.getDefault(), in);
			viewer.setImage(newImage);
			disposeImage();
			image = newImage;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
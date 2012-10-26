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
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
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
import org.openlegacy.ide.eclipse.components.SnapshotComposite;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotImageRenderer;
import org.openlegacy.terminal.support.DefaultTerminalSnapshotsLoader;
import org.openlegacy.terminal.support.SimpleTerminalPosition;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScreenPreview extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mif.plugin.adapter.views.ScreenPreview";
	private static final String SCREEN_ENTITY_ANNOTATION = "org.openlegacy.annotations.screen.ScreenEntity";
	private static final String SCREEN_ENTITY_ANNOTATION_SHORT = "ScreenEntity";
	private static final String IDENTIFIER_ANNOTATION = "org.openlegacy.annotations.screen.Identifier";
	private static final String SCREEN_FIELD_ANNOTATION = "org.openlegacy.annotations.screen.ScreenField";
	private final String XML_EXTENSION = ".xml";

	private Map<String, JavaClassProperties> cacheContainer = new HashMap<String, JavaClassProperties>();
	private SnapshotComposite snapshotComposite;

	private EditorListener editorListener;

	private boolean isVisible;
	private IEditorPart lastActiveEditor;

	private Map<String, StyledText> styledTextContainer = new HashMap<String, StyledText>();
	private boolean isAnnotationVisited = false;
	private TerminalSnapshot terminalSnapshot;

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
		snapshotComposite = new SnapshotComposite(parent);
		snapshotComposite.setIsScalable(true);
		snapshotComposite.initDoubleClickEnlargeListener();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		snapshotComposite.setFocus();
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

			// remove Caret listeners
			for (StyledText styledText : styledTextContainer.values()) {
				if (!styledText.isDisposed()) {
					styledText.removeCaretListener(editorListener);
				}
			}
			editorListener.dispose();
			editorListener = null;
		}
	}

	// **************** PUBLIC ****************

	public void showMessage(String message) {
		MessageDialog.openInformation(snapshotComposite.getShell(), "Screen Preview", message);
	}

	public void handlePartActivated(IWorkbenchPart part) {
		parseAnnotation();
	}

	public void handlePartClosed(IWorkbenchPart part) {
		// hide image if editor with annotation closed
		if (part == lastActiveEditor) {
			snapshotComposite.setVisible(false);
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
			IFile xmlFile = null;

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
				xmlFile = getXmlFile(buffer.getLocation());
			}
			cacheContainer.put(key, new JavaClassProperties(xmlFile, isAccepted.get()));
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

	private static List<String> prepareImports(IImportContainer importContainer) {
		List<String> importList = new ArrayList<String>();
		if (importContainer.getImport(SCREEN_ENTITY_ANNOTATION).exists()
				|| importContainer.getImport(SCREEN_ENTITY_ANNOTATION.replace(SCREEN_ENTITY_ANNOTATION_SHORT, "*")).exists()) {
			importList.add(SCREEN_ENTITY_ANNOTATION);
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

	public void handleCaretMoved(CaretEvent caretEvent) {
		handleCaretMoved(caretEvent.caretOffset);
	}

	// **************** PRIVATE ****************

	private void showPreviewImage(IJavaElement javaInput, IFile xmlFile) throws JavaModelException {

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

	private IFile getXmlFile(IPath path) {
		String className = path.lastSegment().replace("." + path.getFileExtension(), "");
		StringBuilder builder = new StringBuilder(path.removeFileExtension().toOSString());
		builder.append("-resources");
		builder.append(File.separator);
		builder.append(className + XML_EXTENSION);

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
					IFile xmlFile = null;

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
						xmlFile = getXmlFile(javaInput.getPath());
						cacheContainer.put(key, new JavaClassProperties(xmlFile, isAccepted.get()));

					} else {
						JavaClassProperties properties = cacheContainer.get(key);
						// if cached class is annotated then check if there is the latest version of image
						if (properties.isAnnotated()) {
							isAccepted.set(true);
							xmlFile = properties.getXmlFile();
							IFile newXmlFile = getXmlFile(javaInput.getPath());
							if (xmlFile.getModificationStamp() != newXmlFile.getModificationStamp()) {
								xmlFile = newXmlFile;
								properties.setXmlFile(newXmlFile);
							}
						}
					}

					if (isAccepted.get()) {
						// add caret listener
						if (!styledTextContainer.containsKey(key) || styledTextContainer.get(key).isDisposed()) {
							AbstractTextEditor editor = (AbstractTextEditor)activeEditor;
							StyledText styledText = ((StyledText)editor.getAdapter(Control.class));
							styledText.addCaretListener(editorListener);
							styledTextContainer.put(key, styledText);
						}
						try {
							// show image if it exists
							showPreviewImage(javaInput, xmlFile);
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
		snapshotComposite.setVisible(false);
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

	private static boolean inspectAnnotationNode(NormalAnnotation node, List<String> imports) {
		Name typeName = node.getTypeName();
		ITypeBinding binding = typeName.resolveTypeBinding();
		if (binding != null) {
			String qualifiedName = binding.getQualifiedName();
			if (qualifiedName != null) {
				if (qualifiedName.equals(SCREEN_ENTITY_ANNOTATION)) {
					return true;
				}
			}
		} else {
			// in case if binding is not resolved
			String fullyQualifiedName = typeName.getFullyQualifiedName();
			if (fullyQualifiedName.equals(SCREEN_ENTITY_ANNOTATION) || fullyQualifiedName.equals(SCREEN_ENTITY_ANNOTATION_SHORT)) {
				if (imports.contains(SCREEN_ENTITY_ANNOTATION)
						|| imports.contains(SCREEN_ENTITY_ANNOTATION.replace(SCREEN_ENTITY_ANNOTATION_SHORT, "*"))) {
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

	private void handleCaretMoved(int widgetCaretOffset) {
		IEditorPart editorPart = getActiveEditor();
		if (editorPart != null) {
			if (!(editorPart instanceof ITextEditor)) {
				return;
			}
			final IJavaElement javaInput = getJavaInput(editorPart);
			// check if current document is a java file
			if (javaInput != null && javaInput.getPath().getFileExtension().equals("java")) {

				int modelOffset = 0;
				ITextOperationTarget target = (ITextOperationTarget)editorPart.getAdapter(ITextOperationTarget.class);
				if (target instanceof ITextViewer) {
					TextViewer textViewer = (TextViewer)target;
					modelOffset = textViewer.widgetOffset2ModelOffset(widgetCaretOffset);
				}
				final int offset = modelOffset;

				// create parser
				CompilationUnit cu = createParser((ICompilationUnit)javaInput);
				this.isAnnotationVisited = false;
				cu.accept(new ASTVisitor() {

					@Override
					public void endVisit(FieldDeclaration node) {
						super.endVisit(node);
						if (isAnnotationVisited) {
							return;
						}
						if ((offset < node.getStartPosition()) || (offset > (node.getStartPosition() + node.getLength()))) {
							return;
						}
						List<ASTNode> modifiers = castList(ASTNode.class, node.modifiers());
						for (ASTNode astNode : modifiers) {
							if (astNode instanceof NormalAnnotation) {
								ITypeBinding binding = ((NormalAnnotation)astNode).resolveTypeBinding();
								if (binding.getQualifiedName().equals(SCREEN_FIELD_ANNOTATION)) {
									// retrieve rectangle from annotation attributes
									checkVisitedAnnotation((NormalAnnotation)astNode);
								}
							}
						}
					}

					@Override
					public void endVisit(NormalAnnotation node) {
						super.endVisit(node);
						if (isAnnotationVisited) {
							return;
						}
						if ((offset < node.getStartPosition()) || (offset > (node.getStartPosition() + node.getLength()))) {
							snapshotComposite.setDrawingRectangle(null);
							return;
						}
						ITypeBinding binding = node.resolveTypeBinding();
						if (binding.getQualifiedName().equals(IDENTIFIER_ANNOTATION)
								|| binding.getQualifiedName().equals(SCREEN_FIELD_ANNOTATION)) {
							// retrieve rectangle from annotation attributes
							checkVisitedAnnotation(node);
						}
					}

				});
			}
		}
	}

	private void checkVisitedAnnotation(NormalAnnotation annotation) {
		int row = 0;
		int col = 0;
		int endCol = 0;
		String val = "";

		List<MemberValuePair> values = castList(MemberValuePair.class, annotation.values());
		for (MemberValuePair pair : values) {
			SimpleName key = pair.getName();
			Expression value = pair.getValue();
			if (key.getIdentifier().equalsIgnoreCase("row")) {
				row = new Integer(value.toString()).intValue();
			}
			if (key.getIdentifier().equalsIgnoreCase("column")) {
				col = new Integer(value.toString()).intValue();
			}
			if (key.getIdentifier().equalsIgnoreCase("endColumn")) {
				endCol = new Integer(value.toString()).intValue();
			}
			if (key.getIdentifier().equalsIgnoreCase("value")) {
				val = value.toString();
			}
		}
		snapshotComposite.setDrawingRectangle(getRectangle(row, col, endCol, val));
		isAnnotationVisited = true;
	}

	private static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
		List<T> r = new ArrayList<T>(c.size());
		for (Object o : c) {
			r.add(clazz.cast(o));
		}
		return r;
	}

	private Rectangle getRectangle(int row, int column, int endColumn, String value) {
		DefaultTerminalSnapshotImageRenderer renderer = new DefaultTerminalSnapshotImageRenderer();

		int length = 0;
		if (endColumn == 0) {
			SimpleTerminalPosition terminalPosition = new SimpleTerminalPosition(row, column);
			TerminalPosition endPosition = terminalSnapshot.getField(terminalPosition).getEndPosition();
			endColumn = endPosition.getColumn();
		}
		length = endColumn - column + 1;

		int x = renderer.toWidth(column - 1 + renderer.getLeftColumnsOffset());
		int y = renderer.toHeight(row - 1) + renderer.getTopPixelsOffset();
		int width = renderer.toWidth(length);
		int height = renderer.toHeight(1);
		return new Rectangle(x, y, width, height);
	}
}
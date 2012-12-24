package org.openlegacy.ide.eclipse.misc.editors;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.FileEditorInput;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedDefinitionUtils;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.ide.eclipse.editors.graphical.IOpenLegacyEditor;
import org.openlegacy.ide.eclipse.misc.Activator;
import org.openlegacy.ide.eclipse.misc.Messages;
import org.openlegacy.ide.eclipse.misc.editors.pages.fields.FieldsPage;
import org.openlegacy.ide.eclipse.misc.editors.pages.general.GeneralPage;
import org.openlegacy.ide.eclipse.util.PathsUtil;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Graphical editor for java files which contains @ScreenEntity annotation
 * 
 * @author Imivan
 */
public class OpenLegacyJavaEditor extends FormEditor implements IOpenLegacyEditor {

	private ScreenEntityDefinition screenEntityDefinition = null;

	/**
	 * 
	 */
	public OpenLegacyJavaEditor() {}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#createToolkit(org.eclipse.swt.widgets.Display)
	 */
	@Override
	protected FormToolkit createToolkit(Display display) {
		// Create a toolkit that shares colors between editors.
		return new FormToolkit(Activator.getDefault().getFormColors(display));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	@Override
	protected void addPages() {
		setPartName(((FileEditorInput)getEditorInput()).getName());
		try {
			addPage(new GeneralPage(this));
			addPage(new FieldsPage(this));
			addSourcePage();
		} catch (PartInitException e) {
			// TODO mifprojects
			e.printStackTrace();
		} catch (IOException e) {
			// TODO mifprojects
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);

		IJavaElement element = (IJavaElement)input.getAdapter(IJavaElement.class);
		IFile sourceFile = (IFile)element.getResource();

		try {
			CompilationUnit compilationUnit = JavaParser.parse(PathsUtil.toOsLocation(sourceFile));
			File packageDir = new File("", compilationUnit.getPackage().getName().toString().replaceAll("\\.", "/"));
			screenEntityDefinition = CodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, packageDir);
		} catch (Exception e) {
			throw new GenerationException(e);
		}
		if (screenEntityDefinition == null) {
			throw new GenerationException(MessageFormat.format("{0} is not a screen entity", sourceFile.getName()));//$NON-NLS-1$
		}
	}

	private void addSourcePage() throws IOException {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);
		StyledText text = new StyledText(composite, SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);

		IJavaElement element = (IJavaElement)getEditorInput().getAdapter(IJavaElement.class);
		IFile sourceFile = (IFile)element.getResource();
		File file = new File(sourceFile.getLocationURI());
		text.setText(FileUtils.readFileToString(file));

		int index = addPage(composite);
		setPageText(index, Messages.getString("SourcePage.title"));//$NON-NLS-1$
	}
}

package com.openlegacy.enterprise.ide.eclipse.editors.utils;

import com.openlegacy.enterprise.ide.eclipse.editors.models.AbstractEntity;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.ui.actions.OrganizeImportsAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.openlegacy.exceptions.OpenLegacyException;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractEntitySaver {

	protected abstract void doSave(AST ast, CompilationUnit cu, ASTRewrite rewriter, AbstractTypeDeclaration root,
			AbstractEntity entity);

	/**
	 * @param site
	 * @param editorInput
	 * @param entity
	 * @throws OpenLegacyException
	 */
	public void saveEntity(final IEditorSite site, IEditorInput input, AbstractEntity entity) throws OpenLegacyException {
		final IJavaElement javaElement = (IJavaElement)input.getAdapter(IJavaElement.class);
		final CompilationUnit cu = ASTUtils.createParser((ICompilationUnit)javaElement);

		if (cu.types().isEmpty()) {
			return;
		}
		AST ast = cu.getAST();
		ASTRewrite rewriter = ASTRewrite.create(ast);

		// maybe CompilationUnit can have several types?
		AbstractTypeDeclaration root = (AbstractTypeDeclaration)cu.types().get(0);
		// trigger save
		doSave(ast, cu, rewriter, root, entity);

		computationTextEdits(javaElement, cu, rewriter);
		// organize imports
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				OrganizeImportsAction action = new OrganizeImportsAction(site);
				action.run((ICompilationUnit)javaElement);
				// refresh workspace (current folder)
				try {
					javaElement.getJavaProject().getResource().refreshLocal(IResource.DEPTH_ZERO, null);
				} catch (CoreException e) {
				}
			}
		});
	}

	private static void computationTextEdits(IJavaElement javaElement, CompilationUnit cu, ASTRewrite rewrite)
			throws OpenLegacyException {
		try {
			// computation of the text edits
			ICompilationUnit unit = (ICompilationUnit)javaElement;
			Document doc = new Document(unit.getSource());
			TextEdit edits = null;
			if (rewrite != null) {
				edits = rewrite.rewriteAST(doc, unit.getJavaProject().getOptions(true));
			} else {
				edits = cu.rewrite(doc, unit.getJavaProject().getOptions(true));
			}
			// computation of the new source code
			edits.apply(doc);
			// update of the compilation unit
			// get the buffer manager
			ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
			IPath path = cu.getJavaElement().getPath();
			try {
				bufferManager.connect(path, LocationKind.IFILE, null);
				ITextFileBuffer textFileBuffer = bufferManager.getTextFileBuffer(path, LocationKind.IFILE);
				// retrieve the buffer
				IDocument document = textFileBuffer.getDocument();
				document.set(doc.get());
				// commit changes to underlying file
				textFileBuffer.commit(null, false);
			} finally {
				bufferManager.disconnect(path, LocationKind.IFILE, null);
			}
		} catch (CoreException e) {
			throw new OpenLegacyException(e);
		} catch (MalformedTreeException e) {
			throw new OpenLegacyException(e);
		} catch (BadLocationException e) {
			throw new OpenLegacyException(e);
		}
	}

}

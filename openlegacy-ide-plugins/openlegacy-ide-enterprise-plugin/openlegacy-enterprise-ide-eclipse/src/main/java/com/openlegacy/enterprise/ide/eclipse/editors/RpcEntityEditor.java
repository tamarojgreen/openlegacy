package com.openlegacy.enterprise.ide.eclipse.editors;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.rpc.ActionsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.rpc.FieldsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.rpc.GeneralPage;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc.RpcEntitySaver;

import org.apache.commons.lang.CharEncoding;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.openlegacy.definitions.page.support.SimplePageDefinition;
import org.openlegacy.designtime.rpc.generators.support.RpcCodeBasedDefinitionUtils;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.ide.eclipse.preview.rpc.RpcPreview;
import org.openlegacy.ide.eclipse.util.PathsUtil;
import org.openlegacy.layout.PageDefinition;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.text.MessageFormat;

/**
 * Graphical editor for Java files containing @RpcEntity annotation
 * 
 * @author Ivan Bort
 * 
 */
public class RpcEntityEditor extends AbstractEditor {

	private GeneralPage generalPage;
	private FieldsPage fieldsPage;
	private ActionsPage actionsPage;

	private RpcEntity entity;

	public RpcEntityEditor() {}

	@Override
	protected void addEditorPages() throws PartInitException {
		generalPage = new GeneralPage(this);
		fieldsPage = new FieldsPage(this);
		actionsPage = new ActionsPage(this);

		addPage(generalPage);
		addPage(fieldsPage);
		addPage(actionsPage);
		addSourcePage();
	}

	@Override
	protected void populateEntity(IEditorInput editorInput) throws PartInitException {
		RpcEntity entity = null;
		if (editorInput instanceof FileEditorInput) {
			FileEditorInput feInput = (FileEditorInput)editorInput;
			try {
				File inputFile = PathsUtil.toOsLocation(feInput.getFile());
				CompilationUnit compilationUnit = JavaParser.parse(inputFile, CharEncoding.UTF_8);
				File packageDir = new File(inputFile.getParentFile().getAbsolutePath());
				entity = new RpcEntity(RpcCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, packageDir));
			} catch (Exception e) {
				throw new PartInitException(Messages.getString("rpc.error.init.editor"), e);//$NON-NLS-1$
			}

		}
		if (entity.getEntityDefinition() == null) {
			throw new PartInitException(MessageFormat.format("{0} is not a rpc entity", editorInput.getName()));//$NON-NLS-1$
		}
		this.entity = entity;
		// try to open RpcPreview
		IWorkbenchPage activePage = getSite().getWorkbenchWindow().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (activePage != null) {
			activePage.showView(RpcPreview.ID);
		}
	}

	@Override
	protected void doSave() throws PartInitException, OpenLegacyException {
		RpcEntitySaver.INSTANCE.saveEntity(getEditorSite(), getEditorInput(), this.entity);
		populateEntity(getEditorInput());
	}

	@Override
	protected void doAfterSave() {
		generalPage.refresh();
		fieldsPage.refresh();
		actionsPage.refresh();
		editorDirtyStateChanged();
	}

	@Override
	public boolean isDirty() {
		return entity.isDirty();
	}

	public RpcEntity getEntity() {
		return entity;
	}

	@Override
	public void dispose() {
		generalPage = null;
		fieldsPage = null;
		actionsPage = null;
		super.dispose();
	}

	@Override
	protected PageDefinition getPageDefinitionForHtmlPreview() {
		SimplePageDefinition definition = new SimplePageDefinition(entity.getEntityDefinition());
		return definition;
	}

}

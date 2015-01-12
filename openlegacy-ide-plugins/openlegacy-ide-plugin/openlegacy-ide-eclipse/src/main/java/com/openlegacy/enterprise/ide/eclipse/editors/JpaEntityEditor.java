package com.openlegacy.enterprise.ide.eclipse.editors;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.jpa.ActionsPape;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.jpa.FieldsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.jpa.GeneralPage;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.jpa.JpaEntitySaver;

import org.apache.commons.lang.CharEncoding;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.openlegacy.definitions.page.support.SimplePageDefinition;
import org.openlegacy.designtime.db.generators.support.DbCodeBasedDefinitionUtils;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.ide.eclipse.util.PathsUtil;
import org.openlegacy.layout.PageDefinition;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.text.MessageFormat;

/**
 * Graphical editor for Java files containing @Entity annotation
 * 
 * @author Ivan Bort
 * 
 */
public class JpaEntityEditor extends AbstractEditor {

	private GeneralPage generalPage;
	private FieldsPage fieldsPage;
	private ActionsPape actionsPape;

	private JpaEntity entity;

	@Override
	protected void addEditorPages() throws PartInitException {
		generalPage = new GeneralPage(this);
		fieldsPage = new FieldsPage(this);
		actionsPape = new ActionsPape(this);

		addPage(generalPage);
		addPage(fieldsPage);
		addPage(actionsPape);
		addSourcePage();
	}

	@Override
	protected void populateEntity(IEditorInput editorInput) throws PartInitException {
		JpaEntity entity = null;
		if (editorInput instanceof FileEditorInput) {
			FileEditorInput feInput = (FileEditorInput)editorInput;
			try {
				File inputFile = PathsUtil.toOsLocation(feInput.getFile());
				CompilationUnit compilationUnit = JavaParser.parse(inputFile, CharEncoding.UTF_8);
				File packageDir = new File(inputFile.getParentFile().getAbsolutePath());
				entity = new JpaEntity(DbCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, packageDir));
			} catch (Exception e) {
				throw new PartInitException(Messages.getString("jpa.error.init.editor"), e);//$NON-NLS-1$
			}
		}
		if (entity.getEntityDefinition() == null) {
			throw new PartInitException(MessageFormat.format("{0} is not a jpa entity", editorInput.getName()));//$NON-NLS-1$
		}
		this.entity = entity;
	}

	@Override
	protected void doSave() throws PartInitException, OpenLegacyException {
		JpaEntitySaver.INSTANCE.saveEntity(getEditorSite(), getEditorInput(), entity);
		populateEntity(getEditorInput());
	}

	@Override
	protected void doAfterSave() {
		generalPage.refresh();
		fieldsPage.refresh();
		actionsPape.refresh();
		editorDirtyStateChanged();
	}

	@Override
	public boolean isDirty() {
		return entity.isDirty();
	}

	public JpaEntity getEntity() {
		return entity;
	}

	@Override
	public void dispose() {
		generalPage = null;
		fieldsPage = null;
		actionsPape = null;
		super.dispose();
	}

	@Override
	protected PageDefinition getPageDefinitionForHtmlPreview() {
		SimplePageDefinition definition = new SimplePageDefinition(entity.getEntityDefinition());
		return definition;
	}

}

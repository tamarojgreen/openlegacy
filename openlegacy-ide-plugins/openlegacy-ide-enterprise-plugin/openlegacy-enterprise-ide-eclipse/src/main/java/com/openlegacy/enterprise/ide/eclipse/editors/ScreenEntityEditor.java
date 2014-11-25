package com.openlegacy.enterprise.ide.eclipse.editors;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.screen.ActionsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.screen.ChildEntitiesPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.screen.GeneralPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.screen.IdentifiersPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.screen.PartsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.screen.TablesPage;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.screen.ScreenEntitySaver;

import org.apache.commons.lang.CharEncoding;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.openlegacy.designtime.PreferencesConstants;
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.designtime.mains.DesignTimeExecuterImpl;
import org.openlegacy.designtime.terminal.generators.support.ScreenCodeBasedDefinitionUtils;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.ide.eclipse.util.PathsUtil;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.terminal.layout.ScreenPageBuilder;
import org.openlegacy.terminal.layout.support.DefaultBidiScreenPageBuilder;
import org.openlegacy.terminal.layout.support.DefaultScreenPageBuilder;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.text.MessageFormat;
import java.util.Set;
import java.util.UUID;

/**
 * Graphical editor for Java files containing @ScreenEntity annotation
 * 
 * @author Ivan Bort
 */
public class ScreenEntityEditor extends AbstractEditor {

	private GeneralPage generalPage;

	private IdentifiersPage identifiersPage;

	private ActionsPage actionsPage;

	private TablesPage tablesPage;

	private PartsPage partsPage;

	private ChildEntitiesPage childEntitiesPage;

	private ScreenEntity entity;

	private DesignTimeExecuter designTimeExecuter = new DesignTimeExecuterImpl();

	/**
	 * 
	 */
	public ScreenEntityEditor() {}

	@Override
	protected void addEditorPages() throws PartInitException {
		generalPage = new GeneralPage(this);
		partsPage = new PartsPage(this);
		identifiersPage = new IdentifiersPage(this);
		actionsPage = new ActionsPage(this);
		tablesPage = new TablesPage(this);
		childEntitiesPage = new ChildEntitiesPage(this);
		addPage(generalPage);
		addPage(partsPage);
		addPage(identifiersPage);
		addPage(actionsPage);
		addPage(tablesPage);
		addPage(childEntitiesPage);
		addSourcePage();

		performSubscription();
	}

	@Override
	protected void doSave() throws PartInitException, OpenLegacyException {
		ScreenEntitySaver.INSTANCE.saveEntity(getEditorSite(), getEditorInput(), this.entity);
		populateEntity(getEditorInput());
	}

	@Override
	protected void doAfterSave() {
		generalPage.refresh();
		partsPage.refresh();
		identifiersPage.refresh();
		actionsPage.refresh();
		tablesPage.refresh();
		childEntitiesPage.refresh();
		editorDirtyStateChanged();
	}

	@Override
	public boolean isDirty() {
		return this.entity.isDirty();
	}

	@Override
	protected void populateEntity(IEditorInput input) throws PartInitException {
		ScreenEntity entity = null;
		if (input instanceof FileEditorInput) {
			FileEditorInput feInput = (FileEditorInput)input;
			try {
				File inputFile = PathsUtil.toOsLocation(feInput.getFile());
				CompilationUnit compilationUnit = JavaParser.parse(inputFile, CharEncoding.UTF_8);
				//				File packageDir = new File("", compilationUnit.getPackage().getName().toString().replaceAll("\\.", "/"));//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				File packageDir = new File(inputFile.getParentFile().getAbsolutePath());
				entity = new ScreenEntity(ScreenCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, packageDir));
			} catch (Exception e) {
				throw new PartInitException(Messages.getString("error.editor.init.message"), e);//$NON-NLS-1$
			}
		}
		if (entity.getEntityDefinition() == null) {
			throw new PartInitException(MessageFormat.format("{0} is not a screen entity", input.getName()));//$NON-NLS-1$
		}
		this.entity = entity;
	}

	public void addValidationMarker(String key, String text) {
		if (!markers.containsKey(key)) {
			IResource resource = (IResource)getEditorInput().getAdapter(IResource.class);
			try {
				IMarker marker = resource.createMarker(IMarker.PROBLEM);
				marker.setAttribute(IMarker.MESSAGE, text);
				marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
				markers.put(key, marker);
				setTitleImage(Activator.getDefault().getImage(Activator.IMG_EDITOR_ERROR));
			} catch (CoreException e) {
			}
		}
	}

	public void removeValidationMarker(String key) {
		if (markers.containsKey(key)) {
			try {
				markers.get(key).delete();
				markers.remove(key);
				if (markers.isEmpty()) {
					setTitleImage(Activator.getDefault().getImage(Activator.IMG_EDITOR_NORMAL));
				}
			} catch (CoreException e) {
			}
		}
	}

	public void removeValidationMarkers(UUID uuid) {
		if (!markers.isEmpty()) {
			Set<String> keySet = markers.keySet();
			for (String key : keySet) {
				if (key.startsWith(uuid.toString())) {
					removeValidationMarker(key);
				}
			}
		}
	}

	public ScreenEntity getEntity() {
		return entity;
	}

	@Override
	public void dispose() {
		generalPage = null;
		partsPage = null;
		identifiersPage = null;
		actionsPage = null;
		tablesPage = null;
		childEntitiesPage = null;
		super.dispose();
	}

	@Override
	protected PageDefinition getPageDefinitionForHtmlPreview() {
		ScreenPageBuilder builder = null;
		if (getEditorInput() instanceof FileEditorInput) {
			FileEditorInput feInput = (FileEditorInput)getEditorInput();
			File project = PathsUtil.toOsLocation(feInput.getFile().getProject());
			String designtimeContext = designTimeExecuter.getPreferences(project, PreferencesConstants.DESIGNTIME_CONTEXT);
			if (designtimeContext != null && designtimeContext.equals("rtl")) {
				builder = new DefaultBidiScreenPageBuilder();
			}
		}
		if (builder == null) {
			builder = new DefaultScreenPageBuilder();
		}
		return builder.build(entity.getEntityDefinition());
	}

	private void performSubscription() {
		identifiersPage.performSubscription();
	}
}

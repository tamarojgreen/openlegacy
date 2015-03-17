package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ModelUpdater;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class GeneralJpaEntityDetailsPage extends AbstractJpaDetailsPage {

	private JpaEntityModel entityModel;

	public GeneralJpaEntityDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#getDetailsModel()
	 */
	@Override
	public Class<?> getDetailsModel() {
		return JpaEntityModel.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createContents(Composite parent) {
		TableWrapLayout layout = new TableWrapLayout();
		layout.topMargin = 5;
		layout.leftMargin = 5;
		layout.rightMargin = 2;
		layout.bottomMargin = 2;
		parent.setLayout(layout);

		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		section.marginWidth = 10;
		section.setText(Messages.getString("jpa.general.page.details.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("jpa.general.page.details.desc")); //$NON-NLS-1$
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		section.setLayoutData(td);

		Composite client = toolkit.createComposite(section);
		GridLayout glayout = new GridLayout();
		glayout.marginWidth = glayout.marginHeight = 0;
		glayout.numColumns = 2;
		client.setLayout(glayout);

		// create empty row
		FormRowCreator.createSpacer(toolkit, client, 2);
		// create row for "name"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.entity.name"), "", DbAnnotationConstants.NAME, JAVA_DOCUMENTATION_TYPE.JPA, "Entity",//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				DbAnnotationConstants.NAME);
		// create row for "displayName"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.entity.displayName"), "", DbAnnotationConstants.DISPLAY_NAME, JAVA_DOCUMENTATION_TYPE.DB,//$NON-NLS-1$ //$NON-NLS-2$
				"DbEntity", DbAnnotationConstants.DISPLAY_NAME);//$NON-NLS-1$
		// create row for "window"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.entity.window"), false, DbAnnotationConstants.WINDOW, JAVA_DOCUMENTATION_TYPE.DB,//$NON-NLS-1$
				"DbEntity", DbAnnotationConstants.WINDOW);//$NON-NLS-1$
		// create row for "child"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.entity.child"), false, DbAnnotationConstants.CHILD, JAVA_DOCUMENTATION_TYPE.DB,//$NON-NLS-1$
				"DbEntity", DbAnnotationConstants.CHILD);//$NON-NLS-1$

		toolkit.paintBordersFor(section);
		section.setClient(client);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#getModelUUID()
	 */
	@Override
	public UUID getModelUUID() {
		return entityModel != null ? entityModel.getUUID() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.IOpenLegacyDetailsPage#revalidate()
	 */
	@Override
	public void revalidate() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#updateControls()
	 */
	@Override
	protected void updateControls() {
		ControlsUpdater.updateJpaEntityDetailsControls(entityModel, mapTexts, mapCheckBoxes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#doUpdateModel(java.lang.String)
	 */
	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateJpaEntityModel(getEntity(), entityModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#afterDoUpdateModel()
	 */
	@Override
	protected void afterDoUpdateModel() {
		setDirty(getEntity().isDirty());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#selectionChanged(org.eclipse.jface.viewers.
	 * IStructuredSelection)
	 */
	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			entityModel = (JpaEntityModel)selection.getFirstElement();
		} else {
			entityModel = null;
		}
	}

}

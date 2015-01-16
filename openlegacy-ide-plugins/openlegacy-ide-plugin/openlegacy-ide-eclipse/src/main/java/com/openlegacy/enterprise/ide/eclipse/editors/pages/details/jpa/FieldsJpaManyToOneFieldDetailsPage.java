/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaManyToOneFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;
import org.openlegacy.ide.eclipse.Activator;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsJpaManyToOneFieldDetailsPage extends AbstractJpaFieldDetailsPage {

	private JpaManyToOneFieldModel fieldModel;

	private Text targetEntityControl;
	private Text javaTypeControl;

	private TextValidator javaTypeValidator;

	public FieldsJpaManyToOneFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	protected JpaFieldModel getFieldModel() {
		return fieldModel;
	}

	// @Override
	// protected boolean isAddManyToOneSection() {
	// return true;
	// }

	@Override
	protected void addContent(FormToolkit toolkit, Composite client) {
		// create section for @ManyToOne annotation
		Section mtoSection = toolkit.createSection(client, ExpandableComposite.TWISTIE | Section.DESCRIPTION);
		mtoSection.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		mtoSection.setToggleColor(toolkit.getColors().getColor(IFormColors.SEPARATOR));
		toolkit.createCompositeSeparator(mtoSection);

		Composite composite = toolkit.createComposite(mtoSection, SWT.WRAP);
		GridLayout glayout = new GridLayout();
		glayout.numColumns = 2;

		composite.setLayout(glayout);
		// create row for javaType
		javaTypeControl = FormRowCreator.createStringRowWithBrowseButton(toolkit, composite, mapTexts,
				getDefaultModifyListener(), Messages.getString("jpa.field.java.type"), "", Constants.JAVA_TYPE, null, false, null);//$NON-NLS-1$ //$NON-NLS-2$
		javaTypeValidator = new TextValidator(master, managedForm, javaTypeControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateJavaTypeControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return fieldModel;
			}
		};
		// create row for "targetEntity"
		targetEntityControl = FormRowCreator.createStringRowWithBrowseButton(toolkit, composite, mapTexts,
				getDefaultModifyListener(), Messages.getString("jpa.field.list.target.entity"), "",//$NON-NLS-1$ //$NON-NLS-2$
				DbAnnotationConstants.TARGET_ENTITY, null, true, getTargetEntityClearListener());
		// create row for "cascade"
		FormRowCreator.createComboBoxRow(toolkit, composite, mapCombos, getDefaultModifyListener(),
				getDefaultComboBoxKeyListener(), Messages.getString("jpa.field.list.cascade"), getCascadeItems(), 0,
				DbAnnotationConstants.CASCADE, true);
		// create row for "fetch"
		FormRowCreator.createComboBoxRow(toolkit, composite, mapCombos, getDefaultModifyListener(),
				getDefaultComboBoxKeyListener(), Messages.getString("jpa.field.list.fetch.type"), getFetchTypeItems(), 0,//$NON-NLS-1$
				DbAnnotationConstants.FETCH, false);
		// create row for "optional"
		FormRowCreator.createBooleanRow(toolkit, composite, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.optional"), true, DbAnnotationConstants.OPTIONAL);//$NON-NLS-1$

		mtoSection.setText(Messages.getString("jpa.fields.page.many.to.one.section.desc"));//$NON-NLS-1$
		mtoSection.setClient(composite);
		mtoSection.setExpanded(true);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		mtoSection.setLayoutData(gd);

		// add @JoinColumn section
		Section jcSection = toolkit.createSection(client, ExpandableComposite.TWISTIE | Section.DESCRIPTION);
		jcSection.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		jcSection.setToggleColor(toolkit.getColors().getColor(IFormColors.SEPARATOR));
		toolkit.createCompositeSeparator(jcSection);

		Composite jcComposite = toolkit.createComposite(jcSection, SWT.WRAP);
		glayout = new GridLayout();
		glayout.numColumns = 2;

		jcComposite.setLayout(glayout);

		// NOTE: keys for controls must be unique

		// create row for "name"
		FormRowCreator.createStringRow(toolkit, jcComposite, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.name"), "", Constants.JC_NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "referencedColumnName"
		FormRowCreator.createStringRow(toolkit, jcComposite, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.referenced.column.name"), "", Constants.JC_REFERENCED_COLUMN_NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "unique"
		FormRowCreator.createBooleanRow(toolkit, jcComposite, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.unique"), false, Constants.JC_UNIQUE);//$NON-NLS-1$
		// create row for "nullable"
		FormRowCreator.createBooleanRow(toolkit, jcComposite, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.nullable"), true, Constants.JC_NULLABLE);//$NON-NLS-1$
		// create row for "insertable"
		FormRowCreator.createBooleanRow(toolkit, jcComposite, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.insertable"), true, Constants.JC_INSERTABLE);//$NON-NLS-1$
		// create row for "updatable"
		FormRowCreator.createBooleanRow(toolkit, jcComposite, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.updatable"), true, Constants.JC_UPDATABLE);//$NON-NLS-1$
		// create row for "columnDefinition"
		FormRowCreator.createStringRow(toolkit, jcComposite, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.column.definition"), "", Constants.JC_COLUMN_DEFINITION);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "table"
		FormRowCreator.createStringRow(toolkit, jcComposite, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.table"), "", Constants.JC_TABLE);//$NON-NLS-1$ //$NON-NLS-2$

		jcSection.setText(Messages.getString("jpa.fields.page.join.column.section.desc"));//$NON-NLS-1$
		jcSection.setClient(jcComposite);
		jcSection.setExpanded(true);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		jcSection.setLayoutData(gd);

	}

	@Override
	public Class<?> getDetailsModel() {
		return JpaManyToOneFieldModel.class;
	}

	@Override
	public UUID getModelUUID() {
		return fieldModel != null ? fieldModel.getUUID() : null;
	}

	@Override
	protected void updateControls() {
		if (fieldModel == null) {
			return;
		}
		ControlsUpdater.updateJpaFieldDetailsControls(fieldModel, mapTexts, mapCheckBoxes, mapLabels);
		ControlsUpdater.updateJpaManyToOneDetailsControls(fieldModel.getManyToOneModel(), mapTexts, mapCheckBoxes, mapCombos);
		ControlsUpdater.updateJpaJoinColumnDetailsControls(fieldModel.getJoinColumnModel(), mapTexts, mapCheckBoxes);
		revalidate();
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateJpaFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE));
		ModelUpdater.updateJpaManyToOneModel(getEntity(), fieldModel.getManyToOneModel(), key,
				(String)map.get(Constants.TEXT_VALUE), (Boolean)map.get(Constants.BOOL_VALUE),
				(String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
		ModelUpdater.updateJpaJoinColumnModel(getEntity(), fieldModel.getJoinColumnModel(), key,
				(String)map.get(Constants.TEXT_VALUE), (Boolean)map.get(Constants.BOOL_VALUE));
	}

	@Override
	protected void afterDoUpdateModel() {
		setDirty(getEntity().isDirty());
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			fieldModel = (JpaManyToOneFieldModel)selection.getFirstElement();
		} else {
			fieldModel = null;
		}
	}

	@Override
	protected void updateValidators(UUID uuid) {
		super.updateValidators(uuid);
		if (uuid != null) {
			javaTypeValidator.setModelUUID(uuid);
		}
	}

	@Override
	public void revalidate() {
		super.revalidate();
		if (javaTypeValidator != null) {
			javaTypeValidator.revalidate(getModelUUID());
		}
	}

	@Override
	public void removeValidationMarkers() {
		super.removeValidationMarkers();
		if (javaTypeValidator != null) {
			javaTypeValidator.removeValidationMarker();
		}
	}

	private SelectionListener getTargetEntityClearListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				targetEntityControl.setText("");//$NON-NLS-1$
				try {
					ModelUpdater.updateJpaManyToOneModel(getEntity(), getFieldModel().getManyToOneModel(),
							DbAnnotationConstants.TARGET_ENTITY, "", null, "");//$NON-NLS-1$ //$NON-NLS-2$
					setDirty(getEntity().isDirty());
				} catch (Exception ex) {
					ErrorDialog.openError(((IEditorPart)managedForm.getContainer()).getSite().getShell(),
							Messages.getString("error.problem.occurred"), ex.getMessage(), new Status(//$NON-NLS-1$
									IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage()));
				}
			}

		};
	}

	private static String[] getCascadeItems() {
		List<String> list = new ArrayList<String>();
		list.add(CascadeType.ALL.toString());
		list.add(CascadeType.PERSIST.toString());
		list.add(CascadeType.MERGE.toString());
		list.add(CascadeType.REMOVE.toString());
		list.add(CascadeType.REFRESH.toString());
		list.add(CascadeType.DETACH.toString());
		return list.toArray(new String[] {});
	}

	private static String[] getFetchTypeItems() {
		List<String> list = new ArrayList<String>();
		list.add(FetchType.EAGER.toString());
		list.add(FetchType.LAZY.toString());
		return list.toArray(new String[] {});
	}

	private boolean validateJavaTypeControl(TextValidator validator, UUID uuid) {
		boolean isValid = true;
		String text = validator.getControl().getText();
		String fullyQuailifiedName = (String)validator.getControl().getData(FormRowCreator.ID_FULLY_QUALIFIED_NAME);
		if (StringUtils.isEmpty(text)) {
			isValid = false;
		}
		if (isValid && StringUtils.equalsIgnoreCase(text, void.class.getSimpleName())) {
			isValid = false;
		}
		if (!isValid) {
			validator.addMessage(Messages.getString("validation.java.type.must.be.specified"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
			return isValid;
		}
		if (getEntity().getEntityFullyQualifiedName().equals(fullyQuailifiedName)) {
			isValid = false;
		}
		if (!isValid) {
			validator.addMessage(Messages.getString("validation.java.type.equal.to.current.entity"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
		}
		return isValid;
	}

}

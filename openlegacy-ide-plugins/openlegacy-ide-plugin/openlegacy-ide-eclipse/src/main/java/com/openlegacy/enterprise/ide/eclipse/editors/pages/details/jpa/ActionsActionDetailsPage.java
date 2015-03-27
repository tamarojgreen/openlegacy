/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
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
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaActionsModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.jpa.ActionsMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.ComboValidator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.Utils;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.db.actions.DbActions;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.ide.eclipse.Activator;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Entity;

/**
 * @author Ivan Bort
 */
public class ActionsActionDetailsPage extends AbstractJpaDetailsPage {

	private ActionModel model;
	private ComboValidator actionComboValidator;
	private TextValidator targetEntityControlValidator;
	private Text targetEntityControl;

	public ActionsActionDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	public void revalidate() {
		if (actionComboValidator != null) {
			actionComboValidator.revalidate(getModelUUID());
			targetEntityControlValidator.revalidate(getModelUUID());
		}
	}

	@Override
	public void removeValidationMarkers() {
		super.removeValidationMarkers();
		if (actionComboValidator != null) {
			actionComboValidator.removeValidationMarker();
			targetEntityControlValidator.removeValidationMarker();
		}
	}

	@Override
	protected void updateValidators(UUID uuid) {
		super.updateValidators(uuid);
		if (uuid != null) {
			actionComboValidator.setModelUUID(uuid);
			targetEntityControlValidator.setModelUUID(uuid);
		}
	}

	@Override
	public Class<?> getDetailsModel() {
		return ActionModel.class;
	}

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
		section.setText(Messages.getString("rpc.actions.page.details.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("rpc.actions.page.details.desc")); //$NON-NLS-1$
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		section.setLayoutData(td);

		Composite topClient = toolkit.createComposite(section);
		GridLayout glayout = new GridLayout();
		glayout.marginWidth = glayout.marginHeight = 0;
		glayout.numColumns = 1;
		topClient.setLayout(glayout);

		Composite client = toolkit.createComposite(topClient);
		glayout = new GridLayout();
		glayout.marginWidth = glayout.marginHeight = 0;
		glayout.numColumns = 2;
		client.setLayout(glayout);

		// create spacer
		FormRowCreator.createSpacer(toolkit, client, 2);
		// "action" row - combo
		CCombo actionsCombo = FormRowCreator.createComboBoxRow(toolkit, client, mapCombos, getDefaultModifyListener(),
				getDefaultComboBoxKeyListener(), Messages.getString("jpa.actions.page.col.action.label"), getJpaActions(),//$NON-NLS-1$
				0, AnnotationConstants.ACTION, false, JAVA_DOCUMENTATION_TYPE.DB, "Action", "action");//$NON-NLS-1$ //$NON-NLS-2$
		actionComboValidator = new ComboValidator(master, managedForm, actionsCombo, null) {

			@Override
			protected boolean validateControl(ComboValidator validator, UUID uuid) {
				return validateAction(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return model;
			}
		};
		// "displayName" - string
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.actions.page.col.display.name.label"), "", AnnotationConstants.DISPLAY_NAME,//$NON-NLS-1$ //$NON-NLS-2$
				JAVA_DOCUMENTATION_TYPE.DB, "Action", "displayName");//$NON-NLS-1$ //$NON-NLS-2$
		// "alias" - string
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.actions.page.col.alias.label"), "", AnnotationConstants.ALIAS,//$NON-NLS-1$ //$NON-NLS-2$
				JAVA_DOCUMENTATION_TYPE.DB, "Action", "alias");//$NON-NLS-1$ //$NON-NLS-2$
		// "is global" - boolean
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("rpc.actions.page.col.global.label"), false, AnnotationConstants.GLOBAL,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.RPC, "Action", "global");//$NON-NLS-1$ //$NON-NLS-2$
		targetEntityControl = FormRowCreator.createStringRowWithBrowseButton(toolkit, client, mapTexts,
				getDefaultModifyListener(), Messages.getString("jpa.actions.page.col.target.entity"), "",//$NON-NLS-1$ //$NON-NLS-2$
				AnnotationConstants.TARGET_ENTITY, null, true, getTargetEntityClearListener(), JAVA_DOCUMENTATION_TYPE.DB,
				"Action", "targetEntity");
		targetEntityControlValidator = new TextValidator(master, managedForm, targetEntityControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateTargetEntity(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return model;
			}
		};

		toolkit.paintBordersFor(section);
		section.setClient(topClient);
	}

	@Override
	public UUID getModelUUID() {
		return model != null ? model.getUUID() : null;
	}

	@Override
	protected void updateControls() {
		ControlsUpdater.updateJpaActionDetailsControl(model, mapTexts, mapCombos, mapCheckBoxes);
		revalidate();
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		JpaActionsModel actionsModel = ((ActionsMasterBlock) master).getActionsModel();
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateJpaActionModel(getEntity(), model, actionsModel, key, (String) map.get(Constants.TEXT_VALUE),
				(Boolean) map.get(Constants.BOOL_VALUE), (String) map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));

	}

	@Override
	protected void afterDoUpdateModel() {
		setDirty(getEntity().isDirty());
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			model = (ActionModel) selection.getFirstElement();
		} else {
			model = null;
		}
	}

	private static String[] getJpaActions() {
		List<String> list = new ArrayList<String>();
		list.add(DbActions.READ.class.getSimpleName());
		list.add(DbActions.CREATE.class.getSimpleName());
		list.add(DbActions.DELETE.class.getSimpleName());
		list.add(DbActions.UPDATE.class.getSimpleName());
		return list.toArray(new String[] {});
	}

	private boolean validateAction(ComboValidator validator, UUID uuid) {
		boolean isValid = true;
		if (StringUtils.isEmpty(validator.getControl().getText())) {
			validator.addMessage(Messages.getString("validation.field.should.be.specified"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
			isValid = false;
		}
		return isValid;
	}

	private boolean validateTargetEntity(TextValidator validator, UUID uuid) {
		boolean isValid = true;
		String text = validator.getControl().getText();
		String fullyQuailifiedName = (String) validator.getControl().getData(FormRowCreator.ID_FULLY_QUALIFIED_NAME);
		if (StringUtils.isEmpty(fullyQuailifiedName) || StringUtils.isEmpty(text)) {
			return isValid;
		}
		boolean isJpaEntity = false;
		try {
			Class<?> clazz = Utils.getClazz(fullyQuailifiedName);
			if (clazz != null) {
				for (Annotation annotation : clazz.getDeclaredAnnotations()) {
					if (annotation.annotationType().getName().equals(Entity.class.getName())) {
						isJpaEntity = true;
						break;
					}
				}
			} else {
				// add validation marker
				String message = MessageFormat.format("Target entity: {0} \n {1}", fullyQuailifiedName,//$NON-NLS-1$
						Messages.getString("validation.could.not.load.selected.class"));//$NON-NLS-1$
				validator.addMessage(message, IMessageProvider.ERROR, uuid);
				return false;
			}
		} catch (MalformedURLException e) {
		} catch (CoreException e) {
		}
		if (!isJpaEntity && !StringUtils.equals(fullyQuailifiedName, void.class.getName())) {
			isValid = false;
			// add validation marker
			String message = MessageFormat.format("Target entity: {0} \n {1}", fullyQuailifiedName,//$NON-NLS-1$
					Messages.getString("validation.selected.class.is.not.jpa.entity"));//$NON-NLS-1$
			validator.addMessage(message, IMessageProvider.ERROR, uuid);
		}
		return isValid;
	}

	private SelectionListener getTargetEntityClearListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				model.setTargetEntityDefaultValue();
				targetEntityControl.setData(FormRowCreator.ID_FULLY_QUALIFIED_NAME, model.getTargetEntity().getName());
				targetEntityControl.setText(model.getTargetEntityClassName());
				try {
					JpaActionsModel actionsModel = ((ActionsMasterBlock) master).getActionsModel();
					ModelUpdater.updateJpaActionsModel(getEntity(), actionsModel);
					setDirty(getEntity().isDirty());
				} catch (Exception ex) {
					ErrorDialog.openError(((IEditorPart) managedForm.getContainer()).getSite().getShell(),
							Messages.getString("error.problem.occurred"), ex.getMessage(), new Status(IStatus.ERROR,
									Activator.PLUGIN_ID, ex.getMessage()));
				}
			}

		};
	}

}

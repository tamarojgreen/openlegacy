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
package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenActionsModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenNamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen.ActionsMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.ComboValidator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.Utils;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.annotations.screen.Action.ActionType;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 *
 */
public class ActionsActionDetailsPage extends AbstractScreenDetailsPage {

	private ActionModel model;
	private TextValidator rowControlValidator;
	private TextValidator columnControlValidator;
	private TextValidator lengthControlValidator;
	private TextValidator whenControlValidator;
	private TextValidator targetEntityControlValidator;
	private ComboValidator actionComboValidator;

	public ActionsActionDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	public void revalidate() {
		if (rowControlValidator != null) {
			rowControlValidator.revalidate(getModelUUID());
			columnControlValidator.revalidate(getModelUUID());
			lengthControlValidator.revalidate(getModelUUID());
			whenControlValidator.revalidate(getModelUUID());
			targetEntityControlValidator.revalidate(getModelUUID());
			actionComboValidator.revalidate(getModelUUID());
		}
	}

	@Override
	protected void updateValidators(UUID uuid) {
		super.updateValidators(uuid);
		if (uuid != null) {
			rowControlValidator.setModelUUID(uuid);
			columnControlValidator.setModelUUID(uuid);
			lengthControlValidator.setModelUUID(uuid);
			whenControlValidator.setModelUUID(uuid);
			targetEntityControlValidator.setModelUUID(uuid);
			actionComboValidator.setModelUUID(uuid);
		}
	}

	@Override
	public void removeValidationMarkers() {
		super.removeValidationMarkers();
		if (rowControlValidator != null) {
			rowControlValidator.removeValidationMarker();
			columnControlValidator.removeValidationMarker();
			lengthControlValidator.removeValidationMarker();
			whenControlValidator.removeValidationMarker();
			targetEntityControlValidator.removeValidationMarker();
			actionComboValidator.removeValidationMarker();
		}
	}

	@Override
	public ScreenNamedObject getPageScreenNamedObject() {
		return null;
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
		section.setText(Messages.getString("ScreenActionDetailsPage.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("ScreenActionDetailsPage.desc")); //$NON-NLS-1$
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
				getDefaultComboBoxKeyListener(),
				Messages.getString("ActionsPage.col.action"), getTerminalActions(), 0, ScreenAnnotationConstants.ACTION, false,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "Action", "action");//$NON-NLS-1$ //$NON-NLS-2$
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
		// "additionalKey" row - combo
		FormRowCreator.createComboBoxRow(toolkit, client, mapCombos, getDefaultModifyListener(), getDefaultComboBoxKeyListener(),
				Messages.getString("ActionsPage.col.additionalKey"), getAdditionalKeys(), 0,//$NON-NLS-1$
				ScreenAnnotationConstants.ADDITIONAL_KEY, false, JAVA_DOCUMENTATION_TYPE.SCREEN, "Action", "additionalKey");//$NON-NLS-1$ //$NON-NLS-2$
		// "displayName" row - string
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ActionsPage.col.displayName"), "", AnnotationConstants.DISPLAY_NAME,//$NON-NLS-1$ //$NON-NLS-2$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "Action", "displayName");//$NON-NLS-1$ //$NON-NLS-2$
		// "alias" row - string
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ActionsPage.col.alias"), "", AnnotationConstants.ALIAS, JAVA_DOCUMENTATION_TYPE.SCREEN,//$NON-NLS-1$ //$NON-NLS-2$
				"Action", "alias");//$NON-NLS-1$ //$NON-NLS-2$
		// "row" row - int
		Text rowControl = FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(),
				Messages.getString("ActionsPage.col.row"), 0, ScreenAnnotationConstants.ROW, JAVA_DOCUMENTATION_TYPE.SCREEN,//$NON-NLS-1$
				"Action", "row");
		rowControlValidator = new TextValidator(master, managedForm, rowControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateAttributes(validator, uuid) && validateRows(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return model;
			}
		};
		// "column" row - int
		Text columnControl = FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(), Messages.getString("ActionsPage.col.column"), 0, ScreenAnnotationConstants.COLUMN,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "Action", "column");
		columnControlValidator = new TextValidator(master, managedForm, columnControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateAttributes(validator, uuid) && validateColumns(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return model;
			}
		};
		// "length" row - int
		Text lengthControl = FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(),
				Messages.getString("ActionsPage.col.length"), 0, AnnotationConstants.LENGTH, JAVA_DOCUMENTATION_TYPE.SCREEN,//$NON-NLS-1$
				"Action", "length");
		lengthControlValidator = new TextValidator(master, managedForm, lengthControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateAttributes(validator, uuid) && validateColumns(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return model;
			}
		};
		// "when" row - string
		Text whenControl = FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ActionsPage.col.when"), "", AnnotationConstants.WHEN, JAVA_DOCUMENTATION_TYPE.SCREEN,//$NON-NLS-1$ //$NON-NLS-2$
				"Action", "when");
		whenControlValidator = new TextValidator(master, managedForm, whenControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateAttributes(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return model;
			}
		};
		// "focusField" row - string
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ActionsPage.col.focusField"), "", ScreenAnnotationConstants.FOCUS_FIELD,//$NON-NLS-1$ //$NON-NLS-2$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "Action", "focusField");//$NON-NLS-1$ //$NON-NLS-2$
		// "type" row - combo
		FormRowCreator.createComboBoxRow(toolkit, client, mapCombos, getDefaultModifyListener(), getDefaultComboBoxKeyListener(),
				Messages.getString("ActionsPage.col.type"), getActionTypes(), 0, ScreenAnnotationConstants.TYPE, false,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "Action", "type");//$NON-NLS-1$ //$NON-NLS-2$
		// "targetEntity" row - browse
		Text targetEntityControl = FormRowCreator.createStringRowWithBrowseButton(toolkit, client, mapTexts,
				getDefaultModifyListener(),
				Messages.getString("ActionsPage.col.targetEntity"), "", AnnotationConstants.TARGET_ENTITY, null,//$NON-NLS-1$ //$NON-NLS-2$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "Action", "targetEntity");//$NON-NLS-1$ //$NON-NLS-2$
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
		// "sleep" row - int
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("ActionsPage.col.sleep"), 0, ScreenAnnotationConstants.SLEEP,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "Action", "sleep");//$NON-NLS-1$ //$NON-NLS-2$
		// "global" row - boolean
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ActionsPage.col.global"), false, ScreenAnnotationConstants.GLOBAL,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "Action", "global");//$NON-NLS-1$ //$NON-NLS-2$
		// "keyboardKey" row - combo
		FormRowCreator.createComboBoxRow(toolkit, client, mapCombos, getDefaultModifyListener(), getDefaultComboBoxKeyListener(),
				Messages.getString("ActionsPage.col.keyboardKey"), getTerminalActions(), 0, AnnotationConstants.KEYBOARD_KEY,//$NON-NLS-1$
				false, JAVA_DOCUMENTATION_TYPE.SCREEN, "Action", "keyboardKey");//$NON-NLS-1$ //$NON-NLS-2$

		toolkit.paintBordersFor(section);
		section.setClient(topClient);

	}

	@Override
	public UUID getModelUUID() {
		return model != null ? model.getUUID() : null;
	}

	@Override
	protected void updateControls() {
		ControlsUpdater.updateScreenActionDetailsControls(model, mapTexts, mapCombos, mapCheckBoxes);
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		ScreenActionsModel actionsModel = ((ActionsMasterBlock) master).getActionsModel();
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateScreenActionModel(getEntity(), model, actionsModel, key, (String) map.get(Constants.TEXT_VALUE),
				(Boolean) map.get(Constants.BOOL_VALUE), (String) map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
		// for Row,Column,Length and When we must revalidate them after user interaction
		revalidate();
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

	private static String[] getTerminalActions() {
		List<String> list = new ArrayList<String>();
		list.add(TerminalActions.NONE.class.getSimpleName());
		list.add(TerminalActions.NULL.class.getSimpleName());
		list.add(TerminalActions.ENTER.class.getSimpleName());
		list.add(TerminalActions.ESCAPE.class.getSimpleName());
		list.add(TerminalActions.PAGE_DOWN.class.getSimpleName());
		list.add(TerminalActions.PAGE_UP.class.getSimpleName());
		list.add(TerminalActions.F1.class.getSimpleName());
		list.add(TerminalActions.F2.class.getSimpleName());
		list.add(TerminalActions.F3.class.getSimpleName());
		list.add(TerminalActions.F4.class.getSimpleName());
		list.add(TerminalActions.F5.class.getSimpleName());
		list.add(TerminalActions.F6.class.getSimpleName());
		list.add(TerminalActions.F7.class.getSimpleName());
		list.add(TerminalActions.F8.class.getSimpleName());
		list.add(TerminalActions.F9.class.getSimpleName());
		list.add(TerminalActions.F10.class.getSimpleName());
		list.add(TerminalActions.F11.class.getSimpleName());
		list.add(TerminalActions.F12.class.getSimpleName());
		return list.toArray(new String[] {});
	}

	private static String[] getAdditionalKeys() {
		List<String> items = new ArrayList<String>();
		items.add(AdditionalKey.NONE.toString());
		items.add(AdditionalKey.SHIFT.toString());
		items.add(AdditionalKey.CTRL.toString());
		items.add(AdditionalKey.ALT.toString());
		return items.toArray(new String[] {});
	}

	private static String[] getActionTypes() {
		List<String> items = new ArrayList<String>();
		items.add(ActionType.GENERAL.toString());
		items.add(ActionType.LOGICAL.toString());
		items.add(ActionType.NAVIGATION.toString());
		items.add(ActionType.WINDOW.toString());
		return items.toArray(new String[] {});
	}

	// refs #599 - Row, Column, Length and When attributes must be set together.
	// If one of them is missing, should be an
	// appropriate error.
	private boolean validateAttributes(TextValidator validator, UUID uuid) {
		boolean isValid = true;
		String controlIdKey = (String) validator.getControl().getData(FormRowCreator.ID_KEY);
		String text = validator.getControl().getText();
		int textIntVal = StringUtils.isNumeric(text) && !StringUtils.isEmpty(text) ? Integer.valueOf(text) : 0;

		if (controlIdKey.equals(ScreenAnnotationConstants.ROW)) {
			isValid = (textIntVal == 0 && model.getColumn() == 0 && model.getLength() == 0 && StringUtils.isEmpty(model.getWhen()))
					|| (textIntVal != 0 && model.getColumn() != 0 && model.getLength() != 0 && !StringUtils.isEmpty(model.getWhen()));
		} else if (controlIdKey.equals(ScreenAnnotationConstants.COLUMN)) {
			isValid = (textIntVal == 0 && model.getRow() == 0 && model.getLength() == 0 && StringUtils.isEmpty(model.getWhen()))
					|| (textIntVal != 0 && model.getRow() != 0 && model.getLength() != 0 && !StringUtils.isEmpty(model.getWhen()));
		} else if (controlIdKey.equals(ScreenAnnotationConstants.LENGTH)) {
			isValid = (textIntVal == 0 && model.getColumn() == 0 && model.getRow() == 0 && StringUtils.isEmpty(model.getWhen()))
					|| (textIntVal != 0 && model.getColumn() != 0 && model.getRow() != 0 && !StringUtils.isEmpty(model.getWhen()));
		} else if (controlIdKey.equals(ScreenAnnotationConstants.WHEN)) {
			isValid = (StringUtils.isBlank(text) && model.getRow() == 0 && model.getColumn() == 0 && model.getLength() == 0)
					|| (!StringUtils.isBlank(text) && model.getRow() != 0 && model.getColumn() != 0 && model.getLength() != 0);
		}

		if (!isValid) {
			validator.addMessage(Messages.getString("validation.action.attributes.must.be.specified.together"),
					IMessageProvider.ERROR, uuid);
		}

		return isValid;
	}

	private boolean validateRows(TextValidator validator, UUID uuid) {
		boolean isValid = true;
		String text = validator.getControl().getText();

		ScreenEntityModel entityModel = getEntity().getEntityModel();

		if (!StringUtils.isEmpty(text)) {
			int row = Integer.valueOf(text);
			if (row > entityModel.getRows()) {
				isValid = false;
				validator.addMessage(Messages.getString("validation.action.exceeds.screen.bounds"), IMessageProvider.ERROR, uuid);
			}
		}
		return isValid;
	}

	private boolean validateColumns(TextValidator validator, UUID uuid) {
		boolean isValid = true;
		String controlIdKey = (String) validator.getControl().getData(FormRowCreator.ID_KEY);
		String text = validator.getControl().getText();

		ScreenEntityModel entityModel = getEntity().getEntityModel();

		if (controlIdKey.equals(ScreenAnnotationConstants.COLUMN)) {
			int column = !StringUtils.isEmpty(text) ? Integer.valueOf(text) : 0;
			if (column + model.getLength() > entityModel.getColumns()) {
				validator.addMessage(Messages.getString("validation.action.exceeds.screen.bounds"), IMessageProvider.ERROR, uuid);
				isValid = false;
			}
		} else if (controlIdKey.equals(ScreenAnnotationConstants.LENGTH)) {
			int length = !StringUtils.isEmpty(text) ? Integer.valueOf(text) : 0;
			if (length + model.getColumn() > entityModel.getColumns()) {
				validator.addMessage(Messages.getString("validation.action.exceeds.screen.bounds"), IMessageProvider.ERROR, uuid);
				isValid = false;
			}
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
		boolean isScreenEntity = false;
		try {
			Class<?> clazz = Utils.getClazz(fullyQuailifiedName);
			if (clazz != null) {
				for (Annotation annotation : clazz.getDeclaredAnnotations()) {
					if (annotation.annotationType().getName().equals(ScreenEntity.class.getName())) {
						isScreenEntity = true;
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
		if (!isScreenEntity
				&& !StringUtils.equals(fullyQuailifiedName, org.openlegacy.terminal.ScreenEntity.NONE.class.getName())) {
			isValid = false;
			// add validation marker
			String message = MessageFormat.format("Target entity: {0} \n {1}", fullyQuailifiedName,//$NON-NLS-1$
					Messages.getString("validation.selected.class.is.not.screen.entity"));//$NON-NLS-1$
			validator.addMessage(message, IMessageProvider.ERROR, uuid);
		}
		return isValid;
	}

	private boolean validateAction(ComboValidator validator, UUID uuid) {
		boolean isValid = true;
		if (StringUtils.isEmpty(validator.getControl().getText())) {
			validator.addMessage(Messages.getString("validation.field.should.be.specified"), IMessageProvider.ERROR, uuid);
			isValid = false;
		}
		return isValid;
	}

}

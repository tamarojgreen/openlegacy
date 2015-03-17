package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.openlegacy.designtime.generators.AnnotationConstants;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsScreenDateFieldDetailsPage extends AbstractScreenFieldDetailsPage {

	private ScreenDateFieldModel fieldModel;

	public FieldsScreenDateFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	public Class<?> getDetailsModel() {
		return ScreenDateFieldModel.class;
	}

	@Override
	protected void addContent(FormToolkit toolkit, Composite client) {

		// create row for "yearColumn"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("ScreenDateField.yearColumn"), 0, AnnotationConstants.YEAR_COLUMN,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenDateField", AnnotationConstants.YEAR_COLUMN);//$NON-NLS-1$

		// create row for "monthColumn"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("ScreenDateField.monthColumn"), 0, AnnotationConstants.MONTH_COLUMN,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenDateField", AnnotationConstants.MONTH_COLUMN);//$NON-NLS-1$
		// create row for "dayColumn"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("ScreenDateField.dayColumn"), 0, AnnotationConstants.DAY_COLUMN,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenDateField", AnnotationConstants.DAY_COLUMN);//$NON-NLS-1$
		// create row for "patter"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenDateField.pattern"), "", AnnotationConstants.PATTERN, JAVA_DOCUMENTATION_TYPE.SCREEN,//$NON-NLS-1$ //$NON-NLS-2$
				"ScreenDateField", AnnotationConstants.PATTERN);//$NON-NLS-1$
		// create description section
		addScreenDecriptionFieldSection(toolkit, client);
		addScreenDyamicFieldSection(toolkit, client);
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateScreenFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE), (String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
		ModelUpdater.updateScreenDateFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE));
		ModelUpdater.updateScreenDescriptionFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE));
		ModelUpdater.updateScreenDynamicFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE));
	}

	@Override
	protected void afterDoUpdateModel() {
		setScreenPreviewDrawingRectangle(fieldModel);
		setDirty(getEntity().isDirty());
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
		ControlsUpdater.updateScreenFieldDetailsControls(fieldModel, mapTexts, mapCombos, mapCheckBoxes, mapLabels);
		ControlsUpdater.updateScreenDateFieldDetailsControls(fieldModel, mapTexts, mapCheckBoxes);
		ControlsUpdater.updateScreenDescriptionFieldDetailsControls(fieldModel.getDescriptionFieldModel(), mapTexts);
		ControlsUpdater.updateScreenDynamicFieldDetailsControls(fieldModel.getDynamicFieldModel(), mapTexts);
		revalidate();
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			fieldModel = (ScreenDateFieldModel)selection.getFirstElement();
		} else {
			fieldModel = null;
		}

		if (fieldModel != null) {
			// try to draw rectangle of selected field in ScreenPreview
			setScreenPreviewDrawingRectangle(fieldModel);
		}
	}

	@Override
	protected ScreenFieldModel getFieldModel() {
		return fieldModel;
	}
}

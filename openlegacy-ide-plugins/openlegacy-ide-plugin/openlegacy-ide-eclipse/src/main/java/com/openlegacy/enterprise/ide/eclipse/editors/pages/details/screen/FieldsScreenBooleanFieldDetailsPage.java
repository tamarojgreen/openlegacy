package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.openlegacy.designtime.generators.AnnotationConstants;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsScreenBooleanFieldDetailsPage extends AbstractScreenFieldDetailsPage {

	private ScreenBooleanFieldModel fieldModel;

	public FieldsScreenBooleanFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	public Class<?> getDetailsModel() {
		return ScreenBooleanFieldModel.class;
	}

	@Override
	protected void addContent(FormToolkit toolkit, Composite client) {
		// create row for "trueValue"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenBooleanField.trueValue"),//$NON-NLS-1$
				"", AnnotationConstants.TRUE_VALUE, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenBooleanField");//$NON-NLS-1$
		// create row for "falseValue"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenBooleanField.falseValue"),//$NON-NLS-1$
				"", AnnotationConstants.FALSE_VALUE, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenBooleanField");//$NON-NLS-1$
		// create row for "treatEmptyIsNull"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenBooleanField.treatEmptyAsNull"), false, AnnotationConstants.TREAT_EMPTY_AS_NULL, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenBooleanField");//$NON-NLS-1$
		
		addScreenDyamicFieldSection(toolkit, client);
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateScreenFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE), (String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
		ModelUpdater.updateScreenBooleanFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE));
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
		ControlsUpdater.updateScreenBooleanFieldDetailsControls(fieldModel, mapTexts, mapCheckBoxes);
		ControlsUpdater.updateScreenDynamicFieldDetailsControls(fieldModel.getDynamicFieldModel(), mapTexts);
		revalidate();
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			fieldModel = (ScreenBooleanFieldModel)selection.getFirstElement();
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

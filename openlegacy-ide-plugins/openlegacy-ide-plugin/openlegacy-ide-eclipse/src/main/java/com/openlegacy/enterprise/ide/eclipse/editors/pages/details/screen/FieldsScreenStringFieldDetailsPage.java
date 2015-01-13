package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsScreenStringFieldDetailsPage extends AbstractScreenFieldDetailsPage {

	private ScreenFieldModel fieldModel;

	public FieldsScreenStringFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	protected void addContent(FormToolkit toolkit, Composite client) {
		addScreenDecriptionFieldSection(toolkit, client);
	}

	@Override
	public Class<?> getDetailsModel() {
		return ScreenFieldModel.class;
	}

	@Override
	public void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			fieldModel = (ScreenFieldModel)selection.getFirstElement();
		} else {
			fieldModel = null;
		}

		if (fieldModel != null) {
			// try to draw rectangle of selected field in ScreenPreview
			setScreenPreviewDrawingRectangle(fieldModel);
		}
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateScreenFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE), (String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
		ModelUpdater.updateScreenDescriptionFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE));
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
		ControlsUpdater.updateScreenDescriptionFieldDetailsControls(fieldModel.getDescriptionFieldModel(), mapTexts);
		revalidate();
	}

	@Override
	protected ScreenFieldModel getFieldModel() {
		return fieldModel;
	}

}

package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsScreenIntegerFieldDetailsPage extends AbstractScreenFieldDetailsPage {

	private ScreenIntegerFieldModel fieldModel;

	public FieldsScreenIntegerFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	public Class<?> getDetailsModel() {
		return ScreenIntegerFieldModel.class;
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
		ControlsUpdater.updateScreenIntegerFieldDetailsControls(fieldModel, mapTexts, mapCheckBoxes);
		ControlsUpdater.updateScreenDescriptionFieldDetailsControls(fieldModel.getDescriptionFieldModel(), mapTexts);
		ControlsUpdater.updateScreenDynamicFieldDetailsControls(fieldModel.getDynamicFieldModel(), mapTexts);
		revalidate();
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			fieldModel = (ScreenIntegerFieldModel)selection.getFirstElement();
		} else {
			fieldModel = null;
		}

		if (fieldModel != null) {
			// try to draw rectangle of selected field in ScreenPreview
			setScreenPreviewDrawingRectangle(fieldModel);
		}
	}

	@Override
	protected void addContent(FormToolkit toolkit, Composite client) {
		// create row for "pattern"
		Text patternControl = FormRowCreator.createStringRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				Messages.getString("ScreenIntegerField.pattern"), "", ScreenAnnotationConstants.NUMERIC_PATTERN, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenIntegerField", ScreenAnnotationConstants.NUMERIC_PATTERN);//$NON-NLS-1$ $NON-NLS-2$

		patternControl.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent event) {
				if (updatingControls) {
					return;
				}

				String widgetText = ((Text)event.widget).getText();
				String newText = widgetText.substring(0, event.start) + event.character + widgetText.substring(event.start);
				if (!newText.matches("^(?!\\.)(?!,)[#]*[\\.,]{0,1}[#]*$") && event.keyCode != SWT.BS && event.keyCode != SWT.DEL) {
					event.doit = false;
				}
			}
		});

		addScreenDecriptionFieldSection(toolkit, client);
		addScreenDyamicFieldSection(toolkit, client);
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateScreenFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE), (String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
		ModelUpdater.updateScreenIntegerFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE));
		ModelUpdater.updateScreenDescriptionFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE));
		ModelUpdater.updateScreenDynamicFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE));
	}

	@Override
	protected void afterDoUpdateModel() {
		setScreenPreviewDrawingRectangle(fieldModel);
		setDirty(getEntity().isDirty());
	}

	@Override
	protected ScreenFieldModel getFieldModel() {
		return fieldModel;
	}

}

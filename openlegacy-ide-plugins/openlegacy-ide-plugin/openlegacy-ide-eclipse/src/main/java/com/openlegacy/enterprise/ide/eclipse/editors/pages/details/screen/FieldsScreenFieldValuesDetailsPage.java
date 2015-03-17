package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.ProviderViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.TerminalActionViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldValuesModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.terminal.actions.TerminalActions;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsScreenFieldValuesDetailsPage extends AbstractScreenFieldDetailsPage {

	private ScreenFieldValuesModel fieldModel;
	private TextValidator sourceValidator;
	private CCombo autoSubmitActionCombo = null;

	public FieldsScreenFieldValuesDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	public UUID getModelUUID() {
		return fieldModel != null ? fieldModel.getUUID() : null;
	}

	@Override
	protected void updateControls() {
		if (fieldModel == null) {
			return;
		} else {
			sourceValidator.setModelUUID(fieldModel.getUUID());
		}
		ControlsUpdater.updateScreenFieldDetailsControls(fieldModel, mapTexts, mapCombos, mapCheckBoxes, mapLabels);
		ControlsUpdater.updateScreenFieldValuesDetailsControls(fieldModel, mapTexts, mapCheckBoxes, mapCombos);
		ControlsUpdater.updateScreenDescriptionFieldDetailsControls(fieldModel.getDescriptionFieldModel(), mapTexts);
		ControlsUpdater.updateScreenDynamicFieldDetailsControls(fieldModel.getDynamicFieldModel(), mapTexts);
		revalidate();
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			fieldModel = (ScreenFieldValuesModel)selection.getFirstElement();
		} else {
			fieldModel = null;
		}
		if (fieldModel != null) {
			// try to draw rectangle of selected field in ScreenPreview
			setScreenPreviewDrawingRectangle(fieldModel);
			sourceValidator.setModelUUID(fieldModel.getUUID());
		}
	}

	@Override
	public Class<?> getDetailsModel() {
		return ScreenFieldValuesModel.class;
	}

	@Override
	protected void addContent(FormToolkit toolkit, Composite client) {
		// create row for 'sourceScreenEntity'
		Text sourceControl = FormRowCreator.createStringRowWithBrowseButton(toolkit, client, mapTexts,
				getDefaultModifyListener(), Messages.getString("ScreenFieldValues.source"), " ",//$NON-NLS-1$ //$NON-NLS-2$
				ScreenAnnotationConstants.SOURCE_SCREEN_ENTITY, null, JAVA_DOCUMENTATION_TYPE.SCREEN, " ScreenFieldValues",//$NON-NLS-1$
				ScreenAnnotationConstants.SOURCE_SCREEN_ENTITY);
		sourceValidator = new TextValidator(master, managedForm, sourceControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateSourceControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return getFieldModel();
			}

		};
		// create row for 'colleactAll'
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenFieldValues.collectAll"), false, ScreenAnnotationConstants.COLLECT_ALL,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenFieldValues", ScreenAnnotationConstants.COLLECT_ALL);//$NON-NLS-1$
		// create row for 'provider'
		FormRowCreator.createStringRowWithBrowseButton(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreemFieldValues.provider"), "", AnnotationConstants.PROVIDER, new ProviderViewerFilter(),//$NON-NLS-1$ //$NON-NLS-2$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenFieldValues", AnnotationConstants.PROVIDER);//$NON-NLS-1$
		// create row for 'asWindow'
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenFieldValues.asWindow"), false, ScreenAnnotationConstants.AS_WINDOW,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenFieldValues", ScreenAnnotationConstants.AS_WINDOW);//$NON-NLS-1$
		// create row for 'autoSubmitAction'
		autoSubmitActionCombo = FormRowCreator.createComboBoxRowWithBrowseButton(toolkit, client, mapCombos,
				getDefaultModifyListener(), getDefaultComboBoxKeyListener(),
				Messages.getString("ScreenFieldValues.autoSubmitAction"), getTerminalActions(), 0,//$NON-NLS-1$
				ScreenAnnotationConstants.AUTO_SUBMIT_ACTION, new TerminalActionViewerFilter(), true,
				getAutoSubmitActionClearListener(), JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenFieldValues",//$NON-NLS-1$
				ScreenAnnotationConstants.AUTO_SUBMIT_ACTION);
		// create row for 'displayFieldName'
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenFieldValues.displayFieldName"), "", ScreenAnnotationConstants.DISPLAY_FIELD_NAME,//$NON-NLS-1$ //$NON-NLS-2$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenFieldValues", ScreenAnnotationConstants.DISPLAY_FIELD_NAME);//$NON-NLS-1$
		// create row for 'searchField'
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenFieldValues.searchField"), "", ScreenAnnotationConstants.SEARCH_FIELD,//$NON-NLS-1$ //$NON-NLS-2$
				JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenFieldValues", ScreenAnnotationConstants.SEARCH_FIELD);//$NON-NLS-1$
		// create description section
		addScreenDecriptionFieldSection(toolkit, client);
		addScreenDyamicFieldSection(toolkit, client);
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateScreenFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE), (String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
		ModelUpdater.updateScreenFieldValuesModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE), (String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
		ModelUpdater.updateScreenDescriptionFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE));
		ModelUpdater.updateScreenDynamicFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE));
	}

	@Override
	protected void afterDoUpdateModel() {
		setScreenPreviewDrawingRectangle(fieldModel);
		setDirty(getEntity().isDirty());
	}

	private static boolean validateSourceControl(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		boolean isValid = !text.isEmpty();
		if (!isValid) {
			validator.addMessage(Messages.getString("validation.field.should.be.specified"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
		}
		return isValid;
	}

	@Override
	public void removeValidationMarkers() {
		super.removeValidationMarkers();
		if (sourceValidator != null) {
			sourceValidator.removeValidationMarker();
		}
	}

	@Override
	public void revalidate() {
		super.revalidate();
		if (sourceValidator != null) {
			sourceValidator.revalidate(getModelUUID());
		}
	}

	@Override
	protected ScreenFieldModel getFieldModel() {
		return fieldModel;
	}

	private static String[] getTerminalActions() {
		List<String> list = new ArrayList<String>();
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

	private SelectionListener getAutoSubmitActionClearListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				autoSubmitActionCombo.setText("");//$NON-NLS-1$
				try {
					ModelUpdater.updateScreenFieldValuesModel(getEntity(), fieldModel,
							ScreenAnnotationConstants.AUTO_SUBMIT_ACTION, "", false, null);
					setDirty(getEntity().isDirty());
				} catch (Exception ex) {
					ErrorDialog.openError(((IEditorPart)managedForm.getContainer()).getSite().getShell(),
							Messages.getString("error.problem.occurred"), ex.getMessage(), new Status(//$NON-NLS-1$
									IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage()));
				}
			}
		};
	}

}

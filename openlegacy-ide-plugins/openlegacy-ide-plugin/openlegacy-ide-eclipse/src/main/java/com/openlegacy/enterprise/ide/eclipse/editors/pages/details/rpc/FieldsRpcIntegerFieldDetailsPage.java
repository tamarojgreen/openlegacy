package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsRpcIntegerFieldDetailsPage extends AbstractRpcFieldDetailsPage {

	private RpcIntegerFieldModel fieldModel;
	private TextValidator minValueValidator;
	private TextValidator maxValueValidator;

	public FieldsRpcIntegerFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc.AbstractRpcFieldDetailsPage#addContent(org.eclipse.ui.forms
	 * .widgets.FormToolkit, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addContent(FormToolkit toolkit, Composite client) {
		// create row for "minimumValue"
		Text minValueControl = FormRowCreator.createDoubleRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultDoubleVerifyListener(), Messages.getString("rpc.field.integer.minimum.value.label"), 0.0,//$NON-NLS-1$
				RpcAnnotationConstants.MINIMUM_VALUE);
		minValueValidator = new TextValidator(master, managedForm, minValueControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateMinMaxValueControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return getFieldModel();
			}

		};
		// create row for "maximumValue"
		Text maxValueControl = FormRowCreator.createDoubleRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultDoubleVerifyListener(), Messages.getString("rpc.field.integer.maximum.value.label"), 0.0,//$NON-NLS-1$
				RpcAnnotationConstants.MAXIMUM_VALUE);
		maxValueValidator = new TextValidator(master, managedForm, maxValueControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateMinMaxValueControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return getFieldModel();
			}

		};
		// create row for "decimalPlaces"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("rpc.field.integer.decimal.places.label"), 0, RpcAnnotationConstants.DECIMAL_PLACES);//$NON-NLS-1$ 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#getDetailsModel()
	 */
	@Override
	public Class<?> getDetailsModel() {
		return RpcIntegerFieldModel.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#getModelUUID()
	 */
	@Override
	public UUID getModelUUID() {
		return fieldModel != null ? fieldModel.getUUID() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#updateControls()
	 */
	@Override
	protected void updateControls() {
		if (fieldModel == null) {
			return;
		}
		ControlsUpdater.updateRpcFieldDetailsControls(fieldModel, mapTexts, mapCombos, mapCheckBoxes, mapLabels);
		ControlsUpdater.updateRpcIntegerFieldDetailsControls(fieldModel, mapTexts);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#doUpdateModel(java.lang.String)
	 */
	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateRpcFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE), (String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
		ModelUpdater.updateRpcIntegerFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE));
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
			fieldModel = (RpcIntegerFieldModel)selection.getFirstElement();
		} else {
			fieldModel = null;
		}
	}

	@Override
	public void removeValidationMarkers() {
		super.removeValidationMarkers();
		if (minValueValidator != null) {
			minValueValidator.removeValidationMarker();
			maxValueValidator.removeValidationMarker();
		}
	}

	@Override
	public void revalidate() {
		super.revalidate();
		if (minValueValidator != null) {
			minValueValidator.revalidate(getModelUUID());
			maxValueValidator.revalidate(getModelUUID());
		}
	}

	@Override
	protected void updateValidators(UUID uuid) {
		super.updateValidators(uuid);
		if (uuid != null) {
			minValueValidator.setModelUUID(uuid);
			maxValueValidator.setModelUUID(uuid);
		}
	}

	@Override
	protected RpcFieldModel getFieldModel() {
		return fieldModel;
	}

	private static boolean validateMinMaxValueControl(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		boolean isValid = true;
		if (!text.isEmpty()) {
			try {
				Double.parseDouble(text);
			} catch (NumberFormatException e) {
				validator.addMessage(Messages.getString("validation.double.value.parse.error"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
				isValid = false;
			}
		}
		return isValid;
	}
}

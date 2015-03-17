package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ModelUpdater;

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
public class FieldsJpaIntegerFieldDetailsPage extends AbstractJpaFieldDetailsPage {

	private JpaIntegerFieldModel fieldModel;

	public FieldsJpaIntegerFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	protected JpaFieldModel getFieldModel() {
		return fieldModel;
	}

	@Override
	protected void addTopContent(FormToolkit toolkit, Composite client) {
		// create row for displaying java type name
		FormRowCreator.createLabelRow(toolkit, client, mapLabels, Messages.getString("jpa.field.java.type"), "",//$NON-NLS-1$ //$NON-NLS-2$
				Constants.JAVA_TYPE_NAME, JAVA_DOCUMENTATION_TYPE.JAVA_BASICS, "datatypes", "");//$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected void addBottomContent(FormToolkit toolkit, Composite client) {}

	@Override
	public Class<?> getDetailsModel() {
		return JpaIntegerFieldModel.class;
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
		revalidate();
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateJpaFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE));
	}

	@Override
	protected void afterDoUpdateModel() {
		setDirty(getEntity().isDirty());
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			fieldModel = (JpaIntegerFieldModel)selection.getFirstElement();
		} else {
			fieldModel = null;
		}
	}

}

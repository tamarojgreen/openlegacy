package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
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
public class FieldsJpaDateFieldDetailsPage extends AbstractJpaFieldDetailsPage {

	private JpaDateFieldModel fieldModel;

	public FieldsJpaDateFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	protected JpaFieldModel getFieldModel() {
		return fieldModel;
	}

	@Override
	protected void addContent(FormToolkit toolkit, Composite client) {}

	@Override
	public Class<?> getDetailsModel() {
		return JpaDateFieldModel.class;
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
			fieldModel = (JpaDateFieldModel)selection.getFirstElement();
		} else {
			fieldModel = null;
		}
	}

	@Override
	protected boolean isAddManyToOneSection() {
		return true;
	}

}

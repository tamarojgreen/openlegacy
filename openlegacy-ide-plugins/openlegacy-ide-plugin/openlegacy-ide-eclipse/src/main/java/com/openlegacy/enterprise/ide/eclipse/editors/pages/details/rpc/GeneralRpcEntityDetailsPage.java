package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ModelUpdater;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class GeneralRpcEntityDetailsPage extends AbstractRpcDetailsPage {

	private RpcEntityModel entityModel;

	public GeneralRpcEntityDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#getDetailsModel()
	 */
	@Override
	public Class<?> getDetailsModel() {
		return RpcEntityModel.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#createContents(org.eclipse.swt.widgets.Composite)
	 */
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
		section.setText(Messages.getString("rpc.general.page.details.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("rpc.general.page.details.desc")); //$NON-NLS-1$
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		section.setLayoutData(td);

		Composite client = toolkit.createComposite(section);
		GridLayout glayout = new GridLayout();
		glayout.marginWidth = glayout.marginHeight = 0;
		glayout.numColumns = 2;
		client.setLayout(glayout);

		// create empty row
		FormRowCreator.createSpacer(toolkit, client, 2);
		// create row for "name"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("rpc.entity.name"), "", AnnotationConstants.NAME, JAVA_DOCUMENTATION_TYPE.RPC, "RpcEntity",//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				AnnotationConstants.NAME);
		// crate row for "displayName"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("rpc.entity.display.name"), "", AnnotationConstants.DISPLAY_NAME, JAVA_DOCUMENTATION_TYPE.RPC,//$NON-NLS-1$ //$NON-NLS-2$
				"RpcEntity", AnnotationConstants.DISPLAY_NAME);//$NON-NLS-1$
		// crate row for "language"
		FormRowCreator.createComboBoxRow(toolkit, client, mapCombos, getDefaultModifyListener(), getDefaultComboBoxKeyListener(),
				Messages.getString("rpc.entity.language"), getLanguagesItems(), 0, RpcAnnotationConstants.LANGUAGE, false,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.RPC, "RpcEntity", RpcAnnotationConstants.LANGUAGE);//$NON-NLS-1$

		toolkit.paintBordersFor(section);
		section.setClient(client);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#getModelUUID()
	 */
	@Override
	public UUID getModelUUID() {
		return entityModel != null ? entityModel.getUUID() : null;
	}

	@Override
	public void revalidate() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#updateControls()
	 */
	@Override
	protected void updateControls() {
		ControlsUpdater.updateRpcEntityDetailsControls(entityModel, mapTexts, mapCombos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#doUpdateModel(java.lang.String)
	 */
	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateRpcEntityModel(getEntity(), entityModel, key, (String)map.get(Constants.TEXT_VALUE));
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
			entityModel = (RpcEntityModel)selection.getFirstElement();
		} else {
			entityModel = null;
		}
	}

	private static String[] getLanguagesItems() {
		List<String> list = new ArrayList<String>();
		list.add(Languages.UNDEFINED.toString());
		list.add(Languages.C.toString());
		list.add(Languages.COBOL.toString());
		list.add(Languages.FORTRAN.toString());
		list.add(Languages.PL1.toString());
		list.add(Languages.RPG.toString());
		return list.toArray(new String[] {});
	}
}

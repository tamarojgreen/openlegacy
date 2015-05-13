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
package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
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
 * @author Aleksey Yeremeyev
 * 
 */
public class FieldsJpaEnumFieldDetailsPage extends AbstractJpaFieldDetailsPage {

	private JpaEnumFieldModel fieldModel;

	/**
	 * @param master
	 */
	public FieldsJpaEnumFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	/* (non-Javadoc)
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa.AbstractJpaFieldDetailsPage#getFieldModel()
	 */
	@Override
	protected JpaFieldModel getFieldModel() {
		return fieldModel;
	}

	/* (non-Javadoc)
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa.AbstractJpaFieldDetailsPage#addTopContent(org.eclipse.ui.forms.widgets.FormToolkit, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addTopContent(FormToolkit toolkit, Composite client) {
		// create row for displaying java type name
		FormRowCreator.createLabelRow(toolkit, client, mapLabels, Messages.getString("jpa.field.java.type"), "",//$NON-NLS-1$ //$NON-NLS-2$
				Constants.JAVA_TYPE_NAME, JAVA_DOCUMENTATION_TYPE.JAVA_BASICS, "datatypes", "");//$NON-NLS-1$ //$NON-NLS-2$
	}

	/* (non-Javadoc)
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa.AbstractJpaFieldDetailsPage#addBottomContent(org.eclipse.ui.forms.widgets.FormToolkit, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addBottomContent(FormToolkit toolkit, Composite client) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#getDetailsModel()
	 */
	@Override
	public Class<?> getDetailsModel() {
		return JpaEnumFieldModel.class;
	}

	/* (non-Javadoc)
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#getModelUUID()
	 */
	@Override
	public UUID getModelUUID() {
		return fieldModel != null ? fieldModel.getUUID() : null;
	}

	/* (non-Javadoc)
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#updateControls()
	 */
	@Override
	protected void updateControls() {
		if (fieldModel == null) {
			return;
		}
		ControlsUpdater.updateJpaFieldDetailsControls(fieldModel, mapTexts, mapCheckBoxes, mapLabels);
		revalidate();
	}

	/* (non-Javadoc)
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#doUpdateModel(java.lang.String)
	 */
	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateJpaFieldModel(getEntity(), fieldModel, key, (String) map.get(Constants.TEXT_VALUE),
				(Boolean) map.get(Constants.BOOL_VALUE));
	}

	/* (non-Javadoc)
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#afterDoUpdateModel()
	 */
	@Override
	protected void afterDoUpdateModel() {
		setDirty(getEntity().isDirty());
	}

	/* (non-Javadoc)
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#selectionChanged(org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			fieldModel = (JpaEnumFieldModel) selection.getFirstElement();
		} else {
			fieldModel = null;
		}
	}

}

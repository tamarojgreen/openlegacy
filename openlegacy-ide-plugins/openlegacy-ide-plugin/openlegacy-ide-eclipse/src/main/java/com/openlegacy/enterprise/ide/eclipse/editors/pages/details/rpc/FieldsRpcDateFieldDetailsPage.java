/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ModelUpdater;

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
public class FieldsRpcDateFieldDetailsPage extends AbstractRpcFieldDetailsPage {

	private RpcDateFieldModel fieldModel;

	public FieldsRpcDateFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	protected void addContent(FormToolkit toolkit, Composite client) {
		// create row for "pattern"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("rpc.field.pattern"), "", AnnotationConstants.PATTERN, JAVA_DOCUMENTATION_TYPE.RPC,//$NON-NLS-1$ //$NON-NLS-2$
				"RpcDateField", AnnotationConstants.PATTERN);//$NON-NLS-1$

	}

	@Override
	protected RpcFieldModel getFieldModel() {
		return fieldModel;
	}

	@Override
	public Class<?> getDetailsModel() {
		return RpcDateFieldModel.class;
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
		ControlsUpdater.updateRpcFieldDetailsControls(fieldModel, mapTexts, mapCombos, mapCheckBoxes, mapLabels);
		ControlsUpdater.updateRpcDateFieldDetailsControls(fieldModel, mapTexts);
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateRpcFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE), (String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
		ModelUpdater.updateRpcDateFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE));
	}

	@Override
	protected void afterDoUpdateModel() {
		setDirty(getEntity().isDirty());
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			fieldModel = (RpcDateFieldModel)selection.getFirstElement();
		} else {
			fieldModel = null;
		}
	}

}

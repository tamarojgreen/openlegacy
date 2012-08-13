/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.providers.applinx;

import com.sabratec.applinx.baseobject.GXBaseObjectConstants;
import com.sabratec.applinx.common.runtime.field.GXBlockModeCommonFieldData;
import com.sabratec.applinx.common.runtime.field.GXCharModeCommonFieldData;
import com.sabratec.applinx.common.runtime.field.GXIDataTypeSupport;
import com.sabratec.applinx.common.runtime.field.GXIField;

import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.AbstractTerminalField;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SnapshotUtils;

public class ApxTerminalField extends AbstractTerminalField {

	private static final long serialVersionUID = 1L;

	private final GXIField apxField;

	public ApxTerminalField(GXIField field) {
		this.apxField = field;
	}

	@Override
	public String initValue() {
		return apxField.getContent();
	}

	public boolean isEditable() {
		return !apxField.isProtected();
	}

	@Override
	public TerminalPosition initPosition() {
		return SimpleTerminalPosition.newInstance(apxField.getPosition().getRow(), apxField.getPosition().getColumn());
	}

	@Override
	public int initLength() {
		return apxField.getLength();
	}

	@Override
	public TerminalPosition initEndPosition() {
		return SnapshotUtils.getEndPosition(this);
	}

	public boolean isHidden() {
		GXBlockModeCommonFieldData commonData = (GXBlockModeCommonFieldData)apxField.getCommonData();
		return commonData.isHidden();
	}

	public Color getColor() {
		return ApxUtils.convertForeColor(apxField.getCommonData().getForegroundColor());
	}

	public Color getBackColor() {
		return ApxUtils.convertBackColor(apxField.getCommonData().getBackgroundColor());
	}

	public Class<?> getType() {
		if (apxField.getUnprotectedFieldData() instanceof GXIDataTypeSupport) {
			GXIDataTypeSupport dataTypedField = (GXIDataTypeSupport)apxField.getUnprotectedFieldData();
			if (dataTypedField.getDataType() == GXBaseObjectConstants.GX_FIELD_DATA_TYPE_NUMERIC) {
				return Integer.class;
			}
		}
		return String.class;
	}

	public boolean isBold() {
		if (apxField.getCommonData() instanceof GXCharModeCommonFieldData) {
			return ((GXCharModeCommonFieldData)apxField.getCommonData()).isBold();
		}
		return ((GXBlockModeCommonFieldData)apxField.getCommonData()).isIntensified();
	}

	public boolean isReversed() {
		if (this.apxField.getCommonData() instanceof GXCharModeCommonFieldData) {
			return false;
		}
		return ((GXBlockModeCommonFieldData)this.apxField.getCommonData()).isReversedVideo();
	}

	@Override
	public TerminalField clone() {
		ApxTerminalField field = new ApxTerminalField(apxField);
		return field;

	}

	public String getVisualValue() {
		return apxField.getVisualContent();
	}
}

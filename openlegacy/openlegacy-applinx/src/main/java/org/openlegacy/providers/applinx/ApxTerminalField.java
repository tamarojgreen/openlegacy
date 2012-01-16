package org.openlegacy.providers.applinx;

import com.sabratec.applinx.baseobject.GXBaseObjectConstants;
import com.sabratec.applinx.common.runtime.field.GXBlockModeCommonFieldData;
import com.sabratec.applinx.common.runtime.field.GXIDataTypeSupport;
import com.sabratec.applinx.common.runtime.field.GXIField;

import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.AbstractTerminalField;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SnapshotUtils;

public class ApxTerminalField extends AbstractTerminalField {

	private static final long serialVersionUID = 1L;

	private final GXIField apxField;
	private TerminalPosition position;

	private TerminalPosition endPosition;

	public ApxTerminalField(GXIField field) {
		this.apxField = field;
	}

	public String getValue() {
		if (getModifiedValue() != null) {
			return getModifiedValue();
		}
		return apxField.getContent();
	}

	public boolean isEditable() {
		return !apxField.isProtected();
	}

	public TerminalPosition getPosition() {
		if (position == null) {
			position = SimpleTerminalPosition.newInstance(apxField.getPosition().getRow(), apxField.getPosition().getColumn());
		}
		return position;
	}

	public int getLength() {
		return apxField.getLength();
	}

	public TerminalPosition getEndPosition() {
		if (endPosition == null) {
			endPosition = SnapshotUtils.getEndPosition(this);
		}
		return endPosition;
	}

	public boolean isHidden() {
		GXBlockModeCommonFieldData commonData = (GXBlockModeCommonFieldData)apxField.getCommonData();
		return commonData.isHidden();
	}

	public Color getColor() {
		return ApxUtils.convertForeColor(apxField.getCommonData().getBackgroundColor());
	}

	public Color getBackColor() {
		return ApxUtils.convertBackColor(apxField.getCommonData().getBackgroundColor());
	}

	public Class<?> getType() {
		if (apxField.getUnprotectedFieldData() instanceof GXIDataTypeSupport) {
			GXIDataTypeSupport dataTypedField = (GXIDataTypeSupport)apxField;
			if (dataTypedField.getDataType() == GXBaseObjectConstants.GX_FIELD_DATA_TYPE_NUMERIC) {
				return Double.class;
			}
		}
		return String.class;
	}
}

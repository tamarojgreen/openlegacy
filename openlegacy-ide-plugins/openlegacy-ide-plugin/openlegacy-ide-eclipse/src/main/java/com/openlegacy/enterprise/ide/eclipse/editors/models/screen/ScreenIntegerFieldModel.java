package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenIntegerFieldModel extends ScreenFieldModel {

	public ScreenIntegerFieldModel(NamedObject parent) {
		super(parent);
		this.javaTypeName = Messages.getString("type.integer");//$NON-NLS-1$
	}

	/**
	 * @param uuid
	 */
	public ScreenIntegerFieldModel(UUID uuid, NamedObject parent) {
		super(parent);
		this.uuid = uuid;
		this.javaTypeName = Messages.getString("type.integer");//$NON-NLS-1$
	}

	@Override
	public ScreenIntegerFieldModel clone() {
		ScreenIntegerFieldModel model = new ScreenIntegerFieldModel(this.uuid, this.parent);
		fillModel(model);

		return model;
	}

}

package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenIdentification;
import org.openlegacy.terminal.services.ScreenIdentifier;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents @ScreenIdentifiers annotation
 * 
 * @author Ivan Bort
 * 
 */
public class ScreenIdentifiersModel extends ScreenNamedObject {

	private List<IdentifierModel> identifiers = new ArrayList<IdentifierModel>();

	public ScreenIdentifiersModel() {
		super(ScreenIdentifiers.class.getSimpleName());
	}

	public ScreenIdentifiersModel(UUID uuid) {
		super(ScreenIdentifiers.class.getSimpleName());
		this.uuid = uuid;
	}

	@Override
	public void init(CodeBasedScreenEntityDefinition entityDefinition) {
		ScreenIdentification screenIdentification = entityDefinition.getScreenIdentification();
		if (screenIdentification == null) {
			return;
		}
		List<ScreenIdentifier> list = screenIdentification.getScreenIdentifiers();
		for (ScreenIdentifier item : list) {
			this.identifiers.add(new IdentifierModel(item.getPosition(), ((SimpleScreenIdentifier)item).getText(),
					((SimpleScreenIdentifier)item).getAttribute()));
		}
	}

	/**
	 * Method <code>init(ScreenFieldDefinition screenFieldDefinition)</code> is not supported for this model. Use
	 * <code>init(CodeBasedScreenEntityDefinition entityDefinition)</code> instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(ScreenFieldDefinition screenFieldDefinition) is not supported for this model. Use init(CodeBasedScreenEntityDefinition entityDefinition) instead.");//$NON-NLS-1$
	}

	@Override
	public ScreenIdentifiersModel clone() {
		ScreenIdentifiersModel model = new ScreenIdentifiersModel(this.uuid);
		model.setModelName(this.modelName);
		List<IdentifierModel> list = new ArrayList<IdentifierModel>();
		for (IdentifierModel identifierModel : this.identifiers) {
			list.add(new IdentifierModel(identifierModel.getRow(), identifierModel.getColumn(), identifierModel.getText(),
					identifierModel.getAttribute()));
		}
		model.setIdentifiers(list);
		return model;
	}

	public List<IdentifierModel> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(List<IdentifierModel> identifiers) {
		this.identifiers = identifiers;
	}
}

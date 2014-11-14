package com.openlegacy.enterprise.ide.eclipse.editors.pages;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public interface IOpenLegacyDetailsPage {

	public Class<?> getDetailsModel();

	public void removeValidationMarkers();

	public UUID getModelUUID();

	public void revalidate();
}

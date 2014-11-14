package com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen.helpers;

import com.openlegacy.enterprise.ide.eclipse.editors.models.AbstractEntity;

import org.eclipse.jface.viewers.IStructuredSelection;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public interface IPartsMasterBlockCallback {

	public AbstractEntity getAbstractEntity();

	public void reassignMasterBlockViewerInput(UUID uuid);

	public IStructuredSelection getMasterBlockViewerSelection();

	public void removeValidationMarkers(UUID uuid);

	public void removePartsValidationMarkers();

}

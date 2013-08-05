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
package org.openlegacy.designtime.terminal;

import org.openlegacy.EntityDefinition;
import org.openlegacy.designtime.EntityUserInteraction;
import org.openlegacy.designtime.GenerateModelRequest;
import org.openlegacy.designtime.mains.AbstractGenerateRequest;
import org.openlegacy.terminal.TerminalSnapshot;

import java.io.File;

public class GenerateScreenModelRequest extends AbstractGenerateRequest implements GenerateModelRequest {

	// generation is either to a trail file
	private File trailFile;
	// or snapshot
	private TerminalSnapshot[] terminalSnapshots;

	private boolean generateSnapshotXml = true;
	private boolean generateSnapshotImage = false;
	private boolean generateSnapshotText = false;

	private EntityUserInteraction<EntityDefinition<?>> entityUserInteraction;

	public File getTrailFile() {
		return trailFile;
	}

	public void setTrailFile(File trailFile) {
		this.trailFile = trailFile;
	}

	public void setTerminalSnapshots(TerminalSnapshot... terminalSnapshots) {
		this.terminalSnapshots = terminalSnapshots;
	}

	public TerminalSnapshot[] getTerminalSnapshots() {
		return terminalSnapshots;
	}

	public EntityUserInteraction<EntityDefinition<?>> getEntityUserInteraction() {
		return entityUserInteraction;
	}

	public void setEntityUserInteraction(EntityUserInteraction<EntityDefinition<?>> entityUserInteraction) {
		this.entityUserInteraction = entityUserInteraction;
	}

	public boolean isGenerateSnapshotXml() {
		return generateSnapshotXml;
	}

	public void setGenerateSnapshotXml(boolean generateSnapshotXml) {
		this.generateSnapshotXml = generateSnapshotXml;
	}

	public boolean isGenerateSnapshotImage() {
		return generateSnapshotImage;
	}

	public void setGenerateSnapshotImage(boolean generateSnapshotImage) {
		this.generateSnapshotImage = generateSnapshotImage;
	}

	public boolean isGenerateSnapshotText() {
		return generateSnapshotText;
	}

	public void setGenerateSnapshotText(boolean generateSnapshotText) {
		this.generateSnapshotText = generateSnapshotText;
	}

}

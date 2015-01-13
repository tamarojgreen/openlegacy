package com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.utils.screen.ScreenEntityUtils;

import org.eclipse.core.resources.IFile;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.support.DefaultTerminalSnapshotsLoader;

import java.io.File;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenEntityModel extends AbstractEntityModel {

	private CodeBasedScreenEntityDefinition definition = null;
	private TerminalSnapshot terminalSnapshot = null;

	public ScreenEntityModel(CodeBasedScreenEntityDefinition entityDefinition, IFile xmlFile) {
		super(entityDefinition.getEntityName(), xmlFile);
		this.definition = entityDefinition;

		// populate children
		List<ScreenFieldDefinition> sortedFields = entityDefinition.getSortedFields();
		if (sortedFields != null && !sortedFields.isEmpty()) {
			children.addAll(ScreenEntityUtils.getFields(sortedFields, this));
		}
		if (!entityDefinition.getPartsDefinitions().isEmpty()) {
			children.addAll(ScreenEntityUtils.getParts(entityDefinition, this));
		}

		if (xmlFile != null && xmlFile.exists()) {
			DefaultTerminalSnapshotsLoader loader = new DefaultTerminalSnapshotsLoader();
			terminalSnapshot = loader.load(new File(xmlFile.getLocationURI()).getAbsolutePath());
		}
	}

	public CodeBasedScreenEntityDefinition getDefinition() {
		return definition;
	}

	public TerminalSnapshot getTerminalSnapshot() {
		return terminalSnapshot;
	}

}

package com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.utils.rpc.RpcEntityUtils;

import org.eclipse.core.resources.IFile;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.terminal.TerminalSnapshot;

import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class RpcEntityModel extends AbstractEntityModel {

	private CodeBasedRpcEntityDefinition definition = null;
	private TerminalSnapshot terminalSnapshot = null;

	public RpcEntityModel(CodeBasedRpcEntityDefinition definition, IFile srcFile) {
		super(definition.getEntityName(), srcFile);
		this.definition = definition;

		// populate children
		List<RpcFieldDefinition> sortedFields = definition.getSortedFields();
		if (sortedFields != null && !sortedFields.isEmpty()) {
			children.addAll(RpcEntityUtils.getFields(sortedFields, this));
		}

		if (!definition.getPartsDefinitions().isEmpty()) {
			children.addAll(RpcEntityUtils.getParts(definition, this));
		}

	}

	public CodeBasedRpcEntityDefinition getDefinition() {
		return definition;
	}

	public TerminalSnapshot getTerminalSnapshot() {
		return terminalSnapshot;
	}

}

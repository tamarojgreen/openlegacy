package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.designtime.db.generators.support.CodeBasedDbEntityDefinition;
import org.openlegacy.designtime.generators.CodeBasedScreenPartDefinition;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcEntityDefinition;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcPartDefinition;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

/**
 * @author Ivan Bort
 * 
 */
public abstract class RpcNamedObject extends NamedObject {

	// for drag and drop
	private int treeLevel = 0;
	private int treeBranch = 0;
	private int innerBranchesCount = 0;
	private boolean isNew = false;

	public RpcNamedObject(String name, RpcNamedObject parent) {
		super(name, parent);
		if (parent != null) {
			parent.incrementInnerBranchesCount();
			this.treeLevel = parent.getTreeLevel() + 1;
			this.treeBranch = parent.getTreeBranch() * 10 + parent.getInnerBranchesCount();
		}
	}

	/**
	 * Method <code>init(CodeBasedRpcEntityDefinition entityDefinition)</code> is not supported by this model. Use another
	 * <code>init(...)</code> method instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(CodeBasedRpcEntityDefinition entityDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(CodeBasedRpcEntityDefinition entityDefinition) is not supported by this model. Use another init(...) method instead.");//$NON-NLS-1$
	}

	/**
	 * Method <code>init(RpcFieldDefinition rpcFieldDefinition)</code> is not supported by this model. Use another
	 * <code>init(...)</code> method instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(RpcFieldDefinition rpcFieldDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(RpcFieldDefinition rpcFieldDefinition) is not supported by this model. Use another init(...) method instead.");//$NON-NLS-1$
	}

	/**
	 * Method <code>init(CodeBasedRpcPartDefinition partDefinition)</code> is not supported by this model. Use another
	 * <code>init(...)</code> method instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(CodeBasedRpcPartDefinition partDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(CodeBasedRpcPartDefinition partDefinition) is not supported by this model. Use another init(...) method instead.");//$NON-NLS-1$
	}

	/**
	 * Method <code>init(CodeBasedScreenEntityDefinition entityDefinition)</code> is not supported by this model. Use another
	 * <code>init(...)</code> method instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(CodeBasedScreenEntityDefinition entityDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(CodeBasedScreenEntityDefinition entityDefinition) is not supported by this model. Use another init(...) method instead.");//$NON-NLS-1$
	}

	/**
	 * Method <code>init(ScreenFieldDefinition screenFieldDefinition)</code> is not supported by this model. Use another
	 * <code>init(...)</code> method instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(ScreenFieldDefinition screenFieldDefinition) is not supported by this model. Use another init(...) method instead.");//$NON-NLS-1$
	}

	/**
	 * Method <code>init(CodeBasedScreenPartDefinition partDefinition)</code> is not supported by this model. Use another
	 * <code>init(...)</code> method instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(CodeBasedScreenPartDefinition partDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(CodeBasedScreenPartDefinition partDefinition) is not supported by this model. Use another init(...) method instead.");//$NON-NLS-1$
	}

	/**
	 * Method <code>init(CodeBasedDbEntityDefinition dbEntityDefinition)</code> is not supported by this model. Use another
	 * <code>init(...)</code> method instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(CodeBasedDbEntityDefinition dbEntityDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(CodeBasedDbEntityDefinition dbEntityDefinition) is not supported by this model. Use another init(...) method instead.");//$NON-NLS-1$
	}

	/**
	 * Method <code>init(DbFieldDefinition rpcFieldDefinition)</code> is not supported by this model. Use another
	 * <code>init(...)</code> method instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(DbFieldDefinition dbFieldDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(DbFieldDefinition dbFieldDefinition) is not supported by this model. Use another init(...) method instead.");//$NON-NLS-1$
	}

	public void incrementInnerBranchesCount() {
		this.innerBranchesCount++;
	}

	public int getTreeLevel() {
		return treeLevel;
	}

	public void setTreeLevel(int treeLevel) {
		this.treeLevel = treeLevel;
	}

	public int getTreeBranch() {
		return treeBranch;
	}

	public void setTreeBranch(int treeBranch) {
		this.treeBranch = treeBranch;
	}

	public int getInnerBranchesCount() {
		return innerBranchesCount;
	}

	public void setInnerBranchesCount(int innerBranchesCount) {
		this.innerBranchesCount = innerBranchesCount;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

}

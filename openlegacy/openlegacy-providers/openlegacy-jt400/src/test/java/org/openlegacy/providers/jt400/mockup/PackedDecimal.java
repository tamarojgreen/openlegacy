package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.rpc.actions.RpcActions.READ;

import java.util.ArrayList;
import java.util.List;

@RpcEntity(name = "PackedDecimal")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/PACKCBL.PGM") })
public class PackedDecimal implements org.openlegacy.rpc.RpcEntity {

	@RpcNumericField(minimumValue = -9999, maximumValue = 9999, decimalPlaces = 2)
	@RpcField(length = 4, originalName = "child1", editable = true, legacyType = "packed")
	Double packed;

	public Double getPacked() {
		return packed;
	}

	public void setPacked(Double packed) {
		this.packed = packed;
	}

	@Override
	public List<RpcActionDefinition> getActions() {
		return new ArrayList<RpcActionDefinition>();
	}

}

package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.rpc.actions.RpcActions.READ;

@RpcEntity(name = "PackedDecimalLong")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/PACKLONG.PGM") })
public class PackedDecimalLong implements org.openlegacy.rpc.RpcEntity {

	@RpcNumericField(minimumValue = -999999999999.0, maximumValue = 999999999999.0, decimalPlaces = 0)
	@RpcField(length = 12, originalName = "child1", editable = true, legacyType = "packed")
	Double packed;

	public Double getPacked() {
		return packed;
	}

	public void setPacked(Double packed) {
		this.packed = packed;
	}

	// @Override
	// public List<RpcActionDefinition> getActions() {
	// return new ArrayList<RpcActionDefinition>();
	// }

}

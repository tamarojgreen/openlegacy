package org.openlegacy.rpc.support.mock;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcDateField;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.rpc.actions.RpcActions.READ;

import java.util.Date;

@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/DATE.PGM", global = false) })
@RpcEntity(name = "RpcDateEntity")
public class RpcDateEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcDateField(pattern = "dd - MM - yyyy")
	@RpcField(length = 14, originalName = "dateField")
	Date dateField;

	public Date getDateField() {
		return dateField;
	}

	public void setDateField(Date dateField) {
		this.dateField = dateField;
	}

}

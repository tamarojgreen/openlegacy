package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcDateField;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.rpc.RpcActions.READ;

import java.util.Date;

@RpcEntity(name = "DateEntity")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/DATETEST.PGM") })
public class DateEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcDateField(pattern = "dd - MM - yyyy")
	@RpcField(length = 14, originalName = "dateIn")
	Date dateIn;

	@RpcDateField(pattern = "dd - MM - yyyy")
	@RpcField(length = 14, originalName = "dateOut")
	Date dateOut;

	public Date getDateIn() {
		return dateIn;
	}

	public void setDateIn(Date dateIn) {
		this.dateIn = dateIn;
	}

	public Date getDateOut() {
		return dateOut;
	}

	public void setDateOut(Date dateOut) {
		this.dateOut = dateOut;
	}

}

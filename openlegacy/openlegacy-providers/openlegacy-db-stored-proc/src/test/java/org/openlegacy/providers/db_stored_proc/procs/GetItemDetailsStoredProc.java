package org.openlegacy.providers.db_stored_proc.procs;

import org.openlegacy.providers.db_stored_proc.AbstractDatabaseStoredProcedure;
import org.openlegacy.providers.db_stored_proc.entities.ItemDetailsEntity;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcStructureField;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GetItemDetailsStoredProc extends AbstractDatabaseStoredProcedure {

	ItemDetailsEntity entity = new ItemDetailsEntity();

	@Override
	public void invoke(Connection connection) {
		try {
			ResultSet resultSet = null;
			CallableStatement cs = connection
					.prepareCall("{call getItemDetails(?)}");
			cs.setInt(1, entity.itemId);

			resultSet = cs.executeQuery();

			if (resultSet.next()) {
				entity.item.name = resultSet.getString(1);
				entity.item.description = resultSet.getString(2);
				entity.item.weight = resultSet.getInt(3);

				entity.shipping.method = resultSet.getString(4);
				entity.shipping.days = resultSet.getInt(5);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fetchFields(List<RpcField> fields) {
		for (RpcField f : fields) {
			if (f instanceof RpcFlatField) {
				RpcFlatField ff = (RpcFlatField) f;
				if (ff.getName().equals("itemId")) {
					entity.itemId = ((BigDecimal) ff.getValue()).intValue();
				}
			}

			if (f instanceof RpcStructureField) {
				RpcStructureField sf = (RpcStructureField) f;

				if (sf.getName().equals("item")) {
					for (RpcField _f : sf.getChildrens()) {
						if (_f instanceof RpcFlatField) {
							RpcFlatField _ff = (RpcFlatField) _f;
							if (_ff.getName().equals("name")) {
								entity.item.name = (String) _ff.getValue();
							} else if (_ff.getName().equals("description")) {
								entity.item.description = (String) _ff.getValue();
							} else if (_ff.getName().equals("weight")) {
								entity.item.weight = ((BigDecimal) _ff.getValue())
										.intValue();
							}
						}
					}
				} else if (sf.getName().equals("shipping")) {
					for (RpcField _f : sf.getChildrens()) {
						if (_f instanceof RpcFlatField) {
							RpcFlatField _ff = (RpcFlatField) _f;
							if (_ff.getName().equals("method")) {
								entity.shipping.method = (String) _ff.getValue();
							} else if (_ff.getName().equals("days")) {
								entity.shipping.days = ((BigDecimal) _ff.getValue())
										.intValue();
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void updateFields(List<RpcField> fields) {
		for (RpcField f : fields) {
			if (f instanceof RpcStructureField) {
				RpcStructureField sf = (RpcStructureField) f;

				if (sf.getName().equals("item")) {
					for (RpcField _f : sf.getChildrens()) {
						if (_f instanceof RpcFlatField) {
							RpcFlatField _ff = (RpcFlatField) _f;
							if (_ff.getName().equals("name")) {
								_ff.setValue(entity.item.name);
							} else if (_ff.getName().equals("description")) {
								_ff.setValue(entity.item.description);
							} else if (_ff.getName().equals("weight")) {
								_ff.setValue(entity.item.weight);
							}
						}
					}
				} else if (sf.getName().equals("shipping")) {
					for (RpcField _f : sf.getChildrens()) {
						if (_f instanceof RpcFlatField) {
							RpcFlatField _ff = (RpcFlatField) _f;
							if (_ff.getName().equals("method")) {
								_ff.setValue(entity.shipping.method);
							} else if (_ff.getName().equals("days")) {
								_ff.setValue(entity.shipping.days);
							}
						}
					}
				}
			}
		}
	}

	public ItemDetailsEntity getEntity() {
		return entity;
	}

}

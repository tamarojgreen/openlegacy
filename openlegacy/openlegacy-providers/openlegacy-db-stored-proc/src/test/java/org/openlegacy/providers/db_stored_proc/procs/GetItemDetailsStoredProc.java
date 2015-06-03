package org.openlegacy.providers.db_stored_proc.procs;

import org.openlegacy.providers.db_stored_proc.StoredProcEntity;
import org.openlegacy.providers.db_stored_proc.procs.GetItemDetailsStoredProc.Results.Item;
import org.openlegacy.providers.db_stored_proc.procs.GetItemDetailsStoredProc.Results.Shipping;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcStructureField;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GetItemDetailsStoredProc extends StoredProcEntity {

	private int itemId;

	public static class Results extends StoredProcResultSet {
		public static class Item {
			public String name;
			public String description;
			public int weight;
		}

		public static class Shipping {
			public String method;
			public int days;
		}

		public Item item;
		public Shipping shipping;
	}

	@Override
	public void invokeStoredProc(Connection connection) {
		try {
			ResultSet resultSet = null;
			CallableStatement cs = connection.prepareCall("{call getItemDetails(?)}");
			cs.setInt(1, itemId);

			resultSet = cs.executeQuery();

			if (resultSet.next()) {
				Results rr = new Results();
				rr.item = new Item();
				rr.item.name = resultSet.getString(1);
				rr.item.description = resultSet.getString(2);
				rr.item.weight = resultSet.getInt(3);

				rr.shipping = new Shipping();
				rr.shipping.method = resultSet.getString(4);
				rr.shipping.days = resultSet.getInt(5);

				results = rr;
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
					itemId = ((BigDecimal) ff.getValue()).intValue();
				}
			}

			if (results == null) {
				results = new Results();
			}

			Results rr = (Results) results;

			if (f instanceof RpcStructureField) {
				RpcStructureField sf = (RpcStructureField) f;

				if (sf.getName().equals("item")) {
					if (rr.item == null) {
						rr.item = new Item();
					}

					for (RpcField _f : sf.getChildrens()) {
						if (_f instanceof RpcFlatField) {
							RpcFlatField _ff = (RpcFlatField) _f;
							if (_ff.getName().equals("name")) {
								rr.item.name = (String) _ff.getValue();
							} else if (_ff.getName().equals("description")) {
								rr.item.description = (String) _ff.getValue();
							} else if (_ff.getName().equals("weight")) {
								rr.item.weight = ((BigDecimal) _ff.getValue()).intValue();
							}
						}
					}
				} else if (sf.getName().equals("shipping")) {
					if (rr.shipping == null) {
						rr.shipping = new Shipping();
					}

					for (RpcField _f : sf.getChildrens()) {
						if (_f instanceof RpcFlatField) {
							RpcFlatField _ff = (RpcFlatField) _f;
							if (_ff.getName().equals("method")) {
								rr.shipping.method = (String) _ff.getValue();
							} else if (_ff.getName().equals("days")) {
								rr.shipping.days = ((BigDecimal) _ff.getValue()).intValue();
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void updateFields(List<RpcField> fields) {
		Results rr = (Results) results;

		for (RpcField f : fields) {
			if (f instanceof RpcStructureField) {
				RpcStructureField sf = (RpcStructureField) f;

				if (sf.getName().equals("item")) {
					for (RpcField _f : sf.getChildrens()) {
						if (_f instanceof RpcFlatField) {
							RpcFlatField _ff = (RpcFlatField) _f;
							if (_ff.getName().equals("name")) {
								_ff.setValue(rr.item.name);
							} else if (_ff.getName().equals("description")) {
								_ff.setValue(rr.item.description);
							} else if (_ff.getName().equals("weight")) {
								_ff.setValue(rr.item.weight);
							}
						}
					}
				} else if (sf.getName().equals("shipping")) {
					for (RpcField _f : sf.getChildrens()) {
						if (_f instanceof RpcFlatField) {
							RpcFlatField _ff = (RpcFlatField) _f;
							if (_ff.getName().equals("method")) {
								_ff.setValue(rr.shipping.method);
							} else if (_ff.getName().equals("days")) {
								_ff.setValue(rr.shipping.days);
							}
						}
					}
				}
			}
		}
	}

}

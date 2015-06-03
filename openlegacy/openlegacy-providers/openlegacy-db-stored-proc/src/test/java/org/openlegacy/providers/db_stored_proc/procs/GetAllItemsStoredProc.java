package org.openlegacy.providers.db_stored_proc.procs;

import org.openlegacy.providers.db_stored_proc.FieldsUtils;
import org.openlegacy.providers.db_stored_proc.StoredProcEntity;
import org.openlegacy.providers.db_stored_proc.procs.GetAllItemsStoredProc.Results.Item;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcStructureField;
import org.openlegacy.rpc.RpcStructureListField;
import org.openlegacy.rpc.support.SimpleRpcFields;
import org.openlegacy.rpc.support.SimpleRpcStructureField;
import org.openlegacy.rpc.support.SimpleRpcStructureListField;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetAllItemsStoredProc extends StoredProcEntity {

	public static class Results extends StoredProcResultSet {
		public static class Item {
			public Integer id;
			public String name;
			public String description;
		}

		public List<Item> items;
	}

	@Override
	public void invokeStoredProc(Connection connection) {

		try {
			CallableStatement cs = connection.prepareCall("{call getAllItems}");

			ResultSet rs = cs.executeQuery();

			Results rr = new Results();
			rr.items = new ArrayList<GetAllItemsStoredProc.Results.Item>();

			while (rs.next()) {
				Item item = new Item();
				item.id = rs.getInt(1);
				item.name = rs.getString(2);
				item.description = rs.getString(3);

				rr.items.add(item);
			}

			results = rr;

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void fetchFields(List<RpcField> fields) {
		for (RpcField f : fields) {
			if (f instanceof RpcStructureListField) {
				if (f.getName().equals("items")) {
					if (results == null) {
						results = new Results();
					}

					Results rr = (Results) results;
					rr.items = new ArrayList<GetAllItemsStoredProc.Results.Item>();

					SimpleRpcStructureListField lf = (SimpleRpcStructureListField) f;

					for (RpcField field : lf.getChildren(0)) {
						if (field instanceof RpcStructureField && field.getName().equals("item")) {
							RpcStructureField sf = (RpcStructureField) field;

							Item item = new Item();

							for (RpcField itemField : sf.getChildrens()) {
								if (itemField instanceof RpcFlatField) {
									RpcFlatField iff = (RpcFlatField) itemField;

									if (iff.getName().equals("id")) {
										item.id = ((BigDecimal) iff.getValue()).intValue();
									} else if (iff.getName().equals("name")) {
										item.name = (String) iff.getValue();
									} else if (iff.getName().equals("description")) {
										item.description = (String) iff.getValue();
									}
								}
							}

							rr.items.add(item);
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
			if (f instanceof RpcStructureListField) {
				SimpleRpcStructureListField lf = (SimpleRpcStructureListField) f;
				lf.setName("items");

				SimpleRpcFields ffs = new SimpleRpcFields();

				for (Item item : rr.items) {
					SimpleRpcStructureField sf = new SimpleRpcStructureField();

					sf.setName("item");
					sf.getChildrens().add(FieldsUtils.makeField("id", item.id));
					sf.getChildrens().add(FieldsUtils.makeField("name", item.name));
					sf.getChildrens().add(FieldsUtils.makeField("description", item.description));

					ffs.add(sf);
				}

				lf.getChildrens().set(0, ffs);
			}
		}
	}

}

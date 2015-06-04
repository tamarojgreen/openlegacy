package org.openlegacy.providers.db_stored_proc.entities;

import java.util.ArrayList;
import java.util.List;

public class ItemsEntity {

	public static class Item {
		public Integer id;
		public String name;
		public String description;
	}

	public List<Item> items = new ArrayList<ItemsEntity.Item>();
}

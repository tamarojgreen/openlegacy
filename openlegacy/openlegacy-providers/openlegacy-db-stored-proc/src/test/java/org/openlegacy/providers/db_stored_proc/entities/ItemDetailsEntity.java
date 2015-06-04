package org.openlegacy.providers.db_stored_proc.entities;

public class ItemDetailsEntity {
	public int itemId;

	public static class Item {
		public String name;
		public String description;
		public int weight;
	}

	public static class Shipping {
		public String method;
		public int days;
	}

	public Item item = new Item();
	public Shipping shipping = new Shipping();
}

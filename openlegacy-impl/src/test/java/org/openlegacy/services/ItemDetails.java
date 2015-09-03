/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.services;

public class ItemDetails {

	private Integer itemNum;

	private ItemRecord itemRecord;
	private Shipping shipping;

	public ItemDetails(int id) {
		itemNum = id;
		itemRecord = new ItemRecord(itemNum);
		shipping = new Shipping(itemNum);
	}

	public Integer getItemNum() {
		return itemNum;
	}

	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}

	public ItemRecord getItemRecord() {
		return itemRecord;
	}

	public void setItemRecord(ItemRecord itemRecord) {
		this.itemRecord = itemRecord;
	}

	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	public static class ItemRecord {

		private String itemName;
		private String description;
		private Integer weight;

		public ItemRecord(int id) {
			itemName = String.format("Name %d", id);
			description = String.format("Description %d", id);
			weight = id;
		}

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Integer getWeight() {
			return weight;
		}

		public void setWeight(Integer weight) {
			this.weight = weight;
		}

	}

	public static class Shipping {

		private String shippingMethod;
		private Integer days;

		public Shipping(int id) {
			shippingMethod = String.format("AIR %d", id);
			days = id;
		}

		public String getShippingMethod() {
			return shippingMethod;
		}

		public void setShippingMethod(String shippingMethod) {
			this.shippingMethod = shippingMethod;
		}

		public Integer getDays() {
			return days;
		}

		public void setDays(Integer days) {
			this.days = days;
		}

	}
}

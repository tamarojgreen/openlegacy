package org.openlegacy.rpc.mock.mock;

import org.openlegacy.annotations.rpc.RpcBooleanField;
import org.openlegacy.annotations.rpc.RpcDateField;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.annotations.rpc.RpcPartList;
import org.openlegacy.definitions.RpcActionDefinition;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RpcEntity(name = "FieldsEntity")
public class FieldsEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcNumericField(minimumValue = -9999, maximumValue = 9999, decimalPlaces = 0)
	@RpcField(length = 4, key = true, originalName = "ITEM-NUMBER")
	private Integer itemNumber;

	@RpcField(length = 16, originalName = "ITEM-NAME")
	private String itemName;

	@RpcBooleanField(falseValue = "N", trueValue = "Y")
	@RpcField(length = 1, originalName = "bool", editable = true)
	Boolean bool;

	@RpcDateField(pattern = "dd - MM - yyyy")
	@RpcField(length = 14, originalName = "dateField")
	Date dateField;

	InnerPart innerPart;

	@RpcPartList(count = 2)
	List<ListPart> listPart;

	public List<ListPart> getListPart() {
		return listPart;
	}

	public void setListPart(List<ListPart> listPart) {
		this.listPart = listPart;
	}

	public Integer getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(Integer itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Boolean getBool() {
		return bool;
	}

	public void setBool(Boolean bool) {
		this.bool = bool;
	}

	public Date getDateField() {
		return dateField;
	}

	public void setDateField(Date dateField) {
		this.dateField = dateField;
	}

	public InnerPart getInnerPart() {
		return innerPart;
	}

	public void setInnerPart(InnerPart innerPart) {
		this.innerPart = innerPart;
	}

	@RpcPart(name = "InnerPart")
	public static class InnerPart {

		@RpcField(length = 16, originalName = "textField")
		private String textField;

		public String getTextField() {
			return textField;
		}

		public void setTextField(String textField) {
			this.textField = textField;
		}
	}

	@RpcPart(name = "listInner")
	public static class ListPart {

		@RpcField(length = 16, originalName = "textField")
		private String textField;

		public String getTextField() {
			return textField;
		}

		public void setTextField(String textField) {
			this.textField = textField;
		}
	}

	@Override
	public List<RpcActionDefinition> getActions() {
		return Collections.emptyList();
	}
}

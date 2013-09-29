package org.openlegacy.rpc.definitions;

import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.support.AbstractListFieldTypeDefinition;

public class SimpleRpcListFieldTypeDefinition extends AbstractListFieldTypeDefinition implements RpcListFieldTypeDefinition {

	private FieldTypeDefinition fieldTypeDefinition;
	private Class<?> itemJavaType;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.rpc.definitions.RpcListFieldTypeDefinition#getItemFieldTypeDefinition()
	 */
	public FieldTypeDefinition getItemTypeDefinition() {
		return fieldTypeDefinition;
	}

	public SimpleRpcListFieldTypeDefinition(int fieldLength, int count, FieldTypeDefinition fieldTypeDefinition,
			Class<?> itemJavaType) {
		super(fieldLength, count);
		this.fieldTypeDefinition = fieldTypeDefinition;
		this.itemJavaType = itemJavaType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.rpc.definitions.RpcListFieldTypeDefinition#getItemJavaType()
	 */
	public Class<?> getItemJavaType() {
		return itemJavaType;
	}

	public String getItemJavaName() {
		return itemJavaType.getSimpleName();
	}
}

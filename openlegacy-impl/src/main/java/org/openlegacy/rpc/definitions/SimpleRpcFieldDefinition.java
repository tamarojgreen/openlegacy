package org.openlegacy.rpc.definitions;

import org.openlegacy.FieldType;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.definitions.support.AbstractFieldDefinition;

public class SimpleRpcFieldDefinition extends AbstractFieldDefinition<RpcFieldDefinition> implements RpcFieldDefinition {

	private static final long serialVersionUID = 1L;
	private double length;
	private Direction direction;
	private String originalName;
	private int keyIndex;

	public SimpleRpcFieldDefinition(String name, Class<? extends FieldType> type) {
		super(name, type);
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public int getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(int keyIndex) {
		this.keyIndex = keyIndex;
	}
}

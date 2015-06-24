package org.openlegacy.providers.storedproc;

import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.support.SimpleRpcFlatField;

public class FieldsUtils {

	public static SimpleRpcFlatField makeField(String name, Object value) {
		return makeField(name, value, Direction.NONE, 0);
	}

	public static SimpleRpcFlatField makeField(String name, Object value, Direction direction, int order) {
		return makeField(name, value, value.getClass(), direction, order);
	}

	public static SimpleRpcFlatField makeField(String name, Object value, Class<?> type, Direction direction, int order) {
		SimpleRpcFlatField f = new SimpleRpcFlatField();
		f.setName(name);
		f.setValue(value);
		f.setType(type);
		f.setLength(4);
		f.setDirection(direction);
		f.setOrder(order - 1);

		return f;
	}
}

package org.openlegacy.providers.db_stored_proc;

import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.support.SimpleRpcFlatField;

public class FieldsUtils {

	public static SimpleRpcFlatField makeField(String name, Object value) {
		return makeField(name, value, Direction.NONE);
	}

	public static SimpleRpcFlatField makeField(String name, Object value, Direction direction) {
		return makeField(name, value, value.getClass(), direction);
	}

	public static SimpleRpcFlatField makeField(String name, Object value, Class<?> type, Direction direction) {
		SimpleRpcFlatField f = new SimpleRpcFlatField();
		f.setName(name);
		f.setValue(value);
		f.setType(type);
		f.setLength(4);
		f.setDirection(direction);

		return f;
	}
}

package org.openlegacy.rpc.support;

import org.openlegacy.rpc.definitions.OrderedField;

import java.util.Comparator;

public class RpcOrderFieldComparator implements Comparator<OrderedField> {

	@Override
	public int compare(OrderedField arg0, OrderedField arg1) {
		return arg0.getOrder() - arg1.getOrder();
	}

}

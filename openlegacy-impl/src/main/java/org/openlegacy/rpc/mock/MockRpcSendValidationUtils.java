package org.openlegacy.rpc.mock;

import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcStructureField;
import org.openlegacy.rpc.RpcStructureListField;
import org.openlegacy.rpc.exceptions.RpcActionException;
import org.openlegacy.rpc.support.RpcOrderFieldComparator;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

public class MockRpcSendValidationUtils {

	static public void validateInvokeAction(RpcInvokeAction expectedRpcInvokeAction, RpcInvokeAction actuallRpcInvokeAction)
			throws RpcActionException {

		validateFields(expectedRpcInvokeAction.getFields(), actuallRpcInvokeAction.getFields());

	}

	static public void validateFields(List<RpcField> expectedFields, List<RpcField> actualFields) throws RpcActionException {
		if (expectedFields.size() != actualFields.size()) {
			throw new RpcActionException(MessageFormat.format("Fields list dont match the expected sent fields: {0}",
					expectedFields));
		}

		Collections.sort(expectedFields, new RpcOrderFieldComparator());
		Collections.sort(actualFields, new RpcOrderFieldComparator());
		for (int i = 0; i < expectedFields.size(); i++) {
			validateField(expectedFields.get(i), actualFields.get(i));
		}
	}

	static public void validateField(RpcField expectedField, RpcField actualField) throws RpcActionException {
		if (!expectedField.getClass().equals(actualField.getClass())) {
			throw new RpcActionException(MessageFormat.format("Fields type dont match the expected sent field: {0}",
					expectedField));
		}
		if (expectedField instanceof RpcFlatField) {
			Object expectedValue = ((RpcFlatField)expectedField).getValue();
			Object actualValue = ((RpcFlatField)actualField).getValue();
			if (expectedValue != null) {

				if (!expectedValue.equals(actualValue)) {
					throw new RpcActionException(MessageFormat.format("Fields value dont match the expected value: {0}",
							expectedValue));
				}

			}
		} else if (expectedField instanceof RpcStructureField) {

			validateFields(((RpcStructureField)expectedField).getChildrens(), ((RpcStructureField)actualField).getChildrens());

		} else {
			RpcStructureListField expetedList = (RpcStructureListField)expectedField;
			RpcStructureListField actualList = (RpcStructureListField)actualField;
			if (expetedList.count() != actualList.count()) {
				throw new RpcActionException(MessageFormat.format("Structure List value dont match the expected value: {0}",
						expetedList));
			}
			for (int i = 0; i < expetedList.count(); i++) {
				validateFields(expetedList.getChildren(i), actualList.getChildren(i));
			}
		}

	}

}

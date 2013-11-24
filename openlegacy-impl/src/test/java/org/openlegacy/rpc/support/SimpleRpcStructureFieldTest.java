package org.openlegacy.rpc.support;

import org.junit.Test;
import org.openlegacy.AbstractTest;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcStructureNotMappedException;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

public class SimpleRpcStructureFieldTest extends AbstractTest {

	@Test
	public void testDepth() {
		SimpleRpcStructureField rpcTopStructureField = new SimpleRpcStructureField();
		rpcTopStructureField.setName("top");

		SimpleRpcStructureListField rpcRecordField = new SimpleRpcStructureListField();
		rpcRecordField.setName("record");

		rpcTopStructureField.getChildren().add(rpcRecordField);

		for (int i = 0; i < 3; i++) {

			List<RpcField> recordFields = new ArrayList<RpcField>();

			SimpleRpcFlatField rpcFlatField = new SimpleRpcFlatField();
			rpcFlatField.setName("text");
			rpcFlatField.setType(String.class);
			rpcFlatField.setLength(11);
			rpcFlatField.setDirection(Direction.OUTPUT);

			recordFields.add(rpcFlatField);

			rpcFlatField = new SimpleRpcFlatField();
			rpcFlatField.setName("number");
			rpcFlatField.setLength(4);
			rpcFlatField.setType(Byte.class);
			rpcFlatField.setDecimalPlaces(0);
			rpcFlatField.setDirection(Direction.OUTPUT);
			recordFields.add(rpcFlatField);

			rpcRecordField.getChildren().add(recordFields);
		}
		Assert.assertEquals(3, rpcTopStructureField.depth(0, 5));
	}

	@Test
	public void TestLoop() {
		boolean gotExeption = false;
		SimpleRpcStructureField StructureFieldA = new SimpleRpcStructureField();
		StructureFieldA.setName("a");
		SimpleRpcStructureField StructureFieldB = new SimpleRpcStructureField();
		StructureFieldB.setName("b");
		StructureFieldA.getChildren().add(StructureFieldB);
		StructureFieldB.getChildren().add(StructureFieldA);

		try {
			StructureFieldA.depth(0, 5);
		} catch (RpcStructureNotMappedException e) {
			gotExeption = true;
		}
		Assert.assertTrue(gotExeption);

	}

}

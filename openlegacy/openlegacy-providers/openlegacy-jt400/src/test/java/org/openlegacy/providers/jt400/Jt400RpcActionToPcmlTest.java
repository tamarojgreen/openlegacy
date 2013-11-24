package org.openlegacy.providers.jt400;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.support.SimpleRpcStructureField;
import org.openlegacy.rpc.support.SimpleRpcStructureListField;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

@ContextConfiguration("Jt400RpcSessionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Jt400RpcActionToPcmlTest {

	@Test
	public void saveSimplePcml() throws ParserConfigurationException, TransformerException, FileNotFoundException, IOException {
		String resultName = "cobol_flat";
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		SimpleRpcFlatField input = child1();
		input.setDefaultValue(new Byte((byte)10));
		SimpleRpcFlatField out = child2();

		out.setDirection(Direction.OUTPUT);

		rpcInvokeAction.getFields().add(input);
		rpcInvokeAction.getFields().add(out);

		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/FLATCBL.PGM");
		InvokeActionToPcmlUtil pcmlWriter = new InvokeActionToPcmlUtil();
		pcmlWriter.save(rpcInvokeAction, "", resultName);
		compareContent(resultName);

	}

	@Test
	public void saveSimpleStructurePcml() throws ParserConfigurationException, TransformerException, IOException {
		String resultName = "cobol_structure";
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();

		SimpleRpcStructureField rpcStructureField = new SimpleRpcStructureField();
		rpcStructureField.setName("param1");

		rpcStructureField.getChildren().add(child1());
		rpcStructureField.getChildren().add(child2());

		rpcInvokeAction.getFields().add(rpcStructureField);
		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/ROICBL2.PGM");

		InvokeActionToPcmlUtil pcmlWriter = new InvokeActionToPcmlUtil();
		pcmlWriter.save(rpcInvokeAction, "", resultName);

		compareContent(resultName);
	}

	@Test
	public void saveSimpleTreePcml() throws ParserConfigurationException, TransformerException, IOException {
		String resultName = "cobol_tree";
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();

		SimpleRpcStructureField rpcTopStructureField = new SimpleRpcStructureField();
		rpcTopStructureField.setName("top");

		SimpleRpcStructureField rpcStructureField = new SimpleRpcStructureField();
		rpcStructureField.setName("param1");
		rpcStructureField.getChildren().add(child1());
		rpcStructureField.getChildren().add(child2());

		rpcTopStructureField.getChildren().add(rpcStructureField);

		rpcInvokeAction.getFields().add(rpcTopStructureField);
		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/TREECBL.PGM");

		InvokeActionToPcmlUtil pcmlWriter = new InvokeActionToPcmlUtil();
		pcmlWriter.save(rpcInvokeAction, "", resultName);

		compareContent(resultName);
	}

	@Test
	public void saveSimpleStructureListPcml() throws ParserConfigurationException, TransformerException, IOException {
		String resultName = "tree_array";
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();

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
		rpcInvokeAction.getFields().add(rpcTopStructureField);
		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/TREEARRAY.PGM");

		InvokeActionToPcmlUtil pcmlWriter = new InvokeActionToPcmlUtil();
		pcmlWriter.save(rpcInvokeAction, "", resultName);

		compareContent(resultName);
	}

	private static SimpleRpcFlatField child1() {

		SimpleRpcFlatField rpcFlatField = new SimpleRpcFlatField();
		rpcFlatField.setName("child1");
		rpcFlatField.setType(Byte.class);
		rpcFlatField.setLength(2);
		rpcFlatField.setDecimalPlaces(0);
		rpcFlatField.setDirection(Direction.INPUT);
		rpcFlatField.setOrder(0);

		return rpcFlatField;
	}

	private static SimpleRpcFlatField child2() {
		SimpleRpcFlatField rpcFlatField;
		rpcFlatField = new SimpleRpcFlatField();
		rpcFlatField.setName("child2");
		rpcFlatField.setType(Byte.class);
		rpcFlatField.setLength(2);
		rpcFlatField.setDecimalPlaces(0);
		rpcFlatField.setDirection(Direction.INPUT_OUTPUT);
		rpcFlatField.setOrder(1);
		return rpcFlatField;
	}

	private void compareContent(String resultName) throws IOException, FileNotFoundException {
		byte[] resultBytes = IOUtils.toByteArray(new FileInputStream(resultName + ".pcml"));
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(resultName + ".pcml.expected"));

		AssertUtils.assertContent(expectedBytes, resultBytes);
	}

}

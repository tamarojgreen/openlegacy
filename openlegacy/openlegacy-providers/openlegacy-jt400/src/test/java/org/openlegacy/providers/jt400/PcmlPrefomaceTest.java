package org.openlegacy.providers.jt400;

import com.as400samplecode.CallCobolProgramPcml;
import com.ibm.as400.access.AS400;
import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.ProgramCallDocument;

import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.RpcFields;
import org.openlegacy.rpc.support.SimpleRpcFields;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.support.SimpleRpcStructureField;
import org.openlegacy.rpc.support.SimpleRpcStructureListField;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class PcmlPrefomaceTest {

	private static AS400 as400System = null;

	public static void main(String[] args) throws PcmlException, FileNotFoundException, IOException,
			ParserConfigurationException, TransformerException {

		if (args.length < 3) {
			System.out.println("Usage:" + CallCobolProgramPcml.class.getSimpleName() + " host user password");
			return;
		}

		String host = args[0];
		String user = args[1];
		String password = args[2];

		int iterations = 50;
		as400System = new AS400(host, user, password);

		InputStream in = work();
		ProgramCallDocument newPcml;
		System.out.println("Starting load from Stream");

		Long startTime = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			newPcml = new ProgramCallDocument(as400System, "com.as400samplecode.tree_array", in);

			// result = newPcml.callProgram("tree_array");
		}
		long endTime = System.currentTimeMillis();
		System.out.println(iterations + " iteration " + ((Long)(endTime - startTime)).toString() + " Millis");
		System.out.println("Starting with clone");

		newPcml = new ProgramCallDocument(as400System, "com.as400samplecode.tree_array2");

		startTime = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			newPcml.clone();
			// result = newPcml.callProgram("tree_array2");
		}
		endTime = System.currentTimeMillis();
		System.out.println(iterations + " iteration " + ((Long)(endTime - startTime)).toString() + " Millis");

	}

	private static InputStream work() throws ParserConfigurationException, TransformerException {
		String resultName = "tree_array";
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();

		SimpleRpcStructureField rpcTopStructureField = new SimpleRpcStructureField();
		rpcTopStructureField.setName("top");

		SimpleRpcStructureListField rpcRecordField = new SimpleRpcStructureListField();
		rpcRecordField.setName("record");

		rpcTopStructureField.getChildrens().add(rpcRecordField);

		for (int i = 0; i < 3; i++) {

			RpcFields recordFields = new SimpleRpcFields();

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

			rpcRecordField.getChildrens().add(recordFields);
		}
		rpcInvokeAction.getFields().add(rpcTopStructureField);
		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/TREEARRAY.PGM");

		InvokeActionToPcmlUtil pcmlWriter = new InvokeActionToPcmlUtil();
		return new ByteArrayInputStream(pcmlWriter.serilize(rpcInvokeAction, resultName));

	}

}

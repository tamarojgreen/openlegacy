package org.openlegacy.providers.mfrpc;

import net.sf.JRecord.Common.Conversion;
import net.sf.JRecord.Common.RecordException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.support.SimpleRpcStructureField;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.math.BigInteger;

import junit.framework.Assert;

@ContextConfiguration("mfRpcSessionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class InvokeActionToMFBinTest {

	// @Test
	// public void testPicToType() {
	// net.sf.JRecord.Numeric.Convert convert = ConversionManager.getInstance().getConverter(ICopybookDialects.FMT_MAINFRAME);
	//
	// int result = convert.getTypeIdentifier("computational-4", "S9(2)", false, false, "");
	// System.out.println(result);
	// }

	@Test
	public void stringTest() throws RecordException, IOException {

		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		SimpleRpcFlatField input = new SimpleRpcFlatField();
		input.setName("field");
		input.setType(Byte.class);
		input.setLength(10);
		input.setDecimalPlaces(0);
		input.setDirection(Direction.INPUT);
		input.setOrder(0);
		input.setValue("ABCDEFGH");
		input.setLegacyType("Char");

		rpcInvokeAction.getFields().add(input);

		InvokeActionToMFBin invokeActionToMFBin = new InvokeActionToMFBin();
		byte[] resultBytes = invokeActionToMFBin.serilize(rpcInvokeAction, "CP037");
		byte[] expectedBytes = { (byte)0xC1, (byte)0xC2, (byte)0xC3, (byte)0xC4, (byte)0xC5, (byte)0xC6, (byte)0xC7, (byte)0xC8,
				(byte)0x40, (byte)0x40 };

		AssertUtils.assertContent(expectedBytes, resultBytes);
	}

	@Test
	public void integerTest() throws RecordException, IOException {

		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		SimpleRpcFlatField input = new SimpleRpcFlatField();
		input.setName("field");
		input.setType(Integer.class);
		input.setLength(2);
		input.setDecimalPlaces(0);
		input.setDirection(Direction.INPUT);
		input.setOrder(0);
		input.setValue(12);
		input.setLegacyType("Postive Decimal");
		rpcInvokeAction.getFields().add(input);

		InvokeActionToMFBin invokeActionToMFBin = new InvokeActionToMFBin();
		byte[] resultBytes = invokeActionToMFBin.serilize(rpcInvokeAction, "CP037");
		byte[] expectedBytes = { (byte)0xF1, (byte)0xF2 };
		AssertUtils.assertContent(expectedBytes, resultBytes);
	}

	@Test
	public void NegativeIntegerTest() throws RecordException, IOException {

		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		SimpleRpcFlatField input = new SimpleRpcFlatField();
		input.setName("field");
		input.setType(Integer.class);
		input.setLength(2);
		input.setDecimalPlaces(0);
		input.setDirection(Direction.INPUT);
		input.setOrder(0);
		input.setValue(-12);
		input.setLegacyType("Signed Decimal");
		rpcInvokeAction.getFields().add(input);

		InvokeActionToMFBin invokeActionToMFBin = new InvokeActionToMFBin();
		byte[] resultBytes = invokeActionToMFBin.serilize(rpcInvokeAction, "CP037");
		byte[] expectedBytes = { (byte)0xF1, (byte)0xD2 };
		AssertUtils.assertContent(expectedBytes, resultBytes);
	}

	@Test
	public void compIntegerTest() throws RecordException, IOException {

		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		SimpleRpcFlatField input = new SimpleRpcFlatField();
		input.setName("field");
		input.setType(Integer.class);
		input.setLength(2);
		input.setDecimalPlaces(0);
		input.setDirection(Direction.INPUT);
		input.setOrder(0);
		input.setValue(29);
		input.setLegacyType("Binary Integer");

		rpcInvokeAction.getFields().add(input);

		InvokeActionToMFBin invokeActionToMFBin = new InvokeActionToMFBin();
		byte[] resultBytes = invokeActionToMFBin.serilize(rpcInvokeAction, "CP037");
		byte[] expectedBytes = { (byte)0x00, (byte)0x1D };
		AssertUtils.assertContent(expectedBytes, resultBytes);

		// verify back conversion
		BigInteger val = Conversion.getBigInt(resultBytes, 0, 2);
		Assert.assertEquals("29", val.toString());
	}

	@Test
	public void tmpTest() throws RecordException, IOException {

		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		SimpleRpcFlatField input = new SimpleRpcFlatField();
		input.setName("field");
		input.setType(Integer.class);
		input.setLength(2);
		input.setDecimalPlaces(0);
		input.setDirection(Direction.INPUT);
		input.setOrder(0);
		input.setValue(-35);
		input.setLegacyType("Binary Integer");

		rpcInvokeAction.getFields().add(input);

		InvokeActionToMFBin invokeActionToMFBin = new InvokeActionToMFBin();
		byte[] resultBytes = invokeActionToMFBin.serilize(rpcInvokeAction, "CP037");

		// verify back conversion

		BigInteger val = Conversion.getBigInt(resultBytes, 0, 2);

		Assert.assertEquals(new BigInteger("-35"), val);
	}

	@Test
	public void compNegativeIntegerTest() throws RecordException, IOException {

		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		SimpleRpcFlatField input = new SimpleRpcFlatField();
		input.setName("field");
		input.setType(Integer.class);
		input.setLength(2);
		input.setDecimalPlaces(0);
		input.setDirection(Direction.INPUT);
		input.setOrder(0);
		input.setValue(-100);
		input.setLegacyType("Binary Integer");

		rpcInvokeAction.getFields().add(input);

		InvokeActionToMFBin invokeActionToMFBin = new InvokeActionToMFBin();
		byte[] resultBytes = invokeActionToMFBin.serilize(rpcInvokeAction, "CP037");
		byte[] expectedBytes = { (byte)0xFF, (byte)0x9C };
		AssertUtils.assertContent(expectedBytes, resultBytes);
	}

	private static SimpleRpcInvokeAction getHierarchy() {
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		rpcInvokeAction.setAction("RPC");

		SimpleRpcStructureField rpcStructureField = new SimpleRpcStructureField();
		rpcStructureField.setName("dfhcommarea");
		rpcInvokeAction.getFields().add(rpcStructureField);

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setName("caName");
		rpcField.setValue("Roi");
		rpcField.setLength(8);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcField.setLegacyType("Char");
		rpcField.setOrder(0);
		rpcStructureField.getChildrens().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("caLast");
		rpcField.setValue("Mor");
		rpcField.setLength(20);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcField.setLegacyType("Char");
		rpcField.setOrder(1);
		rpcStructureField.getChildrens().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("caAge");
		rpcField.setValue(-1);
		rpcField.setLength(2);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcField.setLegacyType("Binary Integer");
		rpcField.setOrder(2);
		rpcStructureField.getChildrens().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("caSalary");
		rpcField.setValue(1000000);
		rpcField.setLength(4);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcField.setLegacyType("Binary Integer");
		rpcField.setOrder(3);
		rpcStructureField.getChildrens().add(rpcField);

		rpcInvokeAction.setRpcPath("RPC");
		return rpcInvokeAction;
	}

	private static SimpleRpcInvokeAction getFlat() {
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		rpcInvokeAction.setAction("RPC");

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setName("caName");
		rpcField.setValue("Roi");
		rpcField.setLength(8);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcField.setLegacyType("Char");
		rpcField.setOrder(0);

		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("caLast");
		rpcField.setValue("Mor");
		rpcField.setLength(20);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcField.setLegacyType("Char");
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("caAge");
		rpcField.setValue(-1);
		rpcField.setLength(2);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcField.setLegacyType("Binary Integer");
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("caSalary");
		rpcField.setValue(1000000);
		rpcField.setLength(4);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcInvokeAction.getFields().add(rpcField);
		rpcField.setLegacyType("Binary Integer");

		rpcInvokeAction.setRpcPath("RPC");
		return rpcInvokeAction;
	}

	@Test
	public void hierarchyTest() throws RecordException, IOException {

		SimpleRpcInvokeAction flatRpcInvokeAction = getFlat();
		SimpleRpcInvokeAction hierarchyRpcInvokeAction = getHierarchy();

		InvokeActionToMFBin invokeActionToMFBin = new InvokeActionToMFBin();
		byte[] flatBytes = invokeActionToMFBin.serilize(flatRpcInvokeAction, "CP037");
		byte[] hierarchyBytes = invokeActionToMFBin.serilize(hierarchyRpcInvokeAction, "CP037");

		AssertUtils.assertContent(flatBytes, hierarchyBytes);
	}

}

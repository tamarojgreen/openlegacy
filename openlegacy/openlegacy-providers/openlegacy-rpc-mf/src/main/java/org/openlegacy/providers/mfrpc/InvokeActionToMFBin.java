package org.openlegacy.providers.mfrpc;

import net.sf.JRecord.Common.Conversion;
import net.sf.JRecord.Common.RecordException;
import net.sf.JRecord.Types.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcStructureField;
import org.openlegacy.rpc.RpcStructureListField;
import org.openlegacy.rpc.support.SimpleRpcFlatField;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class InvokeActionToMFBin {

	private final static Log logger = LogFactory.getLog(InvokeActionToMFBin.class);
	private static final byte SPACE = 0x40;
	private static final byte ZONED_NEGATIVE_NYBLE_OR = (byte)0xDF;

	private static HashMap<String, Integer> convertor = new HashMap<String, Integer>() {

		{
			put("Mainframe Packed Decimal (comp-3)", Type.ftPackedDecimal);
			put("Postive Decimal", Type.ftPositiveNumAnyDecimal);
			put("Signed Decimal", Type.ftZonedNumeric);
			put("Binary Integer", Type.ftBinaryBigEndian);
			put("Char", Type.ftChar);
		}
	};

	public void deserilize(List<RpcField> fields, byte input[], String fontName, int start) {
		int adress = start;

		for (RpcField field : fields) {

			if (field.getDirection() == Direction.INPUT) {
				adress = adress + field.getLength();
				continue;
			}
			if (field instanceof RpcFlatField) {
				extractSimple(input, fontName, adress, field);
				adress = adress + field.getLength();
			} else if (field instanceof RpcStructureField) {
				RpcStructureField rpcStructureField = (RpcStructureField)field;
				List<RpcField> inFields = rpcStructureField.getChildrens();
				deserilize(inFields, input, fontName, adress);
				adress = adress + rpcStructureField.getLength();
			} else if (field instanceof RpcStructureListField) {
				RpcStructureListField rpcStructureListField = (RpcStructureListField)field;
				int length = field.getLength() / rpcStructureListField.count();
				for (int i = 0; i < rpcStructureListField.count(); i++) {

					deserilize(rpcStructureListField.getChildren(i), input, fontName, adress);
					adress += length;
				}
			}
		}

	}

	private static void extractSimple(byte[] input, String fontName, int adress, RpcField field) {
		SimpleRpcFlatField rpcFlatField = (SimpleRpcFlatField)field;
		int mfType = convertor.get(rpcFlatField.getLegacyType());
		if (mfType == Type.ftChar) {
			((RpcFlatField)field).setValue(Conversion.getString(input, adress, adress + field.getLength(), fontName).trim());
		} else if (mfType == Type.ftBinaryBigEndian) {
			BigInteger res = Conversion.getBigInt(input, adress, field.getLength());
			Class<?> fieldType = rpcFlatField.getType();
			if (Integer.class.equals(fieldType)) {

				((RpcFlatField)field).setValue(Integer.valueOf(res.toString()));
			}

		}
	}

	private static void padWith(byte[] record, final int start, final int len, final String padCh, final String font) {
		byte padByte = Conversion.getBytes(padCh, font)[0];

		int i;
		for (i = start; i < start + len; i++) {
			record[i] = padByte;
		}
	}

	private static void copyRightJust(byte[] record, String val, int pos, int len, String pad, String font) {

		int l = val.length();

		if (l == len) {
			System.arraycopy(Conversion.getBytes(val, font), 0, record, pos, len);

		} else {
			padWith(record, pos, len - val.length(), pad, font);
			System.arraycopy(Conversion.getBytes(val, font), 0, record, pos + len - val.length(), val.length());
		}
	}

	private static void setFlatField(SimpleRpcFlatField rpcFlatField, String fontName, byte[] data, int startPosition) {
		int mfType = convertor.get(rpcFlatField.getLegacyType());
		BigDecimal number;
		try {
			switch (mfType) {
				case Type.ftChar:
					byte[] fieldData = Conversion.getBytes((String)rpcFlatField.getValue(), fontName);
					System.arraycopy(fieldData, 0, data, startPosition, fieldData.length);
					break;

				case Type.ftBinaryBigEndian:
					number = (BigDecimal)rpcFlatField.getValue();

					Conversion.setBigInt(data, startPosition, rpcFlatField.getLength(), number.toBigInteger(), false);

					break;

				case Type.ftBinaryBigEndianPositive:
				case Type.ftPackedDecimalPostive:
					number = (BigDecimal)rpcFlatField.getValue();
					Conversion.setBigInt(data, startPosition, rpcFlatField.getLength(), number.toBigInteger(), true);
					break;

				case Type.ftZonedNumeric:
				case Type.ftNumAnyDecimal:
					number = (BigDecimal)rpcFlatField.getValue();
					if (number.compareTo(BigDecimal.ZERO) < 0) {
						copyRightJust(data, number.toString().substring(1), startPosition, rpcFlatField.getLength(), "0",
								fontName);
						int lastByte = startPosition + rpcFlatField.getLength() - 1;
						data[lastByte] = (byte)(data[lastByte] & ZONED_NEGATIVE_NYBLE_OR);
					} else {
						copyRightJust(data, rpcFlatField.getValue().toString(), startPosition, rpcFlatField.getLength(), " ",
								fontName);
					}

					break;
				case Type.ftPositiveNumAnyDecimal:
					copyRightJust(data, rpcFlatField.getValue().toString(), startPosition, rpcFlatField.getLength(), " ",
							fontName);

					break;
				default:
					// Assert.assertTrue(true);
			}
		} catch (RecordException e) {
			throw new OpenLegacyProviderException(e);
		}

	}

	private void serilize(List<org.openlegacy.rpc.RpcField> rpcFields, byte[] data, int startPosition, String fontName) {

		for (org.openlegacy.rpc.RpcField rpcField : rpcFields) {
			// logger.debug("entering name " + rpcField.getName() + " start " + startPosition);

			if (rpcField instanceof RpcFlatField) {
				setFlatField((SimpleRpcFlatField)rpcField, fontName, data, startPosition);
				startPosition = startPosition + rpcField.getLength();
			} else if (rpcField instanceof RpcStructureField) {
				serilize(((RpcStructureField)rpcField).getChildrens(), data, startPosition, fontName);
				startPosition = startPosition + rpcField.getLength();
			} else if (rpcField instanceof RpcStructureListField) {
				RpcStructureListField rpcStructureListField = (RpcStructureListField)rpcField;
				int length = rpcField.getLength() / rpcStructureListField.count();
				for (int i = 0; i < rpcStructureListField.count(); i++) {

					// logger.debug("name " + rpcField.getName() + " start " + startPosition);
					// logger.debug("index " + i + " length " + rpcField.getLength());

					serilize(rpcStructureListField.getChildren(i), data, startPosition, fontName);
					startPosition += length;
				}
			}
		}

	}

	public byte[] serilize(RpcInvokeAction rpcInvokeAction, String fontName) {

		List<org.openlegacy.rpc.RpcField> rpcFields = rpcInvokeAction.getFields();
		int size = 0;
		for (org.openlegacy.rpc.RpcField rpcField : rpcFields) {
			size = size + rpcField.getLength();
		}
		byte[] buffer = new byte[size];
		Arrays.fill(buffer, SPACE);
		serilize(rpcFields, buffer, 0, fontName);

		return buffer;
	}

}

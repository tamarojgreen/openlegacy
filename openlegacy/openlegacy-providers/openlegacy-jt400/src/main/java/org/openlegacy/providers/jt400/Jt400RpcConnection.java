/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.providers.jt400;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400ByteArray;
import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400ZonedDecimal;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;

import org.openlegacy.FieldFormatter;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvocationException;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.RpcStructureField;
import org.openlegacy.rpc.support.SimpleRpcResult;

import java.util.ArrayList;
import java.util.List;

public class Jt400RpcConnection implements RpcConnection {

	private AS400 as400Session;
	private Integer sequence = 0;

	private FieldFormatter fieldFormatter;

	public Jt400RpcConnection(AS400 as400Session) {
		this.as400Session = as400Session;
	}

	public void setFieldFormatter(FieldFormatter fieldFormatter) {
		this.fieldFormatter = fieldFormatter;
	}

	public Object getDelegate() {
		return as400Session;
	}

	public void disconnect() {
		if (as400Session != null) {
			as400Session.disconnectAllServices();
		}
	}

	public boolean isConnected() {
		return as400Session != null && as400Session.isConnected();
	}

	public RpcResult invoke(RpcInvokeAction rpcInvokeAction) {
		ProgramCall program = new ProgramCall(as400Session);
		try {
			// Initialize the name of the program to run.
			String programName = rpcInvokeAction.getRpcPath();

			List<RpcField> fields = rpcInvokeAction.getFields();
			List<ProgramParameter> programParameters = new ArrayList<ProgramParameter>();

			buildParameters(fields, programParameters);

			program.setProgram(programName, programParameters.toArray(new ProgramParameter[programParameters.size()]));
			// Run the program.
			if (program.run() != true) {
				StringBuilder sb = new StringBuilder();
				// Show the messages.
				AS400Message[] messagelist = program.getMessageList();
				for (int i = 0; i < messagelist.length; ++i) {
					sb.append(messagelist[i]);
				}
				throw (new RpcInvocationException(sb.toString()));
			}
			// Else no error, get output data.
			else {
				sequence++;
				SimpleRpcResult rpcResult = new SimpleRpcResult();
				rpcResult.setRpcFields(fields);
				int count = 0;
				for (RpcField field : fields) {
					if (field instanceof RpcFlatField) {
						RpcFlatField rpcFlatField = (RpcFlatField)field;
						if (field.getDirection() != Direction.INPUT) {
							AS400DataType as400DataType = initAs400DataType(rpcFlatField, Direction.OUTPUT);
							Object value = as400DataType.toObject(programParameters.get(count).getOutputData());
							if (value instanceof String) {
								value = fieldFormatter.format((String)value);
							}
							rpcFlatField.setValue(value);
						}
					}

					count++;
				}

				return rpcResult;
			}
		} catch (Exception e) {
			throw (new RpcInvocationException(e.getMessage()
					+ " - Make sure connection settings are well defined in rpc.properties", e));
		}
	}

	private void buildParameters(List<RpcField> fields, List<ProgramParameter> programParameters) {
		for (RpcField rpcField : fields) {
			if (rpcField instanceof RpcFlatField) {
				RpcFlatField rpcFlatField = (RpcFlatField)rpcField;
				AS400DataType as400Field = initAs400DataType(rpcFlatField, Direction.INPUT);
				if (as400Field == null) {
					programParameters.add(new ProgramParameter(rpcFlatField.getLength()));
				} else {
					programParameters.add(new ProgramParameter(as400Field.toBytes(rpcFlatField.getValue())));
				}
			} else if (rpcField instanceof RpcStructureField) {
				RpcStructureField rpcStructureField = (RpcStructureField)rpcField;
				programParameters.add(new ProgramParameter(ProgramParameter.PASS_BY_REFERENCE, rpcStructureField.getLength()));
				buildParameters(rpcStructureField.getChildren(), programParameters);
			}
		}
	}

	private AS400DataType initAs400DataType(RpcFlatField rpcField, Direction direction) {
		AS400DataType as400Field = null;
		if (rpcField.getType() == String.class) {
			if (rpcField.getDirection() == Direction.INPUT_OUTPUT || rpcField.getDirection() == direction) {
				as400Field = new AS400Text(rpcField.getLength().intValue(), as400Session);
			}
		} else if (rpcField.getType() == Byte.class) {
			as400Field = new AS400ByteArray(1);
		} else if (Number.class.isAssignableFrom(rpcField.getType())) {
			if (rpcField.getDirection() == Direction.INPUT_OUTPUT || rpcField.getDirection() == direction) {
				as400Field = new AS400ZonedDecimal(rpcField.getLength().intValue(), rpcField.getDecimalPlaces());
			}
		}
		return as400Field;
	}

	public RpcSnapshot getSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}

	public RpcSnapshot fetchSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}

	public void doAction(RpcInvokeAction sendAction) {
		// TODO Auto-generated method stub

	}

	public Integer getSequence() {
		return sequence;
	}

	public void login(String user, String password) {
		try {
			as400Session.setUserId(user);
			as400Session.setPassword(password);
			as400Session.authenticate(user, password);
		} catch (Exception e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

}

/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
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
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.ProgramCallDocument;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.openlegacy.rpc.RpcStructureListField;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.support.SimpleRpcResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class Jt400RpcConnection implements RpcConnection {

	private AS400 as400Session;
	private Integer sequence = 0;

	private final static Log logger = LogFactory.getLog(Jt400RpcConnection.class);

	private Map<String, ProgramCallDocument> actionCache = new HashMap<String, ProgramCallDocument>();

	InvokeActionToPcmlUtil invokeActionToPcmlUtil = new InvokeActionToPcmlUtil();

	public void setAs400Session(AS400 as400Session) {
		this.as400Session = as400Session;
	}

	@Override
	public Object getDelegate() {
		return as400Session;
	}

	@Override
	public void disconnect() {
		if (as400Session != null) {
			as400Session.disconnectAllServices();
		}
	}

	@Override
	public boolean isConnected() {
		return as400Session != null && as400Session.isConnected();
	}

	@Override
	public RpcResult invoke(RpcInvokeAction rpcInvokeAction) {

		String actionAlias = rpcInvokeAction.getRpcPath().replaceAll("/", "_").replaceAll("\\.", "_");
		if (rpcInvokeAction.getAction() == null) {
			((SimpleRpcInvokeAction)rpcInvokeAction).setAction(actionAlias);
		}
		String action = rpcInvokeAction.getAction();

		ProgramCallDocument programCallDocument = null;
		if (actionCache.containsKey(action)) {
			programCallDocument = (ProgramCallDocument)actionCache.get(action).clone();
		} else {

			try {

				byte[] pcmlBytes = invokeActionToPcmlUtil.serilize(rpcInvokeAction, action);
				InputStream in = new ByteArrayInputStream(pcmlBytes);
				if (logger.isDebugEnabled()) {
					logger.debug(new String(pcmlBytes));
				}
				// programCallDocument = new ProgramCallDocument(as400Session, domain + action, in);
				programCallDocument = new ProgramCallDocument(as400Session, action, in, null, null,
						ProgramCallDocument.SOURCE_PCML);
				actionCache.put(action, programCallDocument);
			} catch (PcmlException e) {
				throw (new RpcInvocationException(e.getMessage() + " - Fail to generate pcml", e));
			} catch (ParserConfigurationException e) {
				throw (new RpcInvocationException(e.getMessage() + " - Fail to generate pcml", e));
			} catch (TransformerException e) {
				throw (new RpcInvocationException(e.getMessage() + " - Fail to generate pcml", e));
			}

		}
		try {
			setParameters(programCallDocument, rpcInvokeAction.getFields(), action, 0, null);
			logger.debug("Calling programCallDocument for " + action);
			boolean result = programCallDocument.callProgram(action);
			if (result == false) {
				AS400Message[] msgs = programCallDocument.getMessageList(action);
				String msgId, msgText, exceptionString = "";
				// Iterate through messages and write them to standard output
				for (AS400Message msg : msgs) {
					msgId = msg.getID();
					msgText = msg.getText();
					exceptionString = exceptionString + "    " + msgId + " - " + msgText;
				}
				logger.debug(exceptionString);
				throw (new RpcInvocationException(exceptionString));

			}
			getParameters(programCallDocument, rpcInvokeAction.getFields(), action, 0, null);
		} catch (PcmlException e) {
			throw (new RpcInvocationException(e.getMessage() + " - Make sure connection settings are well defined", e));

		}
		SimpleRpcResult rpcResult = new SimpleRpcResult();
		rpcResult.setRpcFields(rpcInvokeAction.getFields());
		sequence++;
		return rpcResult;

	}

	private void setParameters(ProgramCallDocument programCallDocument, List<RpcField> fields, String path, int indexLevel,
			int[] indices) throws PcmlException {

		for (RpcField field : fields) {

			String varPath = path + "." + field.getName();

			if (field instanceof RpcStructureField) {
				setParameters(programCallDocument, ((RpcStructureField)field).getChildrens(), varPath, indexLevel, indices);
			} else if (field instanceof RpcStructureListField) {

				if (indexLevel == 0) {
					indices = new int[1];
				} else {

					int[] oldIndices = indices;
					indices = new int[indexLevel + 1];
					System.arraycopy(oldIndices, 0, indices, 0, indexLevel + 1);
				}

				int count = ((RpcStructureListField)field).count();
				for (int i = 0; i < count; i++) {
					indices[indexLevel] = i;

					setParameters(programCallDocument, ((RpcStructureListField)field).getChildren(i), varPath, indexLevel + 1,
							indices);
				}

			} else {
				if (((RpcFlatField)field).getValue() == null) {
					continue;
				}
				Object value = getValue((RpcFlatField)field);

				if (indexLevel == 0) {
					logger.debug("Stting value to" + varPath + " " + value);
					programCallDocument.setValue(varPath, value);
				} else {
					logger.debug("Stting value to" + varPath + " " + value + " indices " + indices);
					programCallDocument.setValue(varPath, indices, value);
				}
			}

		}

	}

	@SuppressWarnings("static-method")
	private Object getValue(RpcFlatField field) {
		Object result = field.getValue();
		Class<?> clazz = field.getType();

		if (clazz == Integer.class || clazz == Long.class || clazz == Short.class) {

			String imageName = "%0" + field.getLength() + "d";
			return String.format(imageName, Integer.parseInt(String.valueOf(result)));
		} else if (clazz == BigInteger.class) {
			String imageName = "%" + field.getLength() + "s";

			result = String.format(imageName, result.toString());
			return result;
		}

		return result;
	}

	private void getParameters(ProgramCallDocument programCallDocument, List<RpcField> fields, String path, int indexLevel,
			int[] indices) throws PcmlException {

		for (RpcField field : fields) {

			if (field.getDirection() == Direction.INPUT) {

				continue;
			}

			String varPath = path + "." + field.getName();

			if (field instanceof RpcStructureField) {
				getParameters(programCallDocument, ((RpcStructureField)field).getChildrens(), varPath, indexLevel, indices);
			} else if (field instanceof RpcStructureListField) {

				if (indexLevel == 0) {
					indices = new int[1];
				} else {
					int[] oldIndices = indices;
					indices = new int[indexLevel + 1];
					System.arraycopy(oldIndices, 0, indices, 0, indexLevel + 1);
				}
				indexLevel += 1;
				int count = ((RpcStructureListField)field).count();
				for (int i = 0; i < count; i++) {
					indices[indexLevel - 1] = i;
					getParameters(programCallDocument, ((RpcStructureListField)field).getChildren(i), varPath, indexLevel,
							indices);

				}

			} else {

				if (indexLevel == 0) {
					((RpcFlatField)field).setValue(programCallDocument.getValue(varPath));
				} else {
					((RpcFlatField)field).setValue(programCallDocument.getValue(varPath, indices));
				}
			}

		}

	}

	@Override
	public RpcSnapshot getSnapshot() {
		return null;
	}

	@Override
	public RpcSnapshot fetchSnapshot() {
		return null;
	}

	@Override
	public void doAction(RpcInvokeAction invokeAction) {
		invoke(invokeAction);
	}

	@Override
	public Integer getSequence() {
		return sequence;
	}

	@Override
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

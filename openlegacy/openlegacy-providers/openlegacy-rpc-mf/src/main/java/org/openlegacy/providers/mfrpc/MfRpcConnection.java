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
package org.openlegacy.providers.mfrpc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.support.SimpleRpcResult;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class MfRpcConnection implements RpcConnection {

	private HttpClient httpClient;

	private HttpPost httpPost = new HttpPost();

	private InvokeActionToMFBin invokeActionToMFBin = new InvokeActionToMFBin();
	private Integer sequence = 0;
	private boolean isLogin = false;

	private final static Log logger = LogFactory.getLog(MfRpcConnection.class);

	private String codePage;

	// private static String url = "http://192.86.32.142:12345/openlegacy";

	private static byte msgLevel = 0;

	private static byte version = 1;
	private static byte function = 1;

	private static final byte SPACE = 0x40;

	public void setUri(String url) {
		try {
			httpPost.setURI(new URI(url));
		} catch (URISyntaxException e) {
			throw new OpenLegacyProviderException("Failed to set Uri");
		}
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public Object getDelegate() {
		return httpClient;
	}

	public void disconnect() {
		if (httpClient != null) {
			isLogin = false;
			httpClient = null;
		}
	}

	public boolean isConnected() {
		return httpClient != null && isLogin == true;
	}

	public RpcResult invoke(RpcInvokeAction rpcInvokeAction) {

		try {
			byte[] payload = invokeActionToMFBin.serilize(rpcInvokeAction, codePage);
			byte[] buffer = new byte[14 + payload.length];
			Arrays.fill(buffer, SPACE);

			buffer[0] = 0;
			buffer[1] = version;
			buffer[2] = msgLevel; // message level
			buffer[3] = function;

			byte[] ebcdta = rpcInvokeAction.getRpcPath().getBytes(codePage);
			System.arraycopy(ebcdta, 0, buffer, 4, ebcdta.length);

			System.arraycopy(payload, 0, buffer, 14, payload.length);
			httpPost.setEntity(new ByteArrayEntity(buffer));
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}

			BufferedInputStream in = new BufferedInputStream(response.getEntity().getContent());

			byte[] result = new byte[payload.length];

			int bytesRead = 0;
			int offset = 0;
			while (offset < payload.length) {
				bytesRead = in.read(result, offset, result.length - offset);
				if (bytesRead == -1) {
					break;
				}
				offset += bytesRead;
			}
			in.close();
			invokeActionToMFBin.deserilize(rpcInvokeAction.getFields(), result, codePage, 0);

		} catch (IOException e) {
			return null;
		}

		SimpleRpcResult rpcResult = new SimpleRpcResult();
		rpcResult.setRpcFields(rpcInvokeAction.getFields());
		sequence++;
		return rpcResult;

	}

	public RpcSnapshot getSnapshot() {
		return null;
	}

	public RpcSnapshot fetchSnapshot() {
		return null;
	}

	public void doAction(RpcInvokeAction invokeAction) {
		invoke(invokeAction);
	}

	public Integer getSequence() {
		return sequence;
	}

	public void login(String user, String password) {
		try {
			// request.setURI(new URI(loginpath));
			// httpClient.execute(request);

			isLogin = true;

		} catch (Exception e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

	public String getCodePage() {
		return codePage;
	}

	public void setCodePage(String codePage) {
		this.codePage = codePage;
	}

}

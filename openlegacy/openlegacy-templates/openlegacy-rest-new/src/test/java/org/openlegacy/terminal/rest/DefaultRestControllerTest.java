package org.openlegacy.terminal.rest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.junit.Assert;
import org.junit.Test;
import org.openlegacy.test.utils.AssertUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DefaultRestControllerTest {

	private static final String PROTOCOL = "http://";
	private static final String HOST = "localhost";
	private static final String PORT = "8080";

	private static final String BASE_URL = MessageFormat.format("{0}{1}:{2}/openlegacy-rest-new/", PROTOCOL, HOST, PORT);
	private static final String USER = "user1";
	private static final String PASSWORD = "pwd1";

	private final static Log logger = LogFactory.getLog(DefaultRestControllerTest.class);

	@Test
	public void testInventoryRestApplicationJson() throws IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		try {
			// authenticate(httpClient);
			String result = execute(httpClient, "ItemsList.json", "json");
			byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ItemsList.json.expected"));
			AssertUtils.assertContent(expectedBytes, result.getBytes());

			// send to field positionTo=5, focus on positionTo
			postContent(httpClient, "ItemsList.json?action=numberSeq", "json",
					"{\"positionTo\":\"5\",\"focusField\":\"PositionTo\"}");

		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}
	}

	@Test
	public void testRoot() throws IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		try {
			execute(httpClient, "", "json");
			execute(httpClient, "logoff", "json");
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}
	}

	@Test
	public void testBadRequest() throws IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		try {
			execute(httpClient, "Dummy", "json");
		} catch (RuntimeException e) {
			if (!e.getMessage().equals("400")) {
				Assert.assertEquals("400", e.getMessage(), "Expected HTTP error 400");
			}
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}
	}

	@Test
	public void testInventoryRestApplicationJsonMenu() throws IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		try {
			// authenticate(httpClient);
			String result = execute(httpClient, "menu", "json");
			byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("menu.json.expected"));
			AssertUtils.assertContent(expectedBytes, result.getBytes());
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}
	}

	@Test
	public void testInventoryRestApplicationXml() throws IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		try {
			String result = execute(httpClient, "ItemsList.xml", "xml");
			byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ItemsList.xml.expected"));
			AssertUtils.assertContent(expectedBytes, result.getBytes());

			// send to field positionTo=5, focus on positionTo
			postContent(
					httpClient,
					"ItemsList.xml?action=numberSeq",
					"xml",
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?><screenEntity xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:java=\"http://java.sun.com\" xsi:type=\"java:apps.inventory.screens.ItemsList\"><positionTo>5</positionTo><focusField>positionTo</focusField></screenEntity>");
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}
	}

	@Test
	public void testInventoryRestApplicationXmlMenu() throws IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		try {
			// authenticate(httpClient);
			String result = execute(httpClient, "menu", "xml");
			byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("menu.xml.expected"));
			AssertUtils.assertContent(expectedBytes, result.getBytes());
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}
	}

	@SuppressWarnings("unused")
	private static void authenticate(DefaultHttpClient httpClient) {
		HttpHost targetHost = new HttpHost(HOST, Integer.valueOf(PORT), PROTOCOL);

		List<String> authpref = new ArrayList<String>();
		authpref.add(AuthPolicy.BASIC);
		httpClient.getParams().setParameter(AuthPNames.PROXY_AUTH_PREF, authpref);

		httpClient.getCredentialsProvider().setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()),
				new UsernamePasswordCredentials(USER, PASSWORD));

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);

		// Add AuthCache to the execution context
		BasicHttpContext localcontext = new BasicHttpContext();
		localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);
	}

	private static String execute(HttpClient httpClient, String relativeUrl, String type) throws ClientProtocolException,
			IOException {
		HttpGet httpGet = new HttpGet(BASE_URL + relativeUrl);
		httpGet.addHeader("Content-Type", "application/" + type);
		httpGet.addHeader("Accept", "application/" + type);
		HttpResponse response = httpClient.execute(httpGet);
		HttpEntity httpEntity = response.getEntity();
		String result = IOUtils.toString(httpEntity.getContent());

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode >= 400) {
			logger.error("statusCode=" + statusCode);
			logger.error(result);
			throw (new RuntimeException(String.valueOf(statusCode)));
		}

		return result;
	}

	public String postContent(HttpClient httpClient, String relativeUrl, String type, String content)
			throws ClientProtocolException, IOException {

		HttpPost post = new HttpPost(BASE_URL + relativeUrl);
		post.setHeader("Content-Type", "application/" + type);
		post.addHeader("Accept", "application/" + type);
		post.setEntity(new StringEntity(content, "UTF-8"));
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpClient.execute(post, responseHandler);
		return response;
	}
}

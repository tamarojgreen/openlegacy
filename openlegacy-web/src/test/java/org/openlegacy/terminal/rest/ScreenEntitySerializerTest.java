package org.openlegacy.terminal.rest;

import apps.inventory.screens.ItemsList;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.test.utils.AssertUtils;
import org.openlegacy.utils.StringUtil;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.xml.transform.stream.StreamResult;

@ContextConfiguration("/test-web-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ScreenEntitySerializerTest extends AbstractTest {

	@Inject
	private ScreenEntitySerializer screenEntitySerializer;

	@Test
	public void testJsonSerialization() throws IOException {
		TerminalSession terminalSession = newTerminalSession();
		ItemsList itemList = terminalSession.getEntity(ItemsList.class);
		Object result = screenEntitySerializer.createSerializationContainer((ScreenEntity)itemList, terminalSession);
		ObjectMapper mapper = new ObjectMapper();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JsonGenerator generator = mapper.getJsonFactory().createJsonGenerator(baos, JsonEncoding.UTF8);
		mapper.writeValue(generator, result);

		String resultContent = StringUtil.toString(baos);
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ItemsList.json.expected"));
		AssertUtils.assertContent(expectedBytes, resultContent.getBytes());
	}

	@Test
	public void testJsonDesiralization() throws JsonParseException, JsonMappingException, IOException {
		ItemsList itemsList = screenEntitySerializer.deserialize("{\"positionTo\":\"5\",\"focusField\":\"positionTo\"}",
				ItemsList.class);
		Assert.notNull(itemsList);
	}

	@Test
	public void testXmlSerialization() throws IOException {
		TerminalSession terminalSession = newTerminalSession();
		ItemsList itemList = terminalSession.getEntity(ItemsList.class);
		Object wrapper = screenEntitySerializer.createSerializationContainer((ScreenEntity)itemList, terminalSession);

		ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
		CastorMarshaller marshaller = new CastorMarshaller();
		marshaller.afterPropertiesSet();
		marshaller.marshal(wrapper, new StreamResult(bos));
		String result = new String(bos.toByteArray());
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ItemsList.xml.expected"));
		AssertUtils.assertContent(expectedBytes, result.getBytes());

	}
}

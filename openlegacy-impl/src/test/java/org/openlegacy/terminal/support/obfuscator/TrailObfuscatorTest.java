package org.openlegacy.terminal.support.obfuscator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TrailObfuscator;
import org.openlegacy.terminal.modules.trail.TerminalPersistedTrail;
import org.openlegacy.test.utils.AssertUtils;
import org.openlegacy.utils.XmlSerializationUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

@ContextConfiguration("TrailObfuscatorTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TrailObfuscatorTest extends AbstractTest {

	@Inject
	private TrailObfuscator trailObfuscator;

	@Test
	public void testObfuscator() throws JAXBException, IOException {
		InputStream trailStream = getClass().getResourceAsStream(
				"/org/openlegacy/terminal/support/obfuscator/TrailObfuscatorTest.trail");
		TerminalPersistedTrail trail = XmlSerializationUtil.deserialize(TerminalPersistedTrail.class, trailStream);
		trailObfuscator.obfuscate(trail);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XmlSerializationUtil.serialize(TerminalPersistedTrail.class, trail, baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(
				"/org/openlegacy/terminal/support/obfuscator/TrailObfuscatorTest.trail.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}
}

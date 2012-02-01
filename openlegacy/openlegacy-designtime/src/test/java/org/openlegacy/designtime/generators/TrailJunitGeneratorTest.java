package org.openlegacy.designtime.generators;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.terminal.analyzer.support.DefaultTerminalSnapshotsAnalyzer;
import org.openlegacy.designtime.terminal.generators.TrailJunitGenerator;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.modules.trail.TerminalPersistedTrail;
import org.openlegacy.test.utils.AssertUtils;
import org.openlegacy.utils.XmlSerializationUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

@ContextConfiguration("/test-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TrailJunitGeneratorTest {

	@Inject
	private DefaultTerminalSnapshotsAnalyzer snapshotsAnalyzer;

	@Test
	public void testGenerateJunit() throws Exception {

		TerminalPersistedTrail trail = XmlSerializationUtil.deserialize(TerminalPersistedTrail.class,
				getClass().getResourceAsStream("test.trail.xml"));

		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = snapshotsAnalyzer.analyzeTrail(trail);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Collection<ScreenEntityDefinition> screenDefinitions = screenEntitiesDefinitions.values();
		for (ScreenEntityDefinition screenEntityDefinition : screenDefinitions) {
			((ScreenEntityDesigntimeDefinition)screenEntityDefinition).setPackageName("com.test");
		}
		new TrailJunitGenerator().generate(screenDefinitions, "Test", baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("TrailJunit.java.expected"));

		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}
}

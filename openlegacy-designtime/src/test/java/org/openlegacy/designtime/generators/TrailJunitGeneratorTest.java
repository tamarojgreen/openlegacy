package org.openlegacy.designtime.generators;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.terminal.analyzer.support.DefaultTerminalSnapshotsAnalyzer;
import org.openlegacy.designtime.terminal.generators.GenerateUtil;
import org.openlegacy.designtime.terminal.generators.TrailJunitGenerator;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.modules.trail.TerminalPersistedTrail;
import org.openlegacy.test.utils.AssertUtils;
import org.openlegacy.utils.FileUtils;
import org.openlegacy.utils.XmlSerializationUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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

		ByteArrayOutputStream baos = generate(screenEntitiesDefinitions);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("TrailJunit.java.expected"));

		AssertUtils.assertContent(expectedBytes, baos.toByteArray());

		// test custom template
		try {
			File tempFile = FileUtils.extractToTempDir(getClass().getResource("dummyTemplate.txt"), "JunitTrail.java.template");
			GenerateUtil.setTemplateDirectory(tempFile.getParentFile());
			baos = generate(screenEntitiesDefinitions);
			String result = org.openlegacy.utils.StringUtil.toString(baos);
			Assert.assertEquals("This is a dummy template for Test", result);
		} finally {
			GenerateUtil.setTemplateDirectory(null);
		}

	}

	private static ByteArrayOutputStream generate(Map<String, ScreenEntityDefinition> screenEntitiesDefinitions)
			throws TemplateException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Collection<ScreenEntityDefinition> screenDefinitions = screenEntitiesDefinitions.values();
		GenerateUtil.setPackageName(screenDefinitions, "com.test");
		new TrailJunitGenerator().generate(screenDefinitions, "Test", baos);
		return baos;
	}
}

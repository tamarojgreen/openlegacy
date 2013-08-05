package org.openlegacy.designtime.terminal.generators;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.generators.GenerateUtil;
import org.openlegacy.designtime.terminal.analyzer.support.DefaultTerminalSnapshotsAnalyzer;
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

	@Inject
	private GenerateUtil generateUtil;

	@Inject
	private TrailJunitGenerator trailJunitGenerator;

	@Test
	public void testJunit() throws Exception {
		assertGenerateJunit("test.trail", "TrailJunit.java.expected");
	}

	@Ignore("TODO fails when executed with all OpenLegacyScreenDesigntimeSuite ")
	@Test
	public void testDemoSessionJunit() throws Exception {
		assertGenerateJunit("demo_session.trail", "TrailJunitDemoSession.java.expected");
	}

	private void assertGenerateJunit(String trailPath, String expectedResource) throws Exception {

		TerminalPersistedTrail trail = XmlSerializationUtil.deserialize(TerminalPersistedTrail.class,
				getClass().getResourceAsStream(trailPath));

		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = snapshotsAnalyzer.analyzeTrail(trail);

		ByteArrayOutputStream baos = generate(screenEntitiesDefinitions);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(expectedResource));

		AssertUtils.assertContent(expectedBytes, baos.toByteArray());

		// test custom template
		try {
			File tempFile = FileUtils.extractToTempDir(getClass().getResource("dummyTemplate.txt"), "JunitTrail.java.template");
			generateUtil.setTemplateDirectory(tempFile.getParentFile());
			baos = generate(screenEntitiesDefinitions);
			String result = org.openlegacy.utils.StringUtil.toString(baos);
			Assert.assertEquals("This is a dummy template for Test", result);
		} finally {
			generateUtil.setTemplateDirectory(null);
		}

	}

	private ByteArrayOutputStream generate(Map<String, ScreenEntityDefinition> screenEntitiesDefinitions)
			throws TemplateException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Collection<ScreenEntityDefinition> screenDefinitions = screenEntitiesDefinitions.values();
		GenerateUtil.setPackageName(screenDefinitions, "com.test");
		trailJunitGenerator.generate(screenDefinitions, "Test", baos);
		return baos;
	}
}

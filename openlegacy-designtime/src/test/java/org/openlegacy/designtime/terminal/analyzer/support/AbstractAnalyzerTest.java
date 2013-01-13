package org.openlegacy.designtime.terminal.analyzer.support;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.terminal.generators.ScreenEntityJavaGenerator;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalSnapshotsLoader;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.context.ApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

public class AbstractAnalyzerTest {

	@Inject
	protected TerminalSnapshotsLoader snapshotsLoader;

	@Inject
	protected DefaultTerminalSnapshotsAnalyzer snapshotsAnalyzer;

	@Inject
	protected DefaultTerminalSnapshotsOrganizer snapshotsOrganizer;

	@Inject
	protected ScreenEntityJavaGenerator screenEntityJavaGenerator;

	@Inject
	protected ApplicationContext applicationContext;

	private final static Log logger = LogFactory.getLog(DefaultTerminalSnapshotsAnalyzerTest.class);

	protected void assertScreenContent(ScreenEntityDefinition screen, String expectedResource) throws TemplateException,
			IOException {
		((ScreenEntityDesigntimeDefinition)screen).setPackageName("com.test");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		screenEntityJavaGenerator.generate(screen, baos);

		if (expectedResource == null) {
			logger.info("\n" + new String(baos.toByteArray(), CharEncoding.UTF_8));
			return;
		}

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(expectedResource));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

}

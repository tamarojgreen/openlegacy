package org.openlegacy;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.utils.RequestMockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class AbstractTest {

	@Autowired
	protected TerminalSession terminalSession;

	@BeforeClass
	public static void beforeAllTests() {
		RequestMockUtil.initRequest();
	}

	@Before
	public void beforeTest() {
		RequestMockUtil.initRequest();
	}

	@After
	public void afterAllTests() {
		terminalSession.disconnect();
	}

	protected String readResource(String resourceName) throws IOException {
		ClassPathResource resource = new ClassPathResource(resourceName);
		return IOUtils.toString(resource.getInputStream());
	}

}

package org.openlegacy;

import org.apache.commons.io.IOUtils;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import javax.inject.Inject;

public class AbstractTest {

	@Inject
	private ApplicationContext applicationContext;

	protected TerminalSession newTerminalSession() {
		TerminalSession terminalSession = applicationContext.getBean(TerminalSession.class);
		return terminalSession;
	}

	protected String readResource(String resourceName) throws IOException {
		ClassPathResource resource = new ClassPathResource(resourceName);
		return IOUtils.toString(resource.getInputStream());
	}

}

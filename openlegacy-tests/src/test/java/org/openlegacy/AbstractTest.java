package org.openlegacy;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.core.io.ClassPathResource;

import com.someorg.examples.RequestMockUtil;

public class AbstractTest {

	@BeforeClass
	public static void beforeAllTests() {
		RequestMockUtil.initRequest();
	}

	@Before
	public void beforeTest() {
		RequestMockUtil.initRequest();
	}

	protected String readResource(String resourceName) throws IOException {
		ClassPathResource resource = new ClassPathResource(resourceName);
		return IOUtils.toString(resource.getInputStream());
	}

}

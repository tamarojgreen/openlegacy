package org.openlegacy.rpc.render;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openlegacy.terminal.render.DefaultRpcImageRenderer;
import org.openlegacy.terminal.render.RpcImageRenderer;
import org.openlegacy.test.utils.AssertUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RpcImageRendererTest {

	@Test
	public void testCobolImage() throws IOException {
		testImage("/apps/rpc/", "sample.cbl", "sample.jpg");
	}

	public void testImage(String prefix, String resourceName, String expectedImageFileName) throws IOException {
		InputStream resource = getClass().getResourceAsStream(prefix + resourceName);
		String source = IOUtils.toString(resource);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		RpcImageRenderer imageRenderer = new DefaultRpcImageRenderer();
		imageRenderer.render(source, baos);

		byte[] expectedImage = IOUtils.toByteArray(getClass().getResourceAsStream(prefix + expectedImageFileName));
		AssertUtils.assertImageContent(expectedImageFileName, expectedImage, baos.toByteArray());
	}

}

package org.openlegacy.designtime.rpc.source.parsers;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

public class CobolIdentifaiersTest {

	CopyBookFetcher copyBookFetcher = new DefaultCopyBookFetcher();
	CobolNameRecognizer cobolNameRecognizer = new DefaultCobolNameRecognizer();
	CobolLocalPartNamesFethcher localPartNamesFethcher = new DefaultCobolLocalPartNamesFethcher();

	@Test
	public void copyBookFetcherTest() throws FileNotFoundException {
		Map<String, String> copybooks = new HashMap<String, String>();
		copybooks.put("prmuser4o.cpy", "1");
		copybooks.put("prm2o.cpy", "1");
		copybooks.put("prm1i.cpy", "1");

		File file = new File(getClass().getResource("qcblsrc.esuserb.cbl").getPath());
		Map<String, InputStream> copybookStream = copyBookFetcher.getCopyBooks(file);
		Assert.assertEquals(3, copybookStream.size());
		Assert.assertEquals(copybooks.keySet(), copybookStream.keySet());
	}

	@Test
	public void cobolNameRecognizerTest() throws IOException {
		String fileName = "qcblsrc.esuserb.cbl";
		String source = IOUtils.toString(getClass().getResource(fileName));
		String entityName = cobolNameRecognizer.getEntityName(source, fileName);
		Assert.assertEquals("Esuserb", entityName);
	}

	@Test
	public void partNameFeatcherTest() throws IOException {
		String source = IOUtils.toString(getClass().getResource("qcblsrc.esuserb.cbl"));
		List<String> externalParts = new ArrayList<String>();
		externalParts.add("PRM1I");
		externalParts.add("PRM2O");
		externalParts.add("PRMUSER4O");

		Map<String, String> parts = localPartNamesFethcher.get(source, externalParts);
		Assert.assertEquals(3, parts.size());
		Assert.assertEquals("PRM1I", parts.get("Pi1"));
		Assert.assertEquals("PRM2O", parts.get("Po2"));
		Assert.assertEquals("PRMUSER4O", parts.get("Po4"));
	}
}

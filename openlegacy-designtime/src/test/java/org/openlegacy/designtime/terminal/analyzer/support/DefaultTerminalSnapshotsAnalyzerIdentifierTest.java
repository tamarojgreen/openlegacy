package org.openlegacy.designtime.terminal.analyzer.support;

import freemarker.template.TemplateException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ContextConfiguration("/test-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSnapshotsAnalyzerIdentifierTest extends AbstractAnalyzerTest {

	@Test
	/*
	 * Notes: This one gets 1 id from the header.  it could get the program name instead.
	 * 
	 */
	public void testSampleMenu() throws TemplateException, IOException {
		snapshotsOrganizer.setMatchingPercent(95);
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(
				getClass().getResource("/org/openlegacy/designtime/terminal/analyzer/support/identifiers").getFile(), "IdentifierScreen1.xml");
		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = snapshotsAnalyzer.analyzeSnapshots(snapshots);
		System.out.println(screenEntitiesDefinitions.get("PolicyProcessingTestEnvironment").getActions());
		System.out.println(screenEntitiesDefinitions.get("PolicyProcessingTestEnvironment").getScreenIdentification().getScreenIdentifiers().size());
		System.out.println(screenEntitiesDefinitions.get("PolicyProcessingTestEnvironment").getScreenIdentification().getScreenIdentifiers());
		assertScreenContent(screenEntitiesDefinitions.get("PolicyProcessingTestEnvironment"), "identifiers/IdentifierScreen1.java.expected");
	}

	@Test
	public void testSampleEntry() throws TemplateException, IOException {
		snapshotsOrganizer.setMatchingPercent(95);
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(
				getClass().getResource("/org/openlegacy/designtime/terminal/analyzer/support/identifiers").getFile(), "IdentifierScreen2.xml");
		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = snapshotsAnalyzer.analyzeSnapshots(snapshots);
		System.out.println(screenEntitiesDefinitions.keySet().iterator().next());
		System.out.println(screenEntitiesDefinitions.get("TransactionType").getActions());
		System.out.println(screenEntitiesDefinitions.get("TransactionType").getScreenIdentification().getScreenIdentifiers().size());
		System.out.println(screenEntitiesDefinitions.get("TransactionType").getScreenIdentification().getScreenIdentifiers());
		assertScreenContent(screenEntitiesDefinitions.get("TransactionType"), "identifiers/IdentifierScreen2.java.expected");
	}

}

package org.openlegacy.designtime.terminal.generators;

import freemarker.template.TemplateException;

import org.openlegacy.designtime.terminal.analyzer.support.TerminalSnapshotsAnalyzerContext.TerminalSnapshotSequenceComparator;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * A generator which generates a junit test from a given trail
 */
@Component
public class TrailJunitGenerator {

	@Inject
	private GenerateUtil generateUtil;

	public void generate(Collection<ScreenEntityDefinition> screenEntityDefinitions, String testName, OutputStream out)
			throws TemplateException, IOException {

		List<ScreenEntityDefinition> sortedScreenEntityDefintions = sortEntityDefinitions(screenEntityDefinitions);

		GeneratedApi generatedApi = generateContentApiCalls(testName, sortedScreenEntityDefintions);

		generateUtil.generate(generatedApi, out, "JunitTrail.java.template", "");
	}

	private static List<ScreenEntityDefinition> sortEntityDefinitions(Collection<ScreenEntityDefinition> screenEntityDefinitions) {
		List<ScreenEntityDefinition> sortedScreenEntityDefintions = new ArrayList<ScreenEntityDefinition>();
		sortedScreenEntityDefintions.addAll(screenEntityDefinitions);
		Collections.sort(sortedScreenEntityDefintions, TerminalSnapshotSequenceComparator.instance());
		return sortedScreenEntityDefintions;
	}

	private static GeneratedApi generateContentApiCalls(String testName, List<ScreenEntityDefinition> screenEntityDefinitions) {
		GeneratedApi generatedApi = new GeneratedApi();
		generatedApi.setTestName(testName);

		ScreenEntityDefinition firstScreenEntityDefinition = screenEntityDefinitions.get(0);
		String entityName = firstScreenEntityDefinition.getEntityName();
		String variableName = StringUtil.toJavaFieldName(entityName);
		generatedApi.getApiCalls().add(
				MessageFormat.format("{0} {1} = terminalSession.getEntity({0}.class);", entityName, variableName));

		for (ScreenEntityDefinition screenEntityDefinition : screenEntityDefinitions) {

			generatedApi.getRefferedClasses().add(
					MessageFormat.format("{0}.{1}", screenEntityDefinition.getPackageName(),
							screenEntityDefinition.getEntityName()));
			ScreenEntityDefinition accessedFromScreenDefinition = screenEntityDefinition.getAccessedFromScreenDefinition();
			TerminalSnapshot accessedFromSnapshot = screenEntityDefinition.getAccessedFromSnapshot();
			if (accessedFromScreenDefinition != null && accessedFromSnapshot != null) {
				Collection<ScreenFieldDefinition> fields = accessedFromScreenDefinition.getFieldsDefinitions().values();
				for (ScreenFieldDefinition screenFieldDefinition : fields) {
					TerminalField terminalField = accessedFromSnapshot.getField(screenFieldDefinition.getPosition());
					if (terminalField.isModified()) {
						generatedApi.getApiCalls().add(
								MessageFormat.format("{0}.{1}({2});", variableName,
										StringUtil.toSetterMethodName(screenFieldDefinition.getName()),
										StringUtil.surroundStringWithQuotes(terminalField.getValue())));
					}

				}

				String nextEntityName = screenEntityDefinition.getEntityName();
				generatedApi.getApiCalls().add(
						MessageFormat.format("{0} {1} = terminalSession.doAction(TerminalActions.ENTER(),{2},{0}.class);",
								nextEntityName, StringUtil.toJavaFieldName(nextEntityName), variableName));

				entityName = screenEntityDefinition.getEntityName();
				variableName = StringUtil.toJavaFieldName(entityName);
			}
		}
		return generatedApi;

	}

	public static class GeneratedApi {

		private String testName;
		private List<String> refferedClasses = new ArrayList<String>();
		private List<String> apiCalls = new ArrayList<String>();

		public List<String> getApiCalls() {
			return apiCalls;
		}

		public List<String> getRefferedClasses() {
			return refferedClasses;
		}

		public String getTestName() {
			return testName;
		}

		public void setTestName(String testName) {
			this.testName = testName;
		}

	}
}

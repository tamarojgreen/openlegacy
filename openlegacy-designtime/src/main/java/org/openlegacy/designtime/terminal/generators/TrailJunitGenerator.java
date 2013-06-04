/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.designtime.terminal.generators;

import freemarker.template.TemplateException;

import org.openlegacy.designtime.generators.GenerateUtil;
import org.openlegacy.designtime.terminal.analyzer.support.TerminalSnapshotsAnalyzerContext.TerminalSnapshotSequenceComparator;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalActionMapper;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.utils.StringUtil;

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
public class TrailJunitGenerator {

	@Inject
	private GenerateUtil generateUtil;

	@Inject
	private TerminalActionMapper terminalActionMapper;

	public void generate(Collection<ScreenEntityDefinition> screenEntityDefinitions, String testName, OutputStream out)
			throws TemplateException, IOException {

		List<ScreenEntityDefinition> sortedScreenEntityDefintions = sortEntityDefinitions(screenEntityDefinitions);

		GeneratedApi generatedApi = generateContentApiCalls(testName, sortedScreenEntityDefintions);

		if (generatedApi != null) {
			generateUtil.generate(generatedApi, out, "JunitTrail.java.template");
		}
	}

	private static List<ScreenEntityDefinition> sortEntityDefinitions(Collection<ScreenEntityDefinition> screenEntityDefinitions) {
		List<ScreenEntityDefinition> sortedScreenEntityDefintions = new ArrayList<ScreenEntityDefinition>();
		sortedScreenEntityDefintions.addAll(screenEntityDefinitions);
		Collections.sort(sortedScreenEntityDefintions, TerminalSnapshotSequenceComparator.instance());
		return sortedScreenEntityDefintions;
	}

	private GeneratedApi generateContentApiCalls(String testName, List<ScreenEntityDefinition> screenEntityDefinitions) {
		GeneratedApi generatedApi = new GeneratedApi();
		generatedApi.setTestName(testName);

		if (screenEntityDefinitions.size() == 0) {
			return null;
		}

		ScreenEntityDefinition firstScreenEntityDefinition = screenEntityDefinitions.get(0);
		String entityName = firstScreenEntityDefinition.getEntityName();
		String variableName = StringUtil.toJavaFieldName(entityName);
		generatedApi.getApiCalls().add(
				MessageFormat.format("{0} {1} = terminalSession.getEntity({0}.class);", entityName, variableName));

		for (int i = 0; i < screenEntityDefinitions.size(); i++) {

			ScreenEntityDesigntimeDefinition screenEntityDefinition = (ScreenEntityDesigntimeDefinition)screenEntityDefinitions.get(i);

			generatedApi.getRefferedClasses().add(
					MessageFormat.format("{0}.{1}", screenEntityDefinition.getPackageName(),
							screenEntityDefinition.getEntityName()));
			TerminalSnapshot outgoingSnapshot = screenEntityDefinition.getOutgoingSnapshot();
			if (outgoingSnapshot != null) {
				Collection<ScreenFieldDefinition> fields = screenEntityDefinition.getFieldsDefinitions().values();
				for (ScreenFieldDefinition screenFieldDefinition : fields) {
					TerminalField terminalField = outgoingSnapshot.getField(screenFieldDefinition.getPosition());
					if (terminalField != null && terminalField.isModified()) {
						generatedApi.getApiCalls().add(
								MessageFormat.format("{0}.{1}({2});", variableName,
										StringUtil.toSetterMethodName(screenFieldDefinition.getName()),
										StringUtil.surroundStringWithQuotes(terminalField.getValue())));
					}

				}

				String nextEntityName = i < screenEntityDefinitions.size() ? screenEntityDefinitions.get(i + 1).getEntityName()
						: null;

				if (nextEntityName != null) {
					String action = terminalActionMapper.getAction(outgoingSnapshot.getCommand()).toString();
					generatedApi.getApiCalls().add(
							MessageFormat.format("{0} {1} = terminalSession.doAction(TerminalActions.{2}(),{3},{0}.class);",
									nextEntityName, StringUtil.toJavaFieldName(nextEntityName), action, variableName));
				}

				entityName = nextEntityName;
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

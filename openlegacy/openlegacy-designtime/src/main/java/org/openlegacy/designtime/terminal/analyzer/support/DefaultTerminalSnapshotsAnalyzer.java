package org.openlegacy.designtime.terminal.analyzer.support;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.openlegacy.designtime.analyzer.SnapshotsSorter;
import org.openlegacy.designtime.terminal.analyzer.ScreenEntityDefinitionsBuilder;
import org.openlegacy.designtime.terminal.analyzer.TerminalSnapshotsAnalyzer;
import org.openlegacy.designtime.utils.DroolsUtil;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class DefaultTerminalSnapshotsAnalyzer implements TerminalSnapshotsAnalyzer {

	@Inject
	private SnapshotsSorter<TerminalSnapshot> snapshotsSorter;

	@Inject
	private ScreenEntityDefinitionsBuilder screenEntityDefinitionsBuilder;

	private String[] droolsResources;

	private String processName;

	public Map<String, ScreenEntityDefinition> analyzeSnapshots(List<TerminalSnapshot> snapshots) {

		TerminalSnapshotsAnalyzerContext snapshotsAnalyzerContext = new TerminalSnapshotsAnalyzerContext();
		snapshotsAnalyzerContext.setActiveSnapshots(snapshots);

		KnowledgeBase knowledgeBase = DroolsUtil.createKnowledgeBase(droolsResources);
		StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();
		session.setGlobal("snapshotsAnalyzerContext", snapshotsAnalyzerContext);
		session.setGlobal("snapshotsSorter", snapshotsSorter);
		session.setGlobal("screenEntityDefinitionsBuilder", screenEntityDefinitionsBuilder);

		try {
			session.startProcess(processName);
			session.fireAllRules();
		} finally {
			session.dispose();
		}

		return snapshotsAnalyzerContext.getEntitiesDefinitions();
	}

	public void setDroolsResources(String[] droolsResources) {
		this.droolsResources = droolsResources;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}
}

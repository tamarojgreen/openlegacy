package org.openlegacy.designtime.terminal.analyzer.support;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.openlegacy.designtime.analyzer.SnapshotsSorter;
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

	public Map<String, ScreenEntityDefinition> analyzeSnapshots(List<TerminalSnapshot> snapshots) {

		TerminalSnapshotsAnalyzerContext snapshotsAnalyzerContext = new TerminalSnapshotsAnalyzerContext();
		snapshotsAnalyzerContext.setActiveSnapshots(snapshots);

		KnowledgeBase knowledgeBase = DroolsUtil.createKnowledgeBase("basicRule.drl");
		StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();
		session.setGlobal("snapshotsAnalyzerContext", snapshotsAnalyzerContext);
		session.setGlobal("snapshotsSorter", snapshotsSorter);

		try {
			session.fireAllRules();
		} finally {
			session.dispose();
		}

		return snapshotsAnalyzerContext.getEntitiesDefinitions();
	}
}

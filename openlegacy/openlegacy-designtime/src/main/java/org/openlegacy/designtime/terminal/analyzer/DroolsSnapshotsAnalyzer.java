package org.openlegacy.designtime.terminal.analyzer;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.openlegacy.Snapshot;
import org.openlegacy.designtime.analyzer.SnapshotsSorter;
import org.openlegacy.designtime.utils.DroolsUtil;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.mock.MockTerminalScreen;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

@Component
public class DroolsSnapshotsAnalyzer implements TerminalSnapshotsAnalyzer {

	@Inject
	private SnapshotsSorter<TerminalSnapshot> snapshotsSorter;

	public List<ScreenEntityDefinition> analyzeSnapshots(List<TerminalSnapshot> snapshots) {

		snapshotsSorter.add(snapshots);

		Collection<TerminalSnapshot> singlesSnapshots = snapshotsSorter.getFirstOfEachGroup();

		KnowledgeBase knowledgeBase = DroolsUtil.createKnowledgeBase("basicRule.drl");
		StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();
		try {
			List<ScreenEntityDefinition> definitions = new ArrayList<ScreenEntityDefinition>();
			session.setGlobal("definitions", definitions);
			for (Snapshot snapshot : singlesSnapshots) {
				session.insert(new MockTerminalScreen((TerminalSnapshot)snapshot));
			}
			session.fireAllRules();
		} finally {
			session.dispose();
		}
		return null;
	}

}

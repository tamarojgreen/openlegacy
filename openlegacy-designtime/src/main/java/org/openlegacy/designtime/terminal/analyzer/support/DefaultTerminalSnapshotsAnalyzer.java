package org.openlegacy.designtime.terminal.analyzer.support;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.openlegacy.FieldFormatter;
import org.openlegacy.designtime.analyzer.SnapshotsOrganizer;
import org.openlegacy.designtime.analyzer.SnapshotsSimilarityChecker;
import org.openlegacy.designtime.rules.RuleDefinition;
import org.openlegacy.designtime.rules.RuleParametersSet;
import org.openlegacy.designtime.rules.support.RuleParametersSetBean;
import org.openlegacy.designtime.terminal.analyzer.ScreenEntityDefinitionsBuilder;
import org.openlegacy.designtime.terminal.analyzer.TerminalSnapshotsAnalyzer;
import org.openlegacy.designtime.utils.DroolsUtil;
import org.openlegacy.exceptions.UnableToLoadSnapshotException;
import org.openlegacy.terminal.TerminalFieldsSplitter;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.module.TerminalSessionTrail;
import org.openlegacy.terminal.modules.trail.TerminalPersistedTrail;
import org.openlegacy.utils.XmlSerializationUtil;
import org.springframework.beans.factory.InitializingBean;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

public class DefaultTerminalSnapshotsAnalyzer implements TerminalSnapshotsAnalyzer, InitializingBean {

	@Inject
	private SnapshotsOrganizer<TerminalSnapshot> snapshotsOrganizer;

	@Inject
	private ScreenEntityDefinitionsBuilder screenEntityDefinitionsBuilder;

	@Inject
	private SnapshotsSimilarityChecker<TerminalSnapshot> snapshotsSimilarityChecker;

	@Inject
	private FieldFormatter fieldFormatter;

	@Inject
	private TerminalFieldsSplitter fieldsSplitter;

	private RuleDefinition[] ruleDefinitions;

	private String processName;

	private KnowledgeBase knowledgeBase;

	private List<String> droolsResources = new ArrayList<String>();

	private List<RuleParametersSet> ruleParametersFacts;

	private static final Logger logger = Logger.getLogger(DefaultTerminalSnapshotsAnalyzer.class);

	public Map<String, ScreenEntityDefinition> analyzeTrail(InputStream inputStream) {
		TerminalSessionTrail trail;
		try {
			trail = XmlSerializationUtil.deserialize(TerminalPersistedTrail.class, inputStream);
		} catch (JAXBException e) {
			throw (new UnableToLoadSnapshotException(e));
		}
		return analyzeTrail(trail);
	}

	public Map<String, ScreenEntityDefinition> analyzeTrail(TerminalSessionTrail trail) {
		return analyzeSnapshots(trail.getSnapshots());
	}

	public Map<String, ScreenEntityDefinition> analyzeSnapshots(List<TerminalSnapshot> snapshots) {

		snapshotsOrganizer.clear();

		TerminalSnapshotsAnalyzerContext snapshotsAnalyzerContext = new TerminalSnapshotsAnalyzerContext();
		snapshotsAnalyzerContext.setActiveSnapshots(snapshots);

		KnowledgeBase knowledgeBase = getKnowledgeBase();

		long beforeCreateSession = Calendar.getInstance().getTimeInMillis();

		StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();

		for (RuleParametersSet ruleParametersSet : ruleParametersFacts) {
			session.insert(ruleParametersSet);

		}

		session.setGlobal("fieldFormatter", fieldFormatter);
		session.setGlobal("snapshotsAnalyzerContext", snapshotsAnalyzerContext);
		session.setGlobal("snapshotsOrganizer", snapshotsOrganizer);
		session.setGlobal("screenEntityDefinitionsBuilder", screenEntityDefinitionsBuilder);
		session.setGlobal("snapshotsSimilarityChecker", snapshotsSimilarityChecker);
		session.setGlobal("fieldsSplitter", fieldsSplitter);

		try {

			KnowledgeRuntimeLogger droolsLogger = null;
			if (logger.isDebugEnabled()) {
				long afterCreateSession = Calendar.getInstance().getTimeInMillis();
				long timeCreateSession = afterCreateSession - beforeCreateSession;
				logger.debug("Time to create session: " + timeCreateSession);
				droolsLogger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(session);
			}
			long beforeExecuteRules = Calendar.getInstance().getTimeInMillis();

			session.startProcess(processName);
			session.fireAllRules();

			if (logger.isDebugEnabled()) {
				droolsLogger.close();
				long afterExecuteRules = Calendar.getInstance().getTimeInMillis();
				long timeExecuteRules = afterExecuteRules - beforeExecuteRules;
				logger.debug("Time to execute rules: " + timeExecuteRules);
			}

		} finally {
			session.dispose();
		}

		return snapshotsAnalyzerContext.getEntitiesDefinitions();
	}

	private KnowledgeBase getKnowledgeBase() {
		if (knowledgeBase == null) {
			knowledgeBase = DroolsUtil.createKnowledgeBase(droolsResources.toArray(new String[droolsResources.size()]));
		}
		return knowledgeBase;
	}

	public void setRuleDefinitions(RuleDefinition[] ruleDefinitions) {
		this.ruleDefinitions = ruleDefinitions;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	private void initialize() {
		ruleParametersFacts = new ArrayList<RuleParametersSet>();
		for (RuleDefinition ruleDefinition : ruleDefinitions) {
			if (!ruleDefinition.isEnable()) {
				continue;
			}
			droolsResources.add(ruleDefinition.getDroolsFile());

			List<RuleParametersSet> ruleParametersSets = ruleDefinition.getRuleParameterSets();
			for (RuleParametersSet ruleParametersSet : ruleParametersSets) {
				((RuleParametersSetBean)ruleParametersSet).setRuleId(ruleDefinition.getRuleId());
				ruleParametersFacts.add(ruleParametersSet);
			}
		}
	}

	public void afterPropertiesSet() throws Exception {
		initialize();
		getKnowledgeBase();
	}
}

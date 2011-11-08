package org.openlegacy.analyzer.screen;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.utils.JaxbUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

public class DroolsScreensAnalyzer implements ScreensAnalyzer {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage:\njava ScreensTextDumper screens-resource-folder");
			return;
		}
		String root = args[0];

		try {
			List<TerminalSnapshot> snapshots = loadSnapshots(root);
			new DroolsScreensAnalyzer().analyzeScreens(snapshots);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static List<TerminalSnapshot> loadSnapshots(String root) throws FileNotFoundException, JAXBException {
		File rootFolder = new File(root);
		File[] files = rootFolder.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		});

		List<TerminalSnapshot> snapshots = new ArrayList<TerminalSnapshot>();
		for (File file : files) {
			TerminalSnapshot terminalSnapshot = JaxbUtil.unmarshal(TerminalPersistedSnapshot.class, new FileInputStream(file));
			snapshots.add(terminalSnapshot);
		}
		return snapshots;
	}

	public List<ScreenEntityDefinition> analyzeScreens(List<TerminalSnapshot> snapshots) {
		KnowledgeBase knowledgeBase = DroolsUtil.createKnowledgeBase();
		StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();
		try {
			for (TerminalSnapshot terminalSnapshot : snapshots) {
				session.insert(terminalSnapshot);
			}
			session.fireAllRules();
		} finally {
			session.dispose();
		}
		return null;
	}

}

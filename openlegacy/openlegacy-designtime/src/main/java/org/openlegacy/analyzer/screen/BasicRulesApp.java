package org.openlegacy.analyzer.screen;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;

public class BasicRulesApp {

	public static final void main(String[] args) {
		KnowledgeBase knowledgeBase = DroolsUtil.createKnowledgeBase();
		StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();
		try {
			// Account account = new Account();
			// account.setBalance(50);
			// session.insert(account);
			session.fireAllRules();
		} finally {
			session.dispose();
		}
	}

}

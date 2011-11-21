package org.openlegacy.designtime.utils;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;

public class DroolsUtil {

	public static KnowledgeBase createKnowledgeBase(String... droolFiles) {
		KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		for (String droolFile : droolFiles) {
			builder.add(ResourceFactory.newClassPathResource(droolFile), ResourceType.DRL);
		}
		if (builder.hasErrors()) {
			throw new RuntimeException(builder.getErrors().toString());
		}
		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());
		return knowledgeBase;
	}
}

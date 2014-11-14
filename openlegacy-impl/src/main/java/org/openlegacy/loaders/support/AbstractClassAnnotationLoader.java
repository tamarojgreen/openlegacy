/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.loaders.support;

import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

public abstract class AbstractClassAnnotationLoader implements ClassAnnotationsLoader {

	private ConfigurableListableBeanFactory beanFactory;

	@Override
	public int compareTo(ClassAnnotationsLoader o) {
		Order order1 = AnnotationUtils.findAnnotation(getClass(), Order.class);
		Order order2 = AnnotationUtils.findAnnotation(o.getClass(), Order.class);
		int order1Value = order1 != null ? order1.value() : Ordered.LOWEST_PRECEDENCE;
		int order2Value = order2 != null ? order2.value() : Ordered.LOWEST_PRECEDENCE;

		return order1Value - order2Value;
	}

	public void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public ConfigurableListableBeanFactory getBeanFactory() {
		return beanFactory;
	}

}

/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.loaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

import java.beans.PropertyChangeEvent;

/**
 * Mark extended class with @Component
 * */
public abstract class ServiceRegistryPostProcessor implements Comparable<ServiceRegistryPostProcessor> {

	@Autowired
	protected ApplicationContext applicationContext;

	public abstract void init();

	/**
	 * Called after service registry load
	 * */
	public abstract void postLoad();

	/**
	 * Called after service method/pool definitions changed
	 * */
	public abstract void postProcess(PropertyChangeEvent event);

	@Override
	public int compareTo(ServiceRegistryPostProcessor o) {
		Order order1 = AnnotationUtils.findAnnotation(getClass(), Order.class);
		Order order2 = AnnotationUtils.findAnnotation(o.getClass(), Order.class);
		int order1Value = order1 != null ? order1.value() : Ordered.LOWEST_PRECEDENCE;
		int order2Value = order2 != null ? order2.value() : Ordered.LOWEST_PRECEDENCE;

		return order1Value - order2Value;
	}
}

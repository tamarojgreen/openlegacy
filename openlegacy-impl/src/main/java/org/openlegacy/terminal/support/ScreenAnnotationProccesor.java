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
package org.openlegacy.terminal.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.support.AbstractAnnotationProccesor;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.beans.factory.BeanFactory;

/**
 * Open legacy integration point with spring component-scan. The classes are scanned for @ScreenEntity annotation and all the
 * annotations information is extracted and kept in ScreenEntitiesRegistry
 * 
 * @param <T>
 */
public class ScreenAnnotationProccesor extends AbstractAnnotationProccesor {

	@Override
	protected EntitiesRegistry<?, ?> getEntitiesRegistry(BeanFactory beanFactory) {
		return beanFactory.getBean(ScreenEntitiesRegistry.class);
	}

}

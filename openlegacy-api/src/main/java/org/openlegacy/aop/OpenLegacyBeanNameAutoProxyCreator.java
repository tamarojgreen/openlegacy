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

package org.openlegacy.aop;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;

public class OpenLegacyBeanNameAutoProxyCreator extends BeanNameAutoProxyCreator {

	private static final long serialVersionUID = 1L;
	private BeanProxyChecker checker;

	@Override
	protected Object[] getAdvicesAndAdvisorsForBean(Class beanClass, String beanName, TargetSource targetSource) {
		if (checker.needToProxy(beanClass)) {
			return super.getAdvicesAndAdvisorsForBean(beanClass, beanName, targetSource);
		} else {
			return DO_NOT_PROXY;
		}
	}

	public void setChecker(BeanProxyChecker checker) {
		this.checker = checker;
	}

}

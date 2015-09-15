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

package org.openlegacy.services.definitions;

import java.util.List;

public interface ServiceMethodDefinition extends ServiceChild {

	String getName();// annotation

	String getMethodName();// method

	Long getCacheDuration();

	List<ServiceParamDetailsDefinition> getInputParams();

	List<ServiceParamDetailsDefinition> getOutputParams();
}

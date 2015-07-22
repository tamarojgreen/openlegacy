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
package org.openlegacy.annotations.rpc;

import org.openlegacy.annotations.screen.AnnotationConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcPart {

	String displayName() default AnnotationConstants.NULL;

	String name() default AnnotationConstants.NULL;

	String helpText() default AnnotationConstants.NULL;

	String legacyContainerName() default AnnotationConstants.NULL;

	boolean virtual() default false;

	/**
	 * Stores ordered list of WSDL elements' names
	 */
	String[] expandedElements() default {};
}

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

package org.openlegacy.annotations.db;

import org.openlegacy.annotations.screen.AnnotationConstants;
import org.openlegacy.db.actions.DbAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Action {

	Class<? extends DbAction> action();

	String displayName() default "";

	boolean global() default true;

	String alias() default "";

	Class<?> targetEntity() default void.class;

	boolean rolesRequired() default false;

	String[] roles() default AnnotationConstants.NULL;
}
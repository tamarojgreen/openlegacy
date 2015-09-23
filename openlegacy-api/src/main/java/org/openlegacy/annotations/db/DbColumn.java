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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ivan Bort
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DbColumn {

	String displayName() default "";

	boolean password() default false;

	String sampleValue() default "";

	String defaultValue() default "";

	String helpText() default "";

	boolean rightToLeft() default false;

	boolean internal() default false;

	boolean mainDisplayField() default false;

	String[] roles() default AnnotationConstants.NULL;

}

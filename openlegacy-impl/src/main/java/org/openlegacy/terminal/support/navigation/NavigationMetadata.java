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
package org.openlegacy.terminal.support.navigation;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openlegacy.SessionModuleMetadata;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NavigationMetadata implements SessionModuleMetadata {

	private Map<SourceTarget, List<NavigationDefinition>> navigationCache = new HashMap<SourceTarget, List<NavigationDefinition>>();

	public void add(ScreenEntityDefinition source, ScreenEntityDefinition target, List<NavigationDefinition> navigationSteps) {

		navigationCache.put(new SourceTarget(source, target), navigationSteps);
	}

	public List<NavigationDefinition> get(ScreenEntityDefinition source, ScreenEntityDefinition target) {
		return navigationCache.get(new SourceTarget(source, target));
	}

	private static class SourceTarget {

		private ScreenEntityDefinition source;
		private ScreenEntityDefinition target;

		public SourceTarget(ScreenEntityDefinition source, ScreenEntityDefinition target) {
			this.source = source;
			this.target = target;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof SourceTarget)) {
				return false;
			}
			SourceTarget other = (SourceTarget)obj;
			return new EqualsBuilder().append(source, other.source).append(target, other.target).isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(source).append(target).toHashCode();
		}
	}
}

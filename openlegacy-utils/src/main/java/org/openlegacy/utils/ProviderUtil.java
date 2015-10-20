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

package org.openlegacy.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class ProviderUtil {

	private static final String[] RPC = { "openlegacy-db-stored-proc", "openlegacy-jt400", "openlegacy-rpc-mf",
			"openlegacy-ws-rpc", };
	private static final String[] TERMINAL = { "openlegacy-applinx", "openlegacy-h3270", "openlegacy-tn5250j" };

	public enum Solution {
		RPC,
		TERMINAL,
		DB
	}

	public static Solution getSolutionFromProvider(String providerArtifactId) {
		if (new ArrayList<String>(Arrays.asList(RPC)).contains(providerArtifactId)) {
			return Solution.RPC;
		} else if (new ArrayList<String>(Arrays.asList(TERMINAL)).contains(providerArtifactId)) {
			return Solution.TERMINAL;
		} else {
			return Solution.DB;
		}
	}
}

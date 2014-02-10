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
package com.as400samplecode;

import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.rpc.SourceFetcher;
import org.openlegacy.utils.jt400.Jt400SourceFetcher;

public class FetchSource {

	public static void main(String[] args) throws OpenLegacyException {

		if (args.length < 3) {
			System.out.println("Usage:" + SourceFetcher.class.getSimpleName() + " host user password");
			return;
		}

		String host = args[0];
		String user = args[1];
		String password = args[2];
		Jt400SourceFetcher fetchSourceUtil = new Jt400SourceFetcher();
		// System.out.println(fetchSourceUtil.fetch(host, user, password, "/QSYS.LIB/RMR2L1.LIB/QRPGLESRC.FILE/RPGROICH.MBR"));
		byte[] result = fetchSourceUtil.fetch(host, user, password, "/QSYS.LIB/RMR2L1.LIB/QCBLSRC.FILE/ITEMS.MBR");
		System.out.println(new String(result));

	}
}

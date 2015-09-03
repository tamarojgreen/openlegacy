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

package org.openlegacy.services;

import org.openlegacy.annotations.services.Service;

@Service(name = "WebService")
public class WebServiceImpl implements WebService {

	ItemDetails[] details = new ItemDetails[] { new ItemDetails(1000), new ItemDetails(1001), new ItemDetails(1002),
			new ItemDetails(1003), new ItemDetails(1004) };

	@Override
	public ItemDetails getItem(int id) {
		try {
			return details[id - 1000];
		} catch (Exception e) {
			return null;
		}
	}

}

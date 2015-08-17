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

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtils {

	public static byte[] toByteArray(Object... objs) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			for (Object obj : objs) {
				oos.writeObject(obj);
			}
			oos.close();
			byte[] result = baos.toByteArray();
			baos.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object fromByteArray(byte[] obj) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(obj);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object result = ois.readObject();
			ois.close();
			bais.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}

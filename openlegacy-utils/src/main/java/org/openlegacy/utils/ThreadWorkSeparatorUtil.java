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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadWorkSeparatorUtil {

	public static interface OnRun {

		public void onRun(Object obj);
	}

	private static class LocalRunnable implements Runnable {

		private List<?> data;
		private OnRun onRun;

		public LocalRunnable(List<?> data, OnRun onRun) {
			this.data = data;
			this.onRun = onRun;
		}

		@Override
		public void run() {
			if (data == null || onRun == null || data.isEmpty()) {
				return;
			}

			for (Object obj : data) {
				onRun.onRun(obj);
			}
		}
	}

	public static class ThreadWorkSeparator {

		private int processorCount = Runtime.getRuntime().availableProcessors();
		private ExecutorService executor;

		public <T extends OnRun> T getOnRun(Class<T> onRun) {
			try {
				return onRun.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		}

		public <T extends OnRun> void start(List<?> processData, Class<T> onRun) throws Exception {
			int from = 0, to = 0;
			int count = processData.size();

			int dataPerThreadCount = count / processorCount;
			dataPerThreadCount = dataPerThreadCount == 0 ? 1 : dataPerThreadCount;
			executor = Executors.newFixedThreadPool(processorCount);

			int i = 0;
			while (from < count) {
				to = from + dataPerThreadCount >= count ? from + (count - from) : from + dataPerThreadCount;
				executor.execute(new LocalRunnable(processData.subList(from, to), getOnRun(onRun)));
				from = to;
				i++;
			}

			executor.shutdown();
			while (!executor.isShutdown()) {

			}
		}

	}

	public static ThreadWorkSeparator newInstance() throws Exception {
		return new ThreadWorkSeparator();
	}

}
package org.openlegacy.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.loaders.RegistryLoader;
import org.openlegacy.utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for designtime to support loading modified classes
 * 
 * @author Roi Mor
 * 
 */
public class RegistryReloader {

	private Map<File, Long> packageModified = new HashMap<File, Long>();
	private String apiSourcesPath = "src/main/java";;

	private long compileInterval = 3000;

	public RegistryReloader(String apiSourcesPath, long compileInterval) {
		this.apiSourcesPath = apiSourcesPath;
		this.compileInterval = compileInterval;
	}

	public void scanForChanges(final File webApp, final EntitiesRegistry<?, ?, ?> registry, final RegistryLoader registryLoader) {
		File projectRoot = webApp.getParentFile().getParentFile().getParentFile();

		File sourcesDir = new File(projectRoot, apiSourcesPath);
		List<String> packages = registry.getPackages();
		for (String package1 : packages) {
			String packageRelativeDir = package1.replaceAll("\\.", "/");
			File packageSourceDir = new File(sourcesDir, packageRelativeDir);
			Long packageLastModified = packageModified.get(packageSourceDir);
			// find all classes
			if (packageLastModified != null) {
				List<File> javaSources = getSourcesByLastModified(packageSourceDir);

				long fileLastModified = packageLastModified;
				for (File file : javaSources) {
					if (packageLastModified < file.lastModified()) {
						try {
							Thread.sleep(compileInterval);
							String className = package1 + "." + FileUtils.fileWithoutAnyExtension(file.getName());
							Class<?> clazz = Class.forName(className);
							Class<?>[] classes = clazz.getClasses();
							for (Class<?> class1 : classes) {
								class1 = Class.forName(class1.getName());
								registryLoader.loadSingleClass(registry, class1, false);
							}
							registryLoader.loadSingleClass(registry, clazz, true);
							fileLastModified = file.lastModified();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						break;
					}
				}
				if (fileLastModified > packageLastModified) {
					packageModified.put(packageSourceDir, fileLastModified);
				}
			} else {
				List<File> javaSources = getSourcesByLastModified(packageSourceDir);
				long fileModified = packageSourceDir.lastModified();
				if (javaSources.size() > 0) {
					fileModified = javaSources.get(0).lastModified();
				}
				packageModified.put(packageSourceDir, fileModified);
			}
		}
	}

	private static List<File> getSourcesByLastModified(File sourcesDir) {
		File[] files = sourcesDir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(".java");
			}
		});
		if (files == null){
			return new ArrayList<File>();
		}
		List<File> javaSources = Arrays.asList(files);
		Collections.sort(javaSources, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				return o1.lastModified() > o2.lastModified() ? -1 : 1;
			}
		});
		return javaSources;
	};

	public void setCompileInterval(long compileInterval) {
		this.compileInterval = compileInterval;
	}

	public void setSourcesPath(String sourcesPath) {
		this.apiSourcesPath = sourcesPath;
	}
}

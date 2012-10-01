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
package org.openlegacy.designtime.newproject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.PreferencesConstants;
import org.openlegacy.designtime.newproject.model.ProjectProvider;
import org.openlegacy.designtime.newproject.model.ProjectTheme;
import org.openlegacy.designtime.newproject.model.ProjectType;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.utils.XmlSerializationUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Retrieves metadata
 * 
 */
public class NewProjectMetadataRetriever {

	private final static Log logger = LogFactory.getLog(NewProjectMetadataRetriever.class);

	@XmlRootElement(name = "project-types")
	private static class ProjectTypeStore {

		private List<ProjectType> projectTypes;

		@XmlElements({ @XmlElement(name = "project-type", type = ProjectType.class) })
		public List<ProjectType> getProjectTypes() {
			return projectTypes;
		}

		@SuppressWarnings("unused")
		public void setProjectTypes(List<ProjectType> projectTypes) {
			this.projectTypes = projectTypes;
		}
	}

	@XmlRootElement(name = "providers")
	private static class ProjectProviderStore {

		private List<ProjectProvider> providers;

		@XmlElements({ @XmlElement(name = "provider", type = ProjectProvider.class) })
		public List<ProjectProvider> getProviders() {
			return providers;
		}

		@SuppressWarnings("unused")
		public void setProviders(List<ProjectProvider> providers) {
			this.providers = providers;
		}
	}

	@XmlRootElement(name = "themes")
	private static class ProjectThemeStore {

		private List<ProjectTheme> themes;

		@XmlElements({ @XmlElement(name = "theme", type = ProjectTheme.class) })
		public List<ProjectTheme> getThemes() {
			return themes;
		}

		@SuppressWarnings("unused")
		public void setThemes(List<ProjectTheme> themes) {
			this.themes = themes;
		}
	}

	private static final String RESOURCE_XML_PATH = "/templates/xml";
	private static final String RESOURCE_THEME_IMAGES_PATH = "/templates/theme-images";
	private static final String ONLINE_XML_FOLDER = "xml";
	private static final String ONLINE_THEME_IMAGES_FOLDER = "theme-images";

	private ProjectTypeStore projectTypeStore = null;
	private ProjectProviderStore projectProviderStore = null;
	private ProjectThemeStore projectThemeStore = null;

	private String templatesUrl = null;

	private boolean isRetrievedOnline = false;

	private ITemplateFetcher templateFetcher;

	public NewProjectMetadataRetriever(String templatesUrl) {
		setTemplatesUrl(templatesUrl);
	}

	public void retrieveMetadata() throws OpenLegacyException, JAXBException, IOException {
		try {
			this.fetchStoresOnline();
			this.isRetrievedOnline = true;
			this.templateFetcher = new OnlineTemplateFetcher(this.templatesUrl);
		} catch (Exception e) {
			logger.warn("Cannot retrieve metadata online", e);
			this.fetchStores();
			this.isRetrievedOnline = false;
			this.templateFetcher = new ResourceTemplateFetcher();
		}
	}

	public List<ProjectType> getProjectTypes() {
		if (this.projectTypeStore == null) {
			return new ArrayList<ProjectType>();
		}
		return this.projectTypeStore.getProjectTypes();
	}

	public List<ProjectProvider> getProviders() {
		if (this.projectProviderStore == null) {
			return new ArrayList<ProjectProvider>();
		}
		return this.projectProviderStore.getProviders();
	}

	public List<ProjectTheme> getThemes() {
		if (this.projectThemeStore == null) {
			return new ArrayList<ProjectTheme>();
		}
		return this.projectThemeStore.getThemes();
	}

	public void setTemplatesUrl(String templatesUrl) {
		this.templatesUrl = templatesUrl;
	}

	public boolean isRetrievedOnline() {
		return isRetrievedOnline;
	}

	public String getTemplatesUrl() {
		return this.templatesUrl;
	}

	public ITemplateFetcher getTemplateFetcher() {
		return templateFetcher;
	}

	private void fetchStoresOnline() throws IOException, JAXBException, OpenLegacyException {
		this.projectTypeStore = this.fetchStoreOnline(ProjectTypeStore.class, PreferencesConstants.PROJECT_TYPES_FILENAME);
		this.projectProviderStore = this.fetchStoreOnline(ProjectProviderStore.class,
				PreferencesConstants.PROJECT_PROVIDERS_FILENAME);
		this.projectThemeStore = this.fetchStoreOnline(ProjectThemeStore.class, PreferencesConstants.PROJECT_THEMES_FILENAME);

		this.fetchThemeImagesOnline();

		if ((this.projectTypeStore.getProjectTypes() == null) || (this.projectProviderStore.getProviders() == null)
				|| (this.projectThemeStore.getThemes() == null)) {
			throw new OpenLegacyException("Cannot retrieve metadata online from " + this.templatesUrl);
		}
	}

	private void fetchStores() throws JAXBException, IOException, OpenLegacyException {
		this.projectTypeStore = this.fetchStore(ProjectTypeStore.class, PreferencesConstants.PROJECT_TYPES_FILENAME);
		this.projectProviderStore = this.fetchStore(ProjectProviderStore.class, PreferencesConstants.PROJECT_PROVIDERS_FILENAME);
		this.projectThemeStore = this.fetchStore(ProjectThemeStore.class, PreferencesConstants.PROJECT_THEMES_FILENAME);

		this.fetchThemeImages();
		if ((this.projectTypeStore.getProjectTypes() == null) || (this.projectProviderStore.getProviders() == null)
				|| (this.projectThemeStore.getThemes() == null)) {
			throw new OpenLegacyException("Cannot retrieve metadata from resources");
		}
	}

	private <P> P fetchStoreOnline(Class<P> rootClass, String filename) throws IOException, JAXBException {
		InputStream in = getUrlConnectionInputStream(MessageFormat.format("{0}/{1}/{2}", this.templatesUrl, ONLINE_XML_FOLDER,
				filename));
		P store = XmlSerializationUtil.deserialize(rootClass, in);
		in.close();

		return store;
	}

	private byte[] fetchImageOnline(String imageFile) throws IOException {
		InputStream in = getUrlConnectionInputStream(MessageFormat.format("{0}/{1}/{2}", this.templatesUrl,
				ONLINE_THEME_IMAGES_FOLDER, imageFile));
		return getImageData(in);
	}

	private void fetchThemeImagesOnline() throws IOException, NullPointerException {
		for (ProjectTheme theme : this.projectThemeStore.getThemes()) {
			theme.setImageData(this.fetchImageOnline(theme.getImageFile()));
		}
	}

	private <P> P fetchStore(Class<P> rootClass, String filename) throws JAXBException, IOException {
		InputStream in = getClass().getResourceAsStream(MessageFormat.format("{0}/{1}", RESOURCE_XML_PATH, filename));
		P store = XmlSerializationUtil.deserialize(rootClass, in);
		in.close();
		return store;
	}

	private byte[] fetchImage(String imageFile) throws IOException {
		InputStream in = getClass().getResourceAsStream(MessageFormat.format("{0}/{1}", RESOURCE_THEME_IMAGES_PATH, imageFile));
		return getImageData(in);
	}

	private void fetchThemeImages() throws IOException, NullPointerException {
		for (ProjectTheme theme : this.projectThemeStore.getThemes()) {
			theme.setImageData(this.fetchImage(theme.getImageFile()));
		}
	}

	private static InputStream getUrlConnectionInputStream(String urlPath) throws IOException {
		URL url = new URL(urlPath);
		URLConnection con = url.openConnection();
		con.setDoInput(true);
		con.setDoOutput(false);
		con.connect();

		return con.getInputStream();
	}

	private static byte[] getImageData(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];
		int n = 0;
		while ((n = in.read(buff)) != -1) {
			out.write(buff, 0, n);
		}
		out.close();
		in.close();

		return out.toByteArray();
	}

}

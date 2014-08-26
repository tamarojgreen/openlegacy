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
package org.openlegacy.designtime.newproject.organized;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.PreferencesConstants;
import org.openlegacy.designtime.newproject.ITemplateFetcher;
import org.openlegacy.designtime.newproject.organized.model.HostType;
import org.openlegacy.designtime.newproject.organized.model.ProjectTheme;
import org.openlegacy.designtime.newproject.organized.model.ProjectType;
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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Retrieves metadata
 * 
 */
public class NewProjectMetadataRetriever {

	private final static Log logger = LogFactory.getLog(NewProjectMetadataRetriever.class);

	private static final String RESOURCE_XML_PATH = "/templates/xml.organized";
	private static final String RESOURCE_THEME_IMAGES_PATH = "/templates/theme-images";
	private static final String ONLINE_XML_FOLDER = "xml.organized";
	private static final String ONLINE_THEME_IMAGES_FOLDER = "theme-images";

	private ProjectTypeStore projectTypeStore = null;
	private HostTypesStore hostTypeStore = null;
	private ProjectThemeStore projectThemeStore = null;

	private String templatesUrl = null;

	private boolean isRetrievedOnline = false;

	private ITemplateFetcher templateFetcher;

	public NewProjectMetadataRetriever(String templatesUrl) {
		this.templatesUrl = templatesUrl;
	}

	public void retrieveMetadata() throws OpenLegacyException, JAXBException, IOException {
		try {
			fetchStoresOnline();
			isRetrievedOnline = true;
			templateFetcher = new OnlineTemplateFetcher(templatesUrl);
		} catch (Exception e) {
			logger.warn("Cannot retrieve metadata online", e);
			fetchStores();
			isRetrievedOnline = false;
			templateFetcher = new ResourceTemplateFetcher();
		}
	}

	public List<ProjectType> getProjectTypes() {
		if (projectTypeStore == null) {
			return new ArrayList<ProjectType>();
		}
		return projectTypeStore.getProjectTypes();
	}

	public List<HostType> getHostTypes() {
		if (hostTypeStore == null) {
			return new ArrayList<HostType>();
		}
		return hostTypeStore.getHostTypes();
	}

	public List<ProjectTheme> getThemes() {
		if (projectThemeStore == null) {
			return new ArrayList<ProjectTheme>();
		}
		return projectThemeStore.getThemes();
	}

	public List<String> getBackendSolutions() {
		if (projectTypeStore == null || projectTypeStore.getBackendSolutions() == null) {
			return new ArrayList<String>();
		}
		return projectTypeStore.getBackendSolutions();
	}

	public List<String> getFrontendSolutions() {
		if (projectTypeStore == null || projectTypeStore.getFrontendSolutions() == null) {
			return new ArrayList<String>();
		}
		return projectTypeStore.getFrontendSolutions();
	}

	public boolean isRetrievedOnline() {
		return isRetrievedOnline;
	}

	public String getTemplatesUrl() {
		return templatesUrl;
	}

	public ITemplateFetcher getTemplateFetcher() {
		return templateFetcher;
	}

	private void fetchStoresOnline() throws IOException, JAXBException, OpenLegacyException {
		projectTypeStore = fetchStoreOnline(ProjectTypeStore.class, PreferencesConstants.PROJECT_TYPES_FILENAME);
		hostTypeStore = fetchStoreOnline(HostTypesStore.class, PreferencesConstants.PROJECT_HOST_TYPES_FILENAME);
		projectThemeStore = fetchStoreOnline(ProjectThemeStore.class, PreferencesConstants.PROJECT_THEMES_FILENAME);

		fetchThemeImagesOnline();

		if (!projectTypeStore.isDataExist() || !hostTypeStore.isDataExist() || !projectThemeStore.isDataExist()) {
			throw new OpenLegacyException("Cannot retrieve metadata online from " + templatesUrl);
		}
	}

	private void fetchStores() throws JAXBException, IOException, OpenLegacyException {
		this.projectTypeStore = this.fetchStore(ProjectTypeStore.class, PreferencesConstants.PROJECT_TYPES_FILENAME);
		this.hostTypeStore = this.fetchStore(HostTypesStore.class, PreferencesConstants.PROJECT_HOST_TYPES_FILENAME);
		this.projectThemeStore = this.fetchStore(ProjectThemeStore.class, PreferencesConstants.PROJECT_THEMES_FILENAME);

		this.fetchThemeImages();
		if (!projectTypeStore.isDataExist() || !hostTypeStore.isDataExist() || !projectThemeStore.isDataExist()) {
			throw new OpenLegacyException("Cannot retrieve metadata from resources");
		}
	}

	private <P> P fetchStoreOnline(Class<P> rootClass, String filename) throws IOException, JAXBException {
		InputStream in = getUrlConnectionInputStream(MessageFormat.format("{0}/{1}/{2}", templatesUrl, ONLINE_XML_FOLDER,
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

	@XmlRootElement(name = "project-types")
	private static class ProjectTypeStore {

		private List<ProjectType> projectTypes;
		private List<String> backendSolutions;
		private List<String> frontendSolutions;

		@XmlElements({ @XmlElement(name = "project-type", type = ProjectType.class) })
		public List<ProjectType> getProjectTypes() {
			return projectTypes;
		}

		@SuppressWarnings("unused")
		public void setProjectTypes(List<ProjectType> projectTypes) {
			this.projectTypes = projectTypes;
		}

		@XmlElementWrapper(name = "backend-solutions")
		@XmlElement(name = "solution")
		public List<String> getBackendSolutions() {
			return backendSolutions;
		}

		@SuppressWarnings("unused")
		public void setBackendSolutions(List<String> backendSolutions) {
			this.backendSolutions = backendSolutions;
		}

		@XmlElementWrapper(name = "frontend-solutions")
		@XmlElement(name = "solution")
		public List<String> getFrontendSolutions() {
			return frontendSolutions;
		}

		@SuppressWarnings("unused")
		public void setFrontendSolutions(List<String> frontendSolutions) {
			this.frontendSolutions = frontendSolutions;
		}

		public boolean isDataExist() {
			return projectTypes != null && backendSolutions != null && frontendSolutions != null;
		}

	}

	@XmlRootElement(name = "host-types")
	private static class HostTypesStore {

		private List<HostType> hostTypes;

		@XmlElements({ @XmlElement(name = "host-type", type = HostType.class) })
		public List<HostType> getHostTypes() {
			return hostTypes;
		}

		@SuppressWarnings("unused")
		public void setHostTypes(List<HostType> hostTypes) {
			this.hostTypes = hostTypes;
		}

		public boolean isDataExist() {
			return hostTypes != null;
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

		public boolean isDataExist() {
			return themes != null;
		}
	}

}

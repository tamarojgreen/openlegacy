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

package org.openlegacy.designtime.newproject.organized.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Ivan Bort
 * 
 */
@XmlRootElement(name = "db-type")
public class DbType {

	private String name;
	private String databaseDriver;
	private String databaseUrl;
	private String mavenDependency;
	private String ddlAuto;
	private String dialect;

	public DbType() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatabaseDriver() {
		return databaseDriver;
	}

	@XmlElement(name = "database-driver")
	public void setDatabaseDriver(String databaseDriver) {
		this.databaseDriver = databaseDriver;
	}

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	@XmlElement(name = "database-url")
	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	public String getMavenDependency() {
		return mavenDependency;
	}

	@XmlElement(name = "maven-dependency")
	public void setMavenDependency(String mavenDependency) {
		this.mavenDependency = mavenDependency;
	}

	public String getDdlAuto() {
		return ddlAuto;
	}

	@XmlElement(name = "ddl-auto")
	public void setDdlAuto(String ddlAuto) {
		this.ddlAuto = ddlAuto;
	}

	public String getDialect() {
		return dialect;
	}

	@XmlElement(name = "dialect")
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
}

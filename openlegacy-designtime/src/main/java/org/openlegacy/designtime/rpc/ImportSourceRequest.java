package org.openlegacy.designtime.rpc;

import org.openlegacy.designtime.UserInteraction;

public class ImportSourceRequest {

	private String user;
	private String pwd;
	private String host;
	private String workingDirPath;
	private String legacyFile;
	private String newFileName;

	private UserInteraction userInteraction;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getWorkingDirPath() {
		return workingDirPath;
	}

	public void setWorkingDirPath(String workingDirPath) {
		this.workingDirPath = workingDirPath;
	}

	public String getLegacyFile() {
		return legacyFile;
	}

	public void setLegacyFile(String legacyFile) {
		this.legacyFile = legacyFile;
	}

	public UserInteraction getUserInteraction() {
		return userInteraction;
	}

	public void setUserInteraction(UserInteraction userInteraction) {
		this.userInteraction = userInteraction;
	}

	public String getNewFileName() {
		return newFileName;
	}

	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}

}

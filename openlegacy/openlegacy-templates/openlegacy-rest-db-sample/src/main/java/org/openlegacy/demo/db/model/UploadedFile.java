package org.openlegacy.demo.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UploadedFile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String fileName;

	private String filePath;

	@ManyToOne
	private Integer owner_id;

	public Integer getId() {
		return this.id;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}

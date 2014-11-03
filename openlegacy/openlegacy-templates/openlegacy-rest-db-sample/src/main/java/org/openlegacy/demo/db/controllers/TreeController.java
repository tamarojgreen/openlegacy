package org.openlegacy.demo.db.controllers;

import org.apache.commons.io.IOUtils;
import org.openlegacy.demo.db.model.ProductItem;
import org.openlegacy.demo.db.model.UploadedFile;
import org.openlegacy.demo.db.services.DbService;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TreeController {

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private DbService dbService;

	private String defaultFileStoragePath = String.format("%s/olNodesFiles/", System.getProperty("user.home"));

	@RequestMapping(value = "/productTree", method = RequestMethod.GET)
	public ModelAndView getEntitiesTree() {
		Query query = entityManager.createNativeQuery("SELECT * FROM PRODUCT_ITEM WHERE parent_id IS NULL", ProductItem.class);
		List<ProductItem> resultList = query.getResultList();
		return new ModelAndView("model", "model", ProxyUtil.getTargetJpaObject(resultList, true));
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
	public void fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("nodeName") String nodeName) {
		String storagePath = defaultFileStoragePath + nodeName + "/";
		storagePath = storagePath.replace("{user.home}", System.getProperty("user.home"));

		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			File filesStorageDir = new File(storagePath);
			if (!filesStorageDir.exists()) {
				filesStorageDir.mkdirs();
			}

			inputStream = file.getInputStream();
			File newFile = new File(storagePath + file.getOriginalFilename());
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			outputStream = new FileOutputStream(newFile);
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			Query query = entityManager.createNativeQuery(
					String.format("SELECT * FROM PRODUCT_ITEM WHERE product_name='%s'", nodeName), ProductItem.class);
			try {
				Object result = query.getSingleResult();

				UploadedFile uploadedFile = new UploadedFile();
				uploadedFile.setFileName(file.getOriginalFilename());
				uploadedFile.setFilePath(newFile.getAbsolutePath());
				uploadedFile.setProductItem((ProductItem)result);
				dbService.saveEntity(uploadedFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/nodeFilesList", method = RequestMethod.GET)
	public ModelAndView getNodeFilesList(@RequestParam("nodeName") String nodeName) {
		String storagePath = defaultFileStoragePath + nodeName + "/";
		storagePath = storagePath.replace("{user.home}", System.getProperty("user.home"));

		List<String> filesNames = new ArrayList<String>();

		File nodeFolderPath = new File(storagePath);
		if (nodeFolderPath.exists()) {
			File[] listOfFiles = nodeFolderPath.listFiles();
			for (File file : listOfFiles) {
				if (file.isFile()) {
					filesNames.add(file.getName());
				}
			}
		}

		List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();

		try {
			Query query = entityManager.createNativeQuery(String.format("SELECT id FROM PRODUCT_ITEM WHERE product_name='%s'",
					nodeName));
			Object productId = query.getSingleResult();

			query = entityManager.createNativeQuery("SELECT file_name FROM UPLOADED_FILE WHERE product_id=" + productId);

			uploadedFiles = query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("model", "model", uploadedFiles);
	}

	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	public void excel(@RequestParam("nodeName") String nodeName, @RequestParam("fileName") String fileName,
			HttpServletResponse response) throws IOException {
		String storagePath = defaultFileStoragePath + nodeName + "/";
		storagePath = storagePath.replace("{user.home}", System.getProperty("user.home"));

		File fileToDownload = new File(storagePath + fileName);
		if (fileToDownload.exists()) {
			response.setContentType("application/*");
			response.addHeader("Content-Disposition", MessageFormat.format("attachment; filename=\"{0}\"", fileName));
			IOUtils.copy(new FileInputStream(fileToDownload), response.getOutputStream());
			response.flushBuffer();
		}
	}

	public void setDefaultFileStoragePath(String defaultFileStoragePath) {
		this.defaultFileStoragePath = defaultFileStoragePath;
	}
}

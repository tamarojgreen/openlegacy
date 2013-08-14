package org.openlegacy.designtime.generators;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityDefinition;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.mains.GenerateControllerRequest;
import org.openlegacy.designtime.mains.GenerateViewRequest;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.layout.PageDefinition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;

import javax.inject.Inject;

/**
 * Abstract class for generating SPA (Single Page Application) related content
 * 
 * @author roi
 * 
 */
public abstract class AbstractEntitySpaGenerator implements EntityPageGenerator {

	private static final String CONTROLLER_CODE_PLACE_HOLDER_START = "/* Controller code place-holder start";
	private static final String CONTROLLER_CODE_PLACE_HOLDER_END = "Controller code place-holder end */";
	private static final String REGISTER_CONTROLLER_PLACE_HOLDER_START = "/* Register controller place-holder start";
	private static final String REGISTER_CONTROLLER_PLACE_HOLDER_END = "Register controller place-holder end */";
	private static final String APP_JS = "app.js";
	private static final String CONTROLLERS_JS = "controllers.js";

	private final static Log logger = LogFactory.getLog(AbstractEntitySpaGenerator.class);

	@Inject
	private GenerateUtil generateUtil;

	public void generateView(GenerateViewRequest generateViewRequest, EntityDefinition<?> entityDefinition)
			throws GenerationException {

		getGenerateUtil().setTemplateDirectory(generateViewRequest.getTemplatesDirectory());

		UserInteraction userInteraction = generateViewRequest.getUserInteraction();
		FileOutputStream fos = null;
		try {

			PageDefinition pageDefinition = buildPage(entityDefinition);

			// generate web view
			generateView(generateViewRequest, pageDefinition, SpaGenerateUtil.VIEWS_DIR, userInteraction, false);

			GenerateControllerRequest generateControllerRequest = new GenerateControllerRequest();
			generateControllerRequest.setProjectPath(generateViewRequest.getProjectPath());
			generateControllerRequest.setEntitySourceFile(generateViewRequest.getEntitySourceFile());
			generateControllerRequest.setSourceDirectory(generateControllerRequest.getSourceDirectory());
			generateControllerRequest.setTemplatesDirectory(generateControllerRequest.getTemplatesDirectory());
			generateControllerRequest.setUserInteraction(userInteraction);
			generateControllerFromView(generateControllerRequest, entityDefinition);
		} catch (Exception e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	private void generateView(GenerateViewRequest generatePageRequest, PageDefinition pageDefinition, String viewsDir,
			UserInteraction overrideConfirmer, boolean isComposite) throws IOException {

		EntityDefinition<?> entityDefinition = pageDefinition.getEntityDefinition();
		String entityClassName = entityDefinition.getEntityClassName();
		FileOutputStream fos = null;

		File pageFile = new File(generatePageRequest.getProjectPath(), MessageFormat.format("{0}{1}.html", viewsDir,
				entityClassName));
		boolean pageFileExists = pageFile.exists();
		boolean generateView = true;
		if (pageFileExists) {
			boolean override = overrideConfirmer.isOverride(pageFile);
			if (!override) {
				generateView = false;
			}
		}
		if (generateView) {
			pageFile.getParentFile().mkdirs();
			fos = new FileOutputStream(pageFile);
			try {
				generatePage(pageDefinition, fos, "");
				logger.info(MessageFormat.format("Generated html file: {0}", pageFile.getAbsoluteFile()));
			} finally {
				IOUtils.closeQuietly(fos);
				org.openlegacy.utils.FileUtils.deleteEmptyFile(pageFile);
			}

		}
	}

	protected abstract PageDefinition buildPage(EntityDefinition<?> entityDefinition);

	public void generateController(GenerateControllerRequest generateControllerRequest, EntityDefinition<?> entityDefinition)
			throws GenerationException {
		// do nothing (for now)
	}

	private static void generateControllerFromView(GenerateControllerRequest generateControllerRequest,
			EntityDefinition<?> entityDefinition) throws GenerationException {
		updateAppJs(generateControllerRequest, entityDefinition);
		updateControllersJs(generateControllerRequest, entityDefinition);
	}

	private static void updateControllersJs(GenerateControllerRequest generateControllerRequest,
			EntityDefinition<?> entityDefinition) {
		File controllersJsFile = new File(generateControllerRequest.getProjectPath(), SpaGenerateUtil.JS_APP_DIR + CONTROLLERS_JS);

		GenerateUtil.replicateTemplate(controllersJsFile, entityDefinition.getEntityName(), entityDefinition,
				CONTROLLER_CODE_PLACE_HOLDER_START, CONTROLLER_CODE_PLACE_HOLDER_END);
	}

	private static void updateAppJs(GenerateControllerRequest generateControllerRequest, EntityDefinition<?> entityDefinition) {
		File appJsFile = new File(generateControllerRequest.getProjectPath(), SpaGenerateUtil.JS_APP_DIR + APP_JS);
		GenerateUtil.replicateTemplate(appJsFile, entityDefinition.getEntityName(), entityDefinition,
				REGISTER_CONTROLLER_PLACE_HOLDER_START, REGISTER_CONTROLLER_PLACE_HOLDER_END);
	}

	public GenerateUtil getGenerateUtil() {
		return generateUtil;
	}

	public boolean isSupportControllerGeneration() {
		return false;
	}
}

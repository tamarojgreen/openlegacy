package org.openlegacy.log;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.openlegacy.SessionAction;

import java.util.List;

public class ApiLoggerImpl implements ApiLogger {

	private static final Log log = LogFactory.getLog(ApiLogger.class);

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private Level level;

	private FileAppender fileAppender;

	private Logger logger;

	public ApiLoggerImpl() {
		fileAppender = new FileAppender();
		fileAppender.setName("ApiLoggerFileAppender");
		// fileAppender.setFile(logFileName);
		fileAppender.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		fileAppender.setThreshold(org.apache.log4j.Level.ALL);
		fileAppender.setAppend(true);
		fileAppender.activateOptions();

		logger = Logger.getLogger(ApiLogger.class);
		// logger.addAppender(fileAppender);
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public String getLogFileName() {
		return fileAppender.getFile();
	}

	public void setLogFileName(String logFileName) {
		fileAppender.setFile(logFileName);
		fileAppender.activateOptions();

		logger.getLoggerRepository().resetConfiguration();
		logger.addAppender(fileAppender);
	}

	@Override
	public void log(Class<? extends SessionAction<?>> actionClass, Object entity, List<Object> keys) {
		String message;
		if (level == Level.INFO) {
			message = "entity: " + entity.getClass().getSimpleName() + "; action: " + actionClass.getSimpleName();
			log.info(message);
		}
		if (level == Level.TRACE) {
			boolean firstParam = true;
			StringBuilder sb = new StringBuilder("(");
			for (Object k : keys) {

				if (!firstParam) {
					sb.append(",");
				}
				sb.append(k.toString());

				if (firstParam) {
					firstParam = false;
				}
			}
			sb.append(")");

			String key = sb.toString();

			ObjectMapper mapper = new ObjectMapper();
//			mapper.enableDefaultTyping();
			VisibilityChecker<?> vc =
					mapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY).withGetterVisibility(Visibility.NONE);
			mapper.setVisibility(vc);
			String value = "";
			try {
				value = mapper.writeValueAsString(entity);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			message =
					"entity: " + entity.getClass().getSimpleName() + "; action: " + actionClass.getSimpleName()
							+ "; keys: " + key + "; result: " + value;

			log.debug(message);
		}
	}

	@Override
	public void error(Exception e, Class<? extends SessionAction<?>> actionClass, Object entity) {
		log.error(e);
	}

	@Override
	public void warning(String message) {
		// TODO Auto-generated method stub

	}

}

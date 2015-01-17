package org.openlegacy.mvc.mail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.json.EntitySerializationUtils;
import org.openlegacy.utils.UrlUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MailController implements InitializingBean, DisposableBean {

	private final static Log logger = LogFactory.getLog(MailController.class);
	private static final String JSON = "application/json";

	private Properties properties;
	private Session mailSession;
	private Transport transport;
	private boolean debug;

	@RequestMapping(value = "/mail", method = RequestMethod.POST)
	public void sendMailPost(@RequestParam(value = "to") String to, @RequestParam(value = "cc", required = false) String cc,
			@RequestParam(value = "subject") String subject, @RequestParam(value = "body") String body,
			HttpServletResponse response) throws IOException, MessagingException {

		sendMailInner(to, cc, subject, body);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	@RequestMapping(value = "/mail", method = RequestMethod.POST, consumes = JSON)
	public void sendMailJson(@RequestBody String json, HttpServletResponse response) throws IOException, MessagingException {

		json = UrlUtil.decode(json, "{");
		Message message = EntitySerializationUtils.deserialize(json, Message.class);

		sendMailInner(message.getTo(), message.getCc(), message.getSubject(), message.getBody());
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void sendMailInner(String to, String cc, String subject, String body) throws IOException, MessagingException {

		MimeMessage message = new MimeMessage(mailSession);
		message.setSubject(subject, "UTF-8");
		message.setContent(body, "text/html;charset=UTF-8");
		message.setFrom(new InternetAddress(properties.getProperty("from")));

		message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to));
		if (cc != null) {
			message.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse(cc));
		}
		String bcc = properties.getProperty("bcc");
		if (bcc != null) {
			message.setRecipients(javax.mail.Message.RecipientType.BCC, InternetAddress.parse(bcc));
		}

		connect();
		transport.sendMessage(message, message.getRecipients(javax.mail.Message.RecipientType.TO));
		logger.info(MessageFormat.format("Successfully sent message to:{0}, subject:{1}", to, subject));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource resource = new ClassPathResource("/mail.properties");
		properties = PropertiesLoaderUtils.loadProperties(resource);

		mailSession = Session.getDefaultInstance(properties);
		if (debug) {
			mailSession.setDebug(true);
		}

	}

	private void connect() throws MessagingException {
		if (!transport.isConnected()) {
			String host = (String)properties.get("host");
			String userName = (String)properties.get("username");
			transport.connect(host, Integer.valueOf(properties.get("port").toString()), userName,
					(String)properties.get("password"));
			logger.info(MessageFormat.format("Successfully connected to mail host:{0}, account:{1}", userName));
		}
	}

	@Override
	public void destroy() throws Exception {
		transport.close();
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public static class Message {

		private String subject;
		private String body;
		private String to;
		private String cc;

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public String getTo() {
			return to;
		}

		public void setTo(String to) {
			this.to = to;
		}

		public String getCc() {
			return cc;
		}

		public void setCc(String cc) {
			this.cc = cc;
		}
	}
}

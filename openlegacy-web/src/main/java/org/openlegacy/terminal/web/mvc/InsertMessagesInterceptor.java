package org.openlegacy.terminal.web.mvc;

import org.openlegacy.modules.messages.Messages;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Intercepter class for spring MVC. Injects messages into the page context so they can be display within the web page
 * 
 * @author RoiM
 * 
 */
public class InsertMessagesInterceptor extends AbstractInterceptor {

	@Override
	protected void insertModelData(ModelAndView modelAndView) {
		Messages messagesModule = getTerminalSession().getModule(Messages.class);
		if (messagesModule == null) {
			return;
		}

		List<String> messages = messagesModule.getMessages();
		if (messages.size() > 0) {
			modelAndView.addObject("messages", messages.toArray());
		}
		messagesModule.resetMessages();

	}
}

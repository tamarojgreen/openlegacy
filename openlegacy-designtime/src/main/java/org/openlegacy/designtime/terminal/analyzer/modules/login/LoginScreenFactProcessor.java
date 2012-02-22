package org.openlegacy.designtime.terminal.analyzer.modules.login;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.support.ScreenEntityDefinitionsBuilderUtils;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.utils.ClassUtils;

public class LoginScreenFactProcessor implements ScreenFactProcessor {

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {

		LoginScreenFact loginScreenFact = (LoginScreenFact)screenFact;

		screenEntityDefinition.setType(Login.LoginEntity.class);
		screenEntityDefinition.getReferredClasses().add(ClassUtils.getImportDeclaration(Login.LoginEntity.class));

		ScreenFieldDefinition userFieldDefinition = ScreenEntityDefinitionsBuilderUtils.addField(screenEntityDefinition,
				loginScreenFact.getUserField(), loginScreenFact.getUserLabelField().getValue());
		ScreenEntityDefinitionsBuilderUtils.defineFieldType(screenEntityDefinition, userFieldDefinition, Login.UserField.class);

		ScreenFieldDefinition passwordFieldDefinition = ScreenEntityDefinitionsBuilderUtils.addField(screenEntityDefinition,
				loginScreenFact.getPasswordField(), loginScreenFact.getPasswordLabelField().getValue());
		ScreenEntityDefinitionsBuilderUtils.defineFieldType(screenEntityDefinition, passwordFieldDefinition,
				Login.PasswordField.class);

		ScreenFieldDefinition errorFieldDefinition = ScreenEntityDefinitionsBuilderUtils.addField(screenEntityDefinition,
				loginScreenFact.getErrorField(), Login.ERROR_MESSAGE_LABEL);
		ScreenEntityDefinitionsBuilderUtils.defineFieldType(screenEntityDefinition, errorFieldDefinition, Login.ErrorField.class);

	}

	public boolean accept(ScreenFact screenFact) {
		return (screenFact instanceof LoginScreenFact);
	}

}

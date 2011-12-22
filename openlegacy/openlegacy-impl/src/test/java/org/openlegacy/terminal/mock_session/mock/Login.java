package org.openlegacy.terminal.mock_session.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.terminal.support.AbstractScreenEntity;
import org.springframework.stereotype.Component;

@Component
@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(column = 3, row = 1, value = "User:") })
public class Login extends AbstractScreenEntity {

	@ScreenField(row = 1, column = 13, editable = true)
	private String user;

	@ScreenField(row = 2, column = 13, editable = true)
	private String password;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

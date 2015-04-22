package com.openlegacy.ws.openlegacy.services.actions;

import org.openlegacy.modules.login.Login;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;

public class ItemsInitAction implements TerminalAction {
		
		private String user;
		private String password;
		
		public void perform(TerminalSession terminalSession, Object entity, Object... keys){
			terminalSession.getModule(Login.class).login(user, password);
			// PLACE HOLDER for init action
		}

		@Override
		public boolean isMacro() {
			// TODO Auto-generated method stub
			return false;
		}
		
		public void setUser(String user) {
			this.user = user;
		}
		
		public void setPassword(String password) {
			this.password = password;
		}
}

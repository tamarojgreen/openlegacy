package org.openlegacy.terminal.samples.services;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.samples.model.Items;
import org.openlegacy.terminal.samples.model.Items.ItemsRecord;

import java.util.List;

import javax.inject.Inject;
import javax.jws.WebService;

@WebService(endpointInterface = "org.openlegacy.terminal.samples.services.ItemsWebService")
public class ItemsWebServiceImpl implements ItemsWebService {

	@Inject
	private TerminalSession terminalSession;

	@Override
	public List<ItemsRecord> getItems() throws RegistryException, LoginException {
		terminalSession.getModule(Login.class).login("user", "pwd");
		return terminalSession.getModule(Table.class).collectAll(Items.class, ItemsRecord.class);
	}

}

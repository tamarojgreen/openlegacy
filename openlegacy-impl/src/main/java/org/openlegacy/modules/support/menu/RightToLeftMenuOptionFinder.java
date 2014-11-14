package org.openlegacy.modules.support.menu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.modules.menu.MenuOptionFinder;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.utils.FieldsQuery;
import org.openlegacy.terminal.utils.FieldsQuery.FieldsCriteria;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;
import java.util.List;

public class RightToLeftMenuOptionFinder implements MenuOptionFinder{

	private final static Log logger = LogFactory.getLog(RightToLeftMenuOptionFinder.class);

	private char[] charsToRemove = {'.',' '};
	
	@Override
	public String findMenuOption(TerminalSnapshot terminalSnapshot, final String text) {
		List<TerminalField> fields = FieldsQuery.queryFields(terminalSnapshot, new FieldsCriteria() {
			
			@Override
			public boolean match(TerminalField terminalField) {
				return (terminalField.getValue().matches(text));
			}
		});
		if (fields.size() == 0){
			return null;
		}
		TerminalField menuTextField = fields.get(0);
		
		TerminalRow row = terminalSnapshot.getRow(menuTextField.getPosition().getRow());
		for (TerminalField terminalField : row.getFields()) {
			if (terminalField.getPosition().getColumn() > menuTextField.getPosition().getColumn()){
				if (logger.isDebugEnabled()){
					logger.debug(MessageFormat.format("Found menu option:{0} in row:{1}",
							terminalField.getValue(), row.getRowNumber()));
				}
				return StringUtil.ignoreChars(terminalField.getValue(),charsToRemove);
						
			}
		}
		return null;
	}

	public void setCharsToRemove(char[] charsToRemove) {
		this.charsToRemove = charsToRemove;
	}
}

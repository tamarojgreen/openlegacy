package org.openlegacy.terminal.modules.table;

import static org.junit.Assert.assertEquals;
import apps.position.screens.PositionDrillDownDetail;
import apps.position.screens.PositionDrillDownMenu;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration("positiondrilldown/PositionDrillDown-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class PositionBasedDrillDownTest {

	@Inject
	private TerminalSession terminalSession;

	Logger logger = Logger.getLogger("testoutput");

	@Test
	public void testFocusField() {
		PositionDrillDownMenu positionDrillDownMenu = terminalSession.getEntity(PositionDrillDownMenu.class);
		assertEquals("00000", positionDrillDownMenu.getRrn());

		logger.info("initial RRN:" + positionDrillDownMenu.getRrn());
		// logger.info("pre pos:" + terminalSession.getSnapshot().getCursorPosition());
		// logger.info("focus:" + positionDrillDownMenu.getFocusField());
		// row*80 + column
		positionDrillDownMenu.setFocusField("572");
		logger.info("pos:" + terminalSession.getSnapshot().getCursorPosition());
		PositionDrillDownDetail positionDrillDownDetail = terminalSession.doAction(TerminalActions.ENTER(), positionDrillDownMenu);
		assertEquals("00002", positionDrillDownDetail.getRrn());

		logger.info("pos:" + terminalSession.getSnapshot().getCursorPosition());
	}

	@Test
	public void testRetrieveByKeys() {
		PositionDrillDownDetail positionDrillDownDetail = terminalSession.getEntity(PositionDrillDownDetail.class, "00002");
		assertEquals("00002", positionDrillDownDetail.getRrn());
	}
}

package org.openlegacy.terminal.modules.table;

import static org.junit.Assert.assertEquals;
import apps.position.screens.PositionDrillDownDetail;
import apps.position.screens.PositionDrillDownMenu;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration("positiondrilldown/PositionDrillDown-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class PositionBasedDrillDownTest {

	@Inject
	private ApplicationContext applicationContext;

	private TerminalSession terminalSession;

	Logger logger = Logger.getLogger("testoutput");

	@Before
	public void setUp() {
		terminalSession = applicationContext.getBean(TerminalSession.class);
	}

	@Test
	public void testFocusField() {
		PositionDrillDownMenu positionDrillDownMenu = terminalSession.getEntity(PositionDrillDownMenu.class);
		assertEquals("00000", positionDrillDownMenu.getRrn());

		logger.info("initial RRN:" + positionDrillDownMenu.getRrn());
		// logger.info("pre pos:" + terminalSession.getSnapshot().getCursorPosition());
		// logger.info("focus:" + positionDrillDownMenu.getFocusField());
		// row*80 + column
		// positionDrillDownMenu.getDisplayTheLoadallSubfileRecords().get(1).setFocusField("572");
		// positionDrillDownMenu.setFocusField("572");
		positionDrillDownMenu.getDisplayTheLoadallSubfileRecords().get(3).focus();
		logger.info("pos:" + terminalSession.getSnapshot().getCursorPosition());
		logger.info("fetch pos:" + terminalSession.fetchSnapshot().getCursorPosition());
		PositionDrillDownDetail positionDrillDownDetail = terminalSession.doAction(TerminalActions.ENTER(), positionDrillDownMenu);
		assertEquals("00002", positionDrillDownDetail.getRrn());

		logger.info("pos:" + terminalSession.getSnapshot().getCursorPosition());
		logger.info("fetch pos:" + terminalSession.fetchSnapshot().getCursorPosition());
	}

	@Test
	public void testRetrieveByKeys() {
		PositionDrillDownDetail positionDrillDownDetail = terminalSession.getEntity(PositionDrillDownDetail.class, "125");
		assertEquals("00002", positionDrillDownDetail.getRrn());
	}

}

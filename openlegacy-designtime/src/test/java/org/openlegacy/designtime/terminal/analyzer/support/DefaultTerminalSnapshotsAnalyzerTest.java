package org.openlegacy.designtime.terminal.analyzer.support;

import freemarker.template.TemplateException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.FieldWithValuesTypeDefinition;
import org.openlegacy.definitions.BooleanFieldTypeDefinition;
import org.openlegacy.definitions.DateFieldTypeDefinition;
import org.openlegacy.designtime.terminal.analyzer.modules.navigation.ScreenNavigationDesignTimeDefinition;
import org.openlegacy.modules.messages.Messages;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalActions.ENTER;
import org.openlegacy.terminal.actions.TerminalActions.F10;
import org.openlegacy.terminal.actions.TerminalActions.F11;
import org.openlegacy.terminal.actions.TerminalActions.F3;
import org.openlegacy.terminal.actions.TerminalActions.F4;
import org.openlegacy.terminal.actions.TerminalActions.F7;
import org.openlegacy.terminal.actions.TerminalActions.F8;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

@ContextConfiguration("/test-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSnapshotsAnalyzerTest extends AbstractAnalyzerTest {

	@Test
	public void testBasicAnalisys() {

		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = analyze("SimpleScreen.xml", "FormScreen.xml");

		Assert.assertEquals(2, screenEntitiesDefinitions.size());

		ScreenEntityDefinition screen1 = screenEntitiesDefinitions.get("SimpleScreen");
		Assert.assertNotNull(screen1);
		Map<String, ScreenFieldDefinition> fieldsDefinitions = screen1.getFieldsDefinitions();
		Assert.assertEquals(2, fieldsDefinitions.size());
		ScreenFieldDefinition fieldA = fieldsDefinitions.get("fieldA");
		Assert.assertNotNull(fieldA);
		Assert.assertTrue(fieldA.isEditable());
		Assert.assertEquals("Field A", fieldA.getDisplayName());
		Assert.assertEquals(SimpleTerminalPosition.newInstance(4, 13), fieldA.getPosition());

		ScreenEntityDefinition screen2 = screenEntitiesDefinitions.get("FormScreen");
		Assert.assertNotNull(screen2);
		fieldsDefinitions = screen2.getFieldsDefinitions();
		Assert.assertEquals(12, fieldsDefinitions.size());
		fieldA = fieldsDefinitions.get("fieldA");
		Assert.assertNotNull(fieldA);

		ScreenFieldDefinition fieldB = fieldsDefinitions.get("fieldB");
		Assert.assertNotNull(fieldB);
		Assert.assertTrue(!fieldB.isEditable());
		Assert.assertEquals("Field B", fieldB.getDisplayName());

		ScreenFieldDefinition booleanField = fieldsDefinitions.get("booleanField");
		Assert.assertNotNull(booleanField);
		BooleanFieldTypeDefinition typeDefinition = (BooleanFieldTypeDefinition)booleanField.getFieldTypeDefinition();
		Assert.assertEquals("Y", typeDefinition.getTrueValue());
		Assert.assertEquals("N", typeDefinition.getFalseValue());

		ScreenFieldDefinition dateField = fieldsDefinitions.get("dateField");
		Assert.assertNotNull(dateField);
		DateFieldTypeDefinition dateTypeDefinition = (DateFieldTypeDefinition)dateField.getFieldTypeDefinition();
		Assert.assertEquals(20, dateTypeDefinition.getDayColumn().intValue());
		Assert.assertEquals(25, dateTypeDefinition.getMonthColumn().intValue());
		Assert.assertEquals(30, dateTypeDefinition.getYearColumn().intValue());
	}

	private Map<String, ScreenEntityDefinition> analyze(String... fileNames) {
		snapshotsOrganizer.setMatchingPercent(99);
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(getClass().getResource("mock").getFile(), fileNames);
		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = snapshotsAnalyzer.analyzeSnapshots(snapshots);
		return screenEntitiesDefinitions;
	}

	@Test
	public void testSessionAnalyzer() throws TemplateException, IOException {
		Map<String, ScreenEntityDefinition> definitions = snapshotsAnalyzer.analyzeTrail(getClass().getResourceAsStream(
				"Inventory.trail"));
		Assert.assertEquals(6, definitions.size());
	}

	/**
	 * Test to reproduce a bug, when parent and child screen has the same name during analysis. happened since entities were not
	 * "update" in the session after got the actual screen name
	 * 
	 * @throws TemplateException
	 * @throws IOException
	 */
	@Test
	public void testSessionWithChildAnalyzer() throws TemplateException, IOException {
		Map<String, ScreenEntityDefinition> definitions = snapshotsAnalyzer.analyzeTrail(getClass().getResourceAsStream(
				"ChildScreens.trail"));
		ScreenEntityDefinition parent = definitions.get("WorkWithItemMaster");
		Assert.assertNotNull(parent);
		Assert.assertEquals(1, parent.getChildEntitiesDefinitions().size());
		Assert.assertEquals("WorkWithItemMaster1", parent.getChildEntitiesDefinitions().get(0).getEntityName());
		ScreenEntityDefinition childScreen = definitions.get("WorkWithItemMaster1");
		Assert.assertNotNull(childScreen);
		Assert.assertTrue(childScreen.isChild());
	}

	@Test
	public void testNaturalActions() {
		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = analyze("NaturalActionsScreen.xml");

		Assert.assertEquals(1, screenEntitiesDefinitions.size());

		ScreenEntityDefinition screen1 = screenEntitiesDefinitions.get("NaturalActionsScreen");
		Assert.assertNotNull(screen1);
		List<ActionDefinition> actions = screen1.getActions();

		Assert.assertEquals(7, actions.size());

		Assert.assertEquals("Save", actions.get(0).getDisplayName());
		Assert.assertEquals(ENTER.class, actions.get(0).getAction().getClass());

		Assert.assertEquals("Quit", actions.get(1).getDisplayName());
		Assert.assertEquals(F3.class, actions.get(1).getAction().getClass());

		Assert.assertEquals("Add", actions.get(2).getDisplayName());
		Assert.assertEquals(F4.class, actions.get(2).getAction().getClass());

		Assert.assertEquals("Up", actions.get(3).getDisplayName());
		Assert.assertEquals(F7.class, actions.get(3).getAction().getClass());

		Assert.assertEquals("Down", actions.get(4).getDisplayName());
		Assert.assertEquals(F8.class, actions.get(4).getAction().getClass());

		Assert.assertEquals("<", actions.get(5).getDisplayName());
		Assert.assertEquals(F10.class, actions.get(5).getAction().getClass());

		Assert.assertEquals(">", actions.get(6).getDisplayName());
		Assert.assertEquals(F11.class, actions.get(6).getAction().getClass());
	}

	@Test
	public void testChildScreenEntities() throws TemplateException, IOException {
		Map<String, ScreenEntityDefinition> definitions = snapshotsAnalyzer.analyzeTrail(getClass().getResourceAsStream(
				"Inventory.trail"));

		ScreenEntityDefinition workWithItemMaster1 = definitions.get("WorkWithItemMaster1");
		Assert.assertNotNull(workWithItemMaster1);
		ScreenEntityDefinition workWithItemMaster2 = definitions.get("WorkWithItemMaster2");
		Assert.assertNotNull(workWithItemMaster2);
		Assert.assertNotNull(workWithItemMaster2.getNavigationDefinition());
		List<FieldAssignDefinition> assignedFields = workWithItemMaster2.getNavigationDefinition().getAssignedFields();
		Assert.assertEquals(1, assignedFields.size());
		Assert.assertNull(assignedFields.get(0).getValue());
		Assert.assertEquals(1, workWithItemMaster1.getChildEntitiesDefinitions().size());
		Assert.assertEquals("WorkWithItemMaster2", workWithItemMaster1.getChildEntitiesDefinitions().get(0).getEntityName());

		assertScreenContent(workWithItemMaster1, "inventory/WorkWithItemMaster1_with_childScreens.java.expected");
		assertScreenContent(workWithItemMaster2, "inventory/WorkWithItemMaster2_as_child.java.expected");
	}

	@Test
	public void testBasicTable() {

		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = analyze("TableScreen.xml");

		Assert.assertEquals(1, screenEntitiesDefinitions.size());

		ScreenEntityDefinition tableScreen = screenEntitiesDefinitions.get("TableScreen");
		Assert.assertNotNull(tableScreen);
		Map<String, ScreenTableDefinition> tablesDefinitions = tableScreen.getTableDefinitions();
		Assert.assertEquals(1, tablesDefinitions.size());
		ScreenTableDefinition table1 = tablesDefinitions.get("TableScreenRecord");

		Assert.assertEquals(5, table1.getStartRow());
		Assert.assertEquals(7, table1.getEndRow());

		ScreenColumnDefinition columnSelction = table1.getColumnDefinition("action_");
		Assert.assertNotNull(columnSelction);
		Assert.assertEquals(4, columnSelction.getStartColumn());
		Assert.assertEquals(5, columnSelction.getEndColumn());
		Assert.assertTrue(columnSelction.isEditable());
		Assert.assertEquals("Action", columnSelction.getDisplayName());

		ScreenColumnDefinition columnA = table1.getColumnDefinition("columnA");
		Assert.assertNotNull(columnA);
		Assert.assertEquals(11, columnA.getStartColumn());
		Assert.assertEquals(19, columnA.getEndColumn());
		Assert.assertEquals("Column A", columnA.getDisplayName());
		Assert.assertEquals("Cell 1A", columnA.getSampleValue());

		ScreenColumnDefinition columnB = table1.getColumnDefinition("columnB");
		Assert.assertNotNull(columnB);
		Assert.assertEquals(21, columnB.getStartColumn());
		Assert.assertEquals(29, columnB.getEndColumn());
		Assert.assertEquals("Column B", columnB.getDisplayName());
		Assert.assertEquals("Cell 1B", columnB.getSampleValue());

		ScreenColumnDefinition columnC = table1.getColumnDefinition("column4");
		Assert.assertNotNull(columnC);
		Assert.assertEquals(31, columnC.getStartColumn());
		Assert.assertEquals(39, columnC.getEndColumn());
		Assert.assertEquals("Column4", columnC.getDisplayName());
		Assert.assertEquals("Cell 1C", columnC.getSampleValue());

		List<ActionDefinition> actions = table1.getActions();
		Assert.assertEquals(2, actions.size());
		Assert.assertEquals("View", actions.get(0).getDisplayName());
		Assert.assertEquals("Edit", actions.get(1).getDisplayName());

	}

	@Test
	public void testMessagesScreen() {

		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = analyze("MessagesScreen.xml");

		Assert.assertEquals(1, screenEntitiesDefinitions.size());

		ScreenEntityDefinition messagesScreen = screenEntitiesDefinitions.get("DisplayMessages");

		Assert.assertNotNull(messagesScreen);
		Assert.assertEquals("MessagesEntity", messagesScreen.getTypeName());
		Assert.assertNotNull(messagesScreen.getFieldsDefinitions().get(Messages.MESSAGE_FIELD));

	}

	@Test
	public void testGenerate() throws TemplateException, IOException {

		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = analyze("LoginScreen.xml", "MainMenuScreen.xml",
				"SimpleScreen.xml", "FormScreen.xml", "TableScreen.xml", "WindowScreen.xml");

		assertScreenContent(screenEntitiesDefinitions.get("LoginScreen"), "LoginScreen.java.expected");
		assertScreenContent(screenEntitiesDefinitions.get("SimpleScreen"), "SimpleScreen.java.expected");
		assertScreenContent(screenEntitiesDefinitions.get("FormScreen"), "FormScreen.java.expected");
		assertScreenContent(screenEntitiesDefinitions.get("TableScreen"), "TableScreen.java.expected");
		assertScreenContent(screenEntitiesDefinitions.get("WindowScreen"), "WindowScreen.java.expected");
		assertScreenContent(screenEntitiesDefinitions.get("MainMenuScreen"), "MainMenuScreen.java.expected");
	}

	@Test
	public void testGenerateNavigation() throws TemplateException, IOException {

		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = analyze("LoginScreen.xml", "LoginScreen-out.xml",
				"MainMenuScreen.xml", "MainMenuScreen-out.xml", "SubMenu1Screen.xml", "SubMenu1Screen-out.xml",
				"SimpleScreen.xml");

		assertScreenContent(screenEntitiesDefinitions.get("MainMenuScreen"), "navigation/MainMenuScreen.java.expected");
		assertScreenContent(screenEntitiesDefinitions.get("SubMenu1"), "navigation/SubMenu1.java.expected");
		assertScreenContent(screenEntitiesDefinitions.get("SimpleScreen"), "navigation/SimpleScreen.java.expected");
	}

	@Test
	public void testGenerateWindowValues() throws TemplateException, IOException {

		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = analyze("SimpleScreen.xml",
				"SimpleScreen-towindow-out.xml", "WindowTableScreen.xml");

		ScreenEntityDefinition windowTableScreen = screenEntitiesDefinitions.get("WindowTableScreen");
		Assert.assertNotNull(windowTableScreen);
		Assert.assertTrue(windowTableScreen.isWindow());
		Assert.assertEquals(1, windowTableScreen.getTableDefinitions().size());
		ScreenNavigationDesignTimeDefinition navigationDefinition = (ScreenNavigationDesignTimeDefinition)windowTableScreen.getNavigationDefinition();
		Assert.assertNotNull(navigationDefinition);
		Assert.assertNotNull(navigationDefinition.getAccessedFromEntityDefinition());
		// verify the target screen saved cursor field
		Assert.assertEquals(1, navigationDefinition.getAssignedFields().size());

		ScreenEntityDefinition simpleScreenDefinition = screenEntitiesDefinitions.get("SimpleScreen");
		SimpleScreenFieldDefinition fieldAdefinition = (SimpleScreenFieldDefinition)simpleScreenDefinition.getFieldsDefinitions().get(
				"fieldA");
		FieldWithValuesTypeDefinition fieldTypeDefinition = (FieldWithValuesTypeDefinition)fieldAdefinition.getFieldTypeDefinition();
		Assert.assertEquals(windowTableScreen, fieldTypeDefinition.getSourceEntityDefinition());
		assertScreenContent(simpleScreenDefinition, "SimpleScreenValues.java.expected");
	}

}

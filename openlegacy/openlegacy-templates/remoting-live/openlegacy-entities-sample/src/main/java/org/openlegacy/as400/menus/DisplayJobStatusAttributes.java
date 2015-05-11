package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;

import java.io.Serializable;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 26, value = "Display Job Status Attributes"),
		@Identifier(row = 5, column = 2, value = "Status of job . . . . . . . . . . . . . . . :"),
		@Identifier(row = 6, column = 2, value = "Current user profile  . . . . . . . . . . . :") })
@ScreenActions(actions = {
		@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"),
		@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"),
		@Action(action = TerminalActions.F4.class, additionalKey = AdditionalKey.SHIFT, displayName = "Job menu", alias = "jobMenu") })
@ScreenNavigation(accessedFrom = WorkWithJob.class, assignedFields = { @AssignedField(field = "menuSelection", value = "1") })
public class DisplayJobStatusAttributes implements Serializable {

	private static final long serialVersionUID = 1L;

	@ScreenField(row = 2, column = 72, endColumn = 79, labelColumn = 62, displayName = "System", sampleValue = "S1051C6E")
	private String system;

	@ScreenField(row = 3, column = 9, endColumn = 18, labelColumn = 2, displayName = "Job", sampleValue = "QPADEV0001")
	private String job;

	@ScreenField(row = 3, column = 32, endColumn = 41, labelColumn = 24, displayName = "User", sampleValue = "RMR20924")
	private String user;

	@ScreenField(row = 3, column = 57, endColumn = 62, labelColumn = 47, displayName = "Number", sampleValue = "698775")
	private Integer number;

	@ScreenField(row = 5, column = 50, endColumn = 69, labelColumn = 2, displayName = "Status of job", sampleValue = "ACTIVE")
	private String statusOfJob;

	@ScreenField(row = 6, column = 50, endColumn = 59, labelColumn = 2, displayName = "Current user profile", sampleValue = "RMR20924")
	private String currentUserProfile;

	@ScreenField(row = 7, column = 50, endColumn = 59, labelColumn = 2, displayName = "Job user identity", sampleValue = "RMR20924")
	private String jobUserIdentity;

	@ScreenField(row = 8, column = 52, endColumn = 66, labelColumn = 4, displayName = "Set by", sampleValue = "*DEFAULT")
	private String setBy;

	@ScreenField(row = 10, column = 50, endColumn = 57, labelColumn = 4, displayName = "Date", sampleValue = "12/04/12")
	private String date1;

	@ScreenField(row = 11, column = 50, endColumn = 57, labelColumn = 4, displayName = "Time", sampleValue = "04:48:11")
	private String time1;

	@ScreenField(row = 13, column = 50, endColumn = 57, labelColumn = 4, displayName = "Date", sampleValue = "12/04/12")
	private String date;

	@ScreenField(row = 14, column = 50, endColumn = 57, labelColumn = 4, displayName = "Time", sampleValue = "04:48:11")
	private String time;

	@ScreenField(row = 15, column = 50, endColumn = 59, labelColumn = 2, displayName = "Subsystem", sampleValue = "QINTER")
	private String subsystem;

	@ScreenField(row = 16, column = 52, endColumn = 53, labelColumn = 4, displayName = "Subsystem pool ID", sampleValue = "2")
	private Integer subsystemPoolId;

	@ScreenField(row = 17, column = 50, endColumn = 59, labelColumn = 2, displayName = "Type of job", sampleValue = "INTER")
	private String typeOfJob;

	@ScreenField(row = 18, column = 50, endColumn = 54, labelColumn = 2, displayName = "Special environment", sampleValue = "*NONE")
	private String specialEnvironment;

	@ScreenField(row = 19, column = 50, endColumn = 51, labelColumn = 2, displayName = "Program return code", sampleValue = "0")
	private Integer programReturnCode;

}

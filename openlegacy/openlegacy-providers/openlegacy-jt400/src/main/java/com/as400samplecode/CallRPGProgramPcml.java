/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package com.as400samplecode;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.Trace;
import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.ProgramCallDocument;

public class CallRPGProgramPcml {

	public static void main(String[] args) throws PcmlException {

		// Connect to the iSeries using hostname, userid and password

		Trace.setTraceOn(true);
		Trace.setTraceDiagnosticOn(true);
		Trace.setTraceInformationOn(true);
		Trace.setTraceWarningOn(true);
		Trace.setTraceErrorOn(true);
		Trace.setTraceDatastreamOn(true);
		Trace.setTraceThreadOn(true);
		Trace.setTraceJDBCOn(true);

		AS400 as400System = new AS400("as400.openlegacy.org", "RMR20924", "roi045");

		ProgramCallDocument newPcml = new ProgramCallDocument(as400System, "com.as400samplecode.rpg_program");
		newPcml.setValue("rpg_program.FIRSTNAME", "Roi");
		newPcml.setValue("rpg_program.LASTNAME", "Mor");
		boolean result = newPcml.callProgram("rpg_program");

		System.out.println(newPcml.getValue("rpg_program.MESSAGE"));
		// The ProgramCall class allows a user to call an iSeries server program,
		// pass parameters to it (input and output), and access data returned in the
		// output parameters after the program runs. Use ProgramCall to call programs.
		// Done with the server.
		as400System.disconnectAllServices();

		// Print the output from the RPGLE called program

	}
}
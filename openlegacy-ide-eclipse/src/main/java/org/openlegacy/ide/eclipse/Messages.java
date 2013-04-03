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
package org.openlegacy.ide.eclipse;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.openlegacy.ide.eclipse.messages"; //$NON-NLS-1$
	public static String menu_generate_model;
	public static String message_initializing_ol_analyzer;
	public static String console_title_openlegacy;
	public static String error_code_page_not_specified;
	public static String error_host_name_not_specified;
	public static String error_invalid_trail_file_selection;
	public static String error_package_cannot_be_empty;
	public static String error_package_not_specified;
	public static String error_port_not_numeric;
	public static String error_project_already_exists;
	public static String error_project_name_not_specified;
	public static String error_project_name_oinvalid;
	public static String error_provider_not_selected;
	public static String error_source_folder_cannot_be_empty;
	public static String error_template_not_specified;
	public static String errror_host_port_not_specified;
	public static String field_entity_name;
	public static String info_ol_java_api_project;
	public static String info_ol_new_rest_project;
	public static String info_ol_new_web_mvc_project;
	public static String info_ol_project_wizard;
	public static String info_ol_sample_mobile_mvc_project;
	public static String info_ol_sample_web_mvc_project;
	public static String info_provider_applinx;
	public static String info_provider_h3270;
	public static String info_provider_tn5250j;
	public static String job_activating_analyzer;
	public static String job_generating_model;
	public static String job_generating_view;
	public static String job_generating_controller;
	public static String label_browse;
	public static String label_code_page;
	public static String label_default_project;
	public static String label_dropdown_select;
	public static String label_dropdown_select2;
	public static String label_generate_help;
	public static String label_host_ip;
	public static String label_host_port;
	public static String label_package;
	public static String label_project_name;
	public static String label_provider;
	public static String label_source_folder;
	public static String label_template;
	public static String message_customize_rules;
	public static String message_customize_templates;
	public static String message_new_trail_found;
	public static String question_override_file;
	public static String task_generating;
	public static String title_ol_generate_screens_api;
	public static String title_ol_project_wizard;
	public static String title_openlegacy;
	public static String warn_java_source_not_valid_selection;
	public static String error_creating_text_editor;
	public static String error_invalid_input_file;
	public static String error_no_snapshots_found;
	public static String error_unable_to_open_trail_file;
	public static String label_screen_in;
	public static String label_screen_out;
	public static String label_screen_prefix;
	public static String label_sequence;
	public static String page_name_snapshots;
	public static String page_name_source;
	public static String label_col_fields;
	public static String label_col_identifiers;
	public static String label_col_row;
	public static String label_col_column;
	public static String label_command;
	public static String label_templates_url_preference;
	public static String error_new_project_metadata_not_found;
	public static String label_generate_mobile_page;
	public static String label_do_not_show_again;
	public static String TrailJob_job_name;
	public static String TrailJob_message_found_new_trail;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {}
}

package com.someorg.examples.screens;

import javax.persistance.Entity;
import javax.persistance.Id;

@Entity
public class ItemDetails {

	private static String staticString = "staticString";
	private final String finalString = "finalString";

	@Id
	private Integer itemId;

	private String description;

}

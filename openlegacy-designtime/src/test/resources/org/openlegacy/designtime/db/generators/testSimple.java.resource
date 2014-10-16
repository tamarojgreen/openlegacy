package com.someorg.examples.screens;

import javax.persistance.Entity;
import javax.persistance.Id;

@Entity
public class ItemDetails {

	@Id
	private Integer itemId;
	
	private String description;

}

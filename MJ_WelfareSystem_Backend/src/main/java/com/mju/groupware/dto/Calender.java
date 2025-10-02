package com.mju.groupware.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Calender {

	private String id;
	private String title;
	private String description;
	private String start;
	private String end;
	private String backgroundColor;
	private int userId;
	private boolean allDay;


}

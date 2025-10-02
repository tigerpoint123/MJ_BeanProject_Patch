package com.mju.groupware.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TeamUser {
	private int UserID;
	private int TeamID;
	private String UserLoginID;
	private String UserName;

}

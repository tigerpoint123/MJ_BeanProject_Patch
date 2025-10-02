package com.mju.groupware.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Professor extends User {

	private int ProfessorID;
	private String ProfessorColleges; // 단과대학
	private String ProfessorMajor; // 전공
	private String ProfessorRoom; // 교수실
	private String ProfessorRoomNum; // 교수실 전화번호
	private int UserID; // foreign key
	private int WUserID;
	private String WithdrawalDate;

}

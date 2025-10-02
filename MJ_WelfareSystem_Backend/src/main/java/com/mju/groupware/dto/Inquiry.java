package com.mju.groupware.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Inquiry {
	private int Ino;
	private String IBoardSubject;
	private String IBoardWriter;
	private String IBoardContent;
	private String IBoardDate;
	private String IBoardType;
	private String UserEmail;
	private String UserPhoneNum;
	private String State;
	private String IBoardAnswer;

    private int UserID;
	private int IBoardID;


}

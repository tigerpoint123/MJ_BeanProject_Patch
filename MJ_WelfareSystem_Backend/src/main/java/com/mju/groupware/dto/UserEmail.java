package com.mju.groupware.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserEmail {

	private int UserEmailID;
	private String UserEmail;
	private int UserCertificationNum;
	private String UserCertificationTime;

	private int Counter;
	private String From;
	private String Content;
	private String Title;
	private String Date;

}

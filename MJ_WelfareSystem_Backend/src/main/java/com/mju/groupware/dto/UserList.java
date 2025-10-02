package com.mju.groupware.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserList {
	private int UserID;
	// 학번
	private String UserLoginID;
	// 이름
	private String UserName;
	// 전화번호
	private String UserPhoneNum;
	// 이메일
	private String UserEmail;
	// 학생 교수 관리자
	private String UserRole;
	// 권한
	private String Authority;
	// 최근로그인한 날짜
	private String LoginDate;
	// 로그인 활성화
	private boolean Enabled;
	// 탈퇴여부
	private boolean Withdrawal;
	private String OpenName;
	private String OpenEmail;
	private String OpenPhoneNum;
	private String OpenMajor;
	private String OpenGrade;

}

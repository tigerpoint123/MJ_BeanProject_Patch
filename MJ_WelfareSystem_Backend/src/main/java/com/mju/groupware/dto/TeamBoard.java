package com.mju.groupware.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TeamBoard {

	// 게시판정보
	private int TBoardID;
	private String TBoardSubject;
	private String TBoardContent;
	private String TBoardWriter;
	private String TBoardDate;
	private String TUserLoginID;

	// 파일정보
	private int TBno;
	private String TFileID;
	private String TOriginalFileName;
	private String TStoredFileName;
	private String TFileSize;
	private String TeamID;
	
	private int TUserID;

}

package com.mju.groupware.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Board {
	private String BFileID;
	private int Bno;
	private String BoardSubject;
	private String BoardWriter;
	private String BoardContent;
	private String BoardDate;
	private String BoardType;
	private int UserID;
	private int BoardID;
	private int BoardHit;
	private String OriginalFileName;
	private String StoredFileName;
	private int FileSize;

}

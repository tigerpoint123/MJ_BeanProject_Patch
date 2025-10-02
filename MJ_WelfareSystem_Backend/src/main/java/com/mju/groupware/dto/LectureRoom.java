package com.mju.groupware.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LectureRoom {
	private int LectureRoomNo;
	private String RoomLocation;
	private int RoomFloor;
	private int MaxNumOfPeople;
	private String RoomType;

}

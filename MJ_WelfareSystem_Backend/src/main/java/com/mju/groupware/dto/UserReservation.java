package com.mju.groupware.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserReservation {
	private String ReservationDate;
	private String ReservationStartTime;
	private String ReservationEndTime;
	private int ReservationNumOfPeople;
	private int LectureRoomNo;
	private int UserID;


}

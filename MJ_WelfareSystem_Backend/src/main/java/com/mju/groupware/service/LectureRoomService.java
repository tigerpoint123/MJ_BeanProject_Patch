package com.mju.groupware.service;

import java.util.List;

import com.mju.groupware.dto.LectureRoom;
import com.mju.groupware.dto.UserReservation;

public interface LectureRoomService {

	List<LectureRoom> selectLectureRoomList();

	int selectMaxNumOfPeople(String lectureRoomNo);

	String selectLoginUserId(String userLoginID);

	void insertReservation(UserReservation userReservation);

	List<UserReservation> selectStartTime(String lectureRoomNo);

	int selectReservationUserId(int userID);

	String selectUserIdForReservationConfirm(String loginID);

	int selectLectureRoomNo(String userID);

	int selectRoomFloor(int lectureRoomNo);

	int selectLectureRoomMaxNumOfPeople(int lectureRoomNo);

	int selectReservationNumOfPeople(String userID);

	String selectReservationStartTime(String userID);

	String selectLectureRoomLocation(int lectureRoomNo);

	String selectReservationStartTimeForException(String startTime);

	boolean deleteReservation(UserReservation userReservation);

	UserReservation selectRoomInfo(String userID, UserReservation userReservation);


}

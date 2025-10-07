package com.mju.groupware.service;

import com.mju.groupware.dto.LectureRoom;
import com.mju.groupware.dto.UserReservation;
import org.springframework.ui.Model;

import java.util.List;

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

	String getReservationPage(String lectureRoomNo, String reservationDate, Model model);

	String postReservation(int maxNumOfPeople, int reservationNumOfPeople, int lectureRoomNo);

	String getReservationConfirm(int lectureRoomNo, Model model, String userID);

	String getMyReservation(Model model, int lectureRoomNo, String userID);

	// 예약 검증 메서드들
	boolean isExceedingCapacity(int lectureRoomNo, int reservationNumOfPeople);

	boolean hasDuplicateReservation(String userLoginID);

	boolean hasTimeConflict(String startTime);

	void createReservation(String userLoginID, String selectedTime, int lectureRoomNo, 
	                       int reservationNumOfPeople, String reservationDate);

	String getLectureRoomList(Model model);

	boolean cancelReservation(String userLoginID);
}

package com.mju.groupware.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mju.groupware.dao.LectureRoomDao;
import com.mju.groupware.dto.LectureRoom;
import com.mju.groupware.dto.UserReservation;

@Service
public class LectureRoomServiceImpl implements LectureRoomService {
	@Autowired
	private LectureRoomDao lectureRoomDao;

	@Override
	public List<LectureRoom> selectLectureRoomList() {
		return lectureRoomDao.selectLectureRoomList();
	}

	@Override
	public int selectMaxNumOfPeople(String lectureRoomNo) {
		return lectureRoomDao.selectMaxNumOfPeople(lectureRoomNo);
	}

	@Override
	public String selectLoginUserId(String userLoginID) {
		return lectureRoomDao.selectLoginUserId(userLoginID);
	}

	@Override
	public void insertReservation(UserReservation userReservation) {
		lectureRoomDao.insertReservation(userReservation);
	}

	@Override
	public List<UserReservation> selectStartTime(String lectureRoomNo) {
		return lectureRoomDao.selectStartTime(lectureRoomNo);
	}

	@Override
	public int selectReservationUserId(int userID) {
		return lectureRoomDao.selectReservationUserId(userID);
	}

	@Override
	public String selectUserIdForReservationConfirm(String loginID) {
		return lectureRoomDao.selectUserIdForReservationConfirm(loginID);
	}

	@Override
	public int selectLectureRoomNo(String userID) {
		return lectureRoomDao.selectLectureRoomNo(userID);
	}

	@Override
	public String selectLectureRoomLocation(int lectureRoomNo) {
		return lectureRoomDao.selectLectureRoomLocation(lectureRoomNo);
	}

	@Override
	public int selectLectureRoomMaxNumOfPeople(int lectureRoomNo) {
		return lectureRoomDao.selectLectureRoomMaxNumOfPeople(lectureRoomNo);
	}

	@Override
	public int selectReservationNumOfPeople(String userID) {
		return lectureRoomDao.selectReservationNumOfPeople(userID);
	}

	@Override
	public String selectReservationStartTime(String userID) {
		return lectureRoomDao.selectReservationStartTime(userID);
	}

	@Override
	public int selectRoomFloor(int lectureRoomNo) {
		return lectureRoomDao.selectRoomFloor(lectureRoomNo);
	}

	@Override
	public String selectReservationStartTimeForException(String startTime) {
		return lectureRoomDao.selectReservationStartTimeForException(startTime);
	}

	@Override
	public UserReservation selectRoomInfo(String userID, UserReservation userReservation) {
		userReservation = lectureRoomDao.selectRoomInfo(userID, userReservation);
		return userReservation;
	}

	@Override
	public boolean deleteReservation(UserReservation userReservation) {
		boolean check = lectureRoomDao.deleteReservation(userReservation);
		return check;
	}
}

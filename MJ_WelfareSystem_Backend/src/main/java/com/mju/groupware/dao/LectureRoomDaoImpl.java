package com.mju.groupware.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.mju.groupware.dto.LectureRoom;
import com.mju.groupware.dto.UserReservation;

@Service
@Repository
public class LectureRoomDaoImpl implements LectureRoomDao {
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public List<LectureRoom> selectLectureRoomList() {
		return sqlSession.selectList("SelectLectureRoomList");
	}

	@Override
	public int selectMaxNumOfPeople(String lectureRoomNo) {
		return sqlSession.selectOne("SelectMaxNumOfPeople", lectureRoomNo);
	}

	@Override
	public String selectLoginUserId(String userLoginID) {
		return sqlSession.selectOne("SelectLoginUserIDForLecture", userLoginID);
	}

	@Override
	public void insertReservation(UserReservation userReservation) {
		sqlSession.insert("InsertReservation", userReservation);
	}

	@Override
	public List<UserReservation> selectStartTime(String lectureRoomNo) {
		List<UserReservation> output = sqlSession.selectList("SelectStartTime", lectureRoomNo);
		return output;
	}

	@Override
	public int selectReservationUserId(int userID) {
		Integer output = sqlSession.selectOne("SelectReservationUserID", userID);
		if (output != null) {
			return output;
		} else {
			return 0;
		}
	}

	@Override
	public String selectUserIdForReservationConfirm(String loginID) {
		return sqlSession.selectOne("SelectUserIDForReservationConfirm", loginID);
	}

	@Override
	public int selectLectureRoomNo(String userID) {

		Integer selectLectureRoomNo = sqlSession.selectOne("SelectLectureRoomNo", userID);
		if (selectLectureRoomNo != null) {
			return selectLectureRoomNo;
		} else {
			return 0;
		}
	}

	@Override
	public String selectLectureRoomLocation(int lectureRoomNo) {
		return sqlSession.selectOne("SelectLectureRoomLocation", lectureRoomNo);
	}

	@Override
	public int selectLectureRoomMaxNumOfPeople(int lectureRoomNo) {
		return sqlSession.selectOne("SelectLectureRoomMaxNumOfPeople", lectureRoomNo);
	}

	@Override
	public int selectReservationNumOfPeople(String userID) {
		return sqlSession.selectOne("SelectReservationNumOfPeople", userID);
	}

	@Override
	public String selectReservationStartTime(String userID) {
		return sqlSession.selectOne("SelectReservationStartTime", userID);
	}

	@Override
	public int selectRoomFloor(int lectureRoomNo) {
		return sqlSession.selectOne("SelectRoomFloor", lectureRoomNo);
	}

	@Override
	public String selectReservationStartTimeForException(String startTime) {
		String output = sqlSession.selectOne("SelectReservationTimeForException", startTime);
		if (output == null) {
			return "0";
		} else {
			return output;
		}
	}

	@Override
	public UserReservation selectRoomInfo(String userID, UserReservation userReservation) {
		userReservation = sqlSession.selectOne("SelectRoomInfo", userID);
		return userReservation;
	}

	@Override
	public boolean deleteReservation(UserReservation userReservation) {
		// delete문의 경우 삭제된 row의 수를 return한다.
		int row = sqlSession.delete("DeleteReservation", userReservation);
		if (row == 0) {
			return false;
		} else {
			return true;
		}
	}
}

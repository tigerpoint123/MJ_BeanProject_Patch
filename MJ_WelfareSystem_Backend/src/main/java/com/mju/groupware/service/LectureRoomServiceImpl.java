package com.mju.groupware.service;

import com.mju.groupware.constant.ConstantLectureRoomController;
import com.mju.groupware.dao.LectureRoomDao;
import com.mju.groupware.dto.LectureRoom;
import com.mju.groupware.dto.UserReservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureRoomServiceImpl implements LectureRoomService {
	private final LectureRoomDao lectureRoomDao;
	private final ConstantLectureRoomController constantLecture;

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
	public String getReservationPage(String lectureRoomNo, String reservationDate, Model model) {
		int maxNumOfPeople = lectureRoomDao.selectMaxNumOfPeople(lectureRoomNo);
		model.addAttribute("LectureRoomNo", lectureRoomNo);
		model.addAttribute("MaxNumOfPeople", maxNumOfPeople);
		model.addAttribute("ReservationDate", reservationDate);

		if (lectureRoomDao.selectStartTime(lectureRoomNo) != null) {
			// emailLIst와 같은 논리. mapper에서 ReservationStartTime을 가져오고 리스트에 저장해서 jsp로 옮겨줌.
			// reservation jsp에 있는 for each 문으로 들어가서, 예약돼있는 시간대를 선택할 수 없도록 막음.
			List<UserReservation> startTime = lectureRoomDao.selectStartTime(lectureRoomNo);
			model.addAttribute("StartTime", startTime);

			return this.constantLecture.getRReservation();
		} else {
			return this.constantLecture.getRReservation();
		}
	}

	@Override
	public String postReservation(int maxNumOfPeople, int reservationNumOfPeople, int lectureRoomNo) {
		return "";
	}

	@Override
	public String getReservationConfirm(int lectureRoomNo, Model model, String userID) {
		String roomLocation = lectureRoomDao.selectLectureRoomLocation(lectureRoomNo);
		int roomFloor = lectureRoomDao.selectRoomFloor(lectureRoomNo);
		int maxNumOfPeople = lectureRoomDao.selectLectureRoomMaxNumOfPeople(lectureRoomNo);

		int reservationNumOfPeople = lectureRoomDao.selectReservationNumOfPeople(userID);
		String reservationStartTime = lectureRoomDao.selectReservationStartTime(userID);

		model.addAttribute("LectureRoomNo", lectureRoomNo);
		model.addAttribute("LectureRoomLocation", roomLocation);
		model.addAttribute("RoomFloor", roomFloor);
		model.addAttribute("MaxNumOfPeople", maxNumOfPeople);
		model.addAttribute("ReservationNumOfPeople", reservationNumOfPeople);

		if (reservationStartTime.equals(this.constantLecture.getNine())) {
			model.addAttribute("ReservationStartTime", "09:00~11:00");
		} else if (reservationStartTime.equals(this.constantLecture.getEleven())) {
			model.addAttribute("ReservationStartTime", "11:00~13:00");
		} else if (reservationStartTime.equals(this.constantLecture.getThirteen())) {
			model.addAttribute("ReservationStartTime", "13:00~15:00");
		} else if (reservationStartTime.equals(this.constantLecture.getFifteen())) {
			model.addAttribute("ReservationStartTime", "15:00~17:00");
		} else if (reservationStartTime.equals(this.constantLecture.getSeventeen())) {
			model.addAttribute("ReservationStartTime", "17:00~19:00");
		} else if (reservationStartTime.equals(this.constantLecture.getNineteen())) {
			model.addAttribute("ReservationStartTime", "19:00~21:00");
		}

		return this.constantLecture.getRReservationConfirm();
	}

	@Override
	public String getMyReservation(Model model, int lectureRoomNo, String userID) {
		String roomLocation = lectureRoomDao.selectLectureRoomLocation(lectureRoomNo);
		int roomFloor = lectureRoomDao.selectRoomFloor(lectureRoomNo);
		int maxNumOfPeople = lectureRoomDao.selectLectureRoomMaxNumOfPeople(lectureRoomNo);

		int reservationNumOfPeople = lectureRoomDao.selectReservationNumOfPeople(userID);
		String reservationStartTime = lectureRoomDao.selectReservationStartTime(userID);

		model.addAttribute("LectureRoomNo", lectureRoomNo);
		model.addAttribute("LectureRoomLocation", roomLocation);
		model.addAttribute("RoomFloor", roomFloor);
		model.addAttribute("MaxNumOfPeople", maxNumOfPeople);
		model.addAttribute("ReservationNumOfPeople", reservationNumOfPeople);

		if (reservationStartTime.equals(this.constantLecture.getNine())) {
			model.addAttribute("ReservationStartTime", "09:00~11:00");
		} else if (reservationStartTime.equals(this.constantLecture.getEleven())) {
			model.addAttribute("ReservationStartTime", "11:00~13:00");
		} else if (reservationStartTime.equals(this.constantLecture.getThirteen())) {
			model.addAttribute("ReservationStartTime", "13:00~15:00");
		} else if (reservationStartTime.equals(this.constantLecture.getFifteen())) {
			model.addAttribute("ReservationStartTime", "15:00~17:00");
		} else if (reservationStartTime.equals(this.constantLecture.getSeventeen())) {
			model.addAttribute("ReservationStartTime", "17:00~19:00");
		} else if (reservationStartTime.equals(this.constantLecture.getNineteen())) {
			model.addAttribute("ReservationStartTime", "19:00~21:00");
		}
		return this.constantLecture.getRConfirmMyReservation();
	}

	@Override
	public boolean deleteReservation(UserReservation userReservation) {
        return lectureRoomDao.deleteReservation(userReservation);
	}

	@Override
	public boolean isExceedingCapacity(int lectureRoomNo, int reservationNumOfPeople) {
		int maxNumOfPeople = lectureRoomDao.selectMaxNumOfPeople(String.valueOf(lectureRoomNo));
		return maxNumOfPeople < reservationNumOfPeople;
	}

	@Override
	public boolean hasDuplicateReservation(String userLoginID) {
		int userID = Integer.parseInt(lectureRoomDao.selectLoginUserId(userLoginID));
		int reservationUserID = lectureRoomDao.selectReservationUserId(userID);
		return reservationUserID != 0;
	}

	@Override
	public boolean hasTimeConflict(String startTime) {
		String reservationStartTime = lectureRoomDao.selectReservationStartTimeForException(startTime);
		return !reservationStartTime.equals("0");
	}

	@Override
	public void createReservation(String userLoginID, String selectedTime, int lectureRoomNo, 
	                               int reservationNumOfPeople, String reservationDate) {
		// 시간 파싱
		int idx = selectedTime.indexOf("~");
		String startTime = selectedTime.substring(0, idx);
		String endTime = selectedTime.substring(idx + 1);
		
		// UserID 조회
		int userID = Integer.parseInt(lectureRoomDao.selectLoginUserId(userLoginID));
		
		// 예약 저장
		UserReservation reservation = new UserReservation();
		reservation.setLectureRoomNo(lectureRoomNo);
		reservation.setReservationDate(reservationDate);
		reservation.setReservationStartTime(startTime);
		reservation.setReservationEndTime(endTime);
		reservation.setReservationNumOfPeople(reservationNumOfPeople);
		reservation.setUserID(userID);
		lectureRoomDao.insertReservation(reservation);
	}

	@Override
	public String getLectureRoomList(Model model) {
		List<LectureRoom> list = lectureRoomDao.selectLectureRoomList();
		model.addAttribute("list", list);

		return this.constantLecture.getRLectureRoomList();
	}

	@Override
	public boolean cancelReservation(String userLoginID) {
		String userID = lectureRoomDao.selectLoginUserId(userLoginID);
		UserReservation userReservation = lectureRoomDao.selectRoomInfo(userID, new UserReservation());

		return lectureRoomDao.deleteReservation(userReservation);
	}
}

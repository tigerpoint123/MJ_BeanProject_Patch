package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantLectureRoomController;
import com.mju.groupware.dto.LectureRoom;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserReservation;
import com.mju.groupware.service.LectureRoomService;
import com.mju.groupware.util.UserInfoMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class LectureRoomController {
	private final LectureRoomService lectureRoomService;
	private final UserInfoMethod userInfoMethod;
	private final ConstantLectureRoomController constantLecture;

	// 강의실 리스트 /lectureRoomList
	@GetMapping("/lectureRoom/lectureRoomList")
	public String lectureRoomList(Model model, Principal principal, User user) {
		// 유저 정보
		userInfoMethod.getUserInformation(principal, user, model, "STUDENT", "PROFESSOR", "ADMINISTRATOR");
		List<LectureRoom> list = lectureRoomService.selectLectureRoomList();
		model.addAttribute("list", list);

		return this.constantLecture.getRLectureRoomList();
	}

	// 강의실 예약 화면
	@GetMapping("/lectureRoom/reservation")
	public String lectureRoomReservation(Locale locale, Model model, HttpServletRequest request,
			UserReservation userReservation, Principal principal, User user) {
		userInfoMethod.getUserInformation(principal, user, model, "STUDENT", "PROFESSOR", "ADMINISTRATOR");

//		lectureRoomService.getReservationPage();

		String lectureRoomNo = request.getParameter("no");
		int maxNumOfPeople = lectureRoomService.selectMaxNumOfPeople(lectureRoomNo);
		String reservationDate = request.getParameter("ReservationDate");

		model.addAttribute("LectureRoomNo", lectureRoomNo);
		model.addAttribute("MaxNumOfPeople", maxNumOfPeople);
		model.addAttribute("ReservationDate", reservationDate);

		if (lectureRoomService.selectStartTime(lectureRoomNo) != null) {
			// emailLIst와 같은 논리. mapper에서 ReservationStartTime을 가져오고 리스트에 저장해서 jsp로 옮겨줌.
			// reservation jsp에 있는 for each 문으로 들어가서, 예약돼있는 시간대를 선택할 수 없도록 막음.
			List<UserReservation> startTime = lectureRoomService.selectStartTime(lectureRoomNo);
			model.addAttribute("StartTime", startTime);

			return this.constantLecture.getRReservation();
		} else {
			return this.constantLecture.getRReservation();
		}
	}

	@PostMapping("/lectureRoom/LectureRoomReservation")
	public String lectureRoomReservationDO(Principal principal, Model model, HttpServletRequest request,
										   UserReservation userReservation, HttpServletResponse response, User user, RedirectAttributes rttr)
			throws IOException {
		// 유저 정보
		userInfoMethod.getUserInformation(principal, user, model, "STUDENT", "PROFESSOR", "ADMINISTRATOR");

		String selectedTime = request.getParameter("ReservationStartTime"); // 스크롤바에서 선택된 값
		int idx = selectedTime.indexOf("~");// 시작, 종료 시간 나누기 위함.
		String userLoginID = principal.getName();
		Date now = new Date();
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		PrintWriter out = response.getWriter();

		String startTime = selectedTime.substring(0, idx);
		String endTime = selectedTime.substring(idx + 1);
		int lectureRoomNo = Integer.parseInt(request.getParameter("roomNum"));
		int maxNumOfPeople = lectureRoomService.selectMaxNumOfPeople(request.getParameter("roomNum"));
		int userID = Integer.parseInt(lectureRoomService.selectLoginUserId(userLoginID));
		int reservationNumOfPeople = Integer.parseInt(request.getParameter("ReservationNumOfPeople"));

		// 한 사람 중복예약 방지
		userReservation.setUserID(userID);
		int reservationUserID = lectureRoomService.selectReservationUserId(userID);

		userReservation.setReservationStartTime(startTime);
		String reservationStartTime = lectureRoomService.selectReservationStartTimeForException(startTime);

		if (maxNumOfPeople < reservationNumOfPeople) {
			rttr.addFlashAttribute("Checker", "ExceptionNum");
			return this.constantLecture.getRRLectureRoomList();
		} else {
			// 중복예약 방지
			if (reservationUserID != 0) { // 해당 유저가 이미 예약을 한 상태면
				rttr.addFlashAttribute("Checker", "DuplicateReservationExist");
				return this.constantLecture.getRRLectureRoomList();
			} else {
				if (!reservationStartTime.equals("0")) {
					response.setContentType("text/html; charset=UTF-8");
					out.println("<script>alert('이미 예약된 강의실입니다.');</script>");
					out.flush();

					String lectureRoomNo2 = request.getParameter("roomNum");
					int maxNumOfPeople2 = lectureRoomService.selectMaxNumOfPeople(lectureRoomNo2);
					String reservationDate = request.getParameter("ReservationDate");
					model.addAttribute("LectureRoomNo", lectureRoomNo2);
					model.addAttribute("MaxNumOfPeople", maxNumOfPeople2);
					model.addAttribute("ReservationDate", reservationDate);

					if (lectureRoomService.selectStartTime(lectureRoomNo2) != null) {
						List<UserReservation> startTime2 = lectureRoomService.selectStartTime(lectureRoomNo2);
						model.addAttribute("StartTime", startTime2);
					}

					return this.constantLecture.getRReservation();
				} else {
					userReservation.setLectureRoomNo(lectureRoomNo);
					userReservation.setReservationDate(date.format(now));
					userReservation.setReservationEndTime(endTime);
					userReservation.setReservationNumOfPeople(reservationNumOfPeople);
					userReservation.setReservationStartTime(startTime);
					userReservation.setUserID(userID);
					lectureRoomService.insertReservation(userReservation);
					rttr.addFlashAttribute("Checker","reservationConfirm");
					return this.constantLecture.getRRLectureRoomList();
				}
			}
		}

	}

	// 강의실 예약 확인 화면
	@GetMapping("/lectureRoom/reservationConfirm")
	public String lectureRoomReservationConfirm(Locale locale, Model model, Principal principal, User user,
			HttpServletResponse response, RedirectAttributes rttr) {
		// 유저 정보
		String loginID = principal.getName();// 로그인 한 아이디
		userInfoMethod.getUserInformation(principal, user, model, "STUDENT", "PROFESSOR", "ADMINISTRATOR");
		//
		String userID = lectureRoomService.selectUserIdForReservationConfirm(loginID);

		int lectureRoomNo = lectureRoomService.selectLectureRoomNo(userID);
		if (lectureRoomNo != 0) {
			String roomLocation = lectureRoomService.selectLectureRoomLocation(lectureRoomNo);
			int roomFloor = lectureRoomService.selectRoomFloor(lectureRoomNo);
			int maxNumOfPeople = lectureRoomService.selectLectureRoomMaxNumOfPeople(lectureRoomNo);

			int reservationNumOfPeople = lectureRoomService.selectReservationNumOfPeople(userID);
			String reservationStartTime = lectureRoomService.selectReservationStartTime(userID);

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
		} else {
			rttr.addFlashAttribute("Checker", "Noting");
			return this.constantLecture.getRRLectureRoomList();
		}

	}

	@PostMapping("/lectureRoom/ReservationConfirm")
	public String DolectureRoomReservationConfirm(Principal principal, UserReservation userReservation, Model model,
			User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes rttr)
			throws IOException {
		// 유저 정보
		userInfoMethod.getUserInformation(principal, user, model, "STUDENT", "PROFESSOR", "ADMINISTRATOR");
		//
		String userLoginID = getUserLoginID(principal);
		String userID = lectureRoomService.selectLoginUserId(userLoginID);
		userReservation = lectureRoomService.selectRoomInfo(userID, userReservation);

		boolean check = lectureRoomService.deleteReservation(userReservation);
		if (check) {
			rttr.addFlashAttribute("Checker", "true");
			return this.constantLecture.getRRLectureRoomList();
		} else {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html; charset=UTF-8");
			out.println("<script>alert('관리자에게 문의바랍니다.');</script>");
			out.flush();
			return this.constantLecture.getRReservationConfirm();
		}

	}

	private String getUserLoginID(Principal principal) {
		String userLoginID = "";
		if (!principal.getName().isEmpty()) {
			userLoginID = principal.getName();
		} else {
			try {
				principal.wait(10);
				principal.notify();
				userLoginID = principal.getName();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		return userLoginID;

	}

	// 강의실 예약 화면
	@GetMapping("/lectureRoom/reservationModify")
	public String lectureRoomReservationModify(Locale locale, Model model, Principal principal, User user) {
		// 유저 정보
		userInfoMethod.getUserInformation(principal, user, model, "STUDENT", "PROFESSOR", "ADMINISTRATOR");
		//
		return this.constantLecture.getRReservationModify();
	}

	// 마이페이지 - 강의실 예약 확인
	@GetMapping("/confirmMyReservation")
	public String confirmMyReservation(Locale locale, Model model, Principal principal, User user,
			HttpServletResponse response, RedirectAttributes rttr) {
		// 유저 정보
		String loginID = principal.getName();// 로그인 한 아이디
		userInfoMethod.getUserInformation(principal, user, model, "STUDENT", "PROFESSOR", "ADMINISTRATOR");
		//
		String userID = lectureRoomService.selectUserIdForReservationConfirm(loginID);

		int lectureRoomNo = lectureRoomService.selectLectureRoomNo(userID);
		if (lectureRoomNo != 0) {
			String roomLocation = lectureRoomService.selectLectureRoomLocation(lectureRoomNo);
			int roomFloor = lectureRoomService.selectRoomFloor(lectureRoomNo);
			int maxNumOfPeople = lectureRoomService.selectLectureRoomMaxNumOfPeople(lectureRoomNo);

			int reservationNumOfPeople = lectureRoomService.selectReservationNumOfPeople(userID);
			String reservationStartTime = lectureRoomService.selectReservationStartTime(userID);

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
		} else {
			rttr.addFlashAttribute("Checker", "Noting");
			return this.constantLecture.getRRMyPageStudent();
		}
	}

// private GetUserInformation 메서드는 공통 유틸 호출로 대체되었으므로 제거되었습니다.

}
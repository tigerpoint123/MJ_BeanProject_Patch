package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantLectureRoomController;
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
        return lectureRoomService.getLectureRoomList(model);
    }

    // 강의실 예약 화면
    @GetMapping("/lectureRoom/reservation")
    public String lectureRoomReservation(Model model, HttpServletRequest request,
                                         Principal principal, User user) {
        userInfoMethod.getUserInformation(principal, user, model, "STUDENT", "PROFESSOR", "ADMINISTRATOR");

        String lectureRoomNo = request.getParameter("no");
        String reservationDate = request.getParameter("ReservationDate");

        return lectureRoomService.getReservationPage(lectureRoomNo, reservationDate, model);
    }

    @PostMapping("/lectureRoom/LectureRoomReservation")
    public String lectureRoomReservationDO(Principal principal, Model model, HttpServletRequest request,
                                           HttpServletResponse response, User user, RedirectAttributes rttr)
            throws IOException {
        userInfoMethod.getUserInformation(principal, user, model, "STUDENT", "PROFESSOR", "ADMINISTRATOR");

        // Request 파라미터 추출
        String selectedTime = request.getParameter("ReservationStartTime");
        int lectureRoomNo = Integer.parseInt(request.getParameter("roomNum"));
        int reservationNumOfPeople = Integer.parseInt(request.getParameter("ReservationNumOfPeople"));
        String reservationDate = request.getParameter("ReservationDate");
        String userLoginID = principal.getName();

        // 시간 파싱 (검증에 필요)
        int idx = selectedTime.indexOf("~");
        String startTime = selectedTime.substring(0, idx);

        // Early Return 패턴 (Guard Clause)
        // 검증 1: 인원수 초과
        if (lectureRoomService.isExceedingCapacity(lectureRoomNo, reservationNumOfPeople)) {
            rttr.addFlashAttribute("Checker", "ExceptionNum");
            return this.constantLecture.getRRLectureRoomList();
        }

        // 검증 2: 중복 예약
        if (lectureRoomService.hasDuplicateReservation(userLoginID)) {
            rttr.addFlashAttribute("Checker", "DuplicateReservationExist");
            return this.constantLecture.getRRLectureRoomList();
        }

        // 검증 3: 시간대 충돌
        if (lectureRoomService.hasTimeConflict(startTime)) {
            PrintWriter out = response.getWriter();
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script>alert('이미 예약된 강의실입니다.');</script>");
            out.flush();

            model.addAttribute("LectureRoomNo", lectureRoomNo);
            model.addAttribute("MaxNumOfPeople", lectureRoomService.selectMaxNumOfPeople(String.valueOf(lectureRoomNo)));
            model.addAttribute("ReservationDate", reservationDate);

            List<UserReservation> startTimeList = lectureRoomService.selectStartTime(String.valueOf(lectureRoomNo));
            if (startTimeList != null) {
                model.addAttribute("StartTime", startTimeList);
            }
            return this.constantLecture.getRReservation();
        }

        // 모든 검증 통과 → 예약 생성
        Date now = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        lectureRoomService.createReservation(userLoginID, selectedTime, lectureRoomNo,
                reservationNumOfPeople, date.format(now));
        rttr.addFlashAttribute("Checker", "reservationConfirm");
        return this.constantLecture.getRRLectureRoomList();
    }

    // 강의실 예약 확인 화면
    @GetMapping("/lectureRoom/reservationConfirm")
    public String lectureRoomReservationConfirm(Model model, Principal principal, User user, RedirectAttributes rttr) {
        String loginID = principal.getName();// 로그인 한 아이디
        userInfoMethod.getUserInformation(principal, user, model, "STUDENT", "PROFESSOR", "ADMINISTRATOR");
        String userID = lectureRoomService.selectUserIdForReservationConfirm(loginID);
        int lectureRoomNo = lectureRoomService.selectLectureRoomNo(userID);

        if (lectureRoomNo != 0) {
            return lectureRoomService.getReservationConfirm(lectureRoomNo, model, userID);
        } else {
            rttr.addFlashAttribute("Checker", "Noting");
            return this.constantLecture.getRRLectureRoomList();
        }
    }

    @PostMapping("/lectureRoom/ReservationConfirm")
    public String deleteReservation(Principal principal, Model model,
                                    User user, HttpServletResponse response, RedirectAttributes rttr)
            throws IOException {
        userInfoMethod.getUserInformation(principal, user, model, "STUDENT", "PROFESSOR", "ADMINISTRATOR");

        String userLoginID = principal.getName();

        boolean isSuccess = lectureRoomService.cancelReservation(userLoginID);

        if (isSuccess) {
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

    // 강의실 예약 화면
    @GetMapping("/lectureRoom/reservationModify")
    public String lectureRoomReservationModify(Model model, Principal principal, User user) {
        userInfoMethod.getUserInformation(principal, user, model, "STUDENT", "PROFESSOR", "ADMINISTRATOR");
        return this.constantLecture.getRReservationModify();
    }

    // 마이페이지 - 강의실 예약 확인
    @GetMapping("/confirmMyReservation")
    public String confirmMyReservation(Model model, Principal principal, User user, RedirectAttributes rttr) {
        String loginID = principal.getName();// 로그인 한 아이디
        userInfoMethod.getUserInformation(principal, user, model, "STUDENT", "PROFESSOR", "ADMINISTRATOR");
        String userID = lectureRoomService.selectUserIdForReservationConfirm(loginID);

        int lectureRoomNo = lectureRoomService.selectLectureRoomNo(userID);
        if (lectureRoomNo != 0) {
            return lectureRoomService.getMyReservation(model, lectureRoomNo, userID);
        } else {
            rttr.addFlashAttribute("Checker", "Noting");
            return this.constantLecture.getRRMyPageStudent();
        }
    }
}
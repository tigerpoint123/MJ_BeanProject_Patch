package com.mju.groupware.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mju.groupware.constant.ConstantScheduleController;
import com.mju.groupware.dto.Calender;
import com.mju.groupware.dto.User;
import com.mju.groupware.util.UserInfoMethod;
import com.mju.groupware.service.CalenderService;

@Controller
@RequiredArgsConstructor
public class ScheduleController {
    private final ConstantScheduleController Constant;
	private final UserInfoMethod userInfoMethod;
	private final CalenderService calenderService;
	
	// 일정 화면
	@RequestMapping(value = "/schedule/mySchedule", method = { RequestMethod.GET, RequestMethod.POST })
	public String schedule(Locale locale, Model model, Principal principal, User user) {
		if (principal != null) {
			// 유저 정보
			String LoginID = principal.getName();// 로그인 한 아이디
			int UserID = calenderService.SelectUserIdForCalender(LoginID);
			model.addAttribute(this.Constant.getUserID(), UserID);
			userInfoMethod.getUserInformation(principal, user, model, this.Constant.getSRole(), this.Constant.getPRole(), this.Constant.getARole());

		}
		return this.Constant.getSchedule();
	}

	// 일정 받기
	@ResponseBody
	@RequestMapping(value = "/schedule/GetSchedule.do", method = { RequestMethod.GET, RequestMethod.POST })
	public List<HashMap<String, Object>> GetSchedule(Principal principal) {
		int UserID = SelectUserIDForCalender(principal);

        return calenderService.SelectSchedule(UserID);
	}

	// 일정 추가
	@ResponseBody
	@RequestMapping(value = "/schedule/AddSchedule.do", method = RequestMethod.POST)
	public int AddSchedule(@RequestBody Calender calender, Principal principal, Model model) {

		int UserID = SelectUserIDForCalender(principal);
		if (UserID != 0) {
			calender.setUserId(UserID);
		}
        return calenderService.InsertSchedule(calender);
	}

	private int SelectUserIDForCalender(Principal principal) {
		String LoginID = principal.getName();
        return calenderService.SelectUserIdForCalender(LoginID);
	}

	// 일정 선택하여 일정 수정
	@ResponseBody
	@RequestMapping(value = "/schedule/ModifySchedule.do", method = RequestMethod.POST)
	public int ModifySchedule(Principal principal, @RequestBody Calender calender, HttpServletRequest reqeust) {
		int UserID = SelectUserIDForCalender(principal);
		if (UserID != 0) {
			calender.setUserId(UserID);
		}
		String ID = calender.getId();

        return calenderService.UpdateSchedule(Integer.toString(UserID), ID, calender);
	}

	// 월 달력 - 일정 드래그 앤 드롭
	@ResponseBody
	@RequestMapping(value = "/schedule/modifyScheduleFromMonth.do", method = RequestMethod.POST)
	public int modifyTimeInMonth(Principal principal, @RequestBody Calender calender) {
		int UserID = SelectUserIDForCalender(principal);
		HashMap<String, String> Map = new HashMap<>();
		Map.put(this.Constant.getUserID(), Integer.toString(UserID));
		Map.put(this.Constant.getScheduleID(), calender.getId());
		Map.put(this.Constant.getStart(), calender.getStart());
		Map.put(this.Constant.getEnd(), calender.getEnd());

        return calenderService.UpdateTimeInMonth(Map);
	}

	// 주 달력 - 일정 리사이즈
	@ResponseBody
	@RequestMapping(value = "/schedule/ModifyScheduleFromWeek.do", method = RequestMethod.POST)
	public int ModifyScheduleFromWeek(Principal principal, @RequestBody Calender calender, HttpServletRequest reqeust) {
		int UserID = SelectUserIDForCalender(principal);
		if (UserID != 0) {
			calender.setUserId(UserID);
		}
		String ID = calender.getId();

        return calenderService.UpdateTimeInWeek(Integer.toString(UserID), ID, calender);
	}

	// 일정 삭제
	@ResponseBody
	@RequestMapping(value = "/schedule/DeleteSchedule.do", method = RequestMethod.POST)
	public int DeleteSchedule(@RequestBody Calender calender, Principal principal, Model model) {
		int UserID = SelectUserIDForCalender(principal);
		if (UserID != 0) {
			calender.setUserId(UserID);
		}
		String ID = calender.getId();
        return calenderService.DeleteSchedule(Integer.toString(UserID), ID);
	}

	@ResponseBody
	@RequestMapping(value = "/schedule/modfiyMonthTime.do", method = RequestMethod.POST)
	public int ModfiyMonthTime(Principal principal, @RequestParam String start, @RequestParam String end) {
        return 0;
	}
}
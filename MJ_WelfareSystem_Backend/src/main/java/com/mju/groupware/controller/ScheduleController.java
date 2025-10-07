package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantScheduleController;
import com.mju.groupware.dto.Calender;
import com.mju.groupware.dto.User;
import com.mju.groupware.service.ScheduleService;
import com.mju.groupware.util.UserInfoMethod;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class ScheduleController {
    private final ConstantScheduleController constant;
    private final UserInfoMethod userInfoMethod;
    private final ScheduleService scheduleService;

    // 일정 화면
    @RequestMapping(value = "/schedule/mySchedule", method = {RequestMethod.GET, RequestMethod.POST})
    public String schedule(Locale locale, Model model, Principal principal, User user) {
        // 유저 정보
        int userId = selectUserIdForCalender(principal);
        model.addAttribute(this.constant.getUserID(), userId);
        userInfoMethod.getUserInformation(principal, user, model, this.constant.getSRole(), this.constant.getPRole(), this.constant.getARole());
        return this.constant.getSchedule();
    }

    // 일정 받기
    @ResponseBody
    @RequestMapping(value = "/schedule/GetSchedule.do", method = {RequestMethod.GET, RequestMethod.POST})
    public List<HashMap<String, Object>> getSchedule(Principal principal) {
        int userId = selectUserIdForCalender(principal);

        return scheduleService.selectSchedule(userId);
    }

    // 일정 추가
    @ResponseBody
    @RequestMapping(value = "/schedule/AddSchedule.do", method = RequestMethod.POST)
    public int addSchedule(@RequestBody Calender calender, Principal principal, Model model) {

        int userId = selectUserIdForCalender(principal);
        if (userId != 0) {
            calender.setUserId(userId);
        }
        return scheduleService.insertSchedule(calender);
    }

    private int selectUserIdForCalender(Principal principal) {
        String loginId = principal.getName();
        return scheduleService.selectUserIdForCalender(loginId);
    }

    // 일정 선택하여 일정 수정
    @ResponseBody
    @RequestMapping(value = "/schedule/ModifySchedule.do", method = RequestMethod.POST)
    public int modifySchedule(Principal principal, @RequestBody Calender calender, HttpServletRequest reqeust) {
        int userId = selectUserIdForCalender(principal);
        if (userId != 0) {
            calender.setUserId(userId);
        }
        String id = calender.getId();

        return scheduleService.updateSchedule(Integer.toString(userId), id, calender);
    }

    // 월 달력 - 일정 드래그 앤 드롭
    @ResponseBody
    @RequestMapping(value = "/schedule/modifyScheduleFromMonth.do", method = RequestMethod.POST)
    public int modifyTimeInMonth(Principal principal, @RequestBody Calender calender) {
        int userId = selectUserIdForCalender(principal);
        HashMap<String, String> map = new HashMap<>();
        map.put(this.constant.getUserID(), Integer.toString(userId));
        map.put(this.constant.getScheduleID(), calender.getId());
        map.put(this.constant.getStart(), calender.getStart());
        map.put(this.constant.getEnd(), calender.getEnd());

        return scheduleService.updateTimeInMonth(map);
    }

    // 주 달력 - 일정 리사이즈
    @ResponseBody
    @RequestMapping(value = "/schedule/ModifyScheduleFromWeek.do", method = RequestMethod.POST)
    public int modifyScheduleFromWeek(Principal principal, @RequestBody Calender calender, HttpServletRequest reqeust) {
        int userId = selectUserIdForCalender(principal);
        if (userId != 0) {
            calender.setUserId(userId);
        }
        String id = calender.getId();

        return scheduleService.updateTimeInWeek(Integer.toString(userId), id, calender);
    }

    // 일정 삭제
    @ResponseBody
    @RequestMapping(value = "/schedule/DeleteSchedule.do", method = RequestMethod.POST)
    public int deleteSchedule(@RequestBody Calender calender, Principal principal, Model model) {
        int userId = selectUserIdForCalender(principal);
        if (userId != 0) {
            calender.setUserId(userId);
        }
        String id = calender.getId();
        return scheduleService.deleteSchedule(Integer.toString(userId), id);
    }

    @ResponseBody
    @RequestMapping(value = "/schedule/modfiyMonthTime.do", method = RequestMethod.POST)
    public int modifyMonthTime(Principal principal, @RequestParam String start, @RequestParam String end) {
        return 0;
    }
}
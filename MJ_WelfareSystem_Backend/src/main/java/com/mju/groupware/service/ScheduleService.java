package com.mju.groupware.service;

import com.mju.groupware.dto.Calender;

import java.util.HashMap;
import java.util.List;

public interface ScheduleService {

	int insertSchedule(Calender calender);

	List<HashMap<String, Object>> selectSchedule(int userId);

	int selectUserIdForCalender(String loginId);

	int updateSchedule(String string, String id, Calender calender);

	int deleteSchedule(String string, String id);

	int updateTimeInMonth(HashMap<String, String> map);
	
	int updateTimeInWeek(String string, String id, Calender calender);
}

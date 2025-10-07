package com.mju.groupware.service;

import com.mju.groupware.dao.ScheduleDao;
import com.mju.groupware.dto.Calender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private ScheduleDao scheduleDao;

	@Override
	public int insertSchedule(Calender calender) {
		int count = scheduleDao.InsertSchedule(calender);
		return count;
	}

	@Override
	public List<HashMap<String, Object>> selectSchedule(int userId) {
		return scheduleDao.SelectSchedule(userId);
	}

	@Override
	public int selectUserIdForCalender(String loginId) {
		Integer userId = scheduleDao.SelectUserIdForCalender(loginId);
		if (userId == null) {
			return 0;
		} else {
			return userId;
		}
	}

	@Override
	public int updateSchedule(String userId, String id, Calender calender) {
		int count = scheduleDao.UpdateSchedule(userId, id, calender);
		return count;

	}

	@Override
	public int deleteSchedule(String userId, String id) {
		int count = scheduleDao.DeleteSchedule(userId, id);
		return count;
	}

	@Override
	public int updateTimeInMonth(HashMap<String, String> map) {
		return scheduleDao.UpdateTimeInMonth(map);
	}

	@Override
	public int updateTimeInWeek(String userId, String id, Calender calender) {
		int count = scheduleDao.UpdateTimeInWeek(userId, id, calender);
		return count;
	}
	
}

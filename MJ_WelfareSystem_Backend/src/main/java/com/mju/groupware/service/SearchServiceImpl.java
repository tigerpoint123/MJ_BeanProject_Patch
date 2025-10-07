package com.mju.groupware.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mju.groupware.dao.SearchDao;
import com.mju.groupware.dto.Professor;
import com.mju.groupware.dto.SearchKeyWord;
import com.mju.groupware.dto.Student;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserReview;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;

	@Autowired
	private UserService userService;

	@Override
	public List<User> SelectKeyWord(SearchKeyWord searchKeyWord) {
		return searchDao.SelectKeyWord(searchKeyWord);
	}

	@Override
	public Student SelectStudentInfo(int userID) {
		return searchDao.SelectStudentInfo(userID);
	}

	@Override
	public Professor selectProfessorInfo(int userID) {
		return searchDao.SelectProfessorInfo(userID);
	}

	@Override
	public List<UserReview> SelectUserReview(String userID) {
		return searchDao.SelectUserReview(userID);
	}

	@Override
	public List<HashMap<String, Object>> searchUserInfoList(SearchKeyWord searchKeyWord, 
	                                                          String studentRole, 
	                                                          String professorRole,
	                                                          String nameKey,
	                                                          String emailKey,
	                                                          String phoneKey) {
		List<User> userList = SelectKeyWord(searchKeyWord);
		List<HashMap<String, Object>> resultList = new ArrayList<>();
		
		for (User user : userList) {
			HashMap<String, Object> userMap;
			
			if (user.getUserRole().equals(studentRole)) {
				userMap = buildStudentInfo(user, nameKey, emailKey, phoneKey);
			} else if (user.getUserRole().equals(professorRole)) {
				userMap = buildProfessorInfo(user, nameKey, emailKey, phoneKey);
			} else {
				continue;
			}
			
			resultList.add(userMap);
		}
		
		return resultList;
	}

	private HashMap<String, Object> buildProfessorInfo(User user, String nameKey, String emailKey, String phoneKey) {
		HashMap<String, Object> map = new HashMap<>();
		Professor professor = selectProfessorInfo(user.getUserID());
		
		map.put(nameKey, user.getUserName());
		map.put(emailKey, user.getUserEmail());
		map.put("Gender", "비공개");
		
		if (user.getOpenPhoneNum().equals("비공개")) {
			map.put(phoneKey, user.getOpenPhoneNum());
		} else {
			map.put(phoneKey, user.getUserPhoneNum());
		}
		
		map.put("UserMajor", professor.getProfessorMajor());
		map.put("Role", "교수님");
		
		return map;
	}

	private HashMap<String, Object> buildStudentInfo(User user, String nameKey, String emailKey, String phoneKey) {
		HashMap<String, Object> map = new HashMap<>();
		Student student = SelectStudentInfo(user.getUserID());
		
		map.put(nameKey, user.getUserName());
		map.put(emailKey, user.getUserEmail());
		map.put("UserMajor", student.getStudentMajor());
		
		if (user.getOpenPhoneNum().equals("비공개")) {
			map.put(phoneKey, user.getOpenPhoneNum());
		} else {
			map.put(phoneKey, user.getUserPhoneNum());
		}
		
		map.put("Major", student.getStudentMajor());
		map.put("Gender", student.getStudentGender());
		map.put("Role", "학생");
		
		return map;
	}

	@Override
	public List<UserReview> getUserReviewListByEmail(String userEmail) {
		String userID = userService.SelectIDForReview(userEmail);
		if (userID == null || userID.isEmpty()) {
			return null;
		}
		
		List<UserReview> reviewList = SelectUserReview(userID);
		return reviewList.isEmpty() ? null : reviewList;
	}

}

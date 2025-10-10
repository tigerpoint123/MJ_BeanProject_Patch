package com.mju.groupware.service;

import com.mju.groupware.dao.StudentDao;
import com.mju.groupware.dto.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private UserService userService;

	@Override
	public void InsertInformation(Student student) {
		studentDao.InsertInformation(student);
	}

	@Override
	public void UpdateUserID(Student student) {
		studentDao.UpdateUserID(student);
	}

	@Override
	public void updateStudentGender(Student student) {
		studentDao.UpdateStudentGender(student);
	}

	@Override
	public void updateStudentGrade(Student student) {
		studentDao.UpdateStudentGrade(student);
	}

	@Override
	public void UpdateStudentDobuleMajor(Student student) {
		studentDao.UpdateStudentDobuleMajor(student);
	}

	@Override
	public ArrayList<String> selectStudentProfileInfo(String userID) {
		ArrayList<String> studentInfo = new ArrayList<String>();
		studentInfo = studentDao.selectStudentProfileInfo(userID);
		return studentInfo;
	}

	@Override
	public void UpdateStudentGender(Student student) {
		studentDao.UpdateStudentGender(student);
	}

	@Override
	public void UpdateStudentColleges(Student student) {
		studentDao.UpdateStudentColleges(student);
	}

	@Override
	public void UpdateStudentMajor(Student student) {
		studentDao.UpdateStudentMajor(student);
	}

	@Override
	public Student SelectStudentInfo(String userID) {
		return studentDao.SelectStudentInfo(userID);
	}

	@Override
	public void InsertWithdrawalStudent(Student student) {
		studentDao.InsertWithdrawalStudent(student);
	}

	@Override
	public void DeleteWithdrawalStudent(Student student) {
		studentDao.DeleteWithdrawalStudent(student);
	}

	@Override
	public void DeleteWithdrawalStudentList(String string) {
		studentDao.DeleteWithdrawalStudentList(string);
	}

	@Override
	public void UpdateStudentLoginDate(Student student) {
		studentDao.UpdateStudentLoginDate(student);
	}
	
	@Override
	public Student SelectModifyStudentInfo(int userID) {
		return studentDao.SelectModifyStudentInfo(userID);
	}

	@Override
	public HashMap<String, Object> getMyPageStudent(String loginID) {
		HashMap<String, Object> result = new HashMap<>();
		
		// 사용자 프로필 정보 조회
		ArrayList<String> userProfileInfo = userService.selectUserProfileInfo(loginID);
		String userIdStr = userProfileInfo.get(1);
		
		// 학생 정보 조회
		ArrayList<String> studentInfo = selectStudentProfileInfo(userIdStr);
		
		// 기본 정보 설정
		result.put("UserName", userProfileInfo.get(0));
		result.put("Colleges", studentInfo.get(0));
		result.put("UserMajor", studentInfo.get(1));
		result.put("StudentGradeValue", studentInfo.get(2));
		
		// UserRole 조회
		ArrayList<String> roleInfo = userService.selectUserProfileInfo(loginID);
		result.put("UserRole", roleInfo.get(2));
		
		// 마이페이지 상세 정보 조회
		ArrayList<String> detailInfo = userService.selectMyPageUserInfo(loginID);
		
		// 상세 정보 설정
		result.put("UserLoginID", detailInfo.get(0));
		result.put("UserNameDetail", detailInfo.get(1));
		result.put("StudentGender", detailInfo.get(8));
		result.put("UserPhoneNum", detailInfo.get(2));
		result.put("StudentGrade", detailInfo.get(6));
		result.put("StudentColleges", detailInfo.get(4));
		result.put("StudentMajor", detailInfo.get(5));
		result.put("StudentDoubleMajor", detailInfo.get(7));
		
		// 이메일 파싱 (@ 앞부분만 추출)
		String fullEmail = detailInfo.get(3);
		int idx = fullEmail.indexOf("@");
		String email = fullEmail.substring(0, idx);
		result.put("UserEmail", email);
		
		// 정보공개여부
		String openInfo = userService.selectOpenInfo(loginID);
		if (!openInfo.equals("Error")) {
			result.put("UserInfoOpen", openInfo);
		}
		
		return result;
	}

	@Override
	public HashMap<String, Object> getModifyStudent(String loginID) {
		HashMap<String, Object> result = new HashMap<>();
		
		// 사용자 정보 조회
		com.mju.groupware.dto.User userInfo = userService.selectModifyUserInfo(loginID);
		
		// 학생 정보 조회
		Student studentInfo = SelectModifyStudentInfo(userInfo.getUserID());
		
		// 학번
		result.put("UserLoginID", userInfo.getUserLoginID());
		
		// 이름
		result.put("UserName", userInfo.getUserName());
		
		// 이메일 파싱 (@ 앞부분만 추출)
		String fullEmail = userInfo.getUserEmail();
		int location = fullEmail.indexOf("@");
		String email = fullEmail.substring(0, location);
		result.put("Email", email);
		
		// 성별
		result.put("StudentGender", studentInfo.getStudentGender());
		
		// 단과대학
		result.put("StudentColleges", studentInfo.getStudentColleges());
		
		// 전공
		result.put("StudentMajor", studentInfo.getStudentMajor());
		
		// 복수전공
		result.put("StudentDoubleMajor", studentInfo.getStudentDoubleMajor());
		
		// 연락처 공개
		result.put("OpenPhoneNum", userInfo.getOpenPhoneNum());
		
		// 학년 공개
		result.put("OpenGrade", userInfo.getOpenGrade());
		
		return result;
	}

	@Override
	public void setMyPageStudentAttributes(String loginID, Model model, String gradeKey, 
	                                        String userLoginIDKey, String userNameKey, 
	                                        String userPhoneNumKey, String userEmailKey) {
		HashMap<String, Object> myPageData = getMyPageStudent(loginID);
		
		// Model에 모든 데이터 설정
		model.addAttribute("UserName", myPageData.get("UserName"));
		model.addAttribute("Colleges", myPageData.get("Colleges"));
		model.addAttribute("UserMajor", myPageData.get("UserMajor"));
		model.addAttribute(gradeKey, myPageData.get("StudentGradeValue"));
		model.addAttribute("UserRole", myPageData.get("UserRole"));
		model.addAttribute(userLoginIDKey, myPageData.get("UserLoginID"));
		model.addAttribute(userNameKey, myPageData.get("UserNameDetail"));
		model.addAttribute("StudentGender", myPageData.get("StudentGender"));
		model.addAttribute(userPhoneNumKey, myPageData.get("UserPhoneNum"));
		model.addAttribute("StudentGrade", myPageData.get("StudentGrade"));
		model.addAttribute("StudentColleges", myPageData.get("StudentColleges"));
		model.addAttribute("StudentMajor", myPageData.get("StudentMajor"));
		model.addAttribute("StudentDoubleMajor", myPageData.get("StudentDoubleMajor"));
		model.addAttribute(userEmailKey, myPageData.get("UserEmail"));
		
		// 정보공개여부 (존재할 경우에만)
		if (myPageData.containsKey("UserInfoOpen")) {
			model.addAttribute("UserInfoOpen", myPageData.get("UserInfoOpen"));
		}
	}

	@Override
	public void setModifyStudentAttributes(String loginID, Model model, String emailKey) {
		HashMap<String, Object> modifyData = getModifyStudent(loginID);
		
		// Model에 모든 데이터 설정
		model.addAttribute("UserLoginID", modifyData.get("UserLoginID"));
		model.addAttribute("UserName", modifyData.get("UserName"));
		model.addAttribute(emailKey, modifyData.get("Email"));
		model.addAttribute("StudentGender", modifyData.get("StudentGender"));
		model.addAttribute("StudentColleges", modifyData.get("StudentColleges"));
		model.addAttribute("StudentMajor", modifyData.get("StudentMajor"));
		model.addAttribute("StudentDoubleMajor", modifyData.get("StudentDoubleMajor"));
		model.addAttribute("OpenPhoneNum", modifyData.get("OpenPhoneNum"));
		model.addAttribute("OpenGrade", modifyData.get("OpenGrade"));
	}

	@Override
	public void updateStudentInfo(String loginID, String phoneNum, String studentGrade, 
	                              boolean isPhoneOpen, boolean isGradeOpen) {
		// 사용자 정보 조회
		ArrayList<String> userInfo = userService.selectUserInformation(loginID);
		String userIdStr = userInfo.get(0); // 유저아이디
		String userLoginID = userInfo.get(1); // 로그인아이디
		
		// User 객체 설정
		com.mju.groupware.dto.User user = new com.mju.groupware.dto.User();
		user.setUserLoginID(userLoginID);
		
		// Student 객체 설정
		Student student = new Student();
		student.setUserID(Integer.parseInt(userIdStr));
		
		// 연락처 업데이트
		if (phoneNum != null && !phoneNum.isEmpty()) {
			user.setUserPhoneNum(phoneNum);
			userService.updateUserPhoneNumber(user);
		}
		
		// 학년 업데이트
		if (studentGrade != null && !studentGrade.equals(" ")) {
			student.setStudentGrade(studentGrade);
			updateStudentGrade(student);
		}
		
		// 전화번호 공개 여부 업데이트
		if (isPhoneOpen) {
			user.setOpenPhoneNum("전화번호");
		} else {
			user.setOpenPhoneNum("비공개");
		}
		userService.updateOpenPhoneNum(user);
		
		// 학년 공개 여부 업데이트
		if (isGradeOpen) {
			user.setOpenGrade("학년");
		} else {
			user.setOpenGrade("비공개");
		}
		userService.updateOpenGrade(user);
	}
}

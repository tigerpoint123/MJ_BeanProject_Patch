package com.mju.groupware.controller;

import java.security.Principal;
import java.util.ArrayList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mju.groupware.constant.ConstantAdminStudentController;
import com.mju.groupware.dto.Student;
import com.mju.groupware.dto.User;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.UserService;

@Controller
@RequiredArgsConstructor
public class StudentController {

	private final UserService userService;
	private final StudentService studentService;

	private final ConstantAdminStudentController Constant;
	private String StudentColleges;
	private String StudentGrade;
	private String UserMajorForShow;
	private String UserName;

    

	@RequestMapping(value = "/signupStudent", method = RequestMethod.GET)
	public String signupStudent() {
		return this.Constant.getRSignupStudent();
	}

	/* 학생 마이페이지 */
	@RequestMapping(value = "/myPageStudent", method = RequestMethod.GET)
	public String myPageStudent(User user, Model model, HttpServletRequest requestm, Principal Principal) {

		String LoginID = Principal.getName();// 로그인 한 아이디
		ArrayList<String> selectUserProfileInfo = new ArrayList<String>();
		selectUserProfileInfo = userService.selectUserProfileInfo(LoginID);

		user.setUserLoginID(LoginID);
		ArrayList<String> studentInfo = new ArrayList<String>();
		studentInfo = studentService.selectStudentProfileInfo(selectUserProfileInfo.get(1));

		// 학생 이름
		UserName = selectUserProfileInfo.get(0);
		model.addAttribute("UserName", UserName);
		// 학생 소속
		StudentColleges = studentInfo.get(0);
		model.addAttribute("Colleges", StudentColleges);

		UserMajorForShow = studentInfo.get(1);
		model.addAttribute("UserMajor", UserMajorForShow);

		StudentGrade = studentInfo.get(2);
		model.addAttribute(this.Constant.getGrade(), StudentGrade);

		// user role 가져오기
		String UserLoginID = Principal.getName();
		ArrayList<String> info = new ArrayList<String>();
		info = userService.selectUserProfileInfo(UserLoginID);
		model.addAttribute("UserRole", info.get(2));

		// -------------------------------------------------------

		String UserID = Principal.getName();
		ArrayList<String> selectUserInfo = new ArrayList<String>();
		selectUserInfo = userService.SelectMyPageUserInfo(UserID);

		String selectOpenInfo = userService.SelectOpenInfo(UserID);
		// jsp화면 설정
		// 아이디 0
		model.addAttribute(this.Constant.getUserLoginID(), selectUserInfo.get(0));
		// 이름 1
		model.addAttribute(this.Constant.getUserName(), selectUserInfo.get(1));
		// 성별 8
		model.addAttribute("StudentGender", selectUserInfo.get(8));
		// 연락처 2
		model.addAttribute(this.Constant.getUserPhoneNum(), selectUserInfo.get(2));
		// 학년 6
		model.addAttribute("StudentGrade", selectUserInfo.get(6));
		// 단과대학 4
		model.addAttribute("StudentColleges", selectUserInfo.get(4));
		// 전공 5
		model.addAttribute("StudentMajor", selectUserInfo.get(5));
		// 복수전공 7
		model.addAttribute("StudentDoubleMajor", selectUserInfo.get(7));
		// 이메일 5
		int idx = selectUserInfo.get(3).indexOf("@"); // 메일에서 @의 인덱스 번호를 찾음
		String email = selectUserInfo.get(3).substring(0, idx);
		model.addAttribute(this.Constant.getUserEmail(), email);

		// 정보공개여부
		if (!selectOpenInfo.equals("Error")) {
			model.addAttribute("UserInfoOpen", selectOpenInfo);
		}
		return this.Constant.getRMyPageStudent();
	}

	/* 학생 정보 수정 화면 */
	@RequestMapping(value = "/modifyStudent", method = RequestMethod.GET)
	public String modifyStudent(Principal principal, Model model) {
		String loginID = principal.getName();
		User selectUserProfileInfo = userService.SelectModifyUserInfo(loginID);
		Student student = studentService.SelectModifyStudentInfo(selectUserProfileInfo.getUserID());
		// 학번
		model.addAttribute("UserLoginID", selectUserProfileInfo.getUserLoginID());
		// 이름
		model.addAttribute("UserName", selectUserProfileInfo.getUserName());
		// 이메일
		String userEmail = selectUserProfileInfo.getUserEmail();
		int location = userEmail.indexOf("@");
		userEmail = userEmail.substring(0, location);
		model.addAttribute(this.Constant.getEmail(), userEmail);
		// 성별
		model.addAttribute("StudentGender", student.getStudentGender());
		// 단과대학
		model.addAttribute("StudentColleges", student.getStudentColleges());
		// 전공
		model.addAttribute("StudentMajor", student.getStudentMajor());
		// 복수전공
		model.addAttribute("StudentDoubleMajor", student.getStudentDoubleMajor());
		// 연락처 공개
        model.addAttribute("OpenPhoneNum", selectUserProfileInfo.getOpenPhoneNum());
        // 학년 공개
        model.addAttribute("OpenGrade", selectUserProfileInfo.getOpenGrade());
		return this.Constant.getRModifyStudent();
	}

	// 학생 정보 수정
	@RequestMapping(value = "/modifyStudent.do", method = RequestMethod.POST)
	public String DoModifyStudent(HttpServletResponse response, HttpServletRequest request, Model model,
								  Student student, User user, Principal Principal) {
		// 업데이트문 where절을 위한 데이터 get
		String userID = Principal.getName();
		ArrayList<String> userInfo = new ArrayList<String>();
		userInfo = userService.SelectUserInformation(userID);
		userInfo.get(0); // 유저아이디 get
		userInfo.get(1); // 로그인아이디 get

		user.setUserLoginID(userInfo.get(1));
		student.setUserID(Integer.parseInt(userInfo.get(0)));

		// 연락처
		if (!((String) request.getParameter(this.Constant.getUserPhoneNum())).equals("")) {
			user.setUserPhoneNum((String) request.getParameter(this.Constant.getUserPhoneNum()));
			userService.updateUserPhoneNumber(user);
		}
		// 학년
		if (!((String) request.getParameter("StudentGrade")).equals(" ")) {
			student.setStudentGrade((String) request.getParameter("StudentGrade"));
			studentService.updateStudentGrade(student);
		}

		// 정보공개여부 선택
		if (request.getParameter(this.Constant.getUserPhone()) != null) {
			String OpenPhoneNum = "전화번호";
			user.setOpenPhoneNum(OpenPhoneNum);
			userService.UpdateOpenPhoneNum(user);
		} else if (request.getParameter(this.Constant.getUserPhone()) == null) {
			String NotOpen = "비공개";
			user.setOpenPhoneNum(NotOpen);
			userService.UpdateOpenPhoneNum(user);
		}

		if (request.getParameter(this.Constant.getUserGrade()) != null) {
			String OpenGrade = "학년";
			user.setOpenGrade(OpenGrade);
			userService.UpdateOpenGrade(user);
		} else if (request.getParameter(this.Constant.getUserGrade()) == null) {
			String NotOpen = "비공개";
			user.setOpenGrade(NotOpen);
			userService.UpdateOpenGrade(user);
		}

		return this.Constant.getRModifyStudent();
	}

}
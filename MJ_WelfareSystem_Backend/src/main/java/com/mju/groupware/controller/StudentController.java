package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantAdminStudentController;
import com.mju.groupware.dto.User;
import com.mju.groupware.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class StudentController {

	private final StudentService studentService;
	private final ConstantAdminStudentController constant;

	@RequestMapping(value = "/signupStudent", method = RequestMethod.GET)
	public String signupStudent() {
		return this.constant.getRSignupStudent();
	}

	/* 학생 마이페이지 */
	@RequestMapping(value = "/myPageStudent", method = RequestMethod.GET)
	public String myPageStudent(User user, Model model, HttpServletRequest requestm, Principal principal) {
		String loginID = principal.getName();
		user.setUserLoginID(loginID);
		
		// 서비스에서 마이페이지 데이터를 Model에 설정
		studentService.setMyPageStudentAttributes(
			loginID, 
			model, 
			this.constant.getGrade(),
			this.constant.getUserLoginID(),
			this.constant.getUserName(),
			this.constant.getUserPhoneNum(),
			this.constant.getUserEmail()
		);

		return this.constant.getRMyPageStudent();
	}

	/* 학생 정보 수정 화면 */
	@RequestMapping(value = "/modifyStudent", method = RequestMethod.GET)
	public String modifyStudent(Principal principal, Model model) {
		String loginID = principal.getName();
		
		// 서비스에서 수정 화면 데이터를 Model에 설정
		studentService.setModifyStudentAttributes(loginID, model, this.constant.getEmail());
		
		return this.constant.getRModifyStudent();
	}

	// 학생 정보 수정
	@RequestMapping(value = "/modifyStudent.do", method = RequestMethod.POST)
	public String doModifyStudent(HttpServletRequest request, Principal principal) {
		String loginID = principal.getName();
		
		// 요청 파라미터 추출
		String phoneNum = request.getParameter(this.constant.getUserPhoneNum());
		String studentGrade = request.getParameter("StudentGrade");
		boolean isPhoneOpen = request.getParameter(this.constant.getUserPhone()) != null;
		boolean isGradeOpen = request.getParameter(this.constant.getUserGrade()) != null;
		
		// 서비스에서 학생 정보 업데이트 처리
		studentService.updateStudentInfo(loginID, phoneNum, studentGrade, isPhoneOpen, isGradeOpen);
		
		return this.constant.getRModifyStudent();
	}

}
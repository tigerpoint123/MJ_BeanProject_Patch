package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantAdminProfessorController;
import com.mju.groupware.service.ProfessorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProfessorController {
	private final ProfessorService professorService;
	private final ConstantAdminProfessorController constant;

	@GetMapping("/signupProfessor")
	public String signupProfessor() {
		return this.constant.getRSignupProfessor();
	}

	/* 교수 마이페이지 */
	@GetMapping("/myPageProfessor")
	public String myPageProfessor(Model model, Principal principal) {
		String loginID = principal.getName();
		
		// Service에서 모든 마이페이지 데이터를 Model에 설정
		professorService.setProfessorMyPageAttributes(loginID, model);
		
		return constant.getRMyPageProfessor();
	}

	/* 교수 정보 수정 화면 */
	@GetMapping("/modifyProfessor")
	public String modifyProfessor(Model model, Principal principal) {
		String loginID = principal.getName();

		return professorService.modifyProfessorPage(loginID, model);
	}

	// 교수 정보 수정
	@PostMapping("/modifyProfessor.do")
	public String doModifyProfessor(HttpServletRequest request, Principal principal) {
		// Controller에서 Request 파라미터 추출 (웹 계층 처리)
		String loginID = principal.getName();
		String phoneNum = request.getParameter(constant.getUserPhoneNum());
		String professorRoom = request.getParameter("ProfessorRoom");
		String professorRoomNum = request.getParameter("ProfessorRoomNum");
		boolean isPhoneNumOpen = request.getParameter(constant.getUserPhone()) != null;
		
		// Service에 순수 데이터만 전달 (비즈니스 로직)
		professorService.updateProfessorInfo(loginID, phoneNum, professorRoom, 
											 professorRoomNum, isPhoneNumOpen);
		
		return constant.getRModifyProfessor();
	}

}
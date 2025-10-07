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

import com.mju.groupware.constant.ConstantAdminProfessorController;
import com.mju.groupware.dto.Professor;
import com.mju.groupware.dto.User;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.service.UserService;

@Controller
@RequiredArgsConstructor
public class ProfessorController {
	private final UserService userService;
	private final ProfessorService professorService;
	private final ConstantAdminProfessorController Constant;

	@RequestMapping(value = "/signupProfessor", method = RequestMethod.GET)
	public String signupProfessor() {
		return this.Constant.getRSignupProfessor();
	}

	/* 교수 마이페이지 */
	@RequestMapping(value = "/myPageProfessor", method = RequestMethod.GET)
	public String myPageProfessor(User user, Model model, HttpServletRequest requestm, Principal Principal) {

		String LoginID = Principal.getName();// 로그인 한 아이디
		ArrayList<String> selectUserProfileInfo = userService.selectUserProfileInfo(LoginID);

		user.setUserLoginID(LoginID);
		ArrayList<String> professorInfo = professorService.selectProfessorProfileInfo(selectUserProfileInfo.get(1));

		// 교수 이름
		model.addAttribute("UserName", selectUserProfileInfo.get(0));
		// 교수 소속
		model.addAttribute("Colleges", professorInfo.get(0));
		// 교수 전공
		model.addAttribute("UserMajor", professorInfo.get(1));
		// 교수실
		model.addAttribute("ProfessorRoom", professorInfo.get(2));

		// user role 가져오기
		String UserLoginID = Principal.getName();
		ArrayList<String> info = userService.selectUserProfileInfo(UserLoginID);
		model.addAttribute("UserRole", info.get(2));

		// -------------------------------------------------------

		String UserID = Principal.getName();
		ArrayList<String> selectUserInfo = userService.SelectMyPageUserInfo(UserID);
		String selectOpenInfo = userService.SelectOpenInfo(UserID);
		// jsp화면 설정
		// 아이디 0
		model.addAttribute(this.Constant.getUserLoginID(), selectUserInfo.get(0));
		// 이름 1
		model.addAttribute(this.Constant.getUserName(), selectUserInfo.get(1));
		// 교수실 전화번호 7
		model.addAttribute("ProfessorRoomNum", selectUserInfo.get(12));
		// 연락처 2
		model.addAttribute(this.Constant.getUserPhoneNum(), selectUserInfo.get(2));
		// 단과대학 9
		model.addAttribute("ProfessorColleges", selectUserInfo.get(9));
		// 전공 10
		model.addAttribute("ProfessorMajor", selectUserInfo.get(10));
		// 교수실 11
		model.addAttribute("ProfessorRoom", selectUserInfo.get(11));
		// 이메일 3
		int idx = selectUserInfo.get(3).indexOf("@"); // 메일에서 @의 인덱스 번호를 찾음
		String email = selectUserInfo.get(3).substring(0, idx);
		model.addAttribute(this.Constant.getUserEmail(), email);

		// 정보공개여부
		if (!selectOpenInfo.equals("Error")) {
			model.addAttribute("UserInfoOpen", selectOpenInfo);
		}
		return this.Constant.getRMyPageProfessor();

	}

	/* 교수 정보 수정 화면 */
	@RequestMapping(value = "/modifyProfessor", method = RequestMethod.GET)
	public String modifyProfessor(Model model, Principal principal) {
		String loginID = principal.getName();

		User selectUserProfileInfo = userService.SelectModifyUserInfo(loginID);
		Professor professor = professorService.SelectModifyProfessorInfo(selectUserProfileInfo.getUserID());

		model.addAttribute("UserLoginID", selectUserProfileInfo.getUserLoginID());
		model.addAttribute("UserName", selectUserProfileInfo.getUserName());
		String userEmail = selectUserProfileInfo.getUserEmail();
		int location = userEmail.indexOf("@");
		userEmail = userEmail.substring(0, location);
		model.addAttribute(this.Constant.getEmail(), userEmail);
		model.addAttribute(this.Constant.getUserPhoneNum(), professor.getUserPhoneNum());
		model.addAttribute("ProfessorColleges", professor.getProfessorColleges());
		model.addAttribute("ProfessorMajor", professor.getProfessorMajor());
		// 연락처 공개
        model.addAttribute("OpenPhoneNum", selectUserProfileInfo.getOpenPhoneNum());

		return this.Constant.getRModifyProfessor();
	}

	// 교수 정보 수정
	@RequestMapping(value = "/modifyProfessor.do", method = RequestMethod.POST)
	public String DoModifyProfessor(HttpServletResponse response, HttpServletRequest request, Model model,
									Professor professor, User user, Principal Principal) {
		// 업데이트문 where절을 위한 데이터 get
		String userID = Principal.getName();
		ArrayList<String> userInfo = userService.SelectUserInformation(userID);
		userInfo.get(0); // 유저아이디 get
		userInfo.get(1); // 로그인아이디 get
		user.setUserLoginID(userInfo.get(1));

		professor.setUserID(Integer.parseInt(userInfo.get(0)));

		// 연락처
		if (!((String) request.getParameter(this.Constant.getUserPhoneNum())).equals("")) {
			user.setUserPhoneNum((String) request.getParameter(this.Constant.getUserPhoneNum()));
			userService.updateUserPhoneNumber(user);
		}
		// 교수실
		if (!((String) request.getParameter("ProfessorRoom")).equals("")) {
			professor.setProfessorRoom((String) request.getParameter("ProfessorRoom"));
			professorService.UpdateProfessorRoom(professor);
		}
		// 교수실 전화번호
		if (!((String) request.getParameter("ProfessorRoomNum")).equals("")) {
			professor.setProfessorRoomNum((String) request.getParameter("ProfessorRoomNum"));
			professorService.UpdateProfessorRoomNum(professor);
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
		return this.Constant.getRModifyProfessor();
	}

}
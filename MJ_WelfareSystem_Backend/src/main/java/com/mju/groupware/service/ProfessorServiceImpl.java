package com.mju.groupware.service;

import com.mju.groupware.constant.ConstantAdminProfessorController;
import com.mju.groupware.dao.ProfessorDao;
import com.mju.groupware.dto.Professor;
import com.mju.groupware.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {

	private final ProfessorDao professorDao;
	private final UserService userService;
	private final ConstantAdminProfessorController constant;

	@Override
	public void insertInformation(Professor professor) {
		professorDao.InsertInformation(professor);
	}

	@Override
	public void updateUserID(Professor professor) {
		professorDao.UpdateUserID(professor);
	}

	@Override
	public void updateProfessorColleges(Professor professor) {
		professorDao.UpdateProfessorColleges(professor);
	}

	@Override
	public void updateProfessorMajor(Professor professor) {
		professorDao.UpdateProfessorMajor(professor);
	}

	@Override
	public void updateProfessorRoom(Professor professor) {
		professorDao.UpdateProfessorRoom(professor);
	}

	@Override
	public void updateProfessorRoomNum(Professor professor) {
		professorDao.UpdateProfessorRoomNum(professor);
	}

	@Override
	public ArrayList<String> selectProfessorProfileInfo(String userID) {
		ArrayList<String> professorInfo = new ArrayList<String>();
		professorInfo = professorDao.selectProfessorProfileInfo(userID);
		return professorInfo;
	}

	@Override
	public Professor selectProfessorInfo(String userID) {
		return professorDao.SelectProfessorInfo(userID);
	}

	@Override
	public void updateProfessorLoginDate(Professor professor) {
		professorDao.UpdateProfessorLoginDate(professor);
	}

	@Override
	public Professor selectModifyProfessorInfo(int userID) {
		return professorDao.SelectModifyProfessorInfo(userID);
	}

	@Override
	public String modifyProfessorPage(String loginID, Model model) {
		User selectUserProfileInfo = userService.SelectModifyUserInfo(loginID);
		Professor professor = selectModifyProfessorInfo(selectUserProfileInfo.getUserID());

		String userEmail = selectUserProfileInfo.getUserEmail();
		int location = userEmail.indexOf("@");
		userEmail = userEmail.substring(0, location);

		model.addAttribute("UserLoginID", selectUserProfileInfo.getUserLoginID());
		model.addAttribute("UserName", selectUserProfileInfo.getUserName());
		model.addAttribute(this.constant.getEmail(), userEmail);
		model.addAttribute(this.constant.getUserPhoneNum(), professor.getUserPhoneNum());
		model.addAttribute("ProfessorColleges", professor.getProfessorColleges());
		model.addAttribute("ProfessorMajor", professor.getProfessorMajor());
		// 연락처 공개
		model.addAttribute("OpenPhoneNum", selectUserProfileInfo.getOpenPhoneNum());

		return this.constant.getRModifyProfessor();
	}

	@Override
	public void setProfessorMyPageAttributes(String loginID, Model model) {
		// 1. 사용자 기본 정보 조회
		ArrayList<String> selectUserProfileInfo = userService.selectUserProfileInfo(loginID);
		
		// 2. 교수 정보 조회 (userID를 통해)
		ArrayList<String> professorInfo = professorDao.selectProfessorProfileInfo(selectUserProfileInfo.get(1));
		
		// 3. 마이페이지 상세 정보 조회
		ArrayList<String> selectUserInfo = userService.SelectMyPageUserInfo(loginID);
		
		// 4. 공개 정보 조회
		String selectOpenInfo = userService.SelectOpenInfo(loginID);
		
		// 5. 이메일 처리 (@ 앞부분만 추출)
		String email = extractEmailUsername(selectUserInfo.get(3));
		
		// 6. Model에 모든 속성 추가
		// 사용자 기본 정보
		model.addAttribute("UserName", selectUserProfileInfo.get(0));
		model.addAttribute("UserRole", selectUserProfileInfo.get(2));
		
		// 교수 정보
		model.addAttribute("Colleges", professorInfo.get(0));
		model.addAttribute("UserMajor", professorInfo.get(1));
		model.addAttribute("ProfessorRoom", professorInfo.get(2));
		
		// 마이페이지 상세 정보
		model.addAttribute(constant.getUserLoginID(), selectUserInfo.get(0));
		model.addAttribute(constant.getUserName(), selectUserInfo.get(1));
		model.addAttribute(constant.getUserPhoneNum(), selectUserInfo.get(2));
		model.addAttribute(constant.getUserEmail(), email);
		model.addAttribute("ProfessorColleges", selectUserInfo.get(9));
		model.addAttribute("ProfessorMajor", selectUserInfo.get(10));
		model.addAttribute("ProfessorRoom", selectUserInfo.get(11));
		model.addAttribute("ProfessorRoomNum", selectUserInfo.get(12));
		
		// 공개 정보
		if (selectOpenInfo != null && !selectOpenInfo.equals("Error")) {
			model.addAttribute("UserInfoOpen", selectOpenInfo);
		}
	}

	/**
	 * 이메일에서 @ 앞부분만 추출
	 * @param email 이메일 주소
	 * @return @ 앞부분 문자열
	 */
	private String extractEmailUsername(String email) {
		if (email == null || !email.contains("@")) {
			return email;
		}
		return email.substring(0, email.indexOf("@"));
	}

	@Override
	public void updateProfessorInfo(String loginID, String phoneNum, String professorRoom,
									String professorRoomNum, boolean isPhoneNumOpen) {
		// 1. 사용자 정보 조회
		ArrayList<String> userInfo = userService.SelectUserInformation(loginID);
		String userIdStr = userInfo.get(0);  // 유저 ID
		String userLoginID = userInfo.get(1);  // 로그인 ID
		
		// 2. User 객체 생성
		User user = new User();
		user.setUserLoginID(userLoginID);
		
		// 3. 연락처 업데이트
		if (phoneNum != null && !phoneNum.isEmpty()) {
			user.setUserPhoneNum(phoneNum);
			userService.updateUserPhoneNumber(user);
		}
		
		// 4. 교수 정보 업데이트 (교수실 또는 교수실 전화번호가 있을 때만)
		if ((professorRoom != null && !professorRoom.isEmpty()) || 
			(professorRoomNum != null && !professorRoomNum.isEmpty())) {
			
			Professor professor = new Professor();
			professor.setUserID(Integer.parseInt(userIdStr));
			
			// 교수실 업데이트
			if (professorRoom != null && !professorRoom.isEmpty()) {
				professor.setProfessorRoom(professorRoom);
				professorDao.UpdateProfessorRoom(professor);
			}
			
			// 교수실 전화번호 업데이트
			if (professorRoomNum != null && !professorRoomNum.isEmpty()) {
				professor.setProfessorRoomNum(professorRoomNum);
				professorDao.UpdateProfessorRoomNum(professor);
			}
		}
		
		// 5. 정보공개여부 업데이트
		String openPhoneNum = isPhoneNumOpen ? "전화번호" : "비공개";
		user.setOpenPhoneNum(openPhoneNum);
		userService.UpdateOpenPhoneNum(user);
	}
}

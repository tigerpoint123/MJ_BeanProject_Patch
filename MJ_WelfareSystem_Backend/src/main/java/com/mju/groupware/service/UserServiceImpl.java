package com.mju.groupware.service;

import com.mju.groupware.dao.BoardDao;
import com.mju.groupware.dao.InquiryDao;
import com.mju.groupware.dao.ProfessorDao;
import com.mju.groupware.dao.StudentDao;
import com.mju.groupware.dao.UserDao;
import com.mju.groupware.dto.Board;
import com.mju.groupware.dto.Inquiry;
import com.mju.groupware.dto.Professor;
import com.mju.groupware.dto.Student;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserInfoOpen;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserDao userDao;
	private final StudentDao studentDao;
	private final ProfessorDao professorDao;
	private final BoardDao boardDao;
	private final InquiryDao inquiryDao;

	@Override
	public String selectForShowPassword(User user) {// 임시 비밀번호 생성
		boolean Checker = userDao.selectForShowPassword(user);
		Random RandomNum = new Random();
		String Result = "";
		if (Checker) {
			Result = Integer.toString(RandomNum.nextInt(8) + 1);
			for (int i = 0; i < 7; i++) {
				Result += Integer.toString(RandomNum.nextInt(9));
			}
		} else {
			Result = "false";
		}
		return Result;
	}

	@Override
	public ArrayList<String> selectUserProfileInfo(String id) {
		return userDao.selectUserProfileInfo(id);
	}

	@Override
	public void updateUserPhoneNumber(User user) {
		userDao.updateUserPhoneNumber(user);
	}

	@Override
	public ArrayList<String> selectUserInformation(String userId) {
		ArrayList<String> UserInfo = new ArrayList<String>();
		UserInfo = userDao.selectUserInformation(userId);
		return UserInfo;
	}

	@Override
	public boolean selectForPwdCheckBeforeModify(String id, String pw) {
		return userDao.selectForPwdCheckBeforeModify(id, pw);
	}

	@Override
	public void updateDoWithdrawalRecoveryByAdmin(String ajaxMsg) {
		userDao.updateDoWithdrawalRecoveryByAdmin(ajaxMsg);
	}

	@Override
	public ArrayList<String> selectMyPageUserInfo(String userId) {
		ArrayList<String> Info = new ArrayList<>();
		ArrayList<String> UserInfo = userDao.selectMyPageUserInfo(userId);
		ArrayList<String> StudentInfo = studentDao.SelectMyPageUserInfo(UserInfo.get(0));
		ArrayList<String> ProfessorInfo = professorDao.SelectMyPageUserInfo(UserInfo.get(0));
		UserInfo.remove(0);

		for (int i = 0; i < UserInfo.size(); i++) {
			Info.add(UserInfo.get(i));
		}
		for (int i = 0; i < StudentInfo.size(); i++) {
			Info.add(StudentInfo.get(i));
		}
		for (int i = 0; i < ProfessorInfo.size(); i++) {
			Info.add(ProfessorInfo.get(i));
		}
		return Info;
	}

	@Override
	public ArrayList<String> selectUserProfileInfoByID(String mysqlID) {
		ArrayList<String> Info = new ArrayList<>();
		ArrayList<String> UserInfo = userDao.selectMyPageUserInfoByID(mysqlID);
		ArrayList<String> StudentInfo = studentDao.SelectMyPageUserInfoByID(mysqlID);
		ArrayList<String> ProfessorInfo = professorDao.SelectMyPageUserInfoByID(mysqlID);

		for (int i = 0; i < UserInfo.size(); i++) {
			Info.add(UserInfo.get(i));
		}
		for (int i = 0; i < StudentInfo.size(); i++) {
			Info.add(StudentInfo.get(i));
		}
		for (int i = 0; i < ProfessorInfo.size(); i++) {
			Info.add(ProfessorInfo.get(i));
		}
		return Info;
	}

	@Override
	public void updateUserName(User user) {
		userDao.updateUserName(user);
	}

	@Override
	public void updateOpenPhoneNum(User user) {
		userDao.updatePhoneNum(user);
	}

	@Override
	public void updateOpenGrade(User user) {
		userDao.updateOpenGrade(user);
	}

	@Override
	public String selectOpenInfo(String userID) {
		List<UserInfoOpen> SelectOpenInfo = userDao.selectOpenInfo(userID);
		String result = "Error";

		result = SelectOpenInfo.get(0).getOpenGrade() + "," + SelectOpenInfo.get(0).getOpenPhoneNum();

		if (result.contains(",비공개") || result.contains("비공개")) {
			result = result.replaceAll(",비공개", "");
			result = result.replaceAll("비공개", "");
			boolean startComma = result.startsWith(",");
			boolean endComma = result.endsWith(",");
			if (startComma || endComma) {
				result = result.substring(result.length() - (result.length() - 1), result.length());
			}
		}

		return result;

	}

	@Override
	public int selectUserIDFromBoardController(String userLoginID) {
		return userDao.selectUserIDFromBoardController(userLoginID);
	}

	@Override
	public String selectUserName(String userLoginID) {
		return userDao.selectUserName(userLoginID);
	}

	@Override
	public String selectWriter(String userLoginID) {
        return userDao.selectWriter(userLoginID);
	}

	@Override
	public String selectTWriterID(String tWriter) {
		return userDao.selectTWriterID(tWriter);
	}

	@Override
	public String selectEmailForInquiry(String userLoginID) {
        return userDao.selectEmailForInquiry(userLoginID);
	}

	@Override
	public String selectPhoneNumForInquiry(String userLoginID) {
        return userDao.selectPhoneNumForInquiry(userLoginID);
	}

	@Override
	public String selectIDForReview(String userLoginID) {
		return userDao.selectIDForReview(userLoginID);
	}

	@Override
	public User selectModifyUserInfo(String loginID) {
		return userDao.selectModifyUserInfo(loginID);
	}

	@Override
	public void processLoginDateUpdate(String loginID, String dateFormat) {
		// 휴먼 계정 체크 및 복구
		boolean dormantCheck = userDao.selectDormant(loginID);
		if (dormantCheck) {
			userDao.updateRecoveryDormant(loginID);
		}

		// 현재 날짜 생성
		Date now = new Date();
		SimpleDateFormat date = new SimpleDateFormat(dateFormat);
		String formattedDate = date.format(now);

		// UserID 조회
		int userID = Integer.parseInt(userDao.selectUserIDForDate(loginID));

		// User 로그인 날짜 업데이트
		User user = new User();
		user.setUserLoginID(loginID);
		user.setDate(formattedDate);
		userDao.updateLoginDate(user);

		// Student 로그인 날짜 업데이트
		Student student = new Student();
		student.setDate(formattedDate);
		student.setUserID(userID);
		studentDao.UpdateStudentLoginDate(student);

		// Professor 로그인 날짜 업데이트
		Professor professor = new Professor();
		professor.setDate(formattedDate);
		professor.setUserID(userID);
		professorDao.UpdateProfessorLoginDate(professor);
	}

	@Override
	public List<Board> getNoticeBoardList() {
		return boardDao.selectNoticeBoardList();
	}

	@Override
	public List<Board> getCommunityBoardList() {
		return boardDao.selectCommunityBoardList();
	}

	@Override
	public List<Board> getMyBoardList(String loginID) {
		String userID = userDao.selectUserIDForMyBoard(loginID);
		return boardDao.selectMyBoardList(userID);
	}

	@Override
	public List<Inquiry> getMyInquiryList(String loginID) {
		String userID = userDao.selectUserIDForMyBoard(loginID);
		return inquiryDao.SelectMyInquiryList(userID);
	}

	@Override
	public void processWithdrawal(String userLoginID, String dateFormat) {
		// user 정보 조회
		User userInfo = userDao.selectUserInfo(userLoginID);
		
		// 탈퇴한 날짜 생성
		Date now = new Date();
		SimpleDateFormat date = new SimpleDateFormat(dateFormat);
		
		// 탈퇴 처리
		User user = new User();
		user.setUserLoginID(userInfo.getUserLoginID());
		user.setDate(date.format(now));
		userDao.updateWithdrawalUser(user);
	}

	@Override
	public void registerStudent(User user, Student student, String studentColleges, String studentMajor, 
	                            String studentDoubleMajor, String userEmail, boolean isDoubleMajor) {
		// 비밀번호 암호화
		String hashedPw = org.springframework.security.crypto.bcrypt.BCrypt.hashpw(user.getUserLoginPwd(), 
			org.springframework.security.crypto.bcrypt.BCrypt.gensalt());
		user.setUserLoginPwd(hashedPw);
		user.setUserEmail(userEmail);
		
		// User 테이블에 저장
		userDao.insertForSignUp(user);
		
		// UserID 조회 및 설정
		int userID = userDao.selectUserID(student);
		user.setUserID(userID);
		
		// Student 정보 설정
		student.setStudentColleges(studentColleges);
		student.setStudentMajor(studentMajor);
		student.setUserID(userID);
		
		// 부전공 설정
		if (!isDoubleMajor) {
			student.setStudentDoubleMajor("없음");
		} else {
			student.setStudentDoubleMajor(studentDoubleMajor);
		}
		
		// Student 테이블에 저장
		studentDao.InsertInformation(student);
	}

	@Override
	public void registerProfessor(User user, Professor professor, String professorColleges, String professorMajor,
	                              String professorRoom, String professorRoomNum, String userEmail) {
		// 비밀번호 암호화
		String hashedPw = org.springframework.security.crypto.bcrypt.BCrypt.hashpw(user.getUserLoginPwd(), 
			org.springframework.security.crypto.bcrypt.BCrypt.gensalt());
		user.setUserLoginPwd(hashedPw);
		user.setUserEmail(userEmail);
		
		// User 테이블에 저장
		userDao.insertForSignUp(user);
		
		// UserID 조회 및 설정
		int userID = userDao.selectUserID(professor);
		user.setUserID(userID);
		
		// Professor 정보 설정
		professor.setProfessorColleges(professorColleges);
		professor.setProfessorMajor(professorMajor);
		professor.setProfessorRoom(professorRoom);
		professor.setProfessorRoomNum(professorRoomNum);
		professor.setUserID(userID);
		
		// Professor 테이블에 저장
		professorDao.InsertInformation(professor);
	}

	@Override
	public String validateUserLoginID(String userLoginID, User user) {
		if (userLoginID == null || userLoginID.isEmpty()) {
			return "계정을 입력하지 않으셨습니다. 입력해주세요";
		}
		
		if (userLoginID.length() != 8) {
			return "학번/교번(8자리)을 입력해주세요.";
		}
		
		user.setUserLoginID(userLoginID);
		boolean checker = userDao.selectForIDConfirm(user);
		
		if (checker) {
			return "이미 등록된 계정 입니다.";
		}
		
		return "등록 가능한 계정 입니다.";
	}

	@Override
	public String validateStudentSignUp(String studentColleges, String studentMajor) {
		if (studentColleges == null || studentColleges.isEmpty()) {
			return "단과대학을 입력해주세요.";
		}
		
		if ("-선택-".equals(studentMajor)) {
			return "전공을 입력해주세요.";
		}
		
		return null; // 검증 통과
	}

	@Override
	public String validateProfessorSignUp(String professorColleges, String professorMajor) {
		if (professorColleges == null || professorColleges.isEmpty()) {
			return "단과대학을 입력해주세요.";
		}
		
		if ("-선택-".equals(professorMajor)) {
			return "전공을 입력해주세요.";
		}
		
		return null; // 검증 통과
	}

	@Override
	public boolean verifyUserForPasswordReset(User user) {
		return userDao.selectPwdForConfirmToFindPwd(user);
	}

	@Override
	public String generateAndUpdateTemporaryPassword(User user) {
		// 임시 비밀번호 생성
		String newPwd = selectForShowPassword(user);
		
		// 비밀번호 암호화
		String hashedPw = org.springframework.security.crypto.bcrypt.BCrypt.hashpw(newPwd, 
			org.springframework.security.crypto.bcrypt.BCrypt.gensalt());
		user.setUserLoginPwd(hashedPw);
		
		// 임시 비밀번호 업데이트
		userDao.updateTemporaryPwd(user);
		
		return newPwd; // 암호화되지 않은 임시 비밀번호 반환 (화면에 표시용)
	}

	@Override
	public String getRedirectUrlByRole(String userLoginID, String sRole, String pRole, 
	                                   String rmsUrl, String rmpUrl) {
		String mysqlRole = userDao.selectUserRole(userLoginID);
		
		if (mysqlRole.equals(sRole)) {
			return rmsUrl;
		}
		
		if (mysqlRole.equals(pRole)) {
			return rmpUrl;
		}
		
		return "/";
	}

	@Override
	public void updatePassword(String userLoginID, String newPassword) {
		// 비밀번호 암호화
		String hashedPwd = org.springframework.security.crypto.bcrypt.BCrypt.hashpw(newPassword, 
			org.springframework.security.crypto.bcrypt.BCrypt.gensalt());
		
		// 현재 비밀번호 조회
		String userLoginPwd = userDao.selectCurrentPwd(userLoginID);
		
		// User 객체 생성 및 업데이트
		User user = new User();
		user.setUserModifiedPW(hashedPwd);
		user.setUserLoginPwd(userLoginPwd);
		userDao.updatePwd(user);
	}
}
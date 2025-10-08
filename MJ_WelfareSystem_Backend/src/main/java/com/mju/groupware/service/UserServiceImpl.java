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
	public void InsertForSignUp(User user) {
		userDao.InsertForSignUp(user);
	}

	@Override
	public boolean SelctForIDConfirm(User user) {
		boolean Checker = userDao.SelctForIDConfirm(user);
		return Checker;
	}

	@Override
	public boolean SelectPwdForConfirmToFindPwd(User user) {
		boolean PwdChecker = userDao.SelectPwdForConfirmToFindPwd(user);
		return PwdChecker;
	}

	@Override
	public String SelectForShowPassword(User user) {// 임시 비밀번호 생성
		boolean Checker = userDao.SelectForShowPassword(user);
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
	public int SelectUserID(Student student) {
		return userDao.SelectUserID(student);
	}

	@Override
	public int SelectUserID(Professor professor) {
		return userDao.SelectUserID(professor);
	}

	@Override
	public void UpdateLoginDate(User user) {
		userDao.UpdateLoginDate(user);
	}

	@Override
	public String SelectCurrentPwd(String id) {
		return userDao.SelectCurrentPwd(id);
	}

	@Override
	public void UpdatePwd(User user) {
		userDao.UpdatePwd(user);
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
	public void UpdateUserMajor(User user) {
		userDao.updateUserMajor(user);
	}

	@Override
	public void UpdateUserColleges(User user) {
		userDao.updateUserColleges(user);
	}

	@Override
	public ArrayList<String> SelectUserInformation(String userId) {
		ArrayList<String> UserInfo = new ArrayList<String>();
		UserInfo = userDao.SelectUserInformation(userId);
		return UserInfo;
	}

	@Override
	public boolean SelectForPwdCheckBeforeModify(String id, String pw) {
		return userDao.SelectForPwdCheckBeforeModify(id, pw);
	}

	@Override
	public void UpdateTemporaryPwd(User user) {
		userDao.UpdateTemporaryPwd(user);

	}

	@Override
	public void UpdateDoWithdrawalRecoveryByAdmin(String ajaxMsg) {
		userDao.UpdateDoWithdrawalRecoveryByAdmin(ajaxMsg);
	}

	@Override
	public void UpdateDormantOneToZero(String id) {
		userDao.UpdateDormantOneToZero(id);
	}

	@Override
	public void UpdateUserRole(String id, String optionValue) {
		userDao.UpdateUserRole(id, optionValue);
	}

	@Override
	public void UpdateAdminRole(String id, String optionValue) {
		userDao.UpdateAdminRole(id, optionValue);
	}

	@Override
	public ArrayList<String> SelectMyPageUserInfo(String userId) {
		ArrayList<String> Info = new ArrayList<>();
		ArrayList<String> UserInfo = userDao.SelectMyPageUserInfo(userId);
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
	public ArrayList<String> SelectUserProfileInfoByID(String mysqlID) {
		ArrayList<String> Info = new ArrayList<>();
		ArrayList<String> UserInfo = userDao.SelectMyPageUserInfoByID(mysqlID);
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
	public void UpdateUserName(User user) {
		userDao.UpdateUserName(user);
	}

	@Override
	public void UpdateOpenPhoneNum(User user) {
		userDao.UpdatePhoneNum(user);
	}

	@Override
	public void UpdateOpenGrade(User user) {
		userDao.UpdateOpenGrade(user);
	}

	@Override
	public User SelectUserInfo(String userLoginID) {
		return userDao.SelectUserInfo(userLoginID);
	}

	@Override
	public String SelectOpenInfo(String userID) {
		List<UserInfoOpen> SelectOpenInfo = userDao.SelectOpenInfo(userID);
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
	public int SelectUserIDFromBoardController(String userLoginID) {
		return userDao.SelectUserIDFromBoardController(userLoginID);
	}

	@Override
	public String SelectUserRole(String userLoginID) {
		return userDao.SelectUserRole(userLoginID);
	}

	@Override
	public String SelectUserName(String userLoginID) {
		return userDao.SelectUserName(userLoginID);
	}

	@Override
	public void UpdateWithdrawal(User user) {
		userDao.UpdateWithdrawalUser(user);
	}

	@Override
	public void UpdateRecoveryWithdrawal(User user) {
		userDao.UpdateRecoveryWithdrawal(user);
	}

	@Override
	public void UpdateWithdrawalByDormant(String ajxMsg) {
		userDao.UpdateWithdrawalByDormant(ajxMsg);
	}

	@Override
	public boolean SelectDormant(String loginID) {
		boolean DormantCheck = userDao.SelectDormant(loginID);
		return DormantCheck;
	}

	@Override
	public void UpdateRecoveryDormant(String loginID) {
		userDao.UpdateRecoveryDormant(loginID);
	}

	@Override
	public String SelectWriter(String userLoginID) {
		String Output = userDao.SelectWriter(userLoginID);
		return Output;
	}

	@Override
	public String SelectUserIDForDocument(String userLoginID) {
		String Output = userDao.SelectUserIDForDocument(userLoginID);
		return Output;
	}

	@Override
	public String SelectTWriterID(String tWriter) {
		return userDao.SelectTWriterID(tWriter);
	}

	@Override
	public String SelectUserIDForMyBoard(String loginID) {
		return userDao.SelectUserIDForMyBoard(loginID);
	}

	@Override
	public String SelectEmailForInquiry(String userLoginID) {
		String EmailForInquiry = userDao.SelectEmailForInquiry(userLoginID);
		return EmailForInquiry;
	}

	@Override
	public String SelectPhoneNumForInquiry(String userLoginID) {
		String PhoneNumForInquiry = userDao.SelectPhoneNumForInquiry(userLoginID);
		return PhoneNumForInquiry;
	}

	@Override
	public String SelectUserIDForDate(String loginID) {
		return userDao.SelectUserIDForDate(loginID);
	}

	@Override
	public String SelectIDForReview(String userLoginID) {
		return userDao.SelectIDForReview(userLoginID);
	}

	@Override
	public User SelectModifyUserInfo(String loginID) {
		return userDao.SelectModifyUserInfo(loginID);
	}

	@Override
	public void processLoginDateUpdate(String loginID, String dateFormat) {
		// 휴먼 계정 체크 및 복구
		boolean dormantCheck = userDao.SelectDormant(loginID);
		if (dormantCheck) {
			userDao.UpdateRecoveryDormant(loginID);
		}

		// 현재 날짜 생성
		Date now = new Date();
		SimpleDateFormat date = new SimpleDateFormat(dateFormat);
		String formattedDate = date.format(now);

		// UserID 조회
		int userID = Integer.parseInt(userDao.SelectUserIDForDate(loginID));

		// User 로그인 날짜 업데이트
		User user = new User();
		user.setUserLoginID(loginID);
		user.setDate(formattedDate);
		userDao.UpdateLoginDate(user);

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
		return boardDao.SelectNoticeBoardList();
	}

	@Override
	public List<Board> getCommunityBoardList() {
		return boardDao.SelectCommunityBoardList();
	}

	@Override
	public List<Board> getMyBoardList(String loginID) {
		String userID = userDao.SelectUserIDForMyBoard(loginID);
		return boardDao.SelectMyBoardList(userID);
	}

	@Override
	public List<Inquiry> getMyInquiryList(String loginID) {
		String userID = userDao.SelectUserIDForMyBoard(loginID);
		return inquiryDao.SelectMyInquiryList(userID);
	}

	@Override
	public void processWithdrawal(String userLoginID, String dateFormat) {
		// user 정보 조회
		User userInfo = userDao.SelectUserInfo(userLoginID);
		
		// 탈퇴한 날짜 생성
		Date now = new Date();
		SimpleDateFormat date = new SimpleDateFormat(dateFormat);
		
		// 탈퇴 처리
		User user = new User();
		user.setUserLoginID(userInfo.getUserLoginID());
		user.setDate(date.format(now));
		userDao.UpdateWithdrawalUser(user);
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
		userDao.InsertForSignUp(user);
		
		// UserID 조회 및 설정
		int userID = userDao.SelectUserID(student);
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
		userDao.InsertForSignUp(user);
		
		// UserID 조회 및 설정
		int userID = userDao.SelectUserID(professor);
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
		boolean checker = userDao.SelctForIDConfirm(user);
		
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
		return userDao.SelectPwdForConfirmToFindPwd(user);
	}

	@Override
	public String generateAndUpdateTemporaryPassword(User user) {
		// 임시 비밀번호 생성
		String newPwd = SelectForShowPassword(user);
		
		// 비밀번호 암호화
		String hashedPw = org.springframework.security.crypto.bcrypt.BCrypt.hashpw(newPwd, 
			org.springframework.security.crypto.bcrypt.BCrypt.gensalt());
		user.setUserLoginPwd(hashedPw);
		
		// 임시 비밀번호 업데이트
		userDao.UpdateTemporaryPwd(user);
		
		return newPwd; // 암호화되지 않은 임시 비밀번호 반환 (화면에 표시용)
	}

	@Override
	public String getRedirectUrlByRole(String userLoginID, String sRole, String pRole, 
	                                   String rmsUrl, String rmpUrl) {
		String mysqlRole = userDao.SelectUserRole(userLoginID);
		
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
		String userLoginPwd = userDao.SelectCurrentPwd(userLoginID);
		
		// User 객체 생성 및 업데이트
		User user = new User();
		user.setUserModifiedPW(hashedPwd);
		user.setUserLoginPwd(userLoginPwd);
		userDao.UpdatePwd(user);
	}
}
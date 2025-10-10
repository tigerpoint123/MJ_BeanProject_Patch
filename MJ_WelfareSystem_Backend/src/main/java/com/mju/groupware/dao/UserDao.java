package com.mju.groupware.dao;

import com.mju.groupware.dto.Professor;
import com.mju.groupware.dto.Student;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserInfoOpen;
import global.security.UsersDetails;

import java.util.ArrayList;
import java.util.List;

public interface UserDao {

	// 회원가입
	void insertForSignUp(User user);

	// 로그인
	UsersDetails selectByLoginID(String userLoginID);

	// 중복 확인
	boolean selectForIDConfirm(User user);

	// 비번 찾기
	boolean selectPwdForConfirmToFindPwd(User user);

	// userID(다른 테이블들의 foreign key) 찾기
	int selectUserID(Student student);
	int selectUserID(Professor professor);

	// 비번 보여주기
	boolean selectForShowPassword(User user);

	boolean selectForEmailDuplicateCheck(User user);

	void updateLoginDate(User user);

	String selectCurrentPwd(String id);

	void updatePwd(User user);

	boolean selectForPwdCheckBeforeModify(String pw, String pw2);

	ArrayList<String> selectMyPageUserInfo(String userId);

	ArrayList<String> selectUserProfileInfo(String id);

	void updateUserPhoneNumber(User user);

	void updateUserMajor(User user);

	void updateUserColleges(User user);

	ArrayList<String> selectUserInformation(String userId);

	void updateTemporaryPwd(User user);

	void updateDoWithdrawalRecoveryByAdmin(String ajaxMsg);

	void updateDormantOneToZero(String id);

	void updateUserRole(String id, String optionValue);

	void updateAdminRole(String id, String optionValue);

	ArrayList<String> selectMyPageUserInfoByID(String mysqlID);

	void updateUserName(User user);

	void updatePhoneNum(User user);

	void updateOpenGrade(User user);

	User selectUserInfo(String userLoginID);

	List<UserInfoOpen> selectOpenInfo(String userID);

	int selectUserIDFromBoardController(String userLoginID);

	String selectUserRole(String userLoginID);

	String selectUserName(String userLoginID);

	void updateWithdrawalUser(User user);

	void updateRecoveryWithdrawal(User user);

	void updateWithdrawalByDormant(String ajxMsg);

	boolean selectDormant(String loginID);

	void updateRecoveryDormant(String loginID);

	String selectWriter(String userLoginID);

	String selectUserIDForDocument(String userLoginID);

	String selectTWriterID(String tWriter);

	String selectUserIDForMyBoard(String loginID);

	String selectEmailForInquiry(String userLoginID);

	String selectPhoneNumForInquiry(String userLoginID);

	String selectUserIDForDate(String loginID);

	String selectIDForReview(String userLoginID);

	User selectModifyUserInfo(String loginID);

}

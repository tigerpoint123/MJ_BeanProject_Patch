package com.mju.groupware.service;

import java.util.ArrayList;

import com.mju.groupware.dto.Professor;
import com.mju.groupware.dto.Student;
import com.mju.groupware.dto.User;

public interface UserService {

    // 회원가입
    void InsertForSignUp(User user);

    // 중복확인
    boolean SelctForIDConfirm(User user);

    // 비번 찾기
    boolean SelectPwdForConfirmToFindPwd(User user);

    // userID(다른 테이블들의 foreign key) 찾기
    int SelectUserID(Student student);

    int SelectUserID(Professor professor);

    String SelectForShowPassword(User user);

    void UpdateLoginDate(User user);

    String SelectCurrentPwd(String id);

    void UpdatePwd(User user);

    boolean SelectForPwdCheckBeforeModify(String pw, String pw2);

    ArrayList<String> SelectMyPageUserInfo(String userId);

    ArrayList<String> selectUserProfileInfo(String loginID);

    void updateUserPhoneNumber(User user);

    void UpdateUserMajor(User user);

    void UpdateUserColleges(User user);

    ArrayList<String> SelectUserInformation(String userId);

    void UpdateTemporaryPwd(User user);

    void UpdateDoWithdrawalRecoveryByAdmin(String ajaxMsg);

    void UpdateDormantOneToZero(String id);

    void UpdateAdminRole(String string, String optionValue);

    void UpdateUserRole(String string, String optionValue);

    ArrayList<String> SelectUserProfileInfoByID(String mysqlID);

    void UpdateUserName(User user);

    void UpdateOpenPhoneNum(User user);

    void UpdateOpenGrade(User user);

    User SelectUserInfo(String userLoginID);

    String SelectOpenInfo(String userID);

    int SelectUserIDFromBoardController(String userLoginID);

    String SelectUserRole(String userLoginID);

    String SelectUserName(String userLoginID);

    void UpdateWithdrawal(User user);

    void UpdateRecoveryWithdrawal(User user);

    void UpdateWithdrawalByDormant(String string);

    boolean SelectDormant(String loginID);

    void UpdateRecoveryDormant(String loginID);

    String SelectWriter(String userLoginID);

    String SelectUserIDForDocument(String userLoginID);

    String SelectTWriterID(String tWriter);

    String SelectUserIDForMyBoard(String loginID);

    String SelectEmailForInquiry(String userLoginID);

    String SelectPhoneNumForInquiry(String userLoginID);

    String SelectUserIDForDate(String loginID);

    String SelectIDForReview(String userLoginID);

    User SelectModifyUserInfo(String loginID);

}
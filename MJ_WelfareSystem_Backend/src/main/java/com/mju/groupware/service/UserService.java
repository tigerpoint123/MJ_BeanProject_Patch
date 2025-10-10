package com.mju.groupware.service;

import com.mju.groupware.dto.Board;
import com.mju.groupware.dto.Professor;
import com.mju.groupware.dto.Student;
import com.mju.groupware.dto.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {

    String selectForShowPassword(User user);

    boolean selectForPwdCheckBeforeModify(String pw, String pw2);

    ArrayList<String> selectMyPageUserInfo(String userId);

    ArrayList<String> selectUserProfileInfo(String loginID);

    void updateUserPhoneNumber(User user);

    ArrayList<String> selectUserInformation(String userId);

    void updateDoWithdrawalRecoveryByAdmin(String ajaxMsg);

    ArrayList<String> selectUserProfileInfoByID(String mysqlID);

    void updateUserName(User user);

    void updateOpenPhoneNum(User user);

    void updateOpenGrade(User user);

    String selectOpenInfo(String userID);

    int selectUserIDFromBoardController(String userLoginID);

    String selectUserName(String userLoginID);

    String selectWriter(String userLoginID);

    String selectTWriterID(String tWriter);

    String selectEmailForInquiry(String userLoginID);

    String selectPhoneNumForInquiry(String userLoginID);

    String selectIDForReview(String userLoginID);

    User selectModifyUserInfo(String loginID);

    void processLoginDateUpdate(String loginID, String dateFormat);

    List<Board> getNoticeBoardList();

    List<com.mju.groupware.dto.Board> getCommunityBoardList();

    List<Board> getMyBoardList(String loginID);

    List<com.mju.groupware.dto.Inquiry> getMyInquiryList(String loginID);

    void processWithdrawal(String userLoginID, String dateFormat);

    void registerStudent(User user, Student student, String studentColleges, String studentMajor, 
                         String studentDoubleMajor, String userEmail, boolean isDoubleMajor);

    void registerProfessor(User user, Professor professor, String professorColleges, String professorMajor,
                          String professorRoom, String professorRoomNum, String userEmail);

    String validateUserLoginID(String userLoginID, User user);

    String validateStudentSignUp(String studentColleges, String studentMajor);

    String validateProfessorSignUp(String professorColleges, String professorMajor);

    boolean verifyUserForPasswordReset(User user);

    String generateAndUpdateTemporaryPassword(User user);

    String getRedirectUrlByRole(String userLoginID, String sRole, String pRole, 
                                String rmsUrl, String rmpUrl);

    void updatePassword(String userLoginID, String newPassword);

}
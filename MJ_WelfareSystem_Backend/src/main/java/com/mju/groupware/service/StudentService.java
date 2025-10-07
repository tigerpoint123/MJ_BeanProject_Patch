package com.mju.groupware.service;

import com.mju.groupware.dto.Student;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;

public interface StudentService {

    // 정보 저장
    void InsertInformation(Student student);

    // 회원가입 후 userID(foreign key) 업데이트
    void UpdateUserID(Student student);

    // 성별 update
    void updateStudentGender(Student student);

    // 학년 update
    void updateStudentGrade(Student student);

    // 부전공
    void UpdateStudentDobuleMajor(Student student);

    // 로그인 완료 화면에 띄울 데이터 select
    ArrayList<String> selectStudentProfileInfo(String userID);

    void UpdateStudentGender(Student student);

    void UpdateStudentColleges(Student student);

    void UpdateStudentMajor(Student student);

    Student SelectStudentInfo(String userID);

    void InsertWithdrawalStudent(Student student);

    void DeleteWithdrawalStudent(Student student);

    void DeleteWithdrawalStudentList(String string);

    void UpdateStudentLoginDate(Student student);

    Student SelectModifyStudentInfo(int userID);

    /**
     * 학생 마이페이지 데이터 조회
     * @param loginID 로그인 ID
     * @return 마이페이지 데이터 (HashMap)
     */
    HashMap<String, Object> getMyPageStudent(String loginID);

    /**
     * 학생 정보 수정 화면 데이터 조회
     * @param loginID 로그인 ID
     * @return 수정 화면 데이터 (HashMap)
     */
    HashMap<String, Object> getModifyStudent(String loginID);

    /**
     * 학생 마이페이지 데이터를 Model에 설정
     * @param loginID 로그인 ID
     * @param model Spring MVC Model
     * @param gradeKey 학년 키
     * @param userLoginIDKey 로그인 ID 키
     * @param userNameKey 이름 키
     * @param userPhoneNumKey 전화번호 키
     * @param userEmailKey 이메일 키
     */
    void setMyPageStudentAttributes(String loginID, Model model, String gradeKey, 
                                     String userLoginIDKey, String userNameKey, 
                                     String userPhoneNumKey, String userEmailKey);

    /**
     * 학생 정보 수정 화면 데이터를 Model에 설정
     * @param loginID 로그인 ID
     * @param model Spring MVC Model
     * @param emailKey 이메일 키
     */
    void setModifyStudentAttributes(String loginID, Model model, String emailKey);

    /**
     * 학생 정보 수정 처리
     * @param loginID 로그인 ID
     * @param phoneNum 연락처
     * @param studentGrade 학년
     * @param isPhoneOpen 전화번호 공개 여부
     * @param isGradeOpen 학년 공개 여부
     */
    void updateStudentInfo(String loginID, String phoneNum, String studentGrade, 
                          boolean isPhoneOpen, boolean isGradeOpen);
}

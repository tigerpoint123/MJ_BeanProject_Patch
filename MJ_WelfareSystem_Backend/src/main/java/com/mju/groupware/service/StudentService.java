package com.mju.groupware.service;

import java.util.ArrayList;

import com.mju.groupware.dto.Student;

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

}

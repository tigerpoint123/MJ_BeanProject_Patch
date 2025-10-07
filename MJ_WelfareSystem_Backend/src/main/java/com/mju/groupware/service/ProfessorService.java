package com.mju.groupware.service;

import java.util.ArrayList;

import com.mju.groupware.dto.Professor;

public interface ProfessorService {

    // 정보 저장
    void InsertInformation(Professor professor);

    // 회원가입 후 userID(foreign key) 업데이트
    void UpdateUserID(Professor professor);

    //교수 학과 update
    void UpdateProfessorColleges(Professor professor);

    //교수 전공 update
    void UpdateProfessorMajor(Professor professor);

    //교수실 update
    void UpdateProfessorRoom(Professor professor);

    //교수실 전화번호 update
    void UpdateProfessorRoomNum(Professor professor);

    // 로그인 완료 화면에 띄울 데이터 select
    ArrayList<String> selectProfessorProfileInfo(String userID);

    Professor SelectProfessorInfo(String userID);

    void InsertWithdrawalProfessor(Professor professor);

    void DeleteWithdrawalProfessor(Professor professor);

    void DeleteWithdrawalProfessorList(String string);

    void UpdateProfessorLoginDate(Professor professor);

    Professor SelectModifyProfessorInfo(int userID);

}

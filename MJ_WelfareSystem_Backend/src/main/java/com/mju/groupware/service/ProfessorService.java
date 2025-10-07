package com.mju.groupware.service;

import com.mju.groupware.dto.Professor;
import org.springframework.ui.Model;

import java.util.ArrayList;

public interface ProfessorService {

    // 정보 저장
    void insertInformation(Professor professor);

    // 회원가입 후 userID(foreign key) 업데이트
    void updateUserID(Professor professor);

    //교수 학과 update
    void updateProfessorColleges(Professor professor);

    //교수 전공 update
    void updateProfessorMajor(Professor professor);

    //교수실 update
    void updateProfessorRoom(Professor professor);

    //교수실 전화번호 update
    void updateProfessorRoomNum(Professor professor);

    // 로그인 완료 화면에 띄울 데이터 select
    ArrayList<String> selectProfessorProfileInfo(String userID);

    Professor selectProfessorInfo(String userID);

    void updateProfessorLoginDate(Professor professor);

    Professor selectModifyProfessorInfo(int userID);

    String modifyProfessorPage(String loginID, Model model);

    /**
     * 교수 마이페이지 정보를 Model에 설정
     * @param loginID 로그인 ID
     * @param model Spring MVC Model
     */
    void setProfessorMyPageAttributes(String loginID, Model model);

    /**
     * 교수 정보 수정 처리
     * @param loginID 로그인 ID
     * @param phoneNum 연락처
     * @param professorRoom 교수실
     * @param professorRoomNum 교수실 전화번호
     * @param isPhoneNumOpen 전화번호 공개 여부
     */
    void updateProfessorInfo(String loginID, String phoneNum, String professorRoom,
                            String professorRoomNum, boolean isPhoneNumOpen);
}

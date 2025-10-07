package com.mju.groupware.service;

import java.util.HashMap;
import java.util.List;

import com.mju.groupware.dto.Professor;
import com.mju.groupware.dto.SearchKeyWord;
import com.mju.groupware.dto.Student;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserReview;

public interface SearchService {

    List<User> SelectKeyWord(SearchKeyWord searchKeyWord);

    Student SelectStudentInfo(int i);

    Professor selectProfessorInfo(int userID);

    List<UserReview> SelectUserReview(String userID);

    /**
     * 검색된 사용자 목록을 HashMap 형태로 변환
     * @param searchKeyWord 검색 키워드
     * @param studentRole 학생 역할 코드
     * @param professorRole 교수 역할 코드
     * @param nameKey 이름 키
     * @param emailKey 이메일 키
     * @param phoneKey 전화번호 키
     * @return 사용자 정보 맵 리스트
     */
    List<HashMap<String, Object>> searchUserInfoList(SearchKeyWord searchKeyWord, 
                                                       String studentRole, 
                                                       String professorRole,
                                                       String nameKey,
                                                       String emailKey,
                                                       String phoneKey);

    /**
     * 사용자 이메일로 리뷰 목록 조회
     * @param userEmail 사용자 이메일
     * @return 리뷰 목록 (없으면 null)
     */
    List<UserReview> getUserReviewListByEmail(String userEmail);

}

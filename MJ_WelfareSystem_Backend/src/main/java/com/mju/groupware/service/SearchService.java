package com.mju.groupware.service;

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

}

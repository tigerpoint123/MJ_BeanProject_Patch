package com.mju.groupware.service;

import java.util.List;

import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserEmail;

public interface EmailService {
    // 이메일 보내기
    int sendEmail(User user);

    // 인증번호확인
    boolean AuthNum(String authNum);

    // 이메일중복확인
    boolean SelectForEmailDuplicateCheck(User user);

    List<UserEmail> PrintEmailList();

    boolean CheckEmailLogin(String id, String pw);

    List<UserEmail> GetEmailList();

    String sendEmailForPasswordReset(String userEmail);

    UserEmail getEmailContentByIndex(int index);
}

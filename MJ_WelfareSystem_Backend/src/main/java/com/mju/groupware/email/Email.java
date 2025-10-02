package com.mju.groupware.email;

import java.util.List;

import com.mju.groupware.dto.UserEmail;

public interface Email {

    void sendEmail(String email, int Num);

    boolean AuthNum(int authnum, int num);

    List<UserEmail> printEmailList();

    boolean CheckEmailLogin(String id, String pw);

    List<UserEmail> GetEmailList();
}

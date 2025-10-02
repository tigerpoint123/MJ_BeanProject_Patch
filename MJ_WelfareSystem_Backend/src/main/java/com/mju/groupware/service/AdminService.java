package com.mju.groupware.service;

import java.util.List;

import com.mju.groupware.dto.UserList;

public interface AdminService {

    List<UserList> SelectUserlist() throws Exception;

    List<UserList> SelectDormantUserList();

    List<UserList> SelectWithdrawalUserList();

}

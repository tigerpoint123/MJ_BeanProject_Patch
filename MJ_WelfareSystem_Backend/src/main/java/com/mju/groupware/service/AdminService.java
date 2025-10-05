package com.mju.groupware.service;

import com.mju.groupware.dto.UserList;

import java.util.List;
import java.util.Map;

public interface AdminService {

    List<UserList> SelectUserlist() throws Exception;

    List<UserList> SelectDormantUserList();

    List<UserList> SelectWithdrawalUserList();

    Map<String, Object> detailStudent();
}

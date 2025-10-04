package com.mju.groupware.dao;

import java.util.List;

import com.mju.groupware.dto.UserList;

public interface UserListDao {

	List<UserList> SelectUserlist() throws Exception;

	List<UserList> SelectDormantUserList();

	List<UserList> SelectWithdrawalUserList();

}

package com.mju.groupware.dao;

import com.mju.groupware.dto.UserList;

import java.util.List;

public interface UserListDao {

	List<UserList> SelectUserlist();

	List<UserList> SelectDormantUserList();

	List<UserList> SelectWithdrawalUserList();

}

package com.mju.groupware.service;

import com.mju.groupware.dao.UserListDao;
import com.mju.groupware.dto.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private UserListDao userListDao;

	@Override
	public List<UserList> SelectUserlist() throws Exception {
		return userListDao.SelectUserlist();
	}

	@Override
	public List<UserList> SelectDormantUserList() {
		return userListDao.SelectDormantUserList();
	}

	@Override
	public List<UserList> SelectWithdrawalUserList() {
		return userListDao.SelectWithdrawalUserList();
	}

	@Override
	public Map<String, Object> detailStudent() {


		return Map.of();
	}
}

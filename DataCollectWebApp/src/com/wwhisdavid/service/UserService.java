package com.wwhisdavid.service;

import com.wwhisdavid.entity.UserEntity;
import com.wwhisdavid.exception.UserExistsException;

public interface UserService {
	/*
	 * 登陆
	 */
	UserEntity login(UserEntity user);
	
	/*
	 * 注册
	 */
	void register(UserEntity user) throws UserExistsException;
}

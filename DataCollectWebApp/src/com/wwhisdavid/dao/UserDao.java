package com.wwhisdavid.dao;

import com.wwhisdavid.entity.UserEntity;

public interface UserDao {
	/*
	 * 注册
	 */
	public void register(UserEntity user);
	
	/*
	 * 根据用户密码查询
	 */
	public UserEntity findByNameAndPwd(UserEntity user);
	
	/*
	 * 检查用户名是否存在
	 */
	public boolean userExists(String name);
}

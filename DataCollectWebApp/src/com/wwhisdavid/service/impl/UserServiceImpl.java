package com.wwhisdavid.service.impl;

import com.wwhisdavid.dao.UserDao;
import com.wwhisdavid.dao.impl.UserDaoImpl;
import com.wwhisdavid.entity.UserEntity;
import com.wwhisdavid.exception.UserExistsException;
import com.wwhisdavid.service.UserService;

public class UserServiceImpl implements UserService {

	private UserDao userDao = new UserDaoImpl();
	@Override
	public UserEntity login(UserEntity user) {
		try {
			return userDao.findByNameAndPwd(user);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void register(UserEntity user) throws UserExistsException {
		
			// 给调用者提示
			try {
				boolean flag = userDao.userExists(user.getUsername());
				if (flag) {
				throw new UserExistsException("用户名已存在，注册失败！");
				}
			} catch (UserExistsException e) {
				// TODO Auto-generated catch block
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		userDao.register(user);
	}
}

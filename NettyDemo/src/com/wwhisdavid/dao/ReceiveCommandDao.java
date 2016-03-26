package com.wwhisdavid.dao;

import com.wwhisdavid.exception.MessageErrorException;

public interface ReceiveCommandDao {
	/*
	 * 校验并插入数据到mysql(boolean)
	 */
	public void insert2mysql(String msg) throws MessageErrorException;
}

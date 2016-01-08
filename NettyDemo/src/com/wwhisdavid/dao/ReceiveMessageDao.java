package com.wwhisdavid.dao;

import com.wwhisdavid.exception.MessageErrorException;

public interface ReceiveMessageDao {
	/*
	 * 转发一条指令信息
	 */
	public void dispatch(String msg);
	
	/*
	 * 校验并插入数据到mysql(boolean)
	 */
	public void insert2mysql(String msg) throws MessageErrorException;
}

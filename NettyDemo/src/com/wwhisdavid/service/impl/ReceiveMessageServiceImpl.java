package com.wwhisdavid.service.impl;

import com.wwhisdavid.dao.ReceiveMessageDao;
import com.wwhisdavid.dao.impl.ReceiveANodeMessageDaoImpl;
import com.wwhisdavid.exception.MessageErrorException;
import com.wwhisdavid.service.ReceiveMessageService;

public class ReceiveMessageServiceImpl implements ReceiveMessageService {
	private ReceiveMessageDao dao;
	@Override
	public void dispatch(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert2mysql(String msg) throws MessageErrorException {
		
		// 做校验和分配具体dao
		String[] msgs = msg.split("#"); 
		// 分为俩部分，前部分为token，用于校验数据以及对应的表。
		String token = msgs[0];
		if ("1D0B4FAFED5F4E4E4612ECD54E3386E7".equals(token)) { // 反射优化 配置文件
			dao = new ReceiveANodeMessageDaoImpl();
		}
		else return;
		
		// 第二部分,解析参数
		String param = msgs[1];
		dao.insert2mysql(param);
	}

}

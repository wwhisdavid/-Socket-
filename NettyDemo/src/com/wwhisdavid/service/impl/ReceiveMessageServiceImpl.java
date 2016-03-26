package com.wwhisdavid.service.impl;

import com.wwhisdavid.dao.ReceiveCommandDao;
import com.wwhisdavid.dao.ReceiveMessageDao;
import com.wwhisdavid.dao.impl.ReceiveANodeMessageDaoImpl;
import com.wwhisdavid.dao.impl.ReceiveCommandDaoImpl;
import com.wwhisdavid.exception.MessageErrorException;
import com.wwhisdavid.service.ReceiveMessageService;

public class ReceiveMessageServiceImpl implements ReceiveMessageService {
	private ReceiveMessageDao dao;
	private ReceiveCommandDao commandDao;
	@Override
	public void dispatch(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert2mysql(String msg) throws MessageErrorException {
		// 1DCCADFED7BCBB036C56A4AFB97E906F -- command
		// 1D0B4FAFED5F4E4E4612ECD54E3386E7 -- ???
		// 做校验和分配具体dao
		String[] msgs = msg.split("#"); 
		// 分为俩部分，前部分为token，用于校验数据以及对应的表。
		String token = msgs[0];
		if ("1D0B4FAFED5F4E4E4612ECD54E3386E7".equals(token)) { // 反射优化 配置文件
			dao = new ReceiveANodeMessageDaoImpl();
			// 第二部分,解析参数
			String param = msgs[1];
			dao.insert2mysql(param);
		}else if("1DCCADFED7BCBB036C56A4AFB97E906F".equals(token)){
			commandDao = new ReceiveCommandDaoImpl();
			String param2 = msgs[1];
			commandDao.insert2mysql(param2);
		}
		else return;
	}
}

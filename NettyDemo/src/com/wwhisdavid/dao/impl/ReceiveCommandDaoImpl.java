package com.wwhisdavid.dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

import com.wwhisdavid.dao.ReceiveCommandDao;
import com.wwhisdavid.exception.MessageErrorException;
import com.wwhisdavid.util.JdbcUtil;

public class ReceiveCommandDaoImpl implements ReceiveCommandDao{

	@Override
	public void insert2mysql(String msg) throws MessageErrorException {
		String sql = "INSERT INTO node_command(node_id,command_time,username,command) VALUES (?,?,?,?)";
		try {
		QueryRunner queryRunner = JdbcUtil.getRunner();
		String[] params = msg.split("&");
		if (params.length == 4) {
			System.out.println(params[0]+params[1]+params[2]+params[3]);
			switch (params[3]) {
			case "1h":
				params[3] = "1";
				break;
			case "1d":
				params[3] = "2";
				break;
			case "1w":
				params[3] = "3";
				break;
			case "stop":
				params[3] = "-1";
				break;
			}
			queryRunner.update(sql, params[0], params[1], params[2], params[3]);
		}else{
			System.out.println("参数有误，插入失败。");
			throw new MessageErrorException("参数有误，插入失败。");
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

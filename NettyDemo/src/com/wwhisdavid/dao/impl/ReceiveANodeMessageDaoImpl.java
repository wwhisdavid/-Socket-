package com.wwhisdavid.dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.wwhisdavid.dao.ReceiveMessageDao;
import com.wwhisdavid.exception.MessageErrorException;
import com.wwhisdavid.util.JdbcUtil;
/*
 * 收到ANODE类型节点的信息的处理
 */
public class ReceiveANodeMessageDaoImpl implements ReceiveMessageDao {
	@Override
	public void dispatch(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert2mysql(String msg) throws MessageErrorException {
		String sql = "INSERT INTO a_node_detail(node_id,record_time,temperature,humidity,stress_x,stress_y,stress_z) VALUES (?,?,?,?,?,?,?)";
		try {
		QueryRunner queryRunner = JdbcUtil.getRunner();
		String[] params = msg.split("&");
		if (params.length == 7) {
			System.out.println(params[0]+params[1]+params[2]+params[3]+params[4]+params[5]+params[6]);
			queryRunner.update(sql, params[0], params[1], params[2], params[3], params[4], params[5], params[6]);
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

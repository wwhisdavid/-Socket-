package com.wwhisdavid.dao;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.wwhisdavid.util.JdbcUtil;

public class BaseDao {
	private Connection connection;
	private PreparedStatement pstmt;
	public void update(String sql, Object[] parameters){
		try {
			connection = JdbcUtil.getConnection();
			pstmt = connection.prepareStatement(sql);
			
			ParameterMetaData data = pstmt.getParameterMetaData();
			if (parameters != null && parameters.length > 0) {
				for (int i = 0; i < data.getParameterCount(); i++) {
					pstmt.setObject(i+1, parameters[i]);
				}
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcUtil.close(connection, pstmt);
		}	
	}
	
	
}

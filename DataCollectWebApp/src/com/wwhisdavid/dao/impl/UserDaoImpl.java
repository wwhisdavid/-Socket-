package com.wwhisdavid.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wwhisdavid.dao.UserDao;
import com.wwhisdavid.entity.UserEntity;
import com.wwhisdavid.util.JdbcUtil;

public class UserDaoImpl implements UserDao {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	@Override
	public void register(UserEntity user) {
		String sql = "insert into user(username,password) values(?,?)";

		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getPassword());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn, pstmt);
		}
	}

	public UserEntity findByNameAndPwd(UserEntity user) {
		String sql = "SELECT * FROM user where username=? and password=?";
		UserEntity userEntity = null;
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getPassword());

			rs = pstmt.executeQuery();

			if (rs.next()) {
				userEntity = new UserEntity();
				userEntity.setId(rs.getInt("id"));
				userEntity.setUsername(rs.getString("username"));
				userEntity.setPassword(rs.getString("password"));
			}
			return userEntity;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn, pstmt);
		}
	}

	@Override
	public boolean userExists(String name) {
		String sql = "select id from user where username=?";
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				int id = rs.getInt("id");
				if (id > 0) {
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn, pstmt);
		}
	}

}
